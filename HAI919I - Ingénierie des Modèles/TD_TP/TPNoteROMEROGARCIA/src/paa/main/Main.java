package paa.main;


import java.util.ArrayList;
import java.util.List;

import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.PackageableElement;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.VisibilityKind;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMLMapImpl;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Comment;
import org.eclipse.uml2.uml.Interface;

import paa.Entity;
import paa.Field;
import paa.FieldBasicType;
import paa.FieldDependantQuery;
import paa.PaAModel;
import paa.PaaPackage;
import paa.Query;
import paa.Repository;

public class Main {

	public static void main(String[] args) {
		
		//Question 3.1 :
		System.out.println("On construit un modèle UML appelé generatedModel : ");
		Model createdModel = generateModel();
		
		System.out.println("On le sauve dans le répertoire model");
		sauverModeleUML("model/generatedModel.uml", createdModel);
		
		//Je charge le modèle créé à la question 2
		PaAModel Loadmodel = chargerModelePaA("model/PaAModel.xmi");
		
		//Question 3.2:
		System.out.println("\nJ'appelle la méthode getFieldAndReturnProperty() :");
		
		List<Entity> listEntity = Loadmodel.getEntities();
		for(Entity entity : listEntity) {
			List<Field> listFields = entity.getFields();
			for(Field field : listFields) {
				Property property = getFieldAndReturnProperty(field, createdModel);
				System.out.println(property);
			}
		}
		
		
		//Question 3.3:
		System.out.println("\nJ'appelle la méthode getEntityAndReturnClass() :");
		for(Entity entity : listEntity) {
			Class classe = getEntityAndReturnClass(entity, createdModel);	
			System.out.println(classe);
		}
		
		//Question 3.4:
		System.out.println("\nJ'appelle la méthode getFieldDependantQueryAndReturnOperation() :");
		List<Repository> listRepository = Loadmodel.getRepositories();
		for(Repository repo : listRepository) {
			List<Query> listQueries = repo.getQueries();
			for(Query query : listQueries) {
				Operation operation = getFieldDependantQueryAndReturnOperation((FieldDependantQuery) query, createdModel);
				System.out.println(operation);
			}
		}	
		
		//Question 3.5:
		System.out.println("\nJ'appelle la méthode getDepotAndReturnInterface() :");
		for(Repository repo : listRepository) {
			Interface newInterface = getDepotAndReturnInterface(repo, createdModel);
			System.out.println(newInterface);
		}
		
		//Question 3.6:
		System.out.println("\nJ'appelle la méthode getPaAModelAndReturnModel() :");
		//Loadmodel c'est un PaAModel
		Model model = getPaAModelAndReturnModel(Loadmodel, createdModel);
		sauverModeleUML("model/finalModel.uml", model);

	}
	
	public static Model generateModel() {
		
		UMLFactory factory = UMLFactory.eINSTANCE;
		
		//Je crée un nouveau modèle
		Model model = factory.createModel();
		model.setName("generatedModel");
		
		PrimitiveType uml_int=factory.createPrimitiveType();
		PrimitiveType uml_string=factory.createPrimitiveType();
		PrimitiveType uml_float=factory.createPrimitiveType();
		
		uml_int.setName("int");
		uml_string.setName("string");
		uml_float.setName("float");
		
		model.getPackagedElements().add(uml_int);
		model.getPackagedElements().add(uml_string);
		model.getPackagedElements().add(uml_float);
	
		
		return model;
	
	}
	
	//Question 3.2
	public static Property getFieldAndReturnProperty(Field field, Model model) {

        UMLFactory factory = UMLFactory.eINSTANCE;

        Property newProperty = factory.createProperty();
        List<PackageableElement> listPrimitiveType = model.getPackagedElements();

        for (PackageableElement element : listPrimitiveType) {
            if (element instanceof PrimitiveType) {
                if ((field.getType() == FieldBasicType.STRING) && (element.getName()=="string")) {
                    newProperty.setType((Type) element);
                }
                else if ((field.getType() == FieldBasicType.INT) && (element.getName() == "int")) {
                    newProperty.setType((Type) element);
                }
                else {
                    newProperty.setType((Type) element);
                }
            }
        }

        newProperty.setName(field.getName());
        newProperty.setVisibility(VisibilityKind.get(VisibilityKind.PRIVATE));
        return newProperty;
    }

	
	//Question 3.3
	public static Class getEntityAndReturnClass(Entity entity, Model model) {
		
		UMLFactory factory = UMLFactory.eINSTANCE;
		
		//On crée une nouvelle classe
		Class newClass = factory.createClass();
		
		//Je lui donne le même nom que l'entité
		newClass.setName(entity.getName());
		
		//Je récupère tous les champs de l'entité
		List<Field> listFields = entity.getFields();
		
		//Pour chaque champ de l'entité j'appelle la méthode précédemment créée, je récupère une Property et je l'ajoute à ma nouvelle classe
		for(Field field : listFields) {
			Property property = getFieldAndReturnProperty(field, model);
			newClass.getOwnedAttributes().add(property);
		}
		
		//Je crée un commentaire avec ma factory, j'indique que son nom est @Entity
		Comment comment = factory.createComment();
		comment.setBody("@Entity");
		
		//J'ajoute le commentaire à ma classe, que je vais retourner
		newClass.getOwnedComments().add(comment);
		
		return newClass;
		
	}
	
	//Question 3.4
	public static Operation getFieldDependantQueryAndReturnOperation(FieldDependantQuery fieldQuery, Model model) {

        UMLFactory factory = UMLFactory.eINSTANCE;

        // Nouvelle Operation
        Operation newOperation = factory.createOperation();
        // On donne un nom à l'opération
        newOperation.setName(fieldQuery.getType().getName() + fieldQuery.getField().getName());

        // Création de notre paramètre
        Parameter operationParameter = factory.createParameter();
        operationParameter.setName(fieldQuery.getField().getName());

        // On récupère la property que nous rend getFieldAndreturnProperty pour avoir le type primitif du champ référencé
        Property property = getFieldAndReturnProperty(fieldQuery.getField(), model);
        operationParameter.setType(property.getType());

        // On ajoute le parametre à l'opération
        newOperation.getOwnedParameters().add(operationParameter);
        newOperation.setVisibility(VisibilityKind.get(VisibilityKind.PUBLIC));

        return newOperation;
    }
	
	
	//Question 3.5
	public static Interface getDepotAndReturnInterface(Repository repository, Model model) {
		
		UMLFactory factory = UMLFactory.eINSTANCE;
		
		//On crée une nouvelle interface
		Interface newInterface = factory.createInterface();
		
		//Je lui donne le nom demandé
		newInterface.setName(repository.getTypeEntity().getName()+"Repository");
		
		List<Query> listQueries = repository.getQueries();
		
		//Pour chaque requête contenu dans le dépôt
		for(Query query : listQueries) {
			
			//On ignore les requêtes BasicQuery et pour chacune on appelle la méthode précédemment définie pour récupérer les opérations correspondantes et les ajouter à l'interface
			if(query instanceof FieldDependantQuery) {
				Operation operation = getFieldDependantQueryAndReturnOperation((FieldDependantQuery) query, model);
				newInterface.getOwnedOperations().add(operation);
			}
			
		}
		
		//Je crée un commentaire avec ma factory, j'indique que son nom est @Repository
		Comment comment = factory.createComment();
		comment.setBody("@Repository");
				
		//J'ajoute le commentaire à ma classe, que je vais retourner
		newInterface.getOwnedComments().add(comment);
		
		return newInterface; 
		
	}	
	
	//Question 3.6
	public static Model getPaAModelAndReturnModel(PaAModel paAModel, Model model) {

        UMLFactory factory = UMLFactory.eINSTANCE;

        //Liste des entités du PaAModel
        List<Entity> listElement = paAModel.getEntities();

        //Liste des classes que l'on a déjà parcourues
        List<Class> listClassesAlreadyFounded = new ArrayList<Class>();

        //On crée un nouveau modèle
        Model newModel = factory.createModel();

        for (Entity entity : listElement) {

            //On crée une classe à partir de l'entité
            Class myClass = getEntityAndReturnClass(entity, model);
            listClassesAlreadyFounded.add(myClass);
            //Si l'entité a une super entité
            if (entity.getSuperEntity() != null) {

                //On prend la super classe
                Class superClass = getSuperClass(myClass, listClassesAlreadyFounded, entity, listElement, model);
                if (superClass != null) {

                    newModel.getPackagedElements().add(superClass);
                    Entity superEntity = entity.getSuperEntity();

                    //on regarde si la super classe à une super classe
                    // Si oui, on récupère sa super classe et ainsi de suite
                    while ((superEntity = superEntity.getSuperEntity()) != null) {

                        Class superSuperClass = getSuperClass(myClass, listClassesAlreadyFounded, superEntity, listElement, model);
                        if (superSuperClass != null) {
                            newModel.getPackagedElements().add(superSuperClass);
                        }
                    }
                }
            }
            newModel.getPackagedElements().add(myClass);
        }

        //Liste de repositories
        List<Repository> listRepository = paAModel.getRepositories();

        for (Repository repo : listRepository) {

            Interface repoInterface = getDepotAndReturnInterface(repo, model);
            newModel.getPackagedElements().add(repoInterface);

        }
        
        return newModel;
	}
	
	
	public static Class getSuperClass(Class myClass, List<Class> listClassesAlreadyFounded, Entity entity, List<Entity> listElement, Model model) {

        boolean entityAlreadyFounded = false;
        for (Class entityClass : listClassesAlreadyFounded) {

            //Si on a déjà créée la classe de la super entité
            //On la récupère directement
            if (entityClass.getName() == entity.getSuperEntity().getName()) {
                myClass.getSuperClasses().add(entityClass);
                entityAlreadyFounded = true;
                break;
            }
        }
        //Si la classe n'a pas encore été parcourue par la boucle
        //On la crée
        if (!entityAlreadyFounded) {

             Class superClass = getEntityAndReturnClass(entity.getSuperEntity(), model);
             myClass.getSuperClasses().add(superClass);
             listElement.remove(entity.getSuperEntity());
             listClassesAlreadyFounded.add(superClass);

             return superClass;
        }

        return null;
    }
	
	
	//Méthodes données qui permettent de sauver et charger un modèle
	public static void sauverModeleUML(String uri, EObject root) {
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

		public static PaAModel chargerModelePaA(String uri) {
			Resource resource = null;
			   try {
			      URI uriUri = URI.createURI(uri);
			      Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
			      resource = (new ResourceSetImpl()).createResource(uriUri);
			      XMLResource.XMLMap xmlMap = new XMLMapImpl();
			      xmlMap.setNoNamespacePackage(PaaPackage.eINSTANCE);
			      java.util.Map options = new java.util.HashMap();
			      options.put(XMLResource.OPTION_XML_MAP, xmlMap);
			      resource.load(options);
			   }
			   catch(Exception e) {
			      System.err.println("ERREUR chargement du modèle : "+e);
			      e.printStackTrace();
			   }
			   return (PaAModel) resource.getContents().get(0);
			}

}
