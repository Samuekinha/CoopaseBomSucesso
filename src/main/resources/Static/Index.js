const homeButton = document.getElementById('home-button');

// Rola para o topo com comportamento suave e previne recarga
homeButton.addEventListener('click', (e) => {
    e.preventDefault(); // Impede o recarregamento
    window.scrollTo({
        top: 0,
        behavior: 'smooth' // Rolagem suave
    });
});