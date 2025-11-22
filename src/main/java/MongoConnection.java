import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoConnection {//Creamos todos los atributos y métodos estáticos para mayor comodidad.
	static MongoClient cliente;
	static MongoDatabase db;
	
	static MongoCollection<Document> casas;
	static MongoCollection<Document> agentes;
	static MongoCollection<Document> propietarios;
    static MongoCollection<Document> compradores;
	static MongoCollection<Document> contratos;
	static MongoCollection<Document> pagos;
	static MongoCollection<Document> mantenimientos;
	static MongoCollection<Document> visitas;
	
	public static void inicializarConexion() {
		//Establecer la conexión a la BD (si no existe se creará).
		cliente = MongoClients.create("mongodb://127.0.0.1:27017");
		db = cliente.getDatabase("ProyectoRA5");
				
		//Recuperar las colecciones para cualquier acción, si no existen se crean automáticamente.
		casas = db.getCollection("Casas");
		agentes = db.getCollection("Agentes_Inmobiliarios");
		propietarios = db.getCollection("Propietarios");
		compradores = db.getCollection("Compradores");
		contratos = db.getCollection("Contratos");
		pagos = db.getCollection("Pagos");
		mantenimientos = db.getCollection("Mantenimiento");
		visitas = db.getCollection("Visitas");
	}
	
	public static void cerrarConexion() {
		//Cerramos la conexión a la BD porque el usuario ha salido del programa.
		cliente.close();
	}
}
