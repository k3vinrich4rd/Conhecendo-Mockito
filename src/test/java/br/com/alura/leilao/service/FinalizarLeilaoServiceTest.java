package br.com.alura.leilao.service;

import br.com.alura.leilao.dao.LeilaoDao;
import br.com.alura.leilao.model.Lance;
import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

class FinalizarLeilaoServiceTest {

    private FinalizarLeilaoService service;
    @Mock //Outra forma de criar um mock
    //Porém, isso não é feito de uma forma automática
    private LeilaoDao leilaoDao;

    @BeforeEach
    public void inicializarMocks() {
        //MockitoAnnotations — lê as anotações do mockito
        //This (este) — se refere a própria classe de teste
        MockitoAnnotations.initMocks(this); //initMocks inicializa os mocks
        this.service = new FinalizarLeilaoService(leilaoDao);
    }

    @Test
    void deveriaFinalizarUmLeilao() {
        List<Leilao> leilaos = leiloes();
        service = new FinalizarLeilaoService(leilaoDao);
    }

    private List<Leilao> leiloes() {
        List<Leilao> lista = new ArrayList<>();

        Leilao leilao = new Leilao("Celular",
                new BigDecimal("500"),
                new Usuario("Fulano"));

        Lance primeiro = new Lance(new Usuario("Beltrano"),
                new BigDecimal("600"));
        Lance segundo = new Lance(new Usuario("Ciclano"),
                new BigDecimal("900"));

        leilao.propoe(primeiro);
        leilao.propoe(segundo);

        lista.add(leilao);

        return lista;
    }
}