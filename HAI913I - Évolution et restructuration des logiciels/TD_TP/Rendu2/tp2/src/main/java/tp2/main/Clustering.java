package tp2.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;
import guru.nidi.graphviz.parse.Parser;
import tp2.utils.Pair;

public class Clustering {
	
	// The main method of the class
	// Use it for the 2nd exercise
	// It makes the right calls and prints what is expected 
	public static void clustering(Scanner userEntry) throws IOException {

		ArrayList<String> listCluster = createListClusterWithDotFile("target/couplingGraph.dot");
		ArrayList<Pair<String, String>> listPair = new ArrayList<Pair<String, String>>();
		double minimumForClustering;

		System.out.println("Démarrage du clustering : ");
		System.out.println("\n");
		
		while (clusteringChoice(listCluster, listPair)) {

			
			System.out.println("Nouveau cluster trouvé !");
			System.out.println("\n");
			System.out.println("==========================");
			System.out.println("\n");
			System.out.println("Clustering : ");
			System.out.println("\n");
			
			for (Pair<String, String> pair : listPair) {
				
				System.out.println(pair.getLeft() + " avec " + pair.getRight());
			}
			System.out.println("\n");
			System.out.println("==========================");
			System.out.println("\n");
			System.out.println("Liste des clusters restants : ");
			
			for (String cluster : listCluster) {
				
				System.out.println(cluster);
			}
			
			System.out.println("\n");
			System.out.println("==========================");
			System.out.println("\n");
		}
		
		System.out.println("Clustering terminé");
		System.out.println("==========================");
		
		System.out.println("\n");
		System.out.println("==========================");
		System.out.println("Démarrage du clustering avec un couplage minimum : ");
		System.out.println("\n");
		
		// We ask the user to enter a double
		// And we use it for the clusteringWithMinimumCoupling() method
		System.out.println("Veuillez rentrer un nombre(de type double) : ");
		
		minimumForClustering = userEntry.nextDouble();
		clusteringWithMinimumCoupling(minimumForClustering, listPair.size(), listPair);
		
		System.out.println("\n");
		System.out.println("Liste des clusters restants après avec un coupling minimum de : " + minimumForClustering);
		System.out.println("\n");
		
		for (Pair<String, String> pair : listPair) {
			
			System.out.println(pair.getLeft() + " avec " + pair.getRight());
		}
		System.out.println("\n");
	}

	// We transform the DOT File obtained within the first TP in a MutableGraph to
	// manipulate it
	// And we use it to create a list of MutableNode
	protected static ArrayList<String> createListClusterWithDotFile(String filePath) throws IOException {

		ArrayList<String> listCluster = new ArrayList<String>();

		// We open the .dot file to retrieve the graph we created in the tp1
		// Moreover, we parse it into a MutableGraph in order to manipulate it
		File dotFile = new File(filePath);
		FileInputStream inputDotFile = new FileInputStream(dotFile);
		MutableGraph graphFromFile = new Parser().read(inputDotFile);

		for (MutableNode node : graphFromFile.nodes()) {

			// To store the label of the node, we have to split the string where "=" is
			// Because node.attrs() give a string like : "{label=myLabel}".
			// Then we cut it (substring()) to remove the "}" to the end.
			String labelNode[] = node.attrs().toString().split("=");
			String label = labelNode[1].substring(0, labelNode[1].length() - 1);

			listCluster.add(label);
		}

		return listCluster;
	}

	// This method chooses a new cluster among the list
	// Each member in the list is a cluster symbolized with a string
	// A cluster is a class or a concatenation of class
	protected static boolean clusteringChoice(ArrayList<String> listCluster, ArrayList<Pair<String, String>> listPair) throws IOException {

		double coupling = 0.0, testClusterCoupling = 0.0;
		String cluster1 = "", cluster2 = "";
		boolean firstIsConcat = false, secondIsConcat = false;

		// If the size of listCluster == 1 then 
		// we have to return false
		// else we return true
		// Because 
		if (listCluster.size() > 1) {

			for (int i = 0; i < listCluster.size() - 1; i++) {

				// To store all classes of the string
				String[] firstCluster = null;
				// To store the array's size
				int indFirst = 0;

				if (listCluster.get(i).contains(".")) {

					// To split the string and store the parts in an array
					Pair<Integer, String[]> result = splitString(".", listCluster.get(i));
					firstCluster = result.getRight();
					indFirst = result.getLeft();

					// Because we saw that this string is concatenated
					firstIsConcat = true;
				}

				for (int j = i + 1; j < listCluster.size() - 1; j++) {

					// To store all classes of the string
					String[] secondCluster = null;
					// To store the array's size
					int indSecond = 0;

					// If it's a concatenation
					// We split the string in order to obtain all of the classes in it
					if (listCluster.get(j).contains(".")) {

						Pair<Integer, String[]> result = splitString(".", listCluster.get(j));
						secondCluster = result.getRight();
						indSecond = result.getLeft();
						
						// Because we saw that this string is concatenated
						secondIsConcat = true;
					}

					// If the first string is a concatenation (so a cluster with more than one class)
					if (firstIsConcat) {

						// If the second string is a concatenation (so a cluster with more than one class)
						if (secondIsConcat) {

							// We check all possible pairs between the first and second cluster
							// And in each iteration, we add the coupling between them
							// With the previous ones
							// In the end, we check if the coupling is greater
							// Than the previous one
							for (int t = 0; t <= indFirst - 1; t++) {

								for (int k = 0; k < indSecond - 1; k++) {

									testClusterCoupling += Coupling.coupling(firstCluster[t], secondCluster[k], null)
											+ Coupling.coupling(firstCluster[t], secondCluster[k + 1], null);
								}
							}
							// If the coupling obtained previously is greater than the actual one
							// We replace it with the new one
							if (testClusterCoupling > coupling) {

								coupling = testClusterCoupling;
								cluster1 = listCluster.get(i);
								cluster2 = listCluster.get(j);
							}
						} 
						else {

							for (int t = 0; t <= indFirst - 1; t++) {

								testClusterCoupling += Coupling.coupling(firstCluster[t], listCluster.get(j), null);
							}
							if (testClusterCoupling > coupling) {

								coupling = testClusterCoupling;
								cluster1 = listCluster.get(i);
								cluster2 = listCluster.get(j);
							}
						}
					} 
					else {

						if (secondIsConcat) {

							for (int k = 0; k <= indSecond - 1; k++) {

								testClusterCoupling += Coupling.coupling(listCluster.get(i), secondCluster[k], null);
							}
							if (testClusterCoupling > coupling) {

								coupling = testClusterCoupling;
								cluster1 = listCluster.get(i);
								cluster2 = listCluster.get(j);
							}
						} 
						else {

							if (Coupling.coupling(listCluster.get(i), listCluster.get(j), null) > coupling) {

								coupling = Coupling.coupling(listCluster.get(i), listCluster.get(j), null);
								cluster1 = listCluster.get(i);
								cluster2 = listCluster.get(j);
							}
						}
					}
				}
			}
		}
		
		// We test if the two strings cluster1 and cluster2 are empty or not because
		// There is a case where just one cluster exists in the list so one string is empty
		// Or no clusters matched so they are both empty
		// If we don't test this condition, the method never ends
		if (cluster1 != "" && cluster2 != "") {
			
			// We remove the two clusters of the list
			// And add in the new one formed by the concatenation of the two
			// We also store the new cluster in a list of pairs which correspond to clusters
			listCluster.remove(cluster1);
			listCluster.remove(cluster2);
			listCluster.add(cluster1 + "." + cluster2);
			listPair.add(new Pair<String, String>(cluster1, cluster2));
			
			return true;
		}
		else {
			
			return false;	
		}
	}
	
	// Purpose : we need to group classes in a module
	// Each class is contained in only one module
	// Each module have to contains classes of just one cluster
	// The average coupling of the module musn't be lower than the parameter (minimumCoupling)
	protected static void clusteringWithMinimumCoupling(double minimumCoupling, int sizeList, ArrayList<Pair<String, String>> listPair) throws IOException {

		// List of all the pairs we need to remove
		ArrayList<Pair<String, String>> listOfPairsToRemove = new ArrayList<Pair<String, String>>();
		int index = 0, sizeListPair;
		
		// We sort the list by removing the clusters with a coupling lesser 
		// than the minimum required
		for (Pair<String, String> pair : listPair) {
			
			double coupling = getPairCoupling(pair, sizeList, listPair);
			
			if (coupling < minimumCoupling) {
				
				listOfPairsToRemove.add(pair);
			}
		}
		
		// Remove the pairs contained in listOfPairsToRemove
		removePairsFromListOfPairs(listOfPairsToRemove, listPair);
		listOfPairsToRemove.clear();
		sizeListPair = listPair.size();
		
		for (int i = 0; i <= sizeListPair - 1; i++) {
			
			// If the cluster is to removed, 
			// then we add it into the list of pairs that we need to remove
			if (removeCluster(listPair.get(i), listPair)) {
				
				listOfPairsToRemove.add(listPair.get(i));
			}
		}
		
		// Remove the pairs contained in listOfPairsToRemove
		removePairsFromListOfPairs(listOfPairsToRemove, listPair);
		listOfPairsToRemove.clear();
		sizeListPair = listPair.size();
		
		// If the list is always too big
		while (listPair.size() > (sizeList / 2)) {
			
			listPair.remove(listPair.get(index));
		}
		
		// Remove the pairs contained in listOfPairsToRemove
		removePairsFromListOfPairs(listOfPairsToRemove, listPair);
	}
	
	// Get the coupling for a pair of clusters
	protected static double getPairCoupling(Pair<String, String> pair, int sizeList, ArrayList<Pair<String, String>> listPair) throws IOException {
		
		String leftSideOfPair = pair.getLeft(), rightSideOfPair = pair.getRight();
		String[] leftSplitted = new String[]{leftSideOfPair}, rightSplitted = new String[]{rightSideOfPair};
		// We need to know the number of not null elements in the array in order to iterate in it
		// That's the purpose of this two index
		int leftIndex = 1, rightIndex = 0;
		double coupling = 0.0;
		
		if (leftSideOfPair.contains(".")) {
			
			Pair<Integer, String[]> result = splitString(".", leftSideOfPair);
			leftSplitted = result.getRight();
			leftIndex = result.getLeft();
			System.out.println("index : " + leftIndex);
		}
		
		if (rightSideOfPair.contains(".")) {
			
			Pair<Integer, String[]> result = splitString(".", rightSideOfPair);
			rightSplitted = result.getRight();
			rightIndex = result.getLeft();
		}

		for (int i = 0; i <= leftIndex - 1; i ++) {
			
			for (int j = 0; j <= rightIndex - 1; j++) {

				coupling += Coupling.coupling(leftSplitted[i], rightSplitted[j], listPair);
			}
		}

		return coupling;
	}
	
	// Remove one cluster if it's contained in another
	protected static boolean removeCluster(Pair<String, String> clusterToRemove, ArrayList<Pair<String, String>> listPair) {
		
		// We store the two member of the cluster
		String firstTestedString = clusterToRemove.getLeft();
		String secondTestedString = clusterToRemove.getRight();
		
		for (Pair<String, String> pair : listPair) {
			
			if (pair != clusterToRemove) {
				
				String firstMatchedString = pair.getLeft();
				String secondMatchedString = pair.getRight();
				
				// If the cluster we are testing is contained in another,
				// We remove it
				if ((firstMatchedString.contains(firstTestedString))
						|| (firstMatchedString.contains(secondTestedString)) 
						|| (secondMatchedString.contains(firstTestedString)) 
						|| (secondMatchedString.contains(secondTestedString))) {
						
					return true;
				}
			}
		}
		return false;
	}
	
	// Split a string with a symbol
	protected static Pair<Integer, String[]> splitString(String symbol, String stringToSplit) {
		
		String resultArray[] = new String[stringToSplit.length()];
		int index = 0;
		
		StringTokenizer concatCluster = new StringTokenizer(stringToSplit, symbol);

		while (concatCluster.hasMoreTokens()) {

			String classCluster = concatCluster.nextToken();
			resultArray[index] = classCluster;
			index++;
		}
		
		return new Pair<Integer, String[]>(index, resultArray);
	}
	
	// Just to remove a list of pairs from a list
	protected static void removePairsFromListOfPairs(ArrayList<Pair<String, String>> listOfPairsToRemove, ArrayList<Pair<String, String>> listPair) {
		
		for (Pair<String, String> pair : listOfPairsToRemove) {
					
			listPair.remove(pair);
		}
	}
}
