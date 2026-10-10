package ma.projet.service;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import ma.projet.classes.Employe;
import ma.projet.classes.EmployeTache;
import ma.projet.classes.Projet;
import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;

public class EmployeService implements IDao<Employe> {

    @Override
    public boolean create(Employe o) {
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
    public boolean update(Employe o) {
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
    public boolean delete(Employe o) {
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
    public Employe findById(int id) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.get(Employe.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public List<Employe> getAll() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.createQuery("from Employe", Employe.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        } finally {
            if (session != null) session.close();
        }
    }

    /**
     * 1. Afficher la liste des tâches réalisées par un employé.
     */
    public List<EmployeTache> getTachesRealisees(Employe employe) {
        if (employe == null) return Collections.emptyList();
        return getTachesRealisees(employe.getId());
    }

    public List<EmployeTache> getTachesRealisees(int employeId) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.createQuery(
                    "select et from EmployeTache et join fetch et.tache " +
                    "where et.employe.id = :empId",
                    EmployeTache.class
            ).setParameter("empId", employeId).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        } finally {
            if (session != null) session.close();
        }
    }

    public void afficherTachesRealisees(Employe employe) {
        if (employe == null) {
            System.out.println("Employé null !");
            return;
        }
        afficherTachesRealisees(employe.getId());
    }

    public void afficherTachesRealisees(int employeId) {
        Employe emp = findById(employeId);
        if (emp == null) {
            System.out.println("Aucun employé trouvé avec l'id : " + employeId);
            return;
        }

        List<EmployeTache> taches = getTachesRealisees(employeId);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        System.out.println("Tâches réalisées par l'employé : " + emp.getPrenom() + " " + emp.getNom() + " :");
        System.out.printf("%-5s %-20s %-20s %-20s%n", "Num", "Nom Tâche", "Date Début Réelle", "Date Fin Réelle");

        for (EmployeTache et : taches) {
            String debutStr = et.getDateDebutReelle() != null ? sdf.format(et.getDateDebutReelle()) : "-";
            String finStr = et.getDateFinReelle() != null ? sdf.format(et.getDateFinReelle()) : "-";
            System.out.printf("%-5d %-20s %-20s %-20s%n",
                    et.getTache().getId(),
                    et.getTache().getNom(),
                    debutStr,
                    finStr);
        }
    }

    /**
     * 2. Afficher la liste des projets gérés par un employé.
     */
    public List<Projet> getProjetsGeres(Employe employe) {
        if (employe == null) return Collections.emptyList();
        return getProjetsGeres(employe.getId());
    }

    public List<Projet> getProjetsGeres(int employeId) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.createQuery(
                    "from Projet p where p.chefDeProjet.id = :empId",
                    Projet.class
            ).setParameter("empId", employeId).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        } finally {
            if (session != null) session.close();
        }
    }

    public void afficherProjetsGeres(Employe employe) {
        if (employe == null) {
            System.out.println("Employé null !");
            return;
        }
        afficherProjetsGeres(employe.getId());
    }

    public void afficherProjetsGeres(int employeId) {
        Employe emp = findById(employeId);
        if (emp == null) {
            System.out.println("Aucun employé trouvé avec l'id : " + employeId);
            return;
        }

        List<Projet> projets = getProjetsGeres(employeId);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        System.out.println("Projets gérés par : " + emp.getPrenom() + " " + emp.getNom() + " (Chef de projet) :");
        System.out.printf("%-5s %-25s %-15s %-15s%n", "ID", "Nom Projet", "Date Début", "Date Fin");

        for (Projet p : projets) {
            String debutStr = p.getDateDebut() != null ? sdf.format(p.getDateDebut()) : "-";
            String finStr = p.getDateFin() != null ? sdf.format(p.getDateFin()) : "-";
            System.out.printf("%-5d %-25s %-15s %-15s%n", p.getId(), p.getNom(), debutStr, finStr);
        }
    }
}
