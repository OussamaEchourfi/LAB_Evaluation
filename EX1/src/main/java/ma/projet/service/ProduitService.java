package ma.projet.service;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.hibernate.Session;
import org.hibernate.Transaction;
import ma.projet.classes.Categorie;
import ma.projet.classes.Commande;
import ma.projet.classes.LigneCommandeProduit;
import ma.projet.classes.Produit;
import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;

public class ProduitService implements IDao<Produit> {

    @Override
    public boolean create(Produit o) {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            if (o.getCategorie() != null && o.getCategorie().getId() > 0) {
                o.setCategorie(session.get(Categorie.class, o.getCategorie().getId()));
            }
            session.persist(o);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public boolean update(Produit o) {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.merge(o);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public boolean delete(Produit o) {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.remove(session.contains(o) ? o : session.merge(o));
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public Produit findById(int id) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.get(Produit.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public List<Produit> getAll() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.createQuery("from Produit", Produit.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        } finally {
            if (session != null) session.close();
        }
    }

    /**
     * 1. Méthode pour afficher la liste des produits par catégorie.
     */
    public List<Produit> getProduitsParCategorie(Categorie categorie) {
        if (categorie == null) return Collections.emptyList();
        return getProduitsParCategorie(categorie.getId());
    }

    public List<Produit> getProduitsParCategorie(int categorieId) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.createQuery(
                    "from Produit p where p.categorie.id = :catId",
                    Produit.class
            ).setParameter("catId", categorieId).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        } finally {
            if (session != null) session.close();
        }
    }

    public void afficherProduitsParCategorie(Categorie categorie) {
        if (categorie == null) {
            System.out.println("Catégorie inexistante ou null !");
            return;
        }
        afficherProduitsParCategorie(categorie.getId(), categorie.getLibelle());
    }

    public void afficherProduitsParCategorie(int categorieId) {
        CategorieService cs = new CategorieService();
        Categorie c = cs.findById(categorieId);
        String libelle = (c != null) ? c.getLibelle() : "ID: " + categorieId;
        afficherProduitsParCategorie(categorieId, libelle);
    }

    private void afficherProduitsParCategorie(int categorieId, String libelle) {
        List<Produit> produits = getProduitsParCategorie(categorieId);
        System.out.println("Catégorie : " + libelle);
        System.out.println("Liste des produits :");
        System.out.printf("%-10s %-20s %-10s%n", "ID", "Référence", "Prix");
        for (Produit p : produits) {
            String prixStr = (p.getPrix() == (long) p.getPrix())
                    ? String.format("%.0f DH", p.getPrix())
                    : String.format("%.2f DH", p.getPrix());
            System.out.printf("%-10d %-20s %-10s%n", p.getId(), p.getReference(), prixStr);
        }
    }

    /**
     * 2. Méthode pour afficher les produits commandés entre deux dates.
     */
    public List<Produit> getProduitsCommandesEntreDates(Date dateDebut, Date dateFin) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.createQuery(
                    "select distinct l.produit from LigneCommandeProduit l " +
                    "where l.commande.date between :d1 and :d2",
                    Produit.class
            ).setParameter("d1", dateDebut)
             .setParameter("d2", dateFin)
             .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        } finally {
            if (session != null) session.close();
        }
    }

    public void afficherProduitsCommandesEntreDates(Date dateDebut, Date dateFin) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        List<Produit> produits = getProduitsCommandesEntreDates(dateDebut, dateFin);
        System.out.println("Produits commandés entre le " + sdf.format(dateDebut) + " et le " + sdf.format(dateFin) + " :");
        System.out.printf("%-10s %-20s %-10s%n", "ID", "Référence", "Prix");
        for (Produit p : produits) {
            String prixStr = (p.getPrix() == (long) p.getPrix())
                    ? String.format("%.0f DH", p.getPrix())
                    : String.format("%.2f DH", p.getPrix());
            System.out.printf("%-10d %-20s %-10s%n", p.getId(), p.getReference(), prixStr);
        }
    }

    /**
     * 3. Méthode pour afficher les produits commandés dans une commande donnée.
     * Exemple d'affichage attendu :
     * Commande : 4     Date : 14 Mars 2013
     * Liste des produits :
     * Référence   Prix    Quantité
     * ES12        120 DH  7
     * ZR85        100 DH  14
     * EE85        200 DH  5
     */
    public void afficherProduitsParCommande(Commande commande) {
        if (commande == null) {
            System.out.println("Commande inexistante ou null !");
            return;
        }
        afficherProduitsParCommande(commande.getId());
    }

    public void afficherProduitsParCommande(int commandeId) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Commande commande = session.get(Commande.class, commandeId);
            if (commande == null) {
                System.out.println("Aucune commande trouvée avec l'ID : " + commandeId);
                return;
            }

            // Formatage de la date : ex. "14 Mars 2013"
            SimpleDateFormat sdf = new SimpleDateFormat("d MMMM yyyy", Locale.FRENCH);
            String dateFormatted = sdf.format(commande.getDate());
            String[] tokens = dateFormatted.split(" ");
            if (tokens.length == 3) {
                String moisCapitalise = tokens[1].substring(0, 1).toUpperCase() + tokens[1].substring(1);
                dateFormatted = tokens[0] + " " + moisCapitalise + " " + tokens[2];
            }

            System.out.printf("Commande : %-5d Date : %s%n", commande.getId(), dateFormatted);
            System.out.println("Liste des produits :");
            System.out.printf("%-12s%-8s%s%n", "Référence", "Prix", "Quantité");

            List<LigneCommandeProduit> lignes = session.createQuery(
                    "select l from LigneCommandeProduit l join fetch l.produit " +
                    "where l.commande.id = :cmdId",
                    LigneCommandeProduit.class
            ).setParameter("cmdId", commandeId).getResultList();

            for (LigneCommandeProduit l : lignes) {
                Produit p = l.getProduit();
                String prixStr = (p.getPrix() == (long) p.getPrix())
                        ? String.format("%.0f DH", p.getPrix())
                        : String.format("%.2f DH", p.getPrix());
                System.out.printf("%-12s%-8s%d%n", p.getReference(), prixStr, l.getQuantite());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
    }

    /**
     * 4. Méthode pour afficher la liste des produits dont le prix est supérieur à 100 DH,
     * en utilisant une requête nommée.
     */
    public List<Produit> getProduitsPrixSuperieurA100() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.createNamedQuery("Produit.findByPrixSuperieurA100", Produit.class)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        } finally {
            if (session != null) session.close();
        }
    }

    public void afficherProduitsPrixSuperieurA100() {
        List<Produit> produits = getProduitsPrixSuperieurA100();
        System.out.println("Liste des produits dont le prix est supérieur à 100 DH (Requête nommée) :");
        System.out.printf("%-10s %-20s %-10s%n", "ID", "Référence", "Prix");
        for (Produit p : produits) {
            String prixStr = (p.getPrix() == (long) p.getPrix())
                    ? String.format("%.0f DH", p.getPrix())
                    : String.format("%.2f DH", p.getPrix());
            System.out.printf("%-10d %-20s %-10s%n", p.getId(), p.getReference(), prixStr);
        }
    }

    public List<Produit> getProduitsPrixSuperieurA(float prix) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.createNamedQuery("Produit.findByPrixSuperieurA", Produit.class)
                    .setParameter("prix", prix)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        } finally {
            if (session != null) session.close();
        }
    }

    public void afficherProduitsPrixSuperieurA(float prix) {
        List<Produit> produits = getProduitsPrixSuperieurA(prix);
        System.out.println("Liste des produits dont le prix est supérieur à " + (int) prix + " DH (Requête nommée) :");
        System.out.printf("%-10s %-20s %-10s%n", "ID", "Référence", "Prix");
        for (Produit p : produits) {
            String prixStr = (p.getPrix() == (long) p.getPrix())
                    ? String.format("%.0f DH", p.getPrix())
                    : String.format("%.2f DH", p.getPrix());
            System.out.printf("%-10d %-20s %-10s%n", p.getId(), p.getReference(), prixStr);
        }
    }
}
