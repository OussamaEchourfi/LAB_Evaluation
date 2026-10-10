package ma.projet.test;

import java.text.SimpleDateFormat;
import java.util.Date;
import ma.projet.service.TacheService;
import ma.projet.util.HibernateUtil;

public class TestTacheService {

    public static void main(String[] args) {
        System.out.println("=== Test : Méthodes TacheService ===");
        TacheService tacheService = new TacheService();

        try {
            System.out.println("\n--- 1. Tâches dont le prix > 1000 DH (Requête nommée) ---");
            tacheService.afficherTachesPrixSuperieurA1000();

            System.out.println("\n--- 2. Tâches réalisées entre deux dates ---");
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date dateDebut = sdf.parse("01/01/2013");
            Date dateFin = sdf.parse("31/12/2013");
            tacheService.afficherTachesRealiseesEntreDates(dateDebut, dateFin);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            HibernateUtil.shutdown();
        }
    }
}
