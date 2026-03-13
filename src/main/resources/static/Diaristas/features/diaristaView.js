import diaristaViewManager from './diaristaViewManager.js';
import setupServiceBlocks from './serviceBlocks.js';

let tableObserver;

function initDiaristaView() {
    setupServiceBlocks();
    diaristaViewManager.checkForDiaristaView(document);

    function setupTableObserver() {
        if (tableObserver) tableObserver.disconnect();

        tableObserver = new MutationObserver(function (mutations) {
            const table = document.getElementById('tdc');
            if (table) setupTableEvents(table);

            setupStatusButtons();

            mutations.forEach(mutation => {
                mutation.addedNodes.forEach(node => {
                    if (node.nodeType === 1) {
                        diaristaViewManager.checkForDiaristaView(node);
                        if (node.querySelector && node.querySelector('.status-option')) {
                            setupStatusButtons();
                        }
                    }
                });
            });
        });

        tableObserver.observe(document.body, { childList: true, subtree: true });
    }

    function setupTableEvents(table) {
        if (!table) table = document.getElementById('tdc');
        if (!table) return;

        table.removeEventListener('click', handleTableClick);
        table.addEventListener('click', handleTableClick);
    }

    function handleTableClick(e) {
        const row = e.target.closest('.tdc-body');
        if (!row) return;

        // Clique na linha já selecionada: limpa o form e desmarca
        if (row.classList.contains('selected')) {
            row.classList.remove('selected');
            clearForm();
            return;
        }

        toggleRowSelection(row);
        fillFormFromRow(row);
    }

    function toggleRowSelection(selectedRow) {
        document.querySelectorAll('.tdc-body.selected').forEach(r => r.classList.remove('selected'));
        selectedRow.classList.add('selected');
    }

    function fillFormFromRow(row) {
        const cells = row.cells;
        const form = document.querySelector('#formWrapper form');
        if (!form) return;

        const fieldId     = form.querySelector('#diaristaId');
        const fieldNome   = form.querySelector('#diaristaNome');
        const fieldOper   = form.querySelector('#NomeOperadorSpan');
        const fieldCpf    = form.querySelector('#diaristaCpf');

        if (fieldId && cells[0])
            fieldId.value = cells[0].textContent.trim();

        if (fieldNome && cells[1])
            fieldNome.value = cells[1].textContent.trim();

        if (fieldOper && cells[1])
            fieldOper.textContent = cells[1].textContent.trim();

        if (fieldCpf && cells[2])
            fieldCpf.value = cells[2].textContent.trim();
    }

    function clearForm() {
        const form = document.querySelector('#formWrapper form');
        if (!form) return;

        const fieldId     = form.querySelector('#diaristaId');
        const fieldNome   = form.querySelector('#diaristaNome');
        const fieldOper   = form.querySelector('#NomeOperadorSpan');
        const fieldCpf    = form.querySelector('#diaristaCpf');

        if (fieldId) fieldId.value = '';
        if (fieldNome) fieldNome.value = '';
        if (fieldOper) fieldOper.textContent = '[Não informado]';
        if (fieldCpf) fieldCpf.value = '';
    }

    // Inicialização
    setupTableObserver();
    setupStatusButtons();

    const table = document.getElementById('tdc');
    if (table) setupTableEvents(table);
}

export default initDiaristaView;