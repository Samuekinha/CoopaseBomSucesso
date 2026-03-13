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
                Toast.fire({
                  icon: "error",
                  title: "Erro ao recarregar a lista!"
                });
            }
        });
    });
});

