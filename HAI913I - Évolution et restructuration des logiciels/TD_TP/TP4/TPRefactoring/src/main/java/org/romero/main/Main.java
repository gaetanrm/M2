package org.romero.main;

import org.romero.equipe.Entraineur;
import org.romero.equipe.Equipe;
import org.romero.equipe.Joueur;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ROMERO Gaétan
 * numéro étudiant : 21701877
 **/
public class Main {

    public static void main(String[] args) {

        Entraineur entraineur = new Entraineur("Pierre", "Marchal");
        List<Joueur> listJoueur = new ArrayList<>();
        Joueur joueur1 = new Joueur("Carlos", "Sanchez", "Attaquant");
        Joueur joueur2 = new Joueur("Gwendal", "Trinquart", "Remplacant");
        listJoueur.add(joueur1);
        listJoueur.add(joueur2);
        Equipe equipe = new Equipe(entraineur, listJoueur);
        Joueur.choisirStrategie();
    }
}
