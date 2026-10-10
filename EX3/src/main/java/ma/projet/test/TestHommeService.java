package ma.projet.test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import ma.projet.beans.Homme;
import ma.projet.service.HommeService;
import ma.projet.util.HibernateUtil;

public class TestHommeService {

    public static void main(String[] args) {
        System.out.println("=== Test : Fonctionnalités de HommeService ===");
        HommeService hommeService = new HommeService();

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            List<Homme> hommes = hommeService.getAll();
            if (hommes.isEmpty()) {
                System.out.println("Aucun homme en base. Lancez TestApp d'abord.");
                return;
            }

            Homme h = hommes.get(0);

            System.out.println("\n--- 1. Épouses de " + h.getPrenom() + " " + h.getNom() + " entre deux dates ---");
            Date dDebut = sdf.parse("01/01/1980");
            Date dFin = sdf.parse("31/12/2010");
            hommeService.afficherEpousesEntreDates(h, dDebut, dFin);

            System.out.println("\n--- 2. Nombre d'hommes mariés à 4 femmes entre deux dates (Criteria API) ---");
            hommeService.afficherNombreHommesMariesQuatreFemmesEntreDates(dDebut, dFin);

            System.out.println("\n--- 3. Détails complets des mariages d'un homme ---");
            hommeService.afficherMariagesHomme(h);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            HibernateUtil.shutdown();
        }
    }
}
