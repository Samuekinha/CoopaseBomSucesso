// Classe para gerenciar as views de transação
class TransactionViewManager {
    constructor() {
        this.initialized = new Set();
    }

    checkForTransactionView(element) {
        if (element.getAttribute && element.getAttribute('data-view') === 'cadastrar-Transacao') {
            console.log('📄 View de transação detectada diretamente:', element);
            this.initTransactionView(element);
            return;
        }

        const transactionViews = element.querySelectorAll('[data-view="cadastrar-Transacao"]');
        transactionViews.forEach(view => {
            console.log('📄 View de transação encontrada dentro do elemento:', view);
            this.initTransactionView(view);
        });

        const transactionButtons = element.querySelectorAll('.transaction-option');
        if (transactionButtons.length > 0) {
            console.log('🔘 Botões de transação encontrados:', transactionButtons.length);
            this.initTransactionButtons();
        }
    }

    initTransactionView(viewElement) {
        const viewId = viewElement.id || `view-${Date.now()}`;

        if (this.initialized.has(viewId)) {
            console.log('⚠️ View já foi inicializada:', viewId);
            return;
        }

        console.log('🚀 Inicializando view de transação:', viewId);

        setTimeout(() => {
            this.initTransactionButtons(viewElement);
            this.initialized.add(viewId);
        }, 50);
    }

    initTransactionButtons(container = document) {
        console.log('🔘 Procurando botões de transação...');

        const transactionOptions = container.querySelectorAll('.transaction-option');
        const transferenciaField = container.querySelector('#transferencia-field');
        const tipoTransacaoInput = container.querySelector('#TipoTransacao');
        const contaPrincipalSelect = container.querySelector('#ContaPrincipal');
        const nomeOperadorSelect = container.querySelector('#NomeOperador');
        const operatorNameSpan = container.querySelector('#NomeOperadorSpan');

        console.log(`📊 Elementos encontrados:
        - Botões: ${transactionOptions.length}
        - Campo transferência: ${transferenciaField ? 'SIM' : 'NÃO'}
        - Input tipo: ${tipoTransacaoInput ? 'SIM' : 'NÃO'}
        - Select conta: ${contaPrincipalSelect ? 'SIM' : 'NÃO'}
        - Select operador: ${nomeOperadorSelect ? 'SIM' : 'NÃO'}
        - Span operador: ${operatorNameSpan ? 'SIM' : 'NÃO'}`);

        if (transactionOptions.length === 0) {
            console.log('❌ Nenhum botão encontrado');
            return false;
        }

        transactionOptions.forEach((option) => {
            const newOption = option.cloneNode(true);
            option.parentNode.replaceChild(newOption, option);

            newOption.addEventListener('click', (e) => {
                e.preventDefault();
                console.log('🔄 Botão clicado:', newOption.getAttribute('data-type'));
                this.handleTransactionClick(newOption, container);
            });
        });

        const newTransactionOptions = container.querySelectorAll('.transaction-option');
        const newContaPrincipalSelect = container.querySelector('#ContaPrincipal');
        const newNomeOperadorSelect = container.querySelector('#NomeOperador');

        if (newContaPrincipalSelect) {
            const newSelect = newContaPrincipalSelect.cloneNode(true);
            newContaPrincipalSelect.parentNode.replaceChild(newSelect, newContaPrincipalSelect);

            newSelect.addEventListener('change', () => {
                console.log('💰 Conta destino alterada:', newSelect.value);
                if (newSelect.value) {
                    this.fillTable(newSelect.value, container);
                } else {
                    this.clearTable(container);
                }
            });
        }

        if (newNomeOperadorSelect) {
            const newOperatorSelect = newNomeOperadorSelect.cloneNode(true);
            newNomeOperadorSelect.parentNode.replaceChild(newOperatorSelect, newNomeOperadorSelect);

            newOperatorSelect.addEventListener('change', () => {
                console.log('👤 Operador selecionado:', newOperatorSelect.value);
                this.updateOperatorDisplay(newOperatorSelect, container);
            });
        }

        if (newTransactionOptions.length > 0) {
            const firstOption = newTransactionOptions[0];
            firstOption.classList.add('active');

            const tipoInput = container.querySelector('#TipoTransacao');
            if (tipoInput) {
                const transactionType = firstOption.getAttribute('data-type');
                tipoInput.value = transactionType;
            }

            const operadorSelect = container.querySelector('#NomeOperador');
            if (operadorSelect && operadorSelect.value) {
                this.updateOperatorDisplay(operadorSelect, container);
            }
        }

        const dataInput = container.querySelector('#Data');
        if (dataInput && !dataInput.value) {
            const hoje = new Date().toISOString().split('T')[0];
            dataInput.value = hoje;
        }

        console.log('✅ Botões de transação inicializados com sucesso!');
        return true;
    }

    updateOperatorDisplay(operatorSelect, container) {
        const operatorNameSpan = container.querySelector('#NomeOperadorSpan');
        if (!operatorNameSpan || !operatorSelect.value) {
            if (operatorNameSpan) {
                operatorNameSpan.textContent = '[Não informado]';
            }
            return;
        }

        const selectedOption = operatorSelect.options[operatorSelect.selectedIndex];
        const operatorName = selectedOption ? selectedOption.textContent : '[Não informado]';

        operatorNameSpan.textContent = operatorName;
        console.log('👤 Nome do operador atualizado para:', operatorName);
    }

    handleTransactionClick(clickedButton, container) {
        const transactionOptions = container.querySelectorAll('.transaction-option');
        const transferenciaField = container.querySelector('#transferencia-field');
        const tipoTransacaoInput = container.querySelector('#TipoTransacao');
        const contaPrincipalSelect = container.querySelector('#ContaPrincipal');
        const nomeOperadorSelect = container.querySelector('#NomeOperador');
        const operatorNameSpan = container.querySelector('#NomeOperadorSpan');

        transactionOptions.forEach(opt => opt.classList.remove('active'));
        clickedButton.classList.add('active');

        if (tipoTransacaoInput) {
            const transactionType = clickedButton.getAttribute('data-type');
            tipoTransacaoInput.value = transactionType;
            console.log('📝 Tipo de transação definido como:', transactionType);
        }

        if (nomeOperadorSelect && operatorNameSpan) {
            this.updateOperatorDisplay(nomeOperadorSelect, container);
        }

        if (transferenciaField) {
            if (clickedButton.getAttribute('data-type') === 'transferencia') {
                transferenciaField.classList.add('show');
                transferenciaField.style.display = 'block';
                console.log('👁️ Campo transferência mostrado');
            } else {
                transferenciaField.classList.remove('show');
                transferenciaField.style.display = 'none';
                console.log('🙈 Campo transferência escondido');

                if (contaPrincipalSelect) {
                    contaPrincipalSelect.value = '';
                    this.clearTable(container);
                }
            }
        }
    }

    fillTable(contaId, container) {
        console.log('📊 Buscando dados da conta:', contaId);

        fetch(`/Coopase/Transacao/Conta/${contaId}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error("Erro ao buscar conta");
                }
                return response.json();
            })
            .then(data => {
                console.log("📥 Dados recebidos:", data);

                const tbody = container.querySelector(".transaction-table tbody");
                if (!tbody) return;

                tbody.innerHTML = `
                    <tr>
                        <td>${data.nomeConta}</td>
                        <td>${data.saldoAtual}</td>
                        <td>${data.saldoDepois}</td>
                    </tr>
                `;
            })
            .catch(error => {
                console.error("Erro ao carregar preview:", error);
                this.clearTable(container);
            });
    }

    clearTable(container) {
        const tbody = container.querySelector(".transaction-table tbody");
        if (!tbody) return;

        tbody.innerHTML = `
            <tr>
                <td colspan="3" style="text-align: center; color: #6c757d;">
                    Selecione uma conta para ver a preview
                </td>
            </tr>
        `;
        console.log('🧹 Tabela resetada');
    }
}

const transactionViewManager = new TransactionViewManager();
export default transactionViewManager;
