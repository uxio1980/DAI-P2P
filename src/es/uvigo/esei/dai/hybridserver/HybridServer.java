package es.uvigo.esei.dai.hybridserver;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import es.uvigo.esei.dai.hybridserver.http.HTTPHeaders;
import es.uvigo.esei.dai.hybridserver.http.HTTPParseException;
import es.uvigo.esei.dai.hybridserver.http.HTTPRequest;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponse;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponseStatus;

public class HybridServer {
	private static int service_port;
	private static int num_clients;
	//private static String database;
	//private static String userDb;
	//private static String passwordDb;
	
	private Thread serverThread;
	private boolean stop;
	private HtmlDAO htmlDao;

	/**
	 * Constructor vacío.
	 */
	public HybridServer() {
		this.setProperties(null);
	}

	/**
	 * Constructor que recibe un mapa de webs.
	 * @param pages Mapa de páginas web <uuid,contenido>.
	 */
	public HybridServer(Map<String, String> pages) {
		// TODO Mapa con <UID, contenido(para poost)> Lo hace el test
		this.setProperties(null);
		this.htmlDao = new HtmlDAOMap(pages);
	}

	/**
	 * Constructor que recibe propiedades de configuración.
	 * @param properties Propiedades de configuración de la BD.
	 */
	public HybridServer(Properties properties) {
		this.setProperties(properties);
	}

	/**
	 * Devuelve el puerto de escucha del servidor.
	 * @return Puerto del socket.
	 */
	public int getPort() {
		return service_port;
	}
	
	/**
	 * Establece la interfaz HtmlDAO.
	 * @param htmlDao Instancia de la interfaz HtmlDAO.
	 */
	public void setHtmlDao(HtmlDAO htmlDao) {
		this.htmlDao = htmlDao;
	}

	/**
	 * Crea un hilo de conexión por cada cliente conectado hasta un máximo de 50.
	 */
	public void start() {
		this.serverThread = new Thread() {
			@Override
			public void run() {
				System.out.print("Inicializando servidor... ");
				Socket socket = null;
				try (final ServerSocket serverSocket = new ServerSocket(service_port)) {
						
					System.out.println("\t[OK]\nEsperando conexiones entrantes...");
					ExecutorService executor = Executors.newFixedThreadPool(num_clients);
					while (true) {
						try  {
							// Acepta clientes y crea un hilo para cada uno.
							socket = serverSocket.accept();
							if (stop) break;
							System.out.println("Nueva conexión entrante: "+socket);	
							executor.execute(new ClientService(socket, htmlDao));
						} 
						catch (Exception e) {
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

	/**
	 * Detiene el servidor.
	 */
	public void stop() {
		this.stop = true;

		try (Socket socket = new Socket("localhost", service_port)) {
			// Esta conexión se hace, simplemente, para "despertar" al hilo servidor.
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
	
	private void setProperties(Properties properties) {
		Properties dataConfig= null; 
		if (properties!=null)
			dataConfig = properties;
		else {
			dataConfig = new Properties();
			System.out.println("Faltan Argumentos.. (Config.conf). Se cargarán los parámetros por defecto.");
		}
		this.service_port = Integer.parseInt(dataConfig.getProperty("port","8888"));	
		this.num_clients = Integer.parseInt(dataConfig.getProperty("numClients", "50"));
		//this.database = dataConfig.getProperty("db.url", "jdbc:mysql://localhost:3306/hstestdb");
		//this.userDb = dataConfig.getProperty("db.user", "hsdb");
		//this.passwordDb = dataConfig.getProperty("db.password", "hsdbpass");
	}
}
