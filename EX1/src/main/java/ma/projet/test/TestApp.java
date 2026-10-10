package ma.projet.test;

import java.text.SimpleDateFormat;
import java.util.Date;
import ma.projet.classes.Categorie;
import ma.projet.classes.Commande;
import ma.projet.classes.LigneCommandeProduit;
import ma.projet.classes.Produit;
import ma.projet.service.CategorieService;
import ma.projet.service.CommandeService;
import ma.projet.service.LigneCommandeService;
import ma.projet.service.ProduitService;
import ma.projet.util.HibernateUtil;

public class TestApp {

    public static void main(String[] args) {
        System.out.println("=================================================================");
        System.out.println("         APPLICATION DE GESTION DE STOCK - EXERCICE 1            ");
        System.out.println("=================================================================\n");

        CategorieService categorieService = new CategorieService();
        ProduitService produitService = new ProduitService();
        CommandeService commandeService = new CommandeService();
        LigneCommandeService ligneCommandeService = new LigneCommandeService();

        try {
            // -------------------------------------------------------------
            // 1. Initialisation des données de test
            // -------------------------------------------------------------
            System.out.println(">>> 1. Initialisation des données en base de données...\n");

            // Création des catégories
            Categorie c1 = new Categorie("INFO", "Informatique");
            Categorie c2 = new Categorie("ACC", "Accessoires");
            categorieService.create(c1);
            categorieService.create(c2);

            // Création des produits
            Produit p1 = new Produit("ES12", 120.0f, c1);
            Produit p2 = new Produit("ZR85", 100.0f, c1);
            Produit p3 = new Produit("EE85", 200.0f, c1);
            Produit p4 = new Produit("MS10", 50.0f, c2);
            Produit p5 = new Produit("CL20", 80.0f, c2);
            Produit p6 = new Produit("EC30", 150.0f, c1);

            produitService.create(p1);
            produitService.create(p2);
            produitService.create(p3);
            produitService.create(p4);
            produitService.create(p5);
            produitService.create(p6);

            // Création des commandes
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date dateCmd1 = sdf.parse("14/03/2013");
            Date dateCmd2 = sdf.parse("25/04/2013");

            Commande cmd1 = new Commande(dateCmd1);
            Commande cmd2 = new Commande(dateCmd2);
            commandeService.create(cmd1);
            commandeService.create(cmd2);

            // Création des lignes de commandes
            // Pour la commande 1 : ES12 (120 DH, qté 7), ZR85 (100 DH, qté 14), EE85 (200 DH, qté 5)
            LigneCommandeProduit l1 = new LigneCommandeProduit(p1, cmd1, 7);
            LigneCommandeProduit l2 = new LigneCommandeProduit(p2, cmd1, 14);
            LigneCommandeProduit l3 = new LigneCommandeProduit(p3, cmd1, 5);

            ligneCommandeService.create(l1);
            ligneCommandeService.create(l2);
            ligneCommandeService.create(l3);

            // Pour la commande 2
            LigneCommandeProduit l4 = new LigneCommandeProduit(p4, cmd2, 2);
            LigneCommandeProduit l5 = new LigneCommandeProduit(p6, cmd2, 3);
            ligneCommandeService.create(l4);
            ligneCommandeService.create(l5);

            System.out.println("Données de test initialisées avec succès !\n");

            // -------------------------------------------------------------
            // 2. Test fonctionnalité 1 : Produits par catégorie
            // -------------------------------------------------------------
            System.out.println("=================================================================");
            System.out.println("TEST 1 : Affichage des produits par catégorie");
            System.out.println("=================================================================");
            produitService.afficherProduitsParCategorie(c1);
            System.out.println();
            produitService.afficherProduitsParCategorie(c2);
            System.out.println();

            // -------------------------------------------------------------
            // 3. Test fonctionnalité 2 : Produits commandés entre deux dates
            // -------------------------------------------------------------
            System.out.println("=================================================================");
            System.out.println("TEST 2 : Affichage des produits commandés entre deux dates");
            System.out.println("=================================================================");
            Date dateDebut = sdf.parse("01/01/2013");
            Date dateFin = sdf.parse("31/03/2013");
            produitService.afficherProduitsCommandesEntreDates(dateDebut, dateFin);
            System.out.println();

            // -------------------------------------------------------------
            // 4. Test fonctionnalité 3 : Produits commandés dans une commande
            // -------------------------------------------------------------
            System.out.println("=================================================================");
            System.out.println("TEST 3 : Affichage des produits commandés dans une commande donnée");
            System.out.println("=================================================================");
            produitService.afficherProduitsParCommande(cmd1);
            System.out.println();

            // -------------------------------------------------------------
            // 5. Test fonctionnalité 4 : Produits dont le prix > 100 DH (Requête nommée)
            // -------------------------------------------------------------
            System.out.println("=================================================================");
            System.out.println("TEST 4 : Affichage des produits avec prix > 100 DH (Requête nommée)");
            System.out.println("=================================================================");
            produitService.afficherProduitsPrixSuperieurA100();
            System.out.println();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            HibernateUtil.shutdown();
        }
    }
}
