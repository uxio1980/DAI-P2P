package es.uvigo.esei.dai.hybridserver;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import es.uvigo.esei.dai.hybridserver.http.HTTPRequest;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponseStatus;
import es.uvigo.esei.dai.hybridserver.http.MIME;

public class HtmlManager {

	private final HtmlDAO htmlDao;
	private Map<String,String> params;
	private String uuid;
	private HTTPResponseStatus status;
	private String content;
	private String type;

	/**
	 * Constructor que recibe una instancia de la interfaz HtmlDAO.
	 * @param htmlDao Interfaz que permite interactuar con las webs almacenadas.
	 */
	public HtmlManager(HtmlDAO htmlDao) {
		this.htmlDao = htmlDao;
	}
	
	/**
	 * Recibe una petición GET y genera una respuesta que puede ser:
	 * Lista de todas las páginas, una sola página o un error 404.
	 * @param request Petición HTTP.
	 */
	public void methodGet(HTTPRequest request, Map<String, ServersDAO> servers){
		params = request.getResourceParameters();
		uuid = params.get("uuid");
		type = MIME.TEXT_HTML.getMime();
		// Comprueba si se recibe el parámetro uuid.
		if(uuid == null) {  // Recupera una lista de páginas.
			status = HTTPResponseStatus.S200;
			StringBuilder sc = new StringBuilder();
			sc.append("<h1>Local Server</h1>");
			sc.append(htmlDao.getHtmlList()); // Recupera una lista de páginas.
			for (Map.Entry<String, ServersDAO> server: servers.entrySet()) {
				sc.append("<h1>" + server.getKey()+"</h1>");
				sc.append(server.getValue().getHTML());
			}
			content = sc.toString();
			if (content.isEmpty()) {
				status = HTTPResponseStatus.S404;	
			}
		}
		else {
			status = HTTPResponseStatus.S200;
			// Comprueba si existe la página en el servidor.
			if (htmlDao.containsPage(uuid)) {	
				content = htmlDao.getHtmlPage(uuid);		
			} else {
				for (Map.Entry<String, ServersDAO> server: servers.entrySet()) {
					content = server.getValue().htmlContent(uuid);
					if (content != null)
						break;
				}
				if (content == null) {
					status = HTTPResponseStatus.S404;	
				}	
			}
		}
	}

	/**
	 * Recibe una petición POST y genera una respuesta positiva o un error 400.
	 * @param request Petición HTTP.
	 */
	public void methodPost(HTTPRequest request){	
		params = request.getResourceParameters();				
		UUID randomUuid = UUID.randomUUID();
		uuid = randomUuid.toString();

		// Comprueba si el parámetro del formulario se llama html.
		if(params.containsKey("html")){
			htmlDao.createHtmlPage(uuid, params.get("html")); // Crea la página.
			status = HTTPResponseStatus.S200;
			content = "<a href=\"html?uuid="+ uuid +"\">"+ uuid +"</a>";
			type = MIME.TEXT_HTML.getMime();	
		} else{
			status = HTTPResponseStatus.S400;
			type = MIME.TEXT_HTML.getMime();
		}
	}

	/**
	 * Recibe una petición DELETE y genera una respuesta positiva o un error 404.
	 * @param request Petición HTTP.
	 */
	public void methodDelete(HTTPRequest request){
		params = request.getResourceParameters();
		uuid = params.get("uuid");

		// Comprueba si existe la página en el servidor.
		if(htmlDao.containsPage(uuid)){
			htmlDao.deleteHtmlPage(uuid); // Borra la página.
			status = HTTPResponseStatus.S200;
			type = MIME.TEXT_HTML.getMime();
		} else{
			status = HTTPResponseStatus.S404;
			type = MIME.TEXT_HTML.getMime();
		}
	}
	
	public HTTPResponseStatus getStatus() {
		return status;
	}

	public String getContent() {
		return content;
	}

	public String getType() {
		return type;
	}
}
