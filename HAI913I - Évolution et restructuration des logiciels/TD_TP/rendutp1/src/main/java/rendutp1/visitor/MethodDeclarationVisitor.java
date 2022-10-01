package rendutp1.visitor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;

public class MethodDeclarationVisitor extends ASTVisitor {
	List<MethodDeclaration> methods = new ArrayList<MethodDeclaration>();
	
	public boolean visit(MethodDeclaration node) {
		methods.add(node);
		return super.visit(node);
	}
	
	public List<MethodDeclaration> getMethods() {
		return methods;
	}
	
	public int sizeList() {
		return methods.size();
	}
	
	public void printMethodDeclaration() {
		System.out.println("[");
		for(MethodDeclaration method : methods) {
			System.out.println("Method: " + method.getName() + ",");
		}
		System.out.println("]");
	}
	
	public float averageNumberOfLinesPerMethods(CompilationUnit parse) {
		int numberOfLines = 0;
		for(MethodDeclaration method : methods) {
			numberOfLines += parse.getLineNumber(method.getLength() -1);
		}
		System.out.println("Problème : Certaines methodes ne fonctionnent pas avec getLineNumber() \n");
		return (float)numberOfLines/methods.size();
	}
	
	public void printMethodWithMaxParam() {
		int maxNumber = 0;
		MethodDeclaration bestMethod = null;
		
		for (MethodDeclaration method : methods) {
			if (method.parameters().size() > maxNumber) {
				maxNumber = method.parameters().size();
				bestMethod = method;
			}
		}
		System.out.println("Le nombre maximal de paramètre(s) pour une methode dans toute l'application : " + maxNumber);
		System.out.println("La methode ayant ce nombre de paramètre(s) est : " + bestMethod.getName());
	}
}
