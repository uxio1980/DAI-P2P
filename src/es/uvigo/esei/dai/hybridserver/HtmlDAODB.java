package es.uvigo.esei.dai.hybridserver;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.Properties;

public class HtmlDAODB implements HtmlDAO {

	private static String userDb;
	private static String passwordDb;
	private static String url;
	
	/**
	 * Constructor que almacena los parámetros de la conexión en variables.
	 */
	public HtmlDAODB(Properties properties){
		try {
			userDb = properties.getProperty("db.user", "hsdb");
			passwordDb = properties.getProperty("db.password", "hsdbpass");
			url = properties.getProperty("db.url", "jdbc:mysql://localhost:3306/hstestdb");
		} catch (Exception e) {
			System.out.println("Error en HtmlDaoDB:\n\t" + e.getMessage());
		} 
	}

	@Override
	public String getHtmlPage(String uuid) {
		try (Connection connection = DriverManager.getConnection(url, userDb, passwordDb);
				PreparedStatement statement = connection.prepareStatement(
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
	public String getHtmlList() {
		try (Connection connection = DriverManager.getConnection(url, userDb, passwordDb);
				PreparedStatement statement = connection.prepareStatement(
				"SELECT * FROM HTML")) {
			ResultSet result = statement.executeQuery();
			StringBuilder sb = new StringBuilder();
			String uuid;

			while(result.next()) {
				uuid = result.getString("uuid");
				sb.append("<a href='/html?uuid="+ uuid +"' target='_blank'>"+ uuid +"</a><br/>");
			}
			return sb.toString();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void createHtmlPage(String uuid, String content) {
		try (Connection connection = DriverManager.getConnection(url, userDb, passwordDb);
				PreparedStatement statement = connection.prepareStatement(
				"INSERT INTO HTML (uuid, content) " + 
				"VALUES (?, ?)")) {
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
		try (Connection connection = DriverManager.getConnection(url, userDb, passwordDb);
				PreparedStatement statement = connection.prepareStatement(
				"DELETE FROM HTML " + "WHERE uuid=?")) {
			statement.setString(1, uuid);
			int rows = statement.executeUpdate();
			if (rows != 1) {
				throw new RuntimeException("Error insertando página");
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}	
	}
	
	@Override
	public boolean containsPage(String uuid) {
		try (Connection connection = DriverManager.getConnection(url, userDb, passwordDb);
				PreparedStatement statement = connection.prepareStatement(
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