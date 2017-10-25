package es.uvigo.esei.dai.hybridserver;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Launcher {

	public static void main(String[] args) {

		// Invoca un contructor distinto en función de si recibe o no parámetro.
		// Si recibe más de 1 devuelve un error.
		if (args.length == 0 ){
			HybridServer server = new HybridServer();
			server.start();
		}else if (args.length == 1 ){
			Properties dataConfig = loadParameters(args[0]); 

			// Se inicia el servidor.
			HybridServer server = new HybridServer(dataConfig);
			server.start();
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
