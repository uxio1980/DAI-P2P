package es.uvigo.esei.dai.hybridserver;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface ServersDAO {

	@WebMethod
	public List<String> getHTML();
	
	@WebMethod
	public List<String> getXML();
	
	@WebMethod
	public List<String> getXSD();
	
	@WebMethod
	public List<String> getXSLT();
	
	@WebMethod
	public String htmlContent(String uuid);

	@WebMethod
	public String xmlContent(String uuid);
	
	@WebMethod
	public String xsdContent(String uuid);
	
	@WebMethod
	public String xsltContent(String uuid);
	
	@WebMethod
	public String getAssociatedXsd(String uuidXslt);
}
