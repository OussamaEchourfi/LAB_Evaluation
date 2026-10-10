package ma.projet.service;

import java.util.Collections;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import ma.projet.beans.Femme;
import ma.projet.beans.Homme;
import ma.projet.beans.Mariage;
import ma.projet.beans.MariagePK;
import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;

public class MariageService implements IDao<Mariage> {

    @Override
    public boolean create(Mariage o) {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            if (o.getHomme() != null && o.getHomme().getId() > 0) {
                o.setHomme(session.get(Homme.class, o.getHomme().getId()));
            }
            if (o.getFemme() != null && o.getFemme().getId() > 0) {
                o.setFemme(session.get(Femme.class, o.getFemme().getId()));
            }
            if (o.getPk() == null && o.getHomme() != null && o.getFemme() != null) {
                o.setPk(new MariagePK(o.getHomme().getId(), o.getFemme().getId(), o.getDateDebut()));
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
    public boolean update(Mariage o) {
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
    public boolean delete(Mariage o) {
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
    public Mariage findById(int id) {
        // Clé composite homme_id + femme_id + date_debut
        return null;
    }

    public Mariage findById(MariagePK pk) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.get(Mariage.class, pk);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public List<Mariage> getAll() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.createQuery(
                    "select m from Mariage m join fetch m.homme join fetch m.femme",
                    Mariage.class
            ).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        } finally {
            if (session != null) session.close();
        }
    }

    public List<Mariage> getByHomme(Homme homme) {
        if (homme == null) return Collections.emptyList();
        return getByHomme(homme.getId());
    }

    public List<Mariage> getByHomme(int hommeId) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.createQuery(
                    "select m from Mariage m join fetch m.femme where m.homme.id = :hId order by m.pk.dateDebut",
                    Mariage.class
            ).setParameter("hId", hommeId).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        } finally {
            if (session != null) session.close();
        }
    }

    public List<Mariage> getByFemme(Femme femme) {
        if (femme == null) return Collections.emptyList();
        return getByFemme(femme.getId());
    }

    public List<Mariage> getByFemme(int femmeId) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.createQuery(
                    "select m from Mariage m join fetch m.homme where m.femme.id = :fId order by m.pk.dateDebut",
                    Mariage.class
            ).setParameter("fId", femmeId).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        } finally {
            if (session != null) session.close();
        }
    }
}
