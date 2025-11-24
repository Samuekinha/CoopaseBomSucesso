document.addEventListener('DOMContentLoaded', function() {
    // Configura o clique na linha divisória
    const clearViewTrigger = document.getElementById('clear-view-trigger');
    if (clearViewTrigger) {
        clearViewTrigger.addEventListener('click', function() {
            const contentContainer = document.getElementById('dynamic-content');
            if (contentContainer) {
                contentContainer.innerHTML = '';
                document.querySelectorAll('.bloco-link.selected').forEach(b => {
                    b.classList.remove('selected');
                    const bloco = b.querySelector('.bloco');
                    if (bloco) {
                        bloco.style.transform = 'scale(0.98)';
                    }
                });
                contentContainer.innerHTML = '<p class="text-muted">Nenhuma view selecionada</p>';
            }
        });
    }

    // 🔔 Configuração do Toast
    const Toast = Swal.mixin({
        toast: true,
        position: "top-end",
        showConfirmButton: false,
        timer: 2500,
        timerProgressBar: true,
        didOpen: (toast) => {
            toast.onmouseenter = Swal.stopTimer;
            toast.onmouseleave = Swal.resumeTimer;
        }
    });

    // 🚨 Captura de mensagens do backend
    const flashError = document.getElementById('flash-error');
    const flashSuccess = document.getElementById('flash-success');

    if (flashError && flashError.textContent.trim()) {
        Toast.fire({
            icon: "error",
            title: flashError.textContent.trim()
        });
    }

    if (flashSuccess && flashSuccess.textContent.trim()) {
        Toast.fire({
            icon: "success",
            title: flashSuccess.textContent.trim()
        });
    }

    // Inicializa os blocos de serviço
    setupServiceBlocks();

    // Inicializa a view de conta depósito
    initContaDepositoView();
});

function setupServiceBlocks() {
    const blocosLinks = document.querySelectorAll('.bloco-link');
    const closeAllBtn = document.getElementById('clear-view-trigger');

    function resetAllStates() {
        document.querySelectorAll('.bloco-link.selected').forEach(b => {
            b.classList.remove('selected');
            const bloco = b.querySelector('.bloco');
            if (bloco) {
                bloco.style.transform = 'scale(0.98)';
            }
        });

        const contentContainer = document.getElementById('dynamic-content');
        if (contentContainer) {
            contentContainer.innerHTML = '<p>Nenhuma view selecionada</p>';
        }
    }

    resetAllStates();

    blocosLinks.forEach(link => {
        link.addEventListener('click', function(e) {
            e.preventDefault();

            document.querySelectorAll('.bloco-link.selected').forEach(b => {
                b.classList.remove('selected');
                const bloco = b.querySelector('.bloco');
                if (bloco) {
                    bloco.style.transform = 'scale(0.98)';
                }
            });

            this.classList.add('selected');
            const selectedBloco = this.querySelector('.bloco');
            if (selectedBloco) {
                selectedBloco.style.transform = 'scale(1) translateY(-3px)';
            }

            const action = this.getAttribute('data-action');
            if (action && typeof loadView === 'function') {
                loadView(action);
            }
        });
    });

    if (closeAllBtn) {
        closeAllBtn.addEventListener('click', function() {
            resetAllStates();
        });
    }

    window.resetServiceBlocks = resetAllStates;
}

function initContaDepositoView() {
    console.log('Inicializando view de conta depósito...');

    // Configura os botões de status
    setupStatusToggle();

    // Configura eventos das tabelas
    setupTableEvents();

    // Configura observador para tabelas carregadas dinamicamente
    setupTableObserver();
}

function setupStatusToggle() {
    const statusButtons = document.querySelectorAll('.status-option');
    const containerAtivas = document.getElementById('container-ativas');
    const containerInativas = document.getElementById('container-inativas');
    const containerTodas = document.getElementById('container-todas');
    const contaContainer = document.getElementById('contaContainer');

    if (!statusButtons.length) {
        console.log('Botões de status não encontrados');
        return;
    }

    console.log('Configurando', statusButtons.length, 'botões de status');

    statusButtons.forEach(button => {
        // Remove event listeners existentes
        button.replaceWith(button.cloneNode(true));
    });

    // Re-seleciona os botões após o clone
    const refreshedButtons = document.querySelectorAll('.status-option');

    refreshedButtons.forEach(button => {
        button.addEventListener('click', function() {
            const status = this.getAttribute('data-status');
            console.log('Botão clicado:', status);

            // Remove active de todos os botões
            refreshedButtons.forEach(btn => btn.classList.remove('active'));

            // Adiciona active no botão clicado
            this.classList.add('active');

            // Esconde todos os containers
            if (containerAtivas) containerAtivas.classList.remove('active');
            if (containerInativas) containerInativas.classList.remove('active');
            if (containerTodas) containerTodas.classList.remove('active');

            // Mostra o container correspondente
            if (status === 'ATIVAS' && containerAtivas) {
                containerAtivas.classList.add('active');
                console.log('Mostrando contas ativas');
            } else if (status === 'INATIVAS' && containerInativas) {
                containerInativas.classList.add('active');
                console.log('Mostrando contas inativas');
            } else if (status === 'TODAS' && containerTodas) {
                containerTodas.classList.add('active');
                console.log('Mostrando todas as contas');
            }

            // Atualiza o atributo data-mode
            if (contaContainer) {
                contaContainer.setAttribute('data-mode', status);
            }

            // Limpa seleções
            document.querySelectorAll('.expanded-form-row').forEach(r => r.remove());
            document.querySelectorAll('.main-row.selected').forEach(r => r.classList.remove('selected'));

            // Re-configura eventos para a tabela agora visível
            setTimeout(() => {
                setupTableEvents();
            }, 100);
        });
    });

    // Garante que ATIVAS está ativo por padrão
    const ativasBtn = document.querySelector('.status-option[data-status="ATIVAS"]');
    if (ativasBtn && !ativasBtn.classList.contains('active')) {
        ativasBtn.classList.add('active');
    }
}

function setupTableEvents() {
    console.log('Configurando eventos das tabelas...');

    const tables = [
        document.getElementById('listaContasAtivas'),
        document.getElementById('listaContasInativas'),
        document.getElementById('listaContasTodas')
    ];

    let tablesFound = 0;

    tables.forEach(table => {
        if (!table) return;

        tablesFound++;

        // Remove event listeners antigos
        table.removeEventListener('click', handleTableClick);

        // Adiciona novo event listener
        table.addEventListener('click', handleTableClick);

        console.log('Eventos configurados para tabela:', table.id);
    });

    console.log('Total de tabelas configuradas:', tablesFound);
}

function handleTableClick(e) {
    const row = e.target.closest('.main-row');
    if (!row) return;

    console.log('Linha clicada:', row);

    toggleRowSelection(row);
    showFormForRow(row);
    fillFormFromRow(row);
}

function toggleRowSelection(selectedRow) {
    // Remove seleção de todas as linhas em todas as tabelas
    document.querySelectorAll('.main-row.selected').forEach(r => {
        r.classList.remove('selected');
    });

    // Adiciona seleção na linha clicada
    selectedRow.classList.add('selected');
}

function showFormForRow(row) {
    // Remove formulários expandidos existentes
    document.querySelectorAll('.expanded-form-row').forEach(r => r.remove());

    const expandedRow = document.createElement('tr');
    expandedRow.className = 'expanded-form-row';

    const td = document.createElement('td');
    td.colSpan = row.cells.length;
    td.className = 'expanded-form-content';

    const formWrapper = document.getElementById('formWrapper');
    if (formWrapper) {
        const formClone = formWrapper.cloneNode(true);
        formClone.style.display = 'block';
        formClone.id = 'formWrapper-expanded';
        td.appendChild(formClone);
    }

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

        const formatarDataParaInput = (dataStr) => {
            if (!dataStr || dataStr.trim() === '') return '';
            const dataLimpa = dataStr.replace(/\D/g, '');
            if (dataLimpa.length < 8) return '';

            const dia = dataLimpa.substring(0, 2);
            const mes = dataLimpa.substring(2, 4);
            const ano = dataLimpa.substring(4, 8);

            if (dia > 31 || mes > 12) return '';
            return `${ano}-${mes}-${dia}`;
        };

        // Preenche campos do formulário
        const ContaDepositoIdField = form.querySelector('[name="ContaDepositoId"]');
        const ContaDepositoNomeField = form.querySelector('[name="ContaDepositoNome"]');
        const ContaDepositoMontanteField = form.querySelector('[name="ContaDepositoMontante"]');
        const ContaDepositoDataCriacaoField = form.querySelector('[name="ContaDepositoDataCriacao"]');

        if (ContaDepositoIdField) ContaDepositoIdField.value = cells[0].textContent.trim();
        if (ContaDepositoNomeField) ContaDepositoNomeField.value = cells[1].textContent.trim();
        if (ContaDepositoMontanteField) {
            ContaDepositoMontanteField.value = cells[2].textContent.trim();
        }
        if (ContaDepositoDataCriacaoField) {
            ContaDepositoDataCriacaoField.value = formatarDataParaInput(cells[3].textContent);
        }
    });
}

let tableObserver;

function setupTableObserver() {
    if (tableObserver) {
        tableObserver.disconnect();
    }

    tableObserver = new MutationObserver(function(mutations) {
        mutations.forEach(mutation => {
            mutation.addedNodes.forEach(node => {
                if (node.nodeType === 1) { // Element node
                    if (node.querySelector && (
                        node.querySelector('#listaContasAtivas') ||
                        node.querySelector('#listaContasInativas') ||
                        node.querySelector('#listaContasTodas')
                    )) {
                        console.log('Tabela detectada dinamicamente');
                        setTimeout(() => {
                            setupStatusToggle();
                            setupTableEvents();
                        }, 50);
                    }
                }
            });
        });
    });

    tableObserver.observe(document.body, {
        childList: true,
        subtree: true
    });
}