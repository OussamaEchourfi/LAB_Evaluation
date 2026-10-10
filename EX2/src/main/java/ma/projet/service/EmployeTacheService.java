package ma.projet.service;

import java.util.Collections;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import ma.projet.classes.Employe;
import ma.projet.classes.EmployeTache;
import ma.projet.classes.EmployeTachePK;
import ma.projet.classes.Tache;
import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;

public class EmployeTacheService implements IDao<EmployeTache> {

    @Override
    public boolean create(EmployeTache o) {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            if (o.getEmploye() != null && o.getEmploye().getId() > 0) {
                o.setEmploye(session.get(Employe.class, o.getEmploye().getId()));
            }
            if (o.getTache() != null && o.getTache().getId() > 0) {
                o.setTache(session.get(Tache.class, o.getTache().getId()));
            }
            if (o.getId() == null && o.getEmploye() != null && o.getTache() != null) {
                o.setId(new EmployeTachePK(o.getEmploye().getId(), o.getTache().getId()));
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
    public boolean update(EmployeTache o) {
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
    public boolean delete(EmployeTache o) {
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
    public EmployeTache findById(int id) {
        // Clé composite employe_id + tache_id
        return null;
    }

    public EmployeTache findById(EmployeTachePK pk) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.get(EmployeTache.class, pk);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null) session.close();
        }
    }

    public EmployeTache findById(int employeId, int tacheId) {
        return findById(new EmployeTachePK(employeId, tacheId));
    }

    @Override
    public List<EmployeTache> getAll() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.createQuery("from EmployeTache", EmployeTache.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        } finally {
            if (session != null) session.close();
        }
    }

    public List<EmployeTache> getByEmploye(Employe employe) {
        if (employe == null) return Collections.emptyList();
        return getByEmploye(employe.getId());
    }

    public List<EmployeTache> getByEmploye(int employeId) {
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

    public List<EmployeTache> getByTache(Tache tache) {
        if (tache == null) return Collections.emptyList();
        return getByTache(tache.getId());
    }

    public List<EmployeTache> getByTache(int tacheId) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.createQuery(
                    "select et from EmployeTache et join fetch et.employe " +
                    "where et.tache.id = :tacheId",
                    EmployeTache.class
            ).setParameter("tacheId", tacheId).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        } finally {
            if (session != null) session.close();
        }
    }
}
