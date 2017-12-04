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
			url = new URL(serverConf.getWsdl());
			name = new QName(serverConf.getNamespace(), serverConf.getService());
			service = Service.create(url, name);
			server = service.getPort(ServersDAO.class);
			servers.add(server);
		}
	}
	
	public static List<ServersDAO> getServers(){
		return servers;
	}
	
	/**
	 * Recibe una petición GET y genera una respuesta que puede ser:
	 * Lista de todas las páginas, una sola página o un error 404.
	 * @param request Petición HTTP.
	 */
	public void methodGetHtml(HTTPRequest request){
		params = request.getResourceParameters();
		uuid = params.get("uuid");

		// Comprueba si se recibe el parámetro uuid.
		if(uuid == null) {  // Recupera una lista de páginas.
			status = HTTPResponseStatus.S200;
			type = MIME.TEXT_HTML.getMime();
			content = "";
			
		}
/*		else {
			// Comprueba si existe la página en el servidor.
			if (htmlDao.containsPage(uuid)) {
				status = HTTPResponseStatus.S200;
				content = htmlDao.getHtmlPage(uuid);
				type = MIME.TEXT_HTML.getMime();		
			} else{
				status = HTTPResponseStatus.S404;	
				type = MIME.TEXT_HTML.getMime();
			}
		}*/
	}

	
}
