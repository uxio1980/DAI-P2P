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
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import es.uvigo.esei.dai.xml.dom.DOMParsing;

public class XMLConfigurationLoader {
	public Configuration load(File xmlFile) throws Exception {
		
		Document doc = DOMParsing.loadAndValidateWithInternalXSD(xmlFile.getAbsolutePath());
		//System.out.println(DOMParsing.toXML(doc));
		Configuration conf = new Configuration();
		
		
		// Setear conf con los elementos del árbol DOM
		Element root = (Element)doc.getFirstChild();
		
		// Configuration
		List<Node> child = new ArrayList<>();
		NodeList children = root.getChildNodes();
		Node nodo;
		
		for(int i=0;i<children.getLength();i++){
			nodo = children.item(i);
			if (nodo instanceof Element){
				child.add(nodo);
			}
		}
		
		Node connections = child.get(0);
		Node database = child.get(1);
		Node servers = child.get(2);

		// Connections
		children = connections.getChildNodes();
		child = new ArrayList<>();
		
		for(int i=0;i<children.getLength();i++){
			nodo = children.item(i);
			if (nodo instanceof Element){
				child.add(nodo);
			}
		}	
		conf.setHttpPort(Integer.parseInt(child.get(0).getTextContent()));
		conf.setWebServiceURL(child.get(1).getTextContent());
		conf.setNumClients(Integer.parseInt(child.get(2).getTextContent()));
		
		// Database
		children = database.getChildNodes();
		child = new ArrayList<>();
		
		for(int i=0;i<children.getLength();i++){
			nodo = children.item(i);
			if (nodo instanceof Element){
				child.add(nodo);
			}
		}	
		conf.setDbUser(child.get(0).getTextContent());
		conf.setDbPassword(child.get(1).getTextContent());
		conf.setDbURL(child.get(2).getTextContent());
		
		// Servers
		children = servers.getChildNodes();
		child = new ArrayList<>();
		
		for(int i=0;i<children.getLength();i++){
			nodo = children.item(i);
			if (nodo instanceof Element){
				child.add(nodo);
			}
		}	
		
		ServerConfiguration sc = new ServerConfiguration();
		List<ServerConfiguration> serverList = new ArrayList<>();
		NamedNodeMap attributes;
		for(Node n: child){
			attributes = n.getAttributes();
			sc.setName(attributes.getNamedItem("name").getTextContent());
			sc.setWsdl(attributes.getNamedItem("wsdl").getTextContent());
			sc.setNamespace(attributes.getNamedItem("namespace").getTextContent());
			sc.setService(attributes.getNamedItem("service").getTextContent());
			sc.setHttpAddress(attributes.getNamedItem("httpAddress").getTextContent());
			serverList.add(sc);
			sc = new ServerConfiguration();
		}
		conf.setServers(serverList);
		
		return conf;

	}
}

