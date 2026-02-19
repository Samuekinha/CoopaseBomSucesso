export default function setupServiceBlocks() {
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