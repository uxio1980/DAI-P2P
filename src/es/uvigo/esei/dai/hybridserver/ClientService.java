package es.uvigo.esei.dai.hybridserver;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import es.uvigo.esei.dai.hybridserver.http.HTTPHeaders;
import es.uvigo.esei.dai.hybridserver.http.HTTPParseException;
import es.uvigo.esei.dai.hybridserver.http.HTTPRequest;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponse;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponseStatus;
import es.uvigo.esei.dai.hybridserver.http.MIME;

public class ClientService implements Runnable {

	private Socket socket;
	private HTTPResponse response;
	private final String[] RESOURCES = {"html","xml","xsd","xslt"};
	private HtmlManager htmlManager;
	private XmlManager xmlManager;

	/**
	 * Crea un hilo de cliente.
	 * @param socket Socket de conexión con el servidor.
	 * @param htmlDao Interfaz para interactuar con el servidor.
	 */
	public ClientService(Socket socket, HtmlDAO htmlDao, XmlDAO xmlDao) {
		this.socket = socket;
		response = new HTTPResponse();
		htmlManager = new HtmlManager(htmlDao);
		xmlManager = new XmlManager(xmlDao);
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
	 * Genera una respuesta HTTP.
	 * @param status Status HTTP de la respuesta.
	 */
	private void setResponse(HTTPResponseStatus status){
		response.setVersion(HTTPHeaders.HTTP_1_1.getHeader());
		response.setStatus(status);
		response.setContent(status.getStatus());
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
					// En función del método y tipo de recurso llama a un Manager distinto.
					switch (request.getMethod()) {
					case GET:
						if(resource.equals("html")) {
							htmlManager.methodGet(request);
							this.response = htmlManager.getResponse();
						}
						else if(resource.equals("xml")){
							xmlManager.methodGet(request);
							this.response = xmlManager.getResponse();
						}
						break;
					case POST:
						if(resource.equals("html")) {
							htmlManager.methodPost(request);
							this.response = htmlManager.getResponse();
						}
						else if(resource.equals("xml")){
							xmlManager.methodPost(request);
							this.response = xmlManager.getResponse();
						}
						break;
					case DELETE:
						if(resource.equals("html")) {
							htmlManager.methodDelete(request);
							this.response = htmlManager.getResponse();
						}
						else if(resource.equals("xml")){
							xmlManager.methodDelete(request);
							this.response = xmlManager.getResponse();
						}
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
						"</html>", MIME.TEXT_HTML.getMime());	
			else
				setResponse(HTTPResponseStatus.S400);

			System.out.println(getResponse());
			out.println(getResponse());

		} catch (IOException | HTTPParseException e) {
			System.out.println("\t>> Error en Thread Client:\n" + e.getMessage());
		} 
	}
}
