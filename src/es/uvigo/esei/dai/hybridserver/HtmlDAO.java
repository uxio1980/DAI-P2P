package es.uvigo.esei.dai.hybridserver;

import java.util.Map;

public interface HtmlDAO {

	// INTERFAZ con get para que Html manager acceda con el mismo get(uuid) al mapa o a BD.
	
	public Map<String, String> getHtmlPage();
	public Map<String, String> getHtmlList();
}
