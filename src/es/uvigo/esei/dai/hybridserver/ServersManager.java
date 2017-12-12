package es.uvigo.esei.dai.hybridserver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import es.uvigo.esei.dai.hybridserver.http.HTTPRequest;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponseStatus;
import es.uvigo.esei.dai.hybridserver.http.MIME;

public class ServersManager {

	private Map<String,String> params;
	private String uuid;
	private HTTPResponseStatus status;
	private String content;
	private String type;
	private static List<ServersDAO> servers = new ArrayList<>();
	
	public ServersManager(Configuration config) throws MalformedURLException {
		QName name;
		URL url; 
		Service service;
		ServersDAO server;
		
		for (ServerConfiguration serverConf : config.getServers()){	
			if(!serverConf.getName().equals("Down Server")){
				name = new QName(serverConf.getNamespace(), "ServersDAODBService");
				url = new URL(serverConf.getWsdl());
				service = Service.create(url, name);
				server = service.getPort(ServersDAO.class);
				servers.add(server);
			}
		}
	}
	
	public static List<ServersDAO> getServers(){
		return servers;
	}

	
}
