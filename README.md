# Challengue de Literatua

Asegúrate de tener Java 11 o superior instalado en tu sistema.

Clona el repositorio y abre el proyecto en tu IDE favorito (IntelliJ, Eclipse, etc.).

Consumo de la API: Utiliza la URL base https://gutendex.com/books/ para realizar solicitudes a la API de libros.

Análisis de la Respuesta JSON: Procesa los datos JSON obtenidos de la API para extraer información relevante sobre los libros y autores.

Inserción y Consulta en la Base de Datos: Almacena los datos obtenidos en una base de datos PostgreSQL y realiza consultas para presentar la información a los usuarios.

Exhibición de Resultados: Utiliza una interfaz de consola para interactuar con los usuarios y mostrar los resultados de las consultas.

Ejecución del Proyecto Configurar Base de Datos: Crea una base de datos PostgreSQL y actualiza las siguientes variables en application.properties con tus credenciales y nombre de base de datos:

properties spring.datasource.url=jdbc:postgresql://${DB_HOST}/${DB_NAME} spring.datasource.username=${DB_USER} spring.datasource.password=${DB_PASSWORD} spring.datasource.driver-class-name=org.postgresql.Driver hibernate.dialect=org.hibernate.dialect.HSQLDialect spring.jpa.hibernate.ddl-auto=update spring.jpa.show-sql=true spring.jpa.format-sql=true Compilar y Ejecutar: Ejecuta el proyecto desde tu IDE o utilizando la línea de comandos:

sh ./gradlew bootRun Interacción con la Consola: Utiliza la interfaz de consola para interactuar con la aplicación, seleccionando las opciones del menú principal para buscar libros, listar autores, obtener estadísticas, entre otros.
