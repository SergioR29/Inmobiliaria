import com.mongodb.*;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.*;
import static com.mongodb.client.model.Aggregates.*;

import org.bson.*;
import org.bson.conversions.Bson;
import org.bson.types.*;

import java.util.*;


public class ActualizarDocsPrincipales {
	static Scanner sc = new Scanner(System.in);
	
	public static void ActualizarPropietarios(List<String> campos) {
		//Método que permitirá al usuario actualizar campos de un documento de la colección de propietarios.
		
		//1º) Vamos a preguntar al usuario el campo por el que vamos a filtrar y comprobar que exista.
		String campo = campoFiltrar(campos);
		if(campo.length() > 0) {
			/*Si el campo pertenece a la colección entonces se preguntará la condición que el valor debe 
			tomar.*/
			
			String valorCondicion = valorCondicion();
			if(valorCondicion.length() > 0) {
				/*Ahora, una vez sabemos la condición que el valor debe tomar preguntamos al usuario 
				  según el campo de la colección que se haya escogido.*/
				
				System.out.println("\nVALOR QUE DEBE TOMAR EL CAMPO SELECCIONADO");
				System.out.println("==========================================\n");
				
				/*Si la condición es que el campo a filtrar exista significa que no tiene que tomar 
				ningún valor a la hora de la filtración.*/
				
				if(valorCondicion.equalsIgnoreCase("Que el campo exista")) {
					boolean valor = true; //El valor será un booleano a true si se llega aquí.
					
					camposModificarPropietarios(null, valor, campo, campos, valorCondicion);
				} else {
					String[] valor = valorFiltroPropietarios(campo);
					if(valor.length > 0) {
						camposModificarPropietarios(valor[0], false, campo, campos, valorCondicion);
					}
				}
				System.out.println("\n==========================================\n");
			}
		}
	}
	
	public static void camposModificarPropietarios(String valorFiltro, boolean condExista, String campoFiltro, List<String> campos, String valorCondicion) {
		//Método que permite al usuario elegir los campos que va a modificar del propietario.
		System.out.println("CAMPO QUE SE VA A MODIFICAR");
		System.out.println("===========================\n");
		
		for(int i=0; i < campos.size(); i++) {
			String campo = campos.get(i);
			System.out.println((i+1) + ". " + campo);
		}
		System.out.println("\n=========================");
		
		
		//Pedir campos que se van a modificar.
		String modificarCampos = "";
		System.out.print("¿Desea seguir modificando campos? (S/N): ");
		
		while(modificarCampos.length() <= 1) {
			modificarCampos = sc.nextLine();
			
			if(modificarCampos.isBlank() && modificarCampos.length() > 0) {
				modificarCampos = "";
				System.out.println("-------------------------------------------------------------------");
				System.out.println("\t- No se ha especificado si se quiere modificar campos o no.\n");
				
			} else if(modificarCampos.length() > 0) {
				if(!modificarCampos.equals("S") && !modificarCampos.equals("N")) {
					System.out.println("-------------------------------------------------------------------");
					System.out.println("\t- No se ha especificado si se quiere modificar campos o no.\n");
				} else {
					break;
				}
			}
		}
		
		String campo = "";
		if(modificarCampos.equals("S")) {
			System.out.print("Campo a modificar (su nombre y no el 'nº'): ");
			
			while(campo.length() == 0) {
				campo = sc.nextLine();
				
				if(campo.isBlank() && campo.length() > 0) {
					campo = "";
					System.out.println("-------------------------------------------------------------------");
					System.out.println("\t- No se ha introducido el nombre del campo a modficar.\n");
					
					System.out.print("Campo a modificar (su nombre y no el 'nº'): ");
					
				} else if(campo.length() > 0) {
					//Si se ha introducido el campo vamos a comprobar que exista.
					boolean existe = false;
					for(String campoC: campos) {
						if(campoC.equalsIgnoreCase(campo)) {
							existe = true;
							break;
						}
					}
					
					if(existe) {//Si el campo existe salimos del bucle.
						break;
					} else {
						campo = "";
						System.out.println("\n-------------------------------------------------------------------");
						System.out.println("\t\t- Campo inexistente.\n\n");
						
						System.out.print("Campo a modificar (su nombre y no el nº): ");
					}
				}
			}
			
			//Campos
			String[] nombreCompleto = new String[3]; //Nombre completo (nombre + apellido1 + apellido2)
			String DNI = "";
			String telefono = "";
			String correo = "";
			String direccion = "";
			
			if(campo.equalsIgnoreCase("nombre")) {
				//Después de tener claro el campo a modificar pedimos los valores a sustituir de los docs.
				Map<String, List<String>> valores = valoresSustituirNombres(campo);
				
				if(valores.size() > 0) {
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
					
					/*Para actualizar los documentos correspondientes a este campo recorremos los valores
					  seleccionados para sustituir.*/
					UpdateResult resultado = null;
					
					Collection<List<String>> setKV = valores.values();
					for(Iterator<List<String>> it = setKV.iterator(); it.hasNext();) {
						List<String> valoresSustituir = it.next();
						
						for(int i=0; i < valoresSustituir.size(); i++) {
							//Evaluamos que la actualización se ajuste a las condiciones.
							if(valorCondicion.equalsIgnoreCase("Igual a")) {
								Bson filter = Filters.and(
										Filters.eq(campoFiltro, valorFiltro),
										Filters.eq("nombre", valoresSustituir.get(i)));
								
								Bson update = Updates.set("nombre", Arrays.asList(nombreCompleto));
								resultado = MongoConnection.propietarios.updateMany(
										filter, 
										update);
							}
							
							if(valorCondicion.equalsIgnoreCase("Mayor que")) {
								Bson filter = Filters.and(
										Filters.gt(campoFiltro, valorFiltro),
										Filters.eq("nombre", valoresSustituir.get(i)));
								
								Bson update = Updates.set("nombre", Arrays.asList(nombreCompleto));
								resultado = MongoConnection.propietarios.updateMany(
										filter, 
										update);
							}
							
							if(valorCondicion.equalsIgnoreCase("Menor que")) {
								Bson filter = Filters.and(
										Filters.lt(campoFiltro, valorFiltro),
										Filters.eq("nombre", valoresSustituir.get(i)));
								
								Bson update = Updates.set("nombre", Arrays.asList(nombreCompleto));
								resultado = MongoConnection.propietarios.updateMany(
										filter, 
										update);
							}
							
							if(condExista) {
								Bson filter = Filters.and(
										Filters.exists(campoFiltro),
										Filters.eq("nombre", valoresSustituir.get(i)));
								
								Bson update = Updates.set("nombre", Arrays.asList(nombreCompleto));
								resultado = MongoConnection.propietarios.updateMany(
										filter, 
										update);
							}
						}
					}
					if(resultado.getModifiedCount() > 0) {
						System.out.println("Nombres actualizados. (" + resultado.getModifiedCount() + ")");
						
						MongoCursor<Document> documentos = MongoConnection.propietarios.aggregate(Arrays.asList(
								match(new Document("nombre", Arrays.asList(nombreCompleto))))).iterator();
						
						System.out.println("\nPROPIETARIOS ACTUALIZADOS");
						System.out.println("=========================\n");
						while(documentos.hasNext()) {
							System.out.println(documentos.next().toJson());
						}
						System.out.println();
					}
				}
			}
			
			if(campo.equals("DNI")) {
				//Después de tener claro el campo a modificar pedimos los valores a sustituir de los docs.
				Map<String, List<String>> valores = valoresSustituirDNIs(campo);
				
				if(valores.size() > 0) {
					System.out.print("DNI: ");
					
					while(DNI.length() == 0) {
						DNI = sc.nextLine();
										
						if(DNI.isBlank() && DNI.length() > 0) {
							DNI = "";
							System.out.println("----------------------------------------------------------------------------------");
							System.out.println("\tNo se ha introducido el DNI del propietario.\n");

							System.out.print("DNI: ");
							
						} else if(DNI.length() > 0) {
							break;
						}
					}
					
					/*Para actualizar los documentos correspondientes a este campo recorremos los valores
					  seleccionados para sustituir.*/
					UpdateResult resultado = null;
					
					Collection<List<String>> setKV = valores.values();
					for(Iterator<List<String>> it = setKV.iterator(); it.hasNext();) {
						List<String> valoresSustituir = it.next();
						
						for(int i=0; i < valoresSustituir.size(); i++) {
							//Evaluamos que la actualización se ajuste a las condiciones.
							if(valorCondicion.equalsIgnoreCase("Igual a")) {
								Bson filter = Filters.and(
										Filters.eq(campoFiltro, valorFiltro),
										Filters.eq("DNI", valoresSustituir.get(i)));
								
								Bson update = Updates.set("DNI", DNI);
								resultado = MongoConnection.propietarios.updateMany(
										filter, 
										update);
							}
							
							if(valorCondicion.equalsIgnoreCase("Mayor que")) {
								Bson filter = Filters.and(
										Filters.gt(campoFiltro, valorFiltro),
										Filters.eq("DNI", valoresSustituir.get(i)));
								
								Bson update = Updates.set("DNI", DNI);
								resultado = MongoConnection.propietarios.updateMany(
										filter, 
										update);
							}
							
							if(valorCondicion.equalsIgnoreCase("Menor que")) {
								Bson filter = Filters.and(
										Filters.lt(campoFiltro, valorFiltro),
										Filters.eq("DNI", valoresSustituir.get(i)));
								
								Bson update = Updates.set("DNI", DNI);
								resultado = MongoConnection.propietarios.updateMany(
										filter, 
										update);
							}
							
							if(condExista) {
								Bson filter = Filters.and(
										Filters.exists(campoFiltro),
										Filters.eq("DNI", valoresSustituir.get(i)));
								
								Bson update = Updates.set("DNI", DNI);
								resultado = MongoConnection.propietarios.updateMany(
										filter, 
										update);
							}
						}
					}
					if(resultado.getModifiedCount() > 0) {
						System.out.println("DNIs actualizados. (" + resultado.getModifiedCount() + ")");
						
						MongoCursor<Document> documentos = MongoConnection.propietarios.aggregate(Arrays.asList(
								match(new Document("DNI", DNI)))).iterator();
						
						System.out.println("\nPROPIETARIOS ACTUALIZADOS");
						System.out.println("=========================\n");
						while(documentos.hasNext()) {
							System.out.println(documentos.next().toJson());
						}
						System.out.println();
					}
				}
			}
			
			if(campo.equals("telefono")) {
				//Después de tener claro el campo a modificar pedimos los valores a sustituir de los docs.
				Map<String, List<String>> valores = valoresSustituirTelefonos(campo);
				
				if(valores.size() > 0) {
					System.out.print("NºTeléfono: ");
					
					while(telefono.length() == 0) {
						telefono = sc.nextLine();
												
						if(telefono.isBlank() && telefono.length() > 0) {
							telefono = "";
							System.out.println("----------------------------------------------------------------------------------");
							System.out.println("\tNo se ha introducido el nºde teléfono del propietario.\n");

							System.out.print("NºTeléfono: ");
							
						} else if(telefono.length() > 0) {
							break;
						}
					}
					
					/*Para actualizar los documentos correspondientes a este campo recorremos los valores
					  seleccionados para sustituir.*/
					UpdateResult resultado = null;
					
					Collection<List<String>> setKV = valores.values();
					for(Iterator<List<String>> it = setKV.iterator(); it.hasNext();) {
						List<String> valoresSustituir = it.next();
						
						for(int i=0; i < valoresSustituir.size(); i++) {
							//Evaluamos que la actualización se ajuste a las condiciones.
							if(valorCondicion.equalsIgnoreCase("Igual a")) {
								Bson filter = Filters.and(
										Filters.eq(campoFiltro, valorFiltro),
										Filters.eq("telefono", valoresSustituir.get(i)));
								
								Bson update = Updates.set("telefono", telefono);
								resultado = MongoConnection.propietarios.updateMany(
										filter, 
										update);
							}
							
							if(valorCondicion.equalsIgnoreCase("Mayor que")) {
								Bson filter = Filters.and(
										Filters.gt(campoFiltro, valorFiltro),
										Filters.eq("telefono", valoresSustituir.get(i)));
								
								Bson update = Updates.set("telefono", telefono);
								resultado = MongoConnection.propietarios.updateMany(
										filter, 
										update);
							}
							
							if(valorCondicion.equalsIgnoreCase("Menor que")) {
								Bson filter = Filters.and(
										Filters.lt(campoFiltro, valorFiltro),
										Filters.eq("telefono", valoresSustituir.get(i)));
								
								Bson update = Updates.set("telefono", telefono);
								resultado = MongoConnection.propietarios.updateMany(
										filter, 
										update);
							}
							
							if(condExista) {
								Bson filter = Filters.and(
										Filters.exists(campoFiltro),
										Filters.eq("telefono", valoresSustituir.get(i)));
								
								Bson update = Updates.set("telefono", telefono);
								resultado = MongoConnection.propietarios.updateMany(
										filter, 
										update);
							}
						}
					}
					if(resultado.getModifiedCount() > 0) {
						System.out.println("Nºos de Teléfonos actualizados. (" + resultado.getModifiedCount() + ")");
						
						MongoCursor<Document> documentos = MongoConnection.propietarios.aggregate(Arrays.asList(
								match(new Document("telefono", telefono)))).iterator();
						
						System.out.println("\nPROPIETARIOS ACTUALIZADOS");
						System.out.println("=========================\n");
						while(documentos.hasNext()) {
							System.out.println(documentos.next().toJson());
						}
						System.out.println();
					}
				}
			}
			
			if(campo.equals("email")) {
				//Después de tener claro el campo a modificar pedimos los valores a sustituir de los docs.
				Map<String, List<String>> valores = valoresSustituirEmails(campo);
				
				if(valores.size() > 0) {
					System.out.print("Correo electrónico: ");
					
					while(correo.length() == 0) {
						correo = sc.nextLine();
												
						if(correo.isBlank() && correo.length() > 0) {
							correo = "";
							System.out.println("----------------------------------------------------------------------------------");
							System.out.println("\tNo se ha introducido el correo del propietario.\n");
							
							System.out.print("Correo electrónico: ");
							
						} else if(correo.length() > 0) {
							break;
						}
					}
					
					/*Para actualizar los documentos correspondientes a este campo recorremos los valores
					  seleccionados para sustituir.*/
					UpdateResult resultado = null;
					
					Collection<List<String>> setKV = valores.values();
					for(Iterator<List<String>> it = setKV.iterator(); it.hasNext();) {
						List<String> valoresSustituir = it.next();
						
						for(int i=0; i < valoresSustituir.size(); i++) {
							//Evaluamos que la actualización se ajuste a las condiciones.
							if(valorCondicion.equalsIgnoreCase("Igual a")) {
								Bson filter = Filters.and(
										Filters.eq(campoFiltro, valorFiltro),
										Filters.eq("email", valoresSustituir.get(i)));
								
								Bson update = Updates.set("email", correo);
								resultado = MongoConnection.propietarios.updateMany(
										filter, 
										update);
							}
							
							if(valorCondicion.equalsIgnoreCase("Mayor que")) {
								Bson filter = Filters.and(
										Filters.gt(campoFiltro, valorFiltro),
										Filters.eq("email", valoresSustituir.get(i)));
								
								Bson update = Updates.set("email", correo);
								resultado = MongoConnection.propietarios.updateMany(
										filter, 
										update);
							}
							
							if(valorCondicion.equalsIgnoreCase("Menor que")) {
								Bson filter = Filters.and(
										Filters.lt(campoFiltro, valorFiltro),
										Filters.eq("email", valoresSustituir.get(i)));
								
								Bson update = Updates.set("email", correo);
								resultado = MongoConnection.propietarios.updateMany(
										filter, 
										update);
							}
							
							if(condExista) {
								Bson filter = Filters.and(
										Filters.exists(campoFiltro),
										Filters.eq("email", valoresSustituir.get(i)));
								
								Bson update = Updates.set("email", correo);
								resultado = MongoConnection.propietarios.updateMany(
										filter, 
										update);
							}
						}
					}
					if(resultado.getModifiedCount() > 0) {
						System.out.println("Emails actualizados. (" + resultado.getModifiedCount() + ")");

						MongoCursor<Document> documentos = MongoConnection.propietarios.aggregate(Arrays.asList(
								match(new Document("email", correo)))).iterator();
						
						System.out.println("\nPROPIETARIOS ACTUALIZADOS");
						System.out.println("=========================\n");
						while(documentos.hasNext()) {
							System.out.println(documentos.next().toJson());
						}
						System.out.println();
					}
				}
			}
			
			if(campo.equals("direccion")) {
				//Después de tener claro el campo a modificar pedimos los valores a sustituir de los docs.
				Map<String, List<String>> valores = valoresSustituirDirecciones(campo);
				
				if(valores.size() > 0) {
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
					
					/*Para actualizar los documentos correspondientes a este campo recorremos los valores
					  seleccionados para sustituir.*/
					UpdateResult resultado = null;
					
					Collection<List<String>> setKV = valores.values();
					for(Iterator<List<String>> it = setKV.iterator(); it.hasNext();) {
						List<String> valoresSustituir = it.next();
						
						for(int i=0; i < valoresSustituir.size(); i++) {
							//Evaluamos que la actualización se ajuste a las condiciones.
							if(valorCondicion.equalsIgnoreCase("Igual a")) {
								Bson filter = Filters.and(
										Filters.eq(campoFiltro, valorFiltro),
										Filters.eq("direccion", valoresSustituir.get(i)));
								
								Bson update = Updates.set("direccion", direccion);
								resultado = MongoConnection.propietarios.updateMany(
										filter, 
										update);
							}
							
							if(valorCondicion.equalsIgnoreCase("Mayor que")) {
								Bson filter = Filters.and(
										Filters.gt(campoFiltro, valorFiltro),
										Filters.eq("direccion", valoresSustituir.get(i)));
								
								Bson update = Updates.set("direccion", direccion);
								resultado = MongoConnection.propietarios.updateMany(
										filter, 
										update);
							}
							
							if(valorCondicion.equalsIgnoreCase("Menor que")) {
								Bson filter = Filters.and(
										Filters.lt(campoFiltro, valorFiltro),
										Filters.eq("direccion", valoresSustituir.get(i)));
								
								Bson update = Updates.set("direccion", direccion);
								resultado = MongoConnection.propietarios.updateMany(
										filter, 
										update);
							}
							
							if(condExista) {
								Bson filter = Filters.and(
										Filters.exists(campoFiltro),
										Filters.eq("direccion", valoresSustituir.get(i)));
								
								Bson update = Updates.set("direccion", direccion);
								resultado = MongoConnection.propietarios.updateMany(
										filter, 
										update);
							}
						}
					}
					if(resultado.getModifiedCount() > 0) {
						System.out.println("Direcciones actualizadas. (" + resultado.getModifiedCount() + ")");

						MongoCursor<Document> documentos = MongoConnection.propietarios.aggregate(Arrays.asList(
								match(new Document("direccion", direccion)))).iterator();
						
						System.out.println("\nPROPIETARIOS ACTUALIZADOS");
						System.out.println("=========================\n");
						while(documentos.hasNext()) {
							System.out.println(documentos.next().toJson());
						}
						System.out.println();
					}
				}
			}
		}
	}

	public static String[] valorFiltroPropietarios(String campo) {
		//Método que permitirá al usuario establecer el valor del campo por el que se quiere filtrar a los propietarios.
		String[] valor = new String[1]; //Valor de retorno, es un array por el nombre completo.
		
		//Campos
		String[] nombreCompleto = new String[3]; //Nombre completo (nombre + apellido1 + apellido2)
		String DNI = "";
		String telefono = "";
		String correo = "";
		String direccion = "";
		
		if(campo.equalsIgnoreCase("nombre")) {
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
			
			return nombreCompleto;
		}
		
		if(campo.equalsIgnoreCase("DNI")) {
			//DNI
			System.out.print("DNI: ");
			
			while(DNI.length() == 0) {
				DNI = sc.nextLine();
								
				if(DNI.isBlank() && DNI.length() > 0) {
					DNI = "";
					System.out.println("----------------------------------------------------------------------------------");
					System.out.println("\tNo se ha introducido el DNI del propietario.\n");
	
					System.out.print("DNI: ");
				}
			}
			valor[0] = DNI;
		}
		
		if(campo.equalsIgnoreCase("telefono")) {
			//Teléfono
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
			valor[0] = telefono;
		}
		
		if(campo.equalsIgnoreCase("email")) {
			//Email
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
			valor[0] = correo;
		}
		
		if(campo.equalsIgnoreCase("direccion")) {
			//Dirección de la casa, es una cadena con el nombre de la calle y su número.
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
			valor[0] = direccion;
		}
		return valor;
	}

	public static void ActualizarAgentes(List<String> campos) {
		//Método que permitirá al usuario actualizar campos de un documento de la colección de agentes.
		
		//1º) Vamos a preguntar al usuario el campo por el que vamos a filtrar y comprobar que exista.
		String campo = campoFiltrar(campos);
		if(campo.length() > 0) {
			/*Si el campo pertenece a la colección entonces se preguntará la condición que el valor debe 
			tomar.*/
			
			String valorCondicion = valorCondicion();
			if(valorCondicion.length() > 0) {
				/*Ahora, una vez sabemos la condición que el valor debe tomar preguntamos al usuario 
				  según el campo de la colección que se haya escogido.*/
				
				System.out.println("\nVALOR QUE DEBE TOMAR EL CAMPO SELECCIONADO");
				System.out.println("==========================================\n");
				
				/*Si la condición es que el campo a filtrar exista significa que no tiene que tomar 
				ningún valor a la hora de la filtración.*/
				
				if(valorCondicion.equalsIgnoreCase("Que el campo exista")) {
					boolean valor = true; //El valor será un booleano a true si se llega aquí.
					
					camposModificarAgentes(null, valor, campo, campos, valorCondicion);
				} else {
					String[] valor = valorFiltroAgentes(campo);
					if(valor.length > 0) {
						camposModificarAgentes(valor[0], false, campo, campos, valorCondicion);
					}
				}
				System.out.println("\n==========================================\n");
			}
		}
	}
	
	public static void camposModificarAgentes(String valorFiltro, boolean condExista, String campoFiltro, List<String> campos, String valorCondicion) {
		//Método que permite al usuario elegir los campos que va a modificar del agente.
		System.out.println("CAMPO QUE SE VA A MODIFICAR");
		System.out.println("===========================\n");
		
		for(int i=0; i < campos.size(); i++) {
			String campo = campos.get(i);
			System.out.println((i+1) + ". " + campo);
		}
		System.out.println("\n=========================");
		
		
		//Pedir campos que se van a modificar.
		String modificarCampos = "";
		System.out.print("¿Desea seguir modificando campos? (S/N): ");
		
		while(modificarCampos.length() <= 1) {
			modificarCampos = sc.nextLine();
			
			if(modificarCampos.isBlank() && modificarCampos.length() > 0) {
				modificarCampos = "";
				System.out.println("-------------------------------------------------------------------");
				System.out.println("\t- No se ha especificado si se quiere modificar campos o no.\n");
				
			} else if(modificarCampos.length() > 0) {
				if(!modificarCampos.equals("S") && !modificarCampos.equals("N")) {
					System.out.println("-------------------------------------------------------------------");
					System.out.println("\t- No se ha especificado si se quiere modificar campos o no.\n");
				} else {
					break;
				}
			}
		}
		
		String campo = "";
		if(modificarCampos.equals("S")) {
			System.out.print("Campo a modificar (su nombre y no el 'nº'): ");
			
			while(campo.length() == 0) {
				campo = sc.nextLine();
				
				if(campo.isBlank() && campo.length() > 0) {
					campo = "";
					System.out.println("-------------------------------------------------------------------");
					System.out.println("\t- No se ha introducido el nombre del campo a modficar.\n");
					
					System.out.print("Campo a modificar (su nombre y no el 'nº'): ");
					
				} else if(campo.length() > 0) {
					//Si se ha introducido el campo vamos a comprobar que exista.
					boolean existe = false;
					for(String campoC: campos) {
						if(campoC.equalsIgnoreCase(campo)) {
							existe = true;
							break;
						}
					}
					
					if(existe) {//Si el campo existe salimos del bucle.
						break;
					} else {
						campo = "";
						System.out.println("\n-------------------------------------------------------------------");
						System.out.println("\t\t- Campo inexistente.\n\n");
						
						System.out.print("Campo a modificar (su nombre y no el nº): ");
					}
				}
			}
			
			//Campos
			String[] nombreCompleto = new String[3]; //Nombre completo (nombre + apellido1 + apellido2)
			String DNI = "";
			String telefono = "";
			String correo = "";
			
			if(campo.equalsIgnoreCase("nombre")) {
				//Después de tener claro el campo a modificar pedimos los valores a sustituir de los docs.
				Map<String, List<String>> valores = valoresSustituirNombres(campo);
				
				if(valores.size() > 0) {
					String nombre = "";
					System.out.print("Nombre: ");
					
					while(nombre.length() == 0) {
						nombre = sc.nextLine();
										
						if(nombre.isBlank() && nombre.length() > 0) {
							nombre = "";
							System.out.println("----------------------------------------------------------------------------------");
							System.out.println("\tNo se ha introducido el nombre del agente.\n");
							
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
							System.out.println("\tNo se ha introducido el 1ºapellido del agente.\n");
							
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
							System.out.println("\tNo se ha introducido el 2ºapellido del agente.\n");

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
					
					/*Para actualizar los documentos correspondientes a este campo recorremos los valores
					  seleccionados para sustituir.*/
					UpdateResult resultado = null;
					
					Collection<List<String>> setKV = valores.values();
					for(Iterator<List<String>> it = setKV.iterator(); it.hasNext();) {
						List<String> valoresSustituir = it.next();
						
						for(int i=0; i < valoresSustituir.size(); i++) {
							//Evaluamos que la actualización se ajuste a las condiciones.
							if(valorCondicion.equalsIgnoreCase("Igual a")) {
								Bson filter = Filters.and(
										Filters.eq(campoFiltro, valorFiltro),
										Filters.eq("nombre", valoresSustituir.get(i)));
								
								Bson update = Updates.set("nombre", Arrays.asList(nombreCompleto));
								resultado = MongoConnection.agentes.updateMany(
										filter, 
										update);
							}
							
							if(valorCondicion.equalsIgnoreCase("Mayor que")) {
								Bson filter = Filters.and(
										Filters.gt(campoFiltro, valorFiltro),
										Filters.eq("nombre", valoresSustituir.get(i)));
								
								Bson update = Updates.set("nombre", Arrays.asList(nombreCompleto));
								resultado = MongoConnection.agentes.updateMany(
										filter, 
										update);
							}
							
							if(valorCondicion.equalsIgnoreCase("Menor que")) {
								Bson filter = Filters.and(
										Filters.lt(campoFiltro, valorFiltro),
										Filters.eq("nombre", valoresSustituir.get(i)));
								
								Bson update = Updates.set("nombre", Arrays.asList(nombreCompleto));
								resultado = MongoConnection.agentes.updateMany(
										filter, 
										update);
							}
							
							if(condExista) {
								Bson filter = Filters.and(
										Filters.exists(campoFiltro),
										Filters.eq("nombre", valoresSustituir.get(i)));
								
								Bson update = Updates.set("nombre", Arrays.asList(nombreCompleto));
								resultado = MongoConnection.agentes.updateMany(
										filter, 
										update);
							}
						}
					}
					if(resultado.getModifiedCount() > 0) {
						System.out.println("Nombres actualizados. (" + resultado.getModifiedCount() + ")");
						
						MongoCursor<Document> documentos = MongoConnection.agentes.aggregate(Arrays.asList(
								match(new Document("nombre", Arrays.asList(nombreCompleto))))).iterator();
						
						System.out.println("\nAGENTES ACTUALIZADOS");
						System.out.println("=========================\n");
						while(documentos.hasNext()) {
							System.out.println(documentos.next().toJson());
						}
						System.out.println();
					}
				}
			}
			
			if(campo.equals("DNI")) {
				//Después de tener claro el campo a modificar pedimos los valores a sustituir de los docs.
				Map<String, List<String>> valores = valoresSustituirDNIs(campo);
				
				if(valores.size() > 0) {
					System.out.print("DNI: ");
					
					while(DNI.length() == 0) {
						DNI = sc.nextLine();
										
						if(DNI.isBlank() && DNI.length() > 0) {
							DNI = "";
							System.out.println("----------------------------------------------------------------------------------");
							System.out.println("\tNo se ha introducido el DNI del agente.\n");

							System.out.print("DNI: ");
							
						} else if(DNI.length() > 0) {
							break;
						}
					}
					
					/*Para actualizar los documentos correspondientes a este campo recorremos los valores
					  seleccionados para sustituir.*/
					UpdateResult resultado = null;
					
					Collection<List<String>> setKV = valores.values();
					for(Iterator<List<String>> it = setKV.iterator(); it.hasNext();) {
						List<String> valoresSustituir = it.next();
						
						for(int i=0; i < valoresSustituir.size(); i++) {
							//Evaluamos que la actualización se ajuste a las condiciones.
							if(valorCondicion.equalsIgnoreCase("Igual a")) {
								Bson filter = Filters.and(
										Filters.eq(campoFiltro, valorFiltro),
										Filters.eq("DNI", valoresSustituir.get(i)));
								
								Bson update = Updates.set("DNI", DNI);
								resultado = MongoConnection.agentes.updateMany(
										filter, 
										update);
							}
							
							if(valorCondicion.equalsIgnoreCase("Mayor que")) {
								Bson filter = Filters.and(
										Filters.gt(campoFiltro, valorFiltro),
										Filters.eq("DNI", valoresSustituir.get(i)));
								
								Bson update = Updates.set("DNI", DNI);
								resultado = MongoConnection.agentes.updateMany(
										filter, 
										update);
							}
							
							if(valorCondicion.equalsIgnoreCase("Menor que")) {
								Bson filter = Filters.and(
										Filters.lt(campoFiltro, valorFiltro),
										Filters.eq("DNI", valoresSustituir.get(i)));
								
								Bson update = Updates.set("DNI", DNI);
								resultado = MongoConnection.agentes.updateMany(
										filter, 
										update);
							}
							
							if(condExista) {
								Bson filter = Filters.and(
										Filters.exists(campoFiltro),
										Filters.eq("DNI", valoresSustituir.get(i)));
								
								Bson update = Updates.set("DNI", DNI);
								resultado = MongoConnection.agentes.updateMany(
										filter, 
										update);
							}
						}
					}
					if(resultado.getModifiedCount() > 0) {
						System.out.println("DNIs actualizados. (" + resultado.getModifiedCount() + ")");
						
						MongoCursor<Document> documentos = MongoConnection.agentes.aggregate(Arrays.asList(
								match(new Document("DNI", DNI)))).iterator();
						
						System.out.println("\nAGENTES ACTUALIZADOS");
						System.out.println("=========================\n");
						while(documentos.hasNext()) {
							System.out.println(documentos.next().toJson());
						}
						System.out.println();
					}
				}
			}
			
			if(campo.equals("telefono")) {
				//Después de tener claro el campo a modificar pedimos los valores a sustituir de los docs.
				Map<String, List<String>> valores = valoresSustituirTelefonos(campo);
				
				if(valores.size() > 0) {
					System.out.print("NºTeléfono: ");
					
					while(telefono.length() == 0) {
						telefono = sc.nextLine();
												
						if(telefono.isBlank() && telefono.length() > 0) {
							telefono = "";
							System.out.println("----------------------------------------------------------------------------------");
							System.out.println("\tNo se ha introducido el nºde teléfono del agente.\n");

							System.out.print("NºTeléfono: ");
							
						} else if(telefono.length() > 0) {
							break;
						}
					}
					
					/*Para actualizar los documentos correspondientes a este campo recorremos los valores
					  seleccionados para sustituir.*/
					UpdateResult resultado = null;
					
					Collection<List<String>> setKV = valores.values();
					for(Iterator<List<String>> it = setKV.iterator(); it.hasNext();) {
						List<String> valoresSustituir = it.next();
						
						for(int i=0; i < valoresSustituir.size(); i++) {
							//Evaluamos que la actualización se ajuste a las condiciones.
							if(valorCondicion.equalsIgnoreCase("Igual a")) {
								Bson filter = Filters.and(
										Filters.eq(campoFiltro, valorFiltro),
										Filters.eq("telefono", valoresSustituir.get(i)));
								
								Bson update = Updates.set("telefono", telefono);
								resultado = MongoConnection.agentes.updateMany(
										filter, 
										update);
							}
							
							if(valorCondicion.equalsIgnoreCase("Mayor que")) {
								Bson filter = Filters.and(
										Filters.gt(campoFiltro, valorFiltro),
										Filters.eq("telefono", valoresSustituir.get(i)));
								
								Bson update = Updates.set("telefono", telefono);
								resultado = MongoConnection.agentes.updateMany(
										filter, 
										update);
							}
							
							if(valorCondicion.equalsIgnoreCase("Menor que")) {
								Bson filter = Filters.and(
										Filters.lt(campoFiltro, valorFiltro),
										Filters.eq("telefono", valoresSustituir.get(i)));
								
								Bson update = Updates.set("telefono", telefono);
								resultado = MongoConnection.agentes.updateMany(
										filter, 
										update);
							}
							
							if(condExista) {
								Bson filter = Filters.and(
										Filters.exists(campoFiltro),
										Filters.eq("telefono", valoresSustituir.get(i)));
								
								Bson update = Updates.set("telefono", telefono);
								resultado = MongoConnection.agentes.updateMany(
										filter, 
										update);
							}
						}
					}
					if(resultado.getModifiedCount() > 0) {
						System.out.println("Nºos de Teléfonos actualizados. (" + resultado.getModifiedCount() + ")");
						
						MongoCursor<Document> documentos = MongoConnection.agentes.aggregate(Arrays.asList(
								match(new Document("telefono", telefono)))).iterator();
						
						System.out.println("\nAGENTES ACTUALIZADOS");
						System.out.println("=========================\n");
						while(documentos.hasNext()) {
							System.out.println(documentos.next().toJson());
						}
						System.out.println();
					}
				}
			}
			
			if(campo.equals("email")) {
				//Después de tener claro el campo a modificar pedimos los valores a sustituir de los docs.
				Map<String, List<String>> valores = valoresSustituirEmails(campo);
				
				if(valores.size() > 0) {
					System.out.print("Correo electrónico: ");
					
					while(correo.length() == 0) {
						correo = sc.nextLine();
												
						if(correo.isBlank() && correo.length() > 0) {
							correo = "";
							System.out.println("----------------------------------------------------------------------------------");
							System.out.println("\tNo se ha introducido el correo del agente.\n");
							
							System.out.print("Correo electrónico: ");
							
						} else if(correo.length() > 0) {
							break;
						}
					}
					
					/*Para actualizar los documentos correspondientes a este campo recorremos los valores
					  seleccionados para sustituir.*/
					UpdateResult resultado = null;
					
					Collection<List<String>> setKV = valores.values();
					for(Iterator<List<String>> it = setKV.iterator(); it.hasNext();) {
						List<String> valoresSustituir = it.next();
						
						for(int i=0; i < valoresSustituir.size(); i++) {
							//Evaluamos que la actualización se ajuste a las condiciones.
							if(valorCondicion.equalsIgnoreCase("Igual a")) {
								Bson filter = Filters.and(
										Filters.eq(campoFiltro, valorFiltro),
										Filters.eq("email", valoresSustituir.get(i)));
								
								Bson update = Updates.set("email", correo);
								resultado = MongoConnection.agentes.updateMany(
										filter, 
										update);
							}
							
							if(valorCondicion.equalsIgnoreCase("Mayor que")) {
								Bson filter = Filters.and(
										Filters.gt(campoFiltro, valorFiltro),
										Filters.eq("email", valoresSustituir.get(i)));
								
								Bson update = Updates.set("email", correo);
								resultado = MongoConnection.agentes.updateMany(
										filter, 
										update);
							}
							
							if(valorCondicion.equalsIgnoreCase("Menor que")) {
								Bson filter = Filters.and(
										Filters.lt(campoFiltro, valorFiltro),
										Filters.eq("email", valoresSustituir.get(i)));
								
								Bson update = Updates.set("email", correo);
								resultado = MongoConnection.agentes.updateMany(
										filter, 
										update);
							}
							
							if(condExista) {
								Bson filter = Filters.and(
										Filters.exists(campoFiltro),
										Filters.eq("email", valoresSustituir.get(i)));
								
								Bson update = Updates.set("email", correo);
								resultado = MongoConnection.agentes.updateMany(
										filter, 
										update);
							}
						}
					}
					if(resultado.getModifiedCount() > 0) {
						System.out.println("Emails actualizados. (" + resultado.getModifiedCount() + ")");

						MongoCursor<Document> documentos = MongoConnection.agentes.aggregate(Arrays.asList(
								match(new Document("email", correo)))).iterator();
						
						System.out.println("\nAGENTES ACTUALIZADOS");
						System.out.println("=========================\n");
						while(documentos.hasNext()) {
							System.out.println(documentos.next().toJson());
						}
						System.out.println();
					}
				}
			}
			
			if(campo.equals("licencia")) {
				Map<String, List<String>> valores = valoresSustituirLicencias(campo);
				
				if(valores.size() > 0) {
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
							
						} else if(licencia.length() > 0) {
							break;
						}
					}
					
					/*Para actualizar los documentos correspondientes a este campo recorremos los valores
					  seleccionados para sustituir.*/
					UpdateResult resultado = null;
					
					Collection<List<String>> setKV = valores.values();
					for(Iterator<List<String>> it = setKV.iterator(); it.hasNext();) {
						List<String> valoresSustituir = it.next();
						
						for(int i=0; i < valoresSustituir.size(); i++) {
							//Evaluamos que la actualización se ajuste a las condiciones.
							if(valorCondicion.equalsIgnoreCase("Igual a")) {
								Bson filter = Filters.and(
										Filters.eq(campoFiltro, valorFiltro),
										Filters.eq("licencia", valoresSustituir.get(i)));
								
								Bson update = Updates.set("licencia", licencia);
								resultado = MongoConnection.agentes.updateMany(
										filter, 
										update);
							}
							
							if(valorCondicion.equalsIgnoreCase("Mayor que")) {
								Bson filter = Filters.and(
										Filters.gt(campoFiltro, valorFiltro),
										Filters.eq("licencia", valoresSustituir.get(i)));
								
								Bson update = Updates.set("licencia", licencia);
								resultado = MongoConnection.agentes.updateMany(
										filter, 
										update);
							}
							
							if(valorCondicion.equalsIgnoreCase("Menor que")) {
								Bson filter = Filters.and(
										Filters.lt(campoFiltro, valorFiltro),
										Filters.eq("licencia", valoresSustituir.get(i)));
								
								Bson update = Updates.set("licencia", licencia);
								resultado = MongoConnection.agentes.updateMany(
										filter, 
										update);
							}
							
							if(condExista) {
								Bson filter = Filters.and(
										Filters.exists(campoFiltro),
										Filters.eq("licencia", valoresSustituir.get(i)));
								
								Bson update = Updates.set("licencia", licencia);
								resultado = MongoConnection.agentes.updateMany(
										filter, 
										update);
							}
						}
					}
					if(resultado.getModifiedCount() > 0) {
						System.out.println("Licencias actualizadas. (" + resultado.getModifiedCount() + ")");

						MongoCursor<Document> documentos = MongoConnection.agentes.aggregate(Arrays.asList(
								match(new Document("licencia", licencia)))).iterator();
						
						System.out.println("\nAGENTES ACTUALIZADOS");
						System.out.println("=========================\n");
						while(documentos.hasNext()) {
							System.out.println(documentos.next().toJson());
						}
						System.out.println();
					}
				}
			}
		}
	}

	public static String[] valorFiltroAgentes(String campo) {
		//Método que permitirá al usuario establecer el valor del campo por el que se quiere filtrar a los agentes.
		String[] valor = new String[1]; //Valor de retorno, es un array por el nombre completo.
		
		//Campos
		String[] nombreCompleto = new String[3]; //Nombre completo (nombre + apellido1 + apellido2)
		String DNI = "";
		String telefono = "";
		String correo = "";
		
		if(campo.equalsIgnoreCase("nombre")) {
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
			
			return nombreCompleto;
		}
		
		if(campo.equalsIgnoreCase("DNI")) {
			//DNI
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
			valor[0] = DNI;
		}
		
		if(campo.equalsIgnoreCase("telefono")) {
			//Teléfono
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
			valor[0] = telefono;
		}
		
		if(campo.equalsIgnoreCase("email")) {
			//Email
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
			valor[0] = correo;
		}
		
		if(campo.equalsIgnoreCase("licencia")) {
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
			valor[0] = licencia;
		}
		
		return valor;
	}

	public static void ActualizarCompradores(List<String> campos) {
		//Método que permitirá al usuario actualizar campos de un documento de la colección de compradores.
		
		//1º) Vamos a preguntar al usuario el campo por el que vamos a filtrar y comprobar que exista.
		String campo = campoFiltrar(campos);
		if(campo.length() > 0) {
			/*Si el campo pertenece a la colección entonces se preguntará la condición que el valor debe 
			tomar.*/
			
			String valorCondicion = valorCondicion();
			if(valorCondicion.length() > 0) {
				/*Ahora, una vez sabemos la condición que el valor debe tomar preguntamos al usuario 
				  según el campo de la colección que se haya escogido.*/
				
				System.out.println("\nVALOR QUE DEBE TOMAR EL CAMPO SELECCIONADO");
				System.out.println("==========================================\n");
				
				/*Si la condición es que el campo a filtrar exista significa que no tiene que tomar 
				ningún valor a la hora de la filtración.*/
				
				if(valorCondicion.equalsIgnoreCase("Que el campo exista")) {
					boolean valor = true; //El valor será un booleano a true si se llega aquí.
					
					camposModificarCompradores(null, valor, campo, campos, valorCondicion);
				} else {
					String[] valor = valorFiltroCompradores(campo);
					if(valor.length > 0) {
						camposModificarCompradores(valor[0], false, campo, campos, valorCondicion);
					}
				}
				System.out.println("\n==========================================\n");
			}
		}
	}

	public static String[] valorFiltroCompradores(String campo) {
		//Método que permitirá al usuario establecer el valor del campo por el que se quiere filtrar a los compradores.
		String[] valor = new String[1]; //Valor de retorno, es un array por el nombre completo.
		
		//Campos
		String[] nombreCompleto = new String[3]; //Nombre completo (nombre + apellido1 + apellido2)
		String DNI = "";
		String telefono = "";
		String correo = "";
		int presu_max = 0;
		String preferencias = "";
		
		if(campo.equalsIgnoreCase("nombre")) {
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
			
			return nombreCompleto;
		}
		
		if(campo.equalsIgnoreCase("DNI")) {
			//DNI
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
			valor[0] = DNI;
		}
		
		if(campo.equalsIgnoreCase("telefono")) {
			//Teléfono
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
			valor[0] = telefono;
		}
		
		if(campo.equalsIgnoreCase("email")) {
			//Email
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
			valor[0] = correo;
		}
		
		if(campo.equalsIgnoreCase("presupuesto")) {
			while(true) {
				System.out.print("Presupuesto máximo: ");
				try {
					presu_max = sc.nextInt();
				} catch (InputMismatchException e) {
					sc.nextLine();
							
					System.out.println("----------------------------------------");
					System.out.println("\tPresupuesto máximo inválido\n");
							
					continue;
				}
				break;
			}
			valor[0] = String.valueOf(presu_max);
		}
		
		if(campo.equalsIgnoreCase("preferencias")) {
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
			valor[0] = preferencias;
		}
		
		return valor;
	}

	public static void camposModificarCompradores(String valorFiltro, boolean condExista, String campoFiltro, List<String> campos, String valorCondicion) {
		//Método que permite al usuario elegir los campos que va a modificar del comprador.
		System.out.println("CAMPO QUE SE VA A MODIFICAR");
		System.out.println("===========================\n");
		
		for(int i=0; i < campos.size(); i++) {
			String campo = campos.get(i);
			System.out.println((i+1) + ". " + campo);
		}
		System.out.println("\n=========================");
		
		
		//Pedir campos que se van a modificar.
		String modificarCampos = "";
		System.out.print("¿Desea seguir modificando campos? (S/N): ");
		
		while(modificarCampos.length() <= 1) {
			modificarCampos = sc.nextLine();
			
			if(modificarCampos.isBlank() && modificarCampos.length() > 0) {
				modificarCampos = "";
				System.out.println("-------------------------------------------------------------------");
				System.out.println("\t- No se ha especificado si se quiere modificar campos o no.\n");
				
			} else if(modificarCampos.length() > 0) {
				if(!modificarCampos.equals("S") && !modificarCampos.equals("N")) {
					System.out.println("-------------------------------------------------------------------");
					System.out.println("\t- No se ha especificado si se quiere modificar campos o no.\n");
				} else {
					break;
				}
			}
		}
		
		String campo = "";
		if(modificarCampos.equals("S")) {
			System.out.print("Campo a modificar (su nombre y no el 'nº'): ");
			
			while(campo.length() == 0) {
				campo = sc.nextLine();
				
				if(campo.isBlank() && campo.length() > 0) {
					campo = "";
					System.out.println("-------------------------------------------------------------------");
					System.out.println("\t- No se ha introducido el nombre del campo a modficar.\n");
					
					System.out.print("Campo a modificar (su nombre y no el 'nº'): ");
					
				} else if(campo.length() > 0) {
					//Si se ha introducido el campo vamos a comprobar que exista.
					boolean existe = false;
					for(String campoC: campos) {
						if(campoC.equalsIgnoreCase(campo)) {
							existe = true;
							break;
						}
					}
					
					if(existe) {//Si el campo existe salimos del bucle.
						break;
					} else {
						campo = "";
						System.out.println("\n-------------------------------------------------------------------");
						System.out.println("\t\t- Campo inexistente.\n\n");
						
						System.out.print("Campo a modificar (su nombre y no el nº): ");
					}
				}
			}
			
			//Campos
			String[] nombreCompleto = new String[3]; //Nombre completo (nombre + apellido1 + apellido2)
			String DNI = "";
			String telefono = "";
			String correo = "";
			int presu_max = 0;
			String preferencias = "";
			
			if(campo.equalsIgnoreCase("nombre")) {
				//Después de tener claro el campo a modificar pedimos los valores a sustituir de los docs.
				Map<String, List<String>> valores = valoresSustituirNombres(campo);
				
				if(valores.size() > 0) {
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
					
					/*Para actualizar los documentos correspondientes a este campo recorremos los valores
					  seleccionados para sustituir.*/
					UpdateResult resultado = null;
					
					Collection<List<String>> setKV = valores.values();
					for(Iterator<List<String>> it = setKV.iterator(); it.hasNext();) {
						List<String> valoresSustituir = it.next();
						
						for(int i=0; i < valoresSustituir.size(); i++) {
							//Evaluamos que la actualización se ajuste a las condiciones.
							if(valorCondicion.equalsIgnoreCase("Igual a")) {
								Bson filter = Filters.and(
										Filters.eq(campoFiltro, valorFiltro),
										Filters.eq("nombre", valoresSustituir.get(i)));
								
								Bson update = Updates.set("nombre", Arrays.asList(nombreCompleto));
								resultado = MongoConnection.compradores.updateMany(
										filter, 
										update);
							}
							
							if(valorCondicion.equalsIgnoreCase("Mayor que")) {
								Bson filter = Filters.and(
										Filters.gt(campoFiltro, valorFiltro),
										Filters.eq("nombre", valoresSustituir.get(i)));
								
								Bson update = Updates.set("nombre", Arrays.asList(nombreCompleto));
								resultado = MongoConnection.compradores.updateMany(
										filter, 
										update);
							}
							
							if(valorCondicion.equalsIgnoreCase("Menor que")) {
								Bson filter = Filters.and(
										Filters.lt(campoFiltro, valorFiltro),
										Filters.eq("nombre", valoresSustituir.get(i)));
								
								Bson update = Updates.set("nombre", Arrays.asList(nombreCompleto));
								resultado = MongoConnection.compradores.updateMany(
										filter, 
										update);
							}
							
							if(condExista) {
								Bson filter = Filters.and(
										Filters.exists(campoFiltro),
										Filters.eq("nombre", valoresSustituir.get(i)));
								
								Bson update = Updates.set("nombre", Arrays.asList(nombreCompleto));
								resultado = MongoConnection.compradores.updateMany(
										filter, 
										update);
							}
						}
					}
					if(resultado.getModifiedCount() > 0) {
						System.out.println("Nombres actualizados. (" + resultado.getModifiedCount() + ")");
						
						MongoCursor<Document> documentos = MongoConnection.compradores.aggregate(Arrays.asList(
								match(new Document("nombre", Arrays.asList(nombreCompleto))))).iterator();
						
						System.out.println("\nCOMPRADORES ACTUALIZADOS");
						System.out.println("=========================\n");
						while(documentos.hasNext()) {
							System.out.println(documentos.next().toJson());
						}
						System.out.println();
					}
				}
			}
			
			if(campo.equals("DNI")) {
				//Después de tener claro el campo a modificar pedimos los valores a sustituir de los docs.
				Map<String, List<String>> valores = valoresSustituirDNIs(campo);
				
				if(valores.size() > 0) {
					System.out.print("DNI: ");
					
					while(DNI.length() == 0) {
						DNI = sc.nextLine();
										
						if(DNI.isBlank() && DNI.length() > 0) {
							DNI = "";
							System.out.println("----------------------------------------------------------------------------------");
							System.out.println("\tNo se ha introducido el DNI del comprador.\n");
	
							System.out.print("DNI: ");
							
						} else if(DNI.length() > 0) {
							break;
						}
					}
					
					/*Para actualizar los documentos correspondientes a este campo recorremos los valores
					  seleccionados para sustituir.*/
					UpdateResult resultado = null;
					
					Collection<List<String>> setKV = valores.values();
					for(Iterator<List<String>> it = setKV.iterator(); it.hasNext();) {
						List<String> valoresSustituir = it.next();
						
						for(int i=0; i < valoresSustituir.size(); i++) {
							//Evaluamos que la actualización se ajuste a las condiciones.
							if(valorCondicion.equalsIgnoreCase("Igual a")) {
								Bson filter = Filters.and(
										Filters.eq(campoFiltro, valorFiltro),
										Filters.eq("DNI", valoresSustituir.get(i)));
								
								Bson update = Updates.set("DNI", DNI);
								resultado = MongoConnection.compradores.updateMany(
										filter, 
										update);
							}
							
							if(valorCondicion.equalsIgnoreCase("Mayor que")) {
								Bson filter = Filters.and(
										Filters.gt(campoFiltro, valorFiltro),
										Filters.eq("DNI", valoresSustituir.get(i)));
								
								Bson update = Updates.set("DNI", DNI);
								resultado = MongoConnection.compradores.updateMany(
										filter, 
										update);
							}
							
							if(valorCondicion.equalsIgnoreCase("Menor que")) {
								Bson filter = Filters.and(
										Filters.lt(campoFiltro, valorFiltro),
										Filters.eq("DNI", valoresSustituir.get(i)));
								
								Bson update = Updates.set("DNI", DNI);
								resultado = MongoConnection.compradores.updateMany(
										filter, 
										update);
							}
							
							if(condExista) {
								Bson filter = Filters.and(
										Filters.exists(campoFiltro),
										Filters.eq("DNI", valoresSustituir.get(i)));
								
								Bson update = Updates.set("DNI", DNI);
								resultado = MongoConnection.compradores.updateMany(
										filter, 
										update);
							}
						}
					}
					if(resultado.getModifiedCount() > 0) {
						System.out.println("DNIs actualizados. (" + resultado.getModifiedCount() + ")");
						
						MongoCursor<Document> documentos = MongoConnection.compradores.aggregate(Arrays.asList(
								match(new Document("DNI", DNI)))).iterator();
						
						System.out.println("\nCOMPRADORES ACTUALIZADOS");
						System.out.println("=========================\n");
						while(documentos.hasNext()) {
							System.out.println(documentos.next().toJson());
						}
						System.out.println();
					}
				}
			}
			
			if(campo.equals("telefono")) {
				//Después de tener claro el campo a modificar pedimos los valores a sustituir de los docs.
				Map<String, List<String>> valores = valoresSustituirTelefonos(campo);
				
				if(valores.size() > 0) {
					System.out.print("NºTeléfono: ");
					
					while(telefono.length() == 0) {
						telefono = sc.nextLine();
												
						if(telefono.isBlank() && telefono.length() > 0) {
							telefono = "";
							System.out.println("----------------------------------------------------------------------------------");
							System.out.println("\tNo se ha introducido el nºde teléfono del comprador.\n");
	
							System.out.print("NºTeléfono: ");
							
						} else if(telefono.length() > 0) {
							break;
						}
					}
					
					/*Para actualizar los documentos correspondientes a este campo recorremos los valores
					  seleccionados para sustituir.*/
					UpdateResult resultado = null;
					
					Collection<List<String>> setKV = valores.values();
					for(Iterator<List<String>> it = setKV.iterator(); it.hasNext();) {
						List<String> valoresSustituir = it.next();
						
						for(int i=0; i < valoresSustituir.size(); i++) {
							//Evaluamos que la actualización se ajuste a las condiciones.
							if(valorCondicion.equalsIgnoreCase("Igual a")) {
								Bson filter = Filters.and(
										Filters.eq(campoFiltro, valorFiltro),
										Filters.eq("telefono", valoresSustituir.get(i)));
								
								Bson update = Updates.set("telefono", telefono);
								resultado = MongoConnection.compradores.updateMany(
										filter, 
										update);
							}
							
							if(valorCondicion.equalsIgnoreCase("Mayor que")) {
								Bson filter = Filters.and(
										Filters.gt(campoFiltro, valorFiltro),
										Filters.eq("telefono", valoresSustituir.get(i)));
								
								Bson update = Updates.set("telefono", telefono);
								resultado = MongoConnection.compradores.updateMany(
										filter, 
										update);
							}
							
							if(valorCondicion.equalsIgnoreCase("Menor que")) {
								Bson filter = Filters.and(
										Filters.lt(campoFiltro, valorFiltro),
										Filters.eq("telefono", valoresSustituir.get(i)));
								
								Bson update = Updates.set("telefono", telefono);
								resultado = MongoConnection.compradores.updateMany(
										filter, 
										update);
							}
							
							if(condExista) {
								Bson filter = Filters.and(
										Filters.exists(campoFiltro),
										Filters.eq("telefono", valoresSustituir.get(i)));
								
								Bson update = Updates.set("telefono", telefono);
								resultado = MongoConnection.compradores.updateMany(
										filter, 
										update);
							}
						}
					}
					if(resultado.getModifiedCount() > 0) {
						System.out.println("Nºos de Teléfonos actualizados. (" + resultado.getModifiedCount() + ")");
						
						MongoCursor<Document> documentos = MongoConnection.compradores.aggregate(Arrays.asList(
								match(new Document("telefono", telefono)))).iterator();
						
						System.out.println("\nCOMPRADORES ACTUALIZADOS");
						System.out.println("=========================\n");
						while(documentos.hasNext()) {
							System.out.println(documentos.next().toJson());
						}
						System.out.println();
					}
				}
			}
			
			if(campo.equals("email")) {
				//Después de tener claro el campo a modificar pedimos los valores a sustituir de los docs.
				Map<String, List<String>> valores = valoresSustituirEmails(campo);
				
				if(valores.size() > 0) {
					System.out.print("Correo electrónico: ");
					
					while(correo.length() == 0) {
						correo = sc.nextLine();
												
						if(correo.isBlank() && correo.length() > 0) {
							correo = "";
							System.out.println("----------------------------------------------------------------------------------");
							System.out.println("\tNo se ha introducido el correo del comprador.\n");
							
							System.out.print("Correo electrónico: ");
							
						} else if(correo.length() > 0) {
							break;
						}
					}
					
					/*Para actualizar los documentos correspondientes a este campo recorremos los valores
					  seleccionados para sustituir.*/
					UpdateResult resultado = null;
					
					Collection<List<String>> setKV = valores.values();
					for(Iterator<List<String>> it = setKV.iterator(); it.hasNext();) {
						List<String> valoresSustituir = it.next();
						
						for(int i=0; i < valoresSustituir.size(); i++) {
							//Evaluamos que la actualización se ajuste a las condiciones.
							if(valorCondicion.equalsIgnoreCase("Igual a")) {
								Bson filter = Filters.and(
										Filters.eq(campoFiltro, valorFiltro),
										Filters.eq("email", valoresSustituir.get(i)));
								
								Bson update = Updates.set("email", correo);
								resultado = MongoConnection.compradores.updateMany(
										filter, 
										update);
							}
							
							if(valorCondicion.equalsIgnoreCase("Mayor que")) {
								Bson filter = Filters.and(
										Filters.gt(campoFiltro, valorFiltro),
										Filters.eq("email", valoresSustituir.get(i)));
								
								Bson update = Updates.set("email", correo);
								resultado = MongoConnection.compradores.updateMany(
										filter, 
										update);
							}
							
							if(valorCondicion.equalsIgnoreCase("Menor que")) {
								Bson filter = Filters.and(
										Filters.lt(campoFiltro, valorFiltro),
										Filters.eq("email", valoresSustituir.get(i)));
								
								Bson update = Updates.set("email", correo);
								resultado = MongoConnection.compradores.updateMany(
										filter, 
										update);
							}
							
							if(condExista) {
								Bson filter = Filters.and(
										Filters.exists(campoFiltro),
										Filters.eq("email", valoresSustituir.get(i)));
								
								Bson update = Updates.set("email", correo);
								resultado = MongoConnection.compradores.updateMany(
										filter, 
										update);
							}
						}
					}
					if(resultado.getModifiedCount() > 0) {
						System.out.println("Emails actualizados. (" + resultado.getModifiedCount() + ")");
	
						MongoCursor<Document> documentos = MongoConnection.compradores.aggregate(Arrays.asList(
								match(new Document("email", correo)))).iterator();
						
						System.out.println("\nCOMPRADORES ACTUALIZADOS");
						System.out.println("=========================\n");
						while(documentos.hasNext()) {
							System.out.println(documentos.next().toJson());
						}
						System.out.println();
					}
				}
			}
			
			if(campo.equals("presupuesto")) {
				//Después de tener claro el campo a modificar pedimos los valores a sustituir de los docs.
				Map<String, List<Integer>> valores = valoresSustituirPresupuestos(campo);
				
				if(valores.size() > 0) {
					while(true) {
						System.out.print("Presupuesto máximo: ");
						try {
							presu_max = sc.nextInt();
						} catch (InputMismatchException e) {
							sc.nextLine();
									
							System.out.println("----------------------------------------");
							System.out.println("\tPresupuesto máximo inválido\n");
									
							continue;
						}
						break;
					}
					
					/*Para actualizar los documentos correspondientes a este campo recorremos los valores
					  seleccionados para sustituir.*/
					UpdateResult resultado = null;
					
					Collection<List<Integer>> setKV = valores.values();
					for(Iterator<List<Integer>> it = setKV.iterator(); it.hasNext();) {
						List<Integer> valoresSustituir = it.next();
						
						for(int i=0; i < valoresSustituir.size(); i++) {
							//Evaluamos que la actualización se ajuste a las condiciones.
							if(valorCondicion.equalsIgnoreCase("Igual a")) {
								Bson filter = Filters.and(
										Filters.eq(campoFiltro, Integer.parseInt(valorFiltro)),
										Filters.eq("presupuesto", valoresSustituir.get(i)));
								
								Bson update = Updates.set("presupuesto", presu_max);
								resultado = MongoConnection.compradores.updateMany(
										filter, 
										update);
							}
							
							if(valorCondicion.equalsIgnoreCase("Mayor que")) {
								Bson filter = Filters.and(
										Filters.gt(campoFiltro, Integer.parseInt(valorFiltro)),
										Filters.eq("presupuesto", valoresSustituir.get(i)));
								
								Bson update = Updates.set("presupuesto", presu_max);
								resultado = MongoConnection.compradores.updateMany(
										filter, 
										update);
							}
							
							if(valorCondicion.equalsIgnoreCase("Menor que")) {
								Bson filter = Filters.and(
										Filters.lt(campoFiltro, Integer.parseInt(valorFiltro)),
										Filters.eq("presupuesto", valoresSustituir.get(i)));
								
								Bson update = Updates.set("presupuesto", presu_max);
								resultado = MongoConnection.compradores.updateMany(
										filter, 
										update);
							}
							
							if(condExista) {
								Bson filter = Filters.and(
										Filters.exists(campoFiltro),
										Filters.eq("presupuesto", valoresSustituir.get(i)));
								
								Bson update = Updates.set("presupuesto", presu_max);
								resultado = MongoConnection.compradores.updateMany(
										filter, 
										update);
							}
						}
					}
					if(resultado.getModifiedCount() > 0) {
						System.out.println("Presupuestos actualizados. (" + resultado.getModifiedCount() + ")");
	
						MongoCursor<Document> documentos = MongoConnection.compradores.aggregate(Arrays.asList(
								match(new Document("presupuesto", presu_max)))).iterator();
						
						System.out.println("\nCOMPRADORES ACTUALIZADOS");
						System.out.println("=========================\n");
						while(documentos.hasNext()) {
							System.out.println(documentos.next().toJson());
						}
						System.out.println();
					}
				}
			}
			
			if(campo.equals("preferencias")) {
				//Después de tener claro el campo a modificar pedimos los valores a sustituir de los docs.
				Map<String, List<String>> valores = valoresSustituirPreferencias(campo);
				
				if(valores.size() > 0) {
					System.out.print("Preferencias del comprador: ");
					
					while(preferencias.length() == 0) {
						preferencias = sc.nextLine();
																		
						if(preferencias.isBlank() && preferencias.length() > 0) {
							preferencias = "";
							System.out.println("----------------------------------------------------------------------------------");
							System.out.println("\tNo se han introducido las preferencias del comprador.\n");
													
							System.out.print("Preferencias del comprador: ");
						} else if(preferencias.length() > 0) {
							break;
						}
					}
					
					/*Para actualizar los documentos correspondientes a este campo recorremos los valores
					  seleccionados para sustituir.*/
					UpdateResult resultado = null;
					
					Collection<List<String>> setKV = valores.values();
					for(Iterator<List<String>> it = setKV.iterator(); it.hasNext();) {
						List<String> valoresSustituir = it.next();
						
						for(int i=0; i < valoresSustituir.size(); i++) {
							//Evaluamos que la actualización se ajuste a las condiciones.
							if(valorCondicion.equalsIgnoreCase("Igual a")) {
								Bson filter = Filters.and(
										Filters.eq(campoFiltro, valorFiltro),
										Filters.eq("preferencias", valoresSustituir.get(i)));
								
								Bson update = Updates.set("preferencias", preferencias);
								resultado = MongoConnection.compradores.updateMany(
										filter, 
										update);
							}
							
							if(valorCondicion.equalsIgnoreCase("Mayor que")) {
								Bson filter = Filters.and(
										Filters.gt(campoFiltro, valorFiltro),
										Filters.eq("preferencias", valoresSustituir.get(i)));
								
								Bson update = Updates.set("preferencias", preferencias);
								resultado = MongoConnection.compradores.updateMany(
										filter, 
										update);
							}
							
							if(valorCondicion.equalsIgnoreCase("Menor que")) {
								Bson filter = Filters.and(
										Filters.lt(campoFiltro, valorFiltro),
										Filters.eq("preferencias", valoresSustituir.get(i)));
								
								Bson update = Updates.set("preferencias", preferencias);
								resultado = MongoConnection.compradores.updateMany(
										filter, 
										update);
							}
							
							if(condExista) {
								Bson filter = Filters.and(
										Filters.exists(campoFiltro),
										Filters.eq("preferencias", valoresSustituir.get(i)));
								
								Bson update = Updates.set("preferencias", preferencias);
								resultado = MongoConnection.compradores.updateMany(
										filter, 
										update);
							}
						}
					}
					if(resultado.getModifiedCount() > 0) {
						System.out.println("Preferencias actualizadas. (" + resultado.getModifiedCount() + ")");
	
						MongoCursor<Document> documentos = MongoConnection.compradores.aggregate(Arrays.asList(
								match(new Document("preferencias", preferencias)))).iterator();
						
						System.out.println("\nCOMPRADORES ACTUALIZADOS");
						System.out.println("=========================\n");
						while(documentos.hasNext()) {
							System.out.println(documentos.next().toJson());
						}
						System.out.println();
					}
				}
			}
		}
	}

	public static void ActualizarCasas(List<String> campos) {
		//Método que permitirá al usuario actualizar campos de un documento de la colección de casas.
	
		//1º) Vamos a preguntar al usuario el campo por el que vamos a filtrar y comprobar que exista.
		String campo = campoFiltrar(campos);
		if(campo.length() > 0) {
			/*Si el campo pertenece a la colección entonces se preguntará la condición que el valor debe 
			tomar.*/
			
			String valorCondicion = valorCondicion();
			if(valorCondicion.length() > 0) {
				/*Ahora, una vez sabemos la condición que el valor debe tomar preguntamos al usuario 
				  según el campo de la colección que se haya escogido.*/
				
				System.out.println("\nVALOR QUE DEBE TOMAR EL CAMPO SELECCIONADO");
				System.out.println("==========================================\n");
				
				/*Si la condición es que el campo a filtrar exista significa que no tiene que tomar 
				ningún valor a la hora de la filtración.*/
				
				if(valorCondicion.equalsIgnoreCase("Que el campo exista")) {
					boolean valor = true; //El valor será un booleano a true si se llega aquí.
					
					camposModificarCasas(null, valor, campo, campos, valorCondicion);
				} else {
					Object valor = valorFiltroCasas(campo);
					if(valor != null) {
						camposModificarCasas(valor, false, campo, campos, valorCondicion);
					}
				}
				System.out.println("\n==========================================\n");
			}
		}
	}

	public static Object valorFiltroCasas(String campo) {
		//Método que permite al usuario establecer el valor del campo por el que se van a filtrar las casas.
		//El método es de tipo Object porque trabajamos con varios tipos de retorno.
		
		//Campos
		String tipo = "";
		String direccion = "";
		String ciudad = "";
		String provincia = "";
		String CP = "";
		int tam = 0;
		int habitaciones = 0;
		int banos = 0;
		int precio_Venta = 0;
		int precio_Alquiler = 0;
		String estado = "";
		
		if(campo.equals("tipo")) {
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
			return tipo;
		}
		
		if(campo.equals("direccion")) {
			//Dirección de la casa, es una cadena con el nombre de la calle y su número.
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
			return direccion;
		}
		
		if(campo.equals("ciudad")) {
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
			return ciudad;
		}
		
		if(campo.equals("provincia")) {
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
			return provincia;
		}
		
		if(campo.equals("codigo_postal")) {
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
		}
		
		if(campo.equals("tamano")) {
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
			return tam;
		}
		
		if(campo.equals("habitaciones")) {
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
			return habitaciones;
		}
		
		if(campo.equals("banos")) {
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
			return banos;
		}
		
		if(campo.equals("precio")) {
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
			return precio_Venta;
		}
		
		if(campo.equals("alquiler")) {
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
			return precio_Alquiler;
		}
		
		if(campo.equals("estado")) {
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
			return estado;
		}
		
		if(campo.equals("propietario")) {
			List<Document> propietarios = InsertarDocumentos.ListarPropietarios();
			String propietario = "";
			
			//Obtenemos el identificador del documento del propietario correspondiente en la colección para hacer la referencia
			Document consultaP;
			ObjectId _id;
			DBRef refPropietario = null;
			
			//Si no hay propietarios
			if(propietarios.size() == 0) {
				System.out.println("\tNo hay propietarios disponibles para asociar a la casa.\n");
			} else {
				System.out.print("\nPropietario de la casa (DNI): ");
				
				while(propietario.length() == 0) {
					propietario = sc.nextLine();
					
					if(propietario.isBlank() && propietario.length() > 0) {
						propietario = "";
						System.out.println("----------------------------------------------------------------------------------");
						System.out.println("\tNo se ha introducido el propietario de la casa.\n");
		
						System.out.print("Propietario de la casa (DNI): ");
					} else if(propietario.length() > 0) {
						
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
							
							if(consultaP != null) {
								return refPropietario;
							}
						}
						System.out.print("Propietario de la casa (DNI): ");
					}
				}
			}
		}
		
		if(campo.equals("agente")) {
			//Agente inmobiliario encargado de la casa
			List<Document> agentes = InsertarDocumentos.ListarAgentes();
			String agente = "";
			
			//Obtenemos el identificador del documento del agente inmobiliario correspondiente en la colección para hacer la referencia
			Document consultaAGI;
			ObjectId _id = null;
			DBRef refAgente = null;
			
			if(agentes.size() == 0) {
				System.out.println("\tNo hay agentes inmobiliarios disponibles para asociar a la casa.");
			} else {
				System.out.print("\nAgente inmobiliario de la casa (DNI): ");
				
				while(agente.length() == 0) {
					agente = sc.nextLine();
					
					if(agente.isBlank() && agente.length() > 0) {
						agente = "";
						System.out.println("----------------------------------------------------------------------------------");
						System.out.println("\tNo se ha introducido el agente inmobiliario de la casa.\n");
		
						System.out.print("Agente inmobiliario de la casa (DNI): ");
					} else if(agente.length() > 0) {
						
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
							
							if(consultaAGI != null) {
								return refAgente;
							}
						}
						System.out.print("Agente inmobiliario de la casa (DNI): ");
					}
				}
			}
		}
		
		if(campo.equals("comprador")) {
			List<Document> compradores = InsertarDocumentos.ListarCompradores();
			String comprador = "";
			
			//Obtenemos el identificador del documento del comprador correspondiente en la colección para hacer la referencia
			Document consultaCOM;
			ObjectId _id = null;
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
					} else if(comprador.length() > 0) {
						
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
							
							if(consultaCOM != null) {
								return refComprador;
							}
						}
						System.out.print("Comprador de la casa (DNI): ");
					}
				}
			}
		}
		
		return null;
	}

	public static void camposModificarCasas(Object valorFiltro, boolean condExista, String campoFiltro, List<String> campos, String valorCondicion) {
		//Método que permite al usuario modificar el valor de el campo de la casa escogido.
		System.out.println("CAMPO QUE SE VA A MODIFICAR");
		System.out.println("===========================\n");
		
		for(int i=0; i < campos.size(); i++) {
			String campo = campos.get(i);
			System.out.println((i+1) + ". " + campo);
		}
		System.out.println("\n=========================");
		
		
		//Pedir campos que se van a modificar.
		String modificarCampos = "";
		System.out.print("¿Desea seguir modificando campos? (S/N): ");
		
		while(modificarCampos.length() <= 1) {
			modificarCampos = sc.nextLine();
			
			if(modificarCampos.isBlank() && modificarCampos.length() > 0) {
				modificarCampos = "";
				System.out.println("-------------------------------------------------------------------");
				System.out.println("\t- No se ha especificado si se quiere modificar campos o no.\n");
				
			} else if(modificarCampos.length() > 0) {
				if(!modificarCampos.equals("S") && !modificarCampos.equals("N")) {
					System.out.println("-------------------------------------------------------------------");
					System.out.println("\t- No se ha especificado si se quiere modificar campos o no.\n");
				} else {
					break;
				}
			}
		}
		
		String campo = "";
		if(modificarCampos.equals("S")) {
			System.out.print("Campo a modificar (su nombre y no el 'nº'): ");
			
			while(campo.length() == 0) {
				campo = sc.nextLine();
				
				if(campo.isBlank() && campo.length() > 0) {
					campo = "";
					System.out.println("-------------------------------------------------------------------");
					System.out.println("\t- No se ha introducido el nombre del campo a modficar.\n");
					
					System.out.print("Campo a modificar (su nombre y no el 'nº'): ");
					
				} else if(campo.length() > 0) {
					//Si se ha introducido el campo vamos a comprobar que exista.
					boolean existe = false;
					for(String campoC: campos) {
						if(campoC.equalsIgnoreCase(campo)) {
							existe = true;
							break;
						}
					}
					
					if(existe) {//Si el campo existe salimos del bucle.
						break;
					} else {
						campo = "";
						System.out.println("\n-------------------------------------------------------------------");
						System.out.println("\t\t- Campo inexistente.\n\n");
						
						System.out.print("Campo a modificar (su nombre y no el nº): ");
					}
				}
			}
			
			//Campos
			String tipo = "";
			String direccion = "";
			String ciudad = "";
			String provincia = "";
			String CP = "";
			int tam = 0;
			int habitaciones = 0;
			int banos = 0;
			int precio_Venta = 0;
			int precio_Alquiler = 0;
			String estado = "";
			
			if(campo.equals("tipo")) {
				//Después de tener claro el campo a modificar pedimos los valores a sustituir de los docs.
				Map<String, List<String>> valores = valoresSustituirTipos(campo);
				
				if(valores.size() > 0) {
					System.out.print("Tipo: ");
					
					while(tipo.length() == 0) {
						tipo = sc.nextLine();
						
						if(tipo.isBlank() && tipo.length() > 0) {
							tipo = "";
							System.out.println("----------------------------------------------------------------------------------");
							System.out.println("\tNo se ha introducido el tipo de la casa.\n");
				
							System.out.print("Tipo: ");
						} else if(tipo.length() > 0) {
							break;
						}
					}
					
					/*Para actualizar los documentos correspondientes a este campo recorremos los valores
					  seleccionados para sustituir.*/
					UpdateResult resultado = null;
					
					Collection<List<String>> setKV = valores.values();
					for(Iterator<List<String>> it = setKV.iterator(); it.hasNext();) {
						List<String> valoresSustituir = it.next();
						
						for(int i=0; i < valoresSustituir.size(); i++) {
							//Evaluamos que la actualización se ajuste a las condiciones.
							if(valorCondicion.equalsIgnoreCase("Igual a")) {
								Bson filter = Filters.and(
										Filters.eq(campoFiltro, (String) valorFiltro),
										Filters.eq("tipo", valoresSustituir.get(i)));
								
								Bson update = Updates.set("tipo", tipo);
								resultado = MongoConnection.casas.updateMany(
										filter, 
										update);
							}
							
							if(valorCondicion.equalsIgnoreCase("Mayor que")) {
								Bson filter = Filters.and(
										Filters.gt(campoFiltro, (String) valorFiltro),
										Filters.eq("tipo", valoresSustituir.get(i)));
								
								Bson update = Updates.set("tipo", tipo);
								resultado = MongoConnection.casas.updateMany(
										filter, 
										update);
							}
							
							if(valorCondicion.equalsIgnoreCase("Menor que")) {
								Bson filter = Filters.and(
										Filters.lt(campoFiltro, (String) valorFiltro),
										Filters.eq("tipo", valoresSustituir.get(i)));
								
								Bson update = Updates.set("tipo", tipo);
								resultado = MongoConnection.casas.updateMany(
										filter, 
										update);
							}
							
							if(condExista) {
								Bson filter = Filters.and(
										Filters.exists(campoFiltro),
										Filters.eq("tipo", valoresSustituir.get(i)));
								
								Bson update = Updates.set("tipo", tipo);
								resultado = MongoConnection.casas.updateMany(
										filter, 
										update);
							}
						}
					}
					if(resultado.getModifiedCount() > 0) {
						System.out.println("Tipos actualizados. (" + resultado.getModifiedCount() + ")");
						
						MongoCursor<Document> documentos = MongoConnection.casas.aggregate(Arrays.asList(
								match(new Document("tipo", tipo)))).iterator();
						
						System.out.println("\nCASAS ACTUALIZADAS");
						System.out.println("=========================\n");
						while(documentos.hasNext()) {
							System.out.println(documentos.next().toJson());
						}
						System.out.println();
					}
				}
			}
			
			if(campo.equals("direccion")) {
				//Después de tener claro el campo a modificar pedimos los valores a sustituir de los docs.
				Map<String, List<String>> valores = valoresSustituirDirecciones(campo);
				
				if(valores.size() > 0) {
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
					
					/*Para actualizar los documentos correspondientes a este campo recorremos los valores
					  seleccionados para sustituir.*/
					UpdateResult resultado = null;
					
					Collection<List<String>> setKV = valores.values();
					for(Iterator<List<String>> it = setKV.iterator(); it.hasNext();) {
						List<String> valoresSustituir = it.next();
						
						for(int i=0; i < valoresSustituir.size(); i++) {
							//Evaluamos que la actualización se ajuste a las condiciones.
							if(valorCondicion.equalsIgnoreCase("Igual a")) {
								Bson filter = Filters.and(
										Filters.eq(campoFiltro, (String) valorFiltro),
										Filters.eq("direccion", valoresSustituir.get(i)));
								
								Bson update = Updates.set("direccion", direccion);
								resultado = MongoConnection.casas.updateMany(
										filter, 
										update);
							}
							
							if(valorCondicion.equalsIgnoreCase("Mayor que")) {
								Bson filter = Filters.and(
										Filters.gt(campoFiltro, (String) valorFiltro),
										Filters.eq("direccion", valoresSustituir.get(i)));
								
								Bson update = Updates.set("direccion", direccion);
								resultado = MongoConnection.casas.updateMany(
										filter, 
										update);
							}
							
							if(valorCondicion.equalsIgnoreCase("Menor que")) {
								Bson filter = Filters.and(
										Filters.lt(campoFiltro, (String) valorFiltro),
										Filters.eq("direccion", valoresSustituir.get(i)));
								
								Bson update = Updates.set("direccion", direccion);
								resultado = MongoConnection.casas.updateMany(
										filter, 
										update);
							}
							
							if(condExista) {
								Bson filter = Filters.and(
										Filters.exists(campoFiltro),
										Filters.eq("direccion", valoresSustituir.get(i)));
								
								Bson update = Updates.set("direccion", direccion);
								resultado = MongoConnection.casas.updateMany(
										filter, 
										update);
							}
						}
					}
					if(resultado.getModifiedCount() > 0) {
						System.out.println("Direcciones actualizadas. (" + resultado.getModifiedCount() + ")");
						
						MongoCursor<Document> documentos = MongoConnection.casas.aggregate(Arrays.asList(
								match(new Document("direccion", direccion)))).iterator();
						
						System.out.println("\nCASAS ACTUALIZADAS");
						System.out.println("=========================\n");
						while(documentos.hasNext()) {
							System.out.println(documentos.next().toJson());
						}
						System.out.println();
					}
				}
			}
			
			if(campo.equals("ciudad")) {
				//Después de tener claro el campo a modificar pedimos los valores a sustituir de los docs.
				Map<String, List<String>> valores = valoresSustituirCiudades(campo);
				
				if(valores.size() > 0) {
					System.out.print("Ciudad: ");
					
					while(ciudad.length() == 0) {
						ciudad = sc.nextLine();
						
						if(ciudad.isBlank() && ciudad.length() > 0) {
							ciudad = "";
							System.out.println("----------------------------------------------------------------------------------");
							System.out.println("\tNo se ha introducido la ciudad.\n");
				
							System.out.print("Ciudad: ");
						} else if(ciudad.length() > 0) {
							break;
						}
					}
					
					/*Para actualizar los documentos correspondientes a este campo recorremos los valores
					  seleccionados para sustituir.*/
					UpdateResult resultado = null;
					
					Collection<List<String>> setKV = valores.values();
					for(Iterator<List<String>> it = setKV.iterator(); it.hasNext();) {
						List<String> valoresSustituir = it.next();
						
						for(int i=0; i < valoresSustituir.size(); i++) {
							//Evaluamos que la actualización se ajuste a las condiciones.
							if(valorCondicion.equalsIgnoreCase("Igual a")) {
								Bson filter = Filters.and(
										Filters.eq(campoFiltro, (String) valorFiltro),
										Filters.eq("ciudad", valoresSustituir.get(i)));
								
								Bson update = Updates.set("ciudad", ciudad);
								resultado = MongoConnection.casas.updateMany(
										filter, 
										update);
							}
							
							if(valorCondicion.equalsIgnoreCase("Mayor que")) {
								Bson filter = Filters.and(
										Filters.gt(campoFiltro, (String) valorFiltro),
										Filters.eq("ciudad", valoresSustituir.get(i)));
								
								Bson update = Updates.set("ciudad", ciudad);
								resultado = MongoConnection.casas.updateMany(
										filter, 
										update);
							}
							
							if(valorCondicion.equalsIgnoreCase("Menor que")) {
								Bson filter = Filters.and(
										Filters.lt(campoFiltro, (String) valorFiltro),
										Filters.eq("ciudad", valoresSustituir.get(i)));
								
								Bson update = Updates.set("ciudad", ciudad);
								resultado = MongoConnection.casas.updateMany(
										filter, 
										update);
							}
							
							if(condExista) {
								Bson filter = Filters.and(
										Filters.exists(campoFiltro),
										Filters.eq("ciudad", valoresSustituir.get(i)));
								
								Bson update = Updates.set("ciudad", ciudad);
								resultado = MongoConnection.casas.updateMany(
										filter, 
										update);
							}
						}
					}
					if(resultado.getModifiedCount() > 0) {
						System.out.println("Ciudades actualizadas. (" + resultado.getModifiedCount() + ")");
						
						MongoCursor<Document> documentos = MongoConnection.casas.aggregate(Arrays.asList(
								match(new Document("ciudad", ciudad)))).iterator();
						
						System.out.println("\nCASAS ACTUALIZADAS");
						System.out.println("=========================\n");
						while(documentos.hasNext()) {
							System.out.println(documentos.next().toJson());
						}
						System.out.println();
					}
				}
			}
			
			if(campo.equals("provincia")) {
				//Después de tener claro el campo a modificar pedimos los valores a sustituir de los docs.
				Map<String, List<String>> valores = valoresSustituirProvincias(campo);
				
				if(valores.size() > 0) {
					System.out.print("Provincia: ");
					
					while(provincia.length() == 0) {
						provincia = sc.nextLine();
						
						if(provincia.isBlank() && provincia.length() > 0) {
							provincia = "";
							System.out.println("----------------------------------------------------------------------------------");
							System.out.println("\tNo se ha introducido la provincia.\n");
				
							System.out.print("Provincia: ");
						} else if(provincia.length() > 0) {
							break;
						}
					}
					
					/*Para actualizar los documentos correspondientes a este campo recorremos los valores
					  seleccionados para sustituir.*/
					UpdateResult resultado = null;
					
					Collection<List<String>> setKV = valores.values();
					for(Iterator<List<String>> it = setKV.iterator(); it.hasNext();) {
						List<String> valoresSustituir = it.next();
						
						for(int i=0; i < valoresSustituir.size(); i++) {
							//Evaluamos que la actualización se ajuste a las condiciones.
							if(valorCondicion.equalsIgnoreCase("Igual a")) {
								Bson filter = Filters.and(
										Filters.eq(campoFiltro, (String) valorFiltro),
										Filters.eq("provincia", valoresSustituir.get(i)));
								
								Bson update = Updates.set("provincia", provincia);
								resultado = MongoConnection.casas.updateMany(
										filter, 
										update);
							}
							
							if(valorCondicion.equalsIgnoreCase("Mayor que")) {
								Bson filter = Filters.and(
										Filters.gt(campoFiltro, (String) valorFiltro),
										Filters.eq("provincia", valoresSustituir.get(i)));
								
								Bson update = Updates.set("provincia", provincia);
								resultado = MongoConnection.casas.updateMany(
										filter, 
										update);
							}
							
							if(valorCondicion.equalsIgnoreCase("Menor que")) {
								Bson filter = Filters.and(
										Filters.lt(campoFiltro, (String) valorFiltro),
										Filters.eq("provincia", valoresSustituir.get(i)));
								
								Bson update = Updates.set("provincia", provincia);
								resultado = MongoConnection.casas.updateMany(
										filter, 
										update);
							}
							
							if(condExista) {
								Bson filter = Filters.and(
										Filters.exists(campoFiltro),
										Filters.eq("provincia", valoresSustituir.get(i)));
								
								Bson update = Updates.set("provincia", provincia);
								resultado = MongoConnection.casas.updateMany(
										filter, 
										update);
							}
						}
					}
					if(resultado.getModifiedCount() > 0) {
						System.out.println("Provincias actualizadas. (" + resultado.getModifiedCount() + ")");
						
						MongoCursor<Document> documentos = MongoConnection.casas.aggregate(Arrays.asList(
								match(new Document("provincia", provincia)))).iterator();
						
						System.out.println("\nCASAS ACTUALIZADAS");
						System.out.println("=========================\n");
						while(documentos.hasNext()) {
							System.out.println(documentos.next().toJson());
						}
						System.out.println();
					}
				}
			}
			
			if(campo.equals("codigo_postal")) {
				//Después de tener claro el campo a modificar pedimos los valores a sustituir de los docs.
				Map<String, List<String>> valores = valoresSustituirCPs(campo);
				
				if(valores.size() > 0) {
					System.out.print("Código Postal: ");
					
					while(CP.length() == 0) {
						CP = sc.nextLine();
								
						if(CP.isBlank() && CP.length() > 0) {
							CP = "";
							System.out.println("----------------------------------------------------------------------------------");
							System.out.println("\tNo se ha introducido el código postal.\n");
				
							System.out.print("Código Postal: ");
						} else if(CP.length() > 0) {
							break;
						}
					}
					
					/*Para actualizar los documentos correspondientes a este campo recorremos los valores
					  seleccionados para sustituir.*/
					UpdateResult resultado = null;
					
					Collection<List<String>> setKV = valores.values();
					for(Iterator<List<String>> it = setKV.iterator(); it.hasNext();) {
						List<String> valoresSustituir = it.next();
						
						for(int i=0; i < valoresSustituir.size(); i++) {
							//Evaluamos que la actualización se ajuste a las condiciones.
							if(valorCondicion.equalsIgnoreCase("Igual a")) {
								Bson filter = Filters.and(
										Filters.eq(campoFiltro, (String) valorFiltro),
										Filters.eq("codigo_postal", valoresSustituir.get(i)));
								
								Bson update = Updates.set("codigo_postal", CP);
								resultado = MongoConnection.casas.updateMany(
										filter, 
										update);
							}
							
							if(valorCondicion.equalsIgnoreCase("Mayor que")) {
								Bson filter = Filters.and(
										Filters.gt(campoFiltro, (String) valorFiltro),
										Filters.eq("codigo_postal", valoresSustituir.get(i)));
								
								Bson update = Updates.set("codigo_postal", CP);
								resultado = MongoConnection.casas.updateMany(
										filter, 
										update);
							}
							
							if(valorCondicion.equalsIgnoreCase("Menor que")) {
								Bson filter = Filters.and(
										Filters.lt(campoFiltro, (String) valorFiltro),
										Filters.eq("codigo_postal", valoresSustituir.get(i)));
								
								Bson update = Updates.set("codigo_postal", CP);
								resultado = MongoConnection.casas.updateMany(
										filter, 
										update);
							}
							
							if(condExista) {
								Bson filter = Filters.and(
										Filters.exists(campoFiltro),
										Filters.eq("codigo_postal", valoresSustituir.get(i)));
								
								Bson update = Updates.set("codigo_postal", CP);
								resultado = MongoConnection.casas.updateMany(
										filter, 
										update);
							}
						}
					}
					if(resultado.getModifiedCount() > 0) {
						System.out.println("Códigos postales actualizados. (" + resultado.getModifiedCount() + ")");
						
						MongoCursor<Document> documentos = MongoConnection.casas.aggregate(Arrays.asList(
								match(new Document("codigo_postal", CP)))).iterator();
						
						System.out.println("\nCASAS ACTUALIZADAS");
						System.out.println("=========================\n");
						while(documentos.hasNext()) {
							System.out.println(documentos.next().toJson());
						}
						System.out.println();
					}
				}
			}
			
			if(campo.equals("tamano")) {
				//Después de tener claro el campo a modificar pedimos los valores a sustituir de los docs.
				Map<String, List<Integer>> valores = valoresSustituirTamanos(campo);
				
				if(valores.size() > 0) {
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
					
					/*Para actualizar los documentos correspondientes a este campo recorremos los valores
					  seleccionados para sustituir.*/
					UpdateResult resultado = null;
					
					Collection<List<Integer>> setKV = valores.values();
					for(Iterator<List<Integer>> it = setKV.iterator(); it.hasNext();) {
						List<Integer> valoresSustituir = it.next();
						
						for(int i=0; i < valoresSustituir.size(); i++) {
							//Evaluamos que la actualización se ajuste a las condiciones.
							if(valorCondicion.equalsIgnoreCase("Igual a")) {
								Bson filter = Filters.and(
										Filters.eq(campoFiltro, (int) valorFiltro),
										Filters.eq("tamano", valoresSustituir.get(i)));
								
								Bson update = Updates.set("tamano", tam);
								resultado = MongoConnection.casas.updateMany(
										filter, 
										update);
							}
							
							if(valorCondicion.equalsIgnoreCase("Mayor que")) {
								Bson filter = Filters.and(
										Filters.gt(campoFiltro, (int) valorFiltro),
										Filters.eq("tamano", valoresSustituir.get(i)));
								
								Bson update = Updates.set("tamano", tam);
								resultado = MongoConnection.casas.updateMany(
										filter, 
										update);
							}
							
							if(valorCondicion.equalsIgnoreCase("Menor que")) {
								Bson filter = Filters.and(
										Filters.lt(campoFiltro, (int) valorFiltro),
										Filters.eq("tamano", valoresSustituir.get(i)));
								
								Bson update = Updates.set("tamano", tam);
								resultado = MongoConnection.casas.updateMany(
										filter, 
										update);
							}
							
							if(condExista) {
								Bson filter = Filters.and(
										Filters.exists(campoFiltro),
										Filters.eq("tamano", valoresSustituir.get(i)));
								
								Bson update = Updates.set("tamano", tam);
								resultado = MongoConnection.casas.updateMany(
										filter, 
										update);
							}
						}
					}
					if(resultado.getModifiedCount() > 0) {
						System.out.println("Tamaños actualizados. (" + resultado.getModifiedCount() + ")");
						
						MongoCursor<Document> documentos = MongoConnection.casas.aggregate(Arrays.asList(
								match(new Document("tamano", tam)))).iterator();
						
						System.out.println("\nCASAS ACTUALIZADAS");
						System.out.println("=========================\n");
						while(documentos.hasNext()) {
							System.out.println(documentos.next().toJson());
						}
						System.out.println();
					}
				}
			}
			
			if(campo.equals("habitaciones")) {
				//Después de tener claro el campo a modificar pedimos los valores a sustituir de los docs.
				Map<String, List<Integer>> valores = valoresSustituirHabitaciones(campo);
				
				if(valores.size() > 0) {
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
					
					/*Para actualizar los documentos correspondientes a este campo recorremos los valores
					  seleccionados para sustituir.*/
					UpdateResult resultado = null;
					
					Collection<List<Integer>> setKV = valores.values();
					for(Iterator<List<Integer>> it = setKV.iterator(); it.hasNext();) {
						List<Integer> valoresSustituir = it.next();
						
						for(int i=0; i < valoresSustituir.size(); i++) {
							//Evaluamos que la actualización se ajuste a las condiciones.
							if(valorCondicion.equalsIgnoreCase("Igual a")) {
								Bson filter = Filters.and(
										Filters.eq(campoFiltro, (int) valorFiltro),
										Filters.eq("habitaciones", valoresSustituir.get(i)));
								
								Bson update = Updates.set("habitaciones", habitaciones);
								resultado = MongoConnection.casas.updateMany(
										filter, 
										update);
							}
							
							if(valorCondicion.equalsIgnoreCase("Mayor que")) {
								Bson filter = Filters.and(
										Filters.gt(campoFiltro, (int) valorFiltro),
										Filters.eq("habitaciones", valoresSustituir.get(i)));
								
								Bson update = Updates.set("habitaciones", habitaciones);
								resultado = MongoConnection.casas.updateMany(
										filter, 
										update);
							}
							
							if(valorCondicion.equalsIgnoreCase("Menor que")) {
								Bson filter = Filters.and(
										Filters.lt(campoFiltro, (int) valorFiltro),
										Filters.eq("habitaciones", valoresSustituir.get(i)));
								
								Bson update = Updates.set("habitaciones", habitaciones);
								resultado = MongoConnection.casas.updateMany(
										filter, 
										update);
							}
							
							if(condExista) {
								Bson filter = Filters.and(
										Filters.exists(campoFiltro),
										Filters.eq("habitaciones", valoresSustituir.get(i)));
								
								Bson update = Updates.set("habitaciones", habitaciones);
								resultado = MongoConnection.casas.updateMany(
										filter, 
										update);
							}
						}
					}
					if(resultado.getModifiedCount() > 0) {
						System.out.println("NºHabitaciones actualizado. (" + resultado.getModifiedCount() + ")");
						
						MongoCursor<Document> documentos = MongoConnection.casas.aggregate(Arrays.asList(
								match(new Document("habitaciones", habitaciones)))).iterator();
						
						System.out.println("\nCASAS ACTUALIZADAS");
						System.out.println("=========================\n");
						while(documentos.hasNext()) {
							System.out.println(documentos.next().toJson());
						}
						System.out.println();
					}
				}
			}
			
			if(campo.equals("banos")) {
				//Después de tener claro el campo a modificar pedimos los valores a sustituir de los docs.
				Map<String, List<Integer>> valores = valoresSustituirBanos(campo);
				
				if(valores.size() > 0) {
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
					
					/*Para actualizar los documentos correspondientes a este campo recorremos los valores
					  seleccionados para sustituir.*/
					UpdateResult resultado = null;
					
					Collection<List<Integer>> setKV = valores.values();
					for(Iterator<List<Integer>> it = setKV.iterator(); it.hasNext();) {
						List<Integer> valoresSustituir = it.next();
						
						for(int i=0; i < valoresSustituir.size(); i++) {
							//Evaluamos que la actualización se ajuste a las condiciones.
							if(valorCondicion.equalsIgnoreCase("Igual a")) {
								Bson filter = Filters.and(
										Filters.eq(campoFiltro, (int) valorFiltro),
										Filters.eq("banos", valoresSustituir.get(i)));
								
								Bson update = Updates.set("banos", banos);
								resultado = MongoConnection.casas.updateMany(
										filter, 
										update);
							}
							
							if(valorCondicion.equalsIgnoreCase("Mayor que")) {
								Bson filter = Filters.and(
										Filters.gt(campoFiltro, (int) valorFiltro),
										Filters.eq("banos", valoresSustituir.get(i)));
								
								Bson update = Updates.set("banos", banos);
								resultado = MongoConnection.casas.updateMany(
										filter, 
										update);
							}
							
							if(valorCondicion.equalsIgnoreCase("Menor que")) {
								Bson filter = Filters.and(
										Filters.lt(campoFiltro, (int) valorFiltro),
										Filters.eq("banos", valoresSustituir.get(i)));
								
								Bson update = Updates.set("banos", banos);
								resultado = MongoConnection.casas.updateMany(
										filter, 
										update);
							}
							
							if(condExista) {
								Bson filter = Filters.and(
										Filters.exists(campoFiltro),
										Filters.eq("banos", valoresSustituir.get(i)));
								
								Bson update = Updates.set("banos", banos);
								resultado = MongoConnection.casas.updateMany(
										filter, 
										update);
							}
						}
					}
					if(resultado.getModifiedCount() > 0) {
						System.out.println("NºBaños actualizado. (" + resultado.getModifiedCount() + ")");
						
						MongoCursor<Document> documentos = MongoConnection.casas.aggregate(Arrays.asList(
								match(new Document("banos", banos)))).iterator();
						
						System.out.println("\nCASAS ACTUALIZADAS");
						System.out.println("=========================\n");
						while(documentos.hasNext()) {
							System.out.println(documentos.next().toJson());
						}
						System.out.println();
					}
				}
			}
			
			if(campo.equals("precio")) {
				//Después de tener claro el campo a modificar pedimos los valores a sustituir de los docs.
				Map<String, List<Integer>> valores = valoresSustituirPreciosVenta(campo);
				
				if(valores.size() > 0) {
					while(true) {
						System.out.print("Precio de venta: ");
						try {
							precio_Venta = sc.nextInt();
						} catch (InputMismatchException e) {
							sc.nextLine();
							
							System.out.println("----------------------------------------");
							System.out.println("\tPrecio inválido\n");
							
							continue;
						}
						break;
					}
					
					/*Para actualizar los documentos correspondientes a este campo recorremos los valores
					  seleccionados para sustituir.*/
					UpdateResult resultado = null;
					
					Collection<List<Integer>> setKV = valores.values();
					for(Iterator<List<Integer>> it = setKV.iterator(); it.hasNext();) {
						List<Integer> valoresSustituir = it.next();
						
						for(int i=0; i < valoresSustituir.size(); i++) {
							//Evaluamos que la actualización se ajuste a las condiciones.
							if(valorCondicion.equalsIgnoreCase("Igual a")) {
								Bson filter = Filters.and(
										Filters.eq(campoFiltro, (int) valorFiltro),
										Filters.eq("precio", valoresSustituir.get(i)));
								
								Bson update = Updates.set("precio", precio_Venta);
								resultado = MongoConnection.casas.updateMany(
										filter, 
										update);
							}
							
							if(valorCondicion.equalsIgnoreCase("Mayor que")) {
								Bson filter = Filters.and(
										Filters.gt(campoFiltro, (int) valorFiltro),
										Filters.eq("precio", valoresSustituir.get(i)));
								
								Bson update = Updates.set("precio", precio_Venta);
								resultado = MongoConnection.casas.updateMany(
										filter, 
										update);
							}
							
							if(valorCondicion.equalsIgnoreCase("Menor que")) {
								Bson filter = Filters.and(
										Filters.lt(campoFiltro, (int) valorFiltro),
										Filters.eq("precio", valoresSustituir.get(i)));
								
								Bson update = Updates.set("precio", precio_Venta);
								resultado = MongoConnection.casas.updateMany(
										filter, 
										update);
							}
							
							if(condExista) {
								Bson filter = Filters.and(
										Filters.exists(campoFiltro),
										Filters.eq("precio", valoresSustituir.get(i)));
								
								Bson update = Updates.set("precio", precio_Venta);
								resultado = MongoConnection.casas.updateMany(
										filter, 
										update);
							}
						}
					}
					if(resultado.getModifiedCount() > 0) {
						System.out.println("Precios de venta actualizados. (" + resultado.getModifiedCount() + ")");
						
						MongoCursor<Document> documentos = MongoConnection.casas.aggregate(Arrays.asList(
								match(new Document("precio", precio_Venta)))).iterator();
						
						System.out.println("\nCASAS ACTUALIZADAS");
						System.out.println("=========================\n");
						while(documentos.hasNext()) {
							System.out.println(documentos.next().toJson());
						}
						System.out.println();
					}
				}
			}
			
			if(campo.equals("alquiler")) {
				//Después de tener claro el campo a modificar pedimos los valores a sustituir de los docs.
				Map<String, List<Integer>> valores = valoresSustituirPreciosAlquiler(campo);
				
				if(valores.size() > 0) {
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
					
					/*Para actualizar los documentos correspondientes a este campo recorremos los valores
					  seleccionados para sustituir.*/
					UpdateResult resultado = null;
					
					Collection<List<Integer>> setKV = valores.values();
					for(Iterator<List<Integer>> it = setKV.iterator(); it.hasNext();) {
						List<Integer> valoresSustituir = it.next();
						
						for(int i=0; i < valoresSustituir.size(); i++) {
							//Evaluamos que la actualización se ajuste a las condiciones.
							if(valorCondicion.equalsIgnoreCase("Igual a")) {
								Bson filter = Filters.and(
										Filters.eq(campoFiltro, (int) valorFiltro),
										Filters.eq("alquiler", valoresSustituir.get(i)));
								
								Bson update = Updates.set("alquiler", precio_Alquiler);
								resultado = MongoConnection.casas.updateMany(
										filter, 
										update);
							}
							
							if(valorCondicion.equalsIgnoreCase("Mayor que")) {
								Bson filter = Filters.and(
										Filters.gt(campoFiltro, (int) valorFiltro),
										Filters.eq("alquiler", valoresSustituir.get(i)));
								
								Bson update = Updates.set("alquiler", precio_Alquiler);
								resultado = MongoConnection.casas.updateMany(
										filter, 
										update);
							}
							
							if(valorCondicion.equalsIgnoreCase("Menor que")) {
								Bson filter = Filters.and(
										Filters.lt(campoFiltro, (int) valorFiltro),
										Filters.eq("alquiler", valoresSustituir.get(i)));
								
								Bson update = Updates.set("alquiler", precio_Alquiler);
								resultado = MongoConnection.casas.updateMany(
										filter, 
										update);
							}
							
							if(condExista) {
								Bson filter = Filters.and(
										Filters.exists(campoFiltro),
										Filters.eq("alquiler", valoresSustituir.get(i)));
								
								Bson update = Updates.set("alquiler", precio_Alquiler);
								resultado = MongoConnection.casas.updateMany(
										filter, 
										update);
							}
						}
					}
					if(resultado.getModifiedCount() > 0) {
						System.out.println("Precios de alquiler actualizados. (" + resultado.getModifiedCount() + ")");
						
						MongoCursor<Document> documentos = MongoConnection.casas.aggregate(Arrays.asList(
								match(new Document("alquiler", precio_Alquiler)))).iterator();
						
						System.out.println("\nCASAS ACTUALIZADAS");
						System.out.println("=========================\n");
						while(documentos.hasNext()) {
							System.out.println(documentos.next().toJson());
						}
						System.out.println();
					}
				}
			}
			
			if(campo.equals("estado")) {
				//Después de tener claro el campo a modificar pedimos los valores a sustituir de los docs.
				Map<String, List<String>> valores = valoresSustituirEstados(campo);
				
				if(valores.size() > 0) {
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
					
					/*Para actualizar los documentos correspondientes a este campo recorremos los valores
					  seleccionados para sustituir.*/
					UpdateResult resultado = null;
					
					Collection<List<String>> setKV = valores.values();
					for(Iterator<List<String>> it = setKV.iterator(); it.hasNext();) {
						List<String> valoresSustituir = it.next();
						
						for(int i=0; i < valoresSustituir.size(); i++) {
							//Evaluamos que la actualización se ajuste a las condiciones.
							if(valorCondicion.equalsIgnoreCase("Igual a")) {
								Bson filter = Filters.and(
										Filters.eq(campoFiltro, (String) valorFiltro),
										Filters.eq("estado", valoresSustituir.get(i)));
								
								Bson update = Updates.set("estado", estado);
								resultado = MongoConnection.casas.updateMany(
										filter, 
										update);
							}
							
							if(valorCondicion.equalsIgnoreCase("Mayor que")) {
								Bson filter = Filters.and(
										Filters.gt(campoFiltro, (String) valorFiltro),
										Filters.eq("estado", valoresSustituir.get(i)));
								
								Bson update = Updates.set("estado", estado);
								resultado = MongoConnection.casas.updateMany(
										filter, 
										update);
							}
							
							if(valorCondicion.equalsIgnoreCase("Menor que")) {
								Bson filter = Filters.and(
										Filters.lt(campoFiltro, (String) valorFiltro),
										Filters.eq("estado", valoresSustituir.get(i)));
								
								Bson update = Updates.set("estado", estado);
								resultado = MongoConnection.casas.updateMany(
										filter, 
										update);
							}
							
							if(condExista) {
								Bson filter = Filters.and(
										Filters.exists(campoFiltro),
										Filters.eq("estado", valoresSustituir.get(i)));
								
								Bson update = Updates.set("estado", estado);
								resultado = MongoConnection.casas.updateMany(
										filter, 
										update);
							}
						}
					}
					if(resultado.getModifiedCount() > 0) {
						System.out.println("Estados actualizados. (" + resultado.getModifiedCount() + ")");
						
						MongoCursor<Document> documentos = MongoConnection.casas.aggregate(Arrays.asList(
								match(new Document("estado", estado)))).iterator();
						
						System.out.println("\nCASAS ACTUALIZADAS");
						System.out.println("=========================\n");
						while(documentos.hasNext()) {
							System.out.println(documentos.next().toJson());
						}
						System.out.println();
					}
				}
			}
			
			if(campo.equals("propietario")) {
				//Después de tener claro el campo a modificar pedimos los valores a sustituir de los docs.
				Map<String, List<DBRef>> valores = valoresSustituirPropietarios(campo);
				
				if(valores.size() > 0) {
					//Propietario de la casa
					List<Document> propietarios = InsertarDocumentos.ListarPropietarios();
					String propietario = "";
					
					//Obtenemos el identificador del documento del propietario correspondiente en la colección para hacer la referencia
					Document consultaP;
					ObjectId _id;
					DBRef refPropietario = null;
					
					//Si no hay propietarios
					if(propietarios.size() == 0) {
						System.out.println("\tNo hay propietarios disponibles para asociar a la casa.\n");
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
									
									if(consultaP != null) {
										break;
									}
								}
								System.out.print("Propietario de la casa (DNI): ");
							}
						}
					}
					
					/*Para actualizar los documentos correspondientes a este campo recorremos los valores
					  seleccionados para sustituir.*/
					UpdateResult resultado = null;
					
					Collection<List<DBRef>> setKV = valores.values();
					for(Iterator<List<DBRef>> it = setKV.iterator(); it.hasNext();) {
						List<DBRef> valoresSustituir = it.next();
						
						for(int i=0; i < valoresSustituir.size(); i++) {
							//Evaluamos que la actualización se ajuste a las condiciones.
							if(valorCondicion.equalsIgnoreCase("Igual a")) {
								Bson filter = Filters.and(
										Filters.eq(campoFiltro, (DBRef) valorFiltro),
										Filters.eq("propietario", valoresSustituir.get(i)));
								
								Bson update = Updates.set("propietario", refPropietario);
								resultado = MongoConnection.casas.updateMany(
										filter, 
										update);
							}
							
							if(valorCondicion.equalsIgnoreCase("Mayor que")) {
								Bson filter = Filters.and(
										Filters.gt(campoFiltro, (DBRef) valorFiltro),
										Filters.eq("propietario", valoresSustituir.get(i)));
								
								Bson update = Updates.set("propietario", refPropietario);
								resultado = MongoConnection.casas.updateMany(
										filter, 
										update);
							}
							
							if(valorCondicion.equalsIgnoreCase("Menor que")) {
								Bson filter = Filters.and(
										Filters.lt(campoFiltro, (DBRef) valorFiltro),
										Filters.eq("propietario", valoresSustituir.get(i)));
								
								Bson update = Updates.set("propietario", refPropietario);
								resultado = MongoConnection.casas.updateMany(
										filter, 
										update);
							}
							
							if(condExista) {
								Bson filter = Filters.and(
										Filters.exists(campoFiltro),
										Filters.eq("propietario", valoresSustituir.get(i)));
								
								Bson update = Updates.set("propietario", refPropietario);
								resultado = MongoConnection.casas.updateMany(
										filter, 
										update);
							}
						}
					}
					if(resultado.getModifiedCount() > 0) {
						System.out.println("Propietarios actualizados. (" + resultado.getModifiedCount() + ")");
						
						MongoCursor<Document> documentos = MongoConnection.casas.aggregate(Arrays.asList(
								match(new Document("propietario", refPropietario)))).iterator();
						
						System.out.println("\nCASAS ACTUALIZADAS");
						System.out.println("=========================\n");
						while(documentos.hasNext()) {
							Document casa = documentos.next();
							
							//Sacar referencia
							DBRef refPropietarioC = (DBRef) casa.get("propietario");
							if(refPropietarioC != null) {
								ObjectId _idP = (ObjectId) refPropietarioC.getId();
								Document propietarioC = MongoConnection.propietarios.find(new Document("_id", _idP)).first();
								
								if(propietarioC != null) {
									System.out.println(propietarioC.toJson());
								}
							}
						}
						System.out.println();
					}
				}
			}
			
			if(campo.equals("agente")) {
				//Después de tener claro el campo a modificar pedimos los valores a sustituir de los docs.
				Map<String, List<DBRef>> valores = valoresSustituirAgentes(campo);
				
				if(valores.size() > 0) {
					//Agente de la casa
					List<Document> agentes = InsertarDocumentos.ListarAgentes();
					String agente = "";
					
					//Obtenemos el identificador del documento del agente inmobiliario correspondiente en la colección para hacer la referencia
					Document consultaAGI;
					ObjectId _id;
					DBRef refAgente = null;
					
					if(agentes.size() == 0) {
						System.out.println("\tNo hay agentes inmobiliarios disponibles para asociar a la casa.");
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
									
									if(consultaAGI != null) {
										break;
									}
								}
								System.out.print("Agente inmobiliario de la casa (DNI): ");
							}
						}
					}
					
					/*Para actualizar los documentos correspondientes a este campo recorremos los valores
					  seleccionados para sustituir.*/
					UpdateResult resultado = null;
					
					Collection<List<DBRef>> setKV = valores.values();
					for(Iterator<List<DBRef>> it = setKV.iterator(); it.hasNext();) {
						List<DBRef> valoresSustituir = it.next();
						
						for(int i=0; i < valoresSustituir.size(); i++) {
							//Evaluamos que la actualización se ajuste a las condiciones.
							if(valorCondicion.equalsIgnoreCase("Igual a")) {
								Bson filter = Filters.and(
										Filters.eq(campoFiltro, (DBRef) valorFiltro),
										Filters.eq("agente", valoresSustituir.get(i)));
								
								Bson update = Updates.set("agente", refAgente);
								resultado = MongoConnection.casas.updateMany(
										filter, 
										update);
							}
							
							if(valorCondicion.equalsIgnoreCase("Mayor que")) {
								Bson filter = Filters.and(
										Filters.gt(campoFiltro, (DBRef) valorFiltro),
										Filters.eq("agente", valoresSustituir.get(i)));
								
								Bson update = Updates.set("agente", refAgente);
								resultado = MongoConnection.casas.updateMany(
										filter, 
										update);
							}
							
							if(valorCondicion.equalsIgnoreCase("Menor que")) {
								Bson filter = Filters.and(
										Filters.lt(campoFiltro, (DBRef) valorFiltro),
										Filters.eq("agente", valoresSustituir.get(i)));
								
								Bson update = Updates.set("agente", refAgente);
								resultado = MongoConnection.casas.updateMany(
										filter, 
										update);
							}
							
							if(condExista) {
								Bson filter = Filters.and(
										Filters.exists(campoFiltro),
										Filters.eq("agente", valoresSustituir.get(i)));
								
								Bson update = Updates.set("agente", refAgente);
								resultado = MongoConnection.casas.updateMany(
										filter, 
										update);
							}
						}
					}
					if(resultado.getModifiedCount() > 0) {
						System.out.println("Agentes actualizados. (" + resultado.getModifiedCount() + ")");
						
						MongoCursor<Document> documentos = MongoConnection.casas.aggregate(Arrays.asList(
								match(new Document("agente", refAgente)))).iterator();
						
						System.out.println("\nCASAS ACTUALIZADAS");
						System.out.println("=========================\n");
						while(documentos.hasNext()) {
							Document casa = documentos.next();
							
							//Sacar referencia
							DBRef refAgenteC = (DBRef) casa.get("agente");
							if(refAgenteC != null) {
								ObjectId _idA = (ObjectId) refAgenteC.getId();
								Document agenteC = MongoConnection.agentes.find(new Document("_id", _idA)).first();
								
								if(agenteC != null) {
									System.out.println(agenteC.toJson());
								}
							}
						}
						System.out.println();
					}
				}
			}
			
			if(campo.equals("comprador")) {
				//Después de tener claro el campo a modificar pedimos los valores a sustituir de los docs.
				Map<String, List<DBRef>> valores = valoresSustituirCompradores(campo);
				
				if(valores.size() > 0) {
					//Comprador de la casa
					List<Document> compradores = InsertarDocumentos.ListarCompradores();
					String comprador = "";
					
					//Obtenemos el identificador del documento del comprador correspondiente en la colección para hacer la referencia
					Document consultaCOM;
					ObjectId _id;
					DBRef refComprador = null;
					
					if(compradores.size() == 0) {
						System.out.println("\n\tNo hay compradores disponibles para asociar a la casa.");
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
									//Consultamos el comprador con el DNI introducido, si lo encuentra salimos del bucle.
									consultaCOM = MongoConnection.compradores.find(new Document("DNI", comprador)).first();
									_id = consultaCOM.getObjectId("_id");
									refComprador = new DBRef("Compradores", _id);
									
									if(consultaCOM != null) {
										break;
									}
								}
								System.out.print("Comprador de la casa (DNI): ");
							}
						}
					}
					
					/*Para actualizar los documentos correspondientes a este campo recorremos los valores
					  seleccionados para sustituir.*/
					UpdateResult resultado = null;
					
					Collection<List<DBRef>> setKV = valores.values();
					for(Iterator<List<DBRef>> it = setKV.iterator(); it.hasNext();) {
						List<DBRef> valoresSustituir = it.next();
						
						for(int i=0; i < valoresSustituir.size(); i++) {
							//Evaluamos que la actualización se ajuste a las condiciones.
							if(valorCondicion.equalsIgnoreCase("Igual a")) {
								Bson filter = Filters.and(
										Filters.eq(campoFiltro, (DBRef) valorFiltro),
										Filters.eq("comprador", valoresSustituir.get(i)));
								
								Bson update = Updates.set("comprador", refComprador);
								resultado = MongoConnection.casas.updateMany(
										filter, 
										update);
							}
							
							if(valorCondicion.equalsIgnoreCase("Mayor que")) {
								Bson filter = Filters.and(
										Filters.gt(campoFiltro, (DBRef) valorFiltro),
										Filters.eq("comprador", valoresSustituir.get(i)));
								
								Bson update = Updates.set("comprador", refComprador);
								resultado = MongoConnection.casas.updateMany(
										filter, 
										update);
							}
							
							if(valorCondicion.equalsIgnoreCase("Menor que")) {
								Bson filter = Filters.and(
										Filters.lt(campoFiltro, (DBRef) valorFiltro),
										Filters.eq("comprador", valoresSustituir.get(i)));
								
								Bson update = Updates.set("comprador", refComprador);
								resultado = MongoConnection.casas.updateMany(
										filter, 
										update);
							}
							
							if(condExista) {
								Bson filter = Filters.and(
										Filters.exists(campoFiltro),
										Filters.eq("comprador", valoresSustituir.get(i)));
								
								Bson update = Updates.set("comprador", refComprador);
								resultado = MongoConnection.casas.updateMany(
										filter, 
										update);
							}
						}
					}
					if(resultado.getModifiedCount() > 0) {
						System.out.println("Compradores actualizados. (" + resultado.getModifiedCount() + ")");
						
						MongoCursor<Document> documentos = MongoConnection.casas.aggregate(Arrays.asList(
								match(new Document("comprador", refComprador)))).iterator();
						
						System.out.println("\nCASAS ACTUALIZADAS");
						System.out.println("=========================\n");
						while(documentos.hasNext()) {
							Document casa = documentos.next();
							
							//Sacar referencia
							DBRef refCompradorC = (DBRef) casa.get("comprador");
							if(refCompradorC != null) {
								ObjectId _idCOM = (ObjectId) refCompradorC.getId();
								Document compradorC = MongoConnection.agentes.find(new Document("_id", _idCOM)).first();
								
								if(compradorC != null) {
									System.out.println(compradorC.toJson());
								}
							}
						}
						System.out.println();
					}
				}
			}
		}
	}

	public static Map<String, List<String>> valoresSustituirPreferencias(String campo) {
		//Método encargado de que el usuario indique los valores a sustituir del campo preferencias en los documentos.
		Map<String, List<String>> valores = new LinkedHashMap<>();
		List<String> valoresPreferencias = new ArrayList<String>();
		
		String preferencias = "";
		System.out.print("Valor del campo 'preferencias' a sustituir de los documentos ('.' para salir): ");
						
		while(!preferencias.equals(".")) {
			preferencias = sc.nextLine();
															
			if(preferencias.isBlank() && preferencias.length() > 0) {
				preferencias = "";
				System.out.println("----------------------------------------------------------------------------------");
				System.out.println("\tNo se han introducido las preferencias del comprador.\n");
										
				System.out.print("Valor del campo 'preferencias' a sustituir de los documentos ('.' para salir): ");
			
			} else if(preferencias.length() > 0) {
				if(!preferencias.equals(".")) {
					valoresPreferencias.add(preferencias);
					System.out.print("Valor del campo 'preferencias' a sustituir de los documentos ('.' para salir): ");
				} else {
					valores.put(campo, valoresPreferencias);
					break;
				}
			}
		}
		
		return valores;
	}

	public static Map<String, List<Integer>> valoresSustituirPresupuestos(String campo) {
		//Método encargado de que el usuario indique los valores a sustituir del campo 'presupuesto' en los documentos.
		Map<String, List<Integer>> valores = new LinkedHashMap<>();
		List<Integer> valoresPresupuestos = new ArrayList<Integer>();
		
		int presu_max = 0;
		while(presu_max != -1) {
			System.out.print("Valor del campo 'presupuesto' a sustituir de los documentos (-1 para salir): ");
			try {
				presu_max = sc.nextInt();
			} catch (InputMismatchException e) {
				sc.nextLine();
						
				System.out.println("----------------------------------------");
				System.out.println("\tPresupuesto máximo inválido\n");
						
				continue;
			}
			
			if(presu_max != -1) {
				valoresPresupuestos.add(presu_max);
			} else {
				valores.put(campo, valoresPresupuestos);
				break;
			}
		}
		
		return valores;
	}

	public static Map<String, List<String>> valoresSustituirLicencias(String campo) {
		//Método encargado de que el usuario indique los valores a sustituir del campo licencia en los documentos.
		Map<String, List<String>> valores = new LinkedHashMap<>();
		List<String> valoresLicencias = new ArrayList<String>();
		
		String licencia = "";
		System.out.print("Valor del campo 'licencia' a sustituir de los documentos ('.' para salir): ");
		
		while(!licencia.equals(".")) {
			licencia = sc.nextLine();
															
			if(licencia.isBlank() && licencia.length() > 0) {
				licencia = "";
				System.out.println("----------------------------------------------------------------------------------");
				System.out.println("\tNo se ha introducido el nºde licencia del agente inmobiliario.\n");
										
				System.out.print("Valor del campo 'licencia' a sustituir de los documentos ('.' para salir): ");
				
			} else if(licencia.length() > 0) {
				if(!licencia.equals(".")) {
					valoresLicencias.add(licencia);
					System.out.print("Valor del campo 'licencia' a sustituir de los documentos ('.' para salir): ");
				} else {
					valores.put(campo, valoresLicencias);
					break;
				}
			}
		}
		
		return valores;
	}

	public static Map<String, List<String>> valoresSustituirDirecciones(String campo) {
		//Método encargado de que el usuario indique los valores a sustituir del campo Direccion en los documentos.
		Map<String, List<String>> valores = new LinkedHashMap<>();
		List<String> valoresDirecciones = new ArrayList<String>();
		
		System.out.println("\nDirección a sustituir: ");
		System.out.println("=============================================");
		System.out.println("(-1 en el nº y nombre de la calle para salir)\n");
		
		int nCalle = 0;
		String calle = "";
		String direccion = "";
		
		while(nCalle != -1 && !calle.equals("-1")) {
			while(true) {
				System.out.print("NºCalle (-1 para salir): ");
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

			System.out.print("Nombre de la calle (-1 para salir): ");		
			while(calle.length() == 0) {
				calle = sc.nextLine();
						
				if(calle.isBlank() && calle.length() > 0) {
					calle = "";
					System.out.println("----------------------------------------------------------------------------------");
					System.out.println("\tNo se ha introducido el nombre de la calle.\n");

					System.out.print("Nombre de la calle (-1 para salir): ");
				} else if(calle.equals("-1")) {
					break;
				}
			}
			
			direccion = (nCalle == -1 ? "" : nCalle + ", ") + (calle.equals("-1") ? "": calle); //Juntamos los datos de la dirección en una sola cadena.
			if(nCalle == -1 && calle.equals("-1")) {
				valores.put(campo, valoresDirecciones);
				break;
			} else {
				valoresDirecciones.add(direccion);
			}
		}
		
		return valores;
	}

	public static Map<String, List<String>> valoresSustituirEmails(String campo) {
		//Método encargado de que el usuario indique los valores a sustituir del campo email en los documentos.
		Map<String, List<String>> valores = new LinkedHashMap<>();
		List<String> valoresEmails = new ArrayList<String>();
		
		String correo = "";
		System.out.print("Valor del campo 'email' a sustituir de los documentos ('.' para salir): ");
		
		while(!correo.equals(".")) {
			correo = sc.nextLine();
									
			if(correo.isBlank() && correo.length() > 0) {
				correo = "";
				System.out.println("----------------------------------------------------------------------------------");
				System.out.println("\tNo se ha introducido el correo del propietario.\n");
				
				System.out.print("Valor del campo 'email' a sustituir de los documentos ('.' para salir): ");
			
			} else if(correo.length() > 0) {
				if(!correo.equals(".")) {
					valoresEmails.add(correo);
					
					System.out.print("Valor del campo 'email' a sustituir de los documentos ('.' para salir): ");
				} else {
					valores.put(campo, valoresEmails);
					break;
				}
			}
		}
		
		return valores;
	}

	public static Map<String, List<String>> valoresSustituirTelefonos(String campo) {
		//Método encargado de que el usuario indique los valores a sustituir del campo telefono en los documentos.
		Map<String, List<String>> valores = new LinkedHashMap<>();
		List<String> valoresTEL = new ArrayList<String>();
		
		String telefono = "";
		System.out.print("Valor del campo 'telefono' a sustituir de los documentos ('.' para salir): ");
		
		while(!telefono.equals(".")) {
			telefono = sc.nextLine();
			
			if(telefono.isBlank() && telefono.length() > 0) {
				telefono = "";
				System.out.println("--------------------------------------------------------------");
				System.out.println("\t- Valor no introducido.\n\n");
				
				System.out.print("Valor del campo 'telefono' de los documentos ('.' para salir): ");
				
			} else if(telefono.length() > 0) {
				if(!telefono.equals(".")) {
					valoresTEL.add(telefono);
					
					System.out.print("Valor del campo 'telefono' de los documentos ('.' para salir): ");
				} else {
					valores.put(campo, valoresTEL);
					break;
				}
			}
		}
		
		return valores;
	}

	public static Map<String, List<String>> valoresSustituirDNIs(String campo) {
		//Método encargado de que el usuario indique los valores a sustituir del campo DNI en los documentos.
		Map<String, List<String>> valores = new LinkedHashMap<>();
		List<String> valoresDNI = new ArrayList<String>();
		
		String DNI = "";
		System.out.print("Valor del campo DNI a sustituir de los documentos ('.' para salir): ");
		
		while(!DNI.equals(".")) {
			DNI = sc.nextLine();
			
			if(DNI.isBlank() && DNI.length() > 0) {
				DNI = "";
				System.out.println("--------------------------------------------------------------");
				System.out.println("\t- Valor no introducido.\n\n");
				
				System.out.print("Valor del campo DNI de los documentos ('.' para salir): ");
				
			} else if(DNI.length() > 0) {
				if(!DNI.equals(".")) {
					valoresDNI.add(DNI);
					
					System.out.print("Valor del campo DNI a sustituir de los documentos ('.' para salir): ");
				} else {
					valores.put(campo, valoresDNI);
					break;
				}
			}
		}
		
		return valores;
	}

	public static Map<String, List<String>> valoresSustituirNombres(String campo) {
		//Método encargado de que el usuario indique los valores a sustituir del campo nombre en los documentos.
		
		Map<String, List<String>> valores = new LinkedHashMap<>();
		List<String> valoresNombre = new ArrayList<String>();
		
		String nombre = "";
		String apellido1 = "";
		String apellido2 = "";
		System.out.print("Valor del campo 'nombre' a sustituir de los documentos ('.' para salir): ");
		
		while(!nombre.equals(".") || !apellido1.equals(".") || !apellido2.equals(".")) {
			nombre = sc.nextLine();
			
			if(nombre.isBlank() && nombre.length() > 0) {
				nombre = "";
				System.out.println("--------------------------------------------------------------");
				System.out.println("\t- Valor no introducido.\n\n");
				
				System.out.print("Valor del campo nombre de los documentos ('.' para salir): ");
				
			} else if(nombre.length() > 0) {
				if(!nombre.equals(".")){ 
					valoresNombre.add(nombre);
					
					System.out.print("Valor del campo 1ºapellido de los documentos ('.' para salir): ");
					apellido1 = sc.nextLine();
					
					if(apellido1.isBlank() && apellido1.length() > 0) {
						apellido1 = "";
						System.out.println("--------------------------------------------------------------");
						System.out.println("\t- Valor no introducido.\n\n");
						
					} else if(apellido1.length() > 0) {
						if(!apellido1.equals(".")) {
							valoresNombre.add(apellido1);
							
							System.out.print("Valor del campo 2ºapellido de los documentos ('.' para salir): ");
							apellido2 = sc.nextLine();
							
							if(apellido2.isBlank() && apellido2.length() > 0) {
								apellido2 = "";
								System.out.println("--------------------------------------------------------------");
								System.out.println("\t- Valor no introducido.\n\n");
								
							} else if(!apellido2.equals(".")) {
								valoresNombre.add(apellido2);
								
								if(valoresNombre.size() >= 3) {
									valores.put(campo, valoresNombre);
									break;
								}
							} else {
								valores.put(campo, valoresNombre);
								break;
							}
						} else {
							valores.put(campo, valoresNombre);
							break;
						}
					}
					System.out.print("\nValor del campo nombre de los documentos ('.' para salir): ");
					
				} else {
					valores.put(campo, valoresNombre);
					break;
				}
			}
		}
		return valores;
	}

	public static Map<String, List<DBRef>> valoresSustituirCompradores(String campo) {
		//Método que permite al usuario sustituir los agentes especificados de cada casa.
		Map<String, List<DBRef>> valores = new LinkedHashMap<>();
		List<DBRef> referenciasCompradores = new ArrayList<DBRef>();
		
		//Propietario de la casa
		List<Document> compradores = InsertarDocumentos.ListarCompradores();
		String comprador = "";
		
		//Obtenemos el identificador del documento del comprador correspondiente en la colección para hacer la referencia
		Document consultaC;
		ObjectId _id;
		DBRef refComprador = null;
		
		if(compradores.size() == 0) {
			System.out.println("\n\tNo hay compradores disponibles para asociar a la casa.");
		} else {
			System.out.print("\nComprador de la casa a sustituir (DNI, '.' para salir): ");
			
			while(!comprador.equals(".")) {
				comprador = sc.nextLine();
				
				if(comprador.isBlank() && comprador.length() > 0) {
					comprador = "";
					System.out.println("----------------------------------------------------------------------------------");
					System.out.println("\tNo se ha introducido el comprador de la casa.\n");
	
					System.out.print("Comprador de la casa a sustituir (DNI, '.' para salir): ");
				} else if(comprador.length() > 0) {
					
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
						if(comprador.equals(".")) {
							valores.put(campo, referenciasCompradores);
							break;
						} else {
							comprador = "";
							
							System.out.println("----------------------------------------------------------------------------------");
							System.out.println("\tEl comprador introducido no existe.\n");
						}
						
					} else if(comprador.length() > 0) {
						//Consultamos el comprador con el DNI introducido, si lo encuentra salimos del bucle.
						consultaC = MongoConnection.compradores.find(new Document("DNI", comprador)).first();
						_id = consultaC.getObjectId("_id");
						refComprador = new DBRef("Compradores", _id);
						
						if(consultaC != null) {
							referenciasCompradores.add(refComprador);
						}
					}
					System.out.print("Comprador de la casa a sustituir (DNI, '.' para salir): ");
				}
			}
		}
		return valores;
	}

	public static Map<String, List<DBRef>> valoresSustituirAgentes(String campo) {
		//Método que permite al usuario sustituir los agentes especificados de cada casa.
		Map<String, List<DBRef>> valores = new LinkedHashMap<>();
		List<DBRef> referenciasAgentes = new ArrayList<DBRef>();
		
		//Propietario de la casa
		List<Document> agentes = InsertarDocumentos.ListarAgentes();
		String agente = "";
		
		//Obtenemos el identificador del documento del agente correspondiente en la colección para hacer la referencia
		Document consultaA;
		ObjectId _id;
		DBRef refAgente = null;
		
		//Si no hay agentes
		if(agentes.size() == 0) {
			System.out.println("\tNo hay agentes disponibles para asociar a la casa.\n");
		} else {
			System.out.print("\nAgente de la casa a sustituir (DNI, '.' para salir): ");
			
			while(!agente.equals(".")) {
				agente = sc.nextLine();
				
				if(agente.isBlank() && agente.length() > 0) {
					agente = "";
					System.out.println("----------------------------------------------------------------------------------");
					System.out.println("\tNo se ha introducido el agente de la casa.\n");
	
					System.out.print("\nAgente de la casa a sustituir (DNI, '.' para salir): ");
				} else if(agente.length() > 0) {
					
					//Comprobar que el DNI del propietario escrito exista.
					boolean existe = false;
					for(Document AGI: agentes) {
						if(AGI.getString("DNI") != null) {
							if(AGI.getString("DNI").equals(agente)) {
								existe = true;
							}
						}
					}
					if(!existe && agente.length() > 0) {
						if(agente.equals(".")) {
							valores.put(campo, referenciasAgentes);
							break;
						} else {
							agente = "";
							System.out.println("----------------------------------------------------------------------------------");
							System.out.println("\tEl agente introducido no existe.\n");
						}
						
					} else if(agente.length() > 0) {
						//Consultamos el propietario con el DNI introducido, si lo encuentra salimos del bucle.
						consultaA = MongoConnection.agentes.find(new Document("DNI", agente)).first();
						_id = consultaA.getObjectId("_id");
						refAgente = new DBRef("Agentes_Inmobiliarios", _id);
						
						if(consultaA != null) {
							referenciasAgentes.add(refAgente);
						}
					}
					System.out.print("Agente de la casa a sustituir (DNI, '.' para salir): ");
				}
			}
		}
		return valores;
	}

	public static Map<String, List<DBRef>> valoresSustituirPropietarios(String campo) {
		//Método que permite al usuario sustituir los propietarios especificados de cada casa.
		Map<String, List<DBRef>> valores = new LinkedHashMap<>();
		List<DBRef> referenciasPropietarios = new ArrayList<DBRef>();
		
		//Propietario de la casa
		List<Document> propietarios = InsertarDocumentos.ListarPropietarios();
		String propietario = "";
		
		//Obtenemos el identificador del documento del propietario correspondiente en la colección para hacer la referencia
		Document consultaP;
		ObjectId _id;
		DBRef refPropietario = null;
		
		//Si no hay propietarios
		if(propietarios.size() == 0) {
			System.out.println("\tNo hay propietarios disponibles para asociar a la casa.\n");
		} else {
			System.out.print("\nPropietario de la casa a sustituir (DNI, '.' para salir): ");
			
			while(!propietario.equals(".")) {
				propietario = sc.nextLine();
				
				if(propietario.isBlank() && propietario.length() > 0) {
					propietario = "";
					System.out.println("----------------------------------------------------------------------------------");
					System.out.println("\tNo se ha introducido el propietario de la casa.\n");
	
					System.out.print("\nPropietario de la casa a sustituir (DNI, '.' para salir): ");
				} else if(propietario.length() > 0) {
					
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
						if(propietario.equals(".")) {
							valores.put(campo, referenciasPropietarios);
							break;
						} else {
							propietario = "";
							System.out.println("----------------------------------------------------------------------------------");
							System.out.println("\tEl propietario introducido no existe.\n");
						}
						
					} else if(propietario.length() > 0) {
						//Consultamos el propietario con el DNI introducido, si lo encuentra salimos del bucle.
						consultaP = MongoConnection.propietarios.find(new Document("DNI", propietario)).first();
						_id = consultaP.getObjectId("_id");
						refPropietario = new DBRef("Propietarios", _id);
						
						if(consultaP != null) {
							referenciasPropietarios.add(refPropietario);
						}
					}
					System.out.print("Propietario de la casa a sustituir (DNI, '.' para salir): ");
				}
			}
		}
		return valores;
	}

	public static Map<String, List<String>> valoresSustituirEstados(String campo) {
		//Método que permitirá al usuario especificar todos los valores de estados a sustituir
		Map<String, List<String>> valores = new LinkedHashMap<>();
		List<String> valoresEstados = new ArrayList<String>();
		
		String estado = "";
		System.out.print("Valor del campo 'estado' a sustituir de los documentos ('.' para salir): ");
		
		while(!estado.equals(".")) {
			estado = sc.nextLine();
			
			if(estado.isBlank() && estado.length() > 0) {
				estado = "";
				System.out.println("----------------------------------------------------------------------------------");
				System.out.println("\tNo se ha introducido el estado de la casa.\n");
	
				System.out.print("Valor del campo 'estado' a sustituir de los documentos ('.' para salir): ");
				
			} else if(estado.length() > 0) {
				if(!estado.equals(".")) {
					valoresEstados.add(estado);
					System.out.print("Valor del campo 'estado' a sustituir de los documentos ('.' para salir): ");
				} else {
					valores.put(campo, valoresEstados);
					break;
				}
			}
		}
		return valores;
	}

	public static Map<String, List<Integer>> valoresSustituirPreciosAlquiler(String campo) {
		//Método encargado de que el usuario indique los valores a sustituir del campo 'alquiler' en los documentos.
		Map<String, List<Integer>> valores = new LinkedHashMap<>();
		List<Integer> valoresPA = new ArrayList<Integer>();
		
		int precio = 0;
		while(precio != -1) {
			System.out.print("Valor del campo 'alquiler' a sustituir de los documentos (-1 para salir): ");
			try {
				precio = sc.nextInt();
			} catch (InputMismatchException e) {
				sc.nextLine();
						
				System.out.println("----------------------------------------");
				System.out.println("\tPrecio de alquiler inválido\n");
						
				continue;
			}
			
			if(precio != -1) {
				valoresPA.add(precio);
			} else {
				valores.put(campo, valoresPA);
				break;
			}
		}
		
		return valores;
	}

	public static Map<String, List<Integer>> valoresSustituirPreciosVenta(String campo) {
		//Método encargado de que el usuario indique los valores a sustituir del campo 'precio' en los documentos.
		Map<String, List<Integer>> valores = new LinkedHashMap<>();
		List<Integer> valoresPV = new ArrayList<Integer>();
		
		int precio = 0;
		while(precio != -1) {
			System.out.print("Valor del campo 'precio' a sustituir de los documentos (-1 para salir): ");
			try {
				precio = sc.nextInt();
			} catch (InputMismatchException e) {
				sc.nextLine();
						
				System.out.println("----------------------------------------");
				System.out.println("\tPrecio inválido\n");
						
				continue;
			}
			
			if(precio != -1) {
				valoresPV.add(precio);
			} else {
				valores.put(campo, valoresPV);
				break;
			}
		}
		
		return valores;
	}

	public static Map<String, List<Integer>> valoresSustituirBanos(String campo) {
		//Método encargado de que el usuario indique los valores a sustituir del campo 'banos' en los documentos.
		Map<String, List<Integer>> valores = new LinkedHashMap<>();
		List<Integer> valoresBanos = new ArrayList<Integer>();
		
		int banos = 0;
		while(banos != -1) {
			System.out.print("Valor del campo 'banos' a sustituir de los documentos (-1 para salir): ");
			try {
				banos = sc.nextInt();
			} catch (InputMismatchException e) {
				sc.nextLine();
						
				System.out.println("----------------------------------------");
				System.out.println("\tNº de baños inválido\n");
						
				continue;
			}
			
			if(banos != -1) {
				valoresBanos.add(banos);
			} else {
				valores.put(campo, valoresBanos);
				break;
			}
		}
		
		return valores;
	}

	public static Map<String, List<Integer>> valoresSustituirHabitaciones(String campo) {
		//Método encargado de que el usuario indique los valores a sustituir del campo 'habitaciones' en los documentos.
		Map<String, List<Integer>> valores = new LinkedHashMap<>();
		List<Integer> valoresHabitaciones = new ArrayList<Integer>();
		
		int habitaciones = 0;
		while(habitaciones != -1) {
			System.out.print("Valor del campo 'habitaciones' a sustituir de los documentos (-1 para salir): ");
			try {
				habitaciones = sc.nextInt();
			} catch (InputMismatchException e) {
				sc.nextLine();
						
				System.out.println("----------------------------------------");
				System.out.println("\tNº de habitaciones inválido\n");
						
				continue;
			}
			
			if(habitaciones != -1) {
				valoresHabitaciones.add(habitaciones);
			} else {
				valores.put(campo, valoresHabitaciones);
				break;
			}
		}
		
		return valores;
	}

	public static Map<String, List<Integer>> valoresSustituirTamanos(String campo) {
		//Método encargado de que el usuario indique los valores a sustituir del campo 'tamano' en los documentos.
		Map<String, List<Integer>> valores = new LinkedHashMap<>();
		List<Integer> valoresTamanos = new ArrayList<Integer>();
		
		int tamano = 0;
		while(tamano != -1) {
			System.out.print("Valor del campo 'tamano' a sustituir de los documentos (-1 para salir): ");
			try {
				tamano = sc.nextInt();
			} catch (InputMismatchException e) {
				sc.nextLine();
						
				System.out.println("----------------------------------------");
				System.out.println("\tTamaño en metros cuadrados inválido\n");
						
				continue;
			}
			
			if(tamano != -1) {
				valoresTamanos.add(tamano);
			} else {
				valores.put(campo, valoresTamanos);
				break;
			}
		}
		
		return valores;
	}

	public static Map<String, List<String>> valoresSustituirCPs(String campo) {
		//Método que permitirá al usuario especificar todos los valores de códigos postales a sustituir
		Map<String, List<String>> valores = new LinkedHashMap<>();
		List<String> valoresCPs = new ArrayList<String>();
		
		String CP = "";
		System.out.print("Valor del campo 'codigo_postal' a sustituir de los documentos ('.' para salir): ");
		
		while(!CP.equals(".")) {
			CP = sc.nextLine();
			
			if(CP.isBlank() && CP.length() > 0) {
				CP = "";
				System.out.println("----------------------------------------------------------------------------------");
				System.out.println("\tNo se ha introducido la provincia de la casa.\n");
	
				System.out.print("Valor del campo 'codigo_postal' a sustituir de los documentos ('.' para salir): ");
				
			} else if(CP.length() > 0) {
				if(!CP.equals(".")) {
					valoresCPs.add(CP);
					System.out.print("Valor del campo 'codigo_postal' a sustituir de los documentos ('.' para salir): ");
				} else {
					valores.put(campo, valoresCPs);
					break;
				}
			}
		}
		return valores;
	}

	public static Map<String, List<String>> valoresSustituirProvincias(String campo) {
		//Método que permitirá al usuario especificar todos los valores de provincias a sustituir
		Map<String, List<String>> valores = new LinkedHashMap<>();
		List<String> valoresProvincias = new ArrayList<String>();
		
		String provincia = "";
		System.out.print("Valor del campo 'provincia' a sustituir de los documentos ('.' para salir): ");
		
		while(!provincia.equals(".")) {
			provincia = sc.nextLine();
			
			if(provincia.isBlank() && provincia.length() > 0) {
				provincia = "";
				System.out.println("----------------------------------------------------------------------------------");
				System.out.println("\tNo se ha introducido la provincia de la casa.\n");
	
				System.out.print("Valor del campo 'provincia' a sustituir de los documentos ('.' para salir): ");
				
			} else if(provincia.length() > 0) {
				if(!provincia.equals(".")) {
					valoresProvincias.add(provincia);
					System.out.print("Valor del campo 'provincia' a sustituir de los documentos ('.' para salir): ");
				} else {
					valores.put(campo, valoresProvincias);
					break;
				}
			}
		}
		return valores;
	}

	public static Map<String, List<String>> valoresSustituirCiudades(String campo) {
		//Método que permitirá al usuario especificar todos los valores de ciudades a sustituir
		Map<String, List<String>> valores = new LinkedHashMap<>();
		List<String> valoresCiudades = new ArrayList<String>();
		
		String ciudad = "";
		System.out.print("Valor del campo 'ciudad' a sustituir de los documentos ('.' para salir): ");
		
		while(!ciudad.equals(".")) {
			ciudad = sc.nextLine();
			
			if(ciudad.isBlank() && ciudad.length() > 0) {
				ciudad = "";
				System.out.println("----------------------------------------------------------------------------------");
				System.out.println("\tNo se ha introducido la ciudad de la casa.\n");
	
				System.out.print("Valor del campo 'ciudad' a sustituir de los documentos ('.' para salir): ");
				
			} else if(ciudad.length() > 0) {
				if(!ciudad.equals(".")) {
					valoresCiudades.add(ciudad);
					System.out.print("Valor del campo 'ciudad' a sustituir de los documentos ('.' para salir): ");
				} else {
					valores.put(campo, valoresCiudades);
					break;
				}
			}
		}
		return valores;
	}

	public static Map<String, List<String>> valoresSustituirTipos(String campo) {
		//Método que permitirá al usuario indicar los valores de los tipos de casa que desea sustitiuir para los documentos.
		Map<String, List<String>> valores = new LinkedHashMap<>();
		List<String> valoresTipos = new ArrayList<String>();
		
		String tipo = "";
		System.out.print("Valor del campo 'tipo' a sustituir de los documentos ('.' para salir): ");
		
		while(!tipo.equals(".")) {
			tipo = sc.nextLine();
			
			if(tipo.isBlank() && tipo.length() > 0) {
				tipo = "";
				System.out.println("----------------------------------------------------------------------------------");
				System.out.println("\tNo se ha introducido el tipo de la casa.\n");
	
				System.out.print("Valor del campo 'tipo' a sustituir de los documentos ('.' para salir): ");
				
			} else if(tipo.length() > 0) {
				if(!tipo.equals(".")) {
					valoresTipos.add(tipo);
					System.out.print("Valor del campo 'tipo' a sustituir de los documentos ('.' para salir): ");
				} else {
					valores.put(campo, valoresTipos);
					break;
				}
			}
		}
		return valores;
	}

	public static String valorCondicion() {
		//Método que permitirá al usuario elegir la condición para el valor que debe de tomar el campo.
		String valor = "";
		int opc = 0;
		
		while(valor.length() == 0) {
			System.out.println("\nCONDICIÓN DEL VALOR QUE EL CAMPO DEBE TOMAR");
			System.out.println("===========================================\n");
			
			System.out.println("1. Igual a");
			System.out.println("2. Mayor que");
			System.out.println("3. Menor que");
			System.out.println("4. Que el campo exista");
			
			System.out.println("\n=========================================");
			System.out.print("Elige el valor que el campo debe tomar (nº): ");
			try {
				opc = sc.nextInt();
			} catch (InputMismatchException e) {
				valor = "";
				sc.nextLine();
			}
			
			switch(opc) {
			case 1:
				valor = "Igual a";
				break;
			case 2:
				valor = "Mayor que";
				break;
			case 3:
				valor = "Menor que";
				break;
			case 4:
				valor = "Que el campo exista";
				break;
			default:
				valor = "";
				
				System.out.println("----------------------------------------------------------");
				System.out.println("\t- Condición errónea.\n");
				
				break;
			}
		}
		return valor;
	}

	public static String campoFiltrar(List<String> campos) {
		//Método que permitirá elegir al usuario el campo por el que se va a filtrar.
		System.out.println("CAMPO POR EL QUE SE VA A FILTRAR");
		System.out.println("================================\n");
		
		for(int i=0; i < campos.size(); i++) {
			String campo = campos.get(i);
			System.out.println((i+1) + ". " + campo);
		}
		System.out.println("\n================================");
		
		
		String campo = "";
		System.out.print("Campo por el que se va a filtrar: ");
		
		while(campo.length() == 0) {
			campo = sc.nextLine();
			
			if(campo.isBlank() && campo.length() > 0) {
				campo = "";
				System.out.println("-------------------------------------------------------------------");
				System.out.println("\t- No se ha introducido el campo por el que se va a filtrar.\n");
				
				System.out.print("Campo por el que se va a filtrar: ");
				
			} else if(campo.length() > 0) {
				//Si se ha introducido el campo vamos a comprobar que exista.
				boolean existe = false;
				for(String campoC: campos) {
					if(campoC.equalsIgnoreCase(campo)) {
						existe = true;
						break;
					}
				}
				
				if(existe) {//Si el campo existe salimos del bucle.
					break;
				} else {
					campo = "";
					System.out.println("\n-------------------------------------------------------------------");
					System.out.println("\t\t- Campo inexistente.\n\n");
					
					System.out.print("Campo por el que se va a filtrar: ");
				}
			}
		}
		return campo;
	}
}
