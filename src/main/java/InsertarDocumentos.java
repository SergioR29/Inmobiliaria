import java.util.*;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.DBRef;

public class InsertarDocumentos {
	static Scanner sc = new Scanner(System.in);
	
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
				
				//Evitar mostrar las referencias por errores
				casa.append("propietario", 0);
				casa.append("agente", 0);
				casa.append("comprador", 0);
				
				System.out.println((i+1) + ". " + casa.toJson());
				
				//Añadimos cada casa y su número al mapa de pares clave-valor.
				casasKV.put((i+1), casa);
			}
			System.out.println("\n=================");
		}
		return casasKV;
	}

	public static List<Document> ListarCompradores() {
		//Método auxiliar que muestra todos los compradores registrados.
		
		List<Document> compradores = new ArrayList<Document>();
		MongoConnection.compradores.find().into(compradores);
		
		if(compradores.size() > 0) {
			//Mostrar compradores
			System.out.println("\nCOMPRADORES REGISTRADOS");
			System.out.println("=======================\n");
			
			for(int i=0; i < compradores.size(); i++) {
				Document comprador = compradores.get(i);
				System.out.println((i+1) + ". " + comprador.toJson());
			}
			System.out.println("\n=======================");
		}
		return compradores;
	}

	public static List<Document> ListarAgentes() {
		//Método auxiliar que muestra todos los agentes inmobiliarios registrados.
		
		List<Document> agentes = new ArrayList<Document>();
		MongoConnection.agentes.find().into(agentes);
		
		if(agentes.size() > 0) {
			//Mostrar agentes
			System.out.println("\nAGENTES INMOBILIARIOS REGISTRADOS");
			System.out.println("=================================\n");
			
			for(int i=0; i < agentes.size(); i++) {
				Document agente = agentes.get(i);
				System.out.println((i+1) + ". " + agente.toJson());
			}
			System.out.println("\n===============================");
		}
		return agentes;
	}

	public static List<Document> ListarPropietarios() {
		//Método auxiliar que muestra todos los propietarios registrados.
		
		List<Document> propietarios = new ArrayList<Document>();
		MongoConnection.propietarios.find().into(propietarios);
		
		if(propietarios.size() > 0) {
			//Mostrar propietarios
			System.out.println("\nPROPIETARIOS REGISTRADOS");
			System.out.println("========================\n");
			
			for(int i=0; i < propietarios.size(); i++) {
				Document prop = propietarios.get(i);
				System.out.println((i+1) + ". " + prop.toJson());
				
			}
			System.out.println("\n========================");
		}
		return propietarios;
	}

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

	public static void RegistrarCasas() {
		//Método que pedirá los datos de la casa a registrar por el usuario.
		System.out.println("\n==========================================");
		System.out.println("\tDATOS DE LA CASA");
		System.out.println("==========================================");
		System.out.println("(-1 en cualquier campo para que el campo no sea registrado)\n");
		
		//Tipo de casa
		String tipo = "";
		System.out.print("Tipo: ");
		
		while(tipo.length() == 0) {
			tipo = sc.nextLine();
			
			if(tipo.isBlank() && tipo.length() > 0) {
				tipo = "";
				System.out.println("----------------------------------------------------------------------------------");
				System.out.println("\tNo se ha introducido el tipo de la casa.\n");
	
				System.out.print("Tipo: ");
			}
		}
		
		//Dirección de la casa, es una cadena con el nombre de la calle y su número.
		String direccion = "";
		int nCalle = 0;
		String calle = "";
		
		while(true) {
			System.out.print("NºCalle: ");
			try {
				nCalle = sc.nextInt();
			} catch (InputMismatchException e) {
				sc.nextLine();
				
				System.out.println("----------------------------------------");
				System.out.println("\tNº de calle inválido\n");
				
				continue;
			}
			break;
		}
	
		System.out.print("Nombre de la calle: ");
		while(calle.length() == 0) {
			calle = sc.nextLine();
			
			if(calle.isBlank() && calle.length() > 0) {
				calle = "";
				System.out.println("----------------------------------------------------------------------------------");
				System.out.println("\tNo se ha introducido el nombre de la calle.\n");
	
				System.out.print("Nombre de la calle: ");
			}
		}
		direccion = nCalle + ", " + calle; //Juntamos los datos de la dirección en una sola cadena.
		
		
		//Ciudad
		String ciudad = "";
		System.out.print("Ciudad: ");
		
		while(ciudad.length() == 0) {
			ciudad = sc.nextLine();
			
			if(ciudad.isBlank() && ciudad.length() > 0) {
				ciudad = "";
				System.out.println("----------------------------------------------------------------------------------");
				System.out.println("\tNo se ha introducido la ciudad.\n");
	
				System.out.print("Ciudad: ");
			}
		}
		
		//Provincia
		String provincia = "";
		System.out.print("Provincia: ");
		
		while(provincia.length() == 0) {
			provincia = sc.nextLine();
			
			if(provincia.isBlank() && provincia.length() > 0) {
				provincia = "";
				System.out.println("----------------------------------------------------------------------------------");
				System.out.println("\tNo se ha introducido la provincia.\n");
	
				System.out.print("Provincia: ");
			}
		}
		
		//Código Postal
		String CP = "";
		System.out.print("Código Postal: ");
		
		while(CP.length() == 0) {
			CP = sc.nextLine();
					
			if(CP.isBlank() && CP.length() > 0) {
				CP = "";
				System.out.println("----------------------------------------------------------------------------------");
				System.out.println("\tNo se ha introducido el código postal.\n");
	
				System.out.print("Código Postal: ");
			}
		}
		
		//Tamaño en m^2
		int tam = 0;
		while(true) {
			System.out.print("Tamaño (m^2): ");
			try {
				tam = sc.nextInt();
			} catch (InputMismatchException e) {
				sc.nextLine();
				
				System.out.println("----------------------------------------");
				System.out.println("\tTamaño inválido\n");
				
				continue;
			}
			break;
		}
		
		//Habitaciones
		int habitaciones = 0;
		while(true) {
			System.out.print("NºHabitaciones: ");
			try {
				habitaciones = sc.nextInt();
			} catch (InputMismatchException e) {
				sc.nextLine();
						
				System.out.println("----------------------------------------");
				System.out.println("\tNº de habitaciones inválido\n");
						
				continue;
			}
			break;
		}
		
		//Baños
		int banos = 0;
		while(true) {
			System.out.print("NºBaños: ");
			try {
				banos = sc.nextInt();
			} catch (InputMismatchException e) {
				sc.nextLine();
						
				System.out.println("----------------------------------------");
				System.out.println("\tNº de baños inválido\n");
								
				continue;
			}
			break;
		}
		
		//Precio de venta
		int precio_Venta = 0;
		while(true) {
			System.out.print("Precio de venta: ");
			try {
				precio_Venta = sc.nextInt();
			} catch (InputMismatchException e) {
				sc.nextLine();
								
				System.out.println("----------------------------------------");
				System.out.println("\tPrecio de venta inválido\n");
										
				continue;
			}
			break;
		}
		
		//Precio de alquiler
		int precio_Alquiler = 0;
		while(true) {
			System.out.print("Precio de alquiler: ");
			try {
				precio_Alquiler = sc.nextInt();
			} catch (InputMismatchException e) {
				sc.nextLine();
										
				System.out.println("----------------------------------------");
				System.out.println("\tPrecio de alquiler inválido\n");
												
				continue;
			}
			break;
		}
		
		//Estado de la casa
		String estado = "";
		System.out.print("Estado de la casa: ");
		
		while(estado.length() == 0) {
			estado = sc.nextLine();
							
			if(estado.isBlank() && estado.length() > 0) {
				estado = "";
				System.out.println("----------------------------------------------------------------------------------");
				System.out.println("\tNo se ha introducido el estado de la casa.\n");
	
				System.out.print("Estado de la casa: ");
			}
		}
		
		//Propietario de la casa
		List<Document> propietarios = ListarPropietarios();
		String propietario = "";
		
		//Obtenemos el identificador del documento del propietario correspondiente en la colección para hacer la referencia
		Document consultaP;
		ObjectId _id;
		DBRef refPropietario = null;
		
		//Si no hay propietarios
		if(propietarios.size() == 0) {
			System.out.println("\tNo hay propietarios disponibles para asociar a la casa.\n");
			propietario = "-1";
		} else {
			System.out.print("\nPropietario de la casa (DNI): ");
			
			while(propietario.length() == 0) {
				propietario = sc.nextLine();
				
				if(propietario.isBlank() && propietario.length() > 0) {
					propietario = "";
					System.out.println("----------------------------------------------------------------------------------");
					System.out.println("\tNo se ha introducido el propietario de la casa.\n");
	
					System.out.print("Propietario de la casa (DNI): ");
				} else {
					if(propietario.equals("-1")) break;
					
					//Comprobar que el DNI del propietario escrito exista.
					boolean existe = false;
					for(Document prop: propietarios) {
						if(prop.getString("DNI") != null) {
							if(prop.getString("DNI").equals(propietario)) {
								existe = true;
							}
						}
					}
					if(!existe && propietario.length() > 0) {
						propietario = "";
						
						System.out.println("----------------------------------------------------------------------------------");
						System.out.println("\tEl propietario introducido no existe.\n");
						
					} else if(propietario.length() > 0) {
						//Consultamos el propietario con el DNI introducido, si lo encuentra o es -1 salimos del bucle.
						consultaP = MongoConnection.propietarios.find(new Document("DNI", propietario)).first();
						_id = consultaP.getObjectId("_id");
						refPropietario = new DBRef("Propietarios", _id);
						
						if(consultaP != null || propietario.equals("-1")) {
							break;
						}
					}
					System.out.print("Propietario de la casa (DNI): ");
				}
			}
		}
		
		//Agente inmobiliario encargado de la casa
		List<Document> agentes = ListarAgentes();
		String agente = "";
		
		//Obtenemos el identificador del documento del agente inmobiliario correspondiente en la colección para hacer la referencia
		Document consultaAGI;
		DBRef refAgente = null;
		
		if(agentes.size() == 0) {
			System.out.println("\tNo hay agentes inmobiliarios disponibles para asociar a la casa.");
			agente = "-1";
		} else {
			System.out.print("\nAgente inmobiliario de la casa (DNI): ");
			
			while(agente.length() == 0) {
				agente = sc.nextLine();
				
				if(agente.isBlank() && agente.length() > 0) {
					agente = "";
					System.out.println("----------------------------------------------------------------------------------");
					System.out.println("\tNo se ha introducido el agente inmobiliario de la casa.\n");
	
					System.out.print("Agente inmobiliario de la casa (DNI): ");
				} else {
					if(agente.equals("-1")) break;
					
					//Comprobar que el DNI del agente inmobiliario escrito exista.
					boolean existe = false;
					for(Document AGI: agentes) {
						if(AGI.getString("DNI") != null) {
							if(AGI.getString("DNI").equals(agente)) {
								existe = true;
							}
						}
					}
					if(!existe && agente.length() > 0) {
						agente = "";
						
						System.out.println("----------------------------------------------------------------------------------");
						System.out.println("\tEl agente inmobiliario introducido no existe.\n");
						
					} else if(agente.length() > 0) {
						//Consultamos el agente inmobiliario con el DNI introducido, si lo encuentra o es -1 salimos del bucle.
						consultaAGI = MongoConnection.agentes.find(new Document("DNI", agente)).first();
						_id = consultaAGI.getObjectId("_id");
						refAgente = new DBRef("Agentes_Inmobiliarios", _id);
						
						if(consultaAGI != null || agente.equals("-1")) {
							break;
						}
					}
					System.out.print("Agente inmobiliario de la casa (DNI): ");
				}
			}
		}
		
		//Comprador de la casa
		List<Document> compradores = ListarCompradores();
		String comprador = "";
		
		//Obtenemos el identificador del documento del comprador correspondiente en la colección para hacer la referencia
		Document consultaCOM;
		DBRef refComprador = null;
		
		if(compradores.size() == 0) {
			System.out.println("\n\tNo hay compradores disponibles para asociar a la casa.");
			comprador = "-1";
		} else {
			System.out.print("\nComprador de la casa (DNI): ");
			while(comprador.length() == 0) {
				comprador = sc.nextLine();
				
				if(comprador.isBlank() && comprador.length() > 0) {
					comprador = "";
					System.out.println("----------------------------------------------------------------------------------");
					System.out.println("\tNo se ha introducido el comprador de la casa.\n");
	
					System.out.print("Comprador de la casa (DNI): ");
				} else {
					if(comprador.equals("-1")) break;
					
					//Comprobar que el DNI del agente inmobiliario escrito exista.
					boolean existe = false;
					for(Document COM: compradores) {
						if(COM.getString("DNI") != null) {
							if(COM.getString("DNI").equals(comprador)) {
								existe = true;
							}
						}
					}
					if(!existe && comprador.length() > 0) {
						comprador = "";
						
						System.out.println("----------------------------------------------------------------------------------");
						System.out.println("\tEl comprador introducido no existe.\n");
						
					} else if(comprador.length() > 0) {
						//Consultamos el comprador con el DNI introducido, si lo encuentra o es -1 salimos del bucle.
						consultaCOM = MongoConnection.compradores.find(new Document("DNI", comprador)).first();
						_id = consultaCOM.getObjectId("_id");
						refComprador = new DBRef("Compradores", _id);
						
						if(consultaCOM != null || comprador.equals("-1")) {
							break;
						}
					}
					System.out.print("Comprador de la casa (DNI): ");
				}
			}
		}
		
		//Ya podemos crear el documento json de la casa y registrarla en la BD.
		Document casa = new Document();
		if(!tipo.equals("-1")) casa.put("tipo", tipo);
		if(!direccion.equals("-1")) casa.put("direccion", direccion);
		if(!ciudad.equals("-1")) casa.put("ciudad", ciudad);
		if(!provincia.equals("-1")) casa.put("provincia", provincia);
		if(!CP.equals("-1")) casa.put("codigo_postal", CP);
		if(tam != -1) casa.put("tamano", tam);
		if(habitaciones != -1) casa.put("habitaciones", habitaciones);
		if(banos != -1) casa.put("banos", banos);
		if(precio_Venta != -1) casa.put("precio", precio_Venta);
		if(precio_Alquiler != -1) casa.put("alquiler", precio_Alquiler);
		if(!estado.equals("-1")) casa.put("estado", estado);
		if(!propietario.equals("-1")) casa.put("propietario", refPropietario);
		if(!agente.equals("-1")) casa.put("agente", refAgente);
		if(!comprador.equals("-1")) casa.put("comprador", refComprador);
		
		MongoConnection.casas.insertOne(casa);
		System.out.println("\nCasa registrada");
	}

	public static void RegistrarAgentes() {
		//Método que pedirá los datos de un agente inmobiliario a registrar por el usuario.
		System.out.println("\n==========================================");
		System.out.println("\tDATOS DEL AGENTE INMOBILIARIO");
		System.out.println("==========================================");
		System.out.println("(-1 en cualquier campo para que el campo no sea registrado)\n");
		
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
						
						
		//DNI
		String DNI = "";
		System.out.print("DNI: ");
						
		while(DNI.length() == 0) {
			DNI = sc.nextLine();
											
			if(DNI.isBlank() && DNI.length() > 0) {
				DNI = "";
				System.out.println("----------------------------------------------------------------------------------");
				System.out.println("\tNo se ha introducido el DNI del agente inmobiliario.\n");

				System.out.print("DNI: ");
			}
		}
						
		//Teléfono
		String telefono = "";
		System.out.print("NºTeléfono: ");
						
		while(telefono.length() == 0) {
			telefono = sc.nextLine();
													
			if(telefono.isBlank() && telefono.length() > 0) {
				telefono = "";
				System.out.println("----------------------------------------------------------------------------------");
				System.out.println("\tNo se ha introducido el nºde teléfono del agente inmobiliario.\n");

				System.out.print("NºTeléfono: ");
			}
		}		
						
		//Email
		String correo = "";
		System.out.print("Correo electrónico: ");
						
		while(correo.length() == 0) {
			correo = sc.nextLine();
													
			if(correo.isBlank() && correo.length() > 0) {
				correo = "";
				System.out.println("----------------------------------------------------------------------------------");
				System.out.println("\tNo se ha introducido el correo del agente inmobiliario.\n");
								
				System.out.print("Correo electrónico: ");
			}
		}
		
		//NºLicencia
		String licencia = "";
		System.out.print("NºLicencia: ");
								
		while(licencia.length() == 0) {
			licencia = sc.nextLine();
															
			if(licencia.isBlank() && licencia.length() > 0) {
				licencia = "";
				System.out.println("----------------------------------------------------------------------------------");
				System.out.println("\tNo se ha introducido el nºde licencia del agente inmobiliario.\n");
										
				System.out.print("NºLicencia: ");
			}
		}
		
		//Ya podemos insertar los datos
		Document agente = new Document();
		
		if(posVacia < 3) agente.put("nombre", Arrays.asList(nombreCompleto));
		if(!DNI.equals("-1")) agente.put("DNI", DNI);
		if(!telefono.equals("-1")) agente.put("telefono", telefono);
		if(!correo.equals("-1")) agente.put("email", correo);
		agente.put("licencia", licencia);
		
		MongoConnection.agentes.insertOne(agente);
		System.out.println("\nAgente inmobiliario registrado");
	}

	public static void RegistrarCompradores() {
		//Método que pedirá los datos de un comprador a registrar por el usuario.
		System.out.println("\n==========================================");
		System.out.println("\tDATOS DEL COMPRADOR");
		System.out.println("==========================================");
		System.out.println("(-1 en cualquier campo para que el campo no sea registrado)\n");
		
		//Nombre completo (nombre + apellido1 + apellido2)
		String[] nombreCompleto = new String[3];
				
		String nombre = "";
		System.out.print("Nombre: ");
				
		while(nombre.length() == 0) {
			nombre = sc.nextLine();
									
			if(nombre.isBlank() && nombre.length() > 0) {
				nombre = "";
				System.out.println("----------------------------------------------------------------------------------");
				System.out.println("\tNo se ha introducido el nombre del comprador.\n");
						
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
				System.out.println("\tNo se ha introducido el 1ºapellido del comprador.\n");
						
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
				System.out.println("\tNo se ha introducido el 2ºapellido del comprador.\n");

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
				
				
		//DNI
		String DNI = "";
		System.out.print("DNI: ");
				
		while(DNI.length() == 0) {
			DNI = sc.nextLine();
									
			if(DNI.isBlank() && DNI.length() > 0) {
				DNI = "";
				System.out.println("----------------------------------------------------------------------------------");
				System.out.println("\tNo se ha introducido el DNI del comprador.\n");

				System.out.print("DNI: ");
			}
		}
				
		//Teléfono
		String telefono = "";
		System.out.print("NºTeléfono: ");
				
		while(telefono.length() == 0) {
			telefono = sc.nextLine();
											
			if(telefono.isBlank() && telefono.length() > 0) {
				telefono = "";
				System.out.println("----------------------------------------------------------------------------------");
				System.out.println("\tNo se ha introducido el nºde teléfono del comprador.\n");

				System.out.print("NºTeléfono: ");
			}
		}		
				
		//Email
		String correo = "";
		System.out.print("Correo electrónico: ");
				
		while(correo.length() == 0) {
			correo = sc.nextLine();
											
			if(correo.isBlank() && correo.length() > 0) {
				correo = "";
				System.out.println("----------------------------------------------------------------------------------");
				System.out.println("\tNo se ha introducido el correo del comprador.\n");
						
				System.out.print("Correo electrónico: ");
			}
		}
		
		//Presupuesto máximo
		int presu = 0;
		while(true) {
			try {
				System.out.print("Presupuesto: ");
				presu = sc.nextInt();
			} catch (InputMismatchException e) {
				sc.nextLine();
				System.out.println("--------------------");
				System.out.println("\tPresupuesto inválido.\n");
				continue;
			}
			
			if(presu < -1) {
				System.out.println("--------------------");
				System.out.println("\tPresupuesto inválido.\n");
				continue;
			}
			break;
		}
		
		//Preferencias del comprador
		String preferencias = "";
		System.out.print("Preferencias del comprador: ");
						
		while(preferencias.length() == 0) {
			preferencias = sc.nextLine();
															
			if(preferencias.isBlank() && preferencias.length() > 0) {
				preferencias = "";
				System.out.println("----------------------------------------------------------------------------------");
				System.out.println("\tNo se han introducido las preferencias del comprador.\n");
										
				System.out.print("Preferencias del comprador: ");
			}
		}
		
		//Ya podemos insertar los datos.
		Document comprador = new Document();
		if(posVacia < 3) comprador.put("nombre", Arrays.asList(nombreCompleto));
		if(!DNI.equals("-1")) comprador.put("DNI", DNI);
		if(!telefono.equals("-1")) comprador.put("telefono", telefono);
		if(!correo.equals("-1")) comprador.put("email", correo);
		if(presu != -1) comprador.put("presupuesto", presu);
		if(!preferencias.equals("-1")) comprador.put("preferencias", preferencias);
		
		MongoConnection.compradores.insertOne(comprador);
		System.out.println("\nComprador registrado");
	}

	public static void RegistrarPropietarios() {
		//Método que pedirá los datos de un propietario a registrar por el usuario.
		System.out.println("\n==========================================");
		System.out.println("\tDATOS DEL PROPIETARIO");
		System.out.println("==========================================");
		System.out.println("(-1 en cualquier campo para que el campo no sea registrado)\n");
		
		//Nombre completo (nombre + apellido1 + apellido2)
		String[] nombreCompleto = new String[3];
		
		String nombre = "";
		System.out.print("Nombre: ");
		
		while(nombre.length() == 0) {
			nombre = sc.nextLine();
							
			if(nombre.isBlank() && nombre.length() > 0) {
				nombre = "";
				System.out.println("----------------------------------------------------------------------------------");
				System.out.println("\tNo se ha introducido el nombre del propietario.\n");
				
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
				System.out.println("\tNo se ha introducido el 1ºapellido del propietario.\n");
				
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
				System.out.println("\tNo se ha introducido el 2ºapellido del propietario.\n");

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
		
		
		//DNI
		String DNI = "";
		System.out.print("DNI: ");
		
		while(DNI.length() == 0) {
			DNI = sc.nextLine();
							
			if(DNI.isBlank() && DNI.length() > 0) {
				DNI = "";
				System.out.println("----------------------------------------------------------------------------------");
				System.out.println("\tNo se ha introducido el DNI del propietario.\n");

				System.out.print("DNI: ");
			}
			
			//Comprobar que el DNI del propietario escrito exista.
			List<Document> propietarios = ListarPropietarios();
			
			boolean existe = false;
			for(Document prop: propietarios) {
				if(prop.getString("DNI").equals(DNI)) {
					existe = true;
				}
			}
			if(existe) {
				DNI = "";
				System.out.println("----------------------------------------------------------------------------------");
				System.out.println("\tEl DNI escrito ya está asociado con otro propietario.\n");
			} else {
				break;
			}
		}
		
		//Teléfono
		String telefono = "";
		System.out.print("NºTeléfono: ");
		
		while(telefono.length() == 0) {
			telefono = sc.nextLine();
									
			if(telefono.isBlank() && telefono.length() > 0) {
				telefono = "";
				System.out.println("----------------------------------------------------------------------------------");
				System.out.println("\tNo se ha introducido el nºde teléfono del propietario.\n");

				System.out.print("NºTeléfono: ");
			}
		}		
		
		//Email
		String correo = "";
		System.out.print("Correo electrónico: ");
		
		while(correo.length() == 0) {
			correo = sc.nextLine();
									
			if(correo.isBlank() && correo.length() > 0) {
				correo = "";
				System.out.println("----------------------------------------------------------------------------------");
				System.out.println("\tNo se ha introducido el correo del propietario.\n");
				
				System.out.print("Correo electrónico: ");
			}
		}
		//Dirección de la casa, es una cadena con el nombre de la calle y su número.
		String direccion = "";
		int nCalle = 0;
		String calle = "";
				
		while(true) {
			System.out.print("NºCalle: ");
			try {
				nCalle = sc.nextInt();
			} catch (InputMismatchException e) {
				sc.nextLine();
						
				System.out.println("----------------------------------------");
				System.out.println("\tNº de calle inválido\n");
						
				continue;
			}
			break;
		}

		System.out.print("Nombre de la calle: ");		
		while(calle.length() == 0) {
			calle = sc.nextLine();
					
			if(calle.isBlank() && calle.length() > 0) {
				calle = "";
				System.out.println("----------------------------------------------------------------------------------");
				System.out.println("\tNo se ha introducido el nombre de la calle.\n");

				System.out.print("Nombre de la calle: ");
			}
		}
		direccion = (nCalle == -1 ? "" : nCalle + ", ") + (calle.equals("-1") ? "": calle); //Juntamos los datos de la dirección en una sola cadena.
		
		//Datos listos para insertar. Comprobamos que el usuario no haya excluido campos.
		Document propietario = new Document();
		
		if(posVacia < 3) propietario.put("nombre", Arrays.asList(nombreCompleto));
		if(!DNI.equals("-1")) propietario.put("DNI", DNI);
		if(!telefono.equals("-1")) propietario.put("telefono", telefono);
		if(!correo.equals("-1")) propietario.put("email", correo);
		if(direccion.length() > 0) propietario.put("direccion", direccion);
		
		MongoConnection.propietarios.insertOne(propietario);
		System.out.println("\nPropietario registrado");
	}

	public static void RegistrarMantenimiento() {
		//Método para registrar los datos de un mantenimiento.
		System.out.println("\n==========================================");
		System.out.println("\tDATOS DEL MANTENIMIENTO");
		System.out.println("==========================================");
		System.out.println("(-1 en cualquier campo para que el campo no sea registrado)\n");
		
		//Casa a realizar el mantenimiento
		Map<Integer, Document> casas = ListarCasas();
		int casa = 0;

		//Obtenemos el identificador del documento de la casa correspondiente en la colección para hacer la referencia
		ObjectId _id;
		DBRef refCasa = null;
		
		if(casas.size() == 0) {
			System.out.println("\n\tNo hay casas registradas por el momento.\n");
			casa = -1;
		} else {
			System.out.print("\nCasa a realizar el mantenimiento (nº): ");
			
			while(true) {
				try {
					casa = sc.nextInt();
				} catch (InputMismatchException e) {
					sc.nextLine();
					System.out.println("----------------------------------------------------------------------------------");
					System.out.println("\tNº de casa inválido\n");
					
					System.out.print("Casa a realizar el mantenimiento (nº): ");
					continue;
				}
				
				//Si el nº de la casa se ha introducido bien como tal, comprobamos que exista
				Document seleccionada = casas.get(casa);
				if(seleccionada != null) {
					//La casa existe
					_id = seleccionada.getObjectId("_id");
					refCasa = new DBRef("Casas", _id);
					
					break;
				} else {
					if(casa == -1) break;
					System.out.println("\n\tLa casa solicitada no existe.\n");
				}
			}
		}
				
		//Descripción del problema
		String problema = "";
		System.out.print("Descripción del problema: ");
				
		while(problema.length() == 0) {
			problema = sc.nextLine();
									
			if(problema.isBlank() && problema.length() > 0) {
				problema = "";
				System.out.println("----------------------------------------------------------------------------------");
				System.out.println("\tNo se ha introducido la descripción del problema.\n");
						
				System.out.print("Descripción del problema: ");
			}
		}
		
		//Costo
		int costo = 0;
		while(true) {
			System.out.print("Costo: ");
			try {
				costo = sc.nextInt();
			} catch (InputMismatchException e) {
				sc.nextLine();
				
				System.out.println("----------------------------------------");
				System.out.println("\tCosto inválido\n");
				
				continue;
			}
			break;
		}
		
		//Fecha programada
		String fecha = "";
		int dia = 0, mes = 0, año = 0;
		System.out.println("\nFecha del mantenimiento");
		System.out.println("-------------------------");
		
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
			
			if(dia > 31) {
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
			
			if(mes > 12) {
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
		
		//Estado
		String estado = "";
		System.out.print("Estado (pendiente -> [por defecto], en proceso, finalizado): ");
		
		while(estado.length() == 0) {
			estado = sc.nextLine();
									
			if(estado.isBlank() && estado.length() > 0) {
				estado = "";
				System.out.println("----------------------------------------------------------------------------------");
				System.out.println("\tNo se ha introducido el estado del mantenimiento.\n");
						
				System.out.print("Estado (pendiente -> [por defecto], en proceso, finalizado): ");
			} else if(estado.length() > 0) {
				if(!estado.equalsIgnoreCase("pendiente") && !estado.equalsIgnoreCase("en proceso") 
				   && !estado.equalsIgnoreCase("finalizado") && !estado.equals("-1")) {
					
					estado = "pendiente";
				}
			}
		}
		
		//Ya podemos insertar todos los datos del mantenimiento
		Document mantenimiento = new Document();
		if(casa != -1) mantenimiento.put("Casa", refCasa);
		if(!problema.equals("-1")) mantenimiento.put("Descripcion", problema);
		if(costo != -1) mantenimiento.put("Costo", costo);
		if(dia != -1 && mes != -1 && año != -1) mantenimiento.put("fecha", fecha);
		if(!estado.equals("-1")) mantenimiento.put("estado", estado);
		
		MongoConnection.mantenimientos.insertOne(mantenimiento);
		System.out.println("\nMantenimiento registrado");
	}

	public static void RegistrarPagos() {
		//Método para registrar los pagos de un contrato.
		System.out.println("\n==========================================");
		System.out.println("\tDATOS DEL PAGO");
		System.out.println("==========================================");
		System.out.println("(-1 en cualquier campo para que el campo no sea registrado)\n");
		
		//Contrato asociado al pago
		Map<Integer, Document> contratos = ListarContratos();
		int contrato = 0;
		
		//Obtenemos el identificador del documento del contrato correspondiente en la colección para hacer la referencia
		ObjectId _id;
		DBRef refContrato = null;
		
		if(contratos.size() == 0) {
			System.out.println("\n\tNo hay contratos registrados por el momento.\n");
			contrato = -1;
		} else {
			System.out.print("\nContrato asociado al pago (nº): ");
			
			while(true) {
				try {
					contrato = sc.nextInt();
				} catch (InputMismatchException e) {
					sc.nextLine();
					System.out.println("----------------------------------------------------------------------------------");
					System.out.println("\tNº de contrato inválido\n");
					
					System.out.print("Contrato asociado al pago (nº): ");
					continue;
				}
				
				Document seleccionado = contratos.get(contrato);
				if(seleccionado != null) {
					//El contrato existe
					_id = seleccionado.getObjectId("_id");
					refContrato = new DBRef("Contratos", _id);
					
					break;
				} else {
					if(contrato == -1) break;
					System.out.println("\n\tEl contrato asociado al pago no existe.\n");
				}
			}
		}
		
		
		//Fecha del pago (día + mes + año)
		String fecha = "";
		int dia = 0, mes = 0, año = 0;
		System.out.println("\nFecha de pago");
		System.out.println("-------------");
		
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
			
			if(dia > 31) {
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
			
			if(mes > 12) {
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
		
		
		//Cantidad a pagar (ya sea en total o al mes)
		int cantidad = 0;
		while(true) {
			try {
				System.out.print("\nCantidad a pagar: ");
				cantidad = sc.nextInt();
			} catch (InputMismatchException e) {
				sc.nextLine();
				System.out.println("\t  =================");
				System.out.println("\t  Cantidad inválida\n");
				continue;
			}
			break;
		}
		
		//Método de Pago
		String metodoPago = "";
		System.out.print("Método de pago: ");
		
		while(metodoPago.length() == 0) {
			metodoPago = sc.nextLine();
							
			if(metodoPago.isBlank() && metodoPago.length() > 0) {
				metodoPago = "";
				System.out.println("----------------------------------------------------------------------------------");
				System.out.println("\tNo se ha introducido el método de pago.\n");
				
				System.out.print("Método de pago: ");
			}
		}
		
		//Ya podemos insertar todos los datos del pago.
		Document pago = new Document();
		if(contrato != -1) pago.put("Contrato", refContrato);
		if(dia != -1 && mes != -1 && año != -1) pago.put("fecha_pago", fecha);
		if(!metodoPago.equals("-1")) pago.put("metodo_pago", metodoPago);
		if(cantidad != -1) pago.put("cantidad", cantidad);
		
		MongoConnection.pagos.insertOne(pago);
		System.out.println("\nPago registrado");
	}

	public static void RegistrarVisitas() {
		//Método que permitirá al usuario registrar las visitas realizadas a la casa.
		System.out.println("\n==========================================");
		System.out.println("\tDATOS DE LA VISITA");
		System.out.println("==========================================");
		System.out.println("(-1 en cualquier campo para que el campo no sea registrado)\n");
		
		//Comprador de la casa en el contrato
		List<Document> compradores = ListarCompradores();
		String comprador = "";
		
		//Obtenemos el identificador del documento del comprador correspondiente en la colección para hacer la referencia
		Document consultaCOM;
		ObjectId _id;
		DBRef refComprador = null;
		
		if(compradores.size() == 0) {
			System.out.println("\n\tNo hay compradores disponibles para asociar a la visita.");
			comprador = "-1";
		} else {
			System.out.print("\nComprador que va a realizar la visita (DNI): ");
			
			while(comprador.length() == 0) {
				comprador = sc.nextLine();
				
				if(comprador.isBlank() && comprador.length() > 0) {
					comprador = "";
					System.out.println("----------------------------------------------------------------------------------");
					System.out.println("\tNo se ha introducido el comprador que va a realizar la visita.\n");

					System.out.print("Comprador que va a realizar la visita (DNI): ");
				} else {
					if(comprador.equals("-1")) break;
					
					//Comprobar que el DNI del agente inmobiliario escrito exista.
					boolean existe = false;
					for(Document COM: compradores) {
						if(COM != null) {
							if(COM.getString("DNI").equals(comprador)) {
								existe = true;
							}
						}
					}
					if(!existe && comprador.length() > 0) {
						comprador = "";
						
						System.out.println("----------------------------------------------------------------------------------");
						System.out.println("\tEl comprador introducido no existe.\n");
						
					} else if(comprador.length() > 0) {
						//Consultamos el comprador con el DNI introducido, si lo encuentra o es -1 salimos del bucle.
						consultaCOM = MongoConnection.compradores.find(new Document("DNI", comprador)).first();
						_id = consultaCOM.getObjectId("_id");
						refComprador = new DBRef("Compradores", _id);
						
						if(consultaCOM != null || comprador.equals("-1")) {
							break;
						}
					}
					System.out.print("Comprador en el contrato (DNI): ");
				}
			}
		}
		
		//Casa especificada en el contrato
		Map<Integer, Document> casas = ListarCasas();
		int casa = 0;

		//Obtenemos el identificador del documento de la casa correspondiente en la colección para hacer la referencia
		DBRef refCasa = null;
		
		if(casas.size() == 0) {
			System.out.println("\n\tNo hay casas registradas por el momento.\n");
			casa = -1;
		} else {
			System.out.print("\nCasa a realizar la visita (nº): ");
			
			while(true) {
				try {
					casa = sc.nextInt();
				} catch (InputMismatchException e) {
					sc.nextLine();
					System.out.println("----------------------------------------------------------------------------------");
					System.out.println("\tNº de casa inválido\n");
					
					System.out.print("Casa a realizar la visita (nº): ");
					continue;
				}
				
				//Si el nº de la casa se ha introducido bien como tal, comprobamos que exista
				Document seleccionada = casas.get(casa);
				if(seleccionada != null) {
					//La casa existe
					_id = seleccionada.getObjectId("_id");
					refCasa = new DBRef("Casas", _id);
					
					break;
				} else {
					if(casa == -1) break;
					System.out.println("\n\tLa casa solicitada no existe.\n");
				}
			}
		}
		
		//Agente inmobiliario encargado de la casa en el contrato
		List<Document> agentes = ListarAgentes();
		String agente = "";
		
		//Obtenemos el identificador del documento del agente inmobiliario correspondiente en la colección para hacer la referencia
		Document consultaAGI;
		DBRef refAgente = null;
		
		if(agentes.size() == 0) {
			System.out.println("\tNo hay agentes inmobiliarios disponibles para asociar a la visita.");
			agente = "-1";
		} else {
			System.out.print("\nAgente inmobiliario de la visita (DNI): ");
			
			while(agente.length() == 0) {
				agente = sc.nextLine();
				
				if(agente.isBlank() && agente.length() > 0) {
					agente = "";
					System.out.println("----------------------------------------------------------------------------------");
					System.out.println("\tNo se ha introducido el agente inmobiliario de la visita.\n");

					System.out.print("Agente inmobiliario de la visita (DNI): ");
				} else {
					if(agente.equals("-1")) break;
					
					//Comprobar que el DNI del agente inmobiliario escrito exista.
					boolean existe = false;
					for(Document AGI: agentes) {
						if(AGI.getString("DNI") != null) {
							if(AGI.getString("DNI").equals(agente)) {
								existe = true;
							}
						}
					}
					if(!existe && agente.length() > 0) {
						agente = "";
						
						System.out.println("----------------------------------------------------------------------------------");
						System.out.println("\tEl agente inmobiliario introducido no existe.\n");
						
					} else if(agente.length() > 0) {
						//Consultamos el agente inmobiliario con el DNI introducido, si lo encuentra o es -1 salimos del bucle.
						consultaAGI = MongoConnection.agentes.find(new Document("DNI", agente)).first();
						_id = consultaAGI.getObjectId("_id");
						refAgente = new DBRef("Agentes_Inmobiliarios", _id);
						
						if(consultaAGI != null || agente.equals("-1")) {
							break;
						}
					}
					System.out.print("Agente inmobiliario de la visita (DNI): ");
				}
			}
		}
		
		//Fecha de la visita (día + mes + año)
		String fecha = "";
		int dia = 0, mes = 0, año = 0;
		
		System.out.println("\nFecha de la visita");
		System.out.println("------------------");
		
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
			
			if(dia > 31) {
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
			
			if(mes > 12) {
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
		
		//Hora
		String hora = "";
		int num_hora = -2, minutos = -2;
		
		System.out.println("\nHora de la visita");
		System.out.println("-----------------");
		
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
		hora = num_hora + ":" + (minutos >= 10 ? minutos : ("0" + minutos));
		
		//Comentarios
		String comentarios = "";
		System.out.print("Comentarios: ");
		
		while(comentarios.length() == 0) {
			comentarios = sc.nextLine();
							
			if(comentarios.isBlank() && comentarios.length() > 0) {
				comentarios = "";
				System.out.println("----------------------------------------------------------------------------------");
				System.out.println("\tNo se han introducido los comentarios.\n");
				
				System.out.print("Comentarios: ");
			}
		}
		
		//Ya podemos insertar todos los datos de la visita
		Document visita = new Document();
		if(!comprador.equals("-1")) visita.put("Comprador", refComprador);
		if(casa != -1) visita.put("Casa", refCasa);
		if(!agente.equals("-1")) visita.put("Agente", refAgente);
		if(dia != -1 && mes != -1 && año != -1) visita.put("fecha", fecha);
		if(num_hora != -1 && minutos != -1) visita.put("hora", hora);
		if(!comentarios.equals("-1")) visita.put("comentarios", comentarios);
		
		MongoConnection.visitas.insertOne(visita);
		System.out.println("\nVisita registrada");
	}

	public static void RegistrarContratos() {
		//Método que permitirá registrar contratos al usuario.
		System.out.println("\n==========================================");
		System.out.println("\tDATOS DEL CONTRATO");
		System.out.println("==========================================");
		System.out.println("(-1 en cualquier campo para que el campo no sea registrado)\n");
		
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
		//Fecha de inicio
		String fechaI = "";
		int diaI = 0, mesI = 0, añoI = 0;
		System.out.println("\nFecha de inicio");
		System.out.println("-------------------------");
				
		while(true) {//Día
			try {
				System.out.print("\t- Día: ");
				diaI = sc.nextInt();
			} catch (InputMismatchException e) {
				sc.nextLine();
				System.out.println("\t  ============");
				System.out.println("\t  Día inválido\n");
				continue;
			}
			
			if(diaI > 31) {
				System.out.println("\t  ============");
				System.out.println("\t  Día inválido\n");
				continue;
			}
			break;
		}
		while(true) {//Mes
			try {
				System.out.print("\t- Mes: ");
				mesI = sc.nextInt();
			} catch (InputMismatchException e) {
				sc.nextLine();
				System.out.println("\t  ============");
				System.out.println("\t  Mes inválido\n");
				continue;
			}
			
			if(mesI > 12) {
				System.out.println("\t  ============");
				System.out.println("\t  Mes inválido\n");
				continue;
			}
			break;
		}
		while(true) {//Año
			try {
				System.out.print("\t- Año: ");
				añoI = sc.nextInt();
			} catch (InputMismatchException e) {
				sc.nextLine();
				System.out.println("\t  ============");
				System.out.println("\t  Año inválido\n");
				continue;
			}

			if(añoI < -1) {
				System.out.println("\t  ============");
				System.out.println("\t  Año inválido\n");
				continue;
			}
			break;
		}
		fechaI = diaI + "/" + (mesI >= 10 ? mesI : "0" + mesI) + "/" + añoI; //Juntamos los datos necesarios para formar la fecha.
		
		//Fecha de finalización
		String fechaF = "";
		int diaF = 0, mesF = 0, añoF = 0;
		
		System.out.println("\nFecha de finalización");
		System.out.println("-------------------------");
						
		while(true) {//Día
			try {
				System.out.print("\t- Día: ");
				diaF = sc.nextInt();
			} catch (InputMismatchException e) {
				sc.nextLine();
				System.out.println("\t  ============");
				System.out.println("\t  Día inválido\n");
				continue;
			}
			
			if(diaF > 31) {
				System.out.println("\t  ============");
				System.out.println("\t  Día inválido\n");
				continue;
			}
			break;
		}
		while(true) {//Mes
			try {
				System.out.print("\t- Mes: ");
				mesF = sc.nextInt();
			} catch (InputMismatchException e) {
				sc.nextLine();
				System.out.println("\t  ============");
				System.out.println("\t  Mes inválido\n");
				continue;
			}

			if(mesF > 12) {
				System.out.println("\t  ============");
				System.out.println("\t  Mes inválido\n");
				continue;
			}
			break;
		}
		while(true) {//Año
			try {
				System.out.print("\t- Año: ");
				añoF = sc.nextInt();
			} catch (InputMismatchException e) {
				sc.nextLine();
				System.out.println("\t  ============");
				System.out.println("\t  Año inválido\n");
				continue;
			}
			
			if(añoF < -1) {
				System.out.println("\t  ============");
				System.out.println("\t  Año inválido\n");
				continue;
			}
			break;
		}
		fechaF = diaF + "/" + (mesF >= 10 ? mesF : "0" + mesF) + "/" + añoF; //Juntamos los datos necesarios para formar la fecha.
		
		//Método de Pago
		String metodoPago = "";
		System.out.print("Método de pago: ");
				
		while(metodoPago.length() == 0) {
			metodoPago = sc.nextLine();
									
			if(metodoPago.isBlank() && metodoPago.length() > 0) {
				metodoPago = "";
				System.out.println("----------------------------------------------------------------------------------");
				System.out.println("\tNo se ha introducido el método de pago.\n");
					
				System.out.print("Método de pago: ");
			}
		}
		
		//Antes de elegir la cantidad a pagar le daremos la opción al usuario elegir el tipo de cantidad.
		String tipoCantidad = "";
		if(tipo.equalsIgnoreCase("compraventa")) {
			tipoCantidad = "Total";
		} else if(tipo.equalsIgnoreCase("alquiler")) {
			tipoCantidad = "Al mes";
		}
		
		//Cantidad a pagar (ya sea en total o al mes)
		int cantidad = 0;
		while(true) {
			try {
				System.out.print("Cantidad a pagar (ya sea en total o al mes): ");
				cantidad = sc.nextInt();
			} catch (InputMismatchException e) {
				sc.nextLine();
				System.out.println("\t  =================");
				System.out.println("\t  Cantidad inválida\n");
				continue;
			}
			break;
		}
		
		//Estado del contrato
		String estado = "";
		System.out.print("Estado del contrato (activo, finalizado, cancelado): ");
		
		while(estado.length() == 0) {
			estado = sc.nextLine();
			
			if(estado.isBlank() && estado.length() > 0) {
				estado = "";
				System.out.println("----------------------------------------------------------------------------------");
				System.out.println("\tNo se ha introducido el estado del contrato.\n");

				System.out.print("Estado del contrato (activo, finalizado, cancelado): ");
				
			} else if(!estado.isBlank()) {
				if(!estado.equals("-1")) {
					if(!estado.equalsIgnoreCase("activo") && !estado.equalsIgnoreCase("finalizado") && !estado.equalsIgnoreCase("cancelado")) {
						estado = "";
						System.out.println("----------------------------------------------------------------------------------");
						System.out.println("\tEstado de contrato inválido.\n");
						
						System.out.print("Estado del contrato (activo, finalizado, cancelado): ");
					} else {
						break;
					}
				} else {
					break;
				}
			}
		}
		
		//Propietario de la casa en el contrato
		List<Document> propietarios = ListarPropietarios();
		String propietario = "";
				
		//Obtenemos el identificador del documento del propietario correspondiente en la colección para hacer la referencia
		Document consultaP;
		ObjectId _id;
		DBRef refPropietario = null;
				
		//Si no hay propietarios
		if(propietarios.size() == 0) {
			System.out.println("\tNo hay propietarios disponibles para asociar al mantenimiento.\n");
			propietario = "-1";
		} else {
			System.out.println(propietarios);
			System.out.print("\nPropietario en el contrato (DNI): ");
					
			while(propietario.length() == 0) {
				propietario = sc.nextLine();
						
				if(propietario.isBlank() && propietario.length() > 0) {
					propietario = "";
					System.out.println("----------------------------------------------------------------------------------");
					System.out.println("\tNo se ha introducido el propietario en el contrato.\n");

					System.out.print("Propietario en el contrato (DNI): ");
				} else {
					if(propietario.equals("-1")) break;
					
					//Comprobar que el DNI del propietario escrito exista.
					boolean existe = false;
					for(Document prop: propietarios) {
						if(prop.getString("DNI") != null) {
							if(prop.getString("DNI").equals(propietario)) {
								existe = true;
							}
						}
					}
					if(!existe && propietario.length() > 0) {
						propietario = "";
						
						System.out.println("----------------------------------------------------------------------------------");
						System.out.println("\tEl propietario introducido no existe.\n");
						
					} else if(propietario.length() > 0) {
						//Consultamos el propietario con el DNI introducido, si lo encuentra o es -1 salimos del bucle.
						consultaP = MongoConnection.propietarios.find(new Document("DNI", propietario)).first();
						_id = consultaP.getObjectId("_id");
						refPropietario = new DBRef("Propietarios", _id);
						
						if(consultaP != null || propietario.equals("-1")) {
							break;
						}
					}
					System.out.print("Propietario en el contrato (DNI): ");
				}
			}
		}
		
		//Agente inmobiliario encargado de la casa en el contrato
		List<Document> agentes = ListarAgentes();
		String agente = "";
		
		//Obtenemos el identificador del documento del agente inmobiliario correspondiente en la colección para hacer la referencia
		Document consultaAGI;
		DBRef refAgente = null;
		
		if(agentes.size() == 0) {
			System.out.println("\tNo hay agentes inmobiliarios disponibles para asociar al contrato.");
			agente = "-1";
		} else {
			System.out.print("\nAgente inmobiliario en el contrato (DNI): ");
			
			while(agente.length() == 0) {
				agente = sc.nextLine();
				
				if(agente.isBlank() && agente.length() > 0) {
					agente = "";
					System.out.println("----------------------------------------------------------------------------------");
					System.out.println("\tNo se ha introducido el agente inmobiliario en el contrato.\n");

					System.out.print("Agente inmobiliario en el contrato (DNI): ");
				} else {
					if(agente.equals("-1")) break;
					
					//Comprobar que el DNI del agente inmobiliario escrito exista.
					boolean existe = false;
					for(Document AGI: agentes) {
						if(AGI.getString("DNI") != null) {
							if(AGI.getString("DNI").equals(agente)) {
								existe = true;
							}
						}
					}
					if(!existe && agente.length() > 0) {
						agente = "";
						
						System.out.println("----------------------------------------------------------------------------------");
						System.out.println("\tEl agente inmobiliario introducido no existe.\n");
						
					} else if(agente.length() > 0) {
						//Consultamos el agente inmobiliario con el DNI introducido, si lo encuentra o es -1 salimos del bucle.
						consultaAGI = MongoConnection.agentes.find(new Document("DNI", agente)).first();
						_id = consultaAGI.getObjectId("_id");
						refAgente = new DBRef("Agentes_Inmobiliarios", _id);
						
						if(consultaAGI != null || agente.equals("-1")) {
							break;
						}
					}
					System.out.print("Agente inmobiliario en el contrato (DNI): ");
				}
			}
		}
		
		//Comprador de la casa en el contrato
		List<Document> compradores = ListarCompradores();
		String comprador = "";
		
		//Obtenemos el identificador del documento del comprador correspondiente en la colección para hacer la referencia
		Document consultaCOM;
		DBRef refComprador = null;
		
		if(compradores.size() == 0) {
			System.out.println("\n\tNo hay compradores disponibles para asociar al contrato.");
			comprador = "-1";
		} else {
			System.out.print("\nComprador en el contrato (DNI): ");
			
			while(comprador.length() == 0) {
				comprador = sc.nextLine();
				
				if(comprador.isBlank() && comprador.length() > 0) {
					comprador = "";
					System.out.println("----------------------------------------------------------------------------------");
					System.out.println("\tNo se ha introducido el comprador especificado en el contrato.\n");

					System.out.print("Comprador en el contrato (DNI): ");
				} else {
					if(comprador.equals("-1")) break;
					
					//Comprobar que el DNI del agente inmobiliario escrito exista.
					boolean existe = false;
					for(Document COM: compradores) {
						if(COM.getString("DNI") != null) {
							if(COM.getString("DNI").equals(comprador)) {
								existe = true;
							}
						}
					}
					if(!existe && comprador.length() > 0) {
						comprador = "";
						
						System.out.println("----------------------------------------------------------------------------------");
						System.out.println("\tEl comprador introducido no existe.\n");
						
					} else if(comprador.length() > 0) {
						//Consultamos el comprador con el DNI introducido, si lo encuentra o es -1 salimos del bucle.
						consultaCOM = MongoConnection.compradores.find(new Document("DNI", comprador)).first();
						_id = consultaCOM.getObjectId("_id");
						refComprador = new DBRef("Compradores", _id);
						
						if(consultaCOM != null || comprador.equals("-1")) {
							break;
						}
					}
					System.out.print("Comprador en el contrato (DNI): ");
				}
			}
		}
		
		//Casa especificada en el contrato
		Map<Integer, Document> casas = ListarCasas();
		int casa = 0;

		//Obtenemos el identificador del documento de la casa correspondiente en la colección para hacer la referencia
		DBRef refCasa = null;
		
		if(casas.size() == 0) {
			System.out.println("\n\tNo hay casas registradas por el momento.\n");
			casa = -1;
		} else {
			System.out.print("\nCasa especificada en el contrato (nº): ");
			
			while(true) {
				try {
					casa = sc.nextInt();
				} catch (InputMismatchException e) {
					sc.nextLine();
					System.out.println("----------------------------------------------------------------------------------");
					System.out.println("\tNº de casa inválido\n");
					
					System.out.print("Casa especificada en el contrato (nº): ");
					continue;
				}
				
				//Si el nº de la casa se ha introducido bien como tal, comprobamos que exista
				Document seleccionada = casas.get(casa);
				if(seleccionada != null) {
					//La casa existe
					_id = seleccionada.getObjectId("_id");
					refCasa = new DBRef("Casas", _id);
					
					break;
				} else {
					if(casa == -1) break;
					System.out.println("\n\tLa casa solicitada no existe.\n");
				}
			}
		}
		
		//Ya podemos insertar todos los datos del contrato.
		Document contrato = new Document();
		if(!tipo.equals("-1")) contrato.put("tipo", tipo);
		if(diaI != -1 && mesI != -1 && añoI != -1) contrato.put("fecha_inicio", fechaI);
		if(diaF != -1 && mesF != -1 && añoF != -1) contrato.put("fecha_fin", fechaF);
		if(!metodoPago.equals("-1")) contrato.put("metodo_pago", metodoPago);
		if(cantidad != -1) {
			if(tipoCantidad.equalsIgnoreCase("Total")) contrato.put("cantidad_total", cantidad);
			if(tipoCantidad.equalsIgnoreCase("Al mes")) contrato.put("cantidad_mes", cantidad);
		}
		if(!estado.equals("-1")) contrato.put("estado", estado);
		if(!propietario.equals("-1")) contrato.put("Propietario", refPropietario);
		if(!agente.equals("-1")) contrato.put("Agente", refAgente);
		if(!comprador.equals("-1")) contrato.put("Comprador", refComprador);
		if(casa != -1) contrato.put("Casa", refCasa);
		
		MongoConnection.contratos.insertOne(contrato);
		System.out.println("\nContrato registrado");
	}
}
