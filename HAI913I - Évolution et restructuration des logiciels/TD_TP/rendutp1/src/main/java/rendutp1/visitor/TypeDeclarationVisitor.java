package rendutp1.visitor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
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
	
	public float averageNumberOfVariables() {
		int numberOfVariables = 0;
		for(TypeDeclaration type : types) {
			numberOfVariables += type.getFields().length;
		}
		return (float)numberOfVariables/types.size();
	}
	
	// Print the 10% of Classes with the best number of Methods
	public List<TypeDeclaration> printTypeDeclaration10PourcentMethod() {
		List<TypeDeclaration> topClassByNumberOfMethods = new ArrayList<TypeDeclaration>();
		int indList = 0;
		int indListMax = types.size()*(1/10);
		
		// If types.size() < 10, then indListMax = 0, 
		// so we put it at 1 by default to be able to see something
		if (indListMax == 0) {
			indListMax = 1;
		}
		
		while(indList < indListMax) {
			topClassByNumberOfMethods.add(bestChoiceMethod(topClassByNumberOfMethods));
			indList++;
		}
		
		return topClassByNumberOfMethods;
	}
	
	// Chooses the class with the max number of Methods and returns it
	public TypeDeclaration bestChoiceMethod(List<TypeDeclaration> list) {
		TypeDeclaration best = null;
		
		for(TypeDeclaration type : types) {
			if (!list.contains(type)) {
				if (best == null) {
					best = type;
				}else {
					if (type.getMethods().length > best.getMethods().length) {
						best = type;
					}
				}
			}
		}
		return best;
	}
	
	// Print the 10% of Classes with the best number of Variables
	public List<TypeDeclaration> printTypeDeclaration10PourcentVariable() {
		List<TypeDeclaration> topClassByNumberOfVariables = new ArrayList<TypeDeclaration>();
		int indList = 0;
		int indListMax = types.size()*(1/10);
		
		// If types.size() < 10, then indListMax = 0, 
		// so we put it at 1 by default to be able to see something
		if (indListMax == 0) {
			indListMax = 1;
		}
			
		while(indList < indListMax) {
			topClassByNumberOfVariables.add(bestChoiceVariable(topClassByNumberOfVariables));
			indList++;
		}
		
		return topClassByNumberOfVariables;
	}
	
	// Chooses the class with the max number of Methods and returns it
	public TypeDeclaration bestChoiceVariable(List<TypeDeclaration> list) {
		TypeDeclaration best = null;
		
		for(TypeDeclaration type : types) {
			if (!list.contains(type)) {
				if (best == null) {
					best = type;
				}else {
					if (type.getFields().length > best.getFields().length) {
						best = type;
					}
				}
			}
		}
		return best;
	}
	
	// Prints classes with at least X methods
	public void printTypeDeclarationWithXMethods(int minimumNumberOfMethods) {
		System.out.println("Les classes qui possèdent au moins " + minimumNumberOfMethods + " methode(s) : \n");
		
		for(TypeDeclaration type : types) {
			if (type.getMethods().length >= minimumNumberOfMethods) {
				System.out.println("Class : " + type.getName());
			}
		}
	}
	
	// Prints the 10% of Methods with the best number of lines per Class
	public void print10PourcentMethodPerClass(CompilationUnit parse) {
		int indList, indListMax;
		System.out.println("Les 10% des méthodes qui ont le plus grand nombre de lignes, par classe");
		System.out.println("Problème : Certaines methodes ne fonctionnent pas avec getLineNumber()" + "\n");
		
		for(TypeDeclaration type : types) {
			List<MethodDeclaration> topMethods = new ArrayList<MethodDeclaration>();
			indList = 0;
			indListMax = type.getMethods().length*(1/10);
			
			// If type.getMethods().length < 10, then indListMax = 0, 
			// so we put it at 1 by default to be able to see something
			if (indListMax == 0) {
				indListMax = 1;
			}
				
			while(indList < indListMax) {
				topMethods.add(bestChoiceMethodLines(type.getMethods(), topMethods, parse));
				indList++;
			}
			
			System.out.println("Pour la classe : " + type.getName());
			for(MethodDeclaration method : topMethods) {
				System.out.println("Method : " + method.getName());
				System.out.println("Nombre de ligne(s) de celle-ci : " + parse.getLineNumber(method.getLength() -1) + "\n");
			}
		}
	}
	
	public MethodDeclaration bestChoiceMethodLines(MethodDeclaration[] methods, List<MethodDeclaration> listMethods, CompilationUnit parse) {
		MethodDeclaration best = null;
		
		for(MethodDeclaration method : methods) {
			if (!listMethods.contains(method)) {
				if (best == null) {
					best = method;
				}else  {
					if (parse.getLineNumber(method.getLength() -1) > parse.getLineNumber(parse.getExtendedLength(best))) {
						best = method;
					}
				}
			}
		}
		return best;
	}
}
