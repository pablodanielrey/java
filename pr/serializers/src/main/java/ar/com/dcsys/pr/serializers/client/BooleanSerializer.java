package ar.com.dcsys.pr.serializers.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.pr.CSD;

import com.google.gwt.core.shared.GWT;

public class BooleanSerializer implements CSD<Boolean> {

	private static final Logger logger = Logger.getLogger(BooleanSerializer.class.getName()); 
	
	interface Reader extends name.pehl.piriti.json.client.JsonReader<BooleanContainer> { }
	private static final Reader READER = GWT.create(Reader.class);
	
	interface Writer extends name.pehl.piriti.json.client.JsonWriter<BooleanContainer> { }
	private static final Writer WRITER = GWT.create(Writer.class);
	
	@Override
	public Boolean read(String json) {
		logger.log(Level.WARNING, "piriti : " + json);
		BooleanContainer c = READER.read("{\"c\":" + json + "}");
		return c.c;
	}
	
	@Override
	public String toJson(Boolean o) {
		BooleanContainer c = new BooleanContainer();
		c.c = o;
		String d = WRITER.toJson(c);
		String gsonCompatible = d.replace("{\"c\":","").replace("}", "");
		logger.log(Level.WARNING, "piriti : " + gsonCompatible);
		return gsonCompatible;
	}
	
}
