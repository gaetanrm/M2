package org.example.refactoring;

public class Personnage {
    private final Arme arme = new Arme();
    private String nom;
    private int vie;
    private boolean mort = false;

    public Personnage() {
        nom = "toto";
        vie = 10;
        arme.setNomArme("Epée en bois");
        arme.setDegatsArme(2);
    }

    public Personnage(String nom, int vie, String nomArme, int degatsArme) {
        this.nom = nom;
        this.vie = vie;
        this.arme.setNomArme(nomArme);
        this.arme.setDegatsArme(degatsArme);
    }

    public void attaquer(Personnage ennemi) {
        if(!isMort()) {
            System.out.println(nom + " attaque " + ennemi.getNom() + " avec " + arme.getNomArme() + " !");
            ennemi.recevoirDegats(arme.getDegatsArme());
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
