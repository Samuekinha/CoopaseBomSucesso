package com.example.moinho.Dto.Diaristas;

import com.example.moinho.Entity.Transacao;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DiaristaRequestDTO {
    @NotNull(message = "Para cadastrar um diarista é necessário selecionar um cadastro!")
    private Long diaristaId;
}
