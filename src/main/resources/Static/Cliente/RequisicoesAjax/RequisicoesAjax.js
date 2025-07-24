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

    const viewFileName = `/Coopase/Cliente/${viewName}ClienteView`;

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

// ========== jQuery Ready ========== //
$(document).ready(function () {
  configurarPesquisa(); // inicializa na primeira carga
});

// ========== Pesquisa Cliente ========== //
function configurarPesquisa() {
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

  // Remove eventos anteriores para evitar múltiplos binds
  $('.filtros-form').off('submit').on('submit', function (e) {
    e.preventDefault();
    realizarPesquisa();
  });

  $('#clearPesquisar').off('click').on('click', function () {
    $('.filtros-form')[0].reset();
    realizarPesquisa();
  });

  $('#pesquisaPorNome').off('input').on('input', debounce(function () {
    realizarPesquisa();
  }, 300));
}

function realizarPesquisa() {
  const formData = $('.filtros-form').serialize();

  $('#listaDeClientesSelecinavel tbody').html(
    '<tr><td colspan="7" class="text-center">' +
    '<div class="spinner-border text-primary" role="status">' +
    '<span class="visually-hidden">Carregando...</span></div></td></tr>'
  );

  $.ajax({
    url: '/Coopase/Cliente/ConsultarPorPesquisa',
    type: 'GET',
    data: formData,
    success: function (data) {
      $('#listaDeClientesSelecinavel tbody').replaceWith(data);

      Toast.fire({
          icon: "success",
          title: "Resultados atualizados!"
      });

      if (typeof setupTableEvents === 'function') {
        setupTableEvents();
      }
    },
    error: function () {
      Toast.fire({
        icon: "error",
        title: "erro na pesquisa!"
    });

      $('#listaDeClientesSelecinavel tbody').html(
        '<tr><td colspan="7" class="text-center text-danger">Erro ao carregar dados</td></tr>'
      );
    }
  });
}

// ========== Função debounce ========== //
function debounce(func, wait) {
  let timeout;
  return function () {
    const context = this, args = arguments;
    clearTimeout(timeout);
    timeout = setTimeout(() => func.apply(context, args), wait);
  };
}
