# Inmobiliaria
Proyecto que consiste en una aplicación de consola programada en Java que gestiona una base de datos NoSQL creada en MongoDB para una empresa inmobiliaria que les permitan gestionar la información que almacenan. Más información en la memoria del proyecto en PDF [aquí](https://drive.google.com/file/d/1Huceu9CBKJGybJIdBk1QoQzVxIc7qQJ9/view?usp=sharing).  

## ESTRUCTURA GENERAL DEL PROYECTO
<img width="357" height="250" alt="estructura_ProyectoMongoDB" src="https://github.com/user-attachments/assets/4065acf9-f73f-4d4c-bae9-7b69e2682566" />    

- **_src/main/java/Main.java_**: Aquí está el módulo principal del programa a ejecutar. Por cada consulta que se elija se guardará un fichero de texto (.txt) con el resultado de la consulta en la raíz del proyecto acompañando con el resultado de la consulta con un fichero JSON.
  
- **_src/main/java/MongoConnection.java_**: Clase destinada a recuperar las colecciones de la BD para su gestión y ejecución de todo tipo de operaciones (Insertar, Consultar, Modificar, Eliminar) sobre los documentos (registros) contenidos en cada una.  

## TECNOLOGÍAS UTILIZADAS
Lenguaje de Programación: **Java 21**  
Tipo de proyecto: **Maven**   
SGBD (NoSQL): **MongoDB**  
  
Entorno de Desarrollo: **Eclipse IDE**  
Herramienta de Diagramación: **Moon Modeler**

## COLECCIONES
- **Principales:** Propietario, Comprador, Casa, Agente.
- **Secundarias:** Contrato, Pagos, Mantenimiento, Visita.

## DIAGRAMA DE BASE DE DATOS
<img width="1722" height="835" alt="Diagrama Inmobiliaria" src="https://github.com/user-attachments/assets/024643c5-b92a-4646-ad9d-a27204dd20d6" />
