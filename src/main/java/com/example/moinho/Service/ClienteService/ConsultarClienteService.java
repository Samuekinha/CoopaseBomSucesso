package com.example.moinho.Service.ClienteService;

import com.example.moinho.Model.E_Cliente;
import com.example.moinho.Repository.R_Cliente;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ConsultarClienteService {

    private final R_Cliente r_cliente;

    public ConsultarClienteService(R_Cliente r_cliente) {
        this.r_cliente = r_cliente;
    }

    public List<E_Cliente> consultarCliente() {
        return r_cliente.findAllOrderById();
    }

    public List<E_Cliente> consultarComFiltros(String pesquisaPorNome, String pesquisaPorDocumento,
           String pesquisaPorCodCaf, Boolean apenasCooperados, boolean deZaA) {
        if (deZaA) {
            return r_cliente.findAllWithFiltersDesc(pesquisaPorNome, pesquisaPorDocumento,
                    pesquisaPorCodCaf, apenasCooperados);

        } else {
            return r_cliente.findAllWithFiltersAsc(pesquisaPorNome, pesquisaPorDocumento,
                    pesquisaPorCodCaf, apenasCooperados);
        }

    }

    public List<E_Cliente> consultar10Cooperados() {
        return r_cliente.findCooperatedsLimited(10);
    }

    public List<E_Cliente> consultarTodosCooperados() {
        return r_cliente.findAllCooperateds();
    }

    public List<E_Cliente> consultarVendedoresLimitados() {
        return r_cliente.findSellersLimited(5);
    }

    public List<E_Cliente> consultarVendedores() {
        return r_cliente.findSellersLimited(5);
    }

    public int[] consultarInfosProcessadasCooperados() {
        List<E_Cliente> todosCooperados = consultarTodosCooperados();

        int qtdeCoop = 0;
        int qtdeDapAtiva = 0;


        for (int i = 0; i < todosCooperados.toArray().length ; i++) {
            LocalDate maturityCaf = todosCooperados.get(i).getMaturity_caf();
            if (maturityCaf != null) {
                if (maturityCaf.isAfter(LocalDate.now())) {
                    qtdeDapAtiva++;
                }
            }
            qtdeCoop++;
        }

        int[] resultados = {qtdeCoop, qtdeDapAtiva};

        return resultados;
    }

    public List<E_Cliente> consultarOperadores() {
        return r_cliente.findAllOperadores();
    }

        public Integer consultarQuantidadeClientes() {
        List<E_Cliente> listaCompleta = r_cliente.findAll();
        return listaCompleta.size();
    }

}
