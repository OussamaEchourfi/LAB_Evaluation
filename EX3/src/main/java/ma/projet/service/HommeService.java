package ma.projet.service;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import ma.projet.beans.Femme;
import ma.projet.beans.Homme;
import ma.projet.beans.Mariage;
import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;

public class HommeService implements IDao<Homme> {

    @Override
    public boolean create(Homme o) {
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
    public boolean update(Homme o) {
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
    public boolean delete(Homme o) {
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
    public Homme findById(int id) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.get(Homme.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public List<Homme> getAll() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.createQuery("from Homme", Homme.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        } finally {
            if (session != null) session.close();
        }
    }

    /**
     * 1. Afficher les épouses d’un homme entre deux dates.
     */
    public List<Femme> getEpousesEntreDates(int hommeId, Date dateDebut, Date dateFin) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.createQuery(
                    "select distinct m.femme from Mariage m " +
                    "where m.homme.id = :hId and m.pk.dateDebut between :d1 and :d2",
                    Femme.class
            ).setParameter("hId", hommeId)
             .setParameter("d1", dateDebut)
             .setParameter("d2", dateFin)
             .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        } finally {
            if (session != null) session.close();
        }
    }

    public void afficherEpousesEntreDates(Homme homme, Date dateDebut, Date dateFin) {
        if (homme == null) {
            System.out.println("Homme null !");
            return;
        }
        afficherEpousesEntreDates(homme.getId(), dateDebut, dateFin);
    }

    public void afficherEpousesEntreDates(int hommeId, Date dateDebut, Date dateFin) {
        Homme h = findById(hommeId);
        if (h == null) {
            System.out.println("Aucun homme trouvé avec l'id : " + hommeId);
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        List<Femme> epouses = getEpousesEntreDates(hommeId, dateDebut, dateFin);

        System.out.println("Épouses de " + h.getPrenom() + " " + h.getNom() +
                " entre le " + sdf.format(dateDebut) + " et le " + sdf.format(dateFin) + " :");
        if (epouses.isEmpty()) {
            System.out.println("Aucune épouse trouvée pour cette période.");
            return;
        }
        System.out.printf("%-5s %-15s %-15s %-15s%n", "ID", "Nom", "Prénom", "Téléphone");
        for (Femme f : epouses) {
            System.out.printf("%-5d %-15s %-15s %-15s%n", f.getId(), f.getNom(), f.getPrenom(), f.getTelephone());
        }
    }

    /**
     * 2. Méthode utilisant l’API Criteria pour afficher le nombre d’hommes mariés à quatre femmes entre deux dates.
     */
    public long countHommesMariesQuatreFemmesEntreDates(Date dateDebut, Date dateFin) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Homme> cq = cb.createQuery(Homme.class);
            Root<Mariage> root = cq.from(Mariage.class);

            Predicate datePredicate = cb.between(root.get("pk").get("dateDebut"), dateDebut, dateFin);
            cq.where(datePredicate);
            cq.groupBy(root.get("homme"));
            cq.having(cb.ge(cb.countDistinct(root.get("femme")), 4L));
            cq.select(root.get("homme"));

            List<Homme> result = session.createQuery(cq).getResultList();
            return result.size();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            if (session != null) session.close();
        }
    }

    public void afficherNombreHommesMariesQuatreFemmesEntreDates(Date dateDebut, Date dateFin) {
        long count = countHommesMariesQuatreFemmesEntreDates(dateDebut, dateFin);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        System.out.println("Nombre d'hommes mariés à 4 femmes entre le " +
                sdf.format(dateDebut) + " et le " + sdf.format(dateFin) + " (API Criteria) : " + count);
    }

    /**
     * 3. Afficher les mariages d’un homme donné, avec les détails (femme, dates, nombre d’enfants).
     * Exemple d’affichage attendu :
     * Nom : SAFI SAID
     * Mariages En Cours :
     * 1. Femme : SALIMA RAMI   Date Début : 03/09/1990    Nbr Enfants : 4
     * 2. Femme : AMAL ALI      Date Début : 03/09/1995    Nbr Enfants : 2
     * 3. Femme : WAFA ALAOUI   Date Début : 04/11/2000    Nbr Enfants : 3
     * 
     * Mariages échoués :
     * 1. Femme : KARIMA ALAMI  Date Début : 03/09/1989    
     * Date Fin : 03/09/1990    Nbr Enfants : 0
     */
    public void afficherMariagesHomme(Homme homme) {
        if (homme == null) {
            System.out.println("Homme null !");
            return;
        }
        afficherMariagesHomme(homme.getId());
    }

    public void afficherMariagesHomme(int hommeId) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Homme h = session.get(Homme.class, hommeId);
            if (h == null) {
                System.out.println("Aucun homme trouvé avec l'id : " + hommeId);
                return;
            }

            List<Mariage> mariages = session.createQuery(
                    "select m from Mariage m join fetch m.femme " +
                    "where m.homme.id = :hId order by m.pk.dateDebut",
                    Mariage.class
            ).setParameter("hId", hommeId).getResultList();

            List<Mariage> enCours = new ArrayList<>();
            List<Mariage> echoues = new ArrayList<>();

            for (Mariage m : mariages) {
                if (m.getDateFin() == null) {
                    enCours.add(m);
                } else {
                    echoues.add(m);
                }
            }

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            System.out.println("Nom : " + h.getNom().toUpperCase() + " " + h.getPrenom().toUpperCase());

            System.out.println("Mariages En Cours :");
            if (enCours.isEmpty()) {
                System.out.println("Aucun mariage en cours.");
            } else {
                int index = 1;
                for (Mariage m : enCours) {
                    String nomFemme = (m.getFemme().getPrenom() + " " + m.getFemme().getNom()).toUpperCase();
                    String dDebut = sdf.format(m.getDateDebut());
                    System.out.printf("%d. Femme : %-15s Date Début : %s    Nbr Enfants : %d%n",
                            index++, nomFemme, dDebut, m.getNbrEnfant());
                }
            }

            System.out.println();
            System.out.println("Mariages échoués :");
            if (echoues.isEmpty()) {
                System.out.println("Aucun mariage échoué.");
            } else {
                int index = 1;
                for (Mariage m : echoues) {
                    String nomFemme = (m.getFemme().getPrenom() + " " + m.getFemme().getNom()).toUpperCase();
                    String dDebut = sdf.format(m.getDateDebut());
                    String dFin = sdf.format(m.getDateFin());
                    System.out.printf("%d. Femme : %-15s Date Début : %s    %n", index++, nomFemme, dDebut);
                    System.out.printf("Date Fin : %s    Nbr Enfants : %d%n", dFin, m.getNbrEnfant());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
    }
}
