package br.com.alura.leilao.service;

import br.com.alura.leilao.dao.PagamentoDao;
import br.com.alura.leilao.model.Lance;
import br.com.alura.leilao.model.Pagamento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class GeradorDePagamento {
    private PagamentoDao pagamentos;

    @Autowired
    public GeradorDePagamento(PagamentoDao pagamento) {
        this.pagamentos = pagamento;
    }

    public void gerarPagamento(Lance lanceVencedor) {
        LocalDate vencimento = LocalDate.now().plusDays(1);
        Pagamento pagamento = new Pagamento(lanceVencedor, vencimento);
        this.pagamentos.salvar(pagamento);
    }
}
