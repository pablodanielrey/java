package ar.com.dcsys.pr.serializers.client;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.pr.CSD;

import com.google.gwt.core.shared.GWT;

public class DateSerializer implements CSD<Date> {

private static final Logger logger = Logger.getLogger(DateSerializer.class.getName());
	
	interface Reader extends name.pehl.piriti.json.client.JsonReader<DateContainer> { }
	private static final Reader READER = GWT.create(Reader.class);
	
	interface Writer extends name.pehl.piriti.json.client.JsonWriter<DateContainer> { }
	private static final Writer WRITER = GWT.create(Writer.class);
	
	@Override
	public Date read(String json) {
		logger.log(Level.WARNING, "piriti : " + json);
		DateContainer c = READER.read("{\"d\":" + json + "}");
		return c.d;
	}
	
	@Override
	public String toJson(Date o) {
		DateContainer c = new DateContainer();
		c.d = o;
		String d = WRITER.toJson(c);
		String gsonCompatible = d.replace("{\"d\":","").replace("}", "");
		logger.log(Level.WARNING, "piriti : " + gsonCompatible);
		return gsonCompatible;
	}
	
}
