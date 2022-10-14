package rendutp1.visitor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.PackageDeclaration;

public class PackageDeclarationVisitor extends ASTVisitor {
	List<PackageDeclaration> packages = new ArrayList<PackageDeclaration>();

	public boolean visit(PackageDeclaration node) {
		sortedPackagesList(node);
		return super.visit(node);
	}

	public List<PackageDeclaration> getPackages() {
		return packages;
	}
	
	public int sizeList() {
		return packages.size();
	}
	
	public void printPackageDeclaration() {
		System.out.println("[");
		for(PackageDeclaration pack : packages) {
			System.out.println("Package: " + pack.getName() + ",");
		}
		System.out.println("]");
	}

	public void sortedPackagesList(PackageDeclaration node) {
		boolean nodeInOrNot = false;
		for(PackageDeclaration pack : packages) {
			if (pack.getName().toString().equals(node.getName().toString())) {
				nodeInOrNot = true;
			}
		}
		if (!nodeInOrNot)
			packages.add(node);
	}	
}
	
