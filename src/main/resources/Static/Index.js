// Usando delegação de eventos para funcionar com conteúdo dinâmico
document.addEventListener('DOMContentLoaded', function() {
    // Ouvinte no documento que captura eventos de change propagados
    document.addEventListener('change', function(e) {
        if (e.target && e.target.id === 'cooperadoSelect') {
            const camposCooperado = document.getElementById('camposCooperado');
            if (camposCooperado) {
                if (e.target.value === 'true') {
                    camposCooperado.classList.remove('hidden-cooperado');
                } else {
                    camposCooperado.classList.add('hidden-cooperado');
                }
            }
        }
    });

    // Dispara o evento change se o select já existir
    const selectInicial = document.getElementById('cooperadoSelect');
    if (selectInicial) {
        selectInicial.dispatchEvent(new Event('change'));
    }
});