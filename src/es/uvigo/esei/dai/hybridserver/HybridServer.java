package es.uvigo.esei.dai.hybridserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.xml.ws.Endpoint;

import com.sun.org.apache.bcel.internal.generic.NEW;

public class HybridServer {
	private int service_port;
	private int num_clients;
	private Thread serverThread;
	private boolean stop;
	private ExecutorService executor;
	private Properties properties;
	private Configuration config;
	private Endpoint ep;

	/**
	 * Constructor vacío.
	 */
	public HybridServer() {
		service_port = 8888;	
		num_clients = 50;
		properties = new Properties();
	}
	
	/**
	 * Constructor que recibe un archivo de configuración.
	 */
	public HybridServer(Configuration config) {
		service_port = config.getHttpPort();	
		num_clients = config.getNumClients();
		this.config = config;
	}

	/**
	 * Constructor que recibe propiedades de configuración.
	 * @param properties Propiedades de configuración de la BD.
	 */
	public HybridServer(Properties properties) {
		service_port = Integer.parseInt(properties.getProperty("port","8888"));	
		num_clients = Integer.parseInt(properties.getProperty("numClients", "50"));
		this.properties = properties;
	}
	
	/**
	 * Constructor que recibe un mapa de webs.
	 * @param pages Mapa de páginas web <uuid,contenido>.
	 */
	public HybridServer(Map<String, String> pages) {
		//this.htmlDao = new HtmlDAOMap(pages);
	}

	/**
	 * Devuelve el puerto de escucha del servidor.
	 * @return Puerto del socket.
	 */
	public int getPort() {
		return service_port;
	}

	/**
	 * Crea un hilo de conexión por cada cliente conectado hasta un máximo de 50.
	 */
	public void start() {
		this.serverThread = new Thread() {
			@Override
			public void run() {
				System.out.print("Publicando servicios web... ");
				if(config != null)
					ep = Endpoint.publish(config.getWebServiceURL(), new ServersDAODB(config));
				System.out.println("\t[OK]");
				System.out.print("Inicializando servidor... ");
				Socket socket = null;
				try (final ServerSocket serverSocket = new ServerSocket(service_port)) {
						
					System.out.println("\t[OK]\nEsperando conexiones entrantes...");
					 executor = Executors.newFixedThreadPool(num_clients);
					while (true) {
						try  {
							// Acepta clientes y crea un hilo para cada uno.
							socket = serverSocket.accept();
							if (stop) break;
							if(config != null)
								executor.execute(new ClientService(socket, config));
							else
								executor.execute(new ClientService(socket, properties));
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
		executor.shutdownNow();
		if(config != null)
			ep.stop();
		try {
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		this.serverThread = null;
	}
}
