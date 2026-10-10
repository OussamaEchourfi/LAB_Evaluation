package ma.projet.test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import ma.projet.beans.Femme;
import ma.projet.beans.Homme;
import ma.projet.beans.Mariage;
import ma.projet.service.FemmeService;
import ma.projet.service.HommeService;
import ma.projet.service.MariageService;
import ma.projet.util.HibernateUtil;

public class TestApp {

    public static void main(String[] args) {
        System.out.println("=================================================================");
        System.out.println("       APPLICATION DE GESTION DE L'ÉTAT CIVIL - EXERCICE 3       ");
        System.out.println("=================================================================\n");

        HommeService hommeService = new HommeService();
        FemmeService femmeService = new FemmeService();
        MariageService mariageService = new MariageService();

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            // -------------------------------------------------------------
            // 1. Création des données de test (10 femmes et 5 hommes)
            // -------------------------------------------------------------
            System.out.println(">>> 1. Création de 10 femmes et 5 hommes en base de données...\n");

            // Création de 10 femmes (la plus âgée est Karima Alami née en 1945)
            Femme f1 = new Femme("Rami", "Salima", "0611111111", "Rabat", sdf.parse("15/05/1970"));
            Femme f2 = new Femme("Ali", "Amal", "0622222222", "Casablanca", sdf.parse("20/08/1975"));
            Femme f3 = new Femme("Alaoui", "Wafa", "0633333333", "Fès", sdf.parse("10/12/1978"));
            Femme f4 = new Femme("Alami", "Karima", "0644444444", "Marrakech", sdf.parse("05/03/1945")); // Plus âgée
            Femme f5 = new Femme("Bennani", "Samira", "0655555555", "Tanger", sdf.parse("12/01/1980"));
            Femme f6 = new Femme("Tahiri", "Nadia", "0666666666", "Agadir", sdf.parse("22/04/1982"));
            Femme f7 = new Femme("Chraibi", "Mouna", "0677777777", "Meknès", sdf.parse("14/09/1985"));
            Femme f8 = new Femme("El Amrani", "Leila", "0688888888", "Oujda", sdf.parse("30/11/1988"));
            Femme f9 = new Femme("Mansouri", "Imane", "0699999999", "Kenitra", sdf.parse("08/07/1990"));
            Femme f10 = new Femme("Hassani", "Zineb", "0610101010", "Tétouan", sdf.parse("19/02/1992"));

            femmeService.create(f1);
            femmeService.create(f2);
            femmeService.create(f3);
            femmeService.create(f4);
            femmeService.create(f5);
            femmeService.create(f6);
            femmeService.create(f7);
            femmeService.create(f8);
            femmeService.create(f9);
            femmeService.create(f10);

            // Création de 5 hommes (SAFI Said avec les mariages du sujet)
            Homme h1 = new Homme("Safi", "Said", "0601010101", "Rabat", sdf.parse("01/01/1965"));
            Homme h2 = new Homme("El Idrissi", "Omar", "0602020202", "Casablanca", sdf.parse("15/06/1970"));
            Homme h3 = new Homme("Berrada", "Mehdi", "0603030303", "Fès", sdf.parse("10/10/1975"));
            Homme h4 = new Homme("Kabbaj", "Tariq", "0604040404", "Tanger", sdf.parse("25/03/1980"));
            Homme h5 = new Homme("Fassi", "Younes", "0605050505", "Marrakech", sdf.parse("04/11/1985"));

            hommeService.create(h1);
            hommeService.create(h2);
            hommeService.create(h3);
            hommeService.create(h4);
            hommeService.create(h5);

            // -------------------------------------------------------------
            // Création des mariages pour SAFI SAID (H1) :
            // 1 échoué avec Karima Alami (1989-1990, 0 enfant)
            // 3 en cours : Salima Rami (1990, 4 enfants), Amal Ali (1995, 2 enfants), Wafa Alaoui (2000, 3 enfants)
            // -------------------------------------------------------------
            Mariage m1 = new Mariage(h1, f4, sdf.parse("03/09/1989"), sdf.parse("03/09/1990"), 0);
            Mariage m2 = new Mariage(h1, f1, sdf.parse("03/09/1990"), null, 4);
            Mariage m3 = new Mariage(h1, f2, sdf.parse("03/09/1995"), null, 2);
            Mariage m4 = new Mariage(h1, f3, sdf.parse("04/11/2000"), null, 3);

            // Mariage supplémentaire pour Karima Alami avec H2 (pour avoir une femme mariée au moins 2 fois)
            Mariage m5 = new Mariage(h2, f4, sdf.parse("15/05/1992"), null, 1);

            // Autres mariages pour tester
            Mariage m6 = new Mariage(h3, f5, sdf.parse("01/06/2005"), null, 2);
            Mariage m7 = new Mariage(h4, f6, sdf.parse("12/08/2010"), null, 1);

            mariageService.create(m1);
            mariageService.create(m2);
            mariageService.create(m3);
            mariageService.create(m4);
            mariageService.create(m5);
            mariageService.create(m6);
            mariageService.create(m7);

            System.out.println("Données initialisées avec succès !\n");

            // -------------------------------------------------------------
            // 2. Afficher la liste des femmes
            // -------------------------------------------------------------
            System.out.println("=================================================================");
            System.out.println("TEST 1 : Liste de toutes les femmes");
            System.out.println("=================================================================");
            List<Femme> femmes = femmeService.getAll();
            System.out.printf("%-5s %-15s %-15s %-15s %-15s %-15s%n",
                    "ID", "Nom", "Prénom", "Téléphone", "Adresse", "Date Naissance");
            for (Femme f : femmes) {
                System.out.printf("%-5d %-15s %-15s %-15s %-15s %-15s%n",
                        f.getId(), f.getNom(), f.getPrenom(), f.getTelephone(), f.getAdresse(),
                        sdf.format(f.getDateNaissance()));
            }
            System.out.println();

            // -------------------------------------------------------------
            // 3. Afficher la femme la plus âgée
            // -------------------------------------------------------------
            System.out.println("=================================================================");
            System.out.println("TEST 2 : Affichage de la femme la plus âgée");
            System.out.println("=================================================================");
            femmeService.afficherFemmePlusAgee();
            System.out.println();

            // -------------------------------------------------------------
            // 4. Afficher les épouses d’un homme donné entre deux dates
            // -------------------------------------------------------------
            System.out.println("=================================================================");
            System.out.println("TEST 3 : Épouses de SAFI Said entre 1988 et 2002");
            System.out.println("=================================================================");
            Date dDebutRech = sdf.parse("01/01/1988");
            Date dFinRech = sdf.parse("31/12/2002");
            hommeService.afficherEpousesEntreDates(h1, dDebutRech, dFinRech);
            System.out.println();

            // -------------------------------------------------------------
            // 5. Afficher le nombre d’enfants d’une femme entre deux dates (requête native nommée)
            // -------------------------------------------------------------
            System.out.println("=================================================================");
            System.out.println("TEST 4 : Nombre d'enfants d'une femme entre deux dates (Requête native nommée)");
            System.out.println("=================================================================");
            femmeService.afficherNombreEnfantsEntreDates(f1, sdf.parse("01/01/1989"), sdf.parse("31/12/1995"));
            System.out.println();

            // -------------------------------------------------------------
            // 6. Afficher les femmes mariées deux fois ou plus (requête nommée)
            // -------------------------------------------------------------
            System.out.println("=================================================================");
            System.out.println("TEST 5 : Femmes mariées au moins deux fois (Requête nommée)");
            System.out.println("=================================================================");
            femmeService.afficherFemmesMarieesAuMoinsDeuxFois();
            System.out.println();

            // -------------------------------------------------------------
            // 7. Nombre d'hommes mariés à quatre femmes entre deux dates (API Criteria)
            // -------------------------------------------------------------
            System.out.println("=================================================================");
            System.out.println("TEST 6 : Hommes mariés à 4 femmes entre deux dates (API Criteria)");
            System.out.println("=================================================================");
            hommeService.afficherNombreHommesMariesQuatreFemmesEntreDates(
                    sdf.parse("01/01/1980"),
                    sdf.parse("31/12/2010")
            );
            System.out.println();

            // -------------------------------------------------------------
            // 8. Afficher les mariages d’un homme avec tous les détails (format attendu)
            // -------------------------------------------------------------
            System.out.println("=================================================================");
            System.out.println("TEST 7 : Mariages d'un homme avec tous les détails");
            System.out.println("=================================================================");
            hommeService.afficherMariagesHomme(h1);
            System.out.println();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            HibernateUtil.shutdown();
        }
    }
}
