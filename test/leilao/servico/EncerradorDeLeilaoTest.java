package leilao.servico;

import builder.CriadorDeLeilao;
import leilao.dao.LeilaoDao;
import leilao.dominio.Leilao;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EncerradorDeLeilaoTest {

    @Test
    public void deveEncerrarLeiloesQueComecaramUmaSemanaAntes() {
        Calendar antiga = Calendar.getInstance();
        antiga.set(1999, 1, 20);

        Leilao leilao1 = new CriadorDeLeilao().para("Playstation 3").naData(antiga).constroi();
        Leilao leilao2 = new CriadorDeLeilao().para("Xbox").naData(antiga).constroi();
        List<Leilao> leiloesAntigos = new ArrayList<>();
        leiloesAntigos.add(leilao1);
        leiloesAntigos.add(leilao2);

        LeilaoDao daoFalso = mock(LeilaoDao.class);

        when(daoFalso.correntes()).thenReturn(leiloesAntigos);

        EncerradorDeLeilao encerrador = new EncerradorDeLeilao(daoFalso);
        encerrador.encerra();

        assertThat(encerrador.getTotalEncerrados(), equalTo(2));
        assertTrue(leilao1.isEncerrado());
        assertTrue(leilao2.isEncerrado());
    }
}
