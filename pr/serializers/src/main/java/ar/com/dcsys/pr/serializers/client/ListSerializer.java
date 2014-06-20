package ar.com.dcsys.pr.serializers.client;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.pr.CSD;

import com.google.gwt.core.shared.GWT;

public class ListSerializer implements CSD<List> {

	private static final Logger logger = Logger.getLogger(ListSerializer.class.getName());
	
	interface Reader extends name.pehl.piriti.json.client.JsonReader<ListContainer> { }
	private static final Reader READER = GWT.create(Reader.class);
	
	interface Writer extends name.pehl.piriti.json.client.JsonWriter<ListContainer> { }
	private static final Writer WRITER = GWT.create(Writer.class);

	
	@Override
	public String toJson(List o) {
		ListContainer sc = new ListContainer();
		sc.list = o;
		String d = WRITER.toJson(sc);
		logger.log(Level.WARNING,"piriti : " + d);
		return d;
	}

	@Override
	public List read(String json) {
		logger.log(Level.WARNING,"piriti : " + json);
		ListContainer sc = READER.read(json);
		return sc.list;
	}

}
