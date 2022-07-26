CREATE SCHEMA IF NOT EXISTS `inventarioLibros` DEFAULT CHARACTER SET utf8 ;
USE `inventarioLibros` ;

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de datos: `libros`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `registrodelibros`
--

CREATE TABLE IF NOT EXISTS `registrodelibros` (
  `id` int(11) NOT NULL auto_increment,
  `codigoLibro` varchar(255) default NULL,
  `codigoCredencial` varchar(255) default NULL,
  `status` varchar(255) default NULL,
  `fechaHora` varchar(255) default NULL,
  PRIMARY KEY  (`Id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Volcar la base de datos para la tabla `registrodelibros`
--
