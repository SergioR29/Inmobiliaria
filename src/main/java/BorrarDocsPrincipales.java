import com.mongodb.DBRef;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.*;


public class BorrarDocsPrincipales {
	static Scanner sc = new Scanner(System.in);
	
	public static Map<Integer, Document> ListarPropietarios() {
		//Método auxiliar que muestra todos los propietarios registrados por nº identificador.
		
		Map<Integer, Document> propietariosKV = new LinkedHashMap<Integer, Document>();
		List<Document> propietarios = new ArrayList<Document>();
		MongoConnection.propietarios.find().into(propietarios);
		
		if(propietarios.size() > 0) {
			//Mostrar propietarios
			System.out.println("\nPROPIETARIOS REGISTRADOS");
			System.out.println("========================\n");
			
			for(int i=0; i < propietarios.size(); i++) {
				Document prop = propietarios.get(i);
				System.out.println((i+1) + ". " + prop.toJson());
				
				//Añadimos los datos al mapa
				propietariosKV.put((i+1), prop);
			}
			System.out.println("\n========================");
		}
		return propietariosKV;
	}
	
	public static Map<Integer, Document> ListarCompradores() {
		//Método auxiliar que muestra todos los compradores registrados.
		
		Map<Integer, Document> compradoresKV = new LinkedHashMap<>();
		List<Document> compradores = new ArrayList<Document>();
		MongoConnection.compradores.find().into(compradores);
		
		if(compradores.size() > 0) {
			//Mostrar compradores
			System.out.println("\nCOMPRADORES REGISTRADOS");
			System.out.println("=======================\n");
			
			for(int i=0; i < compradores.size(); i++) {
				Document comprador = compradores.get(i);
				System.out.println((i+1) + ". " + comprador.toJson());
				
				//Añadimos los datos al mapa.
				compradoresKV.put((i+1), comprador);
			}
			System.out.println("\n=======================");
		}
		return compradoresKV;
	}
	
	public static Map<Integer, Document> ListarAgentes() {
		//Método auxiliar que muestra todos los agentes inmobiliarios registrados.
		
		Map<Integer, Document> agentesKV = new LinkedHashMap<Integer, Document>();
		List<Document> agentes = new ArrayList<Document>();
		MongoConnection.agentes.find().into(agentes);
		
		if(agentes.size() > 0) {
			//Mostrar agentes
			System.out.println("\nAGENTES INMOBILIARIOS REGISTRADOS");
			System.out.println("=================================\n");
			
			for(int i=0; i < agentes.size(); i++) {
				Document agente = agentes.get(i);
				System.out.println((i+1) + ". " + agente.toJson());
				
				//Añadimos los datos al mapa.
				agentesKV.put((i+1), agente);
			}
			System.out.println("\n===============================");
		}
		return agentesKV;
	}
	
	public static Map<Integer, Document> ListarCasas() {
		//Método auxiliar que lista todas las casas registradas.
		
		Map<Integer, Document> casasKV = new TreeMap<>(); //Creamos un mapa con cada nº de casa y su información
		List<Document> casas = new ArrayList<Document>(); //Creamos una lista de casas para consultar todas
		MongoConnection.casas.find().into(casas);
		
		if(casas.size() > 0) {
			//Mostrar casas
			System.out.println("\nCASAS REGISTRADAS");
			System.out.println("=================\n");
			
			for(int i=0; i < casas.size(); i++) {
				Document casa = casas.get(i);
				
				//Añadimos cada casa y su número al mapa de pares clave-valor.
				casasKV.put((i+1), casa);
				
				//Evitar mostrar las referencias por errores
				casa.append("propietario", 0);
				casa.append("agente", 0);
				casa.append("comprador", 0);
				
				System.out.println((i+1) + ". " + casa.toJson());
			}
			System.out.println("\n=================");
		}
		return casasKV;
	}
	
	public static void BorrarPropietarios() {
		//Método que permite al usuario borrar documentos de la colección de propietarios.
		Map<Integer, Document> propietarios = ListarPropietarios();
		
		if(propietarios.size() > 0) {
			//Elección del propietario, si existe saco su _id en la base de datos.
			int numP = 0;
			ObjectId _id = null;
			Document propietarioEliminar = null;
			
			while(true) {
				try {
					System.out.print("Nº del propietario a eliminar: ");
					numP = sc.nextInt();
				} catch(InputMismatchException e) {
					sc.nextLine();
					System.out.println("---------------------------------------------");
					System.out.println("\tNº de propietario mal introducido.\n");
					continue;
				}
				
				//Comprobamos si el documento introducido existe (su nº).
				boolean existe = false;
				
				Set<Map.Entry<Integer, Document>> set = propietarios.entrySet();
				for(Iterator<Map.Entry<Integer, Document>> it = set.iterator(); it.hasNext();) {
					Map.Entry<Integer, Document> propietario = it.next();
					if(propietario.getKey() == numP) {
						//Sacamos el _id del propietario.
						propietarioEliminar = propietario.getValue();
						_id = propietarioEliminar.getObjectId("_id");
						
						existe = true;
						break;
					}
				}
				if(existe) break;
				else {
					System.out.println("---------------------------------------------");
					System.out.println("\tNo existe ningún propietario con ese nº.\n");
				}
			}
			
			/*Una vez seleccionado un documento existente de un propietario ya podemos eliminarlo.*/
			Bson filtro = Filters.eq("_id", _id);
			DeleteResult borrado = MongoConnection.propietarios.deleteOne(filtro);
			
			if(borrado.getDeletedCount() > 0) {
				System.out.println("\nPropietario eliminado.");
			}
			
			/*Una vez eliminado el documento de la colección de propietarios, eliminamos las referencias 
			  al documento del propietario también en los documentos de las colecciones en las que esté.*/
			DBRef refPropietario = new DBRef("Propietarios", _id);
			
			filtro = Filters.eq("propietario", refPropietario);
			Bson refBorrado = Updates.unset("propietario");
			MongoConnection.casas.updateMany(filtro, refBorrado); //En la de casas
			
			filtro = Filters.eq("Propietario", refPropietario);
			refBorrado = Updates.unset("Propietario");
			MongoConnection.contratos.updateMany(filtro, refBorrado); //En la de contratos
		}
	}
	
	public static void BorrarCompradores() {
		//Método que permite al usuario borrar documentos de la colección de compradores.
		Map<Integer, Document> compradores = ListarCompradores();
		
		if(compradores.size() > 0) {
			//Elección del comprador, si existe saco su _id en la base de datos.
			int numC = 0;
			ObjectId _id = null;
			Document compradorEliminar = null;
			
			while(true) {
				try {
					System.out.print("Nº del comprador a eliminar: ");
					numC = sc.nextInt();
				} catch(InputMismatchException e) {
					sc.nextLine();
					System.out.println("---------------------------------------------");
					System.out.println("\tNº del comprador mal introducido.\n");
					continue;
				}
				
				//Comprobamos si el documento introducido existe (su nº).
				boolean existe = false;
				
				Set<Map.Entry<Integer, Document>> set = compradores.entrySet();
				for(Iterator<Map.Entry<Integer, Document>> it = set.iterator(); it.hasNext();) {
					Map.Entry<Integer, Document> comprador = it.next();
					if(comprador.getKey() == numC) {
						//Sacamos el _id del comprador.
						compradorEliminar = comprador.getValue();
						_id = compradorEliminar.getObjectId("_id");
						
						existe = true;
						break;
					}
				}
				if(existe) break;
				else {
					System.out.println("---------------------------------------------");
					System.out.println("\tNo existe ningún comprador con ese nº.\n");
				}
			}
			
			/*Una vez seleccionado un documento existente de un comprador ya podemos eliminarlo.*/
			Bson filtro = Filters.eq("_id", _id);
			DeleteResult borrado = MongoConnection.compradores.deleteOne(filtro);
			
			if(borrado.getDeletedCount() > 0) {
				System.out.println("\nComprador eliminado.");
			}
			
			//Una vez eliminado el documento del comprador también eliminamos las referencias a él en otras colecciones.
			DBRef refComprador = new DBRef("Compradores", _id);
			
			filtro = Filters.eq("comprador", refComprador);
			Bson refBorrado = Updates.unset("comprador");
			MongoConnection.casas.updateMany(filtro, refBorrado); //En la de casas
			
			filtro = Filters.eq("Comprador", refComprador);
			refBorrado = Updates.unset("Comprador");
			
			MongoConnection.contratos.updateMany(filtro, refBorrado); //En la de contratos
			MongoConnection.visitas.updateMany(filtro, refBorrado); //En la de visitas, el campo se llama igual que en la de contratos.
		} else {
			System.out.println("No hay datos disponibles de compradores.");
		}
	}
	
	public static void BorrarAgentes() {
		//Método que permite al usuario borrar un documento de la colección de agentes.
		Map<Integer, Document> agentes = ListarAgentes();
		
		if(agentes.size() > 0) {
			//Elección del agente, si existe saco su _id en la base de datos.
			int numA = 0;
			ObjectId _id = null;
			Document agenteEliminar = null;
			
			while(true) {
				try {
					System.out.print("Nº del agente a eliminar: ");
					numA = sc.nextInt();
				} catch(InputMismatchException e) {
					sc.nextLine();
					System.out.println("---------------------------------------------");
					System.out.println("\tNº del agente mal introducido.\n");
					continue;
				}
				
				//Comprobamos si el documento introducido existe (su nº).
				boolean existe = false;
				
				Set<Map.Entry<Integer, Document>> set = agentes.entrySet();
				for(Iterator<Map.Entry<Integer, Document>> it = set.iterator(); it.hasNext();) {
					Map.Entry<Integer, Document> agente = it.next();
					if(agente.getKey() == numA) {
						//Sacamos el _id del agente.
						agenteEliminar = agente.getValue();
						_id = agenteEliminar.getObjectId("_id");
						
						existe = true;
						break;
					}
				}
				if(existe) break;
				else {
					System.out.println("---------------------------------------------");
					System.out.println("\tNo existe ningún agente con ese nº.\n");
				}
			}
			
			/*Una vez seleccionado un documento existente de la colección de agentes lo eliminamos.*/
			Bson filtro = Filters.eq("_id", _id);
			DeleteResult borrado = MongoConnection.agentes.deleteOne(filtro);
			
			if(borrado.getDeletedCount() > 0) {
				System.out.println("\nAgente eliminado.");
			}
			
			/*Una vez eliminado el documento de la colección de agentes, también eliminamos referencias 
			  a él en las demás colecciones en las que pueda estar.*/
			DBRef refAgente = new DBRef("Agentes_Inmobiliarios", _id);
			
			filtro = Filters.eq("agente", refAgente);
			Bson refBorrado = Updates.unset("agente");
			MongoConnection.casas.updateMany(filtro, refBorrado); //En la de casas
			
			filtro = Filters.eq("Agente", refAgente);
			refBorrado = Updates.unset("Agente");
			MongoConnection.contratos.updateMany(filtro, refBorrado); //En la de contratos
			MongoConnection.visitas.updateMany(filtro, refBorrado); //En la de visitas
		} else {
			System.out.println("No hay datos de agentes disponibles.");
		}
	}
	
	public static void BorrarCasas() {
		//Método que permite al usuario borrar documentos de la colección de casas.
		Map<Integer, Document> casas = ListarCasas();
		
		if(casas.size() > 0) {
			//Elección del agente, si existe saco su _id en la base de datos.
			int numC = 0;
			ObjectId _id = null;
			Document casaEliminar = null;
			
			while(true) {
				try {
					System.out.print("Nº de la casa a eliminar: ");
					numC = sc.nextInt();
				} catch(InputMismatchException e) {
					sc.nextLine();
					System.out.println("---------------------------------------------");
					System.out.println("\tNº de la casa mal introducido.\n");
					continue;
				}
				
				//Comprobamos si el documento introducido existe (su nº).
				boolean existe = false;
				
				Set<Map.Entry<Integer, Document>> set = casas.entrySet();
				for(Iterator<Map.Entry<Integer, Document>> it = set.iterator(); it.hasNext();) {
					Map.Entry<Integer, Document> casa = it.next();
					if(casa.getKey() == numC) {
						//Sacamos el _id de la casa.
						casaEliminar = casa.getValue();
						_id = casaEliminar.getObjectId("_id");
						
						existe = true;
						break;
					}
				}
				if(existe) break;
				else {
					System.out.println("---------------------------------------------");
					System.out.println("\tNo existe ninguna casa con ese nº.\n");
				}
			}
			
			/*Una vez seleccionado un documento existente de la colección de casas lo eliminamos.*/
			Bson filtro = Filters.eq("_id", _id);
			DeleteResult borrado = MongoConnection.casas.deleteOne(filtro);
			
			if(borrado.getDeletedCount() > 0) {
				System.out.println("\nCasa eliminada.");
			}
			
			/*Una vez eliminado el documento de la colección de casas, también eliminamos las referencias 
			  a él en otras colecciones en las que pueda estar.*/
			DBRef refCasa = new DBRef("Casas", _id);
			
			filtro = Filters.eq("Casa", refCasa);
			Bson refBorrado = Updates.unset("Casa");
			
			MongoConnection.contratos.updateMany(filtro, refBorrado); //En la de contratos
			MongoConnection.visitas.updateMany(filtro, refBorrado); //En la de visitas
			MongoConnection.mantenimientos.updateMany(filtro, refBorrado); //En la de mantenimientos
		} else {
			System.out.println("No hay datos de casas disponibles.");
		}
	}
}
