package es.uvigo.esei.dai.hybridserver;

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
}
