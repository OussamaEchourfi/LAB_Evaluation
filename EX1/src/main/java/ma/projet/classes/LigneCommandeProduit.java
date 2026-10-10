package ma.projet.classes;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "ligne_commande_produit")
public class LigneCommandeProduit implements Serializable {

    @EmbeddedId
    private LigneCommandeProduitPK id = new LigneCommandeProduitPK();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("produit")
    @JoinColumn(name = "produit_id")
    private Produit produit;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("commande")
    @JoinColumn(name = "commande_id")
    private Commande commande;

    @Column(name = "quantite", nullable = false)
    private int quantite;

    public LigneCommandeProduit() {
    }

    public LigneCommandeProduit(Produit produit, Commande commande, int quantite) {
        this.produit = produit;
        this.commande = commande;
        this.quantite = quantite;
        this.id = new LigneCommandeProduitPK(
                produit != null ? produit.getId() : 0,
                commande != null ? commande.getId() : 0
        );
    }

    public LigneCommandeProduit(LigneCommandeProduitPK id, Produit produit, Commande commande, int quantite) {
        this.id = id;
        this.produit = produit;
        this.commande = commande;
        this.quantite = quantite;
    }

    public LigneCommandeProduitPK getId() {
        return id;
    }

    public void setId(LigneCommandeProduitPK id) {
        this.id = id;
    }

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
        if (produit != null) {
            if (this.id == null) this.id = new LigneCommandeProduitPK();
            this.id.setProduit(produit.getId());
        }
    }

    public Commande getCommande() {
        return commande;
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
        if (commande != null) {
            if (this.id == null) this.id = new LigneCommandeProduitPK();
            this.id.setCommande(commande.getId());
        }
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    @Override
    public String toString() {
        return "LigneCommandeProduit{" +
                "id=" + id +
                ", quantite=" + quantite +
                '}';
    }
}
