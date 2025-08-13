package com.example.moinho.Model.Response;

public class CrudResponse {
    private final StatusResponse status;
    private final String mensagem;

    public enum StatusResponse {
        SUCCESS, ERROR, INFO
    }

    // Construtor PRIVADO
    private CrudResponse(StatusResponse status, String mensagem) {
        this.status = status;
        this.mensagem = mensagem;
    }

    // Métodos factory
    public static CrudResponse success(String mensagem) {
        return new CrudResponse(StatusResponse.SUCCESS, mensagem);
    }

    public static CrudResponse error(String mensagem) {
        return new CrudResponse(StatusResponse.ERROR, mensagem);
    }

    public static CrudResponse info(String mensagem) {
        return new CrudResponse(StatusResponse.INFO, mensagem);
    }

    // Getters
    public StatusResponse getStatus() {
        return status;
    }

    public String getMensagem() {
        return mensagem;
    }

    // Método para obter status como string lowercase
    public String getStatusAsString() {
        return status.name().toLowerCase();
    }
}