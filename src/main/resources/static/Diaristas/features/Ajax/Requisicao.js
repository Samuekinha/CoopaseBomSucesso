document.addEventListener('DOMContentLoaded', function () {
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

  const blocosLinks = document.querySelectorAll('.bloco-link');

  blocosLinks.forEach(link => {
    link.addEventListener('click', function (e) {
      e.preventDefault();
      const action = this.getAttribute('data-action');
      loadView(action);
    });
  });

  function loadView(viewName) {
    const contentContainer = document.getElementById('dynamic-content');
    contentContainer.innerHTML = '<p>Carregando...</p>';

    const viewFileName = `/Coopase/Diaristas/${viewName}DiaristaView`;

    fetch(viewFileName)
      .then(response => {
        if (!response.ok) {
          Toast.fire({
            icon: "error",
            title: `Erro ao abrir a view ${viewName}!`
          });
          throw new Error(`View não encontrada: ${viewFileName}`);
        }
        Toast.fire({
          icon: "info",
          title: `A view ${viewName} foi selecionada!`
        });
        return response.text();
      })
      .then(html => {
        contentContainer.innerHTML = html;

        // 🔁 Reexecuta scripts gerais (se existir)
        if (typeof setupTableEvents === 'function') {
          setupTableEvents();
        }
      })
      .catch(error => {
        console.error('Erro ao carregar a view:', error);
        contentContainer.innerHTML = `<p>Erro ao carregar a view: ${error.message}</p>`;
      });
  }
});

