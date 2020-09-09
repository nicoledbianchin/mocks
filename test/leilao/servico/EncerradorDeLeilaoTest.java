package leilao.servico;

import builder.CriadorDeLeilao;
import leilao.dao.LeilaoDaoFalso;
import leilao.dominio.Leilao;
import org.junit.Test;

import java.util.Calendar;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertTrue;

public class EncerradorDeLeilaoTest {

    @Test
    public void deveEncerrarLeiloesQueComecaramUmaSemanaAntes() {
        Calendar antiga = Calendar.getInstance();
        antiga.set(1999, 1, 20);

        Leilao leilao1 = new CriadorDeLeilao().para("Playstation 3").naData(antiga).constroi();
        Leilao leilao2 = new CriadorDeLeilao().para("Xbox").naData(antiga).constroi();

        LeilaoDaoFalso leilaoDao = new LeilaoDaoFalso();
        leilaoDao.salva(leilao1);
        leilaoDao.salva(leilao2 );

        EncerradorDeLeilao encerrador = new EncerradorDeLeilao();
        encerrador.encerra();

        List<Leilao> encerrados = leilaoDao.encerrados();

        assertThat(encerrados.size(), equalTo(2));
        assertTrue(encerrados.get(0).isEncerrado());
        assertTrue(encerrados.get(1).isEncerrado());
    }
}
