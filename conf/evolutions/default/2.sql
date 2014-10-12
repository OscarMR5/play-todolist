# Tasks evolution and create table task-user

# --- !Ups


CREATE TABLE task-user (
    name varchar(255) PRIMARY KEY
);
#Datos de prueba usuarios
INSERT INTO task-user
VALUES ('oscarmarco');
INSERT INTO task-user
VALUES ('pedrogarcia');
INSERT INTO task-user
VALUES ('davidblanco');
INSERT INTO task-user
VALUES ('javiertolosa');
INSERT INTO task-user
VALUES ('jorgeruiz');


ALTER TABLE task ADD owner varchar(255) REFERENCES task-user (name);

 #Datos de prueba usuarios
INSERT INTO task (label,owner)
VALUES (tarea1,'oscarmarco');
INSERT INTO task
VALUES (tarea2,'oscarmarco');
INSERT INTO task
VALUES (tarea3,'davidblanco');
INSERT INTO task
VALUES (tarea4,'javiertolosa');
INSERT INTO task
VALUES (tarea5,'jorgeruiz');

# --- !Downs

ALTER TABLE task DROP owner;
DROP TABLE task-user;
