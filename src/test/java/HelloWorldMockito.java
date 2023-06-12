import br.com.alura.leilao.dao.LeilaoDao;
import br.com.alura.leilao.model.Leilao;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.mock;

public class HelloWorldMockito {

    @Test
    void hello() {
        //Umas das formas de criar um mock de uma classe
        LeilaoDao mock = mock(LeilaoDao.class);
        // Não executa essa busca só simula, pois, é um mock
        List<Leilao> todos = mock.buscarTodos();
        Assertions.assertTrue(todos.isEmpty());
    }
}
