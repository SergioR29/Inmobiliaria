import com.mongodb.DBRef;
import com.mongodb.client.*;
import static com.mongodb.client.model.Aggregates.*;

import org.bson.*;
import org.bson.types.*;

import static com.mongodb.client.model.Filters.*;

import java.io.*;
import java.util.*;


public class ConsultasPatrones {
	static Scanner sc = new Scanner(System.in);
	
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
		//Método que permitirá al usuario ejecutar la consulta nº1
		System.out.println("\n\n==========");
		System.out.println("CONSULTA 1");
		System.out.println("==========\n");
		
		int num_Baños = 0;
		while(true) {
			try {
				System.out.print("Nº máximo de baños: ");
				num_Baños = sc.nextInt();
			} catch (InputMismatchException e) {
				sc.nextLine();
				System.out.println("--------------------");
				System.out.println("\tNº inválido.\n");
				continue;
			}
			
			if(num_Baños < 0) {
				System.out.println("--------------------");
				System.out.println("\tNº inválido.\n");
				continue;
			}
			System.out.println("\n\n");
			break;
		}
		
		//Hora de realizar la consulta, ya hemos obtenido el parámetro introducido por el usuario.
		MongoCursor<Document> consulta1 = MongoConnection.casas.find(lte("banos", num_Baños)).iterator();
		while(consulta1.hasNext()) {
			Document casa = consulta1.next();
			
			//Mostrar los datos y exportar a TXT.
			System.out.println("Dirección: " + casa.getString("direccion"));
			System.out.println("Baños: " + casa.getInteger("banos"));
			
			resultado_consultaTXT += "Dirección: " + casa.getString("direccion") + "\n";
			resultado_consultaTXT += "Baños: " + casa.getInteger("banos") + "\n";
			
			
			//Para sacar el nombre del propietario sacamos el propietario referenciado a la casa.
			DBRef refPropietario = (DBRef) casa.get("propietario");
			ObjectId _Id = null;
			
			if(refPropietario != null) {
				_Id = (ObjectId) refPropietario.getId();
				
				//Obtenemos el documento asociado al propietario referenciado
				Document propietario = MongoConnection.propietarios.find(new Document("_id", _Id)).first();
				if(propietario != null) {
					System.out.println("Propietario: " + propietario.getList("nombre", String.class) + "\n");
					resultado_consultaTXT += "Propietario: " + propietario.getList("nombre", String.class) + "\n\n";
					
					//Exportar propietario a JSON.
					resultado_consultaJSON += propietario.toJson() + "\n\n";
				} else {
					System.out.println("No existe el propietario de esta casa.\n");
					resultado_consultaTXT += "No existe el propietario de esta casa.\n\n";
				}
			} else {
				System.out.println("No existe el propietario de esta casa.\n");
				resultado_consultaTXT += "No existe el propietario de esta casa.\n\n";
			}
			
			//Exportar a JSON. Excluimos los campos referenciados que haya para evitar errores.
			casa.append("propietario", 0);
			casa.append("agente", 0);
			casa.append("comprador", 0);
			resultado_consultaJSON += casa.toJson() + "\n";
		}
		ExportarResultado();
	}
	
	public static void Consulta2() {
		//Método que permitirá al usuario ejecutar la consulta nº2
		System.out.println("\n\n==========");
		System.out.println("CONSULTA 2");
		System.out.println("==========\n");
		
		int num_Hab = 0;
		while(true) {
			try {
				System.out.print("Nº mínimo de habitaciones: ");
				num_Hab = sc.nextInt();
			} catch (InputMismatchException e) {
				sc.nextLine();
				System.out.println("--------------------");
				System.out.println("\tNº inválido.\n");
				continue;
			}
			
			if(num_Hab < 0) {
				System.out.println("--------------------");
				System.out.println("\tNº inválido.\n");
				continue;
			}
			System.out.println("\n\n");
			break;
		}
		
		//Hora de realizar la consulta, ya hemos obtenido el parámetro introducido por el usuario.
		MongoCursor<Document> consulta2 = MongoConnection.casas.find(gte("habitaciones", num_Hab)).iterator();
		while(consulta2.hasNext()) {
			Document casa = consulta2.next();
			
			//Mostrar los datos y exportar a TXT.
			System.out.println("Dirección: " + casa.getString("direccion"));
			System.out.println("Habitaciones: " + casa.getInteger("habitaciones"));
			
			resultado_consultaTXT += "Dirección: " + casa.getString("direccion") + "\n";
			resultado_consultaTXT += "Habitaciones: " + casa.getInteger("habitaciones") + "\n";
			
			
			//Para sacar el nombre del propietario sacamos el propietario referenciado a la casa.
			DBRef refPropietario = (DBRef) casa.get("propietario");
			ObjectId _Id = null;
			
			if(refPropietario != null) {
				_Id = (ObjectId) refPropietario.getId();
				
				//Obtenemos el documento asociado al propietario referenciado
				Document propietario = MongoConnection.propietarios.find(new Document("_id", _Id)).first();
				if(propietario != null) {
					System.out.println("Propietario: " + propietario.getList("nombre", String.class) + "\n");
					resultado_consultaTXT += "Propietario: " + propietario.getList("nombre", String.class) + "\n\n";
					
					//Exportar propietario a JSON.
					resultado_consultaJSON += propietario.toJson() + "\n\n";
				} else {
					System.out.println("No existe el propietario de esta casa.\n");
					resultado_consultaTXT += "No existe el propietario de esta casa.\n\n";
				}
			} else {
				System.out.println("No existe el propietario de esta casa.\n");
				resultado_consultaTXT += "No existe el propietario de esta casa.\n\n";
			}
			
			//Exportar a JSON. Excluimos los campos referenciados que haya para evitar errores.
			casa.append("propietario", 0);
			casa.append("agente", 0);
			casa.append("comprador", 0);
			resultado_consultaJSON += casa.toJson() + "\n";
		}
		ExportarResultado();
	}
	
	public static void Consulta3() {
		//Método que permitirá al usuario ejecutar la consulta nº3
		System.out.println("\n\n==========");
		System.out.println("CONSULTA 3");
		System.out.println("==========\n");
		
		int presu_min = 0;
		while(true) {
			try {
				System.out.print("Presupuesto mínimo: ");
				presu_min = sc.nextInt();
			} catch (InputMismatchException e) {
				sc.nextLine();
				System.out.println("--------------------");
				System.out.println("\tPresupuesto inválido.\n");
				continue;
			}
			
			if(presu_min < 0) {
				System.out.println("--------------------");
				System.out.println("\tPresupuesto inválido.\n");
				continue;
			}
			System.out.println("\n\n");
			break;
		}
		
		//Hora de realizar la consulta, ya tenemos el parámetro correspondiente.
		MongoCursor<Document> consulta3 = MongoConnection.compradores.find(gte("presupuesto", presu_min)).iterator();
		while(consulta3.hasNext()) {
			Document comprador = consulta3.next();
			
			//Mostrar datos
			System.out.println("Nombre: " + comprador.getList("nombre", String.class));
			System.out.println("DNI: " + comprador.getString("DNI"));
			System.out.println("NºTeléfono: " + comprador.getString("telefono"));
			System.out.println("Email: " + comprador.getString("email"));
			System.out.println("Presupuesto: " + comprador.getInteger("presupuesto"));
			System.out.println("Preferencias: " + comprador.getString("preferencias") + "\n");
			
			//Exportar a TXT
			resultado_consultaTXT += "Nombre: " + comprador.getList("nombre", String.class) + "\n";
			resultado_consultaTXT += "DNI: " + comprador.getString("DNI") + "\n";
			resultado_consultaTXT += "NºTeléfono: " + comprador.getString("telefono") + "\n";
			resultado_consultaTXT += "Email: " + comprador.getString("email") + "\n";
			resultado_consultaTXT += "Presupuesto: " + comprador.getInteger("presupuesto") + "\n";
			resultado_consultaTXT += "Preferencias: " + comprador.getString("preferencias") + "\n\n";
			
			//Exportar a JSON.
			resultado_consultaJSON += comprador.toJson() + "\n";
		}
		ExportarResultado();
	}
	
	public static void Consulta4() {
		//Método que permitirá al usuario ejecutar la consulta nº4.
		System.out.println("\n\n==========");
		System.out.println("CONSULTA 4");
		System.out.println("==========\n");
		
		//Nombre completo (nombre + apellido1 + apellido2)
		String[] nombreCompleto = new String[3];
		
		String nombre = "";
		System.out.print("Nombre (-1 para ignorar): ");
		
		while(nombre.length() == 0) {
			nombre = sc.nextLine();
							
			if(nombre.isBlank() && nombre.length() > 0) {
				nombre = "";
				System.out.println("----------------------------------------------------------------------------------");
				System.out.println("\tNo se ha introducido el nombre del propietario.\n");
				
				System.out.print("Nombre (-1 para ignorar): ");
			}
		}
		
		String apellido1 = "";
		System.out.print("1ºApellido (-1 para ignorar): ");
		
		while(apellido1.length() == 0) {
			apellido1 = sc.nextLine();
							
			if(apellido1.isBlank() && apellido1.length() > 0) {
				apellido1 = "";
				System.out.println("----------------------------------------------------------------------------------");
				System.out.println("\tNo se ha introducido el 1ºapellido del propietario.\n");
				
				System.out.print("1ºApellido (-1 para ignorar): ");
			}
		}
		String apellido2 = "";
		System.out.print("2ºApellido (-1 para ignorar): ");
		
		while(apellido2.length() == 0) {
			apellido2 = sc.nextLine();
							
			if(apellido2.isBlank() && apellido2.length() > 0) {
				apellido2 = "";
				System.out.println("----------------------------------------------------------------------------------");
				System.out.println("\tNo se ha introducido el 2ºapellido del propietario.\n");

				System.out.print("2ºApellido (-1 para ignorar): ");
			}
		}
		//Juntamos los datos. Y nos aseguramos de que no haya posiciones vacías en el array.
		int posVacia = 0;
		
		if(!nombre.equals("-1")) nombreCompleto[0] = nombre;
		else {
			posVacia++;
			nombreCompleto = Arrays.copyOf(nombreCompleto, nombreCompleto.length - 1);
		}
		
		if(!apellido1.equals("-1")) nombreCompleto[1 - posVacia] = apellido1;
		else {
			posVacia++;
			nombreCompleto = Arrays.copyOf(nombreCompleto, nombreCompleto.length - 1);
		}
		
		if(!apellido2.equals("-1")) nombreCompleto[2 - posVacia] = apellido2;
		else {
			posVacia++;
			nombreCompleto = Arrays.copyOf(nombreCompleto, nombreCompleto.length - 1);
		}
		List<String> nombre_teclado = new ArrayList<String>();
		nombre_teclado.addAll(Arrays.asList(nombreCompleto));
		
		
		//Tenemos los datos suficientes para hacer la consulta.
		MongoCursor<Document> prop_cons = MongoConnection.propietarios.find().iterator();
		
		int num_Casas = 0;
		while(prop_cons.hasNext()) {
			Document propietario = prop_cons.next();
			List<String> nombre_prop = propietario.getList("nombre", String.class);
			
			if(nombre_prop.equals(nombre_teclado)) {
				/*Hacemos un recuento de la cantidad de casas asociadas al propietario.
				  Para ello, creamos una referencia del propietario obtenido ahora para 
				  compararla con la referencia del propietario de cada casa.
				*/
				ObjectId _id = (ObjectId) propietario.getObjectId("_id");
				DBRef refPropietario = new DBRef("Propietarios", _id);
				
				
				//Hacemos la consulta de casas asociadas a un propietario.
				MongoCursor<Document> casas = MongoConnection.casas.find(exists("propietario")).iterator();
				while(casas.hasNext()) {
					Document casa = casas.next();
					
					//Recuperamos la referencia de cada casa al propietario.
					DBRef refP = (DBRef) casa.get("propietario");
					if(refPropietario.equals(refP)) {
						num_Casas++;
					}
				}
				
				//Exportar el propietario a JSON.
				resultado_consultaJSON = propietario.toJson();
				break;
			}
		}
		System.out.println("\nEl propietario " + nombre_teclado + " tiene " + num_Casas + " casas.");
		
		//Exportar el resultado a TXT.
		resultado_consultaTXT = "El propietario " + nombre_teclado + " tiene " + num_Casas + " casas.";
		
		
		ExportarResultado();
	}
	
	public static void Consulta5() {
		//Método que permitirá al usuario ejecutar la consulta nº5.
		System.out.println("\n\n==========");
		System.out.println("CONSULTA 5");
		System.out.println("==========\n");
		
		int año = 0;
		while(true) {
			try {
				System.out.print("Año de contratos firmados a ver: ");
				año = sc.nextInt();
			} catch(InputMismatchException e) {
				sc.nextLine();
				System.out.println("--------------------");
				System.out.println("\tAño inválido.\n");
				continue;
			}
			
			if(año < 0) {
				System.out.println("--------------------");
				System.out.println("\tAño inválido.\n");
				continue;
			}
			break;
		}
		System.out.println("=====================================\n");
		
		//Hacemos la consulta y sacamos el año de cada fecha de inicio correspondiente al año.
		MongoCursor<Document> consulta5 = MongoConnection.contratos.find().iterator();
		while(consulta5.hasNext()) {
			Document contrato = consulta5.next();
			
			/*Sacamos el trozo de cadena del año de la fecha de inicio de cada contrato para pasarlo a 
			entero y comprobamos que sea el año escrito por teclado.*/
			String añoI_str = contrato.getString("fecha_inicio").substring(6, 10);
			int añoI = Integer.parseInt(añoI_str);
			
			if(año == añoI) {
				//Sacamos la referencias si se ha encontrado un contrato con el año especificado.
				//Propietario
				DBRef refPropietario = (DBRef) contrato.get("Propietario");
				Document propietario = null;
				
				if(refPropietario != null) {
					ObjectId _idP = (ObjectId) refPropietario.getId();
					propietario = MongoConnection.propietarios.find(new Document("_id", _idP)).first();
				}
				
				//Agente
				DBRef refAgente = (DBRef) contrato.get("Agente");
				Document agente = null;
				
				if(refAgente != null) {
					ObjectId _idA = (ObjectId) refAgente.getId();
					agente = MongoConnection.agentes.find(new Document("_id", _idA)).first();
				}
				
				//Comprador
				DBRef refComprador = (DBRef) contrato.get("Comprador");
				Document comprador = null;
				
				if(refComprador != null) {
					ObjectId _idCOM = (ObjectId) refComprador.getId();
					comprador = MongoConnection.compradores.find(new Document("_id", _idCOM)).first();
				}
				
				//Casa
				DBRef refCasa = (DBRef) contrato.get("Casa");
				Document casa = null;
				
				if(refCasa != null) {
					ObjectId _idCasa = (ObjectId) refCasa.getId();
					casa = MongoConnection.casas.find(new Document("_id", _idCasa)).first();
				}
				
				
				//Ya podemos listar los datos y exportar a TXT.
				System.out.println("Tipo: " + contrato.getString("tipo"));
				System.out.println("Fecha de Inicio: " + contrato.getString("fecha_inicio"));
				System.out.println("Fecha de Fin: " + contrato.getString("fecha_fin"));
				System.out.println("Método de Pago: " + contrato.getString("metodo_pago"));
				
				resultado_consultaTXT += "Tipo: " + contrato.getString("tipo") + "\n";
				resultado_consultaTXT += "Fecha de Inicio: " + contrato.getString("fecha_inicio") + "\n";
				resultado_consultaTXT += "Fecha de Fin: " + contrato.getString("fecha_fin") + "\n";
				resultado_consultaTXT += "Método de Pago: " + contrato.getString("metodo_pago") + "\n";
				
				
				if(contrato.getString("tipo").equalsIgnoreCase("alquiler")) {
					System.out.println("Cantidad a pagar (al mes): " + contrato.getInteger("cantidad_mes"));
					resultado_consultaTXT += "Cantidad a pagar (al mes): " + contrato.getInteger("cantidad_mes") + "\n";
					
				} else if(contrato.getString("tipo").equalsIgnoreCase("compraventa")) {
					System.out.println("Cantidad a pagar (Total): " + contrato.getInteger("cantidad_total"));
					resultado_consultaTXT += "Cantidad a pagar (Total): " + contrato.getInteger("cantidad_total") + "\n";
				}
				System.out.println("Estado: " + contrato.getString("estado"));
				resultado_consultaTXT += "Estado: " + contrato.getString("estado") + "\n";
				
				//Exportamos a JSON y para eso excluimos los campos referenciados.
				contrato.append("Propietario", 0);
				contrato.append("Agente", 0);
				contrato.append("Comprador", 0);
				contrato.append("Casa", 0);
				
				resultado_consultaJSON += contrato.toJson() + "\n";
				
				if(propietario != null) {
					System.out.println("Propietario: " + propietario.getString("DNI") + ", " + propietario.getList("nombre", String.class));
					resultado_consultaTXT += "Propietario: " + propietario.getString("DNI") + ", " + propietario.getList("nombre", String.class) + "\n";
					resultado_consultaJSON += propietario.toJson() + "\n";
				}
				if(agente != null) {
					System.out.println("Agente: " + agente.getString("DNI") + ", " + agente.getList("nombre", String.class));
					resultado_consultaTXT += "Agente: " + agente.getString("DNI") + ", " + agente.getList("nombre", String.class) + "\n";
					resultado_consultaJSON += agente.toJson() + "\n";
				}
				if(comprador != null) {
					System.out.println("Comprador: " + comprador.getString("DNI") + ", " + comprador.getList("nombre", String.class));
					resultado_consultaTXT += "Comprador: " + comprador.getString("DNI") + ", " + comprador.getList("nombre", String.class) + "\n";
					resultado_consultaJSON += comprador.toJson() + "\n";
				}
				if(casa != null) {
					//Excluimos referencias para evitar errores.
					casa.append("propietario", 0);
					casa.append("agente", 0);
					casa.append("comprador", 0);
					
					System.out.println("Casa: " + casa.toJson());
					resultado_consultaTXT += "Casa: " + casa.toJson() + "\n";
					resultado_consultaJSON += casa.toJson() + "\n\n";
				}
				System.out.println();
				resultado_consultaTXT += "\n";
				resultado_consultaJSON += "\n";
			}
		}
		ExportarResultado();
	}
	
	public static void Consulta6() {
		//Método que permitirá al usuario ejecutar la consulta nº6.
		System.out.println("\n\n==========");
		System.out.println("CONSULTA 6");
		System.out.println("==========\n");
		
		//Nombre completo (nombre + apellido1 + apellido2)
		String[] nombreCompleto = new String[3];
						
		String nombre = "";
		System.out.print("Nombre: ");
						
		while(nombre.length() == 0) {
			nombre = sc.nextLine();
											
			if(nombre.isBlank() && nombre.length() > 0) {
				nombre = "";
				System.out.println("----------------------------------------------------------------------------------");
				System.out.println("\tNo se ha introducido el nombre del agente inmobiliario.\n");
								
				System.out.print("Nombre: ");
			}
		}
						
		String apellido1 = "";
		System.out.print("1ºApellido: ");
						
		while(apellido1.length() == 0) {
			apellido1 = sc.nextLine();
											
			if(apellido1.isBlank() && apellido1.length() > 0) {
				apellido1 = "";
				System.out.println("----------------------------------------------------------------------------------");
				System.out.println("\tNo se ha introducido el 1ºapellido del agente inmobiliario.\n");
								
				System.out.print("1ºApellido: ");
			}
		}
		String apellido2 = "";
		System.out.print("2ºApellido: ");
						
		while(apellido2.length() == 0) {
			apellido2 = sc.nextLine();
											
			if(apellido2.isBlank() && apellido2.length() > 0) {
				apellido2 = "";
				System.out.println("----------------------------------------------------------------------------------");
				System.out.println("\tNo se ha introducido el 2ºapellido del agente inmobiliario.\n");

				System.out.print("2ºApellido: ");
			}
		}
		//Juntamos los datos. Y nos aseguramos de que no haya posiciones vacías en el array.
		int posVacia = 0;
						
		if(!nombre.equals("-1")) nombreCompleto[0] = nombre;
		else {
			posVacia++;
			nombreCompleto = Arrays.copyOf(nombreCompleto, nombreCompleto.length - 1);
		}
						
		if(!apellido1.equals("-1")) nombreCompleto[1 - posVacia] = apellido1;
		else {
			posVacia++;
			nombreCompleto = Arrays.copyOf(nombreCompleto, nombreCompleto.length - 1);
		}
						
		if(!apellido2.equals("-1")) nombreCompleto[2 - posVacia] = apellido2;
		else {
			posVacia++;
			nombreCompleto = Arrays.copyOf(nombreCompleto, nombreCompleto.length - 1);
		}
		System.out.println();
		
		//Recorremos todos los agentes para luego compararlos con los agentes referenciados a cada visita.
		MongoCursor<Document> agentes = MongoConnection.agentes.find().iterator();
		while(agentes.hasNext()) {
			Document agente = agentes.next();
			List<String> nombre_Agente = agente.getList("nombre", String.class);
			
			if(nombre_Agente.equals(Arrays.asList(nombreCompleto))) {
				//Si el agente es encontrado sacamos su id como campo.
				ObjectId _idA = (ObjectId) agente.getObjectId("_id"); 
				
				//Recorremos todas las vistas para encontrar la que está asociada al agente.
				MongoCursor<Document> visitas = MongoConnection.visitas.find(exists("Agente")).iterator();
				int num_Visitas = 0;
				
				while(visitas.hasNext()) {
					Document visita = visitas.next();
					
					//Obtenemos la referencia al agente.
					DBRef refAgente_visita = (DBRef) visita.get("Agente");
					ObjectId _idA_visita = (ObjectId) refAgente_visita.getId();
					
					//Si la visita asociada con el agente se ha encontrado, sumamos 1 al acumulado.
					if(_idA.equals(_idA_visita)) {
						num_Visitas++;
						
						//Exportamos a JSON las visitas.
						visita.append("Comprador", 0);
						visita.append("Casa", 0);
						visita.append("Agente", 0);
						
						resultado_consultaJSON += visita.toJson() + "\n";
					}
				}
				
				//Mostramos los datos y exportamos a TXT.
				System.out.println("NºVisitas totales del agente " + nombre_Agente + ": " + num_Visitas);
				resultado_consultaTXT += "NºVisitas totales del agente " + nombre_Agente + ": " + num_Visitas + "\n";
			}
		}
		ExportarResultado();
	}
	
	public static void Consulta7() {
		//Método que permitirá al usuario ejecutar la consulta nº7.
		System.out.println("\n\n==========");
		System.out.println("CONSULTA 7");
		System.out.println("==========\n");
		
		//Tipo de contrato (compraventa o alquiler)
		String tipo = "";
		System.out.print("Tipo: ");
		
		while(tipo.length() == 0) {
			tipo = sc.nextLine();
			
			if(tipo.isBlank() && tipo.length() > 0) {
				tipo = "";
				System.out.println("----------------------------------------------------------------------------------");
				System.out.println("\tNo se ha introducido el tipo del contrato.\n");

				System.out.print("Tipo: ");
				
			} else if(!tipo.isBlank()) {
				if(!tipo.equals("-1")) {
					if(!tipo.equalsIgnoreCase("compraventa") && !tipo.equalsIgnoreCase("alquiler")) {
						tipo = "";
						System.out.println("----------------------------------------------------------------------------------");
						System.out.println("\tTipo de contrato inválido.\n");
						
						System.out.print("Tipo: ");
					} else {
						break;
					}
				} else {
					break;
				}
			}
		}
		
		//Al tener el tipo de contrato ya podemos realizar la consulta.
		MongoCursor<Document> consulta7 = MongoConnection.contratos.aggregate(
				Arrays.asList(match(eq("tipo", tipo)), 
						count("contratos"))).iterator();
		
		int num_Contratos = 0;
		while(consulta7.hasNext()) {
			Document contrato = consulta7.next();
			num_Contratos = contrato.getInteger("contratos");
		}
		System.out.println("Contratos de " + tipo + ": " + num_Contratos);
		
		//Exportar a TXT.
		resultado_consultaTXT = "Contratos de " + tipo + ": " + num_Contratos;
		ExportarResultado();
	}

	public static void Consulta8() {
		//Método que permitirá al usuario ejecutar la consulta nº8.
		System.out.println("\n\n==========");
		System.out.println("CONSULTA 8");
		System.out.println("==========\n");
		
		//Pedir parámetro estado por teclado.
		String estado = "";
		System.out.print("Estado de la reparación (pendiente -> [por defecto], en proceso, finalizado): ");
		
		while(estado.length() == 0) {
			estado = sc.nextLine();
									
			if(estado.isBlank() && estado.length() > 0) {
				estado = "";
				System.out.println("----------------------------------------------------------------------------------");
				System.out.println("\tNo se ha introducido el estado de la reparación.\n");
						
				System.out.print("Estado de la reparación (pendiente -> [por defecto], en proceso, finalizado): ");
			} else if(estado.length() > 0) {
				if(!estado.equalsIgnoreCase("pendiente") && !estado.equalsIgnoreCase("en proceso") 
				   && !estado.equalsIgnoreCase("finalizado") && !estado.equals("-1")) {
					
					estado = "pendiente";
				}
			}
		}
		System.out.println("\n\n");
		
		//Con el estado introducido por teclado ya podemos listar todas las reparaciones en ese estado.
		MongoCursor<Document> consulta8 = MongoConnection.mantenimientos.find(eq("estado", estado)).iterator();
		while(consulta8.hasNext()) {
			Document mantenimiento = consulta8.next();
			
			//Sacamos la casa referenciada al mantenimiento correspondiente y mostramos los demás datos.
			DBRef refCasa = (DBRef) mantenimiento.get("Casa");
			ObjectId _idCasa = null;
			Document casa = null;
			
			System.out.println("Descripción: " + mantenimiento.getString("Descripcion"));
			System.out.println("Costo: " + mantenimiento.getInteger("Costo"));
			System.out.println("Fecha: " + mantenimiento.getString("fecha"));
			System.out.println("Estado: " + mantenimiento.getString("estado"));
			System.out.println("\nDATOS DE LA CASA");
			System.out.println("================\n");
			
			//Exportar a TXT.
			resultado_consultaTXT += "Descripción: " + mantenimiento.getString("Descripcion") + "\n";
			resultado_consultaTXT += "Costo: " + mantenimiento.getInteger("Costo") + "\n";
			resultado_consultaTXT += "Fecha: " + mantenimiento.getString("fecha") + "\n";
			resultado_consultaTXT += "Estado: " + mantenimiento.getString("estado") + "\n";
			resultado_consultaTXT += "\nDATOS DE LA CASA\n";
			resultado_consultaTXT += "================\n\n";
			
			if(refCasa != null) {
				_idCasa = (ObjectId) refCasa.getId();
				casa = (Document) MongoConnection.casas.find(new Document("_id", _idCasa)).first();
				
				if(casa != null) {
					System.out.println("\t- Tipo: " + casa.getString("tipo"));
					System.out.println("\t- Dirección: " + casa.getString("direccion"));
					System.out.println("\t- Ciudad: " + casa.getString("ciudad"));
					System.out.println("\t- Provincia: " + casa.getString("provincia"));
					System.out.println("\t- Código Postal: " + casa.getString("codigo_postal"));
					System.out.println("\t- Tamaño (m^2): " + casa.getInteger("tamano"));
					System.out.println("\t- NºHabitaciones: " + casa.getInteger("habitaciones"));
					System.out.println("\t- Baños: " + casa.getInteger("banos"));
					System.out.println("\t- Precio de venta: " + casa.getInteger("precio"));
					System.out.println("\t- Precio de alquiler: " + casa.getInteger("alquiler"));
					System.out.println("\t- Estado: " + casa.getString("estado"));
					
					//Guardar texto para exportar a TXT
					resultado_consultaTXT += "\t- Tipo: " + casa.getString("tipo") + "\n";
					resultado_consultaTXT += "\t- Dirección: " + casa.getString("direccion") + "\n";
					resultado_consultaTXT += "\t- Ciudad: " + casa.getString("ciudad") + "\n";
					resultado_consultaTXT += "\t- Provincia: " + casa.getString("provincia") + "\n";
					resultado_consultaTXT += "\t- Código Postal: " + casa.getString("codigo_postal") + "\n";
					resultado_consultaTXT += "\t- Tamaño (m^2): " + casa.getInteger("tamano") + "\n";
					resultado_consultaTXT += "\t- NºHabitaciones: " + casa.getInteger("habitaciones") + "\n";
					resultado_consultaTXT += "\t- Baños: " + casa.getInteger("banos") + "\n";
					resultado_consultaTXT += "\t- Precio de venta: " + casa.getInteger("precio") + "\n";
					resultado_consultaTXT += "\t- Precio de alquiler: " + casa.getInteger("alquiler") + "\n";
					resultado_consultaTXT += "\t- Estado: " + casa.getString("estado") + "\n";
					
					//Sacamos las referencias de la casa.
					DBRef refPropietario = (DBRef) casa.get("propietario");
					DBRef refAgente = (DBRef) casa.get("agente");
					DBRef refComprador = (DBRef) casa.get("comprador");
					
					//Datos de la casa en JSON, excluimos los campos referenciados para evitar errores.
					casa.append("propietario", 0);
					casa.append("agente", 0);
					casa.append("comprador", 0);
					resultado_consultaJSON += casa.toJson() + "\n";
					
					if(refPropietario != null) {
						ObjectId _idP = (ObjectId) refPropietario.getId();
						
						Document propietario = MongoConnection.propietarios.find(new Document("_id", _idP)).first();
						System.out.println("\t- Propietario: " + propietario.getString("DNI") + ", " + propietario.getList("nombre", String.class));
						
						resultado_consultaTXT += "\t- Propietario: " + propietario.getString("DNI") + ", " + propietario.getList("nombre", String.class) + "\n";
						resultado_consultaJSON += propietario.toJson() + "\n";
					}
					if(refAgente != null) {
						ObjectId _idA = (ObjectId) refAgente.getId();
						
						Document agente = MongoConnection.agentes.find(new Document("_id", _idA)).first();
						System.out.println("\t- Agente: " + agente.getString("DNI") + ", " + agente.getList("nombre", String.class));
					
						resultado_consultaTXT += "\t- Agente: " + agente.getString("DNI") + ", " + agente.getList("nombre", String.class) + "\n";
						resultado_consultaJSON += agente.toJson() + "\n";
					}
					if(refComprador != null) {
						ObjectId _idC = (ObjectId) refComprador.getId();
						
						Document comprador = MongoConnection.compradores.find(new Document("_id", _idC)).first();
						System.out.println("\t- Comprador: " + comprador.getString("DNI") + ", " + comprador.getList("nombre", String.class));
					
						resultado_consultaTXT += "\t- Comprador: " + comprador.getString("DNI") + ", " + comprador.getList("nombre", String.class) + "\n";
						resultado_consultaJSON += comprador.toJson() + "\n";
					}
					resultado_consultaTXT += "\n\n";
					resultado_consultaJSON += "\n\n";
				}
			}
			System.out.println("\n");
		}
		ExportarResultado();
	}
}
