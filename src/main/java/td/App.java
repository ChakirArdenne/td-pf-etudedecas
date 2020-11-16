/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package td;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

import com.google.common.util.concurrent.AtomicDouble;

import td.universite.Annee;
import td.universite.Etudiant;
import td.universite.Matiere;
import td.universite.UE;

public class App {

    static final Predicate<Etudiant> listeEtudiants = x -> true;

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
    private static final Set<Matiere> getAllMatiereFromYear(Annee annee) {
        Set<Matiere> rtr = new HashSet<>();
        for (UE ue : annee.ues()) {
            rtr.addAll(ue.ects().keySet());
        }
        return rtr;
    }
    
    static final Predicate<Etudiant> aDef = etudiant -> {
        Set<Matiere> matiere = getAllMatiereFromYear(etudiant.annee());
        for (Matiere mat :matiere) {
            if(!etudiant.notes().containsKey(mat))
                return true;
        }
        return false;
    };

    //Question 3
    static final  Predicate<Etudiant> aNoteEliminatoire = etudiant -> {
        for (Double note : etudiant.notes().values()) {
            if(note < 6)
                return true;
        }
        return false;
    };


    //Question 4
    private static Map<Matiere, Integer> getMatiereAndEctsOfEtudiant(Etudiant etudiant) {
        Map<Matiere, Integer> rtr = new HashMap<>();
        for (UE ue : etudiant.annee().ues()) {
            rtr.putAll(ue.ects());
        }
        return rtr;
    }

    public static Double moyenne(Etudiant etudiant) {

        if(aDef.test(etudiant))
            return null;

        Double nom = 0.0;
        Double denom = 0.0;
        Map<Matiere, Integer> etudiantMatieresAndEcts = getMatiereAndEctsOfEtudiant(etudiant);

        for (Matiere matiere : etudiantMatieresAndEcts.keySet()) {
            nom += etudiant.notes().get(matiere) * etudiantMatieresAndEcts.get(matiere);
            denom += etudiantMatieresAndEcts.get(matiere);
        }
        return nom / denom;
    }

    //Question 5
    static final Predicate<Etudiant> naPasLaMoyennev1 = e -> moyenne(e) < 10.0;
    //ça plante pour les étudiants défaillant : NullPointerException

    //Question 6
    static final Predicate<Etudiant> naPasLaMoyennev2 = e -> moyenne(e) == null || moyenne(e) < 10.0 ;

    //Question 7
    static final Predicate<Etudiant> session2v1 = e -> aDef.or(aNoteEliminatoire).or(naPasLaMoyennev1).test(e);
    //Si on test naPasLaMoyennev1 en premier on obtient une NullPointerException, faire aDef en premier permet de l'éviter

    //Question 8
    static final Consumer<Etudiant> afficheEtudiant = p -> System.out.println(String.format("%s %s : %s", p.prenom(), p.nom(), moyenne(p) == null ? "défaillant" : String.valueOf(moyenne(p))));

    public static void afficheSiv2(String entete, Predicate<Etudiant> predicate, Annee annee, Consumer<Etudiant> affichageE) {
        System.out.println(entete);
        annee.etudiants().forEach(e -> {
            if(predicate.test(e))
                affichageE.accept(e);
        });
        System.out.println("\n");
    }

    //Question 9
    static final Consumer<Etudiant> afficheEtudiant2 = p -> System.out.println(String.format("%s %s : %s", p.prenom(), p.nom(), moyenneIndicative(p) == null ? "défaillant" : String.valueOf(moyenneIndicative(p))));

    public static Double moyenneIndicative(Etudiant etudiant) {
        AtomicDouble nom = new AtomicDouble(0.0);
        AtomicDouble denom = new AtomicDouble(0.0);
        AtomicDouble note = new AtomicDouble(0.0);
        Map<Matiere, Integer> etudiantMatieresAndEcts = getMatiereAndEctsOfEtudiant(etudiant);

        etudiantMatieresAndEcts.keySet().forEach(matiere -> {
            if(etudiant.notes().containsKey(matiere))
                note.set(etudiant.notes().get(matiere));
            else
                note.set(0.0);

            nom.set(nom.get() + note.get() * etudiantMatieresAndEcts.get(matiere));
            denom.set(denom.get() + etudiantMatieresAndEcts.get(matiere));
        });

        return nom.get() / denom.get();
    }

    //Question 10
    static final Predicate<Etudiant> naPasLaMoyenneGeneralise = e -> {
        if(aDef.test(e))
            return moyenneIndicative(e) == null || moyenneIndicative(e) < 10.0;
        else
            return moyenne(e) == null || moyenne(e) < 10.0 ;
    };

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
        e2.noter(m1, 20.0);
        e2.noter(m3, 20.0);
        Etudiant e3 = new Etudiant("39003", "Charles", "Chaplin", a1);
        e3.noter(m1, 18.0);
        e3.noter(m2, 5.0);
        e3.noter(m3, 14.0);

        //Question 1
        afficheSi("** Tous les etudiants **", listeEtudiants, a1);

        //Question 2
        afficheSi("** Défaillant **", aDef, a1);

        //Question 3
        afficheSi("** ETUDIANTS AVEC NOTE ELIMINATOIRE **", aNoteEliminatoire, a1);

        //Question 4
        System.out.println(moyenne(e2));

        //Question 5
        // afficheSi("** ETUDIANTS SOUS LA MOYENNE **", naPasLaMoyennev1, a1);

        //Question 6
        afficheSi("** ETUDIANTS SOUS LA MOYENNE **", naPasLaMoyennev2, a1);

        //Question 7
        afficheSi("** ETUDIANTS EN SESSION 2 **", session2v1, a1);

        //Question 8
        afficheSiv2("** TOUS LES ETUDIANTS **", listeEtudiants, a1, afficheEtudiant);

        //Question 9
        afficheSiv2("** TOUS LES ETUDIANTS **", listeEtudiants, a1, afficheEtudiant2);

        //Question 10
        afficheSiv2("** TOUS LES ETUDIANTS SOUS LA MOYENNE INDICATIVE **", naPasLaMoyenneGeneralise, a1, afficheEtudiant2);
    }
}
