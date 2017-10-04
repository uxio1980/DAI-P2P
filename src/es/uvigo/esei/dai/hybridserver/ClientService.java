package es.uvigo.esei.dai.hybridserver;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import es.uvigo.esei.dai.hybridserver.http.HTTPHeaders;
import es.uvigo.esei.dai.hybridserver.http.HTTPParseException;
import es.uvigo.esei.dai.hybridserver.http.HTTPRequest;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponse;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponseStatus;

public class ClientService implements Runnable {

	private Socket socket;

	public ClientService(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			//while(true) {
				HTTPRequest request = new HTTPRequest(
						new InputStreamReader(socket.getInputStream()));
				HTTPResponse response = new HTTPResponse();
				response.setVersion(HTTPHeaders.HTTP_1_1.getHeader());
				response.setStatus(HTTPResponseStatus.S200);
				response.setContent("Hybrid Server " + Thread.currentThread().getName());

				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				out.println(response);
				//socket.getOutputStream();
				socket.getOutputStream().flush();
			//}
		} catch (IOException | HTTPParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(">> ClientService " + Thread.currentThread().getName() + " sirvió la página...");
	}

}
