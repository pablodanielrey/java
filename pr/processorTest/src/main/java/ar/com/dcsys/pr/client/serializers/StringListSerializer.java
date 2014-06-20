package ar.com.dcsys.pr.client.serializers;

import java.util.List;

import ar.com.dcsys.pr.CSD;

import com.google.gwt.core.shared.GWT;

public class StringListSerializer implements CSD<List<String>> {

	interface Reader extends name.pehl.piriti.json.client.JsonReader<List<String>> { }
	private final Reader READER = GWT.create(Reader.class);
	
	interface Writer extends name.pehl.piriti.json.client.JsonWriter<List<String>> { }
	private final Writer WRITER = GWT.create(Writer.class);

	
	@Override
	public String toJson(List<String> o) {
		return WRITER.toJson(o);
	}

	@Override
	public List<String> read(String json) {
		return READER.read(json);
	}

}
