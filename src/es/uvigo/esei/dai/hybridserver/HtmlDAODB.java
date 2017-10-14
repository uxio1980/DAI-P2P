package es.uvigo.esei.dai.hybridserver;

import java.sql.SQLException;
import java.util.Properties;
import java.sql.Connection;

import es.uvigo.esei.dai.jdbc.connection.ConnectionUtils;
import es.uvigo.esei.dai.jdbc.connection.MySQLConnectionConfiguration;

public class HtmlDAODB implements HtmlDAO {

	private static String urlDb;
	private static String nameDb;
	private static int portDb;
	private static String userDb;
	private static String passwordDb;
	private static Connection connection;
	
	/**
	 * Constructor que crea una conexión con la base de datos.
	 */
	public HtmlDAODB(Properties dataCongig){
		
		setProperties(dataCongig);
		try {
			connection = ConnectionUtils.getConnection(new MySQLConnectionConfiguration(userDb,passwordDb,urlDb,nameDb,portDb));
		} catch (IllegalArgumentException | SQLException e) {
			//e.printStackTrace();
			System.out.println("Error:\n\t" + e.getMessage());
		} 
	}

	@Override
	public String getHtmlPage(String uuid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getHtmlList(int service_port) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createHtmlPage(String uuid, String content) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteHtmlPage(String uuid) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean containsPage(String uuid) {
		return false;
	}

	private void setProperties(Properties properties) {
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
		HtmlDAODB.userDb = dataConfig.getProperty("db.user", "hsdb");
		HtmlDAODB.passwordDb = dataConfig.getProperty("db.password", "hsdbpass");
		String[] url = dataConfig.getProperty("db.url", "jdbc:mysql://localhost:3306/hstestdb").split("/");
		HtmlDAODB.urlDb = url[2].split(":")[0];
		HtmlDAODB.portDb = Integer.parseInt(url[2].split(":")[1]);
		HtmlDAODB.nameDb = url[3];
	}
}


/*
try (Statement statement = c.createStatement()) {

// 3. EjecuciÃ³n de la consulta 
//try (ResultSet result = statement.executeQuery(
//	"SELECT * FROM pages " +
//	"WHERE nombre LIKE 'A%'"
try (ResultSet result = statement.executeQuery(
		"SELECT * FROM pages "
)) {
	// 4. VisualizaciÃ³n de los resultados
	while (result.next()) {
		System.out.printf("Id: %d, uuid: %s, page: %s\n",
			result.getInt(1),
			result.getString(2),
			result.getString(3)
		);
	}
}
} catch (SQLException e) {
// TODO Auto-generated catch block
e.printStackTrace();
}
*/