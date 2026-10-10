package ma.projet.service;

import static org.junit.jupiter.api.Assertions.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ma.projet.beans.Femme;
import ma.projet.beans.Homme;
import ma.projet.beans.Mariage;

public class EtatCivilServiceTest {

    private static HommeService hommeService;
    private static FemmeService femmeService;
    private static MariageService mariageService;

    @BeforeAll
    public static void setUp() {
        hommeService = new HommeService();
        femmeService = new FemmeService();
        mariageService = new MariageService();
    }

    @Test
    public void testCreateHommeEtFemme() {
        Homme h = new Homme("TestNomH", "TestPrenomH", "0600000001", "Rabat", new Date());
        assertTrue(hommeService.create(h));
        assertTrue(h.getId() > 0);

        Femme f = new Femme("TestNomF", "TestPrenomF", "0600000002", "Casablanca", new Date());
        assertTrue(femmeService.create(f));
        assertTrue(f.getId() > 0);

        Homme foundH = hommeService.findById(h.getId());
        assertNotNull(foundH);
        assertEquals("TestNomH", foundH.getNom());

        Femme foundF = femmeService.findById(f.getId());
        assertNotNull(foundF);
        assertEquals("TestNomF", foundF.getNom());
    }

    @Test
    public void testMariageEtEpousesEntreDates() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Homme h = new Homme("TestH2", "Hassan", "0600000003", "Fes", sdf.parse("1980-01-01"));
        hommeService.create(h);

        Femme f1 = new Femme("TestF1", "Amina", "0600000004", "Fes", sdf.parse("1985-05-05"));
        femmeService.create(f1);

        Mariage m = new Mariage(h, f1, sdf.parse("2010-06-01"), null, 2);
        assertTrue(mariageService.create(m));

        List<Femme> epouses = hommeService.getEpousesEntreDates(
                h.getId(),
                sdf.parse("2010-01-01"),
                sdf.parse("2010-12-31")
        );
        assertNotNull(epouses);
        assertTrue(epouses.stream().anyMatch(e -> e.getId() == f1.getId()));
    }

    @Test
    public void testNamedNativeQueryCountEnfants() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Homme h = new Homme("TestH3", "Youssef", "0600000005", "Tanger", sdf.parse("1978-01-01"));
        hommeService.create(h);

        Femme f = new Femme("TestF_Mere", "Khadija", "0600000006", "Tanger", sdf.parse("1982-01-01"));
        femmeService.create(f);

        Mariage m = new Mariage(h, f, sdf.parse("2005-02-10"), null, 3);
        mariageService.create(m);

        int count = femmeService.countEnfantsBetweenDates(
                f.getId(),
                sdf.parse("2000-01-01"),
                sdf.parse("2010-12-31")
        );
        assertEquals(3, count);
    }

    @Test
    public void testFemmePlusAgee() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Femme fDoyenne = new Femme("Doyenne", "Aicha", "0600000007", "Rabat", sdf.parse("1930-01-01"));
        femmeService.create(fDoyenne);

        Femme plusAgee = femmeService.getFemmePlusAgee();
        assertNotNull(plusAgee);
        assertNotNull(plusAgee.getDateNaissance());
        assertTrue(plusAgee.getDateNaissance().before(sdf.parse("1940-01-01")));
    }

    @Test
    public void testCriteriaHommesMariesQuatreFemmes() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Homme h = new Homme("Polygame", "Ahmed", "0600000008", "Marrakech", sdf.parse("1960-01-01"));
        hommeService.create(h);

        Femme w1 = new Femme("W1", "P1", "0600000009", "M", sdf.parse("1970-01-01"));
        Femme w2 = new Femme("W2", "P2", "0600000010", "M", sdf.parse("1971-01-01"));
        Femme w3 = new Femme("W3", "P3", "0600000011", "M", sdf.parse("1972-01-01"));
        Femme w4 = new Femme("W4", "P4", "0600000012", "M", sdf.parse("1973-01-01"));
        femmeService.create(w1);
        femmeService.create(w2);
        femmeService.create(w3);
        femmeService.create(w4);

        mariageService.create(new Mariage(h, w1, sdf.parse("1991-01-01"), null, 1));
        mariageService.create(new Mariage(h, w2, sdf.parse("1992-01-01"), null, 2));
        mariageService.create(new Mariage(h, w3, sdf.parse("1993-01-01"), null, 0));
        mariageService.create(new Mariage(h, w4, sdf.parse("1994-01-01"), null, 1));

        long count = hommeService.countHommesMariesQuatreFemmesEntreDates(
                sdf.parse("1990-01-01"),
                sdf.parse("1995-12-31")
        );
        assertTrue(count >= 1);
    }
}
