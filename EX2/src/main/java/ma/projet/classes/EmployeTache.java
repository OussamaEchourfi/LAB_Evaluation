package ma.projet.classes;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "employe_tache")
public class EmployeTache implements Serializable {

    @EmbeddedId
    private EmployeTachePK id = new EmployeTachePK();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("employe")
    @JoinColumn(name = "employe_id")
    private Employe employe;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("tache")
    @JoinColumn(name = "tache_id")
    private Tache tache;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_debut_reelle")
    private Date dateDebutReelle;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_fin_reelle")
    private Date dateFinReelle;

    public EmployeTache() {
    }

    public EmployeTache(Employe employe, Tache tache, Date dateDebutReelle, Date dateFinReelle) {
        this.employe = employe;
        this.tache = tache;
        this.dateDebutReelle = dateDebutReelle;
        this.dateFinReelle = dateFinReelle;
        this.id = new EmployeTachePK(
                employe != null ? employe.getId() : 0,
                tache != null ? tache.getId() : 0
        );
    }

    public EmployeTache(EmployeTachePK id, Employe employe, Tache tache, Date dateDebutReelle, Date dateFinReelle) {
        this.id = id;
        this.employe = employe;
        this.tache = tache;
        this.dateDebutReelle = dateDebutReelle;
        this.dateFinReelle = dateFinReelle;
    }

    public EmployeTachePK getId() {
        return id;
    }

    public void setId(EmployeTachePK id) {
        this.id = id;
    }

    public Employe getEmploye() {
        return employe;
    }

    public void setEmploye(Employe employe) {
        this.employe = employe;
        if (employe != null) {
            if (this.id == null) this.id = new EmployeTachePK();
            this.id.setEmploye(employe.getId());
        }
    }

    public Tache getTache() {
        return tache;
    }

    public void setTache(Tache tache) {
        this.tache = tache;
        if (tache != null) {
            if (this.id == null) this.id = new EmployeTachePK();
            this.id.setTache(tache.getId());
        }
    }

    public Date getDateDebutReelle() {
        return dateDebutReelle;
    }

    public void setDateDebutReelle(Date dateDebutReelle) {
        this.dateDebutReelle = dateDebutReelle;
    }

    public Date getDateFinReelle() {
        return dateFinReelle;
    }

    public void setDateFinReelle(Date dateFinReelle) {
        this.dateFinReelle = dateFinReelle;
    }

    @Override
    public String toString() {
        return "EmployeTache{" +
                "id=" + id +
                ", dateDebutReelle=" + dateDebutReelle +
                ", dateFinReelle=" + dateFinReelle +
                '}';
    }
}
