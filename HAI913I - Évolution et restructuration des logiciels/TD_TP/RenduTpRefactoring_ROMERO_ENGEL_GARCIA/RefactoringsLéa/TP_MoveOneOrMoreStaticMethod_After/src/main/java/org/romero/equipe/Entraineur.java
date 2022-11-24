package org.romero.equipe;

public class Entraineur {

    private String firstname;
    private String surname;
    private String composition;

    public Entraineur(String firstname, String surname) {
        this.firstname = firstname;
        this.surname = surname;
        this.composition = "";
    }

    /*
    * Cette méthode n'est pas située au bon endroit
    * Ici, cela signifie qu'un joueur choisit la stratégie de jeu
    * Alors que ca devrait être l'entraîneur
    * On doit utiliser : Move one or more static method */
    public static void choisirStrategie() {
        System.out.println("Nouvelle stratégie choisie !");
    }

    public String newComposition(String newComposition) {
        this.composition = newComposition;
        return this.composition;
    }
}
