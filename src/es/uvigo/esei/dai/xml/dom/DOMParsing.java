package es.uvigo.esei.dai.xml.dom;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import es.uvigo.esei.dai.xml.SimpleErrorHandler;

public class DOMParsing {
	public static Document loadDocument(String documentPath) 
	throws ParserConfigurationException, SAXException, IOException {
		// Construcción del parser del documento
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		// factory.setNamespaceAware(true);
		
		DocumentBuilder builder = factory.newDocumentBuilder();
		
		// Parsing del documento
		return builder.parse(new File(documentPath));
	}
	
	public static Document loadAndValidateWithInternalDTD(String documentPath) 
	throws ParserConfigurationException, SAXException, IOException {
		// Construcción del parser del documento activando validación
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(true);
		
		// Al construir el parser hay que añadir un manejador de errores
		DocumentBuilder builder = factory.newDocumentBuilder();
		builder.setErrorHandler(new SimpleErrorHandler());
		
		// Parsing y validación del documento
		return builder.parse(new File(documentPath));
	}
	
	public static Document loadAndValidateWithInternalXSD(String documentPath) 
	throws ParserConfigurationException, SAXException, IOException {
		// Construcción del parser del documento activando validación
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(true);
		factory.setNamespaceAware(true);
		factory.setAttribute(
			"http://java.sun.com/xml/jaxp/properties/schemaLanguage", 
			XMLConstants.W3C_XML_SCHEMA_NS_URI
		);
		
		// Al construir el parser hay que añadir un manejador de errores
		DocumentBuilder builder = factory.newDocumentBuilder();
		builder.setErrorHandler(new SimpleErrorHandler());
		
		// Parsing y validación del documento
		return builder.parse(new File(documentPath));
	}
	
	public static Document loadAndValidateWithExternalXSD(String documentPath, String schemaPath) 
	throws ParserConfigurationException, SAXException, IOException {
		// Construcción del schema
		SchemaFactory schemaFactory = 
			SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = schemaFactory.newSchema(new File(schemaPath));
		System.out.println(">>"+schema.toString());
		
		// Construcción del parser del documento. Se establece el esquema y se activa
		// la validación y comprobación de namespaces
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(false);
		factory.setNamespaceAware(true);
		factory.setSchema(schema);
		
		// Se añade el manejador de errores
		DocumentBuilder builder = factory.newDocumentBuilder();
		builder.setErrorHandler(new SimpleErrorHandler());
		
		return builder.parse(new File(documentPath));
	}
	
	public static Document loadAndValidateWithExternalURL(String documentPath, String schemaPath) 
			throws ParserConfigurationException, SAXException, IOException {
				// Construcción del schema		

		SchemaFactory schemaFactory = 
			SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = schemaFactory.newSchema(new URL(schemaPath));
		
		// Construcción del parser del documento. Se establece el esquema y se activa
		// la validación y comprobación de namespaces
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(false);
		factory.setNamespaceAware(true);
		factory.setSchema(schema);
		
		// Se añade el manejador de errores
		DocumentBuilder builder = factory.newDocumentBuilder();
		builder.setErrorHandler(new SimpleErrorHandler());
		
		return builder.parse(documentPath);
	}
	
	public static String toXML(Document document)
	throws TransformerException {
		// Creación y configuración del transformador. En este caso, se activa
		// la indentación del XML
		TransformerFactory factory = TransformerFactory.newInstance();
		factory.setAttribute("indent-number", 3);
		
		Transformer transformer = factory.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		
		// El resultado se almacenará en una cadena de texto
		StringWriter writer = new StringWriter();
		
		transformer.transform(
			new DOMSource(document),
			new StreamResult(writer)
		);
		
		return writer.toString();
	}
}
