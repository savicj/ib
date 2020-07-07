DROP SCHEMA IF EXISTS ib;
CREATE SCHEMA ib DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE ib;


CREATE TABLE userauthority(
	id INT AUTO_INCREMENT,
	name VARCHAR(10),
    PRIMARY KEY(id)
);

INSERT INTO AUTHORITY (id, name) VALUES (1, 'REGULAR');
INSERT INTO AUTHORITY (id, name) VALUES (2, 'ADMIN');


CREATE TABLE users(
	id INT AUTO_INCREMENT,
	password VARCHAR(10) NOT NULL, 
	certificate VARCHAR(30) NOT NULL, 
	email VARCHAR(20) NOT NULL, 
	active TINYINT(1),
	authority VARCHAR(10) NOT NULL,
    PRIMARY KEY(id)
);

INSERT INTO USERS (password, certificate, email, active, authority) VALUES ('user1', 'cer1', 'user1@example.com', 0, 'Regular');
INSERT INTO USERS (password, certificate, email, active, authority) VALUES ('user2', 'cer2', 'user2@example.com', 1, 'Admin');

