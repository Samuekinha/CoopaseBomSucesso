package com.example.moinho.Entity.ContaDeposito;

import com.example.moinho.Entity.Transacao;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@DiscriminatorValue("VIRTUAL")
@Data
public class ContaVirtual extends ContaBase {

    public boolean aceitaValor(Transacao.TypeMoney tipo) {
        return true; // aceita qualquer tipo: dinheiro, cheque, moeda, pix, cartão
    }
}

