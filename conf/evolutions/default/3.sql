# Tasks table evolution para incluir la fecha


# --- !Ups
--- Incluir la fecha de terminacion a la tarea
 
ALTER TABLE task ADD COLUMN fecha date;

--- Incluir tareas con fecha
INSERT INTO task (label,owner,fecha)
VALUES ('tarea9','javiertolosa','2014-11-05');
INSERT INTO task (label,owner,fecha)
VALUES ('tarea10','oscarmarco','2014-11-27');
INSERT INTO task (label,owner,fecha)
VALUES ('tarea11','davidblanco','2015-11-26');
INSERT INTO task (label,owner,fecha)
VALUES ('tarea12','javiertolosa','2014-12-05');
INSERT INTO task (label,owner,fecha)
VALUES ('tarea13','guest','2014-11-05');
# --- !Downs
ALTER TABLE task DROP fecha;