package es.uvigo.esei.dai.hybridserver;

public interface XmlDAO {

	/**
	 * Recupera una página web XML del servidor.
	 * @param uuid Identificador de la página web.
	 * @return Contenido de la página web.
	 */
	public String getXmlPage(String uuid);
	
	/**
	 * Muestra una lista de links a todas las páginas web XML del servidor.
	 * @param service_port Puerto por el que escucha el servidor.
	 * @return String con una lista de páginas web.
	 */
	public String getXmlList();
	
	/**
	 * Inserta una nueva página web XML en el servidor.
	 * @param uuid Identificador de la página web.
	 * @param content Contenido de la página web.
	 */
	public void createXmlPage(String uuid, String content);
	
	/**
	 * Elimina una página web XML del sevidor.
	 * @param uuid Identificador de la página web.
	 */
	public void deleteXmlPage(String uuid);
	
	/**
	 * Comprueba si existe la página XML en el servidor.
	 * @param uuid Identificador de la página web.
	 */
	public boolean containsPage(String uuid);
}
