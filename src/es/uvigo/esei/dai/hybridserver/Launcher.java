package es.uvigo.esei.dai.hybridserver;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Launcher {

	public static void main(String[] args) {

		Properties dataConfig = null;
		if (args.length == 1) 
			dataConfig = loadParameters(args[0]); 

		// Implementación para el Map.
		//HtmlDAO htmlDao = new HtmlDAOMap();

		// Implementación para la BD.
		HtmlDAO htmlDao = new HtmlDAODB(dataConfig);
		
		// Se inicia el servidor.
		HybridServer server = new HybridServer(dataConfig);
		server.setHtmlDao(htmlDao);
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
 
	private static void  createFileParameters() {
		Properties prop = new Properties();
		OutputStream output = null;

		try {

			output = new FileOutputStream("config.conf");

			// set the properties value

			prop.setProperty("port", "8888");
			prop.setProperty("numClients", "50");
			prop.setProperty("db.url", "jdbc:mysql://localhost:3306/hstestdb");
			prop.setProperty("db.user", "hsdb");
			prop.setProperty("db.password", "hsdbpass");

			// save properties to project root folder
			prop.store(output, null);

		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
