package ma.projet.test;

import java.util.List;
import ma.projet.classes.Projet;
import ma.projet.service.ProjetService;
import ma.projet.util.HibernateUtil;

public class TestProjetService {

    public static void main(String[] args) {
        System.out.println("=== Test : Méthodes ProjetService ===");
        ProjetService projetService = new ProjetService();

        try {
            List<Projet> projets = projetService.getAll();
            if (projets.isEmpty()) {
                System.out.println("Aucun projet en base. Veuillez lancer TestApp d'abord.");
                return;
            }

            for (Projet p : projets) {
                System.out.println("\n=============================================================");
                projetService.afficherTachesPlanifiees(p);
                System.out.println();
                projetService.afficherTachesRealiseesAvecDates(p);
            }
        } finally {
            HibernateUtil.shutdown();
        }
    }
}
