package es.uvigo.esei.dai.hybridserver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.util.Map;
import java.util.UUID;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;
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
		
		// No recibe uuid ni xslt:
		if(uuid == null && xslt == null) { 
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
			if(content == null) {
				status = HTTPResponseStatus.S404;	
			}else {
				status = HTTPResponseStatus.S200;
			}
		}
		// Recibe uuid pero no xslt:
		else if(uuid != null && xslt == null) {
			type = MIME.APPLICATION_XML.getMime();
			findXml(servers);
		// Recibe uuid y xslt:
		}else {
			type = MIME.TEXT_HTML.getMime();
			findXslt(servers,xslt,port);
		}
	}
	
	private void findXml(Map<String, ServersDAO> servers){
		status = HTTPResponseStatus.S200;
		// Comprueba si existe la página en el servidor.
		if (xmlDao.containsPage(uuid)) {	
			content = xmlDao.getXmlPage(uuid);		
		} else {
			// Busca la página en los servidores.
			if(!servers.isEmpty()){
				for (Map.Entry<String, ServersDAO> server: servers.entrySet()) {
					content = server.getValue().xmlContent(uuid);
					if (content != null)
						break;
				}
			}
			if (content == null) {
				status = HTTPResponseStatus.S404;	
			}	
		}
	}
	
	private void findXslt(Map<String,ServersDAO> servers, String xslt,int port){
		// Busca el XML en todos los servidores.
		findXml(servers);
		// Si lo encuentra busca el XSLT asociado.
		if(content != null) {
			String xsd = xmlDao.getXmlSchema(xslt);
			// Si la plantilla está en local:
			if(xsd != null){
				try{		
					content = applyXslt(content,xslt,xsd,port);
					status = HTTPResponseStatus.S200;
				} catch(Exception e){
					status = HTTPResponseStatus.S400;	
				}
			// Si la plantilla está en uun servidor:
			}else {
				if(!servers.isEmpty()){
					for (Map.Entry<String, ServersDAO> server: servers.entrySet()) {
						xsd = server.getValue().getAssociatedXsd(xslt);
						if (xsd != null)
							break;
					}
					if(xsd != null){
						try{		
							content = applyXslt(content,xslt,xsd,port);
							status = HTTPResponseStatus.S200;
						} catch(Exception e){
							status = HTTPResponseStatus.S400;	
						}
					// Si la plantilla no se encuentra:
					}else{
						status = HTTPResponseStatus.S404;
					}
				}else{
					status = HTTPResponseStatus.S404;
				}
			}
		}
	}
	
	private String applyXslt(String xml, String xslt, String xsd, int port) 
			throws TransformerException, ParserConfigurationException, SAXException, IOException{				
		URL urlXslt = new URL ("http","localhost",port,"/xslt?uuid=" + xslt);
		Document doc = DOMParsing.loadAndValidateWithExternalURL(
			xml, 
			xsd);
		String xmlString = DOMParsing.toXML(doc);
		File xmlFile = new File("file_valid.xml");
		FileWriter xmlWriter = new FileWriter(xmlFile);
		xmlWriter.write(xmlString);
		xmlWriter.flush();
		xmlWriter.close();
		StringWriter writer = new StringWriter();
		XSLTUtils.transform(
				new StreamSource(xmlFile),
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
/*
//Comprueba si existe la página en local y si tiene plantilla.
			if (xmlDao.containsPage(uuid) && xmlDao.containsTemplate(xslt)) {
				String xsd = xmlDao.getXmlSchema(xslt);
				try{		
					content = applyXslt(xmlDao.getXmlPage(uuid),xslt,xsd,port);
					status = HTTPResponseStatus.S200;
					type = MIME.TEXT_HTML.getMime();
				} catch(Exception e){
					status = HTTPResponseStatus.S400;	
					type = MIME.TEXT_HTML.getMime();
				}
			}
			else if(xmlDao.containsPage(uuid) && xslt==null){
				status = HTTPResponseStatus.S200;
		        content = xmlDao.getXmlPage(uuid);
		        type = MIME.APPLICATION_XML.getMime();
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
						try{
							content = applyXslt(xmlDao.getXmlPage(uuid),xslt, xsd, port);
							status = HTTPResponseStatus.S200;
							type = MIME.TEXT_HTML.getMime();
						} catch(Exception e){
							status = HTTPResponseStatus.S400;
							type = MIME.TEXT_HTML.getMime();
						}
					}else{
						status = HTTPResponseStatus.S404;	
						type = MIME.APPLICATION_XML.getMime();
					}
				}
			}
			// XML y plantilla en remoto.
			else  {			
				if(!servers.isEmpty()){
					String xml=null;
					String xsd = null;
					for (Map.Entry<String, ServersDAO> server: servers.entrySet()) {
						xml = server.getValue().xmlContent(uuid);
						if (xml != null)
							break;
					}
					if(xml != null){
						xsd = xmlDao.getXmlSchema(xslt);
						if(xsd == null){
							for (Map.Entry<String, ServersDAO> server: servers.entrySet()) {
								xsd = server.getValue().getAssociatedXsd(xslt);
								if (xsd != null)
									break;
							}
						}
						if(xsd != null){
							try{
								content = applyXslt(xml,xslt,xsd,port);
								status = HTTPResponseStatus.S200;
								type = MIME.TEXT_HTML.getMime();
							} catch(Exception e){
								status = HTTPResponseStatus.S400;
								type = MIME.TEXT_HTML.getMime();
							}
						}else{
							status = HTTPResponseStatus.S404;	
							type = MIME.APPLICATION_XML.getMime();
						}
					}else{
						status = HTTPResponseStatus.S404;	
						type = MIME.APPLICATION_XML.getMime();
					}
					status = HTTPResponseStatus.S200;
					type = MIME.APPLICATION_XML.getMime();
				}
				// No existe la página.
				else {
					status = HTTPResponseStatus.S404;	
					type = MIME.APPLICATION_XML.getMime();
				}
			}
*/
		
