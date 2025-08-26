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