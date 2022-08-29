-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `inventarioLibros` DEFAULT CHARACTER SET utf8 ;
USE `inventarioLibros` ;

-- -----------------------------------------------------
-- Table `mydb`.`Cliente`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `inventarioLibros`.`Cliente` (
  `codigoCliente` VARCHAR(15) NOT NULL,
  `nombre` VARCHAR(45) NULL,
  `status` VARCHAR(45) NULL,
  PRIMARY KEY (`codigoCliente`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`registrodelibros`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `inventarioLibros`.`registrodelibros` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `codigoLibro` VARCHAR(255) NULL,
  `status` VARCHAR(255) NULL,
  `fechaHora` VARCHAR(255) NULL,
  `Cliente_codigoCliente` VARCHAR(15) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_registrodelibros_Cliente_idx` (`Cliente_codigoCliente` ASC) VISIBLE,
  CONSTRAINT `fk_registrodelibros_Cliente`
    FOREIGN KEY (`Cliente_codigoCliente`)
    REFERENCES `inventarioLibros`.`Cliente` (`codigoCliente`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `mydb`.`User`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `inventarioLibros`.`User` (
  `idUser` INT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(255) NULL,
  `password` VARCHAR(45) NULL,
  `nick` VARCHAR(255) NULL,
  `completeName` VARCHAR(255) NULL,
  PRIMARY KEY (`idUser`))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `mydb`.`Admin`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `inventarioLibros`.`Admin` (
  `idAdmin` INT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(255) NULL,
  `password` VARCHAR(45) NULL,
  PRIMARY KEY (`idAdmin`))
ENGINE = InnoDB;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;


-- ----------------------------
-- Records of clientes
-- ----------------------------
INSERT INTO `cliente` VALUES ('2C302F', 'MANUEL LANDEROS TINAJERO', 'activo');
INSERT INTO `cliente` VALUES ('3E41C8', 'CARLOS MANUEL DE LA MORA MERCADO', 'activo');
INSERT INTO `cliente` VALUES ('7F5280', 'VERONICA LARA SANTOS', 'activo');
INSERT INTO `cliente` VALUES ('43E500', 'VIANNEY LOPEZ CRUZ', 'activo');
INSERT INTO `cliente` VALUES ('79C5C4', 'DIANA CAROLINA FIGUEROA', 'activo');
INSERT INTO `cliente` VALUES ('96CB3A', 'BRANDON DANIEL LOPEZ GONZALEZ', 'activo');
INSERT INTO `cliente` VALUES ('ALM231', 'OSCAR RAMIREZ TORREZ', 'activo');
INSERT INTO `cliente` VALUES ('B97B63', 'JORGE LOPEZ ZANCHEZ', 'activo');
INSERT INTO `cliente` VALUES ('BCBEB5', 'JUEN DIEGO MILAN DELGADO', 'activo');
INSERT INTO `cliente` VALUES ('C6A73C', 'MARTHA LORENA MOLINERO HERNANDEZ', 'activo');
INSERT INTO `cliente` VALUES ('F24C4D', 'KEVIN CRISTIAN CASTELLANOS CHAPARRO', 'activo');
INSERT INTO `cliente` VALUES ('YU9601', 'BRANDON GARCIA ROMAN', 'activo');

CALL insertUser("jorzet.94@gmail.com", "1234", "2233", "Jorge Zepeda Tinoco", @result);
SELECT @result;

CALL getUser("jorzet.94@gmail.com", "1234", @result);
SELECT @result;


CALL insertAdmin("ralphmagnifico@gmail.com", "1234", @result);
SELECT @result;

CALL getAdmin(@result);
SELECT @result;

CALL updateAdmin("ralphmagnifico@gmail.com", "jorzet.94@gmail.com", @result);
SELECT @result;

SET FOREIGN_KEY_CHECKS = 0;


DELIMITER $$
CREATE PROCEDURE insertBook(IN codigoLibro  varchar(255), IN codigoCredencial  varchar(255), IN `status`  varchar(255), IN fechaHora  varchar(255), OUT response VARCHAR(100))
BEGIN
  INSERT INTO inventarioLibros.registrodelibros (codigoLibro, `status`, fechaHora, Cliente_codigoCliente)
  VALUES (codigoLibro, `status`, fechaHora, codigoCredencial);
  SET response='Libro registrado correctamente';
END $$

DELIMITER $$
CREATE PROCEDURE getClient(IN codigoCliente VARCHAR(15), OUT response VARCHAR(100))
BEGIN
  IF EXISTS (SELECT c.codigoCliente FROM inventarioLibros.cliente as c where c.codigoCliente = codigoCliente)
	THEN
		SELECT * FROM inventarioLibros.cliente as c where c.codigoCliente = codigoCliente;
		SET response = 'OK';
	ELSE
		SET response = 'No existe el cliente';
	END IF;
END $$

DELIMITER $$
CREATE PROCEDURE insertClient(IN codigoCliente VARCHAR(15), IN clientName VARCHAR(45), IN `status` VARCHAR(45), OUT response VARCHAR(100))
BEGIN
  INSERT INTO inventarioLibros.cliente (codigoCliente, clientName, `status`)
  VALUES (codigoCliente, clientName, `status`);
  SET response='Cliente registrado correctamente';
END $$


DELIMITER $$
CREATE PROCEDURE getUser(IN email VARCHAR(255),IN pass VARCHAR(45), OUT response VARCHAR(100))
BEGIN
  IF EXISTS (SELECT u.idUser FROM inventarioLibros.`User` as u where u.email = email and u.`password` = pass)
	THEN
		SELECT * FROM inventarioLibros.`User` as u where u.email = email and u.`password` = pass;
		SET response = 'OK';
	ELSE
		SET response = 'No existe el usuraio ingresado, compruebe email y contraseña';
	END IF;
END $$

DELIMITER $$
CREATE PROCEDURE insertUser(IN email VARCHAR(255), IN pass VARCHAR(45), IN nick VARCHAR(255), IN completeNAme VARCHAR(255),  OUT response VARCHAR(100))
BEGIN
 IF NOT EXISTS (SELECT u.idUser FROM inventarioLibros.`User` as u where u.email = email and u.`password` = pass)
  THEN
		INSERT INTO inventarioLibros.`User` (email, `password`, nick, completeName)
		VALUES (email, pass, nick, completeName);
		SET response='OK';
  ELSE
		SET response = 'Error, el usuario ya existe';
  END IF;
END $$


DELIMITER $$
CREATE PROCEDURE insertAdmin(IN email VARCHAR(255), IN pass VARCHAR(45),  OUT response VARCHAR(100))
BEGIN
 IF NOT EXISTS (SELECT u.idAdmin FROM inventarioLibros.`Admin` as u where u.email = email and u.`password` = pass)
  THEN
		INSERT INTO inventarioLibros.`Admin` (email, `password`)
		VALUES (email, pass);
		SET response='OK';
  ELSE
		SET response = 'Error, el administrador ya existe';
  END IF;
END $$

DELIMITER $$
CREATE PROCEDURE getAdmin(OUT response VARCHAR(100))
BEGIN
  IF EXISTS (SELECT u.idAdmin FROM inventarioLibros.`Admin` as u)
	THEN
		SELECT * FROM inventarioLibros.`Admin` as u;
		SET response = 'OK';
	ELSE
		SET response = 'No existe el administrador ingresado, compruebe email';
	END IF;
END $$


DELIMITER $$
CREATE PROCEDURE updateAdmin(IN originalEmail VARCHAR(255), IN newEmail VARCHAR(45), OUT response VARCHAR(100))
BEGIN
 IF EXISTS (SELECT u.idAdmin FROM inventarioLibros.`Admin` as u WHERE u.email=originalEmail)
	THEN 
    UPDATE inventarioLibros.`Admin` as u SET u.email=newEmail WHERE u.email=originalEmail;
ELSE 
    SET response = 'No existe el administrador ingresado, compruebe email';
END IF;
END $$

