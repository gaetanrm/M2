package rendutp1.visitor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class TypeDeclarationVisitor extends ASTVisitor {
	List<TypeDeclaration> types = new ArrayList<TypeDeclaration>();
	
	public boolean visit(TypeDeclaration node) {
		types.add(node);
		return super.visit(node);
	}
	
	public List<TypeDeclaration> getTypes() {
		return types;
	}
	
	public int sizeList() {
		return types.size();
	}

	public void printTypeDeclaration() {
		System.out.println("[");
		for(TypeDeclaration type : types) {
			System.out.println("Class: " + type.getName() + ",");
		}
		System.out.println("]");
	}
	
	public float averageNumberOfMethods() {
		int numberOfMethods = 0;
		for (TypeDeclaration type : types) {
			numberOfMethods += type.getMethods().length;
		}
		return (float)numberOfMethods/types.size();
	}
	
}
