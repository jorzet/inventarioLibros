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

