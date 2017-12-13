package es.uvigo.esei.dai.hybridserver;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface ServersDAO {

	@WebMethod
	public String getHTML();
	
	@WebMethod
	public String getXML();
	
	@WebMethod
	public String getXSD();
	
	@WebMethod
	public String getXSLT();
	
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
