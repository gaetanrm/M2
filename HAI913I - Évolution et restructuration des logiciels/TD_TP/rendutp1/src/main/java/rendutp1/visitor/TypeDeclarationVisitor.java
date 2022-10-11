package rendutp1.visitor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import rendutp1.utils.Pair;

public class TypeDeclarationVisitor extends ASTVisitor {
	
	List<TypeDeclaration> types = new ArrayList<TypeDeclaration>();
	List<Pair> listMethodsWithLines = new ArrayList<Pair>();
	int numberOfLines = 0;
	int indiceClass = 0;
	int indiceMethod = 0;
	
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
		
		for(int i=indiceClass; i < types.size(); i++) {
			List<Pair> topMethods = new ArrayList<Pair>();
			MethodDeclaration[] methods = types.get(i).getMethods();
			indList = 0;
			indListMax = methods.length*(1/10);
			
			// If methods.length < 10, then indListMax = 0, 
			// so we put it at 1 by default to be able to see something
			if (indListMax == 0) {
				indListMax = 1;
			}
				
			lineNumberPerMethod(parse, methods);
			
			while(indList < indListMax) {
				topMethods.add(bestChoiceMethodLines(topMethods));
				indList++;
			}
			
			System.out.println("Pour la classe : " + types.get(i).getName());
			
			for(Pair method : topMethods) {
				System.out.println("Method : " + method.getMethod().getName());
				System.out.println("Nombre de ligne(s) de celle-ci : " + method.getNumberOfLines() + "\n");
			}
			
			indiceMethod += methods.length;
		}

		indiceClass = types.size();
		
	}
	
	// Pick the method with the greater number of lines 
	public Pair bestChoiceMethodLines(List<Pair> listMethods) {
		Pair best = null;
		
		for(int i=indiceMethod; i < listMethodsWithLines.size(); i++) {
			Pair methodAndLines = listMethodsWithLines.get(i);
			if (!listMethods.contains(methodAndLines)) {
				if (best == null) {
					best = methodAndLines;
				}else  {
					if (best.getNumberOfLines() < methodAndLines.getNumberOfLines()) {
						best = methodAndLines;
					}
				}
			}
		}
		return best;
	}
	
	// Add to a list a pair of a method and the lines number of it
	public void lineNumberPerMethod(CompilationUnit parse, MethodDeclaration[] methods) {
		int startLineNumber, endLineNumber;

		for(MethodDeclaration method : methods) {
			startLineNumber = parse.getLineNumber(method.getStartPosition());
			endLineNumber = parse.getLineNumber(method.getStartPosition() + method.getLength());
			listMethodsWithLines.add(new Pair(method, (endLineNumber - startLineNumber + 1)));
		}
	}
}
