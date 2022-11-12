package tp2.cli;

import java.util.ArrayList;
import java.util.Scanner;

public class UserInterface {
	
	public static int[] indChoice = {1, 2, 3, 4, 5};
	public static ArrayList<String> descriptionChoice = new ArrayList<String>() {{
		add("Couplage de deux classes");
		add("Création du graphe de couplage pondéré");
		add("Clustering des classes et identification des classes");
		add("Spoon");
		add("Arreter l'application");
		
	}};

	public static void printCLI(int indice) {
		
		if(indice==0) {
			
			System.out.println("Bienvenue !\n Quel exercice voulez vous faire ?");
		} 
		else {
			
			System.out.println("Choisissez l'exercice que vous souhaitez : ");
		}
		for(int i=0; i < 5; i++) {
			
			System.out.println(indChoice[i] + ": " + descriptionChoice.get(i));
		}		
	}
}
