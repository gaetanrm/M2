package org.example;

/**
 * @author GARCIA Léa 21702831
 * **/

public class Main {

    public static void main(String[] args) {

        CompteBancaire compteBancaire = new CompteBancaire();
        compteBancaire.methodUsingPublicField();

        System.out.println("Maintenant je suis dans le main et j'affiche l'attribut de l'instance de la classe EncapsulateField : "+compteBancaire.solde);
    }

}