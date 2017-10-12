package es.uvigo.esei.dai.hybridserver;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Iterator;
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

	public ClientService(Socket socket, HtmlDAO htmlDao) {
		this.socket = socket;
		this.htmlManager = new HtmlManager(htmlDao);
	}

	@Override
	public void run() {
		try (Socket s = socket){
			HTTPRequest request = new HTTPRequest(
					new InputStreamReader(socket.getInputStream()));
			System.out.println(request);
			Map<String,String> params;
			HTTPResponse response ;
			PrintWriter out ;
			String uuid;
			switch (request.getMethod()) {
			case GET:
				params = request.getResourceParameters();
				uuid = params.get("uuid");
				params = request.getResourceParameters();
			    response = new HTTPResponse();
				response.setVersion(HTTPHeaders.HTTP_1_1.getHeader());
				response.setStatus(HTTPResponseStatus.S200);
				if(uuid == null) {
					if (request.getResourceName().equals("html")) {
						//recorrer lista
						StringBuilder sb = new StringBuilder();
						Iterator<String> iterator = (htmlManager.getHtmlList(socket.getLocalPort())).iterator();
						while (iterator.hasNext()) {
							sb.append(iterator.next());
						}
						response.setContent(sb.toString());
					}else {
						response.setContent("Hybrid Server " + Thread.currentThread().getName()+ "\n");		
					}
				}
				else {
					if(!request.getResourceName().equals("html")) {
						response.setStatus(HTTPResponseStatus.S400);
						response.setContent("Bad Request.");
					}else {
						String content = htmlManager.get(uuid);
						if (content == null) {
							response.setStatus(HTTPResponseStatus.S404);
							response.setContent("Page not Found.");
						} else
							response.setContent(content);						
					}
				}
				out = new PrintWriter(socket.getOutputStream(), true);
				out.println(response);
				break;
			case POST:
				params = request.getResourceParameters();			
				UUID randomUuid = UUID.randomUUID();
				uuid = randomUuid.toString();
				//response.setContent(socket.getInetAddress().toString()+html);

				response = new HTTPResponse();
				response.setVersion(HTTPHeaders.HTTP_1_1.getHeader());
				if (!params.keySet().toArray()[0].equals("html")) {
				//if (!content.split("=")[0].equals("html")) {
					response.setStatus(HTTPResponseStatus.S400);
					response.setContent("Invalid content");
				}
				else {
					String type = request.getHeaderParameters().get("Content-Type");
					String content="";
					/*
					 * Cuando la petición HTTP contenga la cabecera Content-Type: application/x-www-form-urlencoded.
					 * el contenido deberá ser decodificado:
					 */
					if (type != null && type.startsWith("application/x-www-form-urlencoded")) {
						System.out.println(">> "+ params.get("html")) ;
						content = URLDecoder.decode(params.get("html"), "UTF-8");
					}else
						content = params.get("html");
					htmlManager.create(uuid, content);
					response.setStatus(HTTPResponseStatus.S200);
					response.setContent("<a href=\"html?uuid=" + uuid + "\">" + uuid + "</a>");
					//response.setContent("localhost/html?uuid="+stringUuid);
				}
				out = new PrintWriter(socket.getOutputStream(), true);
				out.println(response);
				break;
			case DELETE:
				System.out.println(">>");
				System.out.println(request.getResourceParameters());
				params = request.getResourceParameters();
				uuid= params.get("uuid");
				htmlManager.delete(uuid); 
				response = new HTTPResponse();
				response.setStatus(HTTPResponseStatus.S200);
				response.setContent("Pagina borrada");
				response.setVersion(HTTPHeaders.HTTP_1_1.getHeader());
				out = new PrintWriter(socket.getOutputStream(), true);
				out.println(response);
				break;
			default:
				break;
			}
		} catch (IOException | HTTPParseException e) {
			System.out.println("\t>> Error en Thread Client:\n" + e.getMessage());
		} 
		//System.out.println(">> ClientService " + Thread.currentThread().getName() + " sirvió la página...");
	}
}
