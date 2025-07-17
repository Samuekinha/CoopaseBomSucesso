package com.example.moinho.Util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FormatadorUtil {

    public static String formatarDocumento(String documento) {
        if (documento == null || documento.trim().isEmpty()) {
            return "";
        }

        String docNumerico = documento.replaceAll("[^0-9]", "");

        if (docNumerico.length() == 11) {
            return docNumerico.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
        } else if (docNumerico.length() == 14) {
            return docNumerico.replaceAll("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})", "$1.$2.$3/$4-$5");
        }

        return documento; // Retorna original se não for CPF/CNPJ válido
    }

    public static String formatarData(LocalDate data) {
        if (data == null) return "";
        return DateTimeFormatter.ofPattern("dd/MM/yyyy").format(data);
    }

}
