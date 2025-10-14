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
        const tipoTransacaoInput = container.querySelector('#tipoTransacao');

        // IDs CORRETOS do HTML
        const contaPrincipalSelect = container.querySelector('#contaPrincipalId');
        const contaDestinoSelect = container.querySelector('#contaDestinoId');
        const nomeOperadorSelect = container.querySelector('#operadorTransacaoId');
        const operatorNameSpan = container.querySelector('#NomeOperadorSpan');
        const valorInput = container.querySelector('#valorTransacao');

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

        // Atualização do preview com base no valor e tipo
        const atualizarPreview = () => {
            console.log('🔄 Atualizando preview...');
            const tipo = container.querySelector('#tipoTransacao')?.value;
            const valorAtual = container.querySelector('#valorTransacao')?.value || "0";
            const valor = parseFloat(valorAtual.replace(',', '.')) || 0;

            console.log('💰 Valor atual:', valorAtual, '-> Valor parseado:', valor);
            console.log('📝 Tipo de transação:', tipo);

            const principalAtual = container.querySelector('#preview-principal td:nth-child(2)');
            const principalDepois = container.querySelector('#preview-principal td:nth-child(3)');
            const destinoAtual = container.querySelector('#preview-destino td:nth-child(2)');
            const destinoDepois = container.querySelector('#preview-destino td:nth-child(3)');

            if (!principalAtual || !principalDepois) {
                console.log('⚠️ Elementos de preview principal não encontrados');
                return;
            }

            const principalSaldo = parseFloat(principalAtual.textContent.replace(',', '.')) || 0;
            const destinoSaldo = parseFloat(destinoAtual?.textContent.replace(',', '.')) || 0;

            console.log('💳 Saldo principal atual:', principalSaldo);
            if (destinoAtual) console.log('💳 Saldo destino atual:', destinoSaldo);

            if (!tipo) {
                console.log('❌ Tipo de transação não definido');
                return;
            }

            switch (tipo) {
                case "DEPOSIT":
                    principalDepois.textContent = (principalSaldo + valor).toFixed(2);
                    console.log('💸 DEPOSIT - Novo saldo:', (principalSaldo + valor).toFixed(2));
                    break;
                case "WITHDRAW":
                    principalDepois.textContent = (principalSaldo - valor).toFixed(2);
                    console.log('💸 WITHDRAW - Novo saldo:', (principalSaldo - valor).toFixed(2));
                    break;
                case "TRANSFER":
                    principalDepois.textContent = (principalSaldo - valor).toFixed(2);
                    if (destinoDepois) {
                        destinoDepois.textContent = (destinoSaldo + valor).toFixed(2);
                        console.log('💸 TRANSFER - Principal:', (principalSaldo - valor).toFixed(2), 'Destino:', (destinoSaldo + valor).toFixed(2));
                    }
                    break;
            }
        };

        // VERSÃO SEGURA SEM CLONENODE - Apenas adiciona listeners únicos
        transactionOptions.forEach((option) => {
            // Evita listeners duplicados
            if (option.dataset.listenerAdded) return;

            option.addEventListener('click', (e) => {
                e.preventDefault();
                console.log('🔄 Botão clicado:', option.getAttribute('data-type'));
                this.handleTransactionClick(option, container);
                setTimeout(atualizarPreview, 100);
            });

            option.dataset.listenerAdded = 'true';
        });

        if (contaPrincipalSelect && !contaPrincipalSelect.dataset.listenerAdded) {
            contaPrincipalSelect.addEventListener('change', () => {
                const contaDestinoAtual = container.querySelector('#contaDestinoId');
                const tipoAtual = container.querySelector('#tipoTransacao')?.value;

                if (tipoAtual === "TRANSFER" && contaPrincipalSelect.value && contaDestinoAtual && contaPrincipalSelect.value === contaDestinoAtual.value) {
                    Toast.fire({
                        icon: "error",
                        title: `Erro: a conta principal e de destino não podem ser iguais`
                    });
                    contaPrincipalSelect.value = "";
                    this.clearTable(container, "principal");
                    return;
                }

                if (contaPrincipalSelect.value) {
                    this.fillTable(contaPrincipalSelect.value, container, "principal", atualizarPreview);
                } else {
                    this.clearTable(container, "principal");
                }
            });
            contaPrincipalSelect.dataset.listenerAdded = 'true';
        }

        if (contaDestinoSelect && !contaDestinoSelect.dataset.listenerAdded) {
            contaDestinoSelect.addEventListener('change', () => {
                const contaPrincipalAtual = container.querySelector('#contaPrincipalId');
                const tipoAtual = container.querySelector('#tipoTransacao')?.value;

                if (tipoAtual === "TRANSFER" && contaDestinoSelect.value && contaPrincipalAtual && contaDestinoSelect.value === contaPrincipalAtual.value) {
                    Toast.fire({
                        icon: "error",
                        title: `Erro: a conta principal e de destino não podem ser iguais`
                    });
                    contaDestinoSelect.value = "";
                    this.clearTable(container, "destino");
                    return;
                }

                if (contaDestinoSelect.value) {
                    this.fillTable(contaDestinoSelect.value, container, "destino", atualizarPreview);
                } else {
                    this.clearTable(container, "destino");
                }
            });
            contaDestinoSelect.dataset.listenerAdded = 'true';
        }

        // Atualiza preview ao digitar valor
        if (valorInput && !valorInput.dataset.listenerAdded) {
            // Usar tanto 'input' quanto 'keyup' para garantir que funcione
            valorInput.addEventListener('input', () => {
                console.log('⌨️ Input event disparado - Valor:', valorInput.value);
                atualizarPreview();
            });

            valorInput.addEventListener('keyup', () => {
                console.log('⌨️ Keyup event disparado - Valor:', valorInput.value);
                atualizarPreview();
            });

            valorInput.addEventListener('change', () => {
                console.log('⌨️ Change event disparado - Valor:', valorInput.value);
                atualizarPreview();
            });

            valorInput.dataset.listenerAdded = 'true';
        }

        // Operador select com ID correto
        if (nomeOperadorSelect && !nomeOperadorSelect.dataset.listenerAdded) {
            nomeOperadorSelect.addEventListener('change', () => {
                console.log('👤 Operador selecionado:', nomeOperadorSelect.value);
                this.updateOperatorDisplay(nomeOperadorSelect, container);
            });
            nomeOperadorSelect.dataset.listenerAdded = 'true';
        }

        // Inicialização padrão
        if (transactionOptions.length > 0) {
            const firstOption = container.querySelector('.transaction-option');
            firstOption.classList.add('active');

            if (tipoTransacaoInput) {
                const transactionType = firstOption.getAttribute('data-type');
                tipoTransacaoInput.value = transactionType;
            }

            // Atualizar display do operador se já houver um selecionado
            const operadorAtual = container.querySelector('#operadorTransacaoId');
            if (operadorAtual && operadorAtual.value) {
                this.updateOperatorDisplay(operadorAtual, container);
            }
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
        const tipoTransacaoInput = container.querySelector('#tipoTransacao');
        const contaPrincipalSelect = container.querySelector('#contaPrincipalId');
        const contaDestinoSelect = container.querySelector('#contaDestinoId');
        const nomeOperadorSelect = container.querySelector('#operadorTransacaoId');
        const operatorNameSpan = container.querySelector('#NomeOperadorSpan');
        const previewDestino = container.querySelector('#preview-destino');

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
            if (clickedButton.getAttribute('data-type') === 'TRANSFER') {
                transferenciaField.classList.add('show');
                transferenciaField.style.display = 'block';
                if (previewDestino) previewDestino.style.display = 'table-row-group';
                console.log('👁️ Campo transferência mostrado');
            } else {
                transferenciaField.classList.remove('show');
                transferenciaField.style.display = 'none';
                if (previewDestino) previewDestino.style.display = 'none';
                console.log('🙈 Campo transferência escondido');

                // Limpar conta de destino quando não é transferência
                if (contaDestinoSelect) {
                    contaDestinoSelect.value = '';
                    this.clearTable(container, "destino");
                }
            }
        }
    }

    fillTable(contaId, container, tipo, callbackPreview = null) {
        console.log(`🔄 Fazendo fetch para conta ${contaId}, tipo: ${tipo}`);

        fetch(`/Coopase/ContaDeposito/${contaId}`)
            .then(response => {
                console.log('📡 Resposta do fetch:', response.status);
                if (!response.ok) throw new Error(`Erro na resposta: ${response.status}`);
                return response.json();
            })
            .then(data => {
                console.log('💾 Dados recebidos:', data);

                const tbody = container.querySelector(
                    tipo === "principal" ? "#preview-principal" : "#preview-destino"
                );
                if (!tbody) {
                    console.error('❌ Tbody não encontrado para tipo:', tipo);
                    return;
                }

                tbody.style.display = "table-row-group";

                tbody.innerHTML = `
                    <tr>
                        <td>${data.nomeConta ?? data.vaultName ?? "—"}</td>
                        <td>${data.saldoAtual != null ? data.saldoAtual.toFixed(2) : "0.00"}</td>
                        <td>${data.saldoAtual != null ? data.saldoAtual.toFixed(2) : "0.00"}</td>
                    </tr>
                `;

                console.log('✅ Tabela preenchida com sucesso');

                // Chamar o callback de preview se fornecido
                if (callbackPreview) {
                    setTimeout(callbackPreview, 50);
                }
            })
            .catch(err => {
                console.error("❌ Erro ao carregar conta:", err);
                this.clearTable(container, tipo);
            });
    }

    clearTable(container, tipo) {
        const tbody = container.querySelector(
            tipo === "principal" ? "#preview-principal" : "#preview-destino"
        );
        if (!tbody) return;

        tbody.style.display = "table-row-group";
        tbody.innerHTML = `
            <tr>
                <td colspan="3" style="text-align: center; color: #6c757d;">
                    Selecione a conta ${tipo}
                </td>
            </tr>
        `;
    }
}

const transactionViewManager = new TransactionViewManager();
export default transactionViewManager;