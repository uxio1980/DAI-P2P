package es.uvigo.esei.dai.hybridserver;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

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

			Map<String,String> params = request.getResourceParameters();
			String uuid = params.get("uuid");

			HTTPResponse response = new HTTPResponse();
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
				String content = htmlManager.get(uuid);	
				if (content == null) {
					response.setStatus(HTTPResponseStatus.S404);
					response.setContent("Page not Found.");
				} else
					response.setContent(content);
			}
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			out.println(response);
			//socket.getOutputStream();
			//socket.getOutputStream().flush();
		} catch (IOException | HTTPParseException e) {
			System.out.println("\t>> Error en Thread Client:\n" + e.getMessage());
		} 
		//System.out.println(">> ClientService " + Thread.currentThread().getName() + " sirvió la página...");
	}
}
