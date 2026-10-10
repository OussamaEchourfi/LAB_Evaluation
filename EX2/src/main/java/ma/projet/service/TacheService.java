package ma.projet.service;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import ma.projet.classes.EmployeTache;
import ma.projet.classes.Projet;
import ma.projet.classes.Tache;
import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;

public class TacheService implements IDao<Tache> {

    @Override
    public boolean create(Tache o) {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            if (o.getProjet() != null && o.getProjet().getId() > 0) {
                o.setProjet(session.get(Projet.class, o.getProjet().getId()));
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
    public boolean update(Tache o) {
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
    public boolean delete(Tache o) {
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
    public Tache findById(int id) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.get(Tache.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public List<Tache> getAll() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.createQuery("from Tache", Tache.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        } finally {
            if (session != null) session.close();
        }
    }

    /**
     * 1. Méthode pour afficher les tâches dont le prix est supérieur à 1000 DH (requête nommée).
     */
    public List<Tache> getTachesPrixSuperieurA1000() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.createNamedQuery("Tache.findByPrixSuperieurA1000", Tache.class)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        } finally {
            if (session != null) session.close();
        }
    }

    public void afficherTachesPrixSuperieurA1000() {
        List<Tache> taches = getTachesPrixSuperieurA1000();
        System.out.println("Tâches dont le prix est supérieur à 1000 DH (Requête nommée) :");
        System.out.printf("%-5s %-20s %-12s%n", "ID", "Nom Tâche", "Prix");
        for (Tache t : taches) {
            String prixStr = (t.getPrix() == (long) t.getPrix())
                    ? String.format("%.0f DH", t.getPrix())
                    : String.format("%.2f DH", t.getPrix());
            System.out.printf("%-5d %-20s %-12s%n", t.getId(), t.getNom(), prixStr);
        }
    }

    public List<Tache> getTachesPrixSuperieurA(double prix) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.createNamedQuery("Tache.findByPrixSuperieurA", Tache.class)
                    .setParameter("prix", prix)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        } finally {
            if (session != null) session.close();
        }
    }

    public void afficherTachesPrixSuperieurA(double prix) {
        List<Tache> taches = getTachesPrixSuperieurA(prix);
        System.out.println("Tâches dont le prix est supérieur à " + (int) prix + " DH (Requête nommée) :");
        System.out.printf("%-5s %-20s %-12s%n", "ID", "Nom Tâche", "Prix");
        for (Tache t : taches) {
            String prixStr = (t.getPrix() == (long) t.getPrix())
                    ? String.format("%.0f DH", t.getPrix())
                    : String.format("%.2f DH", t.getPrix());
            System.out.printf("%-5d %-20s %-12s%n", t.getId(), t.getNom(), prixStr);
        }
    }

    /**
     * 2. Méthode pour afficher les tâches réalisées entre deux dates.
     */
    public List<EmployeTache> getTachesRealiseesEntreDates(Date dateDebut, Date dateFin) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.createQuery(
                    "select et from EmployeTache et join fetch et.tache t join fetch et.employe e " +
                    "where et.dateDebutReelle >= :d1 and et.dateFinReelle <= :d2 " +
                    "order by et.dateDebutReelle",
                    EmployeTache.class
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

    public void afficherTachesRealiseesEntreDates(Date dateDebut, Date dateFin) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        List<EmployeTache> list = getTachesRealiseesEntreDates(dateDebut, dateFin);

        System.out.println("Tâches réalisées entre le " + sdf.format(dateDebut) + " et le " + sdf.format(dateFin) + " :");
        System.out.printf("%-5s %-20s %-20s %-18s %-18s%n", "Num", "Nom Tâche", "Employé", "Date Début Réelle", "Date Fin Réelle");

        for (EmployeTache et : list) {
            String debutStr = et.getDateDebutReelle() != null ? sdf.format(et.getDateDebutReelle()) : "-";
            String finStr = et.getDateFinReelle() != null ? sdf.format(et.getDateFinReelle()) : "-";
            String empStr = et.getEmploye().getPrenom() + " " + et.getEmploye().getNom();
            System.out.printf("%-5d %-20s %-20s %-18s %-18s%n",
                    et.getTache().getId(),
                    et.getTache().getNom(),
                    empStr,
                    debutStr,
                    finStr);
        }
    }
}
