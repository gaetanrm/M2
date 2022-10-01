package rendutp1.parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import rendutp1.cli.UserInterface;
import rendutp1.visitor.MethodDeclarationVisitor;
import rendutp1.visitor.PackageDeclarationVisitor;
import rendutp1.visitor.TypeDeclarationVisitor;

public class Parser {
	
	
	public static final String projectPath = "/Users/romero/eclipse-workspace/rendutp1";
	public static final String projectSourcePath = projectPath + "/src";
	public static final String jrePath = "/Library/Java/JavaVirtualMachines/jdk-17.0.4.1.jdk";
	
	public static UserInterface userCLI = new UserInterface();
	
	public static CompilationUnit parse;

	public static void main(String[] args) throws IOException, InterruptedException {
		
		Scanner scan = new Scanner(System.in);
		// read java files
		final File folder = new File(projectSourcePath);
		ArrayList<File> javaFiles = listJavaFilesForFolder(folder);
		
		int indice = 0;
		while(userCLI.getTypeVisitor() != 14) {
			userCLI.printCLI(indice, scan);
			indice++;
			System.out.println("\n");
			System.out.println("======================================================");
			userChoice(userCLI.getTypeVisitor(), javaFiles);
			System.out.println("======================================================");
			System.out.println("\n");
			System.out.println("Voulez vous continuer ? Y or N");
			String charReaded = scan.next();
			if (charReaded.equals("N")) {
				System.exit(1);
			}
		}
	}

	// read all java files from specific folder
	public static ArrayList<File> listJavaFilesForFolder(final File folder) {
		ArrayList<File> javaFiles = new ArrayList<File>();
		for (File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				javaFiles.addAll(listJavaFilesForFolder(fileEntry));
			} else if (fileEntry.getName().contains(".java")) {
				// System.out.println(fileEntry.getName());
				javaFiles.add(fileEntry);
			}
		}

		return javaFiles;
	}

	// create AST
	private static CompilationUnit parse(char[] classSource) {
		ASTParser parser = ASTParser.newParser(AST.JLS4); // java +1.6
		parser.setResolveBindings(true);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
 
		parser.setBindingsRecovery(true);
 
		Map options = JavaCore.getOptions();
		parser.setCompilerOptions(options);
 
		parser.setUnitName("");
 
		String[] sources = { projectSourcePath }; 
		String[] classpath = {jrePath};
 
		parser.setEnvironment(classpath, sources, new String[] { "UTF-8"}, true);
		parser.setSource(classSource);
		
		return (CompilationUnit) parser.createAST(null); // create and parse
	}
	
	//choice made by the user
	public static void userChoice(int choice, ArrayList<File> javaFiles) throws IOException {
		
		TypeDeclarationVisitor visitorClass = new TypeDeclarationVisitor();
		MethodDeclarationVisitor visitorMethod = new MethodDeclarationVisitor();
		PackageDeclarationVisitor visitorPackage = new PackageDeclarationVisitor();
		
		switch(choice) {
		
		case 1:
			parseFilesClass(visitorClass, javaFiles);
			System.out.println("Nombre de classe(s) : " + visitorClass.sizeList());
			visitorClass.printTypeDeclaration();
			break;
			
		case 2:
			int nbLines = 0;
			for (File fileEntry : javaFiles) {
				String content = FileUtils.readFileToString(fileEntry);
				parse = parse(content.toCharArray());
				nbLines += countLineNumber();
			}
			System.out.println("Nombre de ligne(s) : " + nbLines);
			break;
			
		case 3:
			parseFilesMethod(visitorMethod, javaFiles);
			System.out.println("Nombre de methode(s) : " + visitorMethod.sizeList());
			visitorMethod.printMethodDeclaration();
			break;
			
		case 4: 
			parseFilesPackage(visitorPackage, javaFiles);
			System.out.println("Nombre de package(s) : " + visitorPackage.sizeList());
			visitorPackage.printPackageDeclaration();
			break;
			
		case 5: 
			parseFilesClass(visitorClass, javaFiles);
			System.out.println("Nombre moyen de methode(s) par classe : " + visitorClass.averageNumberOfMethods());
			break;
		
		case 6:
			parseFilesMethod(visitorMethod, javaFiles);
			System.out.println("Nombre moyen de ligne(s) par methode : " + visitorMethod.averageNumberOfLinesPerMethods(parse));
			break;
			
		case 7:
			parseFilesClass(visitorClass, javaFiles);
			System.out.println("Nombre moyen de variable(s) par classe : " + visitorClass.averageNumberOfVariables());
			break;
			
		case 8:
			parseFilesClass(visitorClass, javaFiles);
			List<TypeDeclaration> topClassByNumberOfMethods = visitorClass.printTypeDeclaration10PourcentMethod();
			System.out.println("Remarque : Si vous n'avez pas suffisamment de classe dans votre projet pour faire 10%");
			System.out.println("L'application vous affichera seulement la classe qui contient le plus de methodes \n");
			System.out.println("Voici les 10% des classes qui possèdent le plus de méthodes : \n");
			
			for(TypeDeclaration type : topClassByNumberOfMethods) {
				System.out.println("Class : " + type.getName());
				System.out.println("Nombre de methode(s) de celle-ci : " + type.getMethods().length + "\n");
			}
			break;
			
		case 9:
			parseFilesClass(visitorClass, javaFiles);
			List<TypeDeclaration> topClassByNumberOfVariables = visitorClass.printTypeDeclaration10PourcentVariable();
			System.out.println("Remarque : Si vous n'avez pas suffisamment de classe dans votre projet pour faire 10%");
			System.out.println("L'application vous affichera seulement la classe qui contient le plus de variables \n");
			System.out.println("Voici les 10% des classes qui possèdent le plus de variables : \n");
			
			for(TypeDeclaration type : topClassByNumberOfVariables) {
				System.out.println("Class : " + type.getName());
				System.out.println("Nombre de variable(s) de celle-ci : " + type.getFields().length + "\n");
			}
			break;
			
		case 10:
			parseFilesClass(visitorClass, javaFiles);
			List<TypeDeclaration> topClassByNumberMethods = visitorClass.printTypeDeclaration10PourcentMethod();
			List<TypeDeclaration> topClassByNumberVariables = visitorClass.printTypeDeclaration10PourcentVariable();
			System.out.println("Voici les classes qui font parties des 10% possédant le plus de variables et de methodes : \n");
			
			for(TypeDeclaration typeMethods : topClassByNumberMethods) {
				for(TypeDeclaration typeVariables : topClassByNumberVariables) {
					if (typeMethods.getName().toString().equals(typeVariables.getName().toString())) {
						System.out.println("Class : " + typeMethods.getName());
						System.out.println("Nombre de variable(s) de celle-ci : " + typeMethods.getFields().length);
						System.out.println("Nombre de methode(s) de celle-ci : " + typeMethods.getMethods().length + "\n");
					}
				}
			}
			break;
			
		case 11:
			parseFilesClass(visitorClass, javaFiles);
			Scanner scanInt = new Scanner(System.in);
			System.out.println("Veuillez donner un nombre minimal de methodes par classe : ");
			visitorClass.printTypeDeclarationWithXMethods(scanInt.nextInt());
			break;
			
		case 12:
			parseFilesClass(visitorClass, javaFiles);
			visitorClass.print10PourcentMethodPerClass(parse);
			break;
			
		case 13:
			parseFilesMethod(visitorMethod, javaFiles);
			visitorMethod.printMethodWithMaxParam();
			break;
			
		case 14:
			System.exit(1);
		}
	}
	
	public static void parseFilesClass(TypeDeclarationVisitor visitorClass, ArrayList<File> javaFiles) throws IOException {
		for (File fileEntry : javaFiles) {
			String content = FileUtils.readFileToString(fileEntry);
			parse = parse(content.toCharArray());
			classInfo(visitorClass);
		}
	}
		
	public static void parseFilesMethod(MethodDeclarationVisitor visitorMethod, ArrayList<File> javaFiles) throws IOException {
		for (File fileEntry : javaFiles) {
			String content = FileUtils.readFileToString(fileEntry);
			parse = parse(content.toCharArray());
			methodInfo(visitorMethod);
		}
	}
		
	public static void parseFilesPackage(PackageDeclarationVisitor visitorPackage, ArrayList<File> javaFiles) throws IOException {
		for (File fileEntry : javaFiles) {
			String content = FileUtils.readFileToString(fileEntry);
			parse = parse(content.toCharArray());
			packageInfo(visitorPackage);
		}
	}
		
	// methods to accept visitor
	public static void classInfo(TypeDeclarationVisitor visitorClass) {
		parse.accept(visitorClass);
	}
		
	public static void methodInfo(MethodDeclarationVisitor visitorMethod) {
		parse.accept(visitorMethod);
	}
		
	public static void packageInfo(PackageDeclarationVisitor visitorPackage) {
		parse.accept(visitorPackage);
	}
		
	// get number of lines in the application
	public static int countLineNumber() {
		return parse.getLineNumber(parse.getLength() - 1);
	}
	
}
