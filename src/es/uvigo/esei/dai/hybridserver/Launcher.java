package es.uvigo.esei.dai.hybridserver;

public class Launcher {
	
	public static void main(String[] args) {

		//Instanciación de las Implementaciones
		HtmlDAO htmlDao = new HtmlDAOMap();
		HybridServer server = new HybridServer();
		server.setHtmlDao(htmlDao);
		server.start();
	}
}
