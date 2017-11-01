package es.uvigo.esei.dai.hybridserver.http;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HTTPResponse {
	
	private HTTPResponseStatus status;
	private String version, content;
	private Map<String, String> parameters;
	
	public HTTPResponse() {
		parameters = new LinkedHashMap<String, String>();
		content = null;
	}

	public HTTPResponseStatus getStatus() {
		return status;
	}

	public void setStatus(HTTPResponseStatus status) {
		this.status = status;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
		putParameter("Content-Length", 
				Integer.toString(getContent().length()));
	}

	public Map<String, String> getParameters() {
		return parameters;
	}

	public String putParameter(String name, String value) {
		parameters.put(name, value);
		return name;
	}

	public boolean containsParameter(String name) {
		return parameters.containsKey(name);
	}

	public String removeParameter(String name) {
		parameters.remove(name);
		return name;
	}

	public void clearParameters() {
		parameters.clear();
	}

	public List<String> listParameters() {
		List<String> params = new ArrayList<String>();
		
		for(String key: parameters.keySet()){
			params.add(key +": "+ parameters.get(key));
		}
		return params;
	}

	public void print(Writer writer) throws IOException {
		
		writer.append(getVersion());
		writer.append(" ");
		writer.append(getStatus().getCode() +" "+ getStatus().getStatus());	
		
		if(!getParameters().isEmpty()) {
			List<String> params = listParameters();
			for(String p: params){
				writer.append("\r\n");
				writer.append(p);
			}
		}
		writer.append("\r\n\r\n");
		
		if(getContent() != null)
			writer.append(getContent());
	}

	@Override
	public String toString() {
		final StringWriter writer = new StringWriter();

		try {
			this.print(writer);
		} catch (IOException e) {
		}

		return writer.toString();
	}
}
