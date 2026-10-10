package ma.projet.service;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import ma.projet.beans.Femme;
import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;

public class FemmeService implements IDao<Femme> {

    @Override
    public boolean create(Femme o) {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
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
    public boolean update(Femme o) {
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
    public boolean delete(Femme o) {
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
    public Femme findById(int id) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.get(Femme.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public List<Femme> getAll() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.createQuery("from Femme", Femme.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        } finally {
            if (session != null) session.close();
        }
    }

    /**
     * 1. Méthode pour exécuter une requête native nommée retournant le nombre d’enfants d’une femme entre deux dates.
     */
    public int countEnfantsBetweenDates(int femmeId, Date dateDebut, Date dateFin) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Object result = session.createNamedQuery("Femme.countEnfantsBetweenDates")
                    .setParameter("femmeId", femmeId)
                    .setParameter("d1", dateDebut)
                    .setParameter("d2", dateFin)
                    .getSingleResult();

            if (result instanceof Number) {
                return ((Number) result).intValue();
            }
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            if (session != null) session.close();
        }
    }

    public int countEnfantsBetweenDates(Femme femme, Date dateDebut, Date dateFin) {
        if (femme == null) return 0;
        return countEnfantsBetweenDates(femme.getId(), dateDebut, dateFin);
    }

    public void afficherNombreEnfantsEntreDates(Femme femme, Date dateDebut, Date dateFin) {
        if (femme == null) {
            System.out.println("Femme null !");
            return;
        }
        afficherNombreEnfantsEntreDates(femme.getId(), dateDebut, dateFin);
    }

    public void afficherNombreEnfantsEntreDates(int femmeId, Date dateDebut, Date dateFin) {
        Femme f = findById(femmeId);
        if (f == null) {
            System.out.println("Aucune femme trouvée avec l'id : " + femmeId);
            return;
        }
        int count = countEnfantsBetweenDates(femmeId, dateDebut, dateFin);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        System.out.println("Nombre d'enfants de " + f.getPrenom() + " " + f.getNom() +
                " entre le " + sdf.format(dateDebut) + " et le " + sdf.format(dateFin) +
                " (Requête native nommée) : " + count);
    }

    /**
     * 2. Méthode pour exécuter une requête nommée retournant les femmes mariées au moins deux fois.
     */
    public List<Femme> getFemmesMarieesAuMoinsDeuxFois() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.createNamedQuery("Femme.findFemmesMarieesAuMoinsDeuxFois", Femme.class)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        } finally {
            if (session != null) session.close();
        }
    }

    public void afficherFemmesMarieesAuMoinsDeuxFois() {
        List<Femme> femmes = getFemmesMarieesAuMoinsDeuxFois();
        System.out.println("Liste des femmes mariées au moins deux fois (Requête nommée) :");
        if (femmes.isEmpty()) {
            System.out.println("Aucune femme mariée deux fois ou plus.");
            return;
        }
        System.out.printf("%-5s %-15s %-15s %-15s%n", "ID", "Nom", "Prénom", "Téléphone");
        for (Femme f : femmes) {
            System.out.printf("%-5d %-15s %-15s %-15s%n", f.getId(), f.getNom(), f.getPrenom(), f.getTelephone());
        }
    }

    /**
     * 3. Méthode pour trouver et afficher la femme la plus âgée.
     */
    public Femme getFemmePlusAgee() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            List<Femme> list = session.createQuery(
                    "from Femme f order by f.dateNaissance asc",
                    Femme.class
            ).setMaxResults(1).getResultList();
            return list.isEmpty() ? null : list.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null) session.close();
        }
    }

    public void afficherFemmePlusAgee() {
        Femme f = getFemmePlusAgee();
        if (f == null) {
            System.out.println("Aucune femme trouvée en base de données.");
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        System.out.println("Femme la plus âgée : " + f.getPrenom() + " " + f.getNom() +
                " (Née le : " + sdf.format(f.getDateNaissance()) + ", Adresse : " + f.getAdresse() + ")");
    }
}
