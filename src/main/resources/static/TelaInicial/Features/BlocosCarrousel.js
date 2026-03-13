// Carousel - Coopase
// Configuração do auto-play (em milissegundos)
const AUTOPLAY_DELAY = 3000; // 3000 = 3 segundos

class Carousel {
    constructor(container) {
        this.container = container;
        this.items = container.querySelectorAll('.bloco-link');
        this.currentIndex = 0;
        this.autoplayInterval = null;
        this.autoplayDelay = AUTOPLAY_DELAY;

        this.init();
    }

    init() {
        // Garantir que o scroll comece em 0 (primeiro item visível)
        this.container.scrollLeft = 0;

        // Criar dots
        this.createDots();

        // Event listeners para botões
        document.querySelector('.carousel-nav.prev').addEventListener('click', () => this.prev());
        document.querySelector('.carousel-nav.next').addEventListener('click', () => this.next());

        // Iniciar auto-play
        this.startAutoplay();

        // Pausar auto-play ao passar o mouse
        this.container.addEventListener('mouseenter', () => this.stopAutoplay());
        this.container.addEventListener('mouseleave', () => this.startAutoplay());
    }

    createDots() {
        const dotsContainer = document.getElementById('dots');
        this.items.forEach((_, index) => {
            const dot = document.createElement('button');
            dot.classList.add('dot');
            if (index === 0) dot.classList.add('active');
            dot.addEventListener('click', () => this.goToSlide(index));
            dotsContainer.appendChild(dot);
        });
        this.dots = dotsContainer.querySelectorAll('.dot');
    }

    goToSlide(index) {
        this.currentIndex = index;
        const itemWidth = this.items[0].offsetWidth;
        const gap = 20; // Gap entre os itens
        const scrollPosition = (itemWidth + gap) * index;

        this.container.scrollTo({
            left: scrollPosition,
            behavior: 'smooth'
        });

        this.updateDots();
    }

    next() {
        this.currentIndex = (this.currentIndex + 1) % this.items.length;
        this.goToSlide(this.currentIndex);
    }

    prev() {
        this.currentIndex = (this.currentIndex - 1 + this.items.length) % this.items.length;
        this.goToSlide(this.currentIndex);
    }

    updateDots() {
        this.dots.forEach((dot, index) => {
            dot.classList.toggle('active', index === this.currentIndex);
        });
    }

    startAutoplay() {
        this.autoplayInterval = setInterval(() => this.next(), this.autoplayDelay);
    }

    stopAutoplay() {
        if (this.autoplayInterval) {
            clearInterval(this.autoplayInterval);
            this.autoplayInterval = null;
        }
    }
}

// Inicializar o carousel quando o DOM estiver pronto
document.addEventListener('DOMContentLoaded', () => {
    const carouselContainer = document.getElementById('carousel');
    if (carouselContainer) {
        new Carousel(carouselContainer);
    }
});