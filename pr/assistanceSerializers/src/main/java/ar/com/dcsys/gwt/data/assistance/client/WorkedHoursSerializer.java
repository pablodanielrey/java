package ar.com.dcsys.gwt.data.assistance.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import name.pehl.piriti.json.client.JsonReader;
import name.pehl.piriti.json.client.JsonWriter;
import ar.com.dcsys.data.period.WorkedHours;
import ar.com.dcsys.pr.CSD;

import com.google.gwt.core.client.GWT;

public class WorkedHoursSerializer implements CSD<WorkedHours> {

	public static final Logger logger = Logger.getLogger(WorkedHoursSerializer.class.getName());
	
	public interface Reader extends JsonReader<WorkedHours> {}
	public static final Reader READER = GWT.create(Reader.class);

	public interface Writer extends JsonWriter<WorkedHours> {}
	public static final Writer WRITER = GWT.create(Writer.class);	
	
	@Override
	public String toJson(WorkedHours o) {
		String rs = WRITER.toJson(o);
		logger.log(Level.WARNING, rs);
		return rs;
	}

	@Override
	public WorkedHours read(String json) {
		logger.log(Level.WARNING,json);
		WorkedHours rs = READER.read(json);
		return rs;
	}
	
}
