package es.uvigo.esei.dai.hybridserver;

/**
 * Interfaz que permite al servidor interactuar con la BD o un mapa de webs.
 *
 */
public interface HtmlDAO {
	
	/**
	 * Recupera una página web del servidor.
	 * @param uuid Identificador de la página web.
	 * @return Contenido de la página web.
	 */
	public String getHtmlPage(String uuid);
	
	/**
	 * Muestra una lista de links a todas las páginas web del servidor.
	 * @param service_port Puerto por el que escucha el servidor.
	 * @return String con una lista de páginas web.
	 */
	public String getHtmlList(int service_port);
	
	/**
	 * Inserta una nueva página web en el servidor.
	 * @param uuid Identificador de la página web.
	 * @param content Contenido de la página web.
	 */
	public void createHtmlPage(String uuid, String content);
	
	/**
	 * Elimina una página web del sevidor.
	 * @param uuid Identificador de la página web.
	 */
	public void deleteHtmlPage(String uuid);
	
	/**
	 * Comprueba si existe la página en el servidor.
	 * @param uuid Identificador de la página web.
	 */
	public boolean containsPage(String uuid);
}
