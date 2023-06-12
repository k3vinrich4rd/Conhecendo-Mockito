package br.com.alura.leilao.service;

import br.com.alura.leilao.dao.PagamentoDao;
import br.com.alura.leilao.model.Lance;
import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

class GeradorDePagamentoTest {

    private GeradorDePagamento gerador;
    @Mock
    private PagamentoDao pagamentoDao;

    @BeforeEach
    public void inicializarMocks() {
        MockitoAnnotations.initMocks(this);
        this.gerador = new GeradorDePagamento(pagamentoDao);
    }

    @Test
    void deveriaCriarUmPagamentoParaVencedorLeilao() {
        Leilao leilaos = leiloes();
        Lance lanceVencedor = leilaos.getLanceVencedor();
        gerador.gerarPagamento(lanceVencedor);
        //Para verificar um objeto criado
        //na classe que está sendo testada
        //Mockito.verify(pagamentoDao).salvar(Mockito.c);
    }

    private Leilao leiloes() {
        Leilao leilao = new Leilao("Celular",
                new BigDecimal("500"),
                new Usuario("Fulano"));

        Lance lance = new Lance(new Usuario("Ciclano"),
                new BigDecimal("900"));
        leilao.propoe(lance);
        leilao.setLanceVencedor(lance);
        return leilao;
    }
}