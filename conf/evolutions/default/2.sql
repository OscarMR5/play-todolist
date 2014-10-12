# Tasks evolution and create table taskuser

# --- !Ups


CREATE TABLE taskuser (
    name varchar(255) PRIMARY KEY
);
-- Datos de prueba usuarios
INSERT INTO taskuser
VALUES ('guest'); -- Invitado
INSERT INTO taskuser
VALUES ('oscarmarco');
INSERT INTO taskuser
VALUES ('pedrogarcia');
INSERT INTO taskuser
VALUES ('davidblanco');
INSERT INTO taskuser
VALUES ('javiertolosa');
INSERT INTO taskuser
VALUES ('jorgeruiz');


ALTER TABLE task ADD COLUMN owner varchar(255);-- REFERENCES taskuser (name);

-- Datos de prueba usuarios
INSERT INTO task (label,owner)
VALUES ('tarea1','oscarmarco');
INSERT INTO task (label,owner) 
VALUES ('tarea2','oscarmarco');
INSERT INTO task (label,owner)
VALUES ('tarea3','davidblanco');
INSERT INTO task (label,owner)
VALUES ('tarea4','javiertolosa');
INSERT INTO task (label,owner)
VALUES ('tarea5','guest');
INSERT INTO task (label,owner)
VALUES ('tarea6','guest');
INSERT INTO task (label,owner)
VALUES ('tarea7','guest');
INSERT INTO task (label,owner)
VALUES ('tarea8','guest');

ALTER TABLE task ADD CONSTRAINT fkeytask FOREIGN KEY (owner) REFERENCES taskuser (name);

# --- !Downs

ALTER TABLE task DROP CONSTRAINT fkeytask;
ALTER TABLE task DROP COLUMN owner;
DROP TABLE taskuser;
