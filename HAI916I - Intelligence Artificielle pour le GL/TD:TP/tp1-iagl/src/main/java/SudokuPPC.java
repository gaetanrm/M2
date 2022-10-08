import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;

import static org.chocosolver.solver.search.strategy.Search.minDomLBSearch;
import static org.chocosolver.util.tools.ArrayUtils.append;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SudokuPPC {

	static int n;
	static int s;
	private static int instance;
	private static long timeout = 3600000; // one hour
	static final int SUDOKUSIZE9 = 9;
	static final int SUDOKUSIZE16 = 16;
	public static List<String> letterList = new ArrayList<String>() {{
		add("A");
		add("B");
		add("C");
		add("D");
		add("E");
		add("F");
		add("G");
	}};

	IntVar[][] rows, cols, shapes;

	Model model;

	public static void main(String[] args) throws ParseException, IOException {

		final Options options = configParameters();
		final CommandLineParser parser = new DefaultParser();
		final CommandLine line = parser.parse(options, args);

		boolean helpMode = line.hasOption("help"); // args.length == 0
		if (helpMode) {
			final HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("sudoku", options, true);
			System.exit(0);
		}
		instance = 4;
		// Check arguments and options
		for (Option opt : line.getOptions()) {
			checkOption(line, opt.getLongOpt());
		}

		n = instance;
		s = (int) Math.sqrt(n);

		System.out.println("====================================================================================");
		System.out.println("\n");
		System.out.println("Génération d'une solution");
		new SudokuPPC().solve();
		System.out.println("\n");
		System.out.println("====================================================================================");
		System.out.println("\n");
		System.out.println("Génération de toutes les solutions");
		new SudokuPPC().solveAll();
		System.out.println("\n");
		System.out.println("====================================================================================");
		System.out.println("\n");
		System.out.println("Génération d'une solution pour l'instance de la matrice 9x9");
		new SudokuPPC().solveFromFile();
		System.out.println("\n");
		System.out.println("====================================================================================");
	}

	public void solve() {

		buildModel();

		model.getSolver().solve();

		printGrid();

		model.getSolver().printStatistics();

	}
	
	public void solveAll() {

		buildModel();

		while(model.getSolver().solve()) {
			printGrid();
		}
		model.getSolver().printStatistics();

	}
	
	public void solveFromFile() throws IOException {
		
		//Sudoku 9x9
		n = SUDOKUSIZE9;
		s = (int) Math.sqrt(n);
		String filePath = "/Users/romero/eclipse-workspace/tp1-iagl/src/main/resources/SudokuPPC9x9.txt";
		
		buildModel();
		addConstraintToModel(loadInstanceFromFile(filePath));
		
		while(model.getSolver().solve()) {
			printGrid();
		}
		model.getSolver().printStatistics();
		
		//Sudoku 16x16
		n = SUDOKUSIZE16;
		s = (int) Math.sqrt(n);
		filePath = "/Users/romero/eclipse-workspace/tp1-iagl/src/main/resources/SudokuPPC16x16.txt";
		
		buildModel();
		addConstraintToModel(loadInstanceFromFile(filePath));
		
		while(model.getSolver().solve()) {
			printGrid();
		}
		model.getSolver().printStatistics();
		
		//Greater Than Sudoku
		n = SUDOKUSIZE9;
		s = (int) Math.sqrt(n);
		filePath = "/Users/romero/eclipse-workspace/tp1-iagl/src/main/resources/GreaterThanSudoku.txt";
				
		buildModel();
		addConstraintToModel(loadInstanceFromFile(filePath));
				
		while(model.getSolver().solve()) {
			printGrid();
		}
		model.getSolver().printStatistics();
	}

	public void printGrid() {

		String a;
		a = "┌───";
		String b;
		b = "├───";
		String c;
		c = "─┬────┐";
		String d;
		d = "─┼────┤";
		String e;
		e = "─┬───";
		String f;
		f = "─┼───";
		String g;
		g = "└────┴─";
		String h;
		h = "───┘";
		String k;
		k = "───┴─";
		String esp = " ";
		
		

		for (int i = 0; i < n; i++) {

			for (int line = 0; line < n; line++) {
				if (line == 0) {
					System.out.print(i == 0 ? a : b);
				} else if (line == n - 1) {
					System.out.print(i == 0 ? c : d);
				} else {
					System.out.print(i == 0 ? e : f);
				}
			}
			System.out.println("");
			System.out.print("│ ");
			for (int j = 0; j < n; j++) {
				if (rows[i][j].getValue() > 9)
					System.out.print(esp + letterList.get(rows[i][j].getValue()-10) + " │ ");
				else
					System.out.print(esp + rows[i][j].getValue() + " │ ");

			}

			if (i == n - 1) {
				System.out.println("");
				for (int line = 0; line < n; line++) {
					System.out.print(line == 0 ? g : (line == n - 1 ? h : k));
				}
			}
			System.out.println("");

		}
	}

	public void buildModel() {
		model = new Model();

		rows = new IntVar[n][n];
		cols = new IntVar[n][n];
		shapes = new IntVar[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				rows[i][j] = model.intVar("c_" + i + "_" + j, 1, n, false);
				cols[j][i] = rows[i][j];
			}
		}

		for (int i = 0; i < s; i++) {
			for (int j = 0; j < s; j++) {
				for (int k = 0; k < s; k++) {
					for (int l = 0; l < s; l++) {
						shapes[j + k * s][i + (l * s)] = rows[l + k * s][i + j * s];
					}
				}
			}
		}

		for (int i = 0; i < n; i++) {
			model.allDifferent(rows[i]).post();
			model.allDifferent(cols[i]).post();
			model.allDifferent(shapes[i]).post();

		}

	}

	// Check all parameters values
	public static void checkOption(CommandLine line, String option) {

		switch (option) {
		case "inst":
			instance = Integer.parseInt(line.getOptionValue(option));
			break;
		case "timeout":
			timeout = Long.parseLong(line.getOptionValue(option));
			break;
		default: {
			System.err.println("Bad parameter: " + option);
			System.exit(2);
		}

		}

	}

	// Add options here
	private static Options configParameters() {

		final Option helpFileOption = Option.builder("h").longOpt("help").desc("Display help message").build();

		final Option instOption = Option.builder("i").longOpt("instance").hasArg(true).argName("sudoku instance")
				.desc("sudoku instance size").required(false).build();

		final Option limitOption = Option.builder("t").longOpt("timeout").hasArg(true).argName("timeout in ms")
				.desc("Set the timeout limit to the specified time").required(false).build();

		// Create the options list
		final Options options = new Options();
		options.addOption(instOption);
		options.addOption(limitOption);
		options.addOption(helpFileOption);

		return options;
	}

	public void configureSearch() {
		model.getSolver().setSearch(minDomLBSearch(append(rows)));

	}
	
	public List<SudokuElement> loadInstanceFromFile(String filePath) throws IOException {
		BufferedReader fileReader = Files.newBufferedReader(Paths.get(filePath));
		String nextLine = null;
		List<SudokuElement> listElement = new ArrayList<SudokuElement>();
		String[] splitFileLine;
		boolean isLetter;

		while ((nextLine = fileReader.readLine()) != null) {
			splitFileLine = nextLine.split(";");
			isLetter = false;
			for(int i=0; i < letterList.size(); i++) {
				if (letterList.get(i).equals(splitFileLine[0])) {
					isLetter = true;
					listElement.add(new SudokuElement(i + 10, Integer.parseInt(splitFileLine[1]), Integer.parseInt(splitFileLine[2])));
				}
			}
			if (!isLetter) {
				switch(splitFileLine[0]) {
				
					case "<":
						splitFileLine[0] = "-1";
						break;
						
					case ">":
						splitFileLine[0] = "-2";
						break;
						
					case "∧":
						splitFileLine[0] = "-3";
						break;
						
					case "v":
						splitFileLine[0] = "-4";
						break;
						
				}
//				System.out.println("element : " + Integer.parseInt(splitFileLine[0]) + ", " + Integer.parseInt(splitFileLine[1]) + ", " + Integer.parseInt(splitFileLine[2]));
				listElement.add(new SudokuElement(Integer.parseInt(splitFileLine[0]), Integer.parseInt(splitFileLine[1]), Integer.parseInt(splitFileLine[2])));
			}
		}
		
		return listElement;
	}
	
	
	public void addConstraintToModel(List<SudokuElement> listElement) {
		for(SudokuElement element : listElement) {
			try {
				switch (element.getElement()) {
				
					case -1:
						model.arithm(rows[element.getPositionX()][element.getPositionY()], "<", rows[element.getPositionX()][element.getPositionY()+1]).post();
						break;
						
					case -2:
						model.arithm(rows[element.getPositionX()][element.getPositionY()], ">", rows[element.getPositionX()][element.getPositionY()+1]).post();
						break;
						
					case -3:
						model.arithm(rows[element.getPositionX()][element.getPositionY()], "<", rows[element.getPositionX()+1][element.getPositionY()]).post();
						break;
						
					case -4:
						model.arithm(rows[element.getPositionX()][element.getPositionY()], ">", rows[element.getPositionX()+1][element.getPositionY()]).post();
						break;
						
					default:
						model.arithm(rows[element.getPositionX()][element.getPositionY()], "=", element.getElement()).post();
						break;
						
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
