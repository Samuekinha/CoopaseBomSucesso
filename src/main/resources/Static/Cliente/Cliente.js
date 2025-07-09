// Usando delegação de eventos para funcionar com conteúdo dinâmico (CadastrarClienteView)
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

// seleciona uma row e adiciona os dados ao form (EditarClienteView) ------------------------------------------

document.addEventListener('DOMContentLoaded', function() {
    // Função para lidar com a seleção de linhas e preenchimento do formulário
    function setupTableSelection() {
        const table = document.getElementById('listaDeClientesSelecinavel');
        if (!table) return;

        // Ouvinte de eventos delegado para os botões de seleção
        table.addEventListener('click', function(e) {
            const btn = e.target.closest('button.btn-primary');
            if (!btn) return;

            e.preventDefault();
            e.stopPropagation();

            // Remove a seleção de todas as outras linhas
            document.querySelectorAll('#listaDeClientesSelecinavel tr.selected').forEach(row => {
                row.classList.remove('selected');
            });

            // Adiciona a seleção na linha atual
            const row = btn.closest('tr');
            row.classList.add('selected');

            // Preenche o formulário com os dados do botão
            fillFormFromButton(btn);
        });
    }

    // Função para preencher o formulário com os dados do botão
    function fillFormFromButton(button) {
        // Mapeamento dos campos do formulário
        const formInputs = {
            id: document.getElementById('ClientId'),
            name: document.getElementById('ClientName'),
            document: document.getElementById('ClientDocument'),
            birthDate: document.getElementById('ClientBirth'),
            cooperated: document.getElementById('cooperadoSelect'),
            maturity_caf: document.getElementById('ClientCafDate'),
            caf: document.getElementById('ClientCafCode')
        };

        // Preenche os campos básicos
        formInputs.id.value = button.getAttribute('th:idSelect') || '';
        formInputs.name.value = button.getAttribute('th:nomeSelect') || '';
        formInputs.document.value = button.getAttribute('th:documentSelect') || '';

        // Converte e preenche a data de nascimento (dd-MM-yyyy → yyyy-MM-dd)
        const birthDate = button.getAttribute('th:birthDateSelect');
        if (birthDate) {
            const [day, month, year] = birthDate.split('-');
            formInputs.birthDate.value = `${year}-${month}-${day}`;
        }

        // Preenche o campo cooperado
        const isCooperado = button.getAttribute('th:cooperatedSelect') === 'true';
        formInputs.cooperated.value = isCooperado;

        // Mostra/oculta campos de cooperado conforme necessário
        const camposCooperado = document.getElementById('camposCooperado');
        if (isCooperado) {
            camposCooperado.classList.remove('hidden-cooperado');

            // Converte e preenche a data de vencimento CAF
            const maturityDate = button.getAttribute('th:maturityCafSelect');
            if (maturityDate) {
                const [day, month, year] = maturityDate.split('-');
                formInputs.maturity_caf.value = `${year}-${month}-${day}`;
            }

            formInputs.caf.value = button.getAttribute('th:cafSeelct') || '';
        } else {
            camposCooperado.classList.add('hidden-cooperado');
            formInputs.maturity_caf.value = '';
            formInputs.caf.value = '';
        }
    }

    // Configura o evento change para o select de cooperado
    function setupCooperadoSelect() {
        const select = document.getElementById('cooperadoSelect');
        if (!select) return;

        select.addEventListener('change', function() {
            const camposCooperado = document.getElementById('camposCooperado');
            if (!camposCooperado) return;

            if (this.value === 'true') {
                camposCooperado.classList.remove('hidden-cooperado');
            } else {
                camposCooperado.classList.add('hidden-cooperado');
            }
        });

        // Dispara o evento change inicialmente
        select.dispatchEvent(new Event('change'));
    }

    // Inicializa todas as funções
    setupTableSelection();
    setupCooperadoSelect();
});
