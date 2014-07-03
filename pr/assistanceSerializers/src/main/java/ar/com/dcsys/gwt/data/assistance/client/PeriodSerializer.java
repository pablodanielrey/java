package ar.com.dcsys.gwt.data.assistance.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import name.pehl.piriti.json.client.JsonReader;
import name.pehl.piriti.json.client.JsonWriter;
import ar.com.dcsys.data.period.Period;
import ar.com.dcsys.pr.CSD;

import com.google.gwt.core.client.GWT;

public class PeriodSerializer implements CSD<Period> {

	public static final Logger logger = Logger.getLogger(PeriodSerializer.class.getName());
	
	public static final WorkedHoursSerializer workedHours = GWT.create(WorkedHoursSerializer.class);
	
	public interface Reader extends JsonReader<Period> {}
	public static final Reader READER = GWT.create(Reader.class);

	public interface Writer extends JsonWriter<Period> {}
	public static final Writer WRITER = GWT.create(Writer.class);	
	
	@Override
	public String toJson(Period o) {
		String rs = WRITER.toJson(o);
		logger.log(Level.WARNING, rs);
		return rs;
	}

	@Override
	public Period read(String json) {
		logger.log(Level.WARNING,json);
		Period rs = READER.read(json);
		return rs;
	}

}
