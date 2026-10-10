package ma.projet.service;

import java.util.Collections;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import ma.projet.classes.Commande;
import ma.projet.classes.LigneCommandeProduit;
import ma.projet.classes.LigneCommandeProduitPK;
import ma.projet.classes.Produit;
import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;

public class LigneCommandeService implements IDao<LigneCommandeProduit> {

    @Override
    public boolean create(LigneCommandeProduit o) {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            if (o.getProduit() != null && o.getProduit().getId() > 0) {
                o.setProduit(session.get(Produit.class, o.getProduit().getId()));
            }
            if (o.getCommande() != null && o.getCommande().getId() > 0) {
                o.setCommande(session.get(Commande.class, o.getCommande().getId()));
            }
            if (o.getId() == null && o.getProduit() != null && o.getCommande() != null) {
                o.setId(new LigneCommandeProduitPK(o.getProduit().getId(), o.getCommande().getId()));
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
    public boolean update(LigneCommandeProduit o) {
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
    public boolean delete(LigneCommandeProduit o) {
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
    public LigneCommandeProduit findById(int id) {
        // L'entité LigneCommandeProduit possédant une clé composite,
        // cette méthode de l'interface IDao n'est pas applicable directement avec un simple entier.
        return null;
    }

    public LigneCommandeProduit findById(LigneCommandeProduitPK pk) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.get(LigneCommandeProduit.class, pk);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null) session.close();
        }
    }

    public LigneCommandeProduit findById(int produitId, int commandeId) {
        return findById(new LigneCommandeProduitPK(produitId, commandeId));
    }

    @Override
    public List<LigneCommandeProduit> getAll() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.createQuery("from LigneCommandeProduit", LigneCommandeProduit.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        } finally {
            if (session != null) session.close();
        }
    }

    public List<LigneCommandeProduit> getByCommande(Commande commande) {
        if (commande == null) return Collections.emptyList();
        return getByCommande(commande.getId());
    }

    public List<LigneCommandeProduit> getByCommande(int commandeId) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.createQuery(
                    "select l from LigneCommandeProduit l join fetch l.produit where l.commande.id = :cmdId",
                    LigneCommandeProduit.class
            ).setParameter("cmdId", commandeId).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        } finally {
            if (session != null) session.close();
        }
    }

    public List<LigneCommandeProduit> getByProduit(Produit produit) {
        if (produit == null) return Collections.emptyList();
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.createQuery(
                    "select l from LigneCommandeProduit l join fetch l.commande where l.produit.id = :prodId",
                    LigneCommandeProduit.class
            ).setParameter("prodId", produit.getId()).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        } finally {
            if (session != null) session.close();
        }
    }
}
