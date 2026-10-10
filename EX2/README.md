# Exercice 2 — Application de gestion de projets

Application Java / JEE de gestion et de suivi de projets, tâches et temps passé par les employés d'un bureau d'études, développée avec Hibernate (JPA / Jakarta Persistence) et MySQL.

---

## 📁 Architecture du Projet

Le projet respecte scrupuleusement l'architecture en couches demandée :

```
EX2/
├── pom.xml                                   # Configuration Maven (Hibernate 6, MySQL Connector, JUnit 5)
├── README.md                                 # Documentation du projet
├── 7342ae99-4527-4470-938d-ec429222c01c.png # Diagramme de classes UML
├── Screenshot 2026-10-10 213416.png          # Capture tables MySQL (phpMyAdmin)
├── Screenshot 2026-10-10 213953.png          # Capture Tests Employé et Projet
├── Screenshot 2026-10-10 214008.png          # Capture Tests Tâche (Requête nommée & Dates)
├── Screenshot 2026-10-10 214053.png          # Capture Tests JUnit 5 & Build Success
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
    │   │           │   └── HibernateUtil.java # Initialisation de la SessionFactory via application.properties
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

## 🗄️ Modèle de Données (UML)

![Diagramme de classes UML](7342ae99-4527-4470-938d-ec429222c01c.png)

---

## ⚙️ Configuration (`application.properties`)

Le fichier `src/main/resources/application.properties` contient la configuration JDBC et Hibernate pour MySQL :

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
## ♻️Database(`gestion_projets`)

![Structure Base de données MySQL](Screenshot%202026-10-10%20213416.png)

---

## 🚀 Méthodes Implémentées dans la Couche Service

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

## 📸 Extraits des Résultats d'Exécution (Captures d'écran)


### 1. Tests Employé et Projet (Tâches planifiées et Tâches réalisées avec dates réelles)

![Tests Employé et Projet](Screenshot%202026-10-10%20213953.png)

---

### 2. Tests Tâche (Requête nommée > 1000 DH et Tâches réalisées entre deux dates)

![Tests Tâche](Screenshot%202026-10-10%20214008.png)

---

### 3. Tests automatisés JUnit 5 & Succès du Build Maven

![Tests JUnit 5 & Build Success](Screenshot%202026-10-10%20214053.png)
