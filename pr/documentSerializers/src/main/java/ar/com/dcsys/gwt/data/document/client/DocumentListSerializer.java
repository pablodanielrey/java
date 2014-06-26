package ar.com.dcsys.gwt.data.document.client;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.document.Document;
import ar.com.dcsys.pr.CSD;

import com.google.gwt.core.shared.GWT;

public class DocumentListSerializer implements CSD<List<Document>> {

	private static final Logger logger = Logger.getLogger(DocumentListSerializer.class.getName());
	
	interface Reader extends name.pehl.piriti.json.client.JsonReader<DocumentListContainer> { }
	private static final Reader READER = GWT.create(Reader.class);
	
	interface Writer extends name.pehl.piriti.json.client.JsonWriter<DocumentListContainer> { }
	private static final Writer WRITER = GWT.create(Writer.class);

	
	@Override
	public String toJson(List<Document> o) {
		DocumentListContainer sc = new DocumentListContainer();
		sc.list = o;
		String d = WRITER.toJson(sc);
		logger.log(Level.WARNING,"piriti : " + d);
		return d;
	}

	@Override
	public List<Document> read(String json) {
		logger.log(Level.WARNING,"piriti : " + json);
		DocumentListContainer sc = READER.read(json);
		return sc.list;
	}

	
	
}
