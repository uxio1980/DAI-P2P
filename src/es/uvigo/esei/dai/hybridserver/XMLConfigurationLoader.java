/**
 *  HybridServer
 *  Copyright (C) 2017 Miguel Reboiro-Jato
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.uvigo.esei.dai.hybridserver;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLConfigurationLoader {
	public Configuration load(File xmlFile) throws Exception {
		
		Configuration conf = new Configuration();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(false);
		factory.setNamespaceAware(true);
		factory.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaLanguage",XMLConstants.W3C_XML_SCHEMA_NS_URI);
		
		// Se añade el manejador de errores
		DocumentBuilder builder = factory.newDocumentBuilder();
		builder.setErrorHandler(new SimpleErrorHandler());
		Document doc = builder.parse(xmlFile);
		
		// Setear conf con los elementos del árbol DOM
		Element root = (Element)doc.getFirstChild();
		
		// Connection
		NodeList nodes = root.getElementsByTagName("connections");
		conf.setHttpPort(Integer.parseInt(
				nodes.item(0).toString()
			));
		conf.setWebServiceURL(
				nodes.item(1).toString()
			);
		conf.setNumClients(Integer.parseInt(
				nodes.item(2).toString()
			));
		
		// Database
		nodes = root.getElementsByTagName("database");
		conf.setDbUser(
				nodes.item(0).toString()
			);
		conf.setDbPassword(
				nodes.item(1).toString()
			);
		conf.setDbURL(
				nodes.item(2).toString()
			);
		
		// Servers
		nodes = root.getElementsByTagName("servers");
		Element element;
		List<ServerConfiguration> servers = new ArrayList<>();
		ServerConfiguration server;
		for(int i=0; i<nodes.getLength(); i++){
			element = (Element)nodes.item(i);
			server = new ServerConfiguration(
				element.getAttribute("name"),
				element.getAttribute("wsdl"),
				element.getAttribute("namespace"),
				element.getAttribute("service"),
				element.getAttribute("httpAddress")
			);
			servers.add(server);
		}
		conf.setServers(servers);
		
		return conf;
	}
}

