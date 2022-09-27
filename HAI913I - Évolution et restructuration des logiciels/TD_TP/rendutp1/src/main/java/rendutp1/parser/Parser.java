package rendutp1.parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.internal.utils.FileUtil;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.internal.compiler.flow.FinallyFlowContext;

import rendutp1.visitor.MethodDeclarationVisitor;
import rendutp1.visitor.MethodInvocationVisitor;
import rendutp1.visitor.TypeDeclarationVisitor;
import rendutp1.visitor.VariableDeclarationFragmentVisitor;

public class Parser {
	
	
	public static final String projectPath = "/Users/romero/eclipse-workspace/rendutp1";
	public static final String projectSourcePath = projectPath + "/src";
	public static final String jrePath = "/Library/Java/JavaVirtualMachines/jdk-17.0.4.1.jdk";

	public static void main(String[] args) throws IOException {
		
		TypeDeclarationVisitor visitorClass = new TypeDeclarationVisitor();
		MethodDeclarationVisitor visitorMethod = new MethodDeclarationVisitor();
		// read java files
		final File folder = new File(projectSourcePath);
		ArrayList<File> javaFiles = listJavaFilesForFolder(folder);

		//
		for (File fileEntry : javaFiles) {
			String content = FileUtils.readFileToString(fileEntry);
			// System.out.println(content);

			final CompilationUnit parse = parse(content.toCharArray());
			
			// count number of classes and print them
			classInfo(parse, visitorClass);
			methodInfo(parse, visitorMethod);
			/*
			// print methods info
			printMethodInfo(parse);

			// print variables info
			printVariableInfo(parse);
			
			//print method invocations
			printMethodInvocationInfo(parse);
			*/
		}
		System.out.println("Nombre de classe(s) : " + visitorClass.sizeList());
		visitorClass.printTypeDeclaration();
		System.out.println("Nombre de methode(s) : " + visitorMethod.sizeList());
		visitorMethod.printMethodDeclaration();
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

	/*
	// navigate method information
	public static void printMethodInfo(CompilationUnit parse) {
		MethodDeclarationVisitor visitor = new MethodDeclarationVisitor();
		parse.accept(visitor);

		for (MethodDeclaration method : visitor.getMethods()) {
			System.out.println("Method name: " + method.getName()
					+ " Return type: " + method.getReturnType2());
		}

	}

	// navigate variables inside method
	public static void printVariableInfo(CompilationUnit parse) {

		MethodDeclarationVisitor visitor1 = new MethodDeclarationVisitor();
		parse.accept(visitor1);
		for (MethodDeclaration method : visitor1.getMethods()) {

			VariableDeclarationFragmentVisitor visitor2 = new VariableDeclarationFragmentVisitor();
			method.accept(visitor2);

			for (VariableDeclarationFragment variableDeclarationFragment : visitor2
					.getVariables()) {
				System.out.println("variable name: "
						+ variableDeclarationFragment.getName()
						+ " variable Initializer: "
						+ variableDeclarationFragment.getInitializer());
			}

		}
	}
	
	// navigate method invocations inside method
		public static void printMethodInvocationInfo(CompilationUnit parse) {

			MethodDeclarationVisitor visitor1 = new MethodDeclarationVisitor();
			parse.accept(visitor1);
			for (MethodDeclaration method : visitor1.getMethods()) {

				MethodInvocationVisitor visitor2 = new MethodInvocationVisitor();
				method.accept(visitor2);

				for (MethodInvocation methodInvocation : visitor2.getMethods()) {
					System.out.println("method " + method.getName() + " invoc method "
							+ methodInvocation.getName());
				}

			}
		}
		*/
		
		
		// methods to accept visitor
		public static void classInfo(CompilationUnit parse, TypeDeclarationVisitor visitorClass) {
			parse.accept(visitorClass);
		}
		
		public static void methodInfo(CompilationUnit parse, MethodDeclarationVisitor visitorMethod) {
			parse.accept(visitorMethod);
		}
		
		// get number of lines in the application
		public static int countLineNumber(CompilationUnit parse) {
			return parse.getLineNumber(parse.getLength() - 1);
		}
}
