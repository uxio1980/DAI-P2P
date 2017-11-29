package es.uvigo.esei.dai.hybridserver;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.Properties;

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
	private XsdManager xsdManager;
	private XsltManager xsltManager;
	private Configuration config;

	/**
	 * Crea un hilo de cliente e inicializa los parámetros de conexión con la BD.
	 * @param socket Socket de conexión con el servidor.
	 * @param htmlDao Interfaz para interactuar con el servidor.
	 */
	public ClientService(Socket socket, Properties properties) {
		this.socket = socket;
		response = new HTTPResponse();
		htmlManager = new HtmlManager(new HtmlDAODB(properties));
		xmlManager = new XmlManager(new XmlDAODB(properties));
		xsdManager = new XsdManager(new XsdDAODB(properties));
		xsltManager = new XsltManager(new XsltDAODB(properties));
	}
	
	public ClientService(Socket socket, Configuration config) {
		this.socket = socket;
		this.config = config;
		response = new HTTPResponse();
		htmlManager = new HtmlManager(new HtmlDAODB(config));
		xmlManager = new XmlManager(new XmlDAODB(config));
		xsdManager = new XsdManager(new XsdDAODB(config));
		xsltManager = new XsltManager(new XsltDAODB(config));
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
		response.putParameter("Content-Type", type);
		if(content != null)
			response.setContent(content);
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
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			String method = request.getMethod().toString();
			String resource = request.getResourceName();

			// Comprueba si el recurso es válido. 
			// Si está vacío lleva a la página de inicio. Si no existe muestra un error.
			if(Arrays.asList(RESOURCES).contains(resource)){
				try{
					// En función del método y tipo de recurso llama a un Manager distinto.
					switch(resource) {
					case "html":
						if(method.equals("GET"))
							htmlManager.methodGet(request);
						else if(method.equals("POST"))
							htmlManager.methodPost(request);
						else if(method.equals("DELETE"))
							htmlManager.methodDelete(request);
						setResponse(htmlManager.getStatus(), 
								htmlManager.getContent(), htmlManager.getType());
						break;
					case "xml":
						if(method.equals("GET"))
							xmlManager.methodGet(request, config.getHttpPort());
						else if(method.equals("POST"))
							xmlManager.methodPost(request);
						else if(method.equals("DELETE"))
							xmlManager.methodDelete(request);
						setResponse(xmlManager.getStatus(), 
								xmlManager.getContent(), xmlManager.getType());
						break;
					case "xsd":
						if(method.equals("GET"))
							xsdManager.methodGet(request);
						else if(method.equals("POST"))
							xsdManager.methodPost(request);
						else if(method.equals("DELETE"))
							xsdManager.methodDelete(request);
						setResponse(xsdManager.getStatus(), 
								xsdManager.getContent(), xsdManager.getType());
						break;
					case "xslt":
						if(method.equals("GET"))
							xsltManager.methodGet(request);
						else if(method.equals("POST"))
							xsltManager.methodPost(request);
						else if(method.equals("DELETE"))
							xsltManager.methodDelete(request);
						setResponse(xsltManager.getStatus(), 
								xsltManager.getContent(), xsltManager.getType());
						break;
					}
					// Si se produce un error no experado en la BD lanza un error 500.
				} catch(Exception e) {
					setResponse(HTTPResponseStatus.S500, null, resource);
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
			out.println(getResponse());
			System.out.println(request);
			System.out.println(getResponse()+"\n");
		} 
		catch (IOException | HTTPParseException e) {
			System.out.println("\t>> Error en Thread Client:\n" + e.getMessage());
		} 
	}
}
