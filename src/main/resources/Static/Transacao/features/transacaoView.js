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
            const table = document.getElementById('listadeContasDepositoSelecinavel');
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
        const table = document.getElementById('listadeContasDepositoSelecinavel');
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

            const ContaDepositoIdField = form.querySelector('[name="ContaDepositoId"]');
            const ContaDepositoNomeField = form.querySelector('[name="ContaDepositoNome"]');
            const ContaDepositoMontanteField = form.querySelector('[name="ContaDepositoMontante"]');
            const ContaDepositoDataCriacaoField = form.querySelector('[name="ContaDepositoDataCriacao"]');

            if (ContaDepositoIdField) ContaDepositoIdField.value = cells[0].textContent.trim();
            if (ContaDepositoNomeField) ContaDepositoNomeField.value = cells[1].textContent.trim();
            if (ContaDepositoMontanteField) ContaDepositoMontanteField.value = cells[2].textContent.trim();

            if (ContaDepositoDataCriacaoField) {
                ContaDepositoDataCriacaoField.value = formatarDataParaInput(cells[3].textContent);
                console.log('Data formatada:', ContaDepositoDataCriacaoField.value);
            }
        });
    }

    setupTableObserver();

    if (document.getElementById('listadeContasDepositoSelecinavel')) {
        setupTableEvents();
    }
}

export default initTransactionView;