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

    // Inicializa a view
    initContaDepositoView();
});

// Classe para gerenciar as views de transação
class TransactionViewManager {
    constructor() {
        this.initialized = new Set();
    }

    // Verifica se o elemento contém uma view de transação
    checkForTransactionView(element) {
        // Verifica se é a própria view de transação
        if (element.getAttribute && element.getAttribute('data-view') === 'cadastrar-Transacao') {
            console.log('📄 View de transação detectada diretamente:', element);
            this.initTransactionView(element);
            return;
        }

        // Busca por views de transação dentro do elemento
        const transactionViews = element.querySelectorAll('[data-view="cadastrar-Transacao"]');
        transactionViews.forEach(view => {
            console.log('📄 View de transação encontrada dentro do elemento:', view);
            this.initTransactionView(view);
        });

        // Busca por botões de transação diretamente
        const transactionButtons = element.querySelectorAll('.transaction-option');
        if (transactionButtons.length > 0) {
            console.log('🔘 Botões de transação encontrados:', transactionButtons.length);
            this.initTransactionButtons();
        }
    }

    // Inicializa uma view de transação específica
    initTransactionView(viewElement) {
        const viewId = viewElement.id || `view-${Date.now()}`;

        if (this.initialized.has(viewId)) {
            console.log('⚠️ View já foi inicializada:', viewId);
            return;
        }

        console.log('🚀 Inicializando view de transação:', viewId);

        // Aguarda um pequeno delay para garantir que todos os elementos foram renderizados
        setTimeout(() => {
            this.initTransactionButtons(viewElement);
            this.initialized.add(viewId);
        }, 50);
    }

    // Inicializa os botões de transação
    initTransactionButtons(container = document) {
        console.log('🔘 Procurando botões de transação...');

        // Seleciona elementos dentro do container especificado
        const transactionOptions = container.querySelectorAll('.transaction-option');
        const transferenciaField = container.querySelector('#transferencia-field');
        const tipoTransacaoInput = container.querySelector('#TipoTransacao');
        const contaDestinoSelect = container.querySelector('#ContaDestino');
        const nomeOperadorSelect = container.querySelector('#NomeOperador');
        const operatorNameSpan = container.querySelector('#operator-name');

        console.log(`📊 Elementos encontrados:
        - Botões: ${transactionOptions.length}
        - Campo transferência: ${transferenciaField ? 'SIM' : 'NÃO'}
        - Input tipo: ${tipoTransacaoInput ? 'SIM' : 'NÃO'}
        - Select conta: ${contaDestinoSelect ? 'SIM' : 'NÃO'}
        - Select operador: ${nomeOperadorSelect ? 'SIM' : 'NÃO'}
        - Span operador: ${operatorNameSpan ? 'SIM' : 'NÃO'}`);

        if (transactionOptions.length === 0) {
            console.log('❌ Nenhum botão encontrado');
            return false;
        }

        // Remove event listeners antigos e adiciona novos
        transactionOptions.forEach((option, index) => {
            // Clona o elemento para remover todos os event listeners
            const newOption = option.cloneNode(true);
            option.parentNode.replaceChild(newOption, option);

            // Adiciona o event listener ao novo elemento
            newOption.addEventListener('click', (e) => {
                e.preventDefault();
                console.log('🔄 Botão clicado:', newOption.getAttribute('data-type'));
                this.handleTransactionClick(newOption, container);
            });
        });

        // Reseleciona os elementos após a clonagem
        const newTransactionOptions = container.querySelectorAll('.transaction-option');
        const newContaDestinoSelect = container.querySelector('#ContaDestino');
        const newNomeOperadorSelect = container.querySelector('#NomeOperador');

        // Event listener para o select de conta destino
        if (newContaDestinoSelect) {
            const newSelect = newContaDestinoSelect.cloneNode(true);
            newContaDestinoSelect.parentNode.replaceChild(newSelect, newContaDestinoSelect);

            newSelect.addEventListener('change', () => {
                console.log('💰 Conta destino alterada:', newSelect.value);
                if (newSelect.value) {
                    this.fillTable(newSelect.value, container);
                } else {
                    this.clearTable(container);
                }
            });
        }

        // Event listener para o select de operador
        if (newNomeOperadorSelect) {
            const newOperatorSelect = newNomeOperadorSelect.cloneNode(true);
            newNomeOperadorSelect.parentNode.replaceChild(newOperatorSelect, newNomeOperadorSelect);

            newOperatorSelect.addEventListener('change', () => {
                console.log('👤 Operador selecionado:', newOperatorSelect.value);
                this.updateOperatorDisplay(newOperatorSelect, container);
            });
        }

        // Inicialização - Define a primeira opção como ativa
        if (newTransactionOptions.length > 0) {
            const firstOption = newTransactionOptions[0];
            firstOption.classList.add('active');

            // Atualiza o campo de tipo de transação
            const tipoInput = container.querySelector('#TipoTransacao');
            if (tipoInput) {
                const transactionType = firstOption.getAttribute('data-type');
                tipoInput.value = transactionType;
            }

            // Atualiza a exibição inicial do operador se já houver um selecionado
            const operadorSelect = container.querySelector('#NomeOperador');
            if (operadorSelect && operadorSelect.value) {
                this.updateOperatorDisplay(operadorSelect, container);
            }
        }

        // Define data atual como padrão
        const dataInput = container.querySelector('#Data');
        if (dataInput && !dataInput.value) {
            const hoje = new Date().toISOString().split('T')[0];
            dataInput.value = hoje;
        }

        console.log('✅ Botões de transação inicializados com sucesso!');
        return true;
    }

    // Atualiza a exibição do operador selecionado
    updateOperatorDisplay(operatorSelect, container) {
        const operatorNameSpan = container.querySelector('#operator-name');
        if (!operatorNameSpan || !operatorSelect.value) {
            if (operatorNameSpan) {
                operatorNameSpan.textContent = '[Não informado]';
            }
            return;
        }

        // Pega o texto da opção selecionada
        const selectedOption = operatorSelect.options[operatorSelect.selectedIndex];
        const operatorName = selectedOption ? selectedOption.textContent : '[Não informado]';

        operatorNameSpan.textContent = operatorName;
        console.log('👤 Nome do operador atualizado para:', operatorName);
    }

    // Handler para cliques nos botões de transação
    handleTransactionClick(clickedButton, container) {
        const transactionOptions = container.querySelectorAll('.transaction-option');
        const transferenciaField = container.querySelector('#transferencia-field');
        const tipoTransacaoInput = container.querySelector('#TipoTransacao');
        const contaDestinoSelect = container.querySelector('#ContaDestino');
        const nomeOperadorSelect = container.querySelector('#NomeOperador');
        const operatorNameSpan = container.querySelector('#operator-name');

        // Remove active de todos os botões
        transactionOptions.forEach(opt => opt.classList.remove('active'));

        // Adiciona active no botão clicado
        clickedButton.classList.add('active');

        // Atualiza o valor do tipo de transação
        if (tipoTransacaoInput) {
            const transactionType = clickedButton.getAttribute('data-type');
            tipoTransacaoInput.value = transactionType;
            console.log('📝 Tipo de transação definido como:', transactionType);
        }

        // Atualiza o nome do operador baseado na seleção atual
        if (nomeOperadorSelect && operatorNameSpan) {
            this.updateOperatorDisplay(nomeOperadorSelect, container);
        }

        // Mostra/esconde campo de transferência
        if (transferenciaField) {
            if (clickedButton.getAttribute('data-type') === 'transferencia') {
                transferenciaField.classList.add('show');
                transferenciaField.style.display = 'block';
                console.log('👁️ Campo transferência mostrado');
            } else {
                transferenciaField.classList.remove('show');
                transferenciaField.style.display = 'none';
                console.log('🙈 Campo transferência escondido');

                // Limpa a seleção da conta destino
                if (contaDestinoSelect) {
                    contaDestinoSelect.value = '';
                    this.clearTable(container);
                }
            }
        }
    }

    // Métodos auxiliares (precisam ser implementados conforme sua lógica)
    fillTable(contaId, container) {
        console.log('📊 Preenchendo tabela para conta:', contaId);
        // Implementar lógica para preencher a tabela com base na conta selecionada
    }

    clearTable(container) {
        console.log('🧹 Limpando tabela');
        // Implementar lógica para limpar a tabela
    }
}

// Instância global do gerenciador de views de transação
const transactionViewManager = new TransactionViewManager();

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

    // Verifica se há views de transação no carregamento inicial
    transactionViewManager.checkForTransactionView(document);

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

            // Verifica se há novas views de transação adicionadas dinamicamente
            mutations.forEach(mutation => {
                mutation.addedNodes.forEach(node => {
                    if (node.nodeType === 1) { // Element nodes only
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

        // Verifica se o formulário expandido contém views de transação
        transactionViewManager.checkForTransactionView(formClone);
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

// Função para carregar views (se não existir, precisa ser definida)
if (typeof loadView === 'undefined') {
    window.loadView = function(action) {
        console.log('Carregando view para ação:', action);
        // Implementar lógica para carregar views dinamicamente
        // Após carregar a view, verificar se contém transações
        setTimeout(() => {
            transactionViewManager.checkForTransactionView(document.getElementById('dynamic-content'));
        }, 100);
    };
}