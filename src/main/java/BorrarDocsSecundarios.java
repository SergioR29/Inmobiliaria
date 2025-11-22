import com.mongodb.DBRef;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.*;


public class BorrarDocsSecundarios {
	static Scanner sc = new Scanner(System.in);

	public static void BorrarContratos() {
		//Método que permite al usuario borrar un documento de un contrato escogido por el usuario.
		Map<Integer, Document> contratos = ActualizarDocsSecundarios.ListarContratos();
		
		if(contratos.size() > 0) {
			//Elección del contrato, si existe saco su _id en la base de datos.
			int numC = 0;
			ObjectId _id = null;
			Document contratoEliminar = null;
			
			while(true) {
				try {
					System.out.print("Nº del contrato a eliminar: ");
					numC = sc.nextInt();
				} catch(InputMismatchException e) {
					sc.nextLine();
					System.out.println("---------------------------------------------");
					System.out.println("\tNº de contrato mal introducido.\n");
					continue;
				}
				
				//Comprobamos si el documento introducido existe (su nº).
				boolean existe = false;
				
				Set<Map.Entry<Integer, Document>> set = contratos.entrySet();
				for(Iterator<Map.Entry<Integer, Document>> it = set.iterator(); it.hasNext();) {
					Map.Entry<Integer, Document> contrato = it.next();
					if(contrato.getKey() == numC) {
						//Sacamos el _id del contrato.
						contratoEliminar = contrato.getValue();
						_id = contratoEliminar.getObjectId("_id");
						
						existe = true;
						break;
					}
				}
				if(existe) break;
				else {
					System.out.println("---------------------------------------------");
					System.out.println("\tNo existe ningún contrato con ese nº.\n");
				}
			}
			
			//Si el contrato existe, ya lo podemos eliminar y mostrar el resultado de ello.
			Bson filtro = Filters.eq("_id", _id);
			DeleteResult eliminado = MongoConnection.contratos.deleteOne(filtro);
			
			if(eliminado.getDeletedCount() > 0) {
				System.out.println("\nContrato eliminado.");
			}
			
			/*Como puede haber referencias al contrato en los documentos de la colección de pagos, 
			  buscamos y eliminamos esas referencias en los documentos correspondientes de la colección.
			  Sólo puede haber referencias a contratos en la colección de pagos.*/
			DBRef refContrato = new DBRef("Contratos", _id);
			
			filtro = Filters.eq("Contrato", refContrato);
			Bson desReferenciado = Updates.unset("Contrato");
			MongoConnection.pagos.updateMany(filtro, desReferenciado);
			
		} else {
			System.out.println("No hay datos de contratos disponibles.");
		}
	}
	
	public static void BorrarPagos() {
		//Método que permite al usuario borrar un documento de la colección de pagos.
		Map<Integer, Document> pagos = ActualizarDocsSecundarios.ListarPagos();
		
		if(pagos.size() > 0) {
			//Elección del pago, si existe saco su _id en la base de datos.
			int numP = 0;
			ObjectId _id = null;
			Document pagoEliminar = null;
			
			while(true) {
				try {
					System.out.print("Nº del pago a eliminar: ");
					numP = sc.nextInt();
				} catch(InputMismatchException e) {
					sc.nextLine();
					System.out.println("---------------------------------------------");
					System.out.println("\tNº de pago mal introducido.\n");
					continue;
				}
				
				//Comprobamos si el documento introducido existe (su nº).
				boolean existe = false;
				
				Set<Map.Entry<Integer, Document>> set = pagos.entrySet();
				for(Iterator<Map.Entry<Integer, Document>> it = set.iterator(); it.hasNext();) {
					Map.Entry<Integer, Document> pago = it.next();
					if(pago.getKey() == numP) {
						//Sacamos el _id del pago.
						pagoEliminar = pago.getValue();
						_id = pagoEliminar.getObjectId("_id");
						
						existe = true;
						break;
					}
				}
				if(existe) break;
				else {
					System.out.println("---------------------------------------------");
					System.out.println("\tNo existe ningún pago con ese nº.\n");
				}
			}
			
			/*Una vez encontrado el pago, ya podemos eliminarlo.*/
			Bson filtro = Filters.eq("_id", _id);
			DeleteResult borrado = MongoConnection.pagos.deleteOne(filtro);
			
			if(borrado.getDeletedCount() > 0) {
				System.out.println("\nPago eliminado.");
			}
		} else {
			System.out.println("No hay datos de pagos disponibles.");
		}
	}
	
	public static void BorrarMantenimientos() {
		//Método que permitirá al usuario borrar un documento de la colección de mantenimientos.
		Map<Integer, Document> mantenimientos = ActualizarDocsSecundarios.ListarMantenimientos();
		
		if(mantenimientos.size() > 0) {
			//Elección del mantenimiento, si existe saco su _id en la base de datos.
			int numM = 0;
			ObjectId _id = null;
			Document mantenimientoEliminar = null;
			
			while(true) {
				try {
					System.out.print("Nº del mantenimiento a eliminar: ");
					numM = sc.nextInt();
				} catch(InputMismatchException e) {
					sc.nextLine();
					System.out.println("---------------------------------------------");
					System.out.println("\tNº de mantenimiento mal introducido.\n");
					continue;
				}
				
				//Comprobamos si el documento introducido existe (su nº).
				boolean existe = false;
				
				Set<Map.Entry<Integer, Document>> set = mantenimientos.entrySet();
				for(Iterator<Map.Entry<Integer, Document>> it = set.iterator(); it.hasNext();) {
					Map.Entry<Integer, Document> mantenimiento = it.next();
					if(mantenimiento.getKey() == numM) {
						//Sacamos el _id del mantenimiento.
						mantenimientoEliminar = mantenimiento.getValue();
						_id = mantenimientoEliminar.getObjectId("_id");
						
						existe = true;
						break;
					}
				}
				if(existe) break;
				else {
					System.out.println("---------------------------------------------");
					System.out.println("\tNo existe ningún mantenimiento con ese nº.\n");
				}
			}
			
			/*Una vez encontrado el pago, ya podemos eliminarlo.*/
			Bson filtro = Filters.eq("_id", _id);
			DeleteResult borrado = MongoConnection.mantenimientos.deleteOne(filtro);
			
			if(borrado.getDeletedCount() > 0) {
				System.out.println("\nMantenimiento eliminado.");
			}
		} else {
			System.out.println("No hay datos de mantenimientos disponibles.");
		}
	}
	
	public static void BorrarVisitas() {
		//Método que permite al usuario borrar un documento de la colección de visitas.
		Map<Integer, Document> visitas = ActualizarDocsSecundarios.ListarVisitas();
		
		if(visitas.size() > 0) {
			//Elección de la visita, si existe saco su _id en la base de datos.
			int numV = 0;
			ObjectId _id = null;
			Document visitaEliminar = null;
			
			while(true) {
				try {
					System.out.print("Nº de la visita a eliminar: ");
					numV = sc.nextInt();
				} catch(InputMismatchException e) {
					sc.nextLine();
					System.out.println("---------------------------------------------");
					System.out.println("\tNº de la visita mal introducido.\n");
					continue;
				}
				
				//Comprobamos si el documento introducido existe (su nº).
				boolean existe = false;
				
				Set<Map.Entry<Integer, Document>> set = visitas.entrySet();
				for(Iterator<Map.Entry<Integer, Document>> it = set.iterator(); it.hasNext();) {
					Map.Entry<Integer, Document> visita = it.next();
					if(visita.getKey() == numV) {
						//Sacamos el _id de la visita.
						visitaEliminar = visita.getValue();
						_id = visitaEliminar.getObjectId("_id");
						
						existe = true;
						break;
					}
				}
				if(existe) break;
				else {
					System.out.println("---------------------------------------------");
					System.out.println("\tNo existe ninguna visita con ese nº.\n");
				}
			}
			
			/*Una vez encontrado el pago, ya podemos eliminarlo.*/
			Bson filtro = Filters.eq("_id", _id);
			DeleteResult borrado = MongoConnection.visitas.deleteOne(filtro);
			
			if(borrado.getDeletedCount() > 0) {
				System.out.println("\nVisita eliminada.");
			}
		} else {
			System.out.println("No hay datos de visitas disponibles.");
		}
	}
}
