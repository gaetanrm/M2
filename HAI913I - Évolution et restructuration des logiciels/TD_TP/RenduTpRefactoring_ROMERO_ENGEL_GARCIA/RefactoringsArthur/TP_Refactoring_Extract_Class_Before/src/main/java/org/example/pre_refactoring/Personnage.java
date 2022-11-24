package org.example.pre_refactoring;

public class Personnage {
    private String nom;
    private int vie;
    private boolean mort = false;

    //TODO : extraire nomArme et degatsArme dans une classe Arme, et la déléguer au Personnage
    private String nomArme;
    private int degatsArme;

    public Personnage() {
        nom = "toto";
        vie = 10;
        nomArme = "Epée en bois";
        degatsArme = 2;
    }

    public Personnage(String nom, int vie, String nomArme, int degatsArme) {
        this.nom = nom;
        this.vie = vie;
        this.nomArme = nomArme;
        this.degatsArme = degatsArme;
    }

    public void attaquer(Personnage ennemi) {
        if(!isMort()) {
            System.out.println(nom + " attaque " + ennemi.getNom() + " avec " + nomArme + " !");
            ennemi.recevoirDegats(degatsArme);
        }
    }

    public void recevoirDegats(int degats) {
        if(!isMort()) {
            vie = getVie() - degats;
            System.out.println(nom + " reçoit " + degats + " points de dégats");
            if(getVie() <= 0) {
                mourir();
            }
        }
    }

    public void mourir() {
        mort = true;
        System.out.println(nom + " : Arrrgh!");
    }

    public String getNom() {
        return nom;
    }

    public int getVie() {
        return vie;
    }

    public boolean isMort() {
        return mort;
    }
}
