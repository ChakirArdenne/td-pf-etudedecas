/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package td;

import java.util.Map;
import java.util.Set;

import javax.lang.model.element.Element;

import com.google.common.base.Predicate;

import td.universite.Annee;
import td.universite.Etudiant;
import td.universite.Matiere;
import td.universite.UE;

public class App {

    final static Predicate<Etudiant> listeEtudiants = x -> true;

    //Question 1
    public static void afficheSi(String entete, Predicate<Etudiant> predicate, Annee anne) {
        System.out.println(entete);
        //version for
        // for (Etudiant etudiant : anne.etudiants()) {
        //     if(predicate.test(etudiant)) {
        //         System.out.println(etudiant);
        //     }
        // }

        //version forEach
        anne.etudiants().forEach( (etudiant) -> {
            if(predicate.test(etudiant))
                System.out.println(etudiant);
        });
    }

    //Question 2

    public static void main(String[] args) {

        Matiere m1 = new Matiere("MAT1");
        Matiere m2 = new Matiere("MAT2");
        UE ue1 = new UE("UE1", Map.of(m1, 2, m2, 2));
        Matiere m3 = new Matiere("MAT3");
        UE ue2 = new UE("UE2", Map.of(m3, 1));
        Annee a1 = new Annee(Set.of(ue1, ue2));
        Etudiant e1 = new Etudiant("39001", "Alice", "Merveille", a1);
        e1.noter(m1, 12.0);
        e1.noter(m2, 14.0);
        e1.noter(m3, 10.0);
        System.out.println(e1);
        Etudiant e2 = new Etudiant("39002", "Bob", "Eponge", a1);
        e2.noter(m1, 14.0);
        e2.noter(m3, 14.0);
        Etudiant e3 = new Etudiant("39003", "Charles", "Chaplin", a1);
        e3.noter(m1, 18.0);
        e3.noter(m2, 5.0);
        e3.noter(m3, 14.0);

        afficheSi("** Tous les etudiants **", listeEtudiants, a1);
    }
}
