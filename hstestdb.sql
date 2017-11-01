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

--
-- Base de datos: `hstestdb`
--

-- --------------------------------------------------------

--
-- Estructura de las tablas
--

CREATE TABLE `HTML` (
  `uuid` varchar(36) CHARACTER SET utf8 NOT NULL,
  `content` mediumtext CHARACTER SET utf8 NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `XML` (
  `uuid` varchar(36) CHARACTER SET utf8 NOT NULL,
  `content` mediumtext CHARACTER SET utf8 NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `XSD` (
  `uuid` varchar(36) CHARACTER SET utf8 NOT NULL,
  `content` mediumtext CHARACTER SET utf8 NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `XSLT` (
  `uuid` varchar(36) CHARACTER SET utf8 NOT NULL,
  `xsd` varchar(36) CHARACTER SET utf8 NOT NULL,
  `content` mediumtext CHARACTER SET utf8 NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Indices de las tablas y autoincrement
--
ALTER TABLE `HTML`
  ADD PRIMARY KEY (`uuid`);

ALTER TABLE `XML`
  ADD PRIMARY KEY (`uuid`);

ALTER TABLE `XSD`
  ADD PRIMARY KEY (`uuid`);

ALTER TABLE `XSLT`
  ADD PRIMARY KEY (`uuid`);

--
-- Volcado de datos para las tablas
--

INSERT INTO `HTML` (`uuid`, `content`) VALUES
('6df1047e-cf19-4a83-8cf3-38f5e53f7725', '<html><body>This is the html page 6df1047e-cf19-4a83-8cf3-38f5e53f7725.</body></html>'),
('79e01232-5ea4-41c8-9331-1c1880a1d3c2', '<html><body>This is the html page 79e01232-5ea4-41c8-9331-1c1880a1d3c2.</body></html>'),
('a35b6c5e-22d6-4707-98b4-462482e26c9e', '<html><body>This is the html page a35b6c5e-22d6-4707-98b4-462482e26c9e.</body></html>'),
('3aff2f9c-0c7f-4630-99ad-27a0cf1af137', '<html><body>This is the html page 3aff2f9c-0c7f-4630-99ad-27a0cf1af137.</body></html>'),
('77ec1d68-84e1-40f4-be8e-066e02f4e373', '<html><body>This is the html page 77ec1d68-84e1-40f4-be8e-066e02f4e373.</body></html>'),
('8f824126-0bd1-4074-b88e-c0b59d3e67a3', '<html><body>This is the html page 8f824126-0bd1-4074-b88e-c0b59d3e67a3.</body></html>'),
('c6c80c75-b335-4f68-b7a7-59434413ce6c', '<html><body>This is the html page c6c80c75-b335-4f68-b7a7-59434413ce6c.</body></html>'),
('f959ecb3-6382-4ae5-9325-8fcbc068e446', '<html><body>This is the html page f959ecb3-6382-4ae5-9325-8fcbc068e446.</body></html>'),
('2471caa8-e8df-44d6-94f2-7752a74f6819', '<html><body>This is the html page 2471caa8-e8df-44d6-94f2-7752a74f6819.</body></html>'),
('fa0979ca-2734-41f7-84c5-e40e0886e408', '<html><body>This is the html page fa0979ca-2734-41f7-84c5-e40e0886e408.</body></html>');

INSERT INTO `XML` (`uuid`, `content`) VALUES
('ddcab7d0-636c-11e4-8db3-685b35c84fb4', '<?xml version=\"1.0\" encoding=\"UTF-8\"?>  <people xmlns=\"http://www.esei.uvigo.es/dai/proyecto\"      xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"     xsi:schemaLocation=\"http://www.esei.uvigo.es/dai/proyecto sample.xsd\">  <person dni=\"123456\"><name>Pepe</name></person> </people>'),
('ea118888-6908-11e4-9620-685b35c84fb4', '<?xml version=\"1.0\" encoding=\"UTF-8\"?>  <tns:collection xmlns:tns=\"http://www.esei.uvigo.es/dai/proyecto\"  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"  xsi:schemaLocation=\"http://www.esei.uvigo.es/dai/proyecto sample2.xsd \">  <disc year=\"1972\">   <name>Live in Japan</name>   <genre>Hard Rock</genre>   <artist>Deep Purple</artist>  </disc>  <disc year=\"1972\">   <name>Led Zeppelin</name>   <genre>Hard Rock</genre>   <artist>Led Zeppelin</artist>  </disc>  <disc year=\"1990\">   <name>Shake Your Money Maker</name>   <genre>Southern Rock</genre>   <artist>The Black Crowes</artist>  </disc>    <movie year=\"1972\">   <name>The Godfather</name>   <genre>Drama</genre>   <director>Francis Ford Coppola</director>  </movie>  <movie>   <name>Interstellar</name>   <genre>Adventure</genre>   <director>Christopher Nolan</director>  </movie>    <book year=\"1965\">   <name>Dune</name>   <genre>Science Fiction</genre>   <author>Frank Herbert</author>   <pages>412</pages>  </book>  <book year=\"1922\">   <name>Siddhartha</name>   <genre>Novel</genre>   <author>Hermann Hesse</author>   <pages>152</pages>  </book> </tns:collection>');

INSERT INTO `XSD` (`uuid`, `content`) VALUES
('05b88faa-6909-11e4-aadc-685b35c84fb4', '<?xml version=\"1.0\" encoding=\"UTF-8\"?> <schema xmlns=\"http://www.w3.org/2001/XMLSchema\"   targetNamespace=\"http://www.esei.uvigo.es/dai/proyecto\"  xmlns:tns=\"http://www.esei.uvigo.es/dai/proyecto\">  <element name=\"collection\">   <complexType>    <sequence>     <element name=\"disc\" type=\"tns:disc\" minOccurs=\"0\" maxOccurs=\"unbounded\"/>     <element name=\"movie\" type=\"tns:movie\" minOccurs=\"0\" maxOccurs=\"unbounded\"/>     <element name=\"book\" type=\"tns:book\" minOccurs=\"0\" maxOccurs=\"unbounded\"/>    </sequence>   </complexType>  </element>      <complexType name=\"item\">      <sequence>       <element name=\"name\" type=\"string\"/>      </sequence>      <attribute name=\"year\" type=\"unsignedInt\"/>     </complexType>      <complexType name=\"disc\">      <complexContent>       <extension base=\"tns:item\">        <sequence>         <element name=\"genre\" type=\"string\"/>         <element name=\"artist\" type=\"string\"/>        </sequence>       </extension>      </complexContent>     </complexType>      <complexType name=\"movie\">      <complexContent>       <extension base=\"tns:item\">        <sequence>         <element name=\"genre\" type=\"string\"/>         <element name=\"director\" type=\"string\"/>        </sequence>       </extension>      </complexContent>     </complexType>      <complexType name=\"book\">      <complexContent>       <extension base=\"tns:item\">        <sequence>         <element name=\"genre\" type=\"string\"/>         <element name=\"author\" type=\"string\"/>         <element name=\"pages\" type=\"unsignedInt\"/>        </sequence>       </extension>      </complexContent>     </complexType> </schema>'),
('e5b64c34-636c-11e4-b729-685b35c84fb4', '<?xml version=\"1.0\" encoding=\"UTF-8\"?> <schema xmlns=\"http://www.w3.org/2001/XMLSchema\" targetNamespace=\"http://www.esei.uvigo.es/dai/proyecto\" xmlns:tns=\"http://www.esei.uvigo.es/dai/proyecto\" elementFormDefault=\"qualified\">  <element name=\"people\">   <complexType>    <sequence>     <element name=\"person\">      <complexType>       <sequence>        <element name=\"name\" type=\"string\" minOccurs=\"1\" maxOccurs=\"1\"/>       </sequence>       <attribute name=\"dni\" type=\"string\"/>      </complexType>     </element>    </sequence>   </complexType>  </element> </schema>');

INSERT INTO `XSLT` (`uuid`, `xsd`, `content`) VALUES
('1fd26c94-6909-11e4-9a75-685b35c84fb4', '05b88faa-6909-11e4-aadc-685b35c84fb4', '<?xml version=\"1.0\" encoding=\"UTF-8\"?> <xsl:stylesheet xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\"  xmlns:tns=\"http://www.esei.uvigo.es/dai/proyecto\" version=\"1.0\">   <xsl:output method=\"html\" indent=\"yes\" encoding=\"utf-8\" />   <xsl:template match=\"/\">   <xsl:text disable-output-escaping=\"yes\">&lt;!DOCTYPE html&gt;</xsl:text>   <html>    <head>     <title>Collection</title>    </head>    <body>     <div>      <h1>Collection</h1>      <h2>Discs</h2>      <xsl:apply-templates select=\"tns:collection/disc\" />      <hr/>      <h2>Movies</h2>      <xsl:apply-templates select=\"tns:collection/movie\" />      <hr/>      <h2>Books</h2>      <xsl:apply-templates select=\"tns:collection/book\" />     </div>    </body>   </html>  </xsl:template>   <xsl:template match=\"disc\">   <div class=\"disc\">    <h3><xsl:value-of select=\"name\"/>(<xsl:value-of select=\"@year\"/>)</h3>    <div class=\"artist\">Artist: <xsl:value-of select=\"artist\" /></div>    <div class=\"genre\">Genre: <xsl:value-of select=\"genre\" /></div>   </div>  </xsl:template>   <xsl:template match=\"movie\">   <div class=\"movie\">    <h3><xsl:value-of select=\"name\"/>(<xsl:value-of select=\"@year\"/>)</h3>    <div class=\"director\">Director: <xsl:value-of select=\"director\" /></div>    <div class=\"genre\">Genre: <xsl:value-of select=\"genre\" /></div>   </div>  </xsl:template>   <xsl:template match=\"book\">   <div class=\"book\">    <h3><xsl:value-of select=\"name\"/>(<xsl:value-of select=\"@year\"/>)</h3>    <div class=\"author\">Author: <xsl:value-of select=\"author\" /></div>    <div class=\"genre\">Genre: <xsl:value-of select=\"genre\" /></div>    <div class=\"pages\">Pages: <xsl:value-of select=\"pages\" /></div>   </div>  </xsl:template> </xsl:stylesheet>'),
('f260dfee-636c-11e4-bbdd-685b35c84fb4', 'e5b64c34-636c-11e4-b729-685b35c84fb4', '<?xml version=\"1.0\" encoding=\"UTF-8\"?> <xsl:stylesheet xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\"  xmlns:tns=\"http://www.esei.uvigo.es/dai/proyecto\" version=\"1.0\">   <xsl:output method=\"html\" indent=\"yes\" encoding=\"utf-8\" />   <xsl:template match=\"/\">   <xsl:text disable-output-escaping=\"yes\">&lt;!DOCTYPE html&gt;</xsl:text>   <html>    <head>     <title>People</title>    </head>    <body>     <div>      <h1>People</h1>      <xsl:apply-templates select=\"tns:people/tns:person\" />     </div>    </body>   </html>  </xsl:template>   <xsl:template match=\"tns:person\">   <div class=\"person\">    <h3>DNI: <xsl:value-of select=\"@dni\"/></h3>    <div class=\"name\"><xsl:value-of select=\"tns:name\" /></div>   </div>  </xsl:template> </xsl:stylesheet>');
