package com.example.moinho.Dto.Conta;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ContaRequestDTO {

    @NotBlank(message = "O nome da conta é obrigatório")
    private String nomeConta;

    @NotBlank(message = "O tipo da conta é obrigatório")
    private String tipoConta;
}
