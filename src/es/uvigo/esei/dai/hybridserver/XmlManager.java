package es.uvigo.esei.dai.hybridserver;

import java.io.IOException;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.UUID;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.xml.sax.SAXException;
import es.uvigo.esei.dai.hybridserver.http.HTTPRequest;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponseStatus;
import es.uvigo.esei.dai.hybridserver.http.MIME;
import es.uvigo.esei.dai.xml.dom.DOMParsing;
import es.uvigo.esei.dai.xml.xslt.XSLTUtils;

public class XmlManager {

	private XmlDAO xmlDao;
	private Map<String,String> params;
	private String uuid;
	private HTTPResponseStatus status;
	private String content;
	private String type;

	/**
	 * Constructor que recibe una instancia de la interfaz XmlDAO.
	 * @param xmlDao Interfaz que permite interactuar con las webs almacenadas.
	 */
	public XmlManager(XmlDAO xmlDao) {
		this.xmlDao = xmlDao;
	}
	
	/**
	 * Recibe una petición GET y genera una respuesta que puede ser:
	 * Lista de todas las páginas, una sola página o un error 404.
	 * @param request Petición HTTP.
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 * @throws TransformerException 
	 */
	public void methodGet(HTTPRequest request, int port, Map<String, ServersDAO> servers) 
			throws IOException, TransformerException{
		params = request.getResourceParameters();
		uuid = params.get("uuid");
		String xslt = params.get("xslt");
		
		// Comprueba si se recibe el parámetro uuid.
		if(uuid == null) { 
			status = HTTPResponseStatus.S200;
			//content = xmlDao.getXmlList(); // Recupera una lista de páginas.
			type = MIME.TEXT_HTML.getMime();
			StringBuilder sc = new StringBuilder();
			sc.append("<h1>Local Server</h1>");
			sc.append(xmlDao.getXmlList()); // Recupera una lista de páginas.
			if(!servers.isEmpty()){
				for (Map.Entry<String, ServersDAO> server: servers.entrySet()) {
					sc.append("<h1>" + server.getKey()+"</h1>");
					sc.append(server.getValue().getXML());
				}
			}
			content = sc.toString();
			if (content.isEmpty()) {
				status = HTTPResponseStatus.S404;	
			}
		}
		else {
			// Comprueba si existe la página en local y si tiene plantilla.
			if (xmlDao.containsPage(uuid) && xmlDao.containsTemplate(xslt)) {
				String xsd = xmlDao.getXmlSchema(xslt);
				try{
					content = applyXslt(xslt, xsd, port);
					status = HTTPResponseStatus.S200;
					type = MIME.TEXT_HTML.getMime();
				} catch(Exception e){
					status = HTTPResponseStatus.S400;	
					type = MIME.TEXT_HTML.getMime();
				}
			}
			// XML en local y plantilla XSLT en remoto.
			else if (xmlDao.containsPage(uuid) && !xmlDao.containsTemplate(xslt)) {
				if(!servers.isEmpty()){
					String xsd = null;
					for (Map.Entry<String, ServersDAO> server: servers.entrySet()) {
						xsd = server.getValue().getAssociatedXsd(xslt);
						if (xsd != null)
							break;
					}
					if(xsd != null){
						type = MIME.TEXT_HTML.getMime();
						try{
							content = applyXslt(xslt, xsd, port);
							System.out.println(">>>"+content);
							status = HTTPResponseStatus.S200;
						} catch(Exception e){
							e.printStackTrace();
							status = HTTPResponseStatus.S400;	
						}
					}else{
						status = HTTPResponseStatus.S200;
						content = xmlDao.getXmlPage(uuid);
						type = MIME.APPLICATION_XML.getMime();
					}
				}
			}
			// XML y plantilla en remoto.
			else  {
				status = HTTPResponseStatus.S200;
				type = MIME.APPLICATION_XML.getMime();
				if(!servers.isEmpty()){
					for (Map.Entry<String, ServersDAO> server: servers.entrySet()) {
						content = server.getValue().xmlContent(uuid);
						if (content != null)
							break;
					}
				}
				// No existe la página.
				if (content == null || content.isEmpty()){
					status = HTTPResponseStatus.S404;	
					type = MIME.APPLICATION_XML.getMime();
				}
			}
		}
	}
	
	private String applyXslt(String xslt, String xsd, int port) 
			throws TransformerException, ParserConfigurationException, SAXException, IOException{
		URL urlXml = new URL ("http","localhost",port,"/xml?uuid=" + uuid);					
		URL urlXslt = new URL ("http","localhost",port,"/xslt?uuid=" + xslt);
		URL urlXsd = new URL ("http","localhost",port,"/xsd?uuid="+xsd);
		System.out.println(">>"+urlXml);
		System.out.println(">>"+urlXslt);
		System.out.println(">>"+urlXsd);
		DOMParsing.loadAndValidateWithExternalURL(
			urlXml.toString(), 
			urlXsd.toString());
		StringWriter writer = new StringWriter();
		XSLTUtils.transform(
				new StreamSource(urlXml.toString()),
				new StreamSource(urlXslt.toString()),
				new StreamResult(writer));
		return writer.toString();
	}

	/**
	 * Recibe una petición POST y genera una respuesta positiva o un error 400.
	 * @param request Petición HTTP.
	 */
	public void methodPost(HTTPRequest request){	
		params = request.getResourceParameters();				
		UUID randomUuid = UUID.randomUUID();
		uuid = randomUuid.toString();

		// Comprueba si el parámetro del formulario se llama xml.
		if(params.containsKey("xml")){
			xmlDao.createXmlPage(uuid, params.get("xml")); // Crea la página.
			status = HTTPResponseStatus.S200;
			content = "<a href=\"xml?uuid="+ uuid +"\">"+ uuid +"</a>";
			type = MIME.APPLICATION_XML.getMime();
		} else{
			status = HTTPResponseStatus.S400;	
			type = MIME.APPLICATION_XML.getMime();
		}
	}

	/**
	 * Recibe una petición DELETE y genera una respuesta positiva o un error 404.
	 * @param request Petición HTTP.
	 */
	public void methodDelete(HTTPRequest request){
		params = request.getResourceParameters();
		uuid = params.get("uuid");

		// Comprueba si existe la página en el servidor.
		if(xmlDao.containsPage(uuid)){
			xmlDao.deleteXmlPage(uuid); // Borra la página.
			status = HTTPResponseStatus.S200;
			type = MIME.APPLICATION_XML.getMime();
		} else{
			status = HTTPResponseStatus.S404;
			type = MIME.APPLICATION_XML.getMime();
		}
	}
	
	public HTTPResponseStatus getStatus() {
		return status;
	}

	public String getContent() {
		return content;
	}

	public String getType() {
		return type;
	}
}
