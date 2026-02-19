class DiaristaViewManager {
    constructor() {
        this.initialized = new Set();
    }

    checkForDiaristaView(element) {
        if (element.getAttribute && element.getAttribute('data-view') === 'cadastrar-Diarista') {
            console.log('📄 View de transação detectada diretamente:', element);
            this.initDiaristaView(element);
            return;
        }

        const diaristaViews = element.querySelectorAll('[data-view="cadastrar-Diarista"]');
        diaristaViews.forEach(view => {
            console.log('📄 View de transação encontrada dentro do elemento:', view);
            this.initDiaristaView(view);
        });

        const transactionButtons = element.querySelectorAll('.transaction-option');
        if (transactionButtons.length > 0) {
            console.log('🔘 Botões de transação encontrados:', transactionButtons.length);
            this.initTransactionButtons();
        }
    }

    initDiaristaView(viewElement) {
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
    }

}

const diaristaViewManager = new DiaristaViewManager();
export default diaristaViewManager;