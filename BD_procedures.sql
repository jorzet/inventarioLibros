CREATE SCHEMA IF NOT EXISTS `inventarioLibros` DEFAULT CHARACTER SET utf8 ;
USE `inventarioLibros` ;

CREATE TABLE IF NOT EXISTS `registrodelibros` (
  `id` int(11) NOT NULL auto_increment,
  `codigoLibro` varchar(255) default NULL,
  `codigoCredencial` varchar(255) default NULL,
  `status` varchar(255) default NULL,
  `fechaHora` varchar(255) default NULL,
  PRIMARY KEY  (`Id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;


DELIMITER $$
CREATE PROCEDURE insertBook(IN codigoLibro  varchar(255), IN codigoCredencial  varchar(255), IN `status`  varchar(255), IN fechaHora  varchar(255), OUT response VARCHAR(100))
BEGIN
  INSERT INTO inventarioLibros.registrodelibros (codigoLibro, codigoCredencial, `status`, fechaHora)
  VALUES (codigoLibro, codigoCredencial, `status`, fechaHora);
  SET response='Libro registrado correctamente';
END $$

CALL insertBook(1, 1, 'disponible', '12/12/22', @response);
select @response;