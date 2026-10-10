package ma.projet.test;

import java.text.SimpleDateFormat;
import java.util.Date;
import ma.projet.classes.Employe;
import ma.projet.classes.EmployeTache;
import ma.projet.classes.Projet;
import ma.projet.classes.Tache;
import ma.projet.service.EmployeService;
import ma.projet.service.EmployeTacheService;
import ma.projet.service.ProjetService;
import ma.projet.service.TacheService;
import ma.projet.util.HibernateUtil;

public class TestApp {

    public static void main(String[] args) {
        System.out.println("=================================================================");
        System.out.println("         APPLICATION DE GESTION DE PROJETS - EXERCICE 2          ");
        System.out.println("=================================================================\n");

        EmployeService employeService = new EmployeService();
        ProjetService projetService = new ProjetService();
        TacheService tacheService = new TacheService();
        EmployeTacheService employeTacheService = new EmployeTacheService();

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            // -------------------------------------------------------------
            // 1. Initialisation des données de test
            // -------------------------------------------------------------
            System.out.println(">>> 1. Initialisation des données en base de données...\n");

            // Création des employés
            Employe emp1 = new Employe("Alami", "Karim", "0612345678");
            Employe emp2 = new Employe("Bennani", "Fatima", "0623456789");
            Employe emp3 = new Employe("Idrissi", "Omar", "0634567890");

            employeService.create(emp1);
            employeService.create(emp2);
            employeService.create(emp3);

            // Création des projets
            Date dateDebutProj = sdf.parse("14/01/2013");
            Date dateFinProj = sdf.parse("30/06/2013");
            Projet proj1 = new Projet("Gestion de stock", dateDebutProj, dateFinProj, emp1);
            Projet proj2 = new Projet("Portail Client", sdf.parse("01/06/2013"), sdf.parse("31/12/2013"), emp1);

            projetService.create(proj1);
            projetService.create(proj2);

            // Création des tâches planifiées pour le projet 1
            Tache t1 = new Tache("Analyse", sdf.parse("01/02/2013"), sdf.parse("28/02/2013"), 1500.0, proj1);
            Tache t2 = new Tache("Conception", sdf.parse("01/03/2013"), sdf.parse("31/03/2013"), 2000.0, proj1);
            Tache t3 = new Tache("Développement", sdf.parse("01/04/2013"), sdf.parse("30/04/2013"), 3500.0, proj1);
            Tache t4 = new Tache("Maintenance", sdf.parse("01/05/2013"), sdf.parse("15/05/2013"), 800.0, proj1);

            tacheService.create(t1);
            tacheService.create(t2);
            tacheService.create(t3);
            tacheService.create(t4);

            // Affectation des tâches aux employés avec dates réelles (données conformes au sujet)
            // Analyse : 10/02/2013 au 20/02/2013
            EmployeTache et1 = new EmployeTache(emp2, t1, sdf.parse("10/02/2013"), sdf.parse("20/02/2013"));
            // Conception : 10/03/2013 au 15/03/2013
            EmployeTache et2 = new EmployeTache(emp2, t2, sdf.parse("10/03/2013"), sdf.parse("15/03/2013"));
            // Développement : 10/04/2013 au 25/04/2013
            EmployeTache et3 = new EmployeTache(emp3, t3, sdf.parse("10/04/2013"), sdf.parse("25/04/2013"));

            employeTacheService.create(et1);
            employeTacheService.create(et2);
            employeTacheService.create(et3);

            System.out.println("Données de test créées avec succès !\n");

            // -------------------------------------------------------------
            // 2. Tests EmployeService
            // -------------------------------------------------------------
            System.out.println("=================================================================");
            System.out.println("TEST EMPLOYE 1 : Liste des tâches réalisées par un employé");
            System.out.println("=================================================================");
            employeService.afficherTachesRealisees(emp2);
            System.out.println();

            System.out.println("=================================================================");
            System.out.println("TEST EMPLOYE 2 : Liste des projets gérés par un employé");
            System.out.println("=================================================================");
            employeService.afficherProjetsGeres(emp1);
            System.out.println();

            // -------------------------------------------------------------
            // 3. Tests ProjetService
            // -------------------------------------------------------------
            System.out.println("=================================================================");
            System.out.println("TEST PROJET 1 : Liste des tâches planifiées pour un projet");
            System.out.println("=================================================================");
            projetService.afficherTachesPlanifiees(proj1);
            System.out.println();

            System.out.println("=================================================================");
            System.out.println("TEST PROJET 2 : Liste des tâches réalisées avec les dates réelles");
            System.out.println("=================================================================");
            projetService.afficherTachesRealiseesAvecDates(proj1);
            System.out.println();

            // -------------------------------------------------------------
            // 4. Tests TacheService
            // -------------------------------------------------------------
            System.out.println("=================================================================");
            System.out.println("TEST TACHE 1 : Tâches dont le prix > 1000 DH (Requête nommée)");
            System.out.println("=================================================================");
            tacheService.afficherTachesPrixSuperieurA1000();
            System.out.println();

            System.out.println("=================================================================");
            System.out.println("TEST TACHE 2 : Tâches réalisées entre deux dates");
            System.out.println("=================================================================");
            Date dateDebutRech = sdf.parse("01/02/2013");
            Date dateFinRech = sdf.parse("31/03/2013");
            tacheService.afficherTachesRealiseesEntreDates(dateDebutRech, dateFinRech);
            System.out.println();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            HibernateUtil.shutdown();
        }
    }
}
