<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:l="http://www.esei.uvigo.es/dai/hybridserver"
>
	<xsl:output method="html" indent="yes" encoding="utf-8"/>

	<xsl:template match="/">
		<html>
			<head>
				<title>Configuración</title>
			</head>
			<body>
				<h1>Configuración</h1>
				<div>
					<h2>Conexión</h2>
					<xsl:apply-templates select="l:configuration/l:connections" />
				</div>
				<div>
					<h2>Base de datos</h2>
					<xsl:apply-templates select="l:configuration/l:database" />
				</div>
				<div>
					<h2>Servidores</h2>
					<xsl:apply-templates select="l:configuration/l:servers" />
				</div>
			</body>
		</html>
	</xsl:template>

	<xsl:template match="l:connections">
		<div class="connection">
			<div class="http">
				<ul>
					<li><b>Puerto: </b><xsl:value-of select="l:http" /></li>
					<li><b>Servicio Web: </b><xsl:value-of select="l:webservice" /></li>
					<li><b>Nº Clientes: </b><xsl:value-of select="l:numClients" /></li>
				</ul>
			</div>
		</div>
	</xsl:template>

	<xsl:template match="l:database">
		<div class="database">
			<div class="user">
				<ul>
					<li><b>Usuario: </b><xsl:value-of select="l:user" /></li>
					<li><b>Contraseña: </b><xsl:value-of select="l:password" /></li>
					<li><b>Url: </b><xsl:value-of select="l:url	" /></li>
				</ul>
			</div>
		</div>
	</xsl:template>

	<xsl:template match="l:servers">
			<div class="servers">
				<ul>
					<xsl:for-each select="l:server">
						<h3><xsl:value-of select="@name"/></h3>
						<dt><b>· Wsdl: </b><xsl:value-of select="@wsdl"/></dt>
						<dt><b>· Namesapce: </b><xsl:value-of select="@namespace"/></dt>
						<dt><b>· Service: </b><xsl:value-of select="@service"/></dt>
						<dt><b>· HttpAddress: </b><xsl:value-of select="@httpAddress	"/></dt>
					</xsl:for-each>
				</ul>
			</div>
		</xsl:template>

</xsl:stylesheet>
