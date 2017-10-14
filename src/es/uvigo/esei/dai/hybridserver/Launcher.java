package es.uvigo.esei.dai.hybridserver;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import es.uvigo.esei.dai.jdbc.connection.ConnectionConfiguration;
import es.uvigo.esei.dai.jdbc.connection.MySQLConnectionConfiguration;
import javafx.beans.property.SetProperty;

public class Launcher {
	

	private static String urlDb;
	private static String nameDb;
	private static int portDb;
	private static String userDb;
	private static String passwordDb;

	public static void main(String[] args) {

		Properties dataConfig = null;
		if (args.length == 1) 
			dataConfig = loadParameters(args[0]); 

		// Implementación para el Map.
		//HtmlDAO htmlDao = new HtmlDAOMap();
		
		// Implementación para la BD.
		setProperties(dataConfig);
		ConnectionConfiguration connectionConfiguration = new MySQLConnectionConfiguration(userDb,passwordDb,urlDb,nameDb,portDb);
		HtmlDAO htmlDao = new HtmlDAODB(dataConfig, connectionConfiguration);
		
		// Se inicia el servidor.
		HybridServer server = new HybridServer(dataConfig);
		server.setHtmlDao(htmlDao);
		server.start();
	}

	private static void setProperties(Properties properties) {
		Properties dataConfig= null; 
		if (properties!=null)
			dataConfig = properties;
		else {
			dataConfig = new Properties();
			System.out.println("Faltan Argumentos.. (Config.conf). Se cargarán los parámetros por defecto.");
		}
		//this.service_port = Integer.parseInt(dataConfig.getProperty("port","8888"));	
		//this.num_clients = Integer.parseInt(dataConfig.getProperty("numClients", "50"));
		//HtmlDAODB.database = dataConfig.getProperty("db.url", "jdbc:mysql://localhost:3306/hstestdb");
		userDb = dataConfig.getProperty("db.user", "hsdb");
		passwordDb = dataConfig.getProperty("db.password", "hsdbpass");
		String[] url = dataConfig.getProperty("db.url", "jdbc:mysql://localhost:3306/hstestdb").split("/");
		urlDb = url[2].split(":")[0];
		portDb = Integer.parseInt(url[2].split(":")[1]);
		nameDb = url[3];
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
