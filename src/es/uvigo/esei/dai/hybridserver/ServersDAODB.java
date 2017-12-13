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
public class ServersDAODB implements ServersDAO {

	private String userDb;
	private String passwordDb;
	private String url;
	
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
	public String getHTML() {
		try (Connection connection = DriverManager.getConnection(url, userDb, passwordDb);
				PreparedStatement statement = connection.prepareStatement(
				"SELECT * FROM HTML")) {
			ResultSet result = statement.executeQuery();
			String uuid;
			StringBuilder sb = new StringBuilder();
			sb.append("<ul>");
			while(result.next()) {
				uuid = result.getString("uuid");
				sb.append("<li><a href='/html?uuid="+ uuid +"' target='_blank'>"+ uuid +"</a></li>");
			}
			sb.append("</ul>");
			return sb.toString();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}	
	}

	@Override
	public String getXML() {
		try (Connection connection = DriverManager.getConnection(url, userDb, passwordDb);
				PreparedStatement statement = connection.prepareStatement(
				"SELECT * FROM XML")) {
			ResultSet result = statement.executeQuery();
			String uuid;
			StringBuilder sb = new StringBuilder();
			sb.append("<ul>");
			while(result.next()) {
				uuid = result.getString("uuid");
				sb.append("<li><a href='/xml?uuid="+ uuid +"' target='_blank'>"+ uuid +"</a></li>");
			}
			sb.append("</ul>");
			return sb.toString();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}	
	}

	@Override
	public String getXSD() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getXSLT() {
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
		}	
	}

	@Override
	public String xmlContent(String uuid) {
		try (Connection connection = DriverManager.getConnection(url, userDb, passwordDb);
				PreparedStatement statement = connection.prepareStatement(
				"SELECT * FROM XML WHERE uuid=?")) {
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