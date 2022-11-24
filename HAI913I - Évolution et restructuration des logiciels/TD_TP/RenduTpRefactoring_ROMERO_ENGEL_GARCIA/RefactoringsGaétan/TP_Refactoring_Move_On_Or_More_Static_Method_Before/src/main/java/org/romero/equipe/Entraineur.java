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

    public String newComposition(String newComposition) {
        this.composition = newComposition;
        return this.composition;
    }
}
