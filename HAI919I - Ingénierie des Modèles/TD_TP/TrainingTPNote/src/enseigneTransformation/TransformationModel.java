package enseigneTransformation;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMLMapImpl;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.UMLFactory;

import enseigne.CaracteristiqueVariable;
import enseigne.ElementDePersonnalisation;
import enseigne.EnseignePackage;
import enseigne.GammePersonnalisable;
import enseigne.Objet;

public class TransformationModel {
	
	public static void main(String[] args) {
		
		Resource resource = chargerModele("model/GammePersonnalisable.xmi", EnseignePackage.eINSTANCE);
		
		UMLFactory transformationFactory = UMLFactory.eINSTANCE;
		Model model = transformationFactory.createModel();
		
		List<Class> listClass = new ArrayList<Class>();
		
		Class objetAbstrait = transformationFactory.createClass();
		model.getPackagedElements().add(objetAbstrait);
		objetAbstrait.setIsAbstract(true);
		objetAbstrait.setName("ObjetAbstrait");
		listClass.add(objetAbstrait);
		
		Class objetDecore = transformationFactory.createClass();
		objetDecore.setIsAbstract(true);
		objetAbstrait.getSuperClasses().add(objetAbstrait);
		objetAbstrait.setName("ObjetDecore");
		listClass.add(objetDecore);
		
		List<Objet> listObjet = getAllObjects(resource);
		for (Objet objet : listObjet) {
			Class objetClass = transformationFactory.createClass();
			objetClass.getSuperClasses().add(objetDecore);
			objetClass.setName(objet.getNom());
			listClass.add(objetClass);
		}
		
		Class decoration = transformationFactory.createClass();
		decoration.setIsAbstract(true);
		decoration.setName("Decoration");
		decoration.getSuperClasses().add(objetAbstrait);
		listClass.add(decoration);
		
		List<ElementDePersonnalisation> listElement = getAllPersonnalisableElements(resource);
		for (ElementDePersonnalisation element : listElement) {
			Class elementClass = transformationFactory.createClass();
			elementClass.getSuperClasses().add(decoration);
			elementClass.setName(element.getNom());
			listClass.add(elementClass);
		}
		
		Property propertyObjetDecore = transformationFactory.createProperty();
		propertyObjetDecore.setType(objetAbstrait);
		propertyObjetDecore.setUpper(1);
		propertyObjetDecore.setLower(1);
		
		Property propertyDecoration = transformationFactory.createProperty();
		propertyDecoration.setType(decoration);
		propertyObjetDecore.setIsComposite(true);
		
		decoration.getOwnedAttributes().add(propertyObjetDecore);
		objetAbstrait.getOwnedAttributes().add(propertyDecoration);
		
		Association association = transformationFactory.createAssociation();
		association.setName("decore");
		association.getMemberEnds().add(propertyDecoration);
		association.getMemberEnds().add(propertyObjetDecore);
		
		// On ajoute autant de méthodes que de caractéristiques variables
		// Pas de type de retour
		// Méthodes abstraites
		
		List<ElementDePersonnalisation> listCarac = getAllPersonnalisableElements(resource);
		for (ElementDePersonnalisation carac : listCarac) {
			Operation caracClass = transformationFactory.createOperation();
			caracClass.setName(carac.getNom());
			for (Class myClass : listClass) {
				if ((myClass.getName().toString().equals("ObjetDecore")) || (myClass.getName().toString().equals("ObjetAbstrait"))) {
					caracClass.setIsAbstract(true);
				}
				myClass.getOwnedOperations().add(caracClass);
			}
		}
		
		sauverModele("model/newModel", model);
	}
	
	public static void sauverModele(String uri, EObject root) {
		   Resource resource = null;
		   try {
		      URI uriUri = URI.createURI(uri);
		      Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("uml", new XMIResourceFactoryImpl());
		      resource = (new ResourceSetImpl()).createResource(uriUri);
		      resource.getContents().add(root);
		      resource.save(null);
		   } catch (Exception e) {
		      System.err.println("ERREUR sauvegarde du modèle : "+e);
		      e.printStackTrace();
		   }
		}

	public static Resource chargerModele(String uri, EPackage pack) {
		Resource resource = null;
		try {
			URI uriUri = URI.createURI(uri);
			Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
			resource = (new ResourceSetImpl()).createResource(uriUri);
			XMLResource.XMLMap xmlMap = new XMLMapImpl();
			xmlMap.setNoNamespacePackage(pack);
			java.util.Map options = new java.util.HashMap();
			options.put(XMLResource.OPTION_XML_MAP, xmlMap);
			resource.load(options);
		} catch (Exception e) {
			System.err.println("ERREUR chargement du modèle : " + e);
			e.printStackTrace();
		}
		return resource;
	}
	
	public static List<Objet> getAllObjects(Resource resource) {
		
		GammePersonnalisable gamme = (GammePersonnalisable) resource.getContents().get(0);
		return gamme.getObjet();
	}
	
	public static List<ElementDePersonnalisation> getAllPersonnalisableElements(Resource resource) {
		
		GammePersonnalisable gamme = (GammePersonnalisable) resource.getContents().get(0);
		return gamme.getElementdepersonnalisation();
	}
	
	public static List<CaracteristiqueVariable> getAllVariableCaracteristics(Resource resource) {
		
		GammePersonnalisable gamme = (GammePersonnalisable) resource.getContents().get(0);
		return gamme.getCaracteristiquevariable();
	}

}
