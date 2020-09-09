package leilao.servico;

import builder.CriadorDeLeilao;
import leilao.dao.LeilaoDao;
import leilao.dominio.Leilao;
import leilao.repositorio.RepositorioDeLeiloes;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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

        RepositorioDeLeiloes daoFalso = mock(RepositorioDeLeiloes.class);
        when(daoFalso.correntes()).thenReturn(leiloesAntigos);

        EncerradorDeLeilao encerrador = new EncerradorDeLeilao(daoFalso);
        encerrador.encerra();

        assertThat(encerrador.getTotalEncerrados(), equalTo(2));
        assertTrue(leilao1.isEncerrado());
        assertTrue(leilao2.isEncerrado());
    }

    @Test
    public void naoDeveEncerrarLeiloesQueComecaramMenosDeUmaSemanaAtras() {
        Calendar ontem = Calendar.getInstance();
        ontem.add(Calendar.DAY_OF_MONTH, -1);

        Leilao leilao1 = new CriadorDeLeilao().para("Playstation 3").naData(ontem).constroi();
        Leilao leilao2 = new CriadorDeLeilao().para("Xbox").naData(ontem).constroi();
        List<Leilao> leiloesAtuais = new ArrayList<>();
        leiloesAtuais.add(leilao1);
        leiloesAtuais.add(leilao2);

        RepositorioDeLeiloes daoFalso = mock(RepositorioDeLeiloes.class);
        when(daoFalso.correntes()).thenReturn(leiloesAtuais);

        EncerradorDeLeilao encerrador = new EncerradorDeLeilao(daoFalso);
        encerrador.encerra();

        assertThat(encerrador.getTotalEncerrados(), equalTo(0));
        assertFalse(leilao1.isEncerrado());
        assertFalse(leilao2.isEncerrado());
    }

    @Test
    public void naoDeveEncerrarLeiloesCasoNaoHajaNenhum() {
        RepositorioDeLeiloes daoFalso = mock(RepositorioDeLeiloes.class);
        when(daoFalso.correntes()).thenReturn(new ArrayList<>());

        EncerradorDeLeilao encerrador = new EncerradorDeLeilao(daoFalso);
        encerrador.encerra();

        assertThat(encerrador.getTotalEncerrados(), equalTo(0));
    }
}
