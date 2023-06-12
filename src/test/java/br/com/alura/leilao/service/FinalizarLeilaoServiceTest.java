package br.com.alura.leilao.service;

import br.com.alura.leilao.dao.LeilaoDao;
import br.com.alura.leilao.model.Lance;
import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.model.Usuario;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

class FinalizarLeilaoServiceTest {

    private FinalizarLeilaoService service;
    @Mock //Outra forma de criar um mock
    //Porém, isso não é feito de uma forma automática
    private LeilaoDao leilaoDao;
    @Mock
    private EnviadorDeEmails enviadorDeEmails;

    @BeforeEach
    public void inicializarMocks() {
        //MockitoAnnotations — lê as anotações do mockito
        //This (este) — se refere a própria classe de teste
        MockitoAnnotations.initMocks(this); //initMocks inicializa os mocks
        this.service = new FinalizarLeilaoService(leilaoDao, enviadorDeEmails);
    }

    @Test
    void deveriaFinalizarUmLeilao() {
        List<Leilao> leilaos = leiloes();
        //Mockito.when, maninupula o comportamento
        //com base na condicação definida, e sim retorna algo (thenReturn)
        Mockito.when(leilaoDao.buscarLeiloesExpirados())
                .thenReturn(leilaos);
        service.finalizarLeiloesExpirados();
        Leilao leilao = leilaos.get(0);
        Assertions.assertTrue(leilao.isFechado());
        Assertions.assertEquals(new BigDecimal("900"),
                leilao.getLanceVencedor().getValor());
        //Verify verifica se o método foi chamado e internamente já faz um assert
        Mockito.verify(leilaoDao).salvar(leilao);
    }

    @Test
    void deveriaEnviarUmEmailParaOVencedor() {
        List<Leilao> leilaos = leiloes();
        //Mockito.when, maninupula o comportamento
        //com base na condicação definida, e sim retorna algo (thenReturn)
        Mockito.when(leilaoDao.buscarLeiloesExpirados())
                .thenReturn(leilaos);
        service.finalizarLeiloesExpirados();
        Leilao leilao = leilaos.get(0);
        Lance lanceVencedor = leilao.getLanceVencedor();
        Mockito.verify(enviadorDeEmails).enviarEmailVencedorLeilao(lanceVencedor);
    }


    @Test
    void naoDeveriaEnviarEmailParaVencedorDoLeilaoEmCasoDeException() {
        List<Leilao> leilaos = leiloes();

        //Mockito.when, maninupula o comportamento
        //com base na condicação definida, e sim retorna algo (thenReturn)
        Mockito.when(leilaoDao.buscarLeiloesExpirados())
                .thenReturn(leilaos);

        //thenThow — Garante que o mockito vai lançar uma exception no seu comportamento.
        //Mockito.any — Quando método salvar for chamado, independente do parâmetro
        // a exception vai ser lançada
        Mockito.when(leilaoDao.salvar(Mockito.any())).thenThrow(RuntimeException.class);

        try { //Para caputurar a exception que esta sendo testada
            service.finalizarLeiloesExpirados();
            Mockito.verifyNoInteractions(enviadorDeEmails);
        } catch (Exception ignored) {
        }

        //Verifica se o método não teve interações
        Mockito.verifyNoInteractions(enviadorDeEmails);
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