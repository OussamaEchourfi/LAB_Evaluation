package ma.projet.test;

import java.util.List;
import ma.projet.classes.Categorie;
import ma.projet.service.CategorieService;
import ma.projet.service.ProduitService;
import ma.projet.util.HibernateUtil;

public class TestProduitsParCategorie {

    public static void main(String[] args) {
        System.out.println("=== Test : Affichage des produits par catégorie ===");
        CategorieService categorieService = new CategorieService();
        ProduitService produitService = new ProduitService();

        try {
            List<Categorie> categories = categorieService.getAll();
            if (categories.isEmpty()) {
                System.out.println("Aucune catégorie trouvée en base. Veuillez lancer TestApp d'abord.");
                return;
            }

            for (Categorie cat : categories) {
                System.out.println();
                produitService.afficherProduitsParCategorie(cat);
            }
        } finally {
            HibernateUtil.shutdown();
        }
    }
}
