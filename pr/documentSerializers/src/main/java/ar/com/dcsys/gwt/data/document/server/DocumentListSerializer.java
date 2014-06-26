package ar.com.dcsys.gwt.data.document.server;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.document.Document;
import ar.com.dcsys.pr.CSD;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class DocumentListSerializer implements CSD<List<Document>> {

	private static final Logger logger = Logger.getLogger(DocumentListSerializer.class.getName());
	
	private final Gson gson = (new GsonBuilder()).create();
	
	private class Container {
		List<Document> list;
	}	
	
	@Override
	public String toJson(List<Document> o) {
		Container sc = new Container();
		sc.list = o;
		String d = gson.toJson(sc);
		logger.log(Level.WARNING, "gson : " + d);
		return d;
	}

	@Override
	public List<Document> read(String json) {
		logger.log(Level.WARNING,"gson : " + json);

		TypeToken<Container> type = new TypeToken<Container>() {};
		Container sc = gson.fromJson(json, type.getType());
		return sc.list;
	}	
	
}
