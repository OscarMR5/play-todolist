###Play-TodoList

Práctica 1
==========

En esta práctica:  

- Convertiremos la aplicación en un **API REST** que trabaja con objetos JSON

- Añadiremos algunos datos adicionales a las tareas que gestiona el API.
    - Usuario que crea la tarea. Propietario
    - Fecha opcional de finalización.

Tenemos que utilizar distintos conocimientos y herramientas para realizar esta práctica correctamente

> **Git**: para realizar un seguimiento de los distintos cambios realizados y tener historial además de poder separar las distintas fases del desarrollo de la práctica (Features)  

> **Play framework** y Scala: es el framework sobre el que vamos a trabajar para realizar la implementación y simplificar el trabajo sobre HTTP y servicios web como un API REST.  

>**HTTP** : *Hypertext Transfer Protocol* es el protocolo usado en cada transacción en la WEB. HTTP es un protocolo sin estado y sobre texto. Una transacción HTTP está formada por un encabezconjunto de encabezados seguido, opcionalmente, por una línea en blanco y el cuerpo.Soporta los métodos: GET, POST, UPDATE, PUT y DELETE.

>  **API REST** : Conocer los fundamentos de diseño de un API REST para poder llevarlos a la práctica en nuestra aplicación de forma correcta. Un API REST se base en la utilización de los distintos métodos HTTP.

> **JSON** : *JavaScript Object Notation* es un formato ligero para el intercambio de datos en soporte texto (como HTTP). Esto nos permite enviar datos complejos a través de HTTP de forma sólida. Para trabajar con JSON hay que realizar un serialización de los objetos de la aplicación para enviarlos y el proceso contrario al recibirlos.

#Servicios diponibles en el API

###Feature 1 y anterior implementación
| Petición        | Descripción   |
| -------------   | :------------:|
| GET  /tasksold        | Funcion anterior que Devuelve la página HTML con la lista de todas las tareas y permite acceder a borrarlas o añadir nuevas |
| POST /tasks/:id/delete|      Función anterior que borra una tarea desde el formulario  |
| GET  /tasks           |       Devuelve una lista JSON con todas las tareas. O una lista vacía |
| GET  /tasks/:id       |       Devuelve la tarea solicitada en representación JSON. Si no existe 404  |
| POST /tasks           |      Recibe el label la nueva tarea en un formulario.Devuelve un JSON con la nueva tarea creada y el código HTTP 201.  |
| DELETE   /tasks/:id    |      Borra la tarea con el id indicado (respuesta HTTP 200). Si la tarea no existe se devuelve HTTP 404. |


###Feature 2  
  
| Petición        | Descripción   |
| ------------- | :-------------: |
| GET  /tasks           |       Devuelve una lista JSON con todas las tareas del usuario anónimo (guest)|
| POST   /tasks/    |   Ahora crea una nueva tarea para el usuario anónimo. A partir de una petición con la tarea en JSON en el cuerpo  |
| GET      /:user/tasks  |      Lista todas las tareas del usuario indicado o lista vacía en JSON. HTTP 404 si el usuario no existe |
| POST     /:user/tasks  |      Añade una nueva tarea a un usuario existe, devolviendo un JSON que representa la nueva tarea.La tarea se envia en el cuerpo de la petición en JSON. En otro caso 404.  |


###Feature 3  
  
| Petición        | Descripción   |
| ------------- | :-------------: |
| POST   /:user/tasks      |      Igual que antes pero ahora la tarea en JSON recibida puede tener un campo fecha |
| GET     /:user/tasks(?fecha)     |      Ahora Este método puede recibir un parámetro con la fecha para filtrar por día |
| GET     /:user/tasks/hoy |      Devuelve una lista JSON con las tareas con fecha de hoy                         |
| DELETE  /:user/tasks?fecha=yyyy/MM/dd     |      Eliminar las tareas de un dia concreto de un usuario    |



#####Evolución de la base de datos para Feature 2
 En la feature 2 se incluye que todas las tareas deben tener un usuario registrado en el sistema como propietario. Para ello tenemos que crear la nueva tabla para los usuarios, taskuser. Y añadir en la tabla task una columna con la clave ajena a usuario. Todos estos cambios además de añadir usuarios y tareas de prueba los he realizado en 2.sql junto con como deshacerlos.
 
 
 ```sql
 # --- !Ups
 
CREATE TABLE taskuser (
    name varchar(255) PRIMARY KEY
);

ALTER TABLE task ADD COLUMN owner varchar(255);

ALTER TABLE task ADD CONSTRAINT fkeytask FOREIGN KEY (owner) REFERENCES taskuser (name);


# --- !Downs

ALTER TABLE task DROP CONSTRAINT fkeytask;
ALTER TABLE task DROP COLUMN owner;
DROP TABLE taskuser;

```
 
 #####Evolución de la base de datos para Feature 3
  Para la feature 3 se ha añadido la columna fecha de tipo date en la tabla task. También se han añadido datos con fechas de ejemplo.

```sql
ALTER TABLE task ADD COLUMN fecha date;
```
-----

####Author: Óscar Marco Ródenas
#####Prácticas Metodologías Ágiles de Desarrollo de Software
######Grado en Ingeniería Informática
######Universidad de Alicante 2014-2015

####Enlaces de interés
http://www.restapitutorial.com/
https://www.atlassian.com/git/tutorials/rewriting-history/git-rebase
https://www.atlassian.com/git/tutorials/using-branches
https://www.atlassian.com/git/tutorials/
https://www.playframework.com/documentation/2.4.x/ScalaAnorm
https://www.playframework.com/documentation/2.4.x/ScalaDatabase
https://www.playframework.com/documentation/2.3.x/ScalaJsonHttp
https://www.playframework.com/documentation/2.3.x/ScalaJson
https://chrome.google.com/webstore/detail/postman-rest-client/fdmmgilgnpjigdojojpjoooidkmcomcm
http://www.restapitutorial.com/httpstatuscodes.html
https://www.playframework.com/documentation/2.4.x/Evolutions