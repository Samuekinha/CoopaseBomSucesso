document.addEventListener('DOMContentLoaded', function() {
    // Configura o clique na linha divisória
    const clearViewTrigger = document.getElementById('clear-view-trigger');
    if (clearViewTrigger) {
        clearViewTrigger.addEventListener('click', function() {
            const contentContainer = document.getElementById('dynamic-content');

            if (contentContainer) {
                // Limpa o conteúdo sem passar pelo controller
                contentContainer.innerHTML = '';

                // Remove a seleção de todos os blocos
                document.querySelectorAll('.bloco-link.selected').forEach(b => {
                    b.classList.remove('selected');
                    const bloco = b.querySelector('.bloco');
                    if (bloco) {
                        bloco.style.transform = 'scale(0.98)';
                    }
                });

                // Opcional: Mostra uma mensagem ou estado vazio
                contentContainer.innerHTML = '<p class="text-muted">Nenhuma view selecionada</p>';
            }
        });
    }

    // Inicializa a view de edição se não houver sistema de views dinâmicas
    initClientEditView();
});

function setupServiceBlocks() {
    const blocosLinks = document.querySelectorAll('.bloco-link');
    const closeAllBtn = document.getElementById('clear-view-trigger');

    // Função para resetar todos os estados
    function resetAllStates() {
        // Remove seleção de todos os blocos
        document.querySelectorAll('.bloco-link.selected').forEach(b => {
            b.classList.remove('selected');
            const bloco = b.querySelector('.bloco');
            if (bloco) {
                bloco.style.transform = 'scale(0.98)';
            }
        });

        // Limpa o conteúdo dinâmico
        const contentContainer = document.getElementById('dynamic-content');
        if (contentContainer) {
            contentContainer.innerHTML = '<p>Nenhuma view selecionada</p>';
        }
    }

    // Inicializa a página no estado correto (limpo)
    resetAllStates();

    blocosLinks.forEach(link => {
        link.addEventListener('click', function(e) {
            e.preventDefault();

            // Remove a seleção de todos os blocos
            document.querySelectorAll('.bloco-link.selected').forEach(b => {
                b.classList.remove('selected');
                const bloco = b.querySelector('.bloco');
                if (bloco) {
                    bloco.style.transform = 'scale(0.98)';
                }
            });

            // Adiciona a seleção no bloco clicado
            this.classList.add('selected');
            const selectedBloco = this.querySelector('.bloco');
            if (selectedBloco) {
                selectedBloco.style.transform = 'scale(1) translateY(-3px)';
            }

            // Carrega a view correspondente
            const action = this.getAttribute('data-action');
            if (action && typeof loadView === 'function') {
                loadView(action);
            } else {
                console.log(`Ação selecionada: ${action}`);
            }
        });
    });

    // Manipula o clique na divisória para limpar seleções
    if (closeAllBtn) {
        closeAllBtn.addEventListener('click', function() {
            resetAllStates();
        });
    }

    // Expõe a função para uso externo se necessário
    window.resetServiceBlocks = resetAllStates;
}

// Variável para controlar o observador
let tableObserver;

function initClientEditView() {
    // coloração de blocos de serviço quando selecionados
    setupServiceBlocks();

    // 1. Controle do campo cooperado - PARA O FORMULÁRIO DE CADASTRO
    function handleCooperadoCadastro() {
        const cooperadoSelect = document.getElementById('cooperadoSelect');
        const camposCooperado = document.getElementById('camposCooperado');

        if (cooperadoSelect && camposCooperado) {
            cooperadoSelect.addEventListener('change', function() {
                camposCooperado.classList.toggle('hidden-cooperado', this.value !== 'true');
            });
            // Dispara o evento inicialmente
            cooperadoSelect.dispatchEvent(new Event('change'));
        }
    }

    // 2. Controle do campo cooperado - PARA O FORMULÁRIO DE EDIÇÃO (existente)
    function handleCooperadoChange(event) {
        const camposCooperado = document.getElementById('camposCooperado');
        if (event && event.target && event.target.id === 'cooperadoSelect' && camposCooperado) {
            camposCooperado.classList.toggle('hidden-cooperado', event.target.value !== 'true');
        }
    }

    // Configura os event listeners
    handleCooperadoCadastro(); // Para o formulário de cadastro
    document.addEventListener('change', handleCooperadoChange); // Para o formulário de edição

    // 2. Observador de mutação para detectar a tabela dinamicamente
    function setupTableObserver() {
        // Se já existe um observador, desconecta antes de criar um novo
        if (tableObserver) {
            tableObserver.disconnect();
        }

        tableObserver = new MutationObserver(function(mutations) {
            const table = document.getElementById('listaDeClientesSelecinavel');
            if (table) {
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
            return;
        }

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
            const clientIdField = form.querySelector('[name="ClientId"]');
            const clientNameField = form.querySelector('[name="ClientName"]');
            const clientDocumentField = form.querySelector('[name="ClientDocument"]');
            const clientBirthField = form.querySelector('[name="ClientBirth"]');

            if (clientIdField) clientIdField.value = cells[0].textContent.trim();
            if (clientNameField) clientNameField.value = cells[1].textContent.trim();
            if (clientDocumentField) clientDocumentField.value = cells[2].textContent.trim();

            // Converte e preenche datas
            if (clientBirthField) {
                const birthDateParts = cells[3].textContent.trim().split('-');
                if (birthDateParts.length === 3) {
                    clientBirthField.value = `${birthDateParts[2]}-${birthDateParts[1]}-${birthDateParts[0]}`;
                }
            }

            // Preenche cooperado - MODIFICAÇÃO PRINCIPAL AQUI
            const cooperadoValue = cells[4].textContent.trim().toLowerCase() === 'true' ? 'true' : 'false';
            const cooperadoSelect = form.querySelector('[name="cooperadoSelect"]');
            if (cooperadoSelect) {
                cooperadoSelect.value = cooperadoValue;
            }

            // Mostra/oculta os campos cooperado imediatamente
            const camposCooperado = wrapper.querySelector('#camposCooperado');
            if (camposCooperado) {
                camposCooperado.classList.toggle('hidden-cooperado', cooperadoValue !== 'true');
            }

            // Preenche campos adicionais se for cooperado
            if (cooperadoValue === 'true') {
                const clientCafDateField = form.querySelector('[name="ClientCafDate"]');
                const clientCafCodeField = form.querySelector('[name="ClientCafCode"]');

                if (clientCafDateField && cells[5]) {
                    const cafDateParts = cells[5].textContent.trim().split('-');
                    if (cafDateParts.length === 3) {
                        clientCafDateField.value = `${cafDateParts[2]}-${cafDateParts[1]}-${cafDateParts[0]}`;
                    }
                }
                if (clientCafCodeField && cells[6]) {
                    clientCafCodeField.value = cells[6].textContent.trim();
                }
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