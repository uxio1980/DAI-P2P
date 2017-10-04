package es.uvigo.esei.dai.hybridserver;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

import es.uvigo.esei.dai.hybridserver.http.HTTPHeaders;
import es.uvigo.esei.dai.hybridserver.http.HTTPParseException;
import es.uvigo.esei.dai.hybridserver.http.HTTPRequest;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponse;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponseStatus;

public class HybridServer {
	private static final int SERVICE_PORT = 8888;
	private Thread serverThread;
	private boolean stop;
	private Map<String, String> pages;

	public HybridServer() {
		// TODO Auto-generated constructor stub
	}

	public HybridServer(Map<String, String> pages) {
		// TODO Mapa con <UID, contenido(para poost)> Lo hace el test
		this.pages = pages;
	}

	public HybridServer(Properties properties) {
		// TODO Auto-generated constructor stub
	}

	public int getPort() {
		return SERVICE_PORT;
	}

	public void start() {
		this.serverThread = new Thread() {
			@Override
			public void run() {
				System.out.print("Inicializando servidor... ");
				ArrayList<Socket> arraySockets = new ArrayList<Socket>();
				Socket socket = null;
				try (final ServerSocket serverSocket = new ServerSocket(SERVICE_PORT)) {
					System.out.println("\t[OK]\nEsperando conexiones entrantes...");
					int idSession = 0;   
					while (true) {
						//try (Socket socket = serverSocket.accept()) {
						try  {
							socket = serverSocket.accept();
							System.out.println("Nueva conexión entrante: "+socket);
							arraySockets.add(socket);
							idSession++;
							if (stop) break;
							new Thread (new ClientService(socket)).start();
						} catch (Exception e) {
							System.out.println("Error en servidor:\n" + e.getMessage());
							System.exit(0);
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};

		this.stop = false;
		this.serverThread.start();
		//while (this.serverThread.isAlive());
	}

	public void stop() {
		this.stop = true;

		try (Socket socket = new Socket("localhost", SERVICE_PORT)) {
			// Esta conexiÃ³n se hace, simplemente, para "despertar" el hilo servidor
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		try {
			this.serverThread.join();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}

		this.serverThread = null;
	}
}
