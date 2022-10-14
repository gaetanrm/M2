package rendutp1.utils;

import org.eclipse.jdt.core.dom.MethodDeclaration;

public class Pair {
	
	MethodDeclaration method;
	int numberOfLines;
	
	public Pair(MethodDeclaration method, int numberOfLines) {
		this.method = method;
		this.numberOfLines = numberOfLines;
	}

	public MethodDeclaration getMethod() {
		return method;
	}

	public int getNumberOfLines() {
		return numberOfLines;
	}
	
	

}
