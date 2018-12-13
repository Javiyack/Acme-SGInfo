DROP DATABASE IF EXISTS `Acme-CRM`;
CREATE DATABASE `Acme-CRM`;

USE `Acme-CRM`;

GRANT SELECT, INSERT, UPDATE, DELETE 
  ON `Acme-CRM`.* TO 'acme-user'@'%';

GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, DROP, REFERENCES, INDEX, ALTER, 
    CREATE TEMPORARY TABLES, LOCK TABLES, CREATE VIEW, CREATE ROUTINE, 
    ALTER ROUTINE, EXECUTE, TRIGGER, SHOW VIEW
  ON `Acme-CRM`.* TO 'acme-manager'@'%';