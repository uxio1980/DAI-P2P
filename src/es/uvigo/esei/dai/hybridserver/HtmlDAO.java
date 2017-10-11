package es.uvigo.esei.dai.hybridserver;

import java.util.ArrayList;
import java.util.Map;

public interface HtmlDAO {

	// INTERFAZ con get para que Html manager acceda con el mismo get(uuid) al mapa o a BD.
	
	public String getHtmlPage(String uuid);
	public ArrayList<String> getHtmlList(int service_port);
	public void createHtmlPage(String uuid, String content);
	public void deleteHtmlPage(String uuid);
}
