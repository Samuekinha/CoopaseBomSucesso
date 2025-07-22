//function initFiltros() {
      // Versão super reforçada com fallbacks
      const toggleFiltros = document.getElementById('toggleFiltrosPesquisa');
      const areaFiltros = document.getElementById('areaFiltros');

      // Debug inicial
      console.log('Elementos encontrados:', { toggleFiltros, areaFiltros });

      if (!toggleFiltros || !areaFiltros) {
          console.error('Elementos não encontrados. Tentando novamente...');
      }

      // Configura o toggle
      toggleFiltros.addEventListener('change', function() {
          areaFiltros.style.display = this.checked ? 'block' : 'none';
      });

      // Dispara evento inicial se necessário
      if (toggleFiltros.checked) {
          areaFiltros.style.display = 'block';
      }

  // Versões alternativas de inicialização
  document.addEventListener('DOMContentLoaded', initFiltros); // Para carregamento normal
  window.addEventListener('load', initFiltros); // Para quando tudo carregar
  setTimeout(initFiltros, 500); // Fallback final - executa depois de 500ms

  // Debug adicional
  console.log('Script FiltrosDePesquisa.js carregado');