package ma.projet.test;

import java.text.SimpleDateFormat;
import java.util.Date;
import ma.projet.service.ProduitService;
import ma.projet.util.HibernateUtil;

public class TestProduitsEntreDates {

    public static void main(String[] args) {
        System.out.println("=== Test : Affichage des produits commandés entre deux dates ===");
        ProduitService produitService = new ProduitService();

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date dateDebut = sdf.parse("01/01/2013");
            Date dateFin = sdf.parse("31/12/2013");

            produitService.afficherProduitsCommandesEntreDates(dateDebut, dateFin);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            HibernateUtil.shutdown();
        }
    }
}
