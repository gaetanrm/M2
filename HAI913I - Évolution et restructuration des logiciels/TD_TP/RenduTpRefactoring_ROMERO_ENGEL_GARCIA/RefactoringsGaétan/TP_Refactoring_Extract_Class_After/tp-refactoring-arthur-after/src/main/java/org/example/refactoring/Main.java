package org.example.refactoring;

public class Main {
    public static void main(String[] args) {
        Personnage arthur = new Personnage("Arthur", 21, "Clavier mécanique", 5);
        Personnage gaetan = new Personnage("Gaétan", 20, "Pelle à tarte", 5);

        while(!(arthur.isMort() || gaetan.isMort())) {
            gaetan.attaquer(arthur);
            arthur.attaquer(gaetan);
            System.out.print("\n");
        }
    }
}