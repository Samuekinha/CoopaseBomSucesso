package com.example.moinho.Entity.ContaDeposito;

import com.example.moinho.Entity.Transacao;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Entity
@DiscriminatorValue("FISICO")
@Data
public class ContaFisica extends ContaBase {

    public enum TipoCofreFisico {
        DINHEIRO, // recebe dinheiro e cheque
        MOEDA     // recebe apenas moedas
    }

    @Enumerated(EnumType.STRING)
    private TipoCofreFisico tipo_cofre;

    public boolean aceitaValor(Transacao.TypeMoney tipo) {
        return switch (tipo_cofre) {
            case DINHEIRO -> tipo == Transacao.TypeMoney.DINHEIRO
                    || tipo == Transacao.TypeMoney.CHEQUE;
            case MOEDA -> tipo == Transacao.TypeMoney.MOEDA;
        };
    }
}
