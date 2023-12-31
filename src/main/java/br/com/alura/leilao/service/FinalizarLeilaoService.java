package br.com.alura.leilao.service;

import br.com.alura.leilao.dao.LeilaoDao;
import br.com.alura.leilao.model.Lance;
import br.com.alura.leilao.model.Leilao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FinalizarLeilaoService {

	/* Boa prática: A injeção de dependência,
	 sempre é feita através do construtor
	 Porque além de deixar explicito
	 que a classe depende daquele construtor
	 é possível passar um mock como parâmetro
	 para o teste
	 */

    private final LeilaoDao leiloes;
    private final EnviadorDeEmails enviadorDeEmails;

    @Autowired
    public FinalizarLeilaoService(LeilaoDao leiloes, EnviadorDeEmails enviadorDeEmails) {
        this.leiloes = leiloes;
        this.enviadorDeEmails = enviadorDeEmails;
    }


    public void finalizarLeiloesExpirados() {
        List<Leilao> expirados = leiloes.buscarLeiloesExpirados();
        expirados.forEach(leilao -> {
            Lance maiorLance = maiorLanceDadoNoLeilao(leilao);
            leilao.setLanceVencedor(maiorLance);
            leilao.fechar();
            leiloes.salvar(leilao);
            enviadorDeEmails.enviarEmailVencedorLeilao(maiorLance);
        });
    }

    private Lance maiorLanceDadoNoLeilao(Leilao leilao) {

        List<Lance> lancesDoLeilao = new ArrayList<>(leilao.getLances());
        lancesDoLeilao.sort((lance1, lance2) -> {
            return lance2.getValor().compareTo(lance1.getValor());
        });
        return lancesDoLeilao.get(0);
    }

}
