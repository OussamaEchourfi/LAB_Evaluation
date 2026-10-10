package ma.projet.service;

import static org.junit.jupiter.api.Assertions.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ma.projet.classes.Employe;
import ma.projet.classes.EmployeTache;
import ma.projet.classes.Projet;
import ma.projet.classes.Tache;

public class GestionProjetServiceTest {

    private static EmployeService employeService;
    private static ProjetService projetService;
    private static TacheService tacheService;
    private static EmployeTacheService employeTacheService;

    @BeforeAll
    public static void setUp() {
        employeService = new EmployeService();
        projetService = new ProjetService();
        tacheService = new TacheService();
        employeTacheService = new EmployeTacheService();
    }

    @Test
    public void testCreateEmployeEtProjet() {
        Employe emp = new Employe("El Mansouri", "Hassan", "0600000001");
        assertTrue(employeService.create(emp));
        assertTrue(emp.getId() > 0);

        Projet proj = new Projet("Application ERP", new Date(), new Date(), emp);
        assertTrue(projetService.create(proj));
        assertTrue(proj.getId() > 0);

        Projet foundProj = projetService.findById(proj.getId());
        assertNotNull(foundProj);
        assertEquals("Application ERP", foundProj.getNom());
    }

    @Test
    public void testProjetsGeresParEmploye() {
        Employe chef = new Employe("Rami", "Amine", "0600000002");
        employeService.create(chef);

        Projet p1 = new Projet("Projet Alpha", new Date(), new Date(), chef);
        Projet p2 = new Projet("Projet Beta", new Date(), new Date(), chef);
        projetService.create(p1);
        projetService.create(p2);

        List<Projet> geres = employeService.getProjetsGeres(chef);
        assertNotNull(geres);
        assertTrue(geres.size() >= 2);
    }

    @Test
    public void testTachesPlanifieesEtRealisees() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Employe emp = new Employe("Chraibi", "Salma", "0600000003");
        employeService.create(emp);

        Projet proj = new Projet("Plateforme E-learning", sdf.parse("2024-01-01"), sdf.parse("2024-12-31"), emp);
        projetService.create(proj);

        Tache t1 = new Tache("Architecture", sdf.parse("2024-02-01"), sdf.parse("2024-02-28"), 2500.0, proj);
        Tache t2 = new Tache("Integration", sdf.parse("2024-03-01"), sdf.parse("2024-03-31"), 1800.0, proj);
        tacheService.create(t1);
        tacheService.create(t2);

        // Vérification des tâches planifiées
        List<Tache> planifiees = projetService.getTachesPlanifiees(proj);
        assertNotNull(planifiees);
        assertTrue(planifiees.size() >= 2);

        // Réalisation réelle
        EmployeTache et1 = new EmployeTache(emp, t1, sdf.parse("2024-02-05"), sdf.parse("2024-02-25"));
        assertTrue(employeTacheService.create(et1));

        // Vérification des tâches réalisées par projet
        List<EmployeTache> realiseesParProjet = projetService.getTachesRealiseesParProjet(proj);
        assertNotNull(realiseesParProjet);
        assertTrue(realiseesParProjet.size() >= 1);

        // Vérification de la méthode d'affichage sans exception
        assertDoesNotThrow(() -> projetService.afficherTachesRealiseesAvecDates(proj));
        assertDoesNotThrow(() -> employeService.afficherTachesRealisees(emp));
    }

    @Test
    public void testNamedQueryTachesPrixSuperieurA1000() {
        Employe emp = new Employe("Naciri", "Youssef", "0600000004");
        employeService.create(emp);

        Projet proj = new Projet("Projet Cloud", new Date(), new Date(), emp);
        projetService.create(proj);

        Tache tCher = new Tache("Migration Cloud", new Date(), new Date(), 4000.0, proj);
        Tache tMoinsCher = new Tache("Documentation", new Date(), new Date(), 500.0, proj);
        tacheService.create(tCher);
        tacheService.create(tMoinsCher);

        List<Tache> cheres = tacheService.getTachesPrixSuperieurA1000();
        assertNotNull(cheres);
        for (Tache t : cheres) {
            assertTrue(t.getPrix() > 1000.0, "Le prix de la tâche doit être > 1000 DH");
        }
    }

    @Test
    public void testTachesRealiseesEntreDates() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Employe emp = new Employe("Tahiri", "Nadia", "0600000005");
        employeService.create(emp);

        Projet proj = new Projet("Refonte SI", new Date(), new Date(), emp);
        projetService.create(proj);

        Tache t = new Tache("Sécurité", sdf.parse("2024-05-01"), sdf.parse("2024-05-31"), 3000.0, proj);
        tacheService.create(t);

        EmployeTache et = new EmployeTache(emp, t, sdf.parse("2024-05-10"), sdf.parse("2024-05-20"));
        employeTacheService.create(et);

        List<EmployeTache> list = tacheService.getTachesRealiseesEntreDates(
                sdf.parse("2024-05-01"),
                sdf.parse("2024-05-31")
        );
        assertNotNull(list);
        boolean found = list.stream().anyMatch(item -> item.getTache().getId() == t.getId());
        assertTrue(found, "La tâche réalisée doit être retrouvée entre les deux dates");
    }
}
