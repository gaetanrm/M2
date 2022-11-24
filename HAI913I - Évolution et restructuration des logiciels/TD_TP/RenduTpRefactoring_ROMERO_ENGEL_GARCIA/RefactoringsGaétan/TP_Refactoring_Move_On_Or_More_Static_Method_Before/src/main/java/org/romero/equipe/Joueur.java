package org.romero.equipe;

public class Joueur {

    private String firstname;
    private String surname;
    private String poste;

    public Joueur(String firstname, String surname, String poste) {
        this.firstname = firstname;
        this.surname = surname;
        this.poste = poste;
    }
    /*
    * Cette méthode n'est pas située au bon endroit
    * Ici, cela signifie qu'un joueur choisit la stratégie de jeu
    * Alors que ca devrait être l'entraîneur*/
    public static void choisirStrategie() {
        System.out.println("Nouvelle stratégie choisie !");
    }
}