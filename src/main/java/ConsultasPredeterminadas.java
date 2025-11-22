import com.mongodb.DBRef;
import com.mongodb.client.MongoCursor;
import static com.mongodb.client.model.Aggregates.*;

import org.bson.Document;
import org.bson.types.ObjectId;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Sorts.*;
import static com.mongodb.client.model.Accumulators.*;

import java.io.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;


public class ConsultasPredeterminadas {
	static String resultado_consultaTXT = ""; //Atributo con el resultado de la consulta realizada en el momento para exportar a TXT.
	static String resultado_consultaJSON = ""; //Atributo con el resultado de la consulta realizada en el momento para exportar a JSON.
	
	public static void ExportarResultado() {
		//Método que permitirá al usuario guardar el resultado de la consulta en un fichero JSON y a otro fichero TXT
		
		try(BufferedWriter bw_JSON = new BufferedWriter(new FileWriter(new File("resultado_consulta.json")));
				BufferedWriter bw_TXT = new BufferedWriter(new FileWriter(new File("resultado_consulta.txt")))) {
			
			bw_JSON.write(resultado_consultaJSON); //Exportamos el resultado de la consulta a JSON
			bw_TXT.write(resultado_consultaTXT); //Exportamos el resultado de la consulta a TXT
			
			//Dejamos vacíos los atributos que guardan el resultado de la consulta para evitar inconsistencias
			resultado_consultaJSON = "";
			resultado_consultaTXT = "";
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void Consulta1() {
		//Método que permite al usuario ejecutar la consulta nº1.
		MongoCursor<Document> consulta1 = MongoConnection.casas.aggregate(Arrays.asList(
				group("promedio", avg("promedio", "$precio")))).iterator();
		
		System.out.println("\n\n==========");
		System.out.println("CONSULTA 1");
		System.out.println("==========");
		
		//Mostrar los datos de la consulta
		while(consulta1.hasNext()) {
			Document promedio = consulta1.next();
			System.out.println("\nPrecio medio de las casa: " + String.format("%.2f", promedio.getDouble("promedio")) + " €");
			
			//Guardamos el resultado en el atributo de texto y luego llamamos al método correspondiente.
			resultado_consultaTXT = "Precio medio de las casas: " + String.format("%.2f", promedio.getDouble("promedio")) + " €";
			ExportarResultado(); //La llamada al método está dentro del bucle porque se sabe que sólo hay 1 resultado.
		}
	}
	
	public static void Consulta2() {
		//Método que permite al usuario ejecutar la consulta nº2.
		MongoCursor<Document> consulta2 = MongoConnection.casas.aggregate(Arrays.asList(
				group("$ciudad", sum("casas", 1)))).iterator();
		
		System.out.println("\n\n==========");
		System.out.println("CONSULTA 2");
		System.out.println("==========\n");
		
		//Recuperar datos de la consulta
		while(consulta2.hasNext()) {
			Document casas = consulta2.next();
			
			System.out.println("Ciudad: " + casas.getString("_id"));
			System.out.println("NºCasas: " + casas.getInteger("casas") + "\n");
			
			//Guardamos el resultado en el atributo de texto y luego llamamos al método correspondiente.
			resultado_consultaTXT += "Ciudad: " + casas.getString("_id") + "\n";
			resultado_consultaTXT += "NºCasas: " + casas.getInteger("casas") + "\n\n";
			
			resultado_consultaJSON += casas.toJson() + "\n";
		}
		ExportarResultado(); //La llamada al método está fuera del bucle si devuelve más de 1 resultado.
	}
	
	public static void Consulta3() {
		//Método que permite al usuario ejecutar la consulta nº3.
		MongoCursor<Document> consulta3 = MongoConnection.casas.aggregate(Arrays.asList(
				sort(descending("precio")), limit(1), 
				project(new Document("direccion", 1)
                        .append("ciudad", 1)
                        .append("provincia", 1)
                        .append("codigo_postal", 1)
                        .append("precio", 1)
                ))).iterator();
		
		System.out.println("\n\n==========");
		System.out.println("CONSULTA 3");
		System.out.println("==========\n");
		
		//Mostrar datos de la consulta
		while(consulta3.hasNext()) {
			Document casa = consulta3.next();
			
			System.out.println("Dirección: " + casa.getString("direccion"));
			System.out.println("Ciudad: " + casa.getString("ciudad"));
			System.out.println("Provincia: " + casa.getString("provincia"));
			System.out.println("Código Postal: " + casa.getString("codigo_postal"));
			System.out.println("Precio: " + casa.getInteger("precio") + " €");
			
			//Nos encargamos de la exportación del resultado.
			resultado_consultaTXT += "Dirección: " + casa.getString("direccion") + "\n";
			resultado_consultaTXT += "Ciudad: " + casa.getString("ciudad") + "\n";
			resultado_consultaTXT += "Provincia: " + casa.getString("provincia") + "\n";
			resultado_consultaTXT += "Código Postal: " + casa.getString("codigo_postal") + "\n";
			resultado_consultaTXT += "Dirección: " + casa.getString("direccion") + "\n";
			
			resultado_consultaJSON = casa.toJson();
		}
		ExportarResultado();
	}

	public static void Consulta4() {
		//Método que permite al usuario ejecutar la consulta nº4.
		MongoCursor<Document> consulta4 = MongoConnection.casas.aggregate(Arrays.asList(
				match(gte("habitaciones", 2)), 
				project(new Document("direccion", 1)
						.append("propietario",  1)
						.append("habitaciones", 1)))).iterator();
		
		System.out.println("\n\n==========");
		System.out.println("CONSULTA 4");
		System.out.println("==========\n");
		
		while(consulta4.hasNext()) {
			Document casas = consulta4.next();
			
			System.out.println("Dirección: " + casas.getString("direccion"));
			resultado_consultaTXT += "Dirección: " + casas.getString("direccion") + "\n";
			
			System.out.println("Habitaciones: " + casas.getInteger("habitaciones"));
			resultado_consultaTXT += "Habitaciones: " + casas.getInteger("habitaciones") + "\n";
			
			
			//Dereferenciar al propietario de la casa
			DBRef propietarioDBRef = (DBRef) casas.get("propietario");
			
			if(propietarioDBRef != null) {
				Document propietario = MongoConnection.propietarios.find(new Document("_id", propietarioDBRef.getId())).first();
				if(propietario != null) {
                    casas.append("propietario", propietario); // Agregar datos completos del propietario al documento casa
					
                    System.out.println("Propietario: " + propietario.getString("DNI") + ", " + propietario.getList("nombre", String.class) == null ? "" : propietario.getList("nombre", String.class));
					resultado_consultaTXT += "Propietario: " + propietario.getString("DNI") + ", " + propietario.getList("nombre", String.class) == null ? "" : propietario.getList("nombre", String.class);
				} else {
					System.out.println("Propietario no encontrado");
					resultado_consultaTXT += "Propietario no encontrado";
				}
			}
			System.out.println();
			
			// Convertir el documento casa a JSON incluyendo datos completos de las referencias
            resultado_consultaJSON += casas.toJson() + "\n";
		}
		ExportarResultado();
	}
	
	public static void Consulta5() {
		//Método que permite al usuario ejecutar la consulta nº5.
		MongoCursor<Document> propietarios = MongoConnection.propietarios.find().iterator();
		
		Document propietario = null;
		int num_Casas = 0, casas_max = 0;
		
		System.out.println("\n\n==========");
		System.out.println("CONSULTA 5");
		System.out.println("==========\n");
		
		while(propietarios.hasNext()) {
			propietario = propietarios.next();
			
			/*Creamos la referencia (un dbref) para el propietario y la buscamos en la colección de casas 
			  para contar las casas que tiene cada uno y ordenar descendentemente limitando a un 
			  documento la visualización del resultado.*/
			ObjectId _idref = (ObjectId) propietario.getObjectId("_id");
			MongoCursor<Document> casas = MongoConnection.casas.find(exists("propietario")).iterator();
			
			//Empezamos a contar
			ObjectId _idPC = null;
			while(casas.hasNext()) {
				Document casa = casas.next();
				
				//Comparamos el propietario de la casa con el propietario recorrido actual.
				DBRef prop_Casa = (DBRef) casa.get("propietario");
				_idPC = (ObjectId) prop_Casa.getId();
				
				if(_idref.equals(_idPC)) { //Si lo encuentra significa una casa suya.
					num_Casas++;
				}
			}
			if(_idref.equals(_idPC) && num_Casas > casas_max) {
				casas_max = num_Casas;
			}
			propietario.put("NºCasas", casas_max);
			num_Casas = 0;
		}
		
		//Mostramos los datos del propietario con más casas.
		if(propietario != null) {
			System.out.println("- Nombre: " + propietario.getList("nombre", String.class));
			System.out.println("- DNI: " + propietario.getString("DNI"));
			System.out.println("- NºCasas: " + casas_max);
			
			//Exportar a TXT
			resultado_consultaTXT += "- Nombre: " + propietario.getList("nombre", String.class) + "\n";
			resultado_consultaTXT += "- DNI: " + propietario.getString("DNI") + "\n";
			resultado_consultaTXT += "- NºCasas: " + casas_max + "\n";
			
			//Exportar a JSON
			resultado_consultaJSON = propietario.toJson();
		}
		ExportarResultado();
	}
	public static void Consulta6() {
		//Método que permite al usuario ejecutar la consulta nº6.
		System.out.println("\n\n==========");
		System.out.println("CONSULTA 6");
		System.out.println("==========\n");
		
		MongoCursor<Document> agentes = MongoConnection.agentes.find().iterator();
		int num_casas = 0;
		
		//Creamos una lista de docuemntos para ordenar los agentes por su nº de casas como queramos.
		List<Document> Agentes_casas = new ArrayList<>();
		Agentes_casas.sort(new Comparator<Document>() {

			@Override
			public int compare(Document o1, Document o2) {
				return o2.getInteger("NºCasas") - o1.getInteger("NºCasas");
			}
			
		});
		
		while(agentes.hasNext()) {
			Document agente = agentes.next();
			
			//Creamos la referencia de cada agente para compararla con cada casa.
			ObjectId _id = (ObjectId) agente.getObjectId("_id");
			MongoCursor<Document> casas = MongoConnection.casas.find(exists("agente")).iterator();
			
			while(casas.hasNext()) {
				Document casa = casas.next();
				
				//Obtenemos la referencia a cada agente asociado a la casa
				DBRef refAgente = (DBRef) casa.get("agente");
				ObjectId _idAg = (ObjectId) refAgente.getId();
				
				if(_id.equals(_idAg)) {
					num_casas++;
				}
			}
			//Añadimos el nº de casas al documento de cada agente y el agente a la lista.
			agente.put("NºCasas", num_casas);
			num_casas = 0;
			
			Agentes_casas.add(agente);
		}
		
		//Recorremos la lista de agentes al revés para mostrar sus datos con su nº de casas de mayor a menor.
		for(int i = Agentes_casas.size() - 1; i >= 0; i--) {
			Document agente = Agentes_casas.get(i);
			
			System.out.println("- Nombre: " + agente.getList("nombre", String.class));
			System.out.println("- DNI: " + agente.getString("DNI"));
			System.out.println("- NºCasas: " + agente.getInteger("NºCasas") + "\n");
			
			//Exportar resultado a TXT.
			resultado_consultaTXT += "- Nombre: " + agente.getList("nombre", String.class) + "\n";
			resultado_consultaTXT += "- DNI: " + agente.getString("DNI") + "\n";
			resultado_consultaTXT += "- NºCasas: " + agente.getInteger("NºCasas") + "\n\n";
			
			//Exportar resultado a JSON.
			resultado_consultaJSON += agente.toJson() + "\n";
		}
		ExportarResultado();
	}

	public static void Consulta7() {
		//Método que permite al usuario ejecutar la consulta nº7.
		MongoCursor<Document> consulta7 = MongoConnection.pagos.aggregate(Arrays.asList(
				group("$metodo_pago"), sort(ascending("metodo_pago")), limit(3))).iterator();
		
		System.out.println("\n\n==========");
		System.out.println("CONSULTA 7");
		System.out.println("==========\n");
		
		//Mostrar datos.
		while(consulta7.hasNext()) {
			Document metodo_pago = consulta7.next();
			System.out.println("- " + metodo_pago.getString("_id"));
			
			//Exportar a TXT
			resultado_consultaTXT += "- " + metodo_pago.getString("_id") + "\n";
			
			//Exportar a JSON, antes hay que evitar las referencias por errores.
			metodo_pago.append("contrato", 0);
			resultado_consultaJSON += metodo_pago.toJson() + "\n";
		}
		ExportarResultado();
	}

	public static void Consulta8() {
		//Método que permite al usuario ejecutar la consulta nº8.
		MongoCursor<Document> consulta8 = MongoConnection.mantenimientos.aggregate(Arrays.asList(
				group("promedio", avg("promedio", "$Costo")))).iterator();
		
		System.out.println("\n\n==========");
		System.out.println("CONSULTA 8");
		System.out.println("==========\n");
		
		//Mostrar datos
		while(consulta8.hasNext()) {
			Document mantenimiento = consulta8.next();
			System.out.println("Promedio de costo en mantenimientos: " + String.format("%.2f", mantenimiento.getDouble("promedio")) + " €");
			
			//Exportación de datos en TXT
			resultado_consultaTXT = "Promedio de costo en mantenimientos: " + String.format("%.2f", mantenimiento.getDouble("promedio")) + " €";
		}
		ExportarResultado();
	}
	
	public static void Consulta9() {
		//Método que permite al usuario ejecutar la consulta nº9.
		MongoCursor<Document> consulta9 = MongoConnection.mantenimientos.aggregate(Arrays.asList(
				project(new Document("_id", 0)
						.append("Descripcion", 1)
						.append("Costo", 1)
						.append("fecha", 1)
						.append("estado", 1)), 
				sort(ascending("Casa")))).iterator();
		
		System.out.println("\n\n==========");
		System.out.println("CONSULTA 9");
		System.out.println("==========\n");
		
		//Mostrar datos
		while(consulta9.hasNext()) {
			Document mantenimiento = consulta9.next();
			
			System.out.println("Descripción: " + mantenimiento.getString("Descripcion"));
			System.out.println("Costo: " + mantenimiento.getInteger("Costo"));
			System.out.println("Fecha: " + mantenimiento.getString("fecha"));
			System.out.println("Estado: " + mantenimiento.getString("estado") + "\n");
			
			//Exportar a TXT
			resultado_consultaTXT += "Descripción: " + mantenimiento.getString("Descripcion") + "\n";
			resultado_consultaTXT += "Costo: " + mantenimiento.getInteger("Costo") + "\n";
			resultado_consultaTXT += "Fecha: " + mantenimiento.getString("fecha") + "\n";
			resultado_consultaTXT += "Estado: " + mantenimiento.getString("estado") + "\n\n";
			
			//Exportar a JSON
			resultado_consultaJSON += mantenimiento.toJson() + "\n";
		}
		ExportarResultado();
	}
	
	public static void Consulta10() {
		//Método que permite al usuario ejecutar la consulta nº10.
		MongoCursor<Document> consulta10 = MongoConnection.contratos.find(and(exists("tipo"), eq("tipo", "alquiler"))).iterator();
		
		System.out.println("\n\n===========");
		System.out.println("CONSULTA 10");
		System.out.println("===========\n");
		
		//Recorrer los contratos que haya de alquiler y sacar sus referencias a la casa correspondiente.
		while(consulta10.hasNext()) {
			Document contrato = consulta10.next();
			
			//Sacar referencia a la casa correspondiente al contrato.
			DBRef refCasa = (DBRef) contrato.get("Casa");
			ObjectId _id = (ObjectId) refCasa.getId();
			
			//Buscar las casas correspondientes a cada referencia.
			MongoCursor<Document> casa_Consulta = MongoConnection.casas.aggregate(Arrays.asList(
					match(new Document("_id", _id)), 
					sort(ascending("provincia", "ciudad", "direccion")))).iterator();
			
			while(casa_Consulta.hasNext()) {
				Document casa = casa_Consulta.next();
				
				//Mostramos los datos que no son referenciados
				System.out.println("Tipo: " + casa.getString("tipo"));
				System.out.println("Dirección: " + casa.getString("direccion"));
				System.out.println("Ciudad: " + casa.getString("ciudad"));
				System.out.println("Provincia: " + casa.getString("provincia"));
				System.out.println("Código Postal: " + casa.getString("codigo_postal"));
				System.out.println("Tamaño (m^2): " + casa.getInteger("tamano"));
				System.out.println("NºHabitaciones: " + casa.getInteger("habitaciones"));
				System.out.println("Baños: " + casa.getInteger("banos"));
				System.out.println("Precio de venta: " + casa.getInteger("precio"));
				System.out.println("Precio de alquiler: " + casa.getInteger("alquiler"));
				System.out.println("Estado: " + casa.getString("estado"));
				
				//Guardar texto para exportar a TXT
				resultado_consultaTXT += "Tipo: " + casa.getString("tipo") + "\n";
				resultado_consultaTXT += "Dirección: " + casa.getString("direccion") + "\n";
				resultado_consultaTXT += "Ciudad: " + casa.getString("ciudad") + "\n";
				resultado_consultaTXT += "Provincia: " + casa.getString("provincia") + "\n";
				resultado_consultaTXT += "Código Postal: " + casa.getString("codigo_postal") + "\n";
				resultado_consultaTXT += "Tamaño (m^2): " + casa.getInteger("tamano") + "\n";
				resultado_consultaTXT += "NºHabitaciones: " + casa.getInteger("habitaciones") + "\n";
				resultado_consultaTXT += "Baños: " + casa.getInteger("banos") + "\n";
				resultado_consultaTXT += "Precio de venta: " + casa.getInteger("precio") + "\n";
				resultado_consultaTXT += "Precio de alquiler: " + casa.getInteger("alquiler") + "\n";
				resultado_consultaTXT += "Estado: " + casa.getString("estado") + "\n";
				
				//Sacar campos referenciados de la casa
				DBRef refPropietario = (DBRef) casa.get("propietario");
				DBRef refAgente = (DBRef) casa.get("agente");
				DBRef refComprador = (DBRef) casa.get("comprador");
				
				//Exportar a JSON, para ello excluimos los campos referenciados
				casa.append("propietario", 0);
				casa.append("agente", 0);
				casa.append("comprador", 0);
				
				resultado_consultaJSON += casa.toJson() + "\n";
				
				if(refPropietario != null) {
					ObjectId _idP = (ObjectId) refPropietario.getId();
					
					Document propietario = MongoConnection.propietarios.find(new Document("_id", _idP)).first();
					System.out.println("Propietario: " + propietario.getString("DNI") + ", " + propietario.getList("nombre", String.class));
					
					resultado_consultaTXT += "Propietario: " + propietario.getString("DNI") + ", " + propietario.getList("nombre", String.class) + "\n";
					resultado_consultaJSON += propietario.toJson() + "\n";
				}
				if(refAgente != null) {
					ObjectId _idA = (ObjectId) refAgente.getId();
					
					Document agente = MongoConnection.agentes.find(new Document("_id", _idA)).first();
					System.out.println("Agente: " + agente.getString("DNI") + ", " + agente.getList("nombre", String.class));
				
					resultado_consultaTXT += "Agente: " + agente.getString("DNI") + ", " + agente.getList("nombre", String.class) + "\n";
					resultado_consultaJSON += agente.toJson() + "\n";
				}
				if(refComprador != null) {
					ObjectId _idC = (ObjectId) refComprador.getId();
					
					Document comprador = MongoConnection.compradores.find(new Document("_id", _idC)).first();
					System.out.println("Comprador: " + comprador.getString("DNI") + ", " + comprador.getList("nombre", String.class));
				
					resultado_consultaTXT += "Comprador: " + comprador.getString("DNI") + ", " + comprador.getList("nombre", String.class) + "\n";
					resultado_consultaJSON += comprador.toJson() + "\n";
				}
				resultado_consultaTXT += "\n";
			}
			System.out.println();
		}
		ExportarResultado();
	}
	
	public static void Consulta11() {
		//Método que permite al usuario ejecutar la consulta nº11.
		MongoCursor<Document> consulta11 = MongoConnection.contratos.find().iterator();
		
		System.out.println("\n\n===========");
		System.out.println("CONSULTA 11");
		System.out.println("===========\n");
		
		/*Recorremos los contratos para sacar la casa a la que están asociados y luego mostramos el 
		  ingreso total de la casa recorriendo el documento de la colección de pagos correspondiente a 
		  cada contrato.*/
		while(consulta11.hasNext()) {
			Document contrato = consulta11.next();
			ObjectId _id = contrato.getObjectId("_id");
			
			//Recorremos los documentos de la colección de pagos que estén asociadas con un contrato.
			MongoCursor<Document> pagos = MongoConnection.pagos.aggregate(Arrays.asList(
					match(exists("Contrato")), 
					sort(ascending("cantidad")))).iterator();
			
			while(pagos.hasNext()) {
				Document pago = pagos.next();
				
				//Sacamos la referencia al contrato con el que está asociado el pago
				DBRef refContrato = (DBRef) pago.get("Contrato");
				ObjectId _idCP = (ObjectId) refContrato.getId();
				if(refContrato != null) {
					if(_id.equals(_idCP)) {
						//Si se ha encontrado el contrato asociado a un pago sacamos la casa asociada al contrato.
						DBRef refCasa = (DBRef) contrato.get("Casa");
						ObjectId _idCasa = (ObjectId) refCasa.getId();
						if(refCasa != null) {
							Document casa = MongoConnection.casas.find(new Document("_id", _idCasa)).first();
							
							//Excluir los campos referenciados para evitar errores.
							casa.append("propietario", 0);
							casa.append("agente", 0);
							casa.append("comprador", 0);
							
							pago.append("Contrato", 0);
							
							
							System.out.println("Casa: " + casa.toJson());
							System.out.println("Total (€): " + pago.getInteger("cantidad") + "\n");
							
							//Exportar a TXT
							resultado_consultaTXT += "Casa: " + casa.toJson() + "\n";
							resultado_consultaTXT += "Total (€): " + pago.getInteger("cantidad") + "\n\n";
							
							//Exportar a JSON
							resultado_consultaJSON += casa.toJson() + "\n";
							resultado_consultaJSON += pago.toJson() + "\n\n";
						}
					}
				}
			}
		}
		ExportarResultado();
	}
	
	public static void Consulta12() {
		//Método que permite al usuario ejecutar la consulta nº12.
		MongoCursor<Document> agentes = MongoConnection.agentes.find().iterator();
		int num_Contratos = 0;
		
		System.out.println("\n\n===========");
		System.out.println("CONSULTA 12");
		System.out.println("===========\n");
		
		//Creamos una lista para almacenar a los agentes con su nº de contratos y así sacar ordenado a los 3 agentes con mayor nº de contratos activos.
		List<Document> agentes_CA = new ArrayList<>();
		agentes_CA.sort(new Comparator<Document>() {

			@Override
			public int compare(Document o1, Document o2) {
				return o2.getInteger("contratos_activos") - o1.getInteger("contratos_activos");
			}
			
		});
		
		//Recorremos los agentes para ver si tienen contratos y así contar los contratos.
		while(agentes.hasNext()) {
			Document agente = agentes.next();
			
			//Sacamos el id para crear una referencia de cada agente para compararlas con la de cada contrato.
			ObjectId _idA = (ObjectId) agente.getObjectId("_id");
			
			
			//Recorremos los contratos asociados a un agente que estén activos.
			MongoCursor<Document> contratos = MongoConnection.contratos.aggregate(Arrays.asList(
					match(eq("estado", "activo")))).iterator();
			
			while(contratos.hasNext()) {
				Document contrato = contratos.next();
				
				//Obtenemos la referencia de cada agente asociado a cada contrato.
				DBRef refAgente = (DBRef) contrato.get("Agente");
				if(refAgente != null) {
					ObjectId _idCA = (ObjectId) refAgente.getId();
					if(_idA.equals(_idCA)) {
						num_Contratos++;
					}
				}
			}
			//Actualizamos el documento de cada agente con su nº de contratos activos.
			agente.put("contratos_activos", num_Contratos);
			num_Contratos = 0;
			
			agentes_CA.add(agente);
		}
		
		if(agentes_CA.size() >= 3) {//Si hay 3 agentes o más en la lista mostramos los datos.
			//Mostramos los datos ordenados de mayor a menor nº de contratos.
			for(int i = agentes_CA.size() - 1; i >= agentes_CA.size() - 3; i--) {
				System.out.println("Nombre: " + agentes_CA.get(i).getList("nombre", String.class));
				System.out.println("DNI: " + agentes_CA.get(i).getString("DNI"));
				System.out.println("NºContratos activos: " + agentes_CA.get(i).getInteger("contratos_activos") + "\n");
				
				//Exportar a TXT.
				resultado_consultaTXT += "Nombre: " + agentes_CA.get(i).getList("nombre", String.class) + "\n";
				resultado_consultaTXT += "DNI: " + agentes_CA.get(i).getString("DNI") + "\n";
				resultado_consultaTXT += "NºContratos activos: " + agentes_CA.get(i).getInteger("contratos_activos") + "\n\n";
				
				//Exportar a JSON.
				resultado_consultaJSON += agentes_CA.get(i).toJson() + "\n";
			}
		} else {
			System.out.println("Hay menos de 3 agentes, no se puede realizar la consulta.");
			resultado_consultaTXT = "Hay menos de 3 agentes, no se puede realizar la consulta.";
		}
		ExportarResultado();
	}
	
	public static void Consulta13() {
		//Método que permite al usuario ejecutar la consulta nº13.
		MongoCursor<Document> contratos = MongoConnection.contratos.find(or(eq("tipo", "alquiler"), eq("tipo", "Alquiler"))).iterator();
		
		System.out.println("\n\n===========");
		System.out.println("CONSULTA 13");
		System.out.println("===========\n");
		
		//Recorremos los contratos que sean de alquiler.
		while(contratos.hasNext()) {
			Document contrato = contratos.next();
			
			//Multiplicamos el campo de cantidad al mes por 12 meses calcular sus ingresos anuales.
			int anuales = contrato.getInteger("cantidad_mes") * 12;
			
			//Mostramos los datos y en el documento del contrato excluimos las referencias para evitar errores.
			contrato.append("Propietario", 0);
			contrato.append("Agente", 0);
			contrato.append("Comprador", 0);
			contrato.append("Casa", 0);
			
			System.out.println("Contrato: " + contrato.toJson());
			System.out.println("Ingresos anuales: " + anuales + " €\n");
			
			//Exportar a TXT
			resultado_consultaTXT += "Contrato: " + contrato.toJson() + "\n";
			resultado_consultaTXT += "Ingresos anuales: " + anuales + " €\n\n";
			
			//Exportar a JSON, añadimos los ingresos anuales al documento de cada contrato.
			contrato.put("anuales", anuales);
			resultado_consultaJSON += contrato.toJson() + "\n";
		}
		ExportarResultado();
	}
	
	public static void Consulta14() {
		//Método que permite al usuario ejecutar la consulta nº14.
		MongoCursor<Document> consulta14 = MongoConnection.contratos.find(or(eq("tipo", "compraventa"), eq("tipo", "Compraventa"))).iterator();
		
		System.out.println("\n\n===========");
		System.out.println("CONSULTA 14");
		System.out.println("===========\n");
		
		//Recorremos los contratos de compraventa.
		/*La variable "dias" es para ir acumulando cada resultado de cálculo de los días que transcurren 
		  entre una fecha de inicio y otra de fin.
		  
		  La variable "contratos" es para ir sumando 1 cada vez que encuentra un contrato para hacer la 
		  media en días de cierre de contratos de compraventa.
		*/
		long dias = 0;
		int contratos = 0;
		
		while(consulta14.hasNext()) {
			Document contrato = consulta14.next();
			
			//Recuperamos la fecha de inicio y de fin para calcular los días que dura cada contrato y así hacer la media.
			String fechaI_str = contrato.getString("fecha_inicio");
			String fechaF_str = contrato.getString("fecha_fin");
			
			//Construimos objetos LocalDate con las fechas de cada contrato y sacamos cada dato para pasarlo a entero.
			//Fecha de inicio
			String diaI_str = fechaI_str.substring(0, 2);
			String mesI_str = fechaI_str.substring(3, 5);
			String añoI_str = fechaI_str.substring(6, 10);
			
			int diaI = Integer.parseInt(diaI_str);
			int mesI = Integer.parseInt(mesI_str);
			int añoI = Integer.parseInt(añoI_str);
			
			LocalDate fechaI = LocalDate.of(añoI, mesI, diaI);
			
			//Fecha de finalización
			String diaF_str = fechaF_str.substring(0, 2);
			String mesF_str = fechaF_str.substring(3, 5);
			String añoF_str = fechaF_str.substring(6, 10);
			
			int diaF = Integer.parseInt(diaF_str);
			int mesF = Integer.parseInt(mesF_str);
			int añoF = Integer.parseInt(añoF_str);
			
			LocalDate fechaF = LocalDate.of(añoF, mesF, diaF);
			
			//Ya podemos calcular los días que transcurren entre cada fecha de inicio y de finalización.
			dias += ChronoUnit.DAYS.between(fechaI, fechaF);
			contratos++;
		}
		
		//Ya podemos calcular la media que queremos.
		long media = dias/contratos;
		System.out.println("Días de media de cierre de contratos de compraventa: " + media);
		
		//Exportar a TXT.
		resultado_consultaTXT = "Días de media de cierre de contratos de compraventa: " + media;
		ExportarResultado();
	}
}
