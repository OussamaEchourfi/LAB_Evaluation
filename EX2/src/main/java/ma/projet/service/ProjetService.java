package ma.projet.service;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import org.hibernate.Session;
import org.hibernate.Transaction;
import ma.projet.classes.Employe;
import ma.projet.classes.EmployeTache;
import ma.projet.classes.Projet;
import ma.projet.classes.Tache;
import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;

public class ProjetService implements IDao<Projet> {

    @Override
    public boolean create(Projet o) {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            if (o.getChefDeProjet() != null && o.getChefDeProjet().getId() > 0) {
                o.setChefDeProjet(session.get(Employe.class, o.getChefDeProjet().getId()));
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
    public boolean update(Projet o) {
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
    public boolean delete(Projet o) {
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
    public Projet findById(int id) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.get(Projet.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public List<Projet> getAll() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.createQuery("from Projet", Projet.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        } finally {
            if (session != null) session.close();
        }
    }

    /**
     * 1. Afficher la liste des tâches planifiées pour un projet.
     */
    public List<Tache> getTachesPlanifiees(Projet projet) {
        if (projet == null) return Collections.emptyList();
        return getTachesPlanifiees(projet.getId());
    }

    public List<Tache> getTachesPlanifiees(int projetId) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.createQuery(
                    "from Tache t where t.projet.id = :projId order by t.id",
                    Tache.class
            ).setParameter("projId", projetId).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        } finally {
            if (session != null) session.close();
        }
    }

    public void afficherTachesPlanifiees(Projet projet) {
        if (projet == null) {
            System.out.println("Projet null !");
            return;
        }
        afficherTachesPlanifiees(projet.getId());
    }

    public void afficherTachesPlanifiees(int projetId) {
        Projet p = findById(projetId);
        if (p == null) {
            System.out.println("Aucun projet trouvé avec l'id : " + projetId);
            return;
        }

        List<Tache> taches = getTachesPlanifiees(projetId);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        System.out.println("Tâches planifiées pour le projet : " + p.getNom() + " (ID: " + p.getId() + ") :");
        System.out.printf("%-5s %-20s %-15s %-15s %-10s%n", "Num", "Nom", "Date Début", "Date Fin", "Prix");

        for (Tache t : taches) {
            String debutStr = t.getDateDebut() != null ? sdf.format(t.getDateDebut()) : "-";
            String finStr = t.getDateFin() != null ? sdf.format(t.getDateFin()) : "-";
            String prixStr = (t.getPrix() == (long) t.getPrix())
                    ? String.format("%.0f DH", t.getPrix())
                    : String.format("%.2f DH", t.getPrix());
            System.out.printf("%-5d %-20s %-15s %-15s %-10s%n", t.getId(), t.getNom(), debutStr, finStr, prixStr);
        }
    }

    /**
     * 2. Afficher la liste des tâches réalisées avec les dates réelles.
     * Exemple d’affichage attendu :
     * Projet : 4      Nom : Gestion de stock     Date début : 14 Janvier 2013
     * Liste des tâches:
     * Num Nom            Date Début Réelle   Date Fin Réelle
     * 12  Analyse        10/02/2013          20/02/2013
     * 13  Conception     10/03/2013          15/03/2013
     * 14  Développement  10/04/2013          25/04/2013
     */
    public List<EmployeTache> getTachesRealiseesParProjet(Projet projet) {
        if (projet == null) return Collections.emptyList();
        return getTachesRealiseesParProjet(projet.getId());
    }

    public List<EmployeTache> getTachesRealiseesParProjet(int projetId) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.createQuery(
                    "select et from EmployeTache et join fetch et.tache t " +
                    "where t.projet.id = :projId " +
                    "order by t.id",
                    EmployeTache.class
            ).setParameter("projId", projetId).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        } finally {
            if (session != null) session.close();
        }
    }

    public void afficherTachesRealiseesAvecDates(Projet projet) {
        if (projet == null) {
            System.out.println("Projet null !");
            return;
        }
        afficherTachesRealiseesAvecDates(projet.getId());
    }

    public void afficherTachesRealiseesAvecDates(int projetId) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Projet p = session.get(Projet.class, projetId);
            if (p == null) {
                System.out.println("Aucun projet trouvé avec l'id : " + projetId);
                return;
            }

            // Formatage de la date de début en français : "14 Janvier 2013"
            String dateDebutStr = "";
            if (p.getDateDebut() != null) {
                SimpleDateFormat sdfFr = new SimpleDateFormat("d MMMM yyyy", Locale.FRENCH);
                String formatted = sdfFr.format(p.getDateDebut());
                String[] parts = formatted.split(" ");
                if (parts.length == 3) {
                    String mois = parts[1].substring(0, 1).toUpperCase() + parts[1].substring(1);
                    dateDebutStr = parts[0] + " " + mois + " " + parts[2];
                } else {
                    dateDebutStr = formatted;
                }
            }

            System.out.printf("Projet : %-6d Nom : %-20s Date début : %s%n", p.getId(), p.getNom(), dateDebutStr);
            System.out.println("Liste des tâches:");
            System.out.printf("%-4s%-15s%-20s%s%n", "Num", "Nom", "Date Début Réelle", "Date Fin Réelle");

            List<EmployeTache> list = session.createQuery(
                    "select et from EmployeTache et join fetch et.tache t " +
                    "where t.projet.id = :projId " +
                    "order by t.id",
                    EmployeTache.class
            ).setParameter("projId", projetId).getResultList();

            SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy");
            for (EmployeTache et : list) {
                String dDebut = et.getDateDebutReelle() != null ? sdfDate.format(et.getDateDebutReelle()) : "-";
                String dFin = et.getDateFinReelle() != null ? sdfDate.format(et.getDateFinReelle()) : "-";
                System.out.printf("%-4d%-15s%-20s%s%n",
                        et.getTache().getId(),
                        et.getTache().getNom(),
                        dDebut,
                        dFin);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
    }
}
