import java.util.*;


public class Main {
	static Scanner sc = new Scanner(System.in);
	
	public static void main(String[] args) {
		//Utilizamos nuestra propia clase personalizada para la conexión a la BD.
		MongoConnection.inicializarConexion();
		
		//Ya hemos terminado las configuraciones iniciales del programa, ahora creamos el menú.
		int opc = -1;
		while(opc != 0) {
			System.out.println("==========================================");
			System.out.println("\t\tMONGODB");
			System.out.println("==========================================\n");
			
			System.out.println("1. Insertar documentos a las colecciones");
			System.out.println("2. Revisar consultas predeterminadas");
			System.out.println("3. Revisar consultas con valores del usuario");
			System.out.println("4. Actualizar documentos para colecciones principales");
			System.out.println("5. Actualizar documentos para colecciones secundarias");
			System.out.println("6. Borrar documentos de colecciones secundarias");
			System.out.println("7. Borrar documentos de colecciones principales\n");
			
			System.out.println("==========================================");
			try {
				System.out.print("¿Qué acción desea realizar (0 para salir)?: ");
				opc = sc.nextInt();
			} catch (InputMismatchException e) {
				sc.nextLine();
				opc = -1;
			}
			System.out.println();
			
			
			switch(opc) {
			case 1:
				InsertarDocumentos();
				break;
			case 2:
				ConsultasPredeterminadas();
				break;
			case 3:
				ConsultasPatrones();
				break;
			case 4:
				ActualizarDocPrincipales();
				break;
			case 5:
				ActualizarDocSecundarias();
				break;
			case 6:
				BorrarDocSecundarias();
				break;
			case 7:
				BorrarDocPrincipales();
				break;
			default:
				if(opc < 0 || opc > 7) {
					System.out.println("---------------------------------------------------");
					System.out.println("ESA OPCIÓN NO EXISTE\n");
				}
				break;
			}
		}
		//Cerramos la conexión a la BD porque el usuario ha salido del programa.
		MongoConnection.cerrarConexion();
	}

	private static void InsertarDocumentos() {
		/*Método que permitirá al usuario insertar documentos en una colección, comprobamos que exista.
		  
		  Con nuestra clase para insertar documentos llamaremos a sus métodos estáticos para insertar 
		  cualquier dato.
		 */
		
		String coleccion_Existente = buscarColeccion("A MODIFICAR", null, null); //Especificamos como parámetro la acción que se va a realizar sobre la colección correspondiente como indicador al usuario.
		if(coleccion_Existente != null) {
			/*Para las referencias en cada colección en los métodos para listar los documentos de 
			  compradores, agentes y propietarios muestro el DNI y sus nombres completos para que el 
			  usuario elija a cada tipo de persona introduciendo su DNI.*/
			
			if(coleccion_Existente.equals("Contratos")) {
				InsertarDocumentos.RegistrarContratos();
			}
			
			if(coleccion_Existente.equals("Visitas")) {
				InsertarDocumentos.RegistrarVisitas();
			}
			
			if(coleccion_Existente.equals("Pagos")) {
				InsertarDocumentos.RegistrarPagos();
			}
			
			if(coleccion_Existente.equals("Propietarios")) {
				InsertarDocumentos.RegistrarPropietarios();
			}
			
			if(coleccion_Existente.equals("Compradores")) {
				InsertarDocumentos.RegistrarCompradores();
			}
			
			if(coleccion_Existente.equals("Agentes_Inmobiliarios")) {
				InsertarDocumentos.RegistrarAgentes();
			}
			
			if(coleccion_Existente.equals("Mantenimiento")) {
				InsertarDocumentos.RegistrarMantenimiento();
			}
			
			if(coleccion_Existente.equals("Casas")) {
				InsertarDocumentos.RegistrarCasas();
			}
			
			System.out.println();
		} else {
			System.out.println("\n\nLa colección introducida no existe.");
		}
	}

	private static void ConsultasPredeterminadas() {
		/*Método que permite al usuario ejecutar las consultas que haya por defecto y guardar su resultado en un fichero.
		  Utilizaremos una clase llamada ConsultasPredeterminadas que incluye métodos estáticos para cada
		  consulta predeterminada.
		*/
		int opc = -1;
		while(opc != 0) {
			System.out.println("\n\n==================================================================");
			System.out.println("\t\tCONSULTAS PREDETERMINADAS");
			System.out.println("==================================================================");
			
			System.out.println("1. Obtener el precio promedio de las casas disponibles.");
			System.out.println("2. Listar el número de casas por ciudad.");
			System.out.println("3. Obtener la dirección, ciudad, provincia, código postal y precio de la casa más cara.");
			System.out.println("4. Listar la dirección, el propietario, y la cantidad de habitaciones de las casas con más de 2 habitaciones.");
			System.out.println("5. Obtener el propietario con más casas registradas.");
			System.out.println("6. Listar los nombres de agentes ordenado desde el que más casas a su cargo al que menos.");
			System.out.println("7. Listar los tres métodos de pago más utilizados en orden ascendente.");
			System.out.println("8. Promedio de costo por intervenciones de mantenimiento por casa.");
			System.out.println("9. Intervenciones de mantenimiento listadas y ordenada por casa.");
			System.out.println("10. Listar casas que estén en alquiler ordenadas por provincia, ciudad y dirección.");
			System.out.println("11. Listar el total de ingresos generados por cada casa (colección de Pagos) ordenado de menor a mayor cantidad de dinero.");
			System.out.println("12. Listar los tres agentes con mayor número de contratos activos.");
			System.out.println("13. Ingresos anuales para cada alquiler activo.");
			System.out.println("14. Obtener el tiempo promedio en días de cierre de contratos de compraventa.");
			
			System.out.println("\n=================================");
			System.out.print("ID de la consulta (0 para salir): ");
			
			try {
				opc = sc.nextInt();
			} catch (InputMismatchException e) {
				sc.nextLine();
				opc = -1;
			}
			System.out.println();
			
			switch (opc) {
			case 1:
				ConsultasPredeterminadas.Consulta1();
				break;
			case 2:
				ConsultasPredeterminadas.Consulta2();
				break;
			case 3:
				ConsultasPredeterminadas.Consulta3();
				break;
			case 4:
				ConsultasPredeterminadas.Consulta4();
				break;
			case 5:
				ConsultasPredeterminadas.Consulta5();
				break;
			case 6:
				ConsultasPredeterminadas.Consulta6();
				break;
			case 7:
				ConsultasPredeterminadas.Consulta7();
				break;
			case 8:
				ConsultasPredeterminadas.Consulta8();
				break;
			case 9:
				ConsultasPredeterminadas.Consulta9();
				break;
			case 10:
				ConsultasPredeterminadas.Consulta10();
				break;
			case 11:
				ConsultasPredeterminadas.Consulta11();
				break;
			case 12:
				ConsultasPredeterminadas.Consulta12();
				break;
			case 13:
				ConsultasPredeterminadas.Consulta13();
				break;
			case 14:
				ConsultasPredeterminadas.Consulta14();
				break;
			default:
				if(opc < 0 || opc > 14) {
					System.out.println("\n---------------------------------------------------");
					System.out.println("ID de consulta inexistente\n");
				}
				break;
			}
			System.out.println();
		}
	}

	private static void ConsultasPatrones() {
		/*Método que permite al usuario ejecutar consultas en las que el mismo puede configurar algunos 
		  parámetros solicitados por teclado correspondientes a cada consulta.*/
		int opc = -1;
		while(opc != 0) {
			System.out.println("\n\n==================================================================");
			System.out.println("\t\tCONSULTAS CON VALORES DEL USUARIO");
			System.out.println("==================================================================");
			
			System.out.println("1. Listar todas las direcciones de las casas, la cantidad de baños\n"
					+ "y los nombres de sus propietarios para aquellas que tengan un número\n"
					+ "máximo de baños solicitado por teclado.\n");
			System.out.println("2. Listar todas las direcciones de las casas, la cantidad de habitaciones\n"
					+ "y los nombres de sus propietarios para aquellas que tengan un número mínimo\n"
					+ "de habitaciones solicitado por teclado.\n");
			System.out.println("3. Listar los compradores con un presupuesto mínimo solicitado por teclado.");
			System.out.println("4. Listar la cantidad de casas asociadas a un propietario solicitado por teclado\n"
					+ "(se introducirá su nombre).\n");
			System.out.println("5. Listar los contratos que han sido firmados en un año concreto.");
			System.out.println("6. Listar el total de visitas para un agente solicitado por teclado\n"
					+ " (se introducirá su nombre).\n");
			System.out.println("7. Número de contratos para un tipo solicitado por teclado.");
			System.out.println("8. Listar las reparaciones en un estado solicitado por teclado.");
			
			System.out.println("\n==================================================================");
			System.out.print("ID de la consulta (0 para salir): ");
			
			try {
				opc = sc.nextInt();
			} catch (InputMismatchException e) {
				sc.nextLine();
				opc = -1;
			}
			System.out.println();
			
			switch(opc) {
			case 1:
				ConsultasPatrones.Consulta1();
				break;
			case 2:
				ConsultasPatrones.Consulta2();
				break;
			case 3:
				ConsultasPatrones.Consulta3();
				break;
			case 4:
				ConsultasPatrones.Consulta4();
				break;
			case 5:
				ConsultasPatrones.Consulta5();
				break;
			case 6:
				ConsultasPatrones.Consulta6();
				break;
			case 7:
				ConsultasPatrones.Consulta7();
				break;
			case 8:
				ConsultasPatrones.Consulta8();
				break;
			default:
				if(opc < 0 || opc > 8) {
					System.out.println("\n---------------------------------------------------");
					System.out.println("ID de consulta inexistente\n");
				}
				break;
			}
			System.out.println();
		}
	}

	private static void ActualizarDocPrincipales() {
		/*Método que permitirá al usuario actualizar documentos para colecciones principales, se 
		  mostrará un menú con los nombres de todas las colecciones principales y el usuario tendrá que 
		  elegir una escribiendo su nombre. Se comprobará que existan todas las colecciones introducidas 
		  por teclado igualmente.
		  
		  Ahora, al método de buscar todas las colecciones "buscarColeccion()" se le pasarán 2 parámetros 
		  más: El tipo de colección (String) y una lista con los nombres de las colecciones principales
		  (List<String>) para controlar que sólo se muestren las colecciones principales.
		  
		  Para actualizar campos de documentos utilizaremos la clase ActualizarDocsPrincipales, con 
		  distintos métodos estáticos para cada colección.*/
		
		
		//Parámetros correspondientes a las búsquedas.
		String accion = "A MODIFICAR";
		String tipoColeccion = "Principales";
		
		List<String> coleccionesPrincipales = new ArrayList<>();
		coleccionesPrincipales.add("Propietarios");
		coleccionesPrincipales.add("Compradores");
		coleccionesPrincipales.add("Casas");
		coleccionesPrincipales.add("Agentes_Inmobiliarios");
		
		//Llamada al método para realizar la búsqueda de las colecciones y comprobar que exista la elegida.
		String coleccionExistente = buscarColeccion(accion, tipoColeccion, coleccionesPrincipales);
		if(coleccionExistente != null) {
			/*Si la colección introducida por teclado se ha encontrado, se comprobará que sea de una de 
			  las colecciones principales y se reutilizarán los métodos para listar todos los documentos 
			  de la clase InsertarDocumentos.
			  
			  Antes de empezar creamos una lista con los campos por los que el usuario puede elegir para 
			  filtrar por el que haya elegido.*/
			List<String> campos = new ArrayList<String>();
			
			if(coleccionExistente.equalsIgnoreCase("Propietarios")) {
				InsertarDocumentos.ListarPropietarios();
				
				//Pasamos los nombres de los campos a la lista y se los pasamos como parámetro al método.
				campos.add("nombre");
				campos.add("DNI");
				campos.add("telefono");
				campos.add("email");
				campos.add("direccion");
				
				//Pasamos como parámetro la lista y procedemos a la actualzación.
				ActualizarDocsPrincipales.ActualizarPropietarios(campos);
			}
			
			if(coleccionExistente.equalsIgnoreCase("Compradores")) {
				InsertarDocumentos.ListarCompradores();
				
				//Pasamos los nombres de los campos a la lista y se los pasamos como parámetro al método.
				campos.add("nombre");
				campos.add("DNI");
				campos.add("telefono");
				campos.add("email");
				campos.add("presupuesto");
				campos.add("preferencias");
				
				//Pasamos como parámetro la lista y procedemos a la actualzación.
				ActualizarDocsPrincipales.ActualizarCompradores(campos);
			}
			
			if(coleccionExistente.equalsIgnoreCase("Casas")) {
				InsertarDocumentos.ListarCasas();
				
				//Pasamos los nombres de los campos a la lista y se los pasamos como parámetro al método.
				campos.add("tipo");
				campos.add("direccion");
				campos.add("ciudad");
				campos.add("provincia");
				campos.add("codigo_postal");
				campos.add("tamano");
				campos.add("habitaciones");
				campos.add("banos");
				campos.add("precio");
				campos.add("alquiler");
				campos.add("estado");
				campos.add("propietario");
				campos.add("agente");
				campos.add("comprador");
				
				//Pasamos como parámetro la lista y procedemos a la actualzación
				ActualizarDocsPrincipales.ActualizarCasas(campos);
			}
			
			if(coleccionExistente.equalsIgnoreCase("Agentes_Inmobiliarios")) {
				InsertarDocumentos.ListarAgentes();
				
				//Pasamos los nombres de los campos a la lista y se los pasamos como parámetro al método.
				campos.add("nombre");
				campos.add("DNI");
				campos.add("telefono");
				campos.add("email");
				campos.add("licencia");
				
				//Pasamos como parámetro la lista y procedemos a la actualzación.
				ActualizarDocsPrincipales.ActualizarAgentes(campos);
			}
			
		} else {
			System.out.println("\tNo existe la colección especificada por teclado.\n");
		}
	}
	
	private static void ActualizarDocSecundarias() {
		/*Método que permitirá al usuario actualizar documentos para colecciones secundarias, se 
		  mostrará un menú con los nombres de todas las colecciones secundarias y el usuario tendrá que 
		  elegir una escribiendo su nombre. Se comprobará que existan todas las colecciones introducidas 
		  por teclado igualmente.
		  
		  Ahora, al método de buscar todas las colecciones "buscarColeccion()" se le pasarán 2 parámetros 
		  más: El tipo de colección (String) y una lista con los nombres de las colecciones secundarias
		  (List<String>) para controlar que sólo se muestren las colecciones secundarias.
		  
		  Para actualizar campos de documentos utilizaremos la clase ActualizarDocsSecundarios, con 
		  distintos métodos estáticos para cada colección.*/
		
		
		//Parámetros correspondientes a las búsquedas.
		String accion = "A MODIFICAR";
		String tipoColeccion = "Secundarias";
		
		List<String> coleccionesSecundarias = new ArrayList<>();
		coleccionesSecundarias.add("Contratos");
		coleccionesSecundarias.add("Pagos");
		coleccionesSecundarias.add("Mantenimiento");
		coleccionesSecundarias.add("Visitas");
		
		//Llamada al método para realizar la búsqueda de las colecciones y comprobar que exista la elegida.
		String coleccionExistente = buscarColeccion(accion, tipoColeccion, coleccionesSecundarias);
		if(coleccionExistente != null) {
			if(coleccionExistente.equalsIgnoreCase("Contratos")) {
				ActualizarDocsSecundarios.ActualizarContratos();
			}
			
			if(coleccionExistente.equalsIgnoreCase("Pagos")) {
				ActualizarDocsSecundarios.ActualizarPagos();
			}
			
			if(coleccionExistente.equalsIgnoreCase("Mantenimiento")) {
				ActualizarDocsSecundarios.ActualizarMantenimientos();
			}
			
			if(coleccionExistente.equalsIgnoreCase("Visitas")) {
				ActualizarDocsSecundarios.ActualizarVisitas();
			}
			System.out.println();
		} else {
			System.out.println("\tNo existe la colección especificada por teclado.\n");
		}
	}
	
	private static void BorrarDocSecundarias() {
		/*Método que permitirá al usuario eliminar documentos de colecciones secundarias, se mostrará un 
		  menú con los nombres de todas las colecciones secundarias y el usuario tendrá que elegir una 
		  escribiendo su nombre. Se comprobará que existan todas las colecciones introducidas por teclado
		  igualmente.
		  
		  Ahora, al método de buscar todas las colecciones "buscarColeccion()" se le pasarán 2 parámetros 
		  más: El tipo de colección (String) y una lista con los nombres de las colecciones secundarias
		  (List<String>) para controlar que sólo se muestren las colecciones secundarias.
		  
		  Para borrar documentos de las colecciones secundarias utilizaremos los diferentes métodos 
		  estáticos de la clase BorrarDocsSecundarios.*/
		
		
		//Parámetros correspondientes a las búsquedas.
		String accion = "A ELIMINAR";
		String tipoColeccion = "Secundarias";
		
		List<String> coleccionesSecundarias = new ArrayList<>();
		coleccionesSecundarias.add("Contratos");
		coleccionesSecundarias.add("Pagos");
		coleccionesSecundarias.add("Mantenimiento");
		coleccionesSecundarias.add("Visitas");
		
		//Llamada al método para realizar la búsqueda de las colecciones y comprobar que exista la elegida.
		String coleccionExistente = buscarColeccion(accion, tipoColeccion, coleccionesSecundarias);
		if(coleccionExistente != null) {
			if(coleccionExistente.equalsIgnoreCase("Contratos")) {
				BorrarDocsSecundarios.BorrarContratos();
			}
			
			if(coleccionExistente.equalsIgnoreCase("Pagos")) {
				BorrarDocsSecundarios.BorrarPagos();
			}
			
			if(coleccionExistente.equalsIgnoreCase("Mantenimiento")) {
				BorrarDocsSecundarios.BorrarMantenimientos();
			}
			
			if(coleccionExistente.equalsIgnoreCase("Visitas")) {
				BorrarDocsSecundarios.BorrarVisitas();
			}
			System.out.println();
		} else {
			System.out.println("\tNo existe la colección especificada por teclado.\n");
		}
	}
	
	private static void BorrarDocPrincipales() {
		/*Método que permitirá al usuario eliminar documentos de colecciones principales, se mostrará un 
		  menú con los nombres de todas las colecciones principales y el usuario tendrá que elegir una 
		  escribiendo su nombre. Se comprobará que existan todas las colecciones introducidas por teclado
		  igualmente.
		  
		  Ahora, al método de buscar todas las colecciones "buscarColeccion()" se le pasarán 2 parámetros 
		  más: El tipo de colección (String) y una lista con los nombres de las colecciones principales
		  (List<String>) para controlar que sólo se muestren las colecciones principales.
		  
		  Para borrar documentos de las colecciones principales utilizaremos los diferentes métodos 
		  estáticos de la clase BorrarDocsPrincipales.*/
		
		//Parámetros correspondientes a las búsquedas.
		String accion = "A ELIMINAR";
		String tipoColeccion = "Principales";
		
		List<String> coleccionesPrincipales = new ArrayList<>();
		coleccionesPrincipales.add("Propietarios");
		coleccionesPrincipales.add("Compradores");
		coleccionesPrincipales.add("Casas");
		coleccionesPrincipales.add("Agentes_Inmobiliarios");
		
		//Llamada al método para realizar la búsqueda de las colecciones y comprobar que exista la elegida.
		String coleccionExistente = buscarColeccion(accion, tipoColeccion, coleccionesPrincipales);
		if(coleccionExistente != null) {
			if(coleccionExistente.equalsIgnoreCase("Propietarios")) {
				BorrarDocsPrincipales.BorrarPropietarios();
			}
			
			if(coleccionExistente.equalsIgnoreCase("Compradores")) {
				BorrarDocsPrincipales.BorrarCompradores();
			}
			
			if(coleccionExistente.equalsIgnoreCase("Casas")) {
				BorrarDocsPrincipales.BorrarCasas();
			}
			
			if(coleccionExistente.equalsIgnoreCase("Agentes_Inmobiliarios")) {
				BorrarDocsPrincipales.BorrarAgentes();
			}
		}
	}

	private static String buscarColeccion(String accion, String tipoColeccion, List<String> colecciones_listaTipo) {
		/*Método que comprobará si existe una colección introducida por el usuario.
		 
		  1º) Mostrar todas las colecciones disponibles de la BD y guardarlas en listas para controlar 
		      que la colección que se introduzca exista y mostrarlas más de una vez.
		*/
		if(tipoColeccion != null && colecciones_listaTipo != null) {
			/*Si no se van a mostrar todas las colecciones, entonces se buscarán las colecciones 
			  del tipo de ellas pasado como parámetro para listarlas y que el usuario pueda elegir una 
			  como en el caso contrario.*/
			
			Iterator<String> it = MongoConnection.db.listCollectionNames().iterator();
			List<String> colecciones = new ArrayList<>(); //Lista para comprobar que la colección escrita exista.
			List<String> listaColecciones = new ArrayList<>(); //Lista utilizada para mostrar las colecciones disponibles por pantalla con un formato.
			
			int numero = 0; //Contador para ir sumando el nº de colecciones que hay y así organizarlas.
			while(it.hasNext()) {
				String coleccion = it.next();
				
				//Controlamos cada caso según el tipo de colecciones que sean a mostrar.
				/*Si el tipo de colecciones es uno de los existentes mostramos las de este tipo y 
				  comprobamos si cada colección recorrida está o no en cada lista de colecciones de
				  cada tipo.*/
				if(tipoColeccion.equalsIgnoreCase("Principales") || tipoColeccion.equalsIgnoreCase("Principal")) {
					//Comprobamos que cada colección recorrida por el bucle sea del tipo correspondiente.
					boolean esPrincipal = false;
					for(String coleccionP: colecciones_listaTipo) {
						if(coleccion.equalsIgnoreCase(coleccionP)) {
							esPrincipal = true;
							break;
						}
					}
					if(esPrincipal) {
						colecciones.add(coleccion);
						listaColecciones.add(++numero + ". " + coleccion);
					}
				} else if(tipoColeccion.equalsIgnoreCase("Secundarias") || tipoColeccion.equalsIgnoreCase("Secundaria")) {
					//Comprobamos que cada colección recorrida por el bucle sea del tipo correspondiente.
					boolean esSecundaria = false;
					for(String coleccionS: colecciones_listaTipo) {
						if(coleccion.equalsIgnoreCase(coleccionS)) {
							esSecundaria = true;
							break;
						}
					}
					if(esSecundaria) {
						colecciones.add(coleccion);
						listaColecciones.add(++numero + ". " + coleccion);
					}
				}
			}
			
			/*Proceso de selección de la colección. Con un booleano controlamos la existencia de 
			  cada colección introducida, se inicializa a false porque no se ha buscado ninguna.
			*/
			System.out.println("=========================================================================");
			System.out.println("\t\tCOLECCIÓN EN LA QUE SE ENCUENTRA EL DOCUMENTO " + accion);
			System.out.println("=========================================================================\n");
			
			//Mostrar todas las colecciones disponibles.
			for(String coleccionDisponible : listaColecciones) {
				System.out.println(coleccionDisponible);
			}
			System.out.println("\n=====================================================");
		
			//Hora de introducir el nombre de la colección.
			System.out.print("Introduce el nombre de una de las colecciones: ");
			String coleccion = sc.next();
			
			//Comprobar ahora si la colección existe.
			for(String coleccionDisponible : colecciones) {
				if(coleccionDisponible.equals(coleccion)) {
					return coleccion;
				}
			}
		} else {
			Iterator<String> it = MongoConnection.db.listCollectionNames().iterator();
			List<String> colecciones = new ArrayList<>(); //Lista para comprobar que la colección escrita exista.
			List<String> listaColecciones = new ArrayList<>(); //Lista utilizada para mostrar las colecciones disponibles por pantalla con un formato.
			
			for(int i=1; it.hasNext(); i++) {
				String coleccion = it.next();
				int numero = i;
				
				colecciones.add(coleccion);
				listaColecciones.add(numero + ". " + coleccion);
			}
			
			/*Proceso de selección de la colección. Con un booleano controlamos la existencia de 
			  cada colección introducida, se inicializa a false porque no se ha buscado ninguna.
			*/
			System.out.println("=========================================================================");
			System.out.println("\t\tCOLECCIÓN EN LA QUE SE ENCUENTRA EL DOCUMENTO " + accion);
			System.out.println("=========================================================================\n");
			
			//Mostrar todas las colecciones disponibles.
			for(String coleccionDisponible : listaColecciones) {
				System.out.println(coleccionDisponible);
			}
			System.out.println("\n=====================================================");
		
			//Hora de introducir el nombre de la colección.
			System.out.print("Introduce el nombre de una de las colecciones: ");
			String coleccion = sc.next();
			
			//Comprobar ahora si la colección existe.
			for(String coleccionDisponible : colecciones) {
				if(coleccionDisponible.equals(coleccion)) {
					return coleccion;
				}
			}
		}
		return null;
	}

}
