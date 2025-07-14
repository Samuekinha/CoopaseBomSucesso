// Verifica a resposta do backend após envio do formulário
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

    if (typeof sucesso !== 'MensagemSucesso' && sucesso !== null) {
        Toast.fire({
                  icon: "success",
                  title: `${MensagemSucesso}`
                });
    } else if (typeof erro !== 'MensagemErro' && erro !== null) {
        Toast.fire({
                  icon: "error",
                  title: `${MensagemErro}`
                });
    } else {
        Toast.fire({
                  icon: "info",
                  title: `$MensagemSemMudanca!`
                });
    }
});