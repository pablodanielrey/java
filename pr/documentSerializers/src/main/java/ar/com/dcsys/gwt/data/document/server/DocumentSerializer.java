package ar.com.dcsys.gwt.data.document.server;

import java.util.logging.Logger;

import ar.com.dcsys.data.document.Document;
import ar.com.dcsys.data.document.DocumentBean;
import ar.com.dcsys.pr.CSD;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class DocumentSerializer implements CSD<Document> {

	private static final Logger logger = Logger.getLogger(DocumentSerializer.class.getName());
	
	private final Gson gson = (new GsonBuilder()).setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").create();
	
	@Override
	public Document read(String json) {
		logger.warning(json);
		DocumentBean document = gson.fromJson(json, DocumentBean.class);
		return document;
	}
	
	@Override
	public String toJson(Document o) {
		String d = gson.toJson(o);
		logger.warning(d);
		return d;
	}
	
}
