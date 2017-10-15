package es.uvigo.esei.dai.hybridserver;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import es.uvigo.esei.dai.jdbc.connection.ConnectionConfiguration;
import es.uvigo.esei.dai.jdbc.connection.ConnectionUtils;

public class HtmlDAODB implements HtmlDAO {

	private static Connection connection;
	
	/**
	 * Constructor que crea una conexión con la base de datos.
	 */
	public HtmlDAODB(ConnectionConfiguration connectionConfiguration){
		try {
			connection = ConnectionUtils.getConnection(connectionConfiguration);
		} catch (IllegalArgumentException | SQLException e) {
			System.out.println("Error en HtmlDaoDB:\n\t" + e.getMessage());
		} 
	}

	@Override
	public String getHtmlPage(String uuid) {
		try (PreparedStatement statement = connection.prepareStatement(
				"SELECT * FROM HTML WHERE uuid=?")) {
			statement.setString(1, uuid);
			ResultSet result = statement.executeQuery();

			if (result.next()) {
				return result.getString("content");
			} else
				return null;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String getHtmlList(int service_port) {
		try (PreparedStatement statement = connection.prepareStatement(
				"SELECT * FROM HTML")) {
			ResultSet result = statement.executeQuery();
			StringBuilder sb = new StringBuilder();
			String uuid;

			while(result.next()) {
				uuid = result.getString("uuid");
				sb.append("<a href='localhost:"+ service_port +"/html?uuid="+ uuid +"' target='_blank'>"+ uuid +"</a><br />");
			}
			return sb.toString();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void createHtmlPage(String uuid, String content) {
		try (PreparedStatement statement = connection.prepareStatement(
				"INSERT INTO HTML (id,uuid, content) " + 
				"VALUES (0, ?, ?)")) {
			statement.setString(1, uuid);
			statement.setString(2, content);
			int rows = statement.executeUpdate();
			if (rows != 1) {
				throw new RuntimeException("Error insertando página");
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}	
	}

	@Override
	public void deleteHtmlPage(String uuid) {
		try (PreparedStatement statement = connection.prepareStatement(
				"DELETE FROM HTML " + "WHERE uuid=?")) {
			statement.setString(1, uuid);
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}	
	}
	
	@Override
	public boolean containsPage(String uuid) {
		try (PreparedStatement statement = connection.prepareStatement(
				"SELECT * FROM HTML WHERE uuid=?")) {
			statement.setString(1, uuid);
			ResultSet result = statement.executeQuery();

			if (result.first()) {
				return true;
			} else
				return false;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}