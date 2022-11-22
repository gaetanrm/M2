package org.romero.equipe;

import java.util.ArrayList;
import java.util.List;

public class Equipe {

    private Entraineur entraineur;
    private List<Joueur> listJoueur = new ArrayList<>();

    private int nbMatchGagne = 0;
    private int nbMatchPerdu = 0;

    public Equipe(Entraineur entraineur, List<Joueur> listJoueur) {
        this.entraineur = entraineur;
        this.listJoueur = listJoueur;
    }

    public int getNbMatchGagne() {
        return nbMatchGagne;
    }

    public int getNbMatchPerdu() {
        return nbMatchPerdu;
    }

    public void addMathGagne() {
        nbMatchGagne++;
    }

    public void addMatchPerdu() {
        nbMatchPerdu++;
    }
}
