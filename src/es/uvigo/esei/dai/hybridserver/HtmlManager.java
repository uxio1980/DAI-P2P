package es.uvigo.esei.dai.hybridserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.print.attribute.standard.PagesPerMinute;

public class HtmlManager {

	private Map<String, String> pages;

	private final HtmlDAO htmlDao;

	public HtmlManager(HtmlDAO htmlDao) {
		// TODO Auto-generated constructor stub
		this.htmlDao = htmlDao;
	}
	
	// get(UUID) y list
	public String get(String uuid) {
		return this.htmlDao.getHtmlPage(uuid);
	}
	
	public ArrayList<String> getHtmlList(int service_port) {
		return new ArrayList<String>(this.htmlDao.getHtmlList(service_port));
	}

	public void create(String uuid, String content){
		this.htmlDao.createHtmlPage(uuid, content);
	}
	
	public void delete(String uuid) {
		this.htmlDao.deleteHtmlPage(uuid);
	}
	
}
