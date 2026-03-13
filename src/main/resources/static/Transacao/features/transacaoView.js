import transactionViewManager from './transactionViewManager.js';
import { formatarDataParaInput } from './utils.js';
import setupServiceBlocks from './serviceBlocks.js';

let tableObserver;

function initTransactionView() {
    setupServiceBlocks();
    transactionViewManager.checkForTransactionView(document);

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
                        transactionViewManager.checkForTransactionView(node);

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

    function setupStatusButtons() {
        const statusButtons = document.querySelectorAll('.status-option');
        const titulo = document.getElementById('tituloTransacao');

        if (statusButtons.length === 0) return;

        statusButtons.forEach(button => {
            // Remove listeners anteriores para evitar duplicação
            button.removeEventListener('click', handleStatusClick);
            button.addEventListener('click', handleStatusClick);
        });

        function handleStatusClick(e) {
            const clickedButton = e.currentTarget;
            const status = clickedButton.dataset.status;

            // Remove classe active de todos os botões
            statusButtons.forEach(btn => btn.classList.remove('active'));

            // Adiciona classe active ao botão clicado
            clickedButton.classList.add('active');

            // Esconde todos os containers de tabela
            const containers = document.querySelectorAll('.table-container');
            containers.forEach(container => container.classList.remove('active'));

            // Mostra o container correspondente
            if (status === 'ATIVAS') {
                const containerAtivas = document.getElementById('container-ativas');
                if (containerAtivas) {
                    containerAtivas.classList.add('active');
                }
                if (titulo) {
                    titulo.textContent = 'Consultar Transações Ativas';
                }
            } else if (status === 'INATIVAS') {
                const containerInativas = document.getElementById('container-inativas');
                if (containerInativas) {
                    containerInativas.classList.add('active');
                }
                if (titulo) {
                    titulo.textContent = 'Consultar Transações Inativas';
                }
            } else if (status === 'TODAS') {
                const containerTodas = document.getElementById('container-todas');
                if (containerTodas) {
                    containerTodas.classList.add('active');
                }
                if (titulo) {
                    titulo.textContent = 'Consultar Todas as Transações';
                }
            }

            // Remove seleções anteriores e formulários expandidos
            document.querySelectorAll('.main-row.selected').forEach(row => {
                row.classList.remove('selected');
            });
            document.querySelectorAll('.expanded-form-row').forEach(row => row.remove());

            // Configura eventos para a tabela que acabou de ficar ativa
            setTimeout(() => {
                const activeTable = document.querySelector('.table-container.active .main-table');
                if (activeTable) {
                    setupTableEvents(activeTable);
                }
            }, 10);
            updateFormActionAndButton(status);
        }

        function updateFormActionAndButton(status) {
            const form = document.querySelector('#AlterarStatusTransacao');
            const btn = document.querySelector('#btnAlterarStatus');
            if (!form || !btn) return;

            if (status === 'ATIVAS') {
                form.action = '/Coopase/Transacao/Inativar';
                btn.textContent = 'Inativar';
                btn.classList.remove('btn-success');
                btn.classList.add('btn-warning');
            } else if (status === 'INATIVAS') {
                form.action = '/Coopase/Transacao/Reativar';
                btn.textContent = 'Ativar';
                btn.classList.remove('btn-warning');
                btn.classList.add('btn-success');
            }
        }
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
        const row = e.target.closest('.main-row');
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
        document.querySelectorAll('.main-row.selected').forEach(r => {
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

        transactionViewManager.checkForTransactionView(formClone);
    }

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

export default initTransactionView;