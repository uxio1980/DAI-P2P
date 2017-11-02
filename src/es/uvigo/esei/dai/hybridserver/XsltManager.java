package es.uvigo.esei.dai.hybridserver;

import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.uvigo.esei.dai.hybridserver.http.HTTPRequest;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponseStatus;
import es.uvigo.esei.dai.hybridserver.http.MIME;

public class XsltManager {

	private final XsltDAO XsltDao;
	private Map<String,String> params;
	private String uuid;
	private HTTPResponseStatus status;
	private String content;
	private String type;

	/**
	 * Constructor que recibe una instancia de la interfaz XsltDAO.
	 * @param XsltDao Interfaz que permite interactuar con las webs almacenadas.
	 */
	public XsltManager(XsltDAO XsltDao) {
		this.XsltDao = XsltDao;
	}
	
	/**
	 * Recibe una petición GET y genera una respuesta que puede ser:
	 * Lista de todas las páginas, una sola página o un error 404.
	 * @param request Petición HTTP.
	 */
	public void methodGet(HTTPRequest request){
		params = request.getResourceParameters();
		uuid = params.get("uuid");

		// Comprueba si se recibe el parámetro uuid.
		if(uuid == null) { 
			status = HTTPResponseStatus.S200;
			content = XsltDao.getXsltList(); // Recupera una lista de páginas.
			type = MIME.TEXT_HTML.getMime();
		}
		else {
			// Comprueba si existe la página en el servidor.
			if (XsltDao.containsPage(uuid)) {
				status = HTTPResponseStatus.S200;
				content = XsltDao.getXsltPage(uuid);
				type = MIME.APPLICATION_XML.getMime();
			} else{
				status = HTTPResponseStatus.S404;	
				type = MIME.APPLICATION_XML.getMime();
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
		type = MIME.APPLICATION_XML.getMime();

		// Comprueba si los parámetros del formulario se llaman xslt y xsd.
		if(params.containsKey("xslt") && params.containsKey("xsd")){
			if (!XsltDao.findXsd(params.get("xsd"))) 
				status = HTTPResponseStatus.S404;
			else {
				XsltDao.createXsltPage(uuid, params.get("xsd"), params.get("xslt")); // Crea la página.
				status = HTTPResponseStatus.S200;
				content = "<a href=\"xslt?uuid="+ uuid +"\">"+ uuid +"</a>";
			}
		} else{
			status = HTTPResponseStatus.S400;	
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
		if(XsltDao.containsPage(uuid)){
			XsltDao.deleteXsltPage(uuid); // Borra la página.
			status = HTTPResponseStatus.S200;
			type = MIME.APPLICATION_XML.getMime();
		} else{
			status = HTTPResponseStatus.S404;
			type = MIME.APPLICATION_XML.getMime();
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
