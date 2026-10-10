package ma.projet.test;

import java.util.List;
import ma.projet.classes.Commande;
import ma.projet.service.CommandeService;
import ma.projet.service.ProduitService;
import ma.projet.util.HibernateUtil;

public class TestProduitsParCommande {

    public static void main(String[] args) {
        System.out.println("=== Test : Affichage des produits commandés dans une commande ===");
        CommandeService commandeService = new CommandeService();
        ProduitService produitService = new ProduitService();

        try {
            List<Commande> commandes = commandeService.getAll();
            if (commandes.isEmpty()) {
                System.out.println("Aucune commande en base. Veuillez lancer TestApp d'abord.");
                return;
            }

            // Affiche la première commande ou toutes les commandes
            for (Commande cmd : commandes) {
                System.out.println();
                produitService.afficherProduitsParCommande(cmd);
            }
        } finally {
            HibernateUtil.shutdown();
        }
    }
}
