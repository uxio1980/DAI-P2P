package es.uvigo.esei.dai.hybridserver;

public class Launcher {
	
	public static void main(String[] args) {

		// Implementación para el Map.
		HtmlDAO htmlDao = new HtmlDAOMap();
		
		// Implementación para la BD.
		
		
		// Se inicia el servidor.
		HybridServer server = new HybridServer();
		server.setHtmlDao(htmlDao);
		server.start();
	}
}
