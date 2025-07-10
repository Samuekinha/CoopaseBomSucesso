// Variável para controlar o observador
let tableObserver;

function initClientEditView() {
    console.log('Inicializando view de edição de cliente');

    // 1. Controle do campo cooperado
    function handleCooperadoChange(event) {
        const camposCooperado = document.getElementById('camposCooperado');
        if (event && event.target && event.target.id === 'cooperadoSelect' && camposCooperado) {
            camposCooperado.classList.toggle('hidden-cooperado', event.target.value !== 'true');
        }
    }

    // Configura o event listener para o select cooperado
    document.addEventListener('change', handleCooperadoChange);

    // 2. Observador de mutação para detectar a tabela dinamicamente
    function setupTableObserver() {
        // Se já existe um observador, desconecta antes de criar um novo
        if (tableObserver) {
            tableObserver.disconnect();
        }

        tableObserver = new MutationObserver(function(mutations) {
            const table = document.getElementById('listaDeClientesSelecinavel');
            if (table) {
                console.log('Tabela detectada via MutationObserver');
                setupTableEvents();
            }
        });

        tableObserver.observe(document.body, {
            childList: true,
            subtree: true
        });
    }

    // 3. Configura os eventos da tabela
    function setupTableEvents() {
        const table = document.getElementById('listaDeClientesSelecinavel');
        const formWrapper = document.getElementById('formWrapper');
        
        if (!table || !formWrapper) {
            console.error('Elementos essenciais não encontrados');
            return;
        }

        console.log('Configurando eventos da tabela');

        // Remove event listener antigo se existir
        table.removeEventListener('click', handleTableClick);
        // Adiciona novo event listener
        table.addEventListener('click', handleTableClick);
    }

    // 4. Handler para clicks na tabela
    function handleTableClick(e) {
        const row = e.target.closest('.main-row');
        if (!row) return;

        toggleRowSelection(row);
        showFormForRow(row);
        fillFormFromRow(row);
    }

    // 5. Funções auxiliares (mantidas iguais)
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
    }

    function fillFormFromRow(row) {
        const cells = row.cells;
        
        ['formWrapper', 'formWrapper-expanded'].forEach(wrapperId => {
            const wrapper = document.getElementById(wrapperId);
            if (!wrapper) return;

            const form = wrapper.querySelector('form');
            if (!form) return;

            // Preenche campos básicos
            form.querySelector('[name="ClientId"]').value = cells[0].textContent.trim();
            form.querySelector('[name="ClientName"]').value = cells[1].textContent.trim();
            form.querySelector('[name="ClientDocument"]').value = cells[2].textContent.trim();

            // Converte e preenche datas
            const birthDateParts = cells[3].textContent.trim().split('-');
            if (birthDateParts.length === 3) {
                form.querySelector('[name="ClientBirth"]').value = `${birthDateParts[2]}-${birthDateParts[1]}-${birthDateParts[0]}`;
            }
            
            // Preenche cooperado
            const cooperadoValue = cells[4].textContent.trim().toLowerCase() === 'true' ? 'true' : 'false';
            const cooperadoSelect = form.querySelector('[name="cooperadoSelect"]');
            cooperadoSelect.value = cooperadoValue;
            cooperadoSelect.dispatchEvent(new Event('change'));
            
            // Preenche campos adicionais se for cooperado
            if (cooperadoValue === 'true') {
                const cafDateParts = cells[5].textContent.trim().split('-');
                if (cafDateParts.length === 3) {
                    form.querySelector('[name="ClientCafDate"]').value = `${cafDateParts[2]}-${cafDateParts[1]}-${cafDateParts[0]}`;
                }
                form.querySelector('[name="ClientCafCode"]').value = cells[6].textContent.trim();
            }
        });
    }

    // Inicialização
    setupTableObserver();
    
    // Verificação inicial
    if (document.getElementById('listaDeClientesSelecinavel')) {
        setupTableEvents();
    }
    
    // Dispara evento change inicial se o select existir
    const initialSelect = document.getElementById('cooperadoSelect');
    if (initialSelect) {
        initialSelect.dispatchEvent(new Event('change'));
    }
}

// Inicializa quando o DOM estiver pronto
document.addEventListener('DOMContentLoaded', initClientEditView);

// Se você tem um sistema de views SPA, chame initClientEditView() sempre que voltar para a view de edição
// Exemplo:
/*
function navigateToView(viewName) {
    // Limpeza da view anterior...
    
    if (viewName === 'edit') {
        initClientEditView();
    }
}
*/