package ma.projet.beans;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Embeddable
public class MariagePK implements Serializable {

    @Column(name = "homme_id")
    private int homme;

    @Column(name = "femme_id")
    private int femme;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_debut")
    private Date dateDebut;

    public MariagePK() {
    }

    public MariagePK(int homme, int femme, Date dateDebut) {
        this.homme = homme;
        this.femme = femme;
        this.dateDebut = dateDebut;
    }

    public int getHomme() {
        return homme;
    }

    public void setHomme(int homme) {
        this.homme = homme;
    }

    public int getFemme() {
        return femme;
    }

    public void setFemme(int femme) {
        this.femme = femme;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MariagePK mariagePK = (MariagePK) o;
        return homme == mariagePK.homme &&
                femme == mariagePK.femme &&
                Objects.equals(dateDebut, mariagePK.dateDebut);
    }

    @Override
    public int hashCode() {
        return Objects.hash(homme, femme, dateDebut);
    }

    @Override
    public String toString() {
        return "MariagePK{" +
                "homme=" + homme +
                ", femme=" + femme +
                ", dateDebut=" + dateDebut +
                '}';
    }
}
