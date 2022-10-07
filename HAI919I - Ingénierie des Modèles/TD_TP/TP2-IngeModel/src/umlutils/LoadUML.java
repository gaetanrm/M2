package umlutils;


import java.lang.annotation.Target;

import org.eclipse.emf.*;
import org.eclipse.emf.common.util.*;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMLMapImpl;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLFactory;
//import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.VisibilityKind;

public class LoadUML {

	public static void main(String[] args) {

			
		
		//Instruction récupérant le modèle sous forme d'arbre à partir de la classe racine "Model"
		Model umlP = chargerModele("model/model.uml");
		
		
		String nomModele=  umlP.getName();
		
		System.out.println("Ton modèle se nomme: \""+nomModele+"\"");
		
		Package packageSource = (Package) umlP.getPackagedElement("p2");
		Package packageTarget = (Package) umlP.getPackagedElement("p1");
		Class classToMove = (Class) packageSource.getPackagedElement("A");
		Class subClass = (Class) packageSource.getPackagedElement("C");
		Class superClass = (Class) packageSource.getPackagedElement("B");
		Operation methodToMove = subClass.getOwnedOperations().get(0);
		Property attToMove = classToMove.getAttributes().get(0);
		
		moveClassToAnotherPackage(classToMove, packageTarget);
		
		//Modifier le nom du modèle UML
		umlP.setName("NewModelName");
		System.out.println("\""+nomModele+"\" Changé ! Le modèle se nomme: \""+umlP.getName()+"\"");
		
		
		//Sauvegarder le modèle avec son nouveau nom
		sauverModele("model/changerNom.uml", umlP);
		
		replacePublicAttByPrivateAttWithAccessors(classToMove, attToMove);
		
		
		//Modifier le nom du modèle UML
		umlP.setName("NewModelName2");
		System.out.println("\""+nomModele+"\" Changé ! Le modèle se nomme: \""+umlP.getName()+"\"");
				
				
		//Sauvegarder le modèle avec son nouveau nom
		sauverModele("model/changerVisibilty.uml", umlP);
		
		moveMethodFromClassToSuperClass(subClass, superClass, methodToMove.getName());
		
		//Modifier le nom du modèle UML
		umlP.setName("NewModelName3");
		System.out.println("\""+nomModele+"\" Changé ! Le modèle se nomme: \""+umlP.getName()+"\"");
						
						
		//Sauvegarder le modèle avec son nouveau nom
		sauverModele("model/changerHierarchy.uml", umlP);
		
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
		
		public static void moveClassToAnotherPackage(Class UMLClass, Package targetPackage) {
			if ((UMLClass != null) & (targetPackage != null)) {
				targetPackage.getPackagedElements().add(UMLClass);
			}
		}
		
		public static void replacePublicAttByPrivateAttWithAccessors(Class classOfAttToMove, Property attToMove) {
			if ((attToMove.getVisibility() == VisibilityKind.get(VisibilityKind.PUBLIC)) && (attToMove.isAttribute())) {
				attToMove.setVisibility(VisibilityKind.get(VisibilityKind.PRIVATE));
				UMLFactory umlFactory = UMLFactory.eINSTANCE;
				
				//Create getter
				Operation getter = umlFactory.createOperation();
				getter.setName("get" + attToMove.getName());
				
				//Create setter
				Operation setter = umlFactory.createOperation();
				setter.setName("set" + attToMove.getName());
				
				//Set Parameter
				Parameter setterParameter = umlFactory.createParameter();
				setterParameter.setName(attToMove.getName());
				setterParameter.setType(attToMove.getType());
				
				//Add parameter to setter operation
				setter.getOwnedParameters().add(setterParameter);
				
				//Add operations to the class
				classOfAttToMove.getOwnedOperations().add(getter);
				classOfAttToMove.getOwnedOperations().add(setter);
				
			}else {
				System.out.println("Ce n'est pas un attribut ou il n'est pas public");
			}
		}
		
		public static void moveMethodFromClassToSuperClass(Class subClass, Class superClass, String methodName) {
			Operation methodToMove;
			Operation abstractMethodToRemove;
			if (subClass.getSuperClasses().contains(superClass)) {
				if ((methodToMove = methodInSubClass(methodName, subClass)) != null) {
					if (((abstractMethodToRemove = methodAlreadyInSuperClass(methodName, superClass)) != null) && (abstractMethodToRemove.isAbstract())) {
						superClass.getOwnedOperations().add(methodToMove);
						superClass.getOwnedOperations().remove(abstractMethodToRemove);
					}
					else {
						System.out.println("La méthode existe déjà dans la super classe");
					}
				}
				else {
					System.out.println("La méthode n'est pas dans la classe");
				}
			}
			else {
				System.out.println("Ce n'est pas une super classe !");
			}
		}
		
		public static Operation methodInSubClass(String methodName, Class subClass) {
			for (Operation op : subClass.getOperations()) {
				if (op.getName().toString().equals(methodName)) {
					return op;
				}
			}
			return null;
		}
		
		public static Operation methodAlreadyInSuperClass(String methodName, Class superClass) {
			for (Operation op : superClass.getOperations()) {
				if ((op.getName().toString().equals(methodName)) && (!op.isAbstract())) {
					return op;
				}
				if ((op.getName().toString().equals(methodName)) && (op.isAbstract())) {
					return op;
				}
			}
			return null;
		}

}
