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

    // Inicializa a view
    initContaDepositoView();
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

function initContaDepositoView() {
    // coloração de blocos de serviço quando selecionados
    setupServiceBlocks();

//    // 1. Controle do campo cooperado - PARA O FORMULÁRIO DE CADASTRO
//    function handleCooperadoCadastro() {
//        const cooperadoSelect = document.getElementById('cooperadoSelect');
//        const camposCooperado = document.getElementById('camposCooperado');
//        console.log(`kur: ${camposCooperado}`);

//        if (cooperadoSelect && camposCooperado) {
//            cooperadoSelect.addEventListener('change', function() {
//                camposCooperado.classList.toggle('hidden-cooperado', this.value !== 'true');
//            });
//            // Dispara o evento inicialmente
//            cooperadoSelect.dispatchEvent(new Event('change'));
//        }
//    }

//    // 2. Controle do campo cooperado - PARA O FORMULÁRIO DE EDIÇÃO (existente)
//    function handleCooperadoChange(event) {
//        const camposCooperado = document.getElementById('camposCooperado');
//        if (event && event.target && event.target.id === 'cooperadoSelect' && camposCooperado) {
//            camposCooperado.classList.toggle('hidden-cooperado', event.target.value !== 'true');
//        }
//    }

//    // Configura os event listeners
//    handleCooperadoCadastro(); // Para o formulário de cadastro
//    document.addEventListener('change', handleCooperadoChange); // Para o formulário de edição

    // 2. Observador de mutação para detectar a tabela dinamicamente
    function setupTableObserver() {
        // Se já existe um observador, desconecta antes de criar um novo
        if (tableObserver) {
            tableObserver.disconnect();
        }

        tableObserver = new MutationObserver(function(mutations) {
            const table = document.getElementById('listadeContasDepositoSelecinavel');
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
        const table = document.getElementById('listadeContasDepositoSelecinavel');
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

//            // Função para limpar caracteres não numéricos
//            const limparDocumento = (doc) => doc ? doc.replace(/\D/g, '') : '';
//
//            // Nova função corrigida para datas
            const formatarDataParaInput = (dataStr) => {
                if (!dataStr || dataStr.trim() === '') return '';

                // Remove qualquer formatação existente
                const dataLimpa = dataStr.replace(/\D/g, '');

                // Se não tiver números suficientes, retorna vazio
                if (dataLimpa.length < 8) return '';

                // Formata para yyyy-MM-dd (padrão do input date)
                const dia = dataLimpa.substring(0, 2);
                const mes = dataLimpa.substring(2, 4);
                const ano = dataLimpa.substring(4, 8);

                // Validação básica (opcional)
                if (dia > 31 || mes > 12) return '';

                return `${ano}-${mes}-${dia}`;
            };

            // Preenche campos básicos
            const ContaDepositoIdField = form.querySelector('[name="ContaDepositoId"]');
            const ContaDepositoNomeField = form.querySelector('[name="ContaDepositoNome"]');
            const ContaDepositoMontanteField = form.querySelector('[name="ContaDepositoMontante"]');
            const ContaDepositoDataCriacaoField = form.querySelector('[name="ContaDepositoDataCriacao"]');

            if (ContaDepositoIdField) ContaDepositoIdField.value = cells[0].textContent.trim();
            if (ContaDepositoNomeField) ContaDepositoNomeField.value = cells[1].textContent.trim();
            if (ContaDepositoMontanteField) {
                ContaDepositoMontanteField.value = cells[2].textContent.trim();;
            }

            // Preenche data de nascimento - CORREÇÃO PRINCIPAL AQUI
            if (ContaDepositoDataCriacaoField) {
                ContaDepositoDataCriacaoField.value = formatarDataParaInput(cells[3].textContent);
                console.log('Data formatada:', ContaDepositoDataCriacaoField.value); // Para debug
            }

        });
    }

    // Inicialização
    setupTableObserver();

    // Verificação inicial
    if (document.getElementById('listadeContasDepositoSelecinavel')) {
        setupTableEvents();
    }
}
