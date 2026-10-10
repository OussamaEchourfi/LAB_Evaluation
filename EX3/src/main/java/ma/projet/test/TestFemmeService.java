package ma.projet.test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import ma.projet.beans.Femme;
import ma.projet.service.FemmeService;
import ma.projet.util.HibernateUtil;

public class TestFemmeService {

    public static void main(String[] args) {
        System.out.println("=== Test : Fonctionnalités de FemmeService ===");
        FemmeService femmeService = new FemmeService();

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            System.out.println("\n--- 1. Liste des femmes ---");
            List<Femme> femmes = femmeService.getAll();
            if (femmes.isEmpty()) {
                System.out.println("Aucune femme en base. Lancez TestApp d'abord.");
                return;
            }
            for (Femme f : femmes) {
                System.out.printf("%-5d %-15s %-15s %-15s%n", f.getId(), f.getNom(), f.getPrenom(), sdf.format(f.getDateNaissance()));
            }

            System.out.println("\n--- 2. Femme la plus âgée ---");
            femmeService.afficherFemmePlusAgee();

            System.out.println("\n--- 3. Femmes mariées au moins deux fois (Requête nommée) ---");
            femmeService.afficherFemmesMarieesAuMoinsDeuxFois();

            System.out.println("\n--- 4. Nombre d'enfants de la première femme (Requête native nommée) ---");
            Date dDebut = sdf.parse("01/01/1980");
            Date dFin = sdf.parse("31/12/2020");
            femmeService.afficherNombreEnfantsEntreDates(femmes.get(0), dDebut, dFin);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            HibernateUtil.shutdown();
        }
    }
}
