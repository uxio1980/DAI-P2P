package es.uvigo.esei.dai.hybridserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Launcher {

	public static void main(String[] args) throws Exception {

		//XMLConfigurationLoader cl = new XMLConfigurationLoader();
		//cl.load(new File(args[0]));
		
		// Invoca un contructor distinto en función de si recibe o no parámetro.
		// Si recibe más de 1 devuelve un error.
		if (args.length == 0 ){
			HybridServer server = new HybridServer();
			server.start();
		}else if (args.length == 1 ){
			//Properties dataConfig = loadParameters(args[0]); 
			XMLConfigurationLoader cl = new XMLConfigurationLoader();
			Configuration c = cl.load(new File(args[0]));
			// Se inicia el servidor.
			HybridServer server = new HybridServer(c);
			server.start();
		}
		else if (args.length == 4 ){
			XMLConfigurationLoader cl = new XMLConfigurationLoader();
			Configuration c1 = cl.load(new File(args[0]));
			Configuration c2 = cl.load(new File(args[1]));
			Configuration c3 = cl.load(new File(args[2]));
			Configuration c4 = cl.load(new File(args[3]));
			// Se inicia el servidor.
			HybridServer server1 = new HybridServer(c1);
			HybridServer server2 = new HybridServer(c2);
			HybridServer server3 = new HybridServer(c3);
			HybridServer server4 = new HybridServer(c4);
			server1.start();
			server2.start();
			server3.start();
			server4.start();
		}
		else
			System.out.println("ERROR: demasiados parámetros.");
		
	}

	/**
	 * Recupera el fichero del string recibido por parámetro.
	 * @param fileName Nombre del fichero.
	 * @return Objeto Properties con las propiedades del fichero.
	 */
	private static Properties loadParameters(String fileName) {

		Properties dataConfig = new Properties();
		try (InputStream input = new FileInputStream(fileName)) {
			// load a properties file
			dataConfig.load(input);
		} catch (IOException ex) {
			System.out.println("Error cargando parametros:\n\t" + ex.getMessage());
		}
		
		return dataConfig;
	}
	
}
