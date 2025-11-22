import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.*;

import org.bson.*;
import org.bson.conversions.Bson;
import org.bson.types.*;

import java.util.*;


public class ActualizarDocsSecundarios {
	static Scanner sc = new Scanner(System.in);
	
	public static Map<Integer, Document> ListarContratos() {
		//Método que listará todos los datos de los contratos ordenados por un nº identificativo.
		
		Map<Integer, Document> contratosKV = new TreeMap<>(); //Creamos un mapa de pares clave-valor para los contratos
		List<Document> contratos = new ArrayList<>(); //Creamos una lista para consultar todos los contratos guardados
		MongoConnection.contratos.find().into(contratos);
		
		if(contratos.size() > 0) {
			//Mostrar contratos
			System.out.println("\nCONTRATOS REGISTRADOS");
			System.out.println("=====================\n");
			
			for(int i=0; i < contratos.size(); i++) {
				/*Esta vez permitimos que se muestre el _id para poder recuperarlo.*/
				Document contrato = contratos.get(i);
				
				//Evitar mostrar las referencias por errores
				contrato.append("Propietario", 0);
				contrato.append("Agente", 0);
				contrato.append("Comprador", 0);
				contrato.append("Casa", 0);
				
				System.out.println((i+1) + ". " + contrato.toJson());
				
				//Añadimos los datos al mapa
				contratosKV.put((i+1), contrato);
			}
			System.out.println("\n=====================");
		}
		return contratosKV;
	}
	
	public static Map<Integer, Document> ListarPagos() {
		//Método que listará todos los documentos de pagos.
		
		Map<Integer, Document> pagosKV = new LinkedHashMap<>(); //Creamos un mapa pares clave-valor para almacenar todos los documentos de pagos ordenados por un ID numérico.
		List<Document> pagos = new ArrayList<>(); //Creamos una lista en la que guardar todos los pagos consultados.
		MongoConnection.pagos.find().into(pagos);
		
		if(pagos.size() > 0) {
			//Mostrar pagos
			System.out.println("\nPAGOS REGISTRADOS");
			System.out.println("=================\n");
			
			for(int i=0; i < pagos.size(); i++) {
				/*Esta vez permitimos que se muestre el _id para poder recuperarlo.*/
				Document pago = pagos.get(i);
				
				//Añadimos los datos al mapa
				pagosKV.put((i+1), pago);
				pago.remove("Contrato"); //Excluimos los campos referenciados para evitar errores en el toJson.
				
				//Mostramos los datos
				System.out.println((i+1) + ". " + pago.toJson());
			}
			System.out.println("\n=================");
		}
		
		return pagosKV;
	}
	
	public static Map<Integer, Document> ListarMantenimientos() {
		//Método que listará todos los documentos de mantenimientos.
		
		Map<Integer, Document> mantenimientosKV = new LinkedHashMap<>(); //Creamos un mapa pares clave-valor para almacenar todos los documentos de pagos ordenados por un ID numérico.
		List<Document> mantenimientos = new ArrayList<>(); //Creamos una lista en la que guardar todos los mentenimientos consultados.
		MongoConnection.mantenimientos.find().into(mantenimientos);
		
		if(mantenimientos.size() > 0) {
			//Mostrar mantenimientos
			System.out.println("\nMANTENIMIENTOS REGISTRADOS");
			System.out.println("==========================\n");
			
			for(int i=0; i < mantenimientos.size(); i++) {
				/*Esta vez permitimos que se muestre el _id para poder recuperarlo.*/
				Document mantenimiento = mantenimientos.get(i);

				//Añadimos los datos al mapa.
				mantenimientosKV.put((i+1), mantenimiento);
				mantenimiento.remove("Casa"); //Excluimos los campos referenciados para evitar errores en el toJson.
				
				//Mostramos datos
				System.out.println((i+1) + ". " + mantenimiento.toJson());
				
			}
			System.out.println("\n==========================");
		}
		return mantenimientosKV;
	}
	
	public static Map<Integer, Document> ListarVisitas() {
		//Método que listará todos los documentos de visitas.
		
		Map<Integer, Document> visitasKV = new LinkedHashMap<>(); //Mapa para almacenar todos los documentos de visitas
		List<Document> visitas = new ArrayList<>();
		MongoConnection.visitas.find().into(visitas);
		
		if(visitas.size() > 0) {
			//Mostrar visitas
			System.out.println("\nVISITAS REGISTRADAS");
			System.out.println("===================\n");
			
			for(int i=0; i < visitas.size(); i++) {
				/*Esta vez permitimos que se muestre el _id para poder recuperarlo.*/
				Document visita = visitas.get(i);
				
				//Almacenar los datos
				visitasKV.put((i+1), visita);
				
				//Excluimos los campos referenciados para evitar errores.
				visita.remove("Comprador");
				visita.remove("Casa");
				visita.remove("Agente");
				
				//Mostrar datos
				System.out.println((i+1) + ". " + visita.toJson());
			}
			System.out.println("\n===================");
		}
		return visitasKV;
	}
	
	public static void ActualizarContratos() {
		//Método que permitirá actualizar el estado de los contratos del tipo que sea.
		Map<Integer, Document> contratos = ListarContratos();
		if(contratos.size() > 0) {
			//Elección del contrato, si existe saco su _id en la base de datos.
			int numC = 0;
			ObjectId _id = null;
			
			while(true) {
				try {
					System.out.print("Nº del contrato a modificar su estado: ");
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
						Document documento = contrato.getValue();
						_id = documento.getObjectId("_id");
						
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
			
			//Pedimos su nuevo estado.
			String estado = "";
			System.out.print("Nuevo estado del contrato (activo, finalizado, cancelado): ");
			
			while(estado.length() == 0) {
				estado = sc.nextLine();
				
				if(estado.isBlank() && estado.length() > 0) {
					estado = "";
					System.out.println("----------------------------------------------------------------------------------");
					System.out.println("\tNo se ha introducido el nuevo estado del contrato.\n");

					System.out.print("Nuevo estado del contrato (activo, finalizado, cancelado): ");
					
				} else if(!estado.isBlank()) {
					if(!estado.equalsIgnoreCase("activo") && !estado.equalsIgnoreCase("finalizado") && !estado.equalsIgnoreCase("cancelado")) {
						estado = "";
						System.out.println("----------------------------------------------------------------------------------");
						System.out.println("\tNuevo estado de contrato inválido.\n");
						
						System.out.print("Nuevo estado del contrato (activo, finalizado, cancelado): ");
					} else {
						break;
					}
				}
			}
			
			//Hora de actualizar el contrato elegido, ya tenemos su nuevo estado introducido por el usuario.
			Bson filtro = Filters.eq("_id", _id);
			Bson update = Updates.set("estado", estado);
			
			UpdateResult actualizado = MongoConnection.contratos.updateOne(filtro, update);
			if(actualizado.getModifiedCount() > 0) {
				System.out.println("\nEstado del contrato actualizado.");
				System.out.println("Mostrando documento actualizado en JSON:\n");
			}
			
			//Mostrar documento del contrato actualizado en JSON.
			Document contrato = MongoConnection.contratos.find(new Document("_id", _id)).first();
			
			//Excluir sus referencias para evitar errores.
			contrato.append("Propietario", 0);
			contrato.append("Agente", 0);
			contrato.append("Comprador", 0);
			contrato.append("Casa", 0);
			
			System.out.println(contrato.toJson());
		} else {
			System.out.println("No hay datos de contratos disponibles.");
		}
	}
	
	public static void ActualizarMantenimientos() {
		//Método que permitirá actualizar el estado de los mantenimientos de las casas.
		Map<Integer, Document> mantenimientos = ListarMantenimientos();
		if(mantenimientos.size() > 0) {
			//Elección del mantenimiento, si existe saco su _id en la base de datos.
			int numM = 0;
			ObjectId _id = null;
			
			while(true) {
				try {
					System.out.print("Nº del mantenimiento a modificar su estado: ");
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
						Document documento = mantenimiento.getValue();
						_id = documento.getObjectId("_id");
						
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
			
			//Una vez seleccionado el mantenimiento permitimos introducir su nuevo estado.
			String estado = "";
			System.out.print("Nuevo estado del mantenimiento (pendiente -> [por defecto], en proceso, finalizado): ");
			
			while(estado.length() == 0) {
				estado = sc.nextLine();
										
				if(estado.isBlank() && estado.length() > 0) {
					estado = "";
					System.out.println("----------------------------------------------------------------------------------");
					System.out.println("\tNo se ha introducido el estado del mantenimiento.\n");
							
					System.out.print("Nuevo estado del mantenimiento (pendiente -> [por defecto], en proceso, finalizado): ");
					
				} else if(estado.length() > 0) {
					if(!estado.equalsIgnoreCase("pendiente") && !estado.equalsIgnoreCase("en proceso") 
					   && !estado.equalsIgnoreCase("finalizado")) {
						
						estado = "pendiente";
					}
				}
			}
			
			//Hora de actualizar el contrato elegido, ya tenemos su nuevo estado introducido por el usuario.
			Bson filtro = Filters.eq("_id", _id);
			Bson update = Updates.set("estado", estado);
			
			UpdateResult actualizado = MongoConnection.mantenimientos.updateOne(filtro, update);
			if(actualizado.getModifiedCount() > 0) {
				System.out.println("\nEstado del mantenimiento actualizado.");
				System.out.println("Mostrando documento actualizado en JSON:\n");
			}
			
			//Mostrar documento del mantenimiento en JSON.
			Document mantenimiento = MongoConnection.mantenimientos.find(new Document("_id", _id)).first();
			
			//Excluir referencias para evitar errores.
			mantenimiento.append("Casa", 0);
			System.out.println(mantenimiento.toJson());
			
		} else {
			System.out.println("No hay datos de mantenimientos disponibles.");
		}
	}

	public static void ActualizarPagos() {
		//Método que permite actualizar la fecha los pagos.
		Map<Integer, Document> pagos = ListarPagos();
		if(pagos.size() > 0) {
			//Elección del pago, si existe saco su _id en la base de datos.
			int numP = 0;
			ObjectId _id = null;
			
			while(true) {
				try {
					System.out.print("Nº del pago a modificar su fecha: ");
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
						Document documento = pago.getValue();
						_id = documento.getObjectId("_id");
						
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
			
			//Pedir la nueva fecha (dia/mes/año)
			String fecha = "";
			int dia = 0, mes = 0, año = 0;
			
			System.out.println("\nDATOS DE LA NUEVA FECHA DEL PAGO");
			System.out.println("================================\n");
			
			while(true) {//Día
				try {
					System.out.print("\t- Día: ");
					dia = sc.nextInt();
				} catch (InputMismatchException e) {
					sc.nextLine();
					System.out.println("\t  ============");
					System.out.println("\t  Día inválido\n");
					continue;
				}
				
				if(dia <= 0 || dia > 31) {
					System.out.println("\t  ============");
					System.out.println("\t  Día inválido\n");
					continue;
				}
				break;
			}
			while(true) {//Mes
				try {
					System.out.print("\t- Mes: ");
					mes = sc.nextInt();
				} catch (InputMismatchException e) {
					sc.nextLine();
					System.out.println("\t  ============");
					System.out.println("\t  Mes inválido\n");
					continue;
				}
				
				if(mes <= 0 || mes > 12) {
					System.out.println("\t  ============");
					System.out.println("\t  Mes inválido\n");
					continue;
				}
				break;
			}
			while(true) {//Año
				try {
					System.out.print("\t- Año: ");
					año = sc.nextInt();
				} catch (InputMismatchException e) {
					sc.nextLine();
					System.out.println("\t  ============");
					System.out.println("\t  Año inválido\n");
					continue;
				}
				
				if(año < -1) {
					System.out.println("\t  ============");
					System.out.println("\t  Año inválido\n");
					continue;
				}
				break;
			}
			fecha = dia + "/" + mes + "/" + año; //Juntamos los datos necesarios para formar la fecha.
			System.out.println("\n================================");
			
			//Una vez formada la nueva fecha de pago actualizamos el documento correspondiente.
			Bson filtro = Filters.eq("_id", _id);
			Bson update = Updates.set("fecha_pago", fecha);
			
			UpdateResult actualizado = MongoConnection.pagos.updateOne(filtro, update);
			if(actualizado.getModifiedCount() > 0) {
				System.out.println("\nFecha del pago actualizada.");
				System.out.println("Mostrando documento actualizado en JSON:\n");
			}
			
			//Mostrar documento del pago en JSON.
			Document pago = MongoConnection.pagos.find(new Document("_id", _id)).first();
			
			//Excluir referencias del pago para evitar errores.
			pago.append("Contrato", 0);
			System.out.println(pago.toJson());
			
		} else {
			System.out.println("No hay datos de pagos disponibles.");
		}
	}

	public static void ActualizarVisitas() {
		//Método que permite actualizar la fecha y la hora de las visitas.
		Map<Integer, Document> visitas = ListarVisitas();
		if(visitas.size() > 0) {
			//Elección de la visita, si existe saco su _id en la base de datos.
			int numV = 0;
			ObjectId _id = null;
			
			while(true) {
				try {
					System.out.print("Nº de la visita a modificar su fecha y hora: ");
					numV = sc.nextInt();
				} catch(InputMismatchException e) {
					sc.nextLine();
					System.out.println("---------------------------------------------");
					System.out.println("\tNº de visita mal introducido.\n");
					continue;
				}
				
				//Comprobamos si el documento introducido existe (su nº).
				boolean existe = false;
				
				Set<Map.Entry<Integer, Document>> set = visitas.entrySet();
				for(Iterator<Map.Entry<Integer, Document>> it = set.iterator(); it.hasNext();) {
					Map.Entry<Integer, Document> visita = it.next();
					if(visita.getKey() == numV) {
						//Sacamos el _id de la visita.
						Document documento = visita.getValue();
						_id = documento.getObjectId("_id");
						
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
			
			//Pedir la nueva fecha (dia/mes/año)
			String fecha = "";
			int dia = 0, mes = 0, año = 0;
			
			System.out.println("\nNUEVA FECHA DE LA VISITA");
			System.out.println("========================\n");
			
			while(true) {//Día
				try {
					System.out.print("\t- Día: ");
					dia = sc.nextInt();
				} catch (InputMismatchException e) {
					sc.nextLine();
					System.out.println("\t  ============");
					System.out.println("\t  Día inválido\n");
					continue;
				}
				
				if(dia <= 0 || dia > 31) {
					System.out.println("\t  ============");
					System.out.println("\t  Día inválido\n");
					continue;
				}
				break;
			}
			while(true) {//Mes
				try {
					System.out.print("\t- Mes: ");
					mes = sc.nextInt();
				} catch (InputMismatchException e) {
					sc.nextLine();
					System.out.println("\t  ============");
					System.out.println("\t  Mes inválido\n");
					continue;
				}
				
				if(mes <= 0 || mes > 12) {
					System.out.println("\t  ============");
					System.out.println("\t  Mes inválido\n");
					continue;
				}
				break;
			}
			while(true) {//Año
				try {
					System.out.print("\t- Año: ");
					año = sc.nextInt();
				} catch (InputMismatchException e) {
					sc.nextLine();
					System.out.println("\t  ============");
					System.out.println("\t  Año inválido\n");
					continue;
				}
				
				if(año < -1) {
					System.out.println("\t  ============");
					System.out.println("\t  Año inválido\n");
					continue;
				}
				break;
			}
			fecha = dia + "/" + mes + "/" + año; //Juntamos los datos necesarios para formar la fecha.
			System.out.println("\n================================");
			
			//Pedimos la nueva hora de la visita (HH:mm)
			String hora = "";
			int num_hora = -2, minutos = -2;
			
			System.out.println("\nNUEVA HORA DE LA VISITA");
			System.out.println("=======================\n");
			
			while(num_hora < -1 || num_hora > 23) {//Hora
				try {
					System.out.print("\t- Hora (0-23): ");
					num_hora = sc.nextInt();
				} catch (InputMismatchException e) {
					sc.nextLine();
					System.out.println("\t  ============");
					System.out.println("\t  Hora inválida\n");
					continue;
				}
				
				if(num_hora < -1 || num_hora > 23) {
					System.out.println("\t  ============");
					System.out.println("\t  Hora inválida\n");
				} else {
					break;
				}
			}
			while(minutos < -1 || minutos > 59) {//Minutos
				try {
					System.out.print("\t- Hora (0-59): ");
					minutos = sc.nextInt();
				} catch (InputMismatchException e) {
					sc.nextLine();
					System.out.println("\t  ============");
					System.out.println("\t  Hora inválida\n");
					continue;
				}
				
				if(minutos < -1 || minutos > 59) {
					System.out.println("\t  =================");
					System.out.println("\t  Minutos inválidos\n");
				} else {
					break;
				}
			}
			hora = num_hora + ":" + (minutos >= 10 ? minutos : ("0" + minutos)); //Juntamos los datos de la hora
			
			//Ya tenemos la nueva fecha y hora de la visita, ahora hay que actualizar el documento correspondiente.
			Bson filtro = Filters.eq("_id", _id);
			Bson updateF = Updates.set("fecha", fecha);
			Bson updateH = Updates.set("hora", hora);
			
			//Metemos en una lista los objetos Bson con cada campo a actualizar.
			List<Bson> updates = new ArrayList<>();
			updates.add(updateF);
			updates.add(updateH);
			
			//Ya podemos actualizar los datos del documento con los dos campos a la vez.
			UpdateResult actualizado = MongoConnection.visitas.updateOne(filtro, updates);
			if(actualizado.getModifiedCount() > 0) {
				System.out.println("\nFecha y hora de la visita actualizada.");
				System.out.println("Mostrando documento actualizado en JSON:\n");
			}
			
			//Documento de la visita en JSON.
			Document visita = MongoConnection.visitas.find(new Document("_id", _id)).first();
			
			//Excluimos las referencias para evitar errores.
			visita.append("Comprador", 0);
			visita.append("Casa", 0);
			visita.append("Agente", 0);
			
			System.out.println(visita.toJson());
		} else {
			System.out.println("No hay datos de visitas disponibles.");
		}
	}
}
