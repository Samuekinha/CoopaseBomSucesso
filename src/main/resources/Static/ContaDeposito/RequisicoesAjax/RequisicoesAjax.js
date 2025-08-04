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

    const viewFileName = `/Coopase/Cliente/${viewName}ContaDView`;

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

        // ✅ Reexecuta scripts específicos da view carregada
        if (viewName === 'Consultar') {
          configurarPesquisa(); // reinicializa eventos da pesquisa
        }
        if (viewName === 'Deletar') {
            configurarPesquisa(); // reinicializa eventos da pesquisa
        }
        if (viewName === 'Editar') {
              configurarPesquisa(); // reinicializa eventos da pesquisa
        }

        // 🔁 Reexecuta scripts gerais (se existir)
        if (typeof setupTableEvents === 'function') {
          setupTableEvents();
        }

        // ✅ Dispara evento customizado se precisar de mais controle
        $(document).trigger("viewLoaded");
      })
      .catch(error => {
        console.error('Erro ao carregar a view:', error);
        contentContainer.innerHTML = `<p>Erro ao carregar a view: ${error.message}</p>`;
      });
  }
});

