# TP 7 : Évaluation — Applications Java EE avec Persistance Hibernate (JPA)

Ce dépôt regroupe l'ensemble des **3 exercices d'évaluation** développés en **Java / JEE**, utilisant le framework de mapping objet-relationnel **Hibernate (Jakarta Persistence)** et une base de données relationnelle **MySQL** (serveur local XAMPP).

---

## 📑 Sommaire Général

1. [Aperçu Global et Environnement Technique](#-aperçu-global-et-environnement-technique)
2. [Exercice 1 : Application de gestion de stock (EX1)](#-exercice-1--application-de-gestion-de-stock)
   - [Architecture du Projet](#-architecture-du-projet-ex1)
   - [Modèle de Données (UML)](#-modèle-de-données-uml---ex1)
   - [Configuration Hibernate & MySQL](#-configuration-applicationproperties---ex1)
   - [Structure de la Base de Données (`stock_db`)](#-base-de-données-stock_db)
   - [Méthodes Implémentées dans `ProduitService`](#-méthodes-implémentées-dans-produitservice)
   - [Résultats d'Exécution et Captures d'Écran](#-extraits-des-résultats-dexécution-captures-décran---ex1)
3. [Exercice 2 : Application de gestion de projets (EX2)](#-exercice-2--application-de-gestion-de-projets)
   - [Architecture du Projet](#-architecture-du-projet-ex2)
   - [Modèle de Données (UML)](#-modèle-de-données-uml---ex2)
   - [Configuration Hibernate & MySQL](#-configuration-applicationproperties---ex2)
   - [Structure de la Base de Données (`gestion_projets`)](#-base-de-données-gestion_projets)
   - [Méthodes Implémentées dans la Couche Service](#-méthodes-implémentées-dans-la-couche-service-ex2)
   - [Résultats d'Exécution et Captures d'Écran](#-extraits-des-résultats-dexécution-captures-décran---ex2)
4. [Exercice 3 : Application de gestion de l’état civil (EX3)](#-exercice-3--application-de-gestion-de-létat-civil)
   - [Architecture du Projet](#-architecture-du-projet-ex3)
   - [Modèle de Données (UML)](#-modèle-de-données-uml---ex3)
   - [Configuration Hibernate & MySQL](#-configuration-applicationproperties---ex3)
   - [Structure de la Base de Données (`gestion_etat_civil`)](#-base-de-données-gestion_etat_civil)
   - [Méthodes Implémentées dans la Couche Service](#-méthodes-implémentées-dans-la-couche-service-ex3)
   - [Résultats d'Exécution et Captures d'Écran](#-extraits-des-résultats-dexécution-console-captures-décran---ex3)
5. [Tableau Récapitulatif Global](#-tableau-récapitulatif-global)
6. [Commandes d'Exécution et de Test](#-commandes-dexécution-et-de-test)

---

## 🛠 Aperçu Global et Environnement Technique

| Composant | Spécification / Version |
| :--- | :--- |
| **Langage** | Java SE 21 (OpenJDK 64-Bit) |
| **Gestionnaire de dépendances** | Apache Maven 3.9+ |
| **ORM / JPA** | Hibernate ORM 6.4.4.Final (`jakarta.persistence.*`) |
| **Pilote JDBC** | MySQL Connector/J 8.3.0 |
| **Serveur de Base de Données** | MySQL 8.x / MariaDB (XAMPP Server, Port 3306) |
| **Framework de Tests** | JUnit 5 Jupiter 5.10.2 |

---

# 📦 Exercice 1 — Application de gestion de stock

**Contexte :**  
Développer une application pour gérer le stock d'un magasin de produits informatiques. Le système gère les catégories de produits, les commandes des clients, ainsi que les lignes de commande avec quantité et prix unitaire.

---

## 📁 Architecture du Projet (EX1)

Le projet respecte scrupuleusement l'architecture en couches demandée :

```
EX1/
├── pom.xml                                   # Configuration Maven (Hibernate 6, MySQL Connector, JUnit 5)
├── README.md                                 # Documentation individuelle de l'exercice 1
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
    │   │           │   └── HibernateUtil.java # SessionFactory via application.properties
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

## 🗄️ Modèle de Données (UML - EX1)

![Diagramme de classes UML EX1](EX1/Screenshot%202026-10-10%20210710.png)

---

## ⚙️ Configuration (`application.properties` - EX1)

Le fichier `EX1/src/main/resources/application.properties` configure la connexion JDBC et le comportement d'Hibernate :

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

## ♻️ Base de Données (`stock_db`)

Tables générées automatiquement par Hibernate avec clés étrangères et contraintes d'intégrité :

![Structure Base de données MySQL EX1](EX1/Screenshot%202026-10-10%20212709.png)

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

## 📸 Extraits des Résultats d'Exécution (Captures d'écran - EX1)

### 1. Tests 1, 2 et 3 : Produits par catégorie, entre deux dates et par commande

![Tests 1, 2 et 3](EX1/Screenshot%202026-10-10%20210215.png)

---

### 2. Test 4 : Affichage des produits avec prix > 100 DH (Requête nommée)

![Test 4 - Requête nommée](EX1/Screenshot%202026-10-10%20210230.png)

---

### 3. Tests automatisés JUnit 5 & Succès du Build Maven

![Tests JUnit 5 & Build Success](EX1/Screenshot%202026-10-10%20210335.png)

---
---

# 📊 Exercice 2 — Application de gestion de projets

**Contexte :**  
Un bureau d’études souhaite suivre le temps passé sur les projets et calculer les coûts globaux. L'application gère les employés, les projets (avec un chef de projet associé), les tâches planifiées et l'association `EmployeTache` qui enregistre les dates réelles de début et de fin de chaque intervention.

---

## 📁 Architecture du Projet (EX2)

Le projet respecte scrupuleusement l'architecture en couches demandée :

```
EX2/
├── pom.xml                                   # Configuration Maven (Hibernate 6, MySQL Connector, JUnit 5)
├── README.md                                 # Documentation individuelle de l'exercice 2
└── src/
    ├── main/
    │   ├── java/
    │   │   └── ma/
    │   │       └── projet/
    │   │           ├── classes/              # Couche Entités (Modèle métier)
    │   │           │   ├── Employe.java
    │   │           │   ├── EmployeTache.java
    │   │           │   ├── EmployeTachePK.java
    │   │           │   ├── Projet.java
    │   │           │   └── Tache.java
    │   │           ├── dao/                  # Couche Accès aux données
    │   │           │   └── IDao.java         # Interface générique IDao<T>
    │   │           ├── service/              # Couche Métier / Services
    │   │           │   ├── EmployeService.java
    │   │           │   ├── EmployeTacheService.java
    │   │           │   ├── ProjetService.java
    │   │           │   └── TacheService.java
    │   │           ├── util/                 # Utilitaires
    │   │           │   └── HibernateUtil.java # SessionFactory via application.properties
    │   │           └── test/                 # Programmes de test console
    │   │               ├── TestApp.java      # Démonstration complète avec les données du sujet
    │   │               ├── TestEmployeService.java
    │   │               ├── TestProjetService.java
    │   │               └── TestTacheService.java
    │   └── resources/
    │       ├── application.properties        # Fichier de configuration Hibernate & base de données
    │       └── simplelogger.properties       # Configuration des logs SLF4J
    └── test/
        └── java/
            └── ma/
                └── projet/
                    └── service/
                        └── GestionProjetServiceTest.java # Tests automatisés JUnit 5
```

---

## 🗄️ Modèle de Données (UML - EX2)

![Diagramme de classes UML EX2](EX2/7342ae99-4527-4470-938d-ec429222c01c.png)

---

## ⚙️ Configuration (`application.properties` - EX2)

Le fichier `EX2/src/main/resources/application.properties` contient la configuration JDBC et Hibernate pour MySQL :

```properties
hibernate.connection.driver_class=com.mysql.cj.jdbc.Driver
hibernate.connection.url=jdbc:mysql://localhost:3306/gestion_projets?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
hibernate.connection.username=root
hibernate.connection.password=

hibernate.dialect=org.hibernate.dialect.MySQLDialect
hibernate.hbm2ddl.auto=update
hibernate.show_sql=false
hibernate.format_sql=true
```

La classe [HibernateUtil.java](file:///d:/DGSI/JEE/TP_7_Evaluation/EX2/src/main/java/ma/projet/util/HibernateUtil.java) charge ce fichier depuis le classpath pour construire la `SessionFactory`.

---

## ♻️ Base de Données (`gestion_projets`)

Tables créées et liées avec les clés primaires et étrangères :

![Structure Base de données MySQL EX2](EX2/Screenshot%202026-10-10%20213416.png)

---

## 🚀 Méthodes Implémentées dans la Couche Service (EX2)

### 1. Dans `EmployeService` :
- **Afficher la liste des tâches réalisées par un employé :**
  ```java
  public void afficherTachesRealisees(Employe employe);
  public void afficherTachesRealisees(int employeId);
  public List<EmployeTache> getTachesRealisees(int employeId);
  ```
- **Afficher la liste des projets gérés par un employé (Chef de projet) :**
  ```java
  public void afficherProjetsGeres(Employe employe);
  public void afficherProjetsGeres(int employeId);
  public List<Projet> getProjetsGeres(int employeId);
  ```

### 2. Dans `ProjetService` :
- **Afficher la liste des tâches planifiées pour un projet :**
  ```java
  public void afficherTachesPlanifiees(Projet projet);
  public void afficherTachesPlanifiees(int projetId);
  public List<Tache> getTachesPlanifiees(int projetId);
  ```
- **Afficher la liste des tâches réalisées avec les dates réelles :**
  ```java
  public void afficherTachesRealiseesAvecDates(Projet projet);
  public void afficherTachesRealiseesAvecDates(int projetId);
  public List<EmployeTache> getTachesRealiseesParProjet(int projetId);
  ```
  **Format d'affichage obtenu :**
  ```text
  Projet : 4      Nom : Gestion de stock     Date début : 14 Janvier 2013
  Liste des tâches:
  Num Nom            Date Début Réelle   Date Fin Réelle
  12  Analyse        10/02/2013          20/02/2013
  13  Conception     10/03/2013          15/03/2013
  14  Développement  10/04/2013          25/04/2013
  ```

### 3. Dans `TacheService` :
- **Afficher les tâches dont le prix est supérieur à 1000 DH (requête nommée) :**
  ```java
  public void afficherTachesPrixSuperieurA1000();
  public List<Tache> getTachesPrixSuperieurA1000();
  ```
- **Afficher les tâches réalisées entre deux dates :**
  ```java
  public void afficherTachesRealiseesEntreDates(Date dateDebut, Date dateFin);
  public List<EmployeTache> getTachesRealiseesEntreDates(Date dateDebut, Date dateFin);
  ```

---

## 📸 Extraits des Résultats d'Exécution (Captures d'écran - EX2)

### 1. Tests Employé et Projet (Tâches planifiées et Tâches réalisées avec dates réelles)

![Tests Employé et Projet](EX2/Screenshot%202026-10-10%20213953.png)

---

### 2. Tests Tâche (Requête nommée > 1000 DH et Tâches réalisées entre deux dates)

![Tests Tâche](EX2/Screenshot%202026-10-10%20214008.png)

---

### 3. Tests automatisés JUnit 5 & Succès du Build Maven

![Tests JUnit 5 & Build Success](EX2/Screenshot%202026-10-10%20214053.png)

---
---

# 💍 Exercice 3 — Application de gestion de l’état civil

**Contexte :**  
Créer une application permettant de gérer les citoyens et leurs relations matrimoniales dans une province. La modélisation exploite la stratégie d'héritage `InheritanceType.JOINED` (`Personne` -> `Homme` / `Femme`) et l'entité d'association `Mariage` avec dates d'union, statut (en cours ou échoué) et nombre d'enfants.

---

## 📁 Architecture du Projet (EX3)

Le projet respecte scrupuleusement l'architecture en couches demandée :

```
EX3/
├── pom.xml                                   # Configuration Maven (Hibernate 6, MySQL Connector, JUnit 5)
├── README.md                                 # Documentation individuelle de l'exercice 3
└── src/
    ├── main/
    │   ├── java/
    │   │   └── ma/
    │   │       └── projet/
    │   │           ├── beans/                # Couche Entités (Héritage JOINED et Associations)
    │   │           │   ├── Personne.java     # Classe abstraite de base
    │   │           │   ├── Homme.java
    │   │           │   ├── Femme.java
    │   │           │   ├── Mariage.java
    │   │           │   └── MariagePK.java
    │   │           ├── dao/                  # Couche Accès aux données
    │   │           │   └── IDao.java         # Interface générique IDao<T>
    │   │           ├── service/              # Couche Métier / Services
    │   │           │   ├── FemmeService.java
    │   │           │   ├── HommeService.java
    │   │           │   └── MariageService.java
    │   │           ├── util/                 # Utilitaires
    │   │           │   └── HibernateUtil.java # SessionFactory via application.properties
    │   │           └── test/                 # Programmes de test console
    │   │               ├── TestApp.java      # Démonstration complète avec les données du sujet
    │   │               ├── TestFemmeService.java
    │   │               └── TestHommeService.java
    │   └── resources/
    │       ├── application.properties        # Fichier de configuration Hibernate & base de données
    │       └── simplelogger.properties       # Configuration des logs SLF4J
    └── test/
        └── java/
            └── ma/
                └── projet/
                    └── service/
                        └── EtatCivilServiceTest.java # Tests automatisés JUnit 5
```

---

## 🗄️ Modèle de Données (UML - EX3)

![Diagramme de classes UML EX3](EX3/f0f3e0ea-f12f-4308-9b2d-f59676c45bbc.png)

---

## ⚙️ Configuration (`application.properties` - EX3)

Le fichier `EX3/src/main/resources/application.properties` contient la configuration JDBC et Hibernate pour MySQL :

```properties
hibernate.connection.driver_class=com.mysql.cj.jdbc.Driver
hibernate.connection.url=jdbc:mysql://localhost:3306/gestion_etat_civil?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
hibernate.connection.username=root
hibernate.connection.password=

hibernate.dialect=org.hibernate.dialect.MySQLDialect
hibernate.hbm2ddl.auto=update
hibernate.show_sql=false
hibernate.format_sql=true
```

La classe [HibernateUtil.java](file:///d:/DGSI/JEE/TP_7_Evaluation/EX3/src/main/java/ma/projet/util/HibernateUtil.java) charge ce fichier depuis le classpath pour construire la `SessionFactory`.

---

## ♻️ Base de Données (`gestion_etat_civil`)

Schéma relationnel généré avec table parent `personne` et tables enfants `homme` et `femme` :

![Structure Base de données MySQL EX3](EX3/Screenshot%202026-10-10%20220102.png)

---

## 🚀 Méthodes Implémentées dans la Couche Service (EX3)

### 1. Dans `HommeService` :
- **Afficher les épouses d’un homme entre deux dates :**
  ```java
  public void afficherEpousesEntreDates(Homme homme, Date dateDebut, Date dateFin);
  public List<Femme> getEpousesEntreDates(int hommeId, Date dateDebut, Date dateFin);
  ```
- **Afficher le nombre d’hommes mariés à 4 femmes entre deux dates (API Criteria) :**
  ```java
  public void afficherNombreHommesMariesQuatreFemmesEntreDates(Date dateDebut, Date dateFin);
  public long countHommesMariesQuatreFemmesEntreDates(Date dateDebut, Date dateFin);
  ```
- **Afficher les mariages d’un homme donné avec tous les détails (en cours et échoués) :**
  ```java
  public void afficherMariagesHomme(Homme homme);
  public void afficherMariagesHomme(int hommeId);
  ```
  **Format d'affichage obtenu :**
  ```text
  Nom : SAFI SAID
  Mariages En Cours :
  1. Femme : SALIMA RAMI   Date Début : 03/09/1990    Nbr Enfants : 4
  2. Femme : AMAL ALI      Date Début : 03/09/1995    Nbr Enfants : 2
  3. Femme : WAFA ALAOUI   Date Début : 04/11/2000    Nbr Enfants : 3

  Mariages échoués :
  1. Femme : KARIMA ALAMI  Date Début : 03/09/1989    
  Date Fin : 03/09/1990    Nbr Enfants : 0
  ```

### 2. Dans `FemmeService` :
- **Nombre d’enfants d’une femme entre deux dates (Requête native nommée) :**
  ```java
  public void afficherNombreEnfantsEntreDates(Femme femme, Date dateDebut, Date dateFin);
  public int countEnfantsBetweenDates(int femmeId, Date dateDebut, Date dateFin);
  ```
- **Femmes mariées au moins deux fois (Requête nommée) :**
  ```java
  public void afficherFemmesMarieesAuMoinsDeuxFois();
  public List<Femme> getFemmesMarieesAuMoinsDeuxFois();
  ```
- **Afficher la femme la plus âgée :**
  ```java
  public void afficherFemmePlusAgee();
  public Femme getFemmePlusAgee();
  ```

---

## 📸 Extraits des Résultats d'Exécution Console (Captures d'écran - EX3)

**TEST 1 : Liste de toutes les femmes**
![TEST 1 - Liste des femmes](EX3/Screenshot%202026-10-10%20220449.png)

**TEST 2 : Affichage de la femme la plus âgée**
![TEST 2 - Femme la plus âgée](EX3/Screenshot%202026-10-10%20220514.png)

**TEST 3 : Épouses de SAFI Said entre 1988 et 2002**
![TEST 3 - Épouses entre deux dates](EX3/Screenshot%202026-10-10%20220522.png)

**TEST 4 : Nombre d'enfants d'une femme entre deux dates (Requête native nommée)**
![TEST 4 - Nombre d'enfants](EX3/Screenshot%202026-10-10%20220531.png)

**TEST 5 : Femmes mariées au moins deux fois (Requête nommée)**
![TEST 5 - Femmes mariées au moins deux fois](EX3/Screenshot%202026-10-10%20220539.png)

**TEST 6 : Hommes mariés à 4 femmes entre deux dates (API Criteria)**
![TEST 6 - Hommes mariés à 4 femmes](EX3/Screenshot%202026-10-10%20220545.png)

**TEST 7 : Mariages d'un homme avec tous les détails**
![TEST 7 - Mariages d'un homme avec détails](EX3/Screenshot%202026-10-10%20220554.png)

---

### Validation des Tests Unitaires JUnit 5 (EX3)

![Validation Tests JUnit 5 EX3](EX3/Screenshot%202026-10-10%20220639.png)

---
---

## 📊 Tableau Récapitulatif Global

| Critère | Exercice 1 (Stock) | Exercice 2 (Projets) | Exercice 3 (État Civil) |
| :--- | :--- | :--- | :--- |
| **Package Entités** | `ma.projet.classes` | `ma.projet.classes` | `ma.projet.beans` |
| **Entités** | `Categorie`, `Produit`, `Commande`, `LigneCommandeProduit` | `Employe`, `Projet`, `Tache`, `EmployeTache` | `Personne` (abstraite), `Homme`, `Femme`, `Mariage` |
| **Relations Clés** | `@ManyToOne`, `@OneToMany`, `@EmbeddedId` | `@ManyToOne`, `@OneToMany`, `@EmbeddedId` | `@Inheritance(JOINED)`, `@EmbeddedId` |
| **Base de Données** | `stock_db` | `gestion_projets` | `gestion_etat_civil` |
| **Techniques Hibernate** | Requête JPQL nommée (`@NamedQuery`) | Requête JPQL nommée (`@NamedQuery`), Jointures | API Criteria, Requête Nommée JPQL, Requête Nommée Native SQL (`@NamedNativeQuery`) |
| **Tests Automatisés** | `ProduitServiceTest` (JUnit 5) | `GestionProjetServiceTest` (JUnit 5) | `EtatCivilServiceTest` (JUnit 5) |
| **Statut du Build** | `BUILD SUCCESS` | `BUILD SUCCESS` | `BUILD SUCCESS` |

---

## 💻 Commandes d'Exécution et de Test

Pour compiler, exécuter les tests automatisés ou lancer les applications de démonstration :

### Exercice 1 (Stock)
```bash
cd D:/DGSI/JEE/TP_7_Evaluation/EX1
mvn clean test
mvn exec:java -Dexec.mainClass="ma.projet.test.TestApp"
```

### Exercice 2 (Projets)
```bash
cd D:/DGSI/JEE/TP_7_Evaluation/EX2
mvn clean test
mvn exec:java -Dexec.mainClass="ma.projet.test.TestApp"
```

### Exercice 3 (État Civil)
```bash
cd D:/DGSI/JEE/TP_7_Evaluation/EX3
mvn clean test
mvn exec:java -Dexec.mainClass="ma.projet.test.TestApp"
```
