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
            const table = document.getElementById('listaDeTransacaoSelecionavel');
            if (table) {
                setupTableEvents();
            }

            mutations.forEach(mutation => {
                mutation.addedNodes.forEach(node => {
                    if (node.nodeType === 1) {
                        transactionViewManager.checkForTransactionView(node);
                    }
                });
            });
        });

        tableObserver.observe(document.body, {
            childList: true,
            subtree: true
        });
    }

    function setupTableEvents() {
        const table = document.getElementById('listaDeTransacaoSelecionavel');
        const formWrapper = document.getElementById('formWrapper');

        if (!table || !formWrapper) {
            return;
        }

        table.removeEventListener('click', handleTableClick);
        table.addEventListener('click', handleTableClick);
    }

    function handleTableClick(e) {
        const row = e.target.closest('.main-row');
        if (!row) return;

        toggleRowSelection(row);
        showFormForRow(row);
        fillFormFromRow(row);
    }

    function toggleRowSelection(selectedRow) {
        document.querySelectorAll('.main-row.selected').forEach(r => r.classList.remove('selected'));
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

        ['formWrapper', 'formWrapper-expanded'].forEach(wrapperId => {
            const wrapper = document.getElementById(wrapperId);
            if (!wrapper) return;

            const form = wrapper.querySelector('form');
            if (!form) return;

            // Mapear os campos baseados na estrutura da tabela HTML
            // Colunas: Valor, Tipo, Conta Principal, Conta Destino, Data, Saldo Anterior, Saldo Posterior
            const valorTransacaoField = form.querySelector('[name="valorTransacao"]');
            const tipoTransacaoField = form.querySelector('[name="TipoTransacao"]');
            const contaPrincipalField = form.querySelector('[name="ContaPrincipal"]') ||
                                       form.querySelector('input[id*="ContaPrincipal"]') ||
                                       form.querySelector('select[id*="ContaPrincipal"]');
            const contaDestinoField = form.querySelector('[name="ContaDestino"]') ||
                                     form.querySelector('input[id*="ContaDestino"]') ||
                                     form.querySelector('select[id*="ContaDestino"]');

            // Preencher os campos com os dados da linha
            if (valorTransacaoField && cells[0]) {
                valorTransacaoField.value = cells[0].textContent.trim();
            }

            if (tipoTransacaoField && cells[1]) {
                const tipoTransacao = cells[1].textContent.trim();
                tipoTransacaoField.value = tipoTransacao;

                // Mostrar/ocultar campo de transferência baseado no tipo
                const transferenciaField = form.querySelector('#transferencia-field');
                if (transferenciaField) {
                    if (tipoTransacao.toLowerCase().includes('transferencia') ||
                        tipoTransacao.toLowerCase().includes('transferência')) {
                        transferenciaField.style.display = 'block';
                    } else {
                        transferenciaField.style.display = 'none';
                    }
                }
            }

            if (contaPrincipalField && cells[2]) {
                const contaPrincipal = cells[2].textContent.trim();
                contaPrincipalField.value = contaPrincipal;

                // Se for um campo de texto readonly, pode incluir o nome da conta
                const contaPrincipalNomeField = form.querySelector('[name="ContaPrincipalNome"]');
                if (contaPrincipalNomeField) {
                    contaPrincipalNomeField.value = contaPrincipal;
                }
            }

            if (contaDestinoField && cells[3]) {
                const contaDestino = cells[3].textContent.trim();
                if (contaDestino && contaDestino !== '-' && contaDestino !== '') {
                    contaDestinoField.value = contaDestino;

                    // Se for um campo de texto readonly, pode incluir o nome da conta
                    const contaDestinoNomeField = form.querySelector('[name="ContaDestinoNome"]');
                    if (contaDestinoNomeField) {
                        contaDestinoNomeField.value = contaDestino;
                    }
                }
            }

            // Campo de data (se necessário no futuro)
            if (cells[4]) {
                const dataField = form.querySelector('[name="dataTransacao"]') ||
                                form.querySelector('[name="DataTransacao"]');
                if (dataField) {
                    dataField.value = formatarDataParaInput(cells[4].textContent);
                }
            }

            // Campos adicionais que podem estar nos dados da transação
            // Saldo Anterior (cells[5]) e Saldo Posterior (cells[6]) - não utilizados no form atual

            console.log('Formulário preenchido com dados da transação:', {
                valor: cells[0]?.textContent.trim(),
                tipo: cells[1]?.textContent.trim(),
                contaPrincipal: cells[2]?.textContent.trim(),
                contaDestino: cells[3]?.textContent.trim(),
                data: cells[4]?.textContent.trim()
            });
        });
    }

    setupTableObserver();

    if (document.getElementById('listaDeTransacaoSelecionavel')) {
        setupTableEvents();
    }
}

export default initTransactionView;