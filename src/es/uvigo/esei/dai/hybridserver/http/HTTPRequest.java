package es.uvigo.esei.dai.hybridserver.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;

public class HTTPRequest {
	
	private HTTPRequestMethod method;
	private String resourceChain;
	private String[] resourcePath = {};
	private String resourceName;
	private Map<String, String> resourceParameters;
	private String httpVersion;
	private Map<String, String> headerParameters;
	private String content;
	private int contentLength;
	
	public HTTPRequest(Reader reader) throws IOException, HTTPParseException {
		BufferedReader in = new BufferedReader(reader);
		resourceParameters = new LinkedHashMap<String, String>();
		headerParameters = new LinkedHashMap<String, String>();
		String[] request = in.readLine().split(" ");
		String[] parameters, parameter;
		String line;
		
		try{	
			method = HTTPRequestMethod.valueOf(request[0]);
			resourceChain = request[1];
			httpVersion = request[2];
			
			switch(method.toString()){
				case "GET": {
					if(request[1].contains("?")){
						resourceName = request[1].substring(
							1, request[1].indexOf("?")
						);
						resourcePath = request[1].substring(
							1, request[1].indexOf("?")
						).split("/");
						
						parameters = request[1].substring(
							request[1].indexOf("?")+1
						).split("&");
						for(String p : parameters){
							parameter = p.split("=");
							resourceParameters.put(parameter[0], parameter[1]);
						}
					}else{
						resourceName = request[1].substring(1);
						if(!request[1].equals("/"))
							resourcePath = request[1].substring(1).split("/");
	
					}
					while(!(line = in.readLine()).equals("")){
						parameter = line.split(": ");
						headerParameters.put(parameter[0], parameter[1]);
					}
					break;
				}
				case "POST": {
					resourceName = request[1].substring(1);
					if(!request[1].equals("/"))
						resourcePath = request[1].substring(1).split("/");
	
					contentLength = -1;
					Boolean codificado = false;
					while(!(line = in.readLine()).equals("")){
						parameter = line.split(": ");
						headerParameters.put(parameter[0], parameter[1]);
						if (parameter[0].equals("Content-Length"))
							contentLength = Integer.parseInt(parameter[1]);
						else if (parameter[0].equals("Content-Type") && (!parameter[1].equals("UTF-8")))
							codificado = true;
					}
					if (contentLength != -1) {
						//leer un buffer con tamaño contentLength y asignarlo a Content
						char[] buffer = new char[contentLength];
						in.read(buffer, 0, contentLength);
						content = new String(buffer);
						//content = in.readLine();
						//contentLength = content.length();
						//solo si está codificado con cabecera "Content-Type: application/x-www-form-urlencoded\r\n"
						if (codificado)
							content = URLDecoder.decode( content, "UTF-8" );

						parameters = content.split("&");
						for(String p : parameters){ 
							parameter = p.split("=");
							resourceParameters.put(parameter[0], parameter[1]);
						}	
					}
					break;
				}
			}
		} catch(Exception e) {
			throw new HTTPParseException(in.readLine());
		}
	}

	public HTTPRequestMethod getMethod() {
		return method;
	}

	public String getResourceChain() {
		return resourceChain;
	}

	public String[] getResourcePath() {
		return resourcePath;
	}

	public String getResourceName() {
		return resourceName;
	}

	public Map<String, String> getResourceParameters() {
		return resourceParameters;
	}

	public String getHttpVersion() {
		return httpVersion;
	}

	public Map<String, String> getHeaderParameters() {
		return headerParameters;
	}

	public String getContent() {
		return content;
	}

	public int getContentLength() {
		return contentLength;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(this.getMethod().name()).append(' ').append(this.getResourceChain())
				.append(' ').append(this.getHttpVersion()).append("\r\n");

		for (Map.Entry<String, String> param : this.getHeaderParameters().entrySet()) {
			sb.append(param.getKey()).append(": ").append(param.getValue()).append("\r\n");
		}

		if (this.getContentLength() > 0) {
			sb.append("\r\n").append(this.getContent());
		}

		return sb.toString();
	}
}
