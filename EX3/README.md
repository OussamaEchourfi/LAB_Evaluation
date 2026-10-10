# Exercice 3 — Application de gestion de l’état civil

Application Java / JEE permettant de gérer les citoyens et leurs relations matrimoniales dans une province avec persistance Hibernate (JPA / Jakarta Persistence) et base de données MySQL.

---

## 📁 Architecture du Projet

Le projet respecte scrupuleusement l'architecture en couches demandée :

```
EX3/
├── pom.xml                                   # Configuration Maven (Hibernate 6, MySQL Connector, JUnit 5)
├── README.md                                 # Documentation du projet
├── f0f3e0ea-f12f-4308-9b2d-f59676c45bbc.png # Diagramme de classes UML
├── Screenshot 2026-10-10 220102.png          # Capture tables MySQL (phpMyAdmin)
├── Screenshot 2026-10-10 220449.png          # Test 1 : Liste de toutes les femmes
├── Screenshot 2026-10-10 220514.png          # Test 2 : Femme la plus âgée
├── Screenshot 2026-10-10 220522.png          # Test 3 : Épouses entre deux dates
├── Screenshot 2026-10-10 220531.png          # Test 4 : Enfants d'une femme (Native query)
├── Screenshot 2026-10-10 220539.png          # Test 5 : Femmes mariées >= 2 fois
├── Screenshot 2026-10-10 220545.png          # Test 6 : Hommes mariés à 4 femmes (Criteria)
├── Screenshot 2026-10-10 220554.png          # Test 7 : Mariages d'un homme détaillés
├── Screenshot 2026-10-10 220639.png          # Tests automatisés JUnit 5 & Build
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
    │   │           │   └── HibernateUtil.java # Initialisation de la SessionFactory via application.properties
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

## 🗄️ Modèle de Données (UML)

![f0f3e0ea-f12f-4308-9b2d-f59676c45bbc.png](f0f3e0ea-f12f-4308-9b2d-f59676c45bbc.png)
---

## ⚙️ Configuration (`application.properties`)

Le fichier `src/main/resources/application.properties` contient la configuration JDBC et Hibernate pour MySQL :

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
![Screenshot 2026-10-10 220102.png](Screenshot%202026-10-10%20220102.png)
---
## 🚀 Méthodes Implémentées dans la Couche Service

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

## 📸 Extraits des Résultats d'Exécution Console



**TEST 1 : Liste de toutes les femmes**

![Screenshot 2026-10-10 220449.png](Screenshot%202026-10-10%20220449.png)

**TEST 2 : Affichage de la femme la plus âgée**

![Screenshot 2026-10-10 220514.png](Screenshot%202026-10-10%20220514.png)

**TEST 3 : Épouses de SAFI Said entre 1988 et 2002**

![Screenshot 2026-10-10 220522.png](Screenshot%202026-10-10%20220522.png)

**TEST 4 : Nombre d'enfants d'une femme entre deux dates (Requête native nommée)**

![Screenshot 2026-10-10 220531.png](Screenshot%202026-10-10%20220531.png)

**TEST 5 : Femmes mariées au moins deux fois (Requête nommée)**

![Screenshot 2026-10-10 220539.png](Screenshot%202026-10-10%20220539.png)

**TEST 6 : Hommes mariés à 4 femmes entre deux dates (API Criteria)**

![Screenshot 2026-10-10 220545.png](Screenshot%202026-10-10%20220545.png)

**TEST 7 : Mariages d'un homme avec tous les détails**

![Screenshot 2026-10-10 220554.png](Screenshot%202026-10-10%20220554.png)

## Validation des Tests Unitaires JUnit 5

![Screenshot 2026-10-10 220639.png](Screenshot%202026-10-10%20220639.png)