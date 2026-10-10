package ma.projet.test;

import java.util.List;
import ma.projet.classes.Employe;
import ma.projet.service.EmployeService;
import ma.projet.util.HibernateUtil;

public class TestEmployeService {

    public static void main(String[] args) {
        System.out.println("=== Test : Méthodes EmployeService ===");
        EmployeService employeService = new EmployeService();

        try {
            List<Employe> employes = employeService.getAll();
            if (employes.isEmpty()) {
                System.out.println("Aucun employé en base. Veuillez lancer TestApp d'abord.");
                return;
            }

            for (Employe emp : employes) {
                System.out.println("\n-------------------------------------------------------------");
                employeService.afficherTachesRealisees(emp);
                employeService.afficherProjetsGeres(emp);
            }
        } finally {
            HibernateUtil.shutdown();
        }
    }
}
