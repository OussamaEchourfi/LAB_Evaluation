package ma.projet.service;

import static org.junit.jupiter.api.Assertions.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ma.projet.classes.Categorie;
import ma.projet.classes.Commande;
import ma.projet.classes.LigneCommandeProduit;
import ma.projet.classes.Produit;

public class ProduitServiceTest {

    private static ProduitService produitService;
    private static CategorieService categorieService;
    private static CommandeService commandeService;
    private static LigneCommandeService ligneCommandeService;

    @BeforeAll
    public static void setUp() {
        produitService = new ProduitService();
        categorieService = new CategorieService();
        commandeService = new CommandeService();
        ligneCommandeService = new LigneCommandeService();
    }

    @Test
    public void testCreateAndFindProduit() {
        Categorie cat = new Categorie("TEST_CAT", "Categorie Test");
        assertTrue(categorieService.create(cat));

        Produit p = new Produit("REF_TEST_1", 150.0f, cat);
        assertTrue(produitService.create(p));
        assertTrue(p.getId() > 0);

        Produit found = produitService.findById(p.getId());
        assertNotNull(found);
        assertEquals("REF_TEST_1", found.getReference());
        assertEquals(150.0f, found.getPrix());
    }

    @Test
    public void testProduitsParCategorie() {
        Categorie cat = new Categorie("CAT_PC", "Ordinateurs Portables");
        categorieService.create(cat);

        Produit p1 = new Produit("PC_DELL", 5000.0f, cat);
        Produit p2 = new Produit("PC_HP", 4500.0f, cat);
        produitService.create(p1);
        produitService.create(p2);

        List<Produit> produits = produitService.getProduitsParCategorie(cat);
        assertNotNull(produits);
        assertTrue(produits.size() >= 2);
    }

    @Test
    public void testNamedQueryPrixSuperieurA100() {
        Categorie cat = new Categorie("CAT_PRIX", "Divers");
        categorieService.create(cat);

        Produit pHigh = new Produit("PROD_CHERE", 250.0f, cat);
        Produit pLow = new Produit("PROD_PAS_CHERE", 40.0f, cat);
        produitService.create(pHigh);
        produitService.create(pLow);

        List<Produit> superieursA100 = produitService.getProduitsPrixSuperieurA100();
        assertNotNull(superieursA100);
        for (Produit p : superieursA100) {
            assertTrue(p.getPrix() > 100.0f, "Le prix du produit " + p.getReference() + " doit être > 100 DH");
        }
    }

    @Test
    public void testProduitsCommandesEntreDatesEtParCommande() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = sdf.parse("2025-05-10");
        Date dateDebut = sdf.parse("2025-05-01");
        Date dateFin = sdf.parse("2025-05-31");

        Categorie cat = new Categorie("CAT_CMD", "Matériel");
        categorieService.create(cat);

        Produit p = new Produit("REF_CMD_TEST", 120.0f, cat);
        produitService.create(p);

        Commande cmd = new Commande(date1);
        commandeService.create(cmd);

        LigneCommandeProduit lcp = new LigneCommandeProduit(p, cmd, 8);
        assertTrue(ligneCommandeService.create(lcp));

        // Vérification produits commandés entre deux dates
        List<Produit> listEntreDates = produitService.getProduitsCommandesEntreDates(dateDebut, dateFin);
        assertNotNull(listEntreDates);
        boolean containsProduct = listEntreDates.stream().anyMatch(prod -> prod.getId() == p.getId());
        assertTrue(containsProduct, "La liste entre les deux dates doit contenir le produit commandé");

        // Vérification de la méthode d'affichage par commande sans exception
        assertDoesNotThrow(() -> produitService.afficherProduitsParCommande(cmd));
        assertDoesNotThrow(() -> produitService.afficherProduitsParCommande(cmd.getId()));
    }
}
