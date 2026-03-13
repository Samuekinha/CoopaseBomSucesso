import initContaDepositoView from './transacaoView.js';
import transactionViewManager from './transactionViewManager.js';

document.addEventListener('DOMContentLoaded', function() {
    // Seu código aqui (igual ao que você enviou)
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

    initContaDepositoView();
    transactionViewManager.checkForTransactionView(document);

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

});

if (typeof loadView === 'undefined') {
    window.loadView = function(action) {
        console.log('Carregando view para ação:', action);
        setTimeout(() => {
            const dynamicContent = document.getElementById('dynamic-content');
            if (dynamicContent) {
                transactionViewManager.checkForTransactionView(dynamicContent);
            }
        }, 100);
    };
}