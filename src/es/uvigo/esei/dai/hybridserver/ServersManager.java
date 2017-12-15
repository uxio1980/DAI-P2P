package es.uvigo.esei.dai.hybridserver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
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
	private Map<String, ServersDAO> servers;
	
	public ServersManager(Configuration config) throws MalformedURLException {
		QName name;
		URL url; 
		Service service;
		ServersDAO server;
		servers = new HashMap<>();
		
		for (ServerConfiguration serverConf : config.getServers()){	
			name = new QName(serverConf.getNamespace(), "ServersDAODBService");
			url = new URL(serverConf.getWsdl());
			try{
				service = Service.create(url, name);
				server = service.getPort(ServersDAO.class);
				servers.put(serverConf.getName(), server);
			} catch(Exception e){}
		}
	}
	
	public Map<String, ServersDAO> getServers(){
		return servers;
	}

	
}
