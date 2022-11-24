package org.example;

public class CompteBancaire {

    //solde = Champ public auquel on accède librement depuis les méthodes de la classe et le main de la classe Main

    //Ce champ a une visibilité publique, sans getter ni setter et est accessible depuis toute l'application directement
    //    //Opération de refactoring visée : Encapsulate Field
    //Résultat : Rendre cet attribut privé, lui créer un getter et setter et remplacer tous les accès direct par le getter
    public int solde;

    //Méthode qui utilise field
    public void methodUsingPublicField() {

        System.out.println("Je suis la classe EncapsulateField et mon attribut est : "+solde);

    }

}

