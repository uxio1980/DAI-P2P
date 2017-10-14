-- phpMyAdmin SQL Dump
-- version 4.6.6deb4
-- https://www.phpmyadmin.net/
--
-- Servidor: localhost:3306
-- Tiempo de generación: 14-10-2017 a las 21:20:15
-- Versión del servidor: 5.7.19-0ubuntu0.17.04.1
-- Versión de PHP: 7.0.22-0ubuntu0.17.04.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

CREATE USER IF NOT EXISTS 'hsdb'@'localhost'
  IDENTIFIED BY 'hsdbpass';
CREATE DATABASE IF NOT EXISTS `hstestdb`;
GRANT ALL PRIVILEGES ON `hstestdb` . * TO 'hsdb'@'localhost';

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `hstestdb`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `HTML`
--

CREATE TABLE `HTML` (
  `id` int(11) NOT NULL,
  `uuid` varchar(40) CHARACTER SET utf8 NOT NULL,
  `content` mediumtext CHARACTER SET utf8 NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `HTML`
--

INSERT INTO `HTML` (`id`, `uuid`, `content`) VALUES
(443, '6df1047e-cf19-4a83-8cf3-38f5e53f7725', '<html><body>This is the html page 6df1047e-cf19-4a83-8cf3-38f5e53f7725.</body></html>'),
(444, '79e01232-5ea4-41c8-9331-1c1880a1d3c2', '<html><body>This is the html page 79e01232-5ea4-41c8-9331-1c1880a1d3c2.</body></html>'),
(445, 'a35b6c5e-22d6-4707-98b4-462482e26c9e', '<html><body>This is the html page a35b6c5e-22d6-4707-98b4-462482e26c9e.</body></html>'),
(446, '3aff2f9c-0c7f-4630-99ad-27a0cf1af137', '<html><body>This is the html page 3aff2f9c-0c7f-4630-99ad-27a0cf1af137.</body></html>'),
(447, '77ec1d68-84e1-40f4-be8e-066e02f4e373', '<html><body>This is the html page 77ec1d68-84e1-40f4-be8e-066e02f4e373.</body></html>'),
(448, '8f824126-0bd1-4074-b88e-c0b59d3e67a3', '<html><body>This is the html page 8f824126-0bd1-4074-b88e-c0b59d3e67a3.</body></html>'),
(449, 'c6c80c75-b335-4f68-b7a7-59434413ce6c', '<html><body>This is the html page c6c80c75-b335-4f68-b7a7-59434413ce6c.</body></html>'),
(450, 'f959ecb3-6382-4ae5-9325-8fcbc068e446', '<html><body>This is the html page f959ecb3-6382-4ae5-9325-8fcbc068e446.</body></html>'),
(451, '2471caa8-e8df-44d6-94f2-7752a74f6819', '<html><body>This is the html page 2471caa8-e8df-44d6-94f2-7752a74f6819.</body></html>'),
(452, 'fa0979ca-2734-41f7-84c5-e40e0886e408', '<html><body>This is the html page fa0979ca-2734-41f7-84c5-e40e0886e408.</body></html>');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `HTML`
--
ALTER TABLE `HTML`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `HTML`
--
ALTER TABLE `HTML`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=453;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
