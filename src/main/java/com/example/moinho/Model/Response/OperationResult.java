package com.example.moinho.Model.Response;

import lombok.Getter;

@Getter
public class OperationResult {
    // Getters
    private final StatusResponse status;
    private final String mensagem;

    public enum StatusResponse {
        SUCCESS, ERROR, INFO
    }

    // Construtor PRIVADO
    private OperationResult(StatusResponse status, String mensagem) {
        this.status = status;
        this.mensagem = mensagem;
    }

    // Métodos factory
    public static OperationResult success(String mensagem) {
        return new OperationResult(StatusResponse.SUCCESS, mensagem);
    }

    public static OperationResult error(String mensagem) {
        return new OperationResult(StatusResponse.ERROR, mensagem);
    }

    public static OperationResult info(String mensagem) {
        return new OperationResult(StatusResponse.INFO, mensagem);
    }

    // Metodo para obter status como string lowercase
    public String getStatusAsString() {
        return status.name().toLowerCase();
    }

    // Métodos de verificação de status
    public boolean isError() {
        return status == StatusResponse.ERROR;
    }

    public boolean isSuccess() {
        return status == StatusResponse.SUCCESS;
    }

    public boolean isInfo() {
        return status == StatusResponse.INFO;
    }

    // Metodo para verificar se não é erro (sucesso ou info)
    public boolean isNotError() {
        return !isError();
    }
}