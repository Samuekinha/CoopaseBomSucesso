// Sistema robusto para lidar com views carregadas dinamicamente
class DynamicViewManager {
    constructor() {
        this.observers = new Map();
        this.initialized = new Set();
        this.initGlobalObserver();
    }

    // Observador global que monitora mudanças no DOM
    initGlobalObserver() {
        const globalObserver = new MutationObserver((mutations) => {
            mutations.forEach((mutation) => {
                if (mutation.type === 'childList') {
                    mutation.addedNodes.forEach((node) => {
                        if (node.nodeType === Node.ELEMENT_NODE) {
                            this.checkForTransactionView(node);
                        }
                    });
                }
            });
        });

        globalObserver.observe(document.body, {
            childList: true,
            subtree: true,
            attributes: true,
            attributeFilter: ['data-view']
        });

        console.log('🔍 Observador global iniciado - monitorando views dinâmicas');
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
        const operatorNameSpan = container.querySelector('#operator-name');

        console.log(`📊 Elementos encontrados:
        - Botões: ${transactionOptions.length}
        - Campo transferência: ${transferenciaField ? 'SIM' : 'NÃO'}
        - Input tipo: ${tipoTransacaoInput ? 'SIM' : 'NÃO'}
        - Select conta: ${contaDestinoSelect ? 'SIM' : 'NÃO'}
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

        // Inicialização - Define a primeira opção como ativa
        if (newTransactionOptions.length > 0) {
            const firstOption = newTransactionOptions[0];
            firstOption.classList.add('active');

            const tipoInput = container.querySelector('#TipoTransacao');
            const operatorSpan = container.querySelector('#operator-name');

            if (tipoInput) {
                tipoInput.value = firstOption.getAttribute('data-type');
            }

            if (operatorSpan) {
                const transactionType = firstOption.getAttribute('data-type');
                operatorSpan.textContent = transactionType.charAt(0).toUpperCase() + transactionType.slice(1);
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

    // Handler para cliques nos botões de transação
    handleTransactionClick(clickedButton, container) {
        const transactionOptions = container.querySelectorAll('.transaction-option');
        const transferenciaField = container.querySelector('#transferencia-field');
        const tipoTransacaoInput = container.querySelector('#TipoTransacao');
        const contaDestinoSelect = container.querySelector('#ContaDestino');
        const operatorNameSpan = container.querySelector('#operator-name');

        // Remove active de todos os botões
        transactionOptions.forEach(opt => opt.classList.remove('active'));

        // Adiciona active no botão clicado
        clickedButton.classList.add('active');

        // Atualiza o valor do tipo de transação
        if (tipoTransacaoInput) {
            const transactionType = clickedButton.getAttribute('data-type');
            tipoTransacaoInput.value = transactionType;

            // Atualiza o nome do operador
            if (operatorNameSpan) {
                operatorNameSpan.textContent = transactionType.charAt(0).toUpperCase() + transactionType.slice(1);
            }
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

    // Função para preencher a tabela
    fillTable(contaId, container = document) {
        const tableBody = container.querySelector('.transaction-table tbody');
        if (!tableBody) return;

        tableBody.innerHTML = '';

        // Dados simulados
        const mockData = {
            '1': [
                { id: 101, descricao: 'Depósito inicial', valor: 'R$ 1.000,00', data: '10/05/2023' },
                { id: 102, descricao: 'Transferência recebida', valor: 'R$ 500,00', data: '15/05/2023' },
                { id: 103, descricao: 'Pagamento PIX', valor: 'R$ 250,00', data: '18/05/2023' }
            ],
            '2': [
                { id: 201, descricao: 'Aplicação', valor: 'R$ 2.000,00', data: '05/05/2023' },
                { id: 202, descricao: 'Rendimento', valor: 'R$ 50,00', data: '20/05/2023' },
                { id: 203, descricao: 'Resgate parcial', valor: 'R$ 300,00', data: '22/05/2023' }
            ],
            '3': [
                { id: 301, descricao: 'Compra de ações', valor: 'R$ 3.000,00', data: '01/05/2023' },
                { id: 302, descricao: 'Dividendos', valor: 'R$ 150,00', data: '25/05/2023' },
                { id: 303, descricao: 'Venda de ações', valor: 'R$ 1.800,00', data: '28/05/2023' }
            ]
        };

        const data = mockData[contaId] || [];

        data.forEach(item => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${item.id}</td>
                <td>${item.descricao}</td>
                <td>${item.valor}</td>
                <td>${item.data}</td>
            `;
            tableBody.appendChild(row);
        });

        if (data.length === 0) {
            const row = document.createElement('tr');
            row.innerHTML = '<td colspan="4" style="text-align: center; color: #6c757d;">Nenhuma transação encontrada</td>';
            tableBody.appendChild(row);
        }

        console.log(`📊 Tabela preenchida para conta ${contaId} com ${data.length} transações`);
    }

    // Função para limpar a tabela
    clearTable(container = document) {
        const tableBody = container.querySelector('.transaction-table tbody');
        if (!tableBody) return;

        tableBody.innerHTML = `
            <tr>
                <td colspan="4" style="text-align: center; color: #6c757d;">
                    Selecione uma conta de destino para ver as transações
                </td>
            </tr>
        `;
    }

    // Método para forçar inicialização manual
    forceInit() {
        console.log('🔧 Forçando inicialização manual...');
        const transactionViews = document.querySelectorAll('[data-view="cadastrar-Transacao"]');
        transactionViews.forEach(view => this.initTransactionView(view));

        // Se não encontrou views específicas, tenta inicializar botões diretamente
        if (transactionViews.length === 0) {
            this.initTransactionButtons();
        }
    }
}

// Instância global do gerenciador
const dynamicViewManager = new DynamicViewManager();

// Funções de compatibilidade com o código existente
function initTransactionButtons() {
    return dynamicViewManager.initTransactionButtons();
}

// Modificação da função initTransacaoView existente
function initTransacaoView() {
    // coloração de blocos de serviço quando selecionados
    setupServiceBlocks();

    // Força a inicialização dos botões
    dynamicViewManager.forceInit();

    // Resto do código original mantido...
    // (setupTableObserver, etc.)
    setupTableObserver();
    if (document.getElementById('listaDeTransacaoSelecinavel')) {
        setupTableEvents();
    }
}

// Event listener para DOMContentLoaded (mantém compatibilidade)
document.addEventListener('DOMContentLoaded', function() {
    console.log('🌟 DOM carregado - iniciando sistema de views dinâmicas');
    dynamicViewManager.forceInit();
});

// Expõe funções globalmente
window.initTransactionButtons = initTransactionButtons;
window.initTransacaoView = initTransacaoView;
window.forceInitTransactionButtons = () => dynamicViewManager.forceInit();

// Resto do seu código original (setupServiceBlocks, etc.)
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

                // Aguarda a view ser carregada e força inicialização
                setTimeout(() => {
                    dynamicViewManager.forceInit();
                }, 100);
            } else {
                console.log(`Ação selecionada: ${action}`);
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

// Variável para controlar o observador (mantém código original)
let tableObserver;

function setupTableObserver() {
    if (tableObserver) {
        tableObserver.disconnect();
    }

    tableObserver = new MutationObserver(function(mutations) {
        const table = document.getElementById('listaDeTransacaoSelecinavel');
        if (table) {
            setupTableEvents();
        }
    });

    tableObserver.observe(document.body, {
        childList: true,
        subtree: true
    });
}

function setupTableEvents() {
    const table = document.getElementById('listaDeTransacaoSelecinavel');
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

        const ContaDepositoIdField = form.querySelector('[name="NãoMechaDeepSeek,Ok"]');
        const ContaDepositoNomeField = form.querySelector('[name="NãoMechaDeepSeek,Ok2"]');
        const ContaDepositoMontanteField = form.querySelector('[name="NãoMechaDeepSeek,Ok3"]');
        const ContaDepositoDataCriacaoField = form.querySelector('[name="NãoMechaDeepSeek,Ok4"]');

        if (ContaDepositoIdField) ContaDepositoIdField.value = cells[0].textContent.trim();
        if (ContaDepositoNomeField) ContaDepositoNomeField.value = cells[1].textContent.trim();
        if (ContaDepositoMontanteField) {
            ContaDepositoMontanteField.value = cells[2].textContent.trim();
        }

        if (ContaDepositoDataCriacaoField) {
            ContaDepositoDataCriacaoField.value = formatarDataParaInput(cells[3].textContent);
            console.log('Data formatada:', ContaDepositoDataCriacaoField.value);
        }
    });
}