package br.com.alura.leilao.service;

import br.com.alura.leilao.dao.PagamentoDao;
import br.com.alura.leilao.model.Lance;
import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.model.Pagamento;
import br.com.alura.leilao.model.Usuario;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.time.*;

class GeradorDePagamentoTest {

    private GeradorDePagamento gerador;

    @Mock
    private PagamentoDao pagamentoDao;

    //Para caputurar um determinado objeto
    @Captor
    private ArgumentCaptor<Pagamento> captor;

    @Mock
    private Clock clock;

    @BeforeEach
    public void inicializarMocks() {
        MockitoAnnotations.initMocks(this);
        this.gerador = new GeradorDePagamento(pagamentoDao, clock);

    }

    @Test
    void deveriaCriarUmPagamentoParaVencedorLeilao() {
        Leilao leilaos = leiloes();
        Lance lanceVencedor = leilaos.getLanceVencedor();

        ///Para testar o dia do pagamento
        LocalDate data = LocalDate.of(2023, Month.JUNE, 7);
        Instant instant = data.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Mockito.when(clock.instant()).thenReturn(instant);
        Mockito.when(clock.getZone()).thenReturn(ZoneId.systemDefault());

        gerador.gerarPagamento(lanceVencedor);
        //Esta verificando se o método foi chamado e tá capurando o parametro
        Mockito.verify(pagamentoDao).salvar(captor.capture());
        //Para verificar um objeto criado
        //na classe que está sendo testada
        //Mockito.verify(pagamentoDao).salvar(Mockito.c);
        //devolve o objeto capturado
        Pagamento pagamento = captor.getValue();
        Assertions.assertEquals(LocalDate.now(clock).plusDays(1), pagamento.getVencimento());
        Assertions.assertEquals(lanceVencedor.getValor(), pagamento.getValor());
        Assertions.assertFalse(pagamento.getPago());
        Assertions.assertEquals(lanceVencedor.getUsuario(), pagamento.getUsuario());
        Assertions.assertEquals(leilaos, pagamento.getLeilao());
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