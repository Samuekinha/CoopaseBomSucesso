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
        const contaPrincipalSelect = container.querySelector('#ContaPrincipal');
        const contaDestinoSelect = container.querySelector('#ContaDestino');
        const nomeOperadorSelect = container.querySelector('#NomeOperador');
        const operatorNameSpan = container.querySelector('#NomeOperadorSpan');

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

        transactionOptions.forEach((option) => {
            const newOption = option.cloneNode(true);
            option.parentNode.replaceChild(newOption, option);

            newOption.addEventListener('click', (e) => {
                e.preventDefault();
                console.log('🔄 Botão clicado:', newOption.getAttribute('data-type'));
                this.handleTransactionClick(newOption, container);
                setTimeout(atualizarPreview, 50);
            });
        });

        if (contaPrincipalSelect) {
            let newPrincipal = contaPrincipalSelect.cloneNode(true);
            contaPrincipalSelect.parentNode.replaceChild(newPrincipal, contaPrincipalSelect);

            newPrincipal.addEventListener('change', () => {
                const contaDestinoAtual = container.querySelector('#ContaDestino');
                const tipoAtual = container.querySelector('#tipoTransacao')?.value;

                if (tipoAtual === "TRANSFER" && newPrincipal.value && contaDestinoAtual && newPrincipal.value === contaDestinoAtual.value) {
                    Toast.fire({
                        icon: "error",
                        title: `Erro: a conta principal e de destino não podem ser iguais`
                    });
                    newPrincipal.value = "";
                    this.clearTable(container, "principal");
                    return;
                }

                if (newPrincipal.value) {
                    this.fillTable(newPrincipal.value, container, "principal");
                } else {
                    this.clearTable(container, "principal");
                }

                setTimeout(atualizarPreview, 50);
            });
        }

        if (contaDestinoSelect) {
            let newDestino = contaDestinoSelect.cloneNode(true);
            contaDestinoSelect.parentNode.replaceChild(newDestino, contaDestinoSelect);

            newDestino.addEventListener('change', () => {
                const contaPrincipalAtual = container.querySelector('#ContaPrincipal');
                const tipoAtual = container.querySelector('#tipoTransacao')?.value;

                if (tipoAtual === "TRANSFER" && newDestino.value && contaPrincipalAtual && newDestino.value === contaPrincipalAtual.value) {
                    Toast.fire({
                        icon: "error",
                        title: `Erro: a conta principal e de destino não podem ser iguais`
                    });
                    newDestino.value = "";
                    this.clearTable(container, "destino");
                    return;
                }

                if (newDestino.value) {
                    this.fillTable(newDestino.value, container, "destino");
                } else {
                    this.clearTable(container, "destino");
                }

                setTimeout(atualizarPreview, 50);
            });
        }

        // Atualização do preview com base no valor e tipo
        const valorInput = container.querySelector('#Valor');
        const atualizarPreview = () => {
            const tipo = container.querySelector('#tipoTransacao')?.value;
            const valor = parseFloat((valorInput?.value ?? "0").replace(',', '.')) || 0;

            const principalAtual = container.querySelector('#preview-principal td:nth-child(2)');
            const principalDepois = container.querySelector('#preview-principal td:nth-child(3)');
            const destinoAtual = container.querySelector('#preview-destino td:nth-child(2)');
            const destinoDepois = container.querySelector('#preview-destino td:nth-child(3)');

            const principalSaldo = parseFloat(principalAtual?.textContent.replace(',', '.')) || 0;
            const destinoSaldo = parseFloat(destinoAtual?.textContent.replace(',', '.')) || 0;

            if (!tipo) return;

            switch (tipo) {
                case "DEPOSIT":
                    if (principalDepois) principalDepois.textContent = (principalSaldo + valor).toFixed(2);
                    break;
                case "WITHDROW":
                    if (principalDepois) principalDepois.textContent = (principalSaldo - valor).toFixed(2);
                    break;
                case "TRANSFER":
                    if (principalDepois) principalDepois.textContent = (principalSaldo - valor).toFixed(2);
                    if (destinoDepois) destinoDepois.textContent = (destinoSaldo + valor).toFixed(2);
                    break;
            }
        };

        // Atualiza preview ao digitar valor
        if (valorInput) valorInput.addEventListener('input', atualizarPreview);

        const newNomeOperadorSelect = container.querySelector('#NomeOperador');
        if (newNomeOperadorSelect) {
            const newOperatorSelect = newNomeOperadorSelect.cloneNode(true);
            newNomeOperadorSelect.parentNode.replaceChild(newOperatorSelect, newNomeOperadorSelect);

            newOperatorSelect.addEventListener('change', () => {
                console.log('👤 Operador selecionado:', newOperatorSelect.value);
                this.updateOperatorDisplay(newOperatorSelect, container);
            });
        }

        if (transactionOptions.length > 0) {
            const firstOption = container.querySelector('.transaction-option');
            firstOption.classList.add('active');

            if (tipoTransacaoInput) {
                const transactionType = firstOption.getAttribute('data-type');
                tipoTransacaoInput.value = transactionType;
            }

            if (nomeOperadorSelect && nomeOperadorSelect.value) {
                this.updateOperatorDisplay(nomeOperadorSelect, container);
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
        const tipoTransacaoInput = container.querySelector('#tipoTransacao');
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
            if (clickedButton.getAttribute('data-type') === 'TRANSFER') {
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

    fillTable(contaId, container, tipo) {
        fetch(`/Coopase/ContaDeposito/${contaId}`)
            .then(response => {
                if (!response.ok) throw new Error("Erro na resposta");
                return response.json();
            })
            .then(data => {
                const tbody = container.querySelector(
                    tipo === "principal" ? "#preview-principal" : "#preview-destino"
                );
                if (!tbody) return;

                tbody.style.display = "table-row-group";

                tbody.innerHTML = `
                    <tr>
                        <td>${data.nomeConta ?? "—"}</td>
                        <td>${data.saldoAtual != null ? data.saldoAtual : "—"}</td>
                        <td>${data.saldoPrevisto != null ? data.saldoPrevisto : "—"}</td>
                    </tr>
                `;
            })
            .catch(err => {
                console.error("Erro ao carregar conta:", err);
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
