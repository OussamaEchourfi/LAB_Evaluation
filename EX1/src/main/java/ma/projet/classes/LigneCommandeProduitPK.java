package ma.projet.classes;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class LigneCommandeProduitPK implements Serializable {

    @Column(name = "produit_id")
    private int produit;

    @Column(name = "commande_id")
    private int commande;

    public LigneCommandeProduitPK() {
    }

    public LigneCommandeProduitPK(int produit, int commande) {
        this.produit = produit;
        this.commande = commande;
    }

    public int getProduit() {
        return produit;
    }

    public void setProduit(int produit) {
        this.produit = produit;
    }

    public int getCommande() {
        return commande;
    }

    public void setCommande(int commande) {
        this.commande = commande;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LigneCommandeProduitPK that = (LigneCommandeProduitPK) o;
        return produit == that.produit && commande == that.commande;
    }

    @Override
    public int hashCode() {
        return Objects.hash(produit, commande);
    }

    @Override
    public String toString() {
        return "LigneCommandeProduitPK{" +
                "produit=" + produit +
                ", commande=" + commande +
                '}';
    }
}
