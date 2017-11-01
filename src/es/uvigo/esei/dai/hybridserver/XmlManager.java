package es.uvigo.esei.dai.hybridserver;

import java.util.Map;
import java.util.UUID;
import es.uvigo.esei.dai.hybridserver.http.HTTPHeaders;
import es.uvigo.esei.dai.hybridserver.http.HTTPRequest;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponse;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponseStatus;
import es.uvigo.esei.dai.hybridserver.http.MIME;

public class XmlManager {

	private final XmlDAO xmlDao;
	private HTTPResponse response;
	private Map<String,String> params;
	private String uuid;

	/**
	 * Constructor que recibe una instancia de la interfaz XmlDAO.
	 * @param xmlDao Interfaz que permite interactuar con las webs almacenadas.
	 */
	public XmlManager(XmlDAO xmlDao) {
		this.xmlDao = xmlDao;
		response = new HTTPResponse();
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
		if(uuid == null) {  // Recupera una lista de páginas.
			setResponse(HTTPResponseStatus.S200, xmlDao.getXmlList(), MIME.TEXT_HTML.getMime());
		}
		else {
			// Comprueba si existe la página en el servidor.
			if (xmlDao.containsPage(uuid)) {
				String content = xmlDao.getXmlPage(uuid); // Recupera una página.
				setResponse(HTTPResponseStatus.S200, content, MIME.APPLICATION_XML.getMime());		
			} else
				setResponse(HTTPResponseStatus.S404, MIME.APPLICATION_XML.getMime());
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
		if(params.containsKey("xml")){
			xmlDao.createXmlPage(uuid, params.get("xml")); // Crea la página.
			setResponse(HTTPResponseStatus.S200, "<a href=\"xml?uuid="+ uuid +"\">"+ uuid +"</a>", 
					MIME.APPLICATION_XML.getMime());
		} else
			setResponse(HTTPResponseStatus.S400, MIME.APPLICATION_XML.getMime());
	}

	/**
	 * Recibe una petición DELETE y genera una respuesta positiva o un error 404.
	 * @param request Petición HTTP.
	 */
	public void methodDelete(HTTPRequest request){
		params = request.getResourceParameters();
		uuid = params.get("uuid");

		// Comprueba si existe la página en el servidor.
		if(xmlDao.containsPage(uuid)){
			xmlDao.deleteXmlPage(uuid); // Borra la página.
			setResponse(HTTPResponseStatus.S200, MIME.APPLICATION_XML.getMime());
		} else
			setResponse(HTTPResponseStatus.S404, MIME.APPLICATION_XML.getMime());
	}
	
	/**
	 * Genera una respuesta HTTP.
	 * @param Status Status HTTP de la respuesta.
	 * @param content Contenido de la respuesta.
	 * @param type Tipo del contenido.
	 */
	private void setResponse(HTTPResponseStatus status, String content, String type){
		response.setVersion(HTTPHeaders.HTTP_1_1.getHeader());
		response.setStatus(status);
		response.setContent(content);
		response.putParameter("Content-Type", type);
	}

	/**
	 * Genera una respuesta HTTP.
	 * @param status Status HTTP de la respuesta.
	 * @param type Tipo del contenido.
	 */
	private void setResponse(HTTPResponseStatus status, String type){
		response.setVersion(HTTPHeaders.HTTP_1_1.getHeader());
		response.setStatus(status);
		response.setContent(status.getStatus());
		response.putParameter("Content-Type", type);
	}
	
	/**
	 * Devuelve una respuesta HTTP.
	 * @return Respuesta HTTP
	 */
	public HTTPResponse getResponse(){
		return response;
	}
}
