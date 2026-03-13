/**
 * Configura o toggle de filtros para todas as views exceto CadastrarCliente
 * Usa MutationObserver para aguardar o carregamento dinâmico das views
 */

class ConsultarClienteView {
  constructor() {
    this.observer = null;
    this.initialized = false;
  }

  init() {
    if (this.initialized) return;

    // Configura o observer para detectar quando qualquer view (exceto cadastro) é carregada
    this.setupViewObserver();
    this.initialized = true;
  }

  setupViewObserver() {
    // Observa mudanças no DOM para detectar quando as views são injetadas
    this.observer = new MutationObserver((mutations) => {
      // Procura por qualquer view de cliente, exceto a de cadastro
      const views = document.querySelectorAll('[data-view]');

      views.forEach(view => {
        const viewType = view.getAttribute('data-view');

        // Ignora a view de cadastro e configura as demais
        if (viewType && viewType !== "cadastrar-cliente") {
          this.setupFilterToggle();
        }
      });
    });

    // Começa a observar o corpo do documento
    this.observer.observe(document.body, {
      childList: true,
      subtree: true
    });
  }

  setupFilterToggle() {
    const toggle = document.getElementById('toggleFiltrosPesquisa');
    const filtros = document.getElementById('areaFiltros');

    // Se já foi configurado ou elementos não existem, retorna
    if (!toggle || !filtros || toggle.dataset.initialized) {
      return;
    }

    // Marca como inicializado para evitar duplicação
    toggle.dataset.initialized = "true";

    // Estado inicial
    filtros.style.display = toggle.checked ? 'block' : 'none';

    // Evento de change
    toggle.addEventListener('change', () => {
      filtros.style.display = toggle.checked ? 'block' : 'none';
    });
  }
}

// Exporta a instância para uso global
window.consultarClienteView = new ConsultarClienteView();

// Inicializa automaticamente quando o DOM estiver pronto
document.addEventListener('DOMContentLoaded', () => {
  window.consultarClienteView.init();
});