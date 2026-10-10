# Exercice 1 — Application de gestion de stock

Application Java / JEE de gestion de stock d'un magasin de produits informatiques avec persistance Hibernate (JPA / Jakarta Persistence) et base de données MySQL.

---

## 📁 Architecture du Projet

Le projet respecte scrupuleusement l'architecture en couches demandée :

```
EX1/
├── pom.xml                                   # Configuration Maven (Hibernate 6, MySQL Connector, JUnit 5)
├── README.md                                 # Documentation du projet
├── Screenshot 2026-10-10 210215.png          # Capture d'écran Tests 1, 2 et 3
├── Screenshot 2026-10-10 210230.png          # Capture d'écran Test 4 (Requête nommée)
├── Screenshot 2026-10-10 210335.png          # Capture d'écran Tests JUnit 5 & Build
├── Screenshot 2026-10-10 210710.png          # Diagramme de classes UML
├── Screenshot 2026-10-10 212709.png          # Capture tables MySQL (phpMyAdmin)
└── src/
    ├── main/
    │   ├── java/
    │   │   └── ma/
    │   │       └── projet/
    │   │           ├── classes/              # Couche Entités (Modèle métier)
    │   │           │   ├── Categorie.java
    │   │           │   ├── Commande.java
    │   │           │   ├── LigneCommandeProduit.java
    │   │           │   ├── LigneCommandeProduitPK.java
    │   │           │   └── Produit.java
    │   │           ├── dao/                  # Couche Accès aux données
    │   │           │   └── IDao.java         # Interface générique IDao<T>
    │   │           ├── service/              # Couche Métier / Services
    │   │           │   ├── CategorieService.java
    │   │           │   ├── CommandeService.java
    │   │           │   ├── LigneCommandeService.java
    │   │           │   └── ProduitService.java
    │   │           ├── util/                 # Utilitaires
    │   │           │   └── HibernateUtil.java # Initialisation de la SessionFactory via application.properties
    │   │           └── test/                 # Programmes de test console
    │   │               ├── TestApp.java      # Programme principal complet avec démonstration
    │   │               ├── TestProduitsParCategorie.java
    │   │               ├── TestProduitsEntreDates.java
    │   │               ├── TestProduitsParCommande.java
    │   │               └── TestRequeteNommee.java
    │   └── resources/
    │       ├── application.properties        # Fichier de configuration Hibernate & base de données
    │       └── simplelogger.properties       # Configuration des logs SLF4J
    └── test/
        └── java/
            └── ma/
                └── projet/
                    └── service/
                        └── ProduitServiceTest.java # Tests automatisés JUnit 5
```

---

## 🗄️ Modèle de Données (UML)
![Screenshot 2026-10-10 210710.png](Screenshot%202026-10-10%20210710.png)

---

## ⚙️ Configuration (`application.properties`)

Le fichier `src/main/resources/application.properties` contient la configuration JDBC et Hibernate :

```properties
hibernate.connection.driver_class=com.mysql.cj.jdbc.Driver
hibernate.connection.url=jdbc:mysql://localhost:3306/stock_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
hibernate.connection.username=root
hibernate.connection.password=

hibernate.dialect=org.hibernate.dialect.MySQLDialect
hibernate.hbm2ddl.auto=update
hibernate.show_sql=false
hibernate.format_sql=true
```

La classe [HibernateUtil.java](file:///d:/DGSI/JEE/TP_7_Evaluation/EX1/src/main/java/ma/projet/util/HibernateUtil.java) charge ce fichier depuis le classpath pour construire la `SessionFactory`.

---
## ♻️ Database

![Screenshot 2026-10-10 212709.png](Screenshot%202026-10-10%20212709.png)
---

## 🚀 Méthodes Implémentées dans `ProduitService`

1. **Afficher les produits par catégorie :**
   ```java
   public void afficherProduitsParCategorie(Categorie categorie);
   public List<Produit> getProduitsParCategorie(Categorie categorie);
   ```

2. **Afficher les produits commandés entre deux dates :**
   ```java
   public void afficherProduitsCommandesEntreDates(Date dateDebut, Date dateFin);
   public List<Produit> getProduitsCommandesEntreDates(Date dateDebut, Date dateFin);
   ```

3. **Afficher les produits commandés dans une commande donnée :**
   ```java
   public void afficherProduitsParCommande(Commande commande);
   public void afficherProduitsParCommande(int commandeId);
   ```
   **Format d'affichage obtenu :**
   ```text
   Commande : 4     Date : 14 Mars 2013
   Liste des produits :
   Référence   Prix    Quantité
   ES12        120 DH  7
   ZR85        100 DH  14
   EE85        200 DH  5
   ```

4. **Afficher les produits dont le prix est supérieur à 100 DH (Requête nommée) :**
   ```java
   public void afficherProduitsPrixSuperieurA100();
   public List<Produit> getProduitsPrixSuperieurA100();
   ```

---

## 📸 Extraits des Résultats d'Exécution (Captures d'écran)

Les captures d'écran des résultats d'exécution enregistrées dans `D:\DGSI\JEE\TP_7_Evaluation\EX1` illustrent la validation complète des exigences fonctionnelles et des tests unitaires :

### 1. Tests 1, 2 et 3 : Produits par catégorie, entre deux dates et par commande

![Tests 1, 2 et 3](Screenshot%202026-10-10%20210215.png)

---

### 2. Test 4 : Affichage des produits avec prix > 100 DH (Requête nommée)

![Test 4 - Requête nommée](Screenshot%202026-10-10%20210230.png)

---

### 3. Tests automatisés JUnit 5 & Succès du Build Maven

![Tests JUnit 5 & Build Success](Screenshot%202026-10-10%20210335.png)


