package ar.com.dcsys.gwt.data.document.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import name.pehl.piriti.json.client.JsonReader;
import name.pehl.piriti.json.client.JsonWriter;
import ar.com.dcsys.data.document.Document;
import ar.com.dcsys.data.document.DocumentBean;
import ar.com.dcsys.pr.CSD;

import com.google.gwt.core.client.GWT;

public class DocumentSerializer implements CSD<Document> {

	public static final Logger logger = Logger.getLogger(DocumentSerializer.class.getName());
	
	public interface Reader extends JsonReader<DocumentBean> {}
	public static final Reader READER = GWT.create(Reader.class);
	
	public interface Writer extends JsonWriter<DocumentBean> {}
	public static final Writer WRITER = GWT.create(Writer.class);	
	
	
	@Override
	public String toJson(Document o) {
		String d = WRITER.toJson((DocumentBean)o);
		logger.log(Level.WARNING,d);
		
		return d;
	}

	@Override
	public Document read(String json) {
		logger.log(Level.WARNING,json);
		DocumentBean d = READER.read(json);
		return d;
	}

}
