package ar.com.dcsys.gwt.data.assistance.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import name.pehl.piriti.json.client.JsonReader;
import name.pehl.piriti.json.client.JsonWriter;
import ar.com.dcsys.data.log.AttLog;
import ar.com.dcsys.pr.CSD;

import com.google.gwt.core.client.GWT;

public class AttLogSerializer implements CSD<AttLog> {

	public static final Logger logger = Logger.getLogger(AttLogSerializer.class.getName());
	
	public interface Reader extends JsonReader<AttLog> {}
	public static final Reader READER = GWT.create(Reader.class);

	public interface Writer extends JsonWriter<AttLog> {}
	public static final Writer WRITER = GWT.create(Writer.class);	
	
	@Override
	public String toJson(AttLog o) {
		String rs = WRITER.toJson(o);
		logger.log(Level.WARNING, rs);
		return rs;
	}

	@Override
	public AttLog read(String json) {
		logger.log(Level.WARNING,json);
		AttLog rs = READER.read(json);
		return rs;
	}

}
