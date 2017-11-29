package es.uvigo.esei.dai.hybridserver;

import java.io.IOException;
import java.io.StringWriter;
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

	private final XmlDAO xmlDao;
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
	public void methodGet(HTTPRequest request, int port) 
			throws IOException, TransformerException{
		params = request.getResourceParameters();
		uuid = params.get("uuid");
		String xslt = params.get("xslt");

		// Comprueba si se recibe el parámetro uuid.
		if(uuid == null) { 
			status = HTTPResponseStatus.S200;
			content = xmlDao.getXmlList(); // Recupera una lista de páginas.
			type = MIME.TEXT_HTML.getMime();
		}
		else {
			// Comprueba si existe la página en el servidor y si tiene plantilla.
			if (xmlDao.containsPage(uuid) && xmlDao.containsTemplate(xslt)) {
				String xsd = xmlDao.getXmlSchema(xslt);
				URL urlXml = new URL ("http","localhost",port,"/xml?uuid=" + uuid);					
				URL urlXslt = new URL ("http","localhost",port,"/xslt?uuid=" + xslt);
				URL urlXsd = new URL ("http","localhost",port,"/xsd?uuid="+xsd);
				try{
					DOMParsing.loadAndValidateWithExternalURL(
						urlXml.toString(), 
						urlXsd.toString());
					StringWriter writer = new StringWriter();
					XSLTUtils.transform(
							new StreamSource(urlXml.toString()),
							new StreamSource(urlXslt.toString()),
							new StreamResult(writer));
					status = HTTPResponseStatus.S200;
					content = writer.toString();
					type = MIME.TEXT_HTML.getMime();
				} catch(Exception e){
					status = HTTPResponseStatus.S400;	
					type = MIME.TEXT_HTML.getMime();
				}
			}
			// XML sin plantilla XSLT.
			else if (xmlDao.containsPage(uuid) && xslt==null) {
				status = HTTPResponseStatus.S200;
				content = xmlDao.getXmlPage(uuid);
				type = MIME.APPLICATION_XML.getMime();
			}
			// No existe la página.
			else{
				status = HTTPResponseStatus.S404;	
				type = MIME.APPLICATION_XML.getMime();
			}
		}
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
