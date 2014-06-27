package ar.com.dcsys.gwt.data.assistance.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import name.pehl.piriti.json.client.JsonReader;
import name.pehl.piriti.json.client.JsonWriter;
import ar.com.dcsys.data.justification.Justification;
import ar.com.dcsys.data.justification.JustificationBean;
import ar.com.dcsys.pr.CSD;

import com.google.gwt.core.client.GWT;

public class JustificationSerializer implements CSD<Justification> {
	
	public static final Logger logger = Logger.getLogger(JustificationSerializer.class.getName());
	
	public interface Reader extends JsonReader<JustificationBean> {}
	public static final Reader READER = GWT.create(Reader.class);
	
	public interface Writer extends JsonWriter<JustificationBean> {}
	public static final Writer WRITER = GWT.create(Writer.class);	
	
	
	@Override
	public String toJson(Justification o) {
		String d = WRITER.toJson((JustificationBean)o);
		logger.log(Level.WARNING,d);
		
		return d;
	}

	@Override
	public Justification read(String json) {
		logger.log(Level.WARNING,json);
		JustificationBean d = READER.read(json);
		return d;
	}
}
