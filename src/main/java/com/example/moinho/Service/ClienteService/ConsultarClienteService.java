package com.example.moinho.Service.ClienteService;

import com.example.moinho.Entity.Pessoa.Papel;
import com.example.moinho.Entity.Pessoa.PessoaBase;
import com.example.moinho.Entity.Pessoa.PessoaFisica;
import com.example.moinho.Repository.Pessoa;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ConsultarClienteService {

    private final Pessoa pessoa;

    public ConsultarClienteService(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public List<PessoaBase> consultarCliente() {
        return pessoa.findAll();
    }

    public List<PessoaFisica> consultarComFiltros(String pesquisaPorNome, String pesquisaPorDocumento,
                                                                   String pesquisaPorCodCaf, Boolean apenasCooperados, boolean deZaA) {
        if (deZaA) {
            return pessoa.findTodosComFiltrosDesc(pesquisaPorNome, pesquisaPorDocumento,
                    pesquisaPorCodCaf, apenasCooperados);

        } else {
            return pessoa.findTodosComFiltrosAsc(pesquisaPorNome, pesquisaPorDocumento,
                    pesquisaPorCodCaf, apenasCooperados);
        }

    }

    public List<PessoaFisica> consultar10Cooperados() {
        return pessoa.findPorPapelLimitados(Papel.COOPERADO, PageRequest.of(0, 10));
    }

    public List<PessoaFisica> consultarTodosCooperados() {
        return pessoa.findPorPapel(Papel.COOPERADO);
    }

    public List<PessoaFisica> consultarVendedoresLimitados() {
        return pessoa.findPorPapelLimitados(Papel.VENDEDOR,PageRequest.of(0, 5));
    }

    public List<PessoaFisica> consultarVendedores() {
        return pessoa.findPorPapel(Papel.VENDEDOR);
    }

    public int[] consultarInfosProcessadasCooperados() {
        List<PessoaFisica> todosCooperados = consultarTodosCooperados();

        int qtdeCoop = 0;
        int qtdeDapAtiva = 0;


        if (todosCooperados instanceof PessoaFisica pf) {
            for (int i = 0; i < todosCooperados.toArray().length ; i++) {
                LocalDate maturityCaf = pf.getValidade_caf_fisica();
                if (maturityCaf != null) {
                    if (maturityCaf.isAfter(LocalDate.now())) {
                        qtdeDapAtiva++;
                    }
                }
                qtdeCoop++;
            }
        }

        int[] resultados = {qtdeCoop, qtdeDapAtiva};

        return resultados;
    }

    public List<PessoaFisica> consultarOperadores() {
        return pessoa.findPorPapel(Papel.OPERADOR);
    }

        public Integer consultarQuantidadeClientes() {
        List<PessoaBase> listaCompleta = pessoa.findAll();
        return listaCompleta.size();
    }

    public List<PessoaFisica> ConsultarClienteNaoDiarista() {
        return pessoa.findSemPapel(Papel.DIARISTA, PageRequest.of(0,5));
    }
}
