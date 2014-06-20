package ar.com.dcsys.pr.client.serializers;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.pr.CSD;

import com.google.gwt.core.shared.GWT;

public class StringListSerializer implements CSD<List<String>> {

	private static final Logger logger = Logger.getLogger(StringListSerializer.class.getName());
	
	interface Reader extends name.pehl.piriti.json.client.JsonReader<StringContainer> { }
	private static final Reader READER = GWT.create(Reader.class);
	
	interface Writer extends name.pehl.piriti.json.client.JsonWriter<StringContainer> { }
	private static final Writer WRITER = GWT.create(Writer.class);

	
	@Override
	public String toJson(List<String> o) {
		StringContainer sc = new StringContainer();
		sc.list = o;
		String d = WRITER.toJson(sc);
		logger.log(Level.WARNING,"piriti : " + d);
		return d;
	}

	@Override
	public List<String> read(String json) {
		logger.log(Level.WARNING,"piriti : " + json);
		StringContainer sc = READER.read(json);
		return sc.list;
	}

}
