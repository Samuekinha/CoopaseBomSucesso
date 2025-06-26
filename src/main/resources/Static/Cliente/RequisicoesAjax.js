document.addEventListener('DOMContentLoaded', function() {
    async function loadContent(action) {
        try {
            const response = await fetch(`/Coopase/Cliente/Servicos/${action}`, {
                headers: { 'Accept': 'text/html' }
            });

            if (!response.ok) throw new Error(`Erro HTTP: ${response.status}`);

            const html = await response.text();

            // Verifica se o HTML contém o fragmento esperado
            if (!html.includes('th:fragment')) {
                throw new Error('Resposta não contém fragmento Thymeleaf');
            }

            // Cria um container temporário
            const tempContainer = document.createElement('div');
            tempContainer.innerHTML = html;

            // Tenta encontrar o fragmento de três maneiras diferentes
            const fragment =
                tempContainer.querySelector('[th\\:fragment]') ||  // Notação Thymeleaf
                tempContainer.querySelector('div') ||               // Primeira div
                tempContainer;                                    // Fallback para todo conteúdo

            // Insere no container principal
            document.getElementById('dynamic-content').innerHTML = fragment.innerHTML;

            // Atualiza a URL
            history.pushState({action}, null, `/Coopase/Cliente/Servicos?action=${action}`);

        } catch (error) {
            console.error('Erro ao carregar:', error);
            document.getElementById('dynamic-content').innerHTML = `
                <div class="alert alert-danger">
                    Erro: ${error.message || 'Falha ao carregar o conteúdo'}
                </div>
            `;
        }
    }

    // Configuração dos eventos (mantido igual)
    document.querySelectorAll('.bloco-link').forEach(link => {
        link.addEventListener('click', function(e) {
            e.preventDefault();
            loadContent(this.getAttribute('data-action'));
        });
    });

    // Carregamento inicial (mantido igual)
    const urlParams = new URLSearchParams(window.location.search);
    loadContent(urlParams.get('action') || 'Cadastrar');
});