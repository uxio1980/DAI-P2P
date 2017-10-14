package es.uvigo.esei.dai.hybridserver;

import java.sql.SQLException;
import java.util.Properties;
import java.sql.Connection;

import es.uvigo.esei.dai.jdbc.connection.ConnectionConfiguration;
import es.uvigo.esei.dai.jdbc.connection.ConnectionUtils;
import es.uvigo.esei.dai.jdbc.connection.MySQLConnectionConfiguration;

public class HtmlDAODB implements HtmlDAO {

	private Properties dataConfig;
	private static Connection connection;
	
	/**
	 * Constructor que crea una conexión con la base de datos.
	 */
	public HtmlDAODB(Properties dataCongig, ConnectionConfiguration connectionConfiguration){
		
		this.dataConfig = dataConfig;
		try {
			connection = ConnectionUtils.getConnection(connectionConfiguration);
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