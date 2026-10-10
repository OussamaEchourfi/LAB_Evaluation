package ma.projet.test;

import ma.projet.service.ProduitService;
import ma.projet.util.HibernateUtil;

public class TestRequeteNommee {

    public static void main(String[] args) {
        System.out.println("=== Test : Requête nommée (Produits dont le prix > 100 DH) ===");
        ProduitService produitService = new ProduitService();

        try {
            // Affichage avec la requête nommée fixe (> 100 DH)
            produitService.afficherProduitsPrixSuperieurA100();

            System.out.println();
            // Affichage avec la requête nommée paramétrée (ex: > 150 DH)
            produitService.afficherProduitsPrixSuperieurA(150.0f);
        } finally {
            HibernateUtil.shutdown();
        }
    }
}
