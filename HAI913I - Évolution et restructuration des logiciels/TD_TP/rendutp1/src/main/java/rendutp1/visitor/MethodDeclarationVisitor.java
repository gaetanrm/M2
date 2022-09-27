package rendutp1.visitor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

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
		return getMethods().size();
	}
	
	public void printMethodDeclaration() {
		System.out.println("[");
		for(MethodDeclaration method : methods) {
			System.out.println("Method: " + method.getName() + ",");
		}
		System.out.println("]");
	}
}
