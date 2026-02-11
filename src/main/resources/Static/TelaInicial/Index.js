async function handleFormSubmit(event) {
    event.preventDefault()

    const form = event.target
    const formData = new FormData(form)

    try {
        const response = await fetch(form.action, {
            method: 'POST',
            body: formData,
            headers: {
                'Accept': 'application/json'
            }
        })

        showToastMessage(response)
        window.location.reload()
    } catch (error) {
        console.error('Erro:', error)
    }
}

function showToastMessage(response) {
    const Toast = Swal.mixin({
        toast: true,
        position: "top-end",
        showConfirmButton: false,
        timer: 2500,
        timerProgressBar: true,
        didOpen: (toast) => {
            toast.onmouseenter = Swal.stopTimer
            toast.onmouseleave = Swal.resumeTimer
        }
    })

    const mensagemErro = response.headers.get('X-Mensagem-Erro')
    const mensagemSucesso = response.headers.get('X-Mensagem-Sucesso')
    const mensagemInfo = response.headers.get('X-Mensagem-Info')

    if (mensagemSucesso) {
        Toast.fire({ icon: "success", title: mensagemSucesso })
    } else if (mensagemErro) {
        Toast.fire({ icon: "error", title: mensagemErro })
    } else if (mensagemInfo) {
        Toast.fire({ icon: "info", title: mensagemInfo })
    }
}

document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('seuFormulario')
    if (form) {
        form.addEventListener('submit', handleFormSubmit)
    }
})
