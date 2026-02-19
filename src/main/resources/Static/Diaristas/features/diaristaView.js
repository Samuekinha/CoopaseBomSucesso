import diaristaViewManager from './diaristaViewManager.js';
import { formatarDataParaInput } from './utils.js';
import setupServiceBlocks from './serviceBlocks.js';

let tableObserver;

function initDiaristaView() {
    setupServiceBlocks();
    diaristaViewManager.checkForDiaristaView(document);

    function setupTableObserver() {
        if (tableObserver) {
            tableObserver.disconnect();
        }

        tableObserver = new MutationObserver(function(mutations) {
            // Procura por todas as tabelas possíveis
            const tables = [
                document.getElementById('listaDeTransacaoSelecionavel'),
                document.getElementById('listaDeTransacaoSelecionavel-ativas'),
                document.getElementById('listaDeTransacaoSelecionavel-inativas'),
                document.getElementById('listaDeTransacaoSelecionavel-todas')
            ].filter(Boolean);

            if (tables.length > 0) {
                tables.forEach(table => setupTableEvents(table));
            }

            // Verifica os botões de status
            setupStatusButtons();

            mutations.forEach(mutation => {
                mutation.addedNodes.forEach(node => {
                    if (node.nodeType === 1) {
                        diaristaViewManager.checkForDiaristaView(node);

                        // Verifica se novos botões de status foram adicionados
                        if (node.querySelector && node.querySelector('.status-option')) {
                            setupStatusButtons();
                        }

                        // Verifica se novas tabelas foram adicionadas
                        const newTables = [
                            node.querySelector('#listaDeTransacaoSelecionavel'),
                            node.querySelector('#listaDeTransacaoSelecionavel-ativas'),
                            node.querySelector('#listaDeTransacaoSelecionavel-inativas'),
                            node.querySelector('#listaDeTransacaoSelecionavel-todas')
                        ].filter(Boolean);

                        newTables.forEach(table => setupTableEvents(table));
                    }
                });
            });
        });

        tableObserver.observe(document.body, {
            childList: true,
            subtree: true
        });
    }

    function setupTableEvents(table) {
        if (!table) {
            // Fallback para compatibilidade - procura pelo ID original
            table = document.getElementById('listaDeTransacaoSelecionavel');
        }

        const formWrapper = document.getElementById('formWrapper');

        if (!table || !formWrapper) {
            return;
        }

        // Aplica classes de tipo em todas as linhas da tabela
        table.querySelectorAll('.main-row').forEach(row => {
            const tipoTransacao = row.cells[0]?.textContent.trim().toUpperCase();
            row.classList.remove('deposito', 'saque', 'transferencia');

            if (tipoTransacao.includes("DEPOSITO")) {
                row.classList.add("deposito");
            } else if (tipoTransacao.includes("SAQUE")) {
                row.classList.add("saque");
            } else if (tipoTransacao.includes("TRANSFERENCIA")) {
                row.classList.add("transferencia");
            }
        });

        table.removeEventListener('click', handleTableClick);
        table.addEventListener('click', handleTableClick);
    }

    function handleTableClick(e) {
        // insira aqui o tr do body da tabela
        const row = e.target.closest('.tdc-body');
        if (!row) return;

        // Se a linha já está selecionada, "desmarca" e fecha o formulário
        if (row.classList.contains('selected')) {
            row.classList.remove('selected');
            document.querySelectorAll('.expanded-form-row').forEach(r => r.remove());
            return;
        }

        toggleRowSelection(row);
        showFormForRow(row);
        fillFormFromRow(row);
    }

    function toggleRowSelection(selectedRow) {
        document.querySelectorAll('.tdc-body.selected').forEach(r => {
            r.classList.remove('selected');
        });

        selectedRow.classList.add('selected');
    }

    function showFormForRow(row) {
        document.querySelectorAll('.expanded-form-row').forEach(r => r.remove());

        const expandedRow = document.createElement('tr');
        expandedRow.className = 'expanded-form-row';

        const td = document.createElement('td');
        td.colSpan = row.cells.length;
        td.className = 'expanded-form-content';

        const formClone = document.getElementById('formWrapper').cloneNode(true);
        formClone.style.display = 'block';
        formClone.id = 'formWrapper-expanded';
        td.appendChild(formClone);

        expandedRow.appendChild(td);
        row.parentNode.insertBefore(expandedRow, row.nextSibling);

        diaristaViewManager.checkForDiaristaView(formClone);
    }

    // Aqui os dados são puxados para o formulário expandido
    function fillFormFromRow(row) {
        const cells = row.cells;

        function setSelectValue(select, textOrId) {
            if (!select) return;
            select.value = textOrId;
            if (select.value !== textOrId) {
                const opt = Array.from(select.options).find(o => o.textContent.trim() === textOrId);
                if (opt) select.value = opt.value;
            }
        }

        ['formWrapper', 'formWrapper-expanded'].forEach(wrapperId => {
            const wrapper = document.getElementById(wrapperId);
            if (!wrapper) return;

            const form = wrapper.querySelector('form');
            if (!form) return;

            // enche o form das besteiras "alterar os nomes qnd copiar por padrão
            const idTransacaoField = form.querySelector('#idTransacao');
            const tipoTransacaoField = form.querySelector('#TipoTransacao');
            const valorTransacaoField = form.querySelector('#valorTransacao');
            const formaDinheiroField = form.querySelector('#formaDinheiroTransacao');
            const contaPrincipalField = form.querySelector('#contaOrigemIdReadOnly');
            const contaDestinoField = form.querySelector('#ContaDestinoIdReadOnly');
            const operadorField = form.querySelector('#operadorTransacaoIdReadOnly');
            const descricaoField = form.querySelector('#Descricao');
            const dataField = form.querySelector('#dataTransacao');

            if (tipoTransacaoField && cells[0]) {
                tipoTransacaoField.value = cells[0].textContent.trim();
            }

            if (valorTransacaoField && cells[1]) {
                valorTransacaoField.value = cells[1].textContent.trim().replace(",", ".");
            }

            if (formaDinheiroField && cells[2]) {
                setSelectValue(formaDinheiroField, cells[2].textContent.trim());
            }

            if (contaPrincipalField && cells[3]) {
                contaPrincipalField.value = cells[3].textContent.trim();
            }

            if (contaDestinoField && cells[4]) {
                contaDestinoField.value = cells[4].textContent.trim();
            }

            if (dataField && cells[5]) {
                dataField.value = formatarDataParaInput(cells[5].textContent.trim());
            }

            if (descricaoField && cells[6]) {
                descricaoField.value = cells[6].textContent.trim();
            }

            if (idTransacaoField && cells[8]) {
                idTransacaoField.value = cells[8].textContent.trim();
            }

            if (operadorField && cells[9]) {
                operadorField.value = cells[9].textContent.trim();
            }

            const transferenciaField = form.querySelector('#transferencia-field');
            if (transferenciaField && tipoTransacaoField) {
                const tipo = tipoTransacaoField.value.toLowerCase();
                if (tipo.includes('transferencia') || tipo.includes('transferência')) {
                    transferenciaField.style.display = 'block';
                } else {
                    transferenciaField.style.display = 'none';
                }
            }
        });
    }

    // Inicialização
    setupTableObserver();

    // Configura botões de status se já existirem
    setupStatusButtons();

    // Configura tabelas existentes
    const existingTables = [
        document.getElementById('listaDeTransacaoSelecionavel'),
        document.getElementById('listaDeTransacaoSelecionavel-ativas'),
        document.getElementById('listaDeTransacaoSelecionavel-inativas'),
        document.getElementById('listaDeTransacaoSelecionavel-todas')
    ].filter(Boolean);

    existingTables.forEach(table => setupTableEvents(table));
}

export default initDiaristaView;