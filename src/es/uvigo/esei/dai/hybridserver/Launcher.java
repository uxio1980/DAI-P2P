package es.uvigo.esei.dai.hybridserver;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Launcher {

	public static void main(String[] args) {

		Properties dataConfig = null;
		if (args.length == 1) 
			dataConfig = loadParameters(args[0]); 
	
		// Se inicia el servidor.
		HybridServer server = new HybridServer(dataConfig);
		server.start();
	}

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
