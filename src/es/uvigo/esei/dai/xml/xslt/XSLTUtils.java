package es.uvigo.esei.dai.xml.xslt;

import java.io.File;
import java.io.StringWriter;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class XSLTUtils {
	public static void transform(Source xmlSource, Source xsltSource, Result result
	) throws TransformerException {
		TransformerFactory tFactory = TransformerFactory.newInstance();
		Transformer transformer = tFactory.newTransformer(xsltSource);
		
		transformer.transform(xmlSource, result);
	}

	public static String transform(File xml, File xslt)
	throws TransformerException {
		TransformerFactory tFactory = TransformerFactory.newInstance();
		Transformer transformer = tFactory.newTransformer(new StreamSource(xslt));
		
		StringWriter writer = new StringWriter();
		transformer.transform(
			new StreamSource(xml), 
			new StreamResult(writer)
		);
		
		return writer.toString();
	}

}
