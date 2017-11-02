package es.uvigo.esei.dai.hybridserver;

public interface XsltDAO {

	/**
	 * Recupera una página web Xslt del servidor.
	 * @param uuid Identificador de la página web.
	 * @return Contenido de la página web.
	 */
	public String getXsltPage(String uuid);
	
	/**
	 * Muestra una lista de links a todas las páginas web Xslt del servidor.
	 * @param service_port Puerto por el que escucha el servidor.
	 * @return String con una lista de páginas web.
	 */
	public String getXsltList();
	
	/**
	 * Inserta una nueva página web Xslt en el servidor.
	 * @param uuid Identificador de la página web.
	 * @param content Contenido de la página web.
	 */
	public void createXsltPage(String uuid, String xsd, String content);
	
	/**
	 * Elimina una página web Xslt del sevidor.
	 * @param uuid Identificador de la página web.
	 */
	public void deleteXsltPage(String uuid);
	
	/**
	 * Comprueba si existe la página Xslt en el servidor.
	 * @param uuid Identificador de la página web.
	 */
	public boolean containsPage(String uuid);
}
