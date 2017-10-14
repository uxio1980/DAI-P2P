package es.uvigo.esei.dai.jdbc.connection;

import java.util.Properties;

public interface ConnectionConfiguration {
	public String getConnectionString();
	public Properties getConnectionProperties();
}
