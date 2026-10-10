package ma.projet.beans;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "mariage")
public class Mariage implements Serializable {

    @EmbeddedId
    private MariagePK pk = new MariagePK();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "homme_id", insertable = false, updatable = false)
    private Homme homme;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "femme_id", insertable = false, updatable = false)
    private Femme femme;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_fin")
    private Date dateFin;

    @Column(name = "nbr_enfant")
    private int nbrEnfant;

    public Mariage() {
    }

    public Mariage(Homme homme, Femme femme, Date dateDebut, Date dateFin, int nbrEnfant) {
        this.homme = homme;
        this.femme = femme;
        this.dateFin = dateFin;
        this.nbrEnfant = nbrEnfant;
        this.pk = new MariagePK(
                homme != null ? homme.getId() : 0,
                femme != null ? femme.getId() : 0,
                dateDebut
        );
    }

    public Mariage(MariagePK pk, Homme homme, Femme femme, Date dateFin, int nbrEnfant) {
        this.pk = pk;
        this.homme = homme;
        this.femme = femme;
        this.dateFin = dateFin;
        this.nbrEnfant = nbrEnfant;
    }

    public MariagePK getPk() {
        return pk;
    }

    public void setPk(MariagePK pk) {
        this.pk = pk;
    }

    public Homme getHomme() {
        return homme;
    }

    public void setHomme(Homme homme) {
        this.homme = homme;
        if (homme != null) {
            if (this.pk == null) this.pk = new MariagePK();
            this.pk.setHomme(homme.getId());
        }
    }

    public Femme getFemme() {
        return femme;
    }

    public void setFemme(Femme femme) {
        this.femme = femme;
        if (femme != null) {
            if (this.pk == null) this.pk = new MariagePK();
            this.pk.setFemme(femme.getId());
        }
    }

    public Date getDateDebut() {
        return pk != null ? pk.getDateDebut() : null;
    }

    public void setDateDebut(Date dateDebut) {
        if (this.pk == null) this.pk = new MariagePK();
        this.pk.setDateDebut(dateDebut);
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public int getNbrEnfant() {
        return nbrEnfant;
    }

    public void setNbrEnfant(int nbrEnfant) {
        this.nbrEnfant = nbrEnfant;
    }

    @Override
    public String toString() {
        return "Mariage{" +
                "pk=" + pk +
                ", dateFin=" + dateFin +
                ", nbrEnfant=" + nbrEnfant +
                '}';
    }
}
