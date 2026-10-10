package ma.projet.util;

import java.io.InputStream;
import java.util.Properties;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import ma.projet.classes.Categorie;
import ma.projet.classes.Commande;
import ma.projet.classes.LigneCommandeProduit;
import ma.projet.classes.Produit;

public class HibernateUtil {

    private static SessionFactory sessionFactory;

    static {
        try {
            Configuration configuration = new Configuration();

            // Chargement du fichier application.properties depuis le classpath
            Properties properties = new Properties();
            try (InputStream input = HibernateUtil.class.getClassLoader().getResourceAsStream("application.properties")) {
                if (input != null) {
                    properties.load(input);
                } else {
                    System.err.println("Avertissement: Impossible de trouver application.properties, utilisation des valeurs par défaut.");
                }
            }

            // Normalisation des clés si nécessaire
            if (properties.containsKey("jdbc.driver") && !properties.containsKey("hibernate.connection.driver_class")) {
                properties.setProperty("hibernate.connection.driver_class", properties.getProperty("jdbc.driver"));
            }
            if (properties.containsKey("jdbc.url") && !properties.containsKey("hibernate.connection.url")) {
                properties.setProperty("hibernate.connection.url", properties.getProperty("jdbc.url"));
            }
            if (properties.containsKey("jdbc.user") && !properties.containsKey("hibernate.connection.username")) {
                properties.setProperty("hibernate.connection.username", properties.getProperty("jdbc.user"));
            }
            if (properties.containsKey("jdbc.password") && !properties.containsKey("hibernate.connection.password")) {
                properties.setProperty("hibernate.connection.password", properties.getProperty("jdbc.password"));
            }

            configuration.setProperties(properties);

            // Enregistrement des classes entités
            configuration.addAnnotatedClass(Categorie.class);
            configuration.addAnnotatedClass(Produit.class);
            configuration.addAnnotatedClass(Commande.class);
            configuration.addAnnotatedClass(LigneCommandeProduit.class);

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties())
                    .build();

            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        } catch (Throwable ex) {
            System.err.println("Erreur lors de l'initialisation de la SessionFactory : " + ex);
            ex.printStackTrace();
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
        try {
            com.mysql.cj.jdbc.AbandonedConnectionCleanupThread.checkedShutdown();
        } catch (Throwable ignored) {
        }
    }
}
