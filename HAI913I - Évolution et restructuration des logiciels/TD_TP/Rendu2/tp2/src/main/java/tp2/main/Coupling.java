package tp2.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.DefaultAttribute;
import org.jgrapht.nio.dot.DOTExporter;

import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Link;
import guru.nidi.graphviz.model.LinkSource;
import guru.nidi.graphviz.model.LinkTarget;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;
import guru.nidi.graphviz.parse.Parser;
import tp2.spoon.parsers.SpoonParser;
import tp2.utils.Pair;

public class Coupling {

	// We transform the DOT File obtained within the first TP in a MutableGraph to
	// manipulate it
	// And we use it to create a list of pairs of Link
	protected static ArrayList<Pair<String, String>> createPairWithDotFile(String filePath) throws IOException {

		ArrayList<Pair<LinkSource, LinkTarget>> listPair = new ArrayList<Pair<LinkSource, LinkTarget>>();

		// We open the .dot file to retrieve the graph we created in the tp1
		// Moreover, we parse it into a MutableGraph in order to manipulate it
		File dotFile = new File(filePath);
		FileInputStream inputDotFile = new FileInputStream(dotFile);
		MutableGraph graphFromFile = new Parser().read(inputDotFile);

		for (Link edge : graphFromFile.edges()) {
			listPair.add(new Pair<LinkSource, LinkTarget>(edge.from(), edge.to()));
		}

		return simplifyListOfPairs(listPair, graphFromFile);
	}

	// We simplify the list of pairs to have the label instead of a Link
	// Thanks to that, we just can manipulate strings instead of links
	protected static ArrayList<Pair<String, String>> simplifyListOfPairs(ArrayList<Pair<LinkSource, LinkTarget>> listPair,
			MutableGraph graphFromFile) {

		ArrayList<Pair<String, String>> listPairString = new ArrayList<Pair<String, String>>();

		for (Pair<LinkSource, LinkTarget> pair : listPair) {

			Pair<String, String> newPair = new Pair<String, String>();

			for (MutableNode node : graphFromFile.nodes()) {

				// To store the label of the node, we have to split the string where "=" is
				// Because node.attrs() give a string like : "{label=myLabel}".
				// Then we cut it (substring()) to remove the "}" to the end.
				String labelNode[] = node.attrs().toString().split("=");
				String label = labelNode[1].substring(0, labelNode[1].length() - 1);

				if (pair.getLeft().name() == node.name()) {
					newPair.setLeft(label);
				}

				else if (pair.getRight().name() == node.name()) {
					newPair.setRight(label);
				}
			}

			listPairString.add(newPair);
		}

		return listPairString;
	}

	// Calculates the coupling between class A and B
	// In few words, it calculates the number of calls between methods of class A
	// and class B
	// And return the difference between that and the total number of calls
	public static double coupling(String classA, String classB, ArrayList<Pair<String, String>> listPair) throws IOException {

		if (listPair == null) {
			
			listPair = createPairWithDotFile("src/main/resources/callGraph.dot");
		}
		
		int numberOfCalls = 0;

		for (Pair<String, String> pair : listPair) {

			// If classA and classB are contained in a pair
			// Then it's a call between classA and classB
			if (pair.getLeft().contains(classA)) {

				if (pair.getRight().contains(classB)) {

					numberOfCalls++;
				}
			} else if (pair.getLeft().contains(classB)) {

				if (pair.getRight().contains(classA)) {

					numberOfCalls++;
				}
			}
		}

		return (double) numberOfCalls / listPair.size();
	}

	// To create a coupling graph of all classes in an application
	// We use JGrapht library to do it
	// It allows to create a simple Graph directed or not and weighted or not
	public static void createCouplingGraph(ArrayList<Pair<String, String>> listPair) throws IOException {

		if (listPair == null) {
			
			listPair = createPairWithDotFile("src/main/resources/callGraph.dot");
		}
		ArrayList<String> listClass = new ArrayList<>();
		Graph<String, DefaultWeightedEdge> graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);

		for (Pair<String, String> pair : listPair) {

			if (pair.getLeft().contains(".")) {

				// To cut the string where "." is in order to store the class's name of the
				// method
				// in the current pair
				StringTokenizer stringLeftToken = new StringTokenizer(pair.getLeft(), ".");
				String tokenLeft = stringLeftToken.nextToken();

				if (!listClass.contains(tokenLeft)) {

					listClass.add(tokenLeft);
				}
			}

			if (pair.getRight().contains(".")) {

				StringTokenizer stringRightToken = new StringTokenizer(pair.getRight(), ".");
				String tokenRight = stringRightToken.nextToken();

				if (!listClass.contains(tokenRight)) {

					listClass.add(tokenRight);
				}
			}
		}

		for (int i = 0; i < listClass.size() - 2; i++) {

			// Add the first class as a node in the graph
			graph.addVertex(listClass.get(i));

			for (int j = i + 1; j < listClass.size() - 1; j++) {

				double coupling = coupling(listClass.get(i), listClass.get(j), listPair);

				if (coupling > 0) {
					// Add the second class as a node if it's paired with the first class somewhere
					// And so we add the corresponding weighted edge
					graph.addVertex(listClass.get(j));
					graph.setEdgeWeight(graph.addEdge(listClass.get(i), listClass.get(j)), coupling);
				}
			}
		}

		
		// It allows us to limit a double to 5 decimal places.
		DecimalFormat sizeCoupling = new DecimalFormat();
		sizeCoupling.setMaximumFractionDigits(5);

		// We use the DOTExporter to export the graph in DOT Format and PNG Format
		DOTExporter<String, DefaultWeightedEdge> exporter = new DOTExporter<String, DefaultWeightedEdge>();
		//Put vertices in a form which is known by the DOT Format -> "label"
		exporter.setVertexAttributeProvider((v) -> {
			Map<String, Attribute> map = new LinkedHashMap<String, Attribute>();
			map.put("label", DefaultAttribute.createAttribute(v.toString()));
			return map;
		});
		// Put edges in a form which is known by the DOT Format -> "weight"
		// And in order to print it in the graph we create a "label" with his value
		exporter.setEdgeAttributeProvider((v) -> {
			Map<String, Attribute> map = new LinkedHashMap<String, Attribute>();
			map.put("weight", DefaultAttribute.createAttribute(graph.getEdgeWeight(v)));
			map.put("label", DefaultAttribute.createAttribute(sizeCoupling.format(graph.getEdgeWeight(v))));
			return map;
		});
		Writer writer = new StringWriter();
		exporter.exportGraph(graph, writer);
		exporter.exportGraph(graph, new File("target/couplingGraph.dot")); // To DOT File
		MutableGraph g = new guru.nidi.graphviz.parse.Parser().read(writer.toString());
		Graphviz.fromGraph(g).height(1000).render(Format.PNG).toFile(new File("target/couplingGraph.png"));
	}
}
