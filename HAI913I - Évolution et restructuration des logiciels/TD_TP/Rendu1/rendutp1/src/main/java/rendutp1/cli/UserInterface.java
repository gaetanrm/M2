package rendutp1.cli;

import java.util.ArrayList;
import java.util.Scanner;

public class UserInterface {

	private int typeVisitor;
	
	public static int[] indChoice = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14};
	public static ArrayList<String> descriptionChoice = new ArrayList<String>() {{
		add("Nombre de classe(s) dans l'application");
		add("Nombre de ligne(s) dans l'application");
		add("Nombre de méthode(s) dans l'application");
		add("Nombre de package(s) dans l'application");
		add("Nombre moyen de méthode(s) par classe");
		add("Nombre moyen de ligne(s) par méthode");
		add("Nombre moyen d'attribut(s) par classe");
		add("Les 10% de(s) classe(s) qui ont le plus grand nombre de méthode(s)");
		add("Les 10% de(s) classe(s) qui ont le plus grand nombre d'attribut(s)");
		add("La(Les) classe(s) qui est(sont) dans les 2 catégories précédentes");
		add("La(Les) classe(s) qui posséde(ent) plus de X méthode(s)");
		add("Les 10% de(s) méthode(s) qui ont le plus grand nombre de ligne(s)");
		add("Le nombre maximal de paramètre(s) entre toutes les méthodes de l'application ");
		add("Arreter l'application");
		
	}};
	
	public UserInterface() {
	}
	
	public void printCLI(int indice, Scanner scanClavier) {
		
		if(indice==0) {
			System.out.println("Bienvenue !\n Quel exercice voulez vous faire ?");
		} else {
			System.out.println("Choisissez l'exercice que vous souhaitez : ");
		}
		
		System.out.println("1 ou 2");
		typeVisitor = scanClavier.nextInt();
		
		if (typeVisitor == 1) {
			
			for(int i=0; i < 14; i++) {
				System.out.println(indChoice[i] + ": " + descriptionChoice.get(i));
			}
			typeVisitor = scanClavier.nextInt();
		} else {
			typeVisitor = 0;
		}
		
	}

	public int getTypeVisitor() {
		return typeVisitor;
	}

	public void setTypeVisitor(int typeVisitor) {
		this.typeVisitor = typeVisitor;
	}
}
