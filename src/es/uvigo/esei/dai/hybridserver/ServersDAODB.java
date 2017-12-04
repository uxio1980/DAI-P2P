package es.uvigo.esei.dai.hybridserver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.jws.WebService;

@WebService(endpointInterface = "es.uvigo.esei.dai.hybridserver.ServersDAO")
public class ServersDAODB implements ServersDAO{

	private static String userDb;
	private static String passwordDb;
	private static String url;
	
	public ServersDAODB(Configuration config) {
		try {
			userDb = config.getDbUser();
			passwordDb = config.getDbPassword();
			url = config.getDbURL();
		} catch (Exception e) {
			System.out.println("Error en ServersDaoDB:\n\t" + e.getMessage());
		} 
	}
	
	@Override
	public List<String> getHTML() {
		try (Connection connection = DriverManager.getConnection(url, userDb, passwordDb);
				PreparedStatement statement = connection.prepareStatement(
				"SELECT * FROM HTML")) {
			ResultSet result = statement.executeQuery();
			List<String> list = new ArrayList<>();
			String uuid;

			while(result.next()) {
				uuid = result.getString("uuid");
				list.add(uuid);
			}
			return list;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}	
	}

	@Override
	public List<String> getXML() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getXSD() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getXSLT() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String htmlContent(String uuid) {
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
		}	}

	@Override
	public String xmlContent(String uuid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String xsdContent(String uuid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String xsltContent(String uuid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAssociatedXsd(String uuidXslt) {
		// TODO Auto-generated method stub
		return null;
	}

}