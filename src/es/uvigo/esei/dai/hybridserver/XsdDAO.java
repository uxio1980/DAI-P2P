package es.uvigo.esei.dai.hybridserver;

public interface XsdDAO {

	/**
	 * Recupera una página web Xsd del servidor.
	 * @param uuid Identificador de la página web.
	 * @return Contenido de la página web.
	 */
	public String getXsdPage(String uuid);
	
	/**
	 * Muestra una lista de links a todas las páginas web Xsd del servidor.
	 * @param service_port Puerto por el que escucha el servidor.
	 * @return String con una lista de páginas web.
	 */
	public String getXsdList();
	
	/**
	 * Inserta una nueva página web Xsd en el servidor.
	 * @param uuid Identificador de la página web.
	 * @param content Contenido de la página web.
	 */
	public void createXsdPage(String uuid, String content);
	
	/**
	 * Elimina una página web Xsd del sevidor.
	 * @param uuid Identificador de la página web.
	 */
	public void deleteXsdPage(String uuid);
	
	/**
	 * Comprueba si existe la página Xsd en el servidor.
	 * @param uuid Identificador de la página web.
	 */
	public boolean containsPage(String uuid);
}
