import java.io.File;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.*;
import org.eclipse.emf.common.util.*;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMLMapImpl;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Behavior;
import org.eclipse.uml2.uml.BehavioralFeature;
import org.eclipse.uml2.uml.CallEvent;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Event;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PackageableElement;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.uml2.uml.Trigger;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLFactory;
//import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.Vertex;
import org.eclipse.uml2.uml.VisibilityKind;

public class MethodGenerator {
	
	public static void main(String[] args) {
			
		
		//Instruction récupérant le modèle sous forme d'arbre à partir de la classe racine "Model"
		Model umlP = chargerModele("model/model.uml");
		
		Class describedClass = (Class) umlP.getPackagedElement("AssociatedClass");
		
		List<StateMachine> listStateMachine = (getClassAndReturnStateMachine(describedClass));
		
		for (StateMachine stateMachine : listStateMachine) {
			System.out.println(stateMachine.getName());
			
			System.out.println(isConform(stateMachine));
			
			System.out.println("States : ");
			for (State state : getStatesOfStateMachine(stateMachine)) {
				System.out.println("     " + state.getName());
			}
			
			System.out.println("Operations : ");
			for (Operation operation : getOperationsLikeTriggersInStateMachine(stateMachine)) {
				System.out.println("     " + operation.getName());
			}
			
			applyStatePatternToAClass(describedClass);
			
			sauverModele("model/newStatePattern.uml", umlP);
			
		}
		
		applyStatePatternToAFile("model/model.uml");
		
		sauverModele("model/newAllStatePattern.uml", umlP);
		
	}

	public static void sauverModele(String uri, EObject root) {
	   Resource resource = null;
	   try {
	      URI uriUri = URI.createURI(uri);
	      Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
	      resource = (new ResourceSetImpl()).createResource(uriUri);
	      resource.getContents().add(root);
	      resource.save(null);
	   } catch (Exception e) {
	      System.err.println("ERREUR sauvegarde du modèle : "+e);
	      e.printStackTrace();
	   }
	}
	
	public static Model chargerModele(String uri) {
	   Resource resource = null;
	   EPackage pack=UMLPackage.eINSTANCE;
	   try {
	      URI uriUri = URI.createURI(uri);
	      Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("uml", new XMIResourceFactoryImpl());
	      resource = (new ResourceSetImpl()).createResource(uriUri);
	      XMLResource.XMLMap xmlMap = new XMLMapImpl();
	      xmlMap.setNoNamespacePackage(pack);
	      java.util.Map options = new java.util.HashMap();
	      options.put(XMLResource.OPTION_XML_MAP, xmlMap);
	      resource.load(options);
	   }
	   catch(Exception e) {
	      System.err.println("ERREUR chargement du modèle : "+e);
	      e.printStackTrace();
	   }
	   return (Model) resource.getContents().get(0);
	}
	
	
	public static List<StateMachine>  getClassAndReturnStateMachine(Class describedClass) {
		List<StateMachine> listOfStateMachines = new ArrayList<StateMachine>();
		EList<Behavior> describedClassBehavior = describedClass.getOwnedBehaviors();
		
		for (Behavior behavior : describedClassBehavior) {
			if (behavior instanceof StateMachine) {
				listOfStateMachines.add((StateMachine)behavior);
			}
		}
		
		return listOfStateMachines;
	}
	
	public static boolean isConform(StateMachine stateMachine) {
		
		if (stateMachine.getRegions().size() != 1) {
			return false;
		}
		else {
			return true;
		}
	}
	
	public static List<State> getStatesOfStateMachine(StateMachine stateMachine) {
		
		Region stateRegion = stateMachine.getRegions().get(0);
		EList<Vertex> stateVertex = stateRegion.getSubvertices();
		List<State> listStates = new ArrayList<State>();
		
		for (Vertex vertex : stateVertex) {
			if (vertex instanceof State) {
				listStates.add((State) vertex);
			}
		}
		return listStates;
	}
	
	public static List<Operation> getOperationsLikeTriggersInStateMachine(StateMachine stateMachine) {
		
		Region stateRegion = stateMachine.getRegions().get(0);
		EList<Transition> stateFeatures = stateRegion.getTransitions();
		List<Operation> listOperations = new ArrayList<Operation>();
		
		for (Transition transition : stateFeatures) {
			EList<Trigger> listTriggers = transition.getTriggers();
			
			for (Trigger trigger : listTriggers) {
				Event event = trigger.getEvent();
				
				if (event instanceof CallEvent) {
					Operation operation = ((CallEvent)event).getOperation();
					listOperations.add(operation);
				}
			}
		}
		return listOperations;
	}
	
	public static void applyStatePatternToAClass(Class sourceClass) {
		
		UMLFactory UmlFactory = UMLFactory.eINSTANCE;
		
		Class newAbstractClass = UmlFactory.createClass();
		newAbstractClass.setName("EtatA");
		newAbstractClass.setIsAbstract(true);
		newAbstractClass.setPackage(sourceClass.getPackage());
		
		Property propertyAbstractClass = UmlFactory.createProperty();
		propertyAbstractClass.setType(sourceClass);
		propertyAbstractClass.setUpper(-1);
		propertyAbstractClass.setLower(0);
		
		Property propertySourceClass = UmlFactory.createProperty();
		propertySourceClass.setType(newAbstractClass);
		propertySourceClass.setUpper(1);
		propertySourceClass.setLower(1);
		
		newAbstractClass.getOwnedAttributes().add(propertyAbstractClass);
		sourceClass.getOwnedAttributes().add(propertySourceClass);
		
		Association association = UmlFactory.createAssociation();
		association.setName("applyStatePattern");
		association.getMemberEnds().add(propertyAbstractClass);
		association.getMemberEnds().add(propertySourceClass);
		association.setPackage(newAbstractClass.getPackage());
		
		StateMachine stateMachine = getClassAndReturnStateMachine(sourceClass).get(0);
		for (Operation operation : getOperationsLikeTriggersInStateMachine(stateMachine)) {
			
			Operation newOperation = UmlFactory.createOperation();
			newOperation.setName(operation.getName());
			newOperation.setIsAbstract(true);
			newAbstractClass.getOwnedOperations().add(newOperation);
		}
		
		for (State state : getStatesOfStateMachine(stateMachine)) {
			
			Class newSubClass = UmlFactory.createClass();
			newSubClass.setName(state.getName());
			newSubClass.getSuperClasses().add(newAbstractClass);
			newSubClass.setPackage(newAbstractClass.getPackage());
			
			for (Operation operation : newAbstractClass.getOwnedOperations()) {
				Operation newOperation = UmlFactory.createOperation();
				newOperation.setName(operation.getName());
				newOperation.setIsAbstract(false);
				newSubClass.getOwnedOperations().add(newOperation);
			}
			
		}
		
	}
	
	public static void applyStatePatternToAFile(String fileName) {
		
		Model umlModel = chargerModele(fileName);
		int numberOfPackageableElement = umlModel.getPackagedElements().size();
		
		for (int i = 0; i < numberOfPackageableElement; i++) {
			PackageableElement umlClass = umlModel.getPackagedElements().get(i);
			if (umlClass instanceof Class) {
				applyStatePatternToAClass((Class)umlClass);
			}
		}
	}
}
