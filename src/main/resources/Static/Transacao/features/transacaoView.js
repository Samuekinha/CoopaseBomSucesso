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

        // Aplica classes de tipo em todas as linhas da tabela
        table.querySelectorAll('.main-row').forEach(row => {
            const tipoTransacao = row.cells[0]?.textContent.trim().toUpperCase();
            row.classList.remove('deposit', 'withdraw', 'transfer');

            if (tipoTransacao.includes("DEPOSIT")) {
                row.classList.add("deposit");
            } else if (tipoTransacao.includes("WITHDRAW")) {
                row.classList.add("withdraw");
            } else if (tipoTransacao.includes("TRANSFER")) {
                row.classList.add("transfer");
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

            if (idTransacaoField && cells[7]) {
                idTransacaoField.value = cells[7].textContent.trim(); // coloca o id da transação
            }

            if (operadorField && cells[8]) {
                operadorField.value = cells[8].textContent.trim(); // mostra o nome do operador
                operadorField.dataset.operadorId = cells[8].dataset.operadorId; // mantém id do operador para uso interno
            }


            const transferenciaField = form.querySelector('#transferencia-field');
            if (transferenciaField && tipoTransacaoField) {
                if (tipoTransacaoField.value.toLowerCase().includes('transferencia') ||
                    tipoTransacaoField.value.toLowerCase().includes('transferência')) {
                    transferenciaField.style.display = 'block';
                } else {
                    transferenciaField.style.display = 'none';
                }
            }
        });
    }

    setupTableObserver();

    if (document.getElementById('listaDeTransacaoSelecionavel')) {
        setupTableEvents();
    }
}

export default initTransactionView;