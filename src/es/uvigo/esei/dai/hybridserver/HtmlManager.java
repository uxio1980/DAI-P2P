package es.uvigo.esei.dai.hybridserver;

public class HtmlManager {

	private final HtmlDAO htmlDao;

	/**
	 * Constructor que recibe una instancia de la interfaz HtmlDAO.
	 * @param htmlDao Interfaz que permite interactuar con las webs almacenadas.
	 */
	public HtmlManager(HtmlDAO htmlDao) {
		this.htmlDao = htmlDao;
	}
	
	/**
	 * Recupera una página web del servidor.
	 * @param uuid Identificador de la página web.
	 * @return Contenido de la página web.
	 */
	public String get(String uuid) {
		return this.htmlDao.getHtmlPage(uuid);
	}
	
	/**
	 * Muestra una lista de links a todas las páginas web del servidor.
	 * @param service_port Puerto por el que escucha el servidor.
	 * @return String con una lista de páginas web.
	 */
	public String getHtmlList(int service_port) {
		return this.htmlDao.getHtmlList(service_port);
	}

	/**
	 * Inserta una nueva página web en el servidor.
	 * @param uuid Identificador de la página web.
	 * @param content Contenido de la página web.
	 */
	public void create(String uuid, String content){
		this.htmlDao.createHtmlPage(uuid, content);
	}
	
	/**
	 * Elimina una página web del sevidor.
	 * @param uuid Identificador de la página web.
	 */
	public void delete(String uuid) {
		this.htmlDao.deleteHtmlPage(uuid);
	}
	
	/**
	 * Comprueba si existe la página en el servidor.
	 * @param uuid Identificador de la página web.
	 */
	public boolean contains(String uuid) {
		return htmlDao.containsPage(uuid);
	}
}
