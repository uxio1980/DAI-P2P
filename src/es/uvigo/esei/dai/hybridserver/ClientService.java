package es.uvigo.esei.dai.hybridserver;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

import es.uvigo.esei.dai.hybridserver.http.HTTPHeaders;
import es.uvigo.esei.dai.hybridserver.http.HTTPParseException;
import es.uvigo.esei.dai.hybridserver.http.HTTPRequest;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponse;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponseStatus;

public class ClientService implements Runnable {

	private Socket socket;
	private HtmlManager htmlManager;
	private Map<String,String> params;
	private HTTPResponse response;
	private String uuid;
	private final String[] RESOURCES = {"html","xml","xsd","xslt"};

	/**
	 * Crea un hilo de cliente.
	 * @param socket Socket de conexión con el servidor.
	 * @param htmlDao Interfaz para interactuar con el servidor.
	 */
	public ClientService(Socket socket, HtmlDAO htmlDao) {
		this.socket = socket;
		response = new HTTPResponse();
		htmlManager = new HtmlManager(htmlDao);
	}

	/**
	 * Recibe una petición GET y genera una respuesta que puede ser:
	 * Lista de todas las páginas, una sola página o un error 404.
	 * @param request Petición HTTP.
	 */
	private void methodGet(HTTPRequest request){
		params = request.getResourceParameters();
		uuid = params.get("uuid");

		// Comprueba si se recibe el parámetro uuid.
		if(uuid == null) {  // Recupera una lista de páginas.
			setResponse(HTTPResponseStatus.S200, htmlManager.getHtmlList(), request.getResourceName());
		}
		else {
			// Comprueba si existe la página en el servidor.
			if (htmlManager.contains(uuid)) {
				String id = htmlManager.get(uuid); // Recupera una página.
				setResponse(HTTPResponseStatus.S200, id, request.getResourceName());		
			} else
				setResponse(HTTPResponseStatus.S404, request.getResourceName());
		}
	}

	/**
	 * Recibe una petición POST y genera una respuesta positiva o un error 400.
	 * @param request Petición HTTP.
	 */
	private void methodPost(HTTPRequest request){	
		params = request.getResourceParameters();				
		UUID randomUuid = UUID.randomUUID();
		uuid = randomUuid.toString();

		// Comprueba si el parámetro del formulario se llama html.
		if(params.containsKey("html")){
			htmlManager.create(uuid, params.get("html")); // Crea la página.
			setResponse(HTTPResponseStatus.S200, "<a href=\"html?uuid="+ uuid +"\">"+ uuid +"</a>", request.getResourceName());
		} else
			setResponse(HTTPResponseStatus.S400, request.getResourceName());
	}

	/**
	 * Recibe una petición DELETE y genera una respuesta positiva o un error 404.
	 * @param request Petición HTTP.
	 */
	private void methodDelete(HTTPRequest request){
		params = request.getResourceParameters();
		uuid = params.get("uuid");

		// Comprueba si existe la página en el servidor.
		if(htmlManager.contains(uuid)){
			htmlManager.delete(uuid); // Borra la página.
			setResponse(HTTPResponseStatus.S200, request.getResourceName());
		} else
			setResponse(HTTPResponseStatus.S404, request.getResourceName());
	}

	/**
	 * Genera una respuesta HTTP.
	 * @param Status Status HTTP de la respuesta.
	 * @param content Contenido de la respuesta.
	 */
	private void setResponse(HTTPResponseStatus status, String content, String type){
		response.setVersion(HTTPHeaders.HTTP_1_1.getHeader());
		response.setStatus(status);
		response.setContent(content);
		response.putParameter("Content-Type", "text/"+type);
	}

	/**
	 * Genera una respuesta HTTP.
	 * @param status Status HTTP de la respuesta.
	 */
	private void setResponse(HTTPResponseStatus status, String type){
		response.setVersion(HTTPHeaders.HTTP_1_1.getHeader());
		response.setStatus(status);
		response.setContent(status.getStatus());
		response.putParameter("Content-Type", "text/"+type);
	}

	/**
	 * Devuelve una respuesta HTTP.
	 * @return Respuesta HTTP
	 */
	private HTTPResponse getResponse(){
		return response;
	}

	/**
	 * Ejecuta el hilo del cliente que gestiona las request/response.
	 */
	@Override
	public void run() {
		try (Socket s = socket){
			HTTPRequest request = new HTTPRequest(
					new InputStreamReader(socket.getInputStream()));
			System.out.println(request);
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			String resource = request.getResourceName();

			// Comprueba si el recurso es válido. 
			// Si está vacío lleva a la página de inicio. Si no existe muestra un error.
			if(Arrays.asList(RESOURCES).contains(resource)){
				try{
					switch (request.getMethod()) {
					case GET:
						methodGet(request);
						break;
					case POST:
						methodPost(request);
						break;
					case DELETE:
						methodDelete(request);
						break;
					default: break;
					}
					// Si se produce un error no experado en la BD lanza un error 500.
				} catch(Exception e) {
					setResponse(HTTPResponseStatus.S500,resource);
					out.println(getResponse());
				}
			}
			else if(resource.isEmpty())
				setResponse(HTTPResponseStatus.S200, 
						"<html>" +
						"<head><meta charset='UTF-8'></head> " +
						"<p><strong>Hybrid Server</strong></p> " +
						"Iago Fernández González & Jose Eugenio González Fernández" +
						"</html>", resource);	
			else
				setResponse(HTTPResponseStatus.S400, resource);

			System.out.println(getResponse());
			out.println(getResponse());

		} catch (IOException | HTTPParseException e) {
			System.out.println("\t>> Error en Thread Client:\n" + e.getMessage());
		} 
	}
}
