package tp2.spoon.parsers;

import java.io.File;
import java.util.ArrayList;

import spoon.Launcher;
import spoon.reflect.CtModel;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;
import spoon.reflect.visitor.filter.TypeFilter;
import tp2.utils.Pair;

public class SpoonParser {

	private static boolean spoonOrNot = false;
	
	// Return a list formed with pairs of classes
	// Each pair is like that : (calling class, called class)
	public static ArrayList<Pair<String, String>> getListPairFromFileWithSpoon (ArrayList<File> files) {
		
		ArrayList<CtModel> models = new ArrayList<CtModel>();

		for (File file : files) {
			
			Launcher launcher = new Launcher();
			
			// addInputResource() add a resource, so a file or a folder, in the launcher
			// It allows us to add as many resources as we want in the launcher
			// In order to parse them
			launcher.addInputResource(file.getPath());
			models.add(launcher.buildModel());
		}		
		return takeModelAndreturnList(models);
		
	}
	
	// Take a List of CtModel created with a spoon launcher
	// And return a list of pairs of classes
	protected static ArrayList<Pair<String, String>> takeModelAndreturnList(ArrayList<CtModel> models) {
		
		ArrayList<Pair<String, String>> listPairs = new ArrayList<Pair<String, String>>();
		
		// We get each class in each model
		// Then we search each time a method is called into each method of the class
		// And we create a pair
		for (CtModel model : models) {
			
			for (CtType<?> type : model.getAllTypes()) {
				
				for (Object method : type.getAllMethods()) {
					
					CtMethod<?> ctmethod = null;
					
					if (method instanceof CtMethod) {
						
						ctmethod = (CtMethod<?>) method;
					}
					if (ctmethod != null) {
						
						String methodName = ctmethod.getSimpleName();
						
						// We filter the children in order to only keep the invocation ones
						ctmethod.filterChildren(new TypeFilter<CtInvocation<?>>(CtInvocation.class)).forEach(inv -> {
							CtInvocation<?> newInvocation = (CtInvocation<?>) inv;
							
							// If the method called is not in the same class that the one calling
							if (newInvocation.getExecutable().getSimpleName() != type.getSimpleName()) {
								if (newInvocation.getExecutable().getDeclaringType() != null) {
									
									listPairs.add(new Pair<String, String>(type.getSimpleName() + "." + methodName, 
											newInvocation.getExecutable().getDeclaringType().getSimpleName() + 
											"." + newInvocation.getExecutable().getSimpleName()));
								}
							}
						});
					}
				}
			}
		}
		return listPairs;
	}
}
