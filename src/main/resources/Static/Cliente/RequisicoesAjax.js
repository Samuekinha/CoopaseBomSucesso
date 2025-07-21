document.addEventListener('DOMContentLoaded', function() {
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

  // Seleciona todos os links dos blocos
  const blocosLinks = document.querySelectorAll('.bloco-link');

  // Adiciona o event listener para cada link
  blocosLinks.forEach(link => {
    link.addEventListener('click', function(e) {
      e.preventDefault(); // Previne o comportamento padrão do link

      const action = this.getAttribute('data-action');
      loadView(action);
    });
  });

  // Função para carregar a view via AJAX
  function loadView(viewName) {
    const contentContainer = document.getElementById('dynamic-content');

    // Mostra um indicador de carregamento (opcional)
    contentContainer.innerHTML = '<p>Carregando...</p>';

    // Converte o nome da view para o padrão do seu sistema (ex: "Cadastrar" -> "CadastrarClienteView")
    const viewFileName = `/Coopase/Cliente/${viewName}ClienteView`;

    // Faz a requisição AJAX
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
        // Insere o conteúdo no container
        contentContainer.innerHTML = html;
      })
      .catch(error => {
        console.error('Erro ao carregar a view:', error);
        contentContainer.innerHTML = `<p>Erro ao carregar a view: ${error.message}</p>`;
      });
  }

  // Carrega a view inicial (opcional)
  //  loadView('Cadastrar');
});

$(document).ready(function() {
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
    $('#toggleCooperados').change(function() {
        const showAll = this.checked;

        // Faz a requisição AJAX
        $.ajax({
            url: '/Coopase/Cliente/Servicos',
            type: 'GET',
            data: { toggleCooperados: showAll },
            success: function(data) {
                // Procura pela tabela na resposta HTML
                const newTable = $(data).find('#listaDeCooperados').first();

                // Substitui a tabela atual pela nova
                if (newTable.length > 0) {
                    $('#listaDeCooperados').replaceWith(newTable);

                    Toast.fire({
                      icon: "success",
                      title: showAll ? "Lista completa carregada!" : "Lista reduzida carregada!"
                    });
                } else {
                    Toast.fire({
                      icon: "warning",
                      title: "Tabela não encontrada na resposta!"
                    });
                }
            },
            error: function(xhr, status, error) {
                console.error('Erro AJAX:', error);
                Toast.fire({
                  icon: "error",
                  title: "Erro ao recarregar a lista!"
                });
            }
        });
    });
});