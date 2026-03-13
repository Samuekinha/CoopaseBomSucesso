export function formatarDataParaInput(dataStr) {
    if (!dataStr || dataStr.trim() === '') return '';

    // Remove qualquer formatação existente
    const dataLimpa = dataStr.replace(/\D/g, '');

    // Se não tiver números suficientes, retorna vazio
    if (dataLimpa.length < 8) return '';

    // Formata para yyyy-MM-dd (padrão do input date)
    const dia = dataLimpa.substring(0, 2);
    const mes = dataLimpa.substring(2, 4);
    const ano = dataLimpa.substring(4, 8);

    // Validação básica (opcional)
    if (dia > 31 || mes > 12) return '';

    return `${ano}-${mes}-${dia}`;
}