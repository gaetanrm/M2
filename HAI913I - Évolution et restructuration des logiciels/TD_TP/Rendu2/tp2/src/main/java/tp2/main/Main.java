package tp2.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import tp2.cli.UserInterface;
import tp2.spoon.parsers.SpoonParser;
import tp2.utils.Pair;

public class Main {

	public static final String projectPath = "/Users/romero/eclipse-workspace/rendutp1";
	public static final String projectSourcePath = projectPath + "/src";


	public static void main(String[] args) throws IOException, InterruptedException {

		int indice = 0;
		Scanner scan = new Scanner(System.in);

		UserInterface.printCLI(indice);
		int userChoice = scan.nextInt();
		
		while (true) {
			indice++;
			
			System.out.println("\n");
			System.out.println("======================================================");
			userChoice(userChoice, scan);
			System.out.println("======================================================");
			System.out.println("\n");
			
			Thread.sleep(2000);
			
			System.out.println("Voulez vous continuer ? Y or N");
			String charReaded = "";
			
			while ((charReaded = scan.nextLine()) == "") {
			}
			
			if (charReaded.equals("N")) {
				
				System.exit(1);
				break;
			}
			UserInterface.printCLI(indice);
			userChoice = scan.nextInt();
			
			while (!testUserChoice(userChoice)) {
				
				System.out.println("Numéro invalide ! Veuillez rentrer un autre numéro :");
				userChoice = scan.nextInt();
			}
		}
	}

	// read all java files from specific folder
	protected static ArrayList<File> listJavaFilesForFolder(final File folder) {
		
		ArrayList<File> javaFiles = new ArrayList<File>();
		
		for (File fileEntry : folder.listFiles()) {
			
			if (fileEntry.isDirectory()) {
				
				javaFiles.addAll(listJavaFilesForFolder(fileEntry));
			} 
			else if (fileEntry.getName().contains(".java")) {
				
				// System.out.println(fileEntry.getName());
				javaFiles.add(fileEntry);
			}
		}

		return javaFiles;
	}
	
	// test if the choice made by the user is what is expected or not
	protected static boolean testUserChoice(int userChoice) {
		
		if (userChoice <= 5) {
			
			if (userChoice >= 0) {
				
				return true;
			}
		}
		return false;
	}
	
	protected static void userChoice (int userChoice, Scanner scan) throws IOException, InterruptedException {
		
		switch(userChoice) {
		
		case 1 :
			
			System.out.println("Vous avez choisi le couplage entre deux classes !");
			System.out.println("\n");
			
			String firstClass = null;
			String secondClass = null;
			
			System.out.println("Veuillez donner une classe à tester : ");
			
			while ((firstClass = scan.nextLine()) == "") {
			}
			
			System.out.println("\n");
			System.out.println("Veuillez donner une autre classe à tester : ");
			
			while ((secondClass = scan.nextLine()) == "") {
			}

			double coupling = Coupling.coupling(firstClass, secondClass, null);
			System.out.println("\n");
			System.out.println("Le couplage obtenu entre la classe " + firstClass + " et la classe " + secondClass
					 + " est de : " + coupling);
			
			break;
			
		case 2 :
			
			System.out.println("Vous avez choisi le graphe de couplage !");
			System.out.println("\n");
			Coupling.createCouplingGraph(null);
			System.out.println("\n");
			System.out.println("Le graphe se trouve à l'emplacement : /target/couplingGraph.png");
			System.out.println("\n");
			
			break;
			
		case 3 :
			
			System.out.println("Vous avez choisi le clustering !");
			System.out.println("\n");
			Clustering.clustering(scan);
			
			break;
			
		case 4 :
			
			System.out.println("Vous avez choisi Spoon !");
			System.out.println("\n");
			
			// store the java files folder in the project
			final File folder = new File(projectSourcePath);
			ArrayList<Pair<String, String>> listPair = SpoonParser.getListPairFromFileWithSpoon(listJavaFilesForFolder(folder));
			
			// Coupling
			String firstClassSpoon = null;
			String secondClassSpoon = null;
			
			System.out.println("Veuillez donner une classe à tester : ");
			
			while ((firstClassSpoon = scan.nextLine()) == "") {
			}
			
			System.out.println("\n");
			System.out.println("Veuillez donner une autre classe à tester : ");
			
			while ((secondClassSpoon = scan.nextLine()) == "") {
			}

			double couplingSpoon = Coupling.coupling(firstClassSpoon, secondClassSpoon, null);
			System.out.println("\n");
			System.out.println("Le couplage obtenu entre la classe " + firstClassSpoon + " et la classe " + secondClassSpoon
					 + " est de : " + couplingSpoon);
			
			System.out.println("----------------------------------------");
			
			// CouplingGraph			
			System.out.println("\n");
			Coupling.createCouplingGraph(listPair);
			System.out.println("\n");
			System.out.println("Le graphe se trouve à l'emplacement : /target/couplingGraph.png");
			System.out.println("\n");
			
			System.out.println("----------------------------------------");
			
			// Clustering
			System.out.println("\n");
			Clustering.clustering(scan);
			
			break;
			
		case 5 : 
			
			System.out.println("Au revoir !");
			System.exit(1);
			
			break;
			
		}
	}
}
