package ar.com.dcsys.gwt.data.assistance.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import name.pehl.piriti.json.client.JsonReader;
import name.pehl.piriti.json.client.JsonWriter;
import ar.com.dcsys.data.report.Report;
import ar.com.dcsys.pr.CSD;

import com.google.gwt.core.client.GWT;

public class ReportSerializer implements CSD<Report> {
	
	public static final Logger logger = Logger.getLogger(ReportSerializer.class.getName());
	
	//public static final PeriodSerializer ps = GWT.create(PeriodSerializer.class);
	
	public interface Reader extends JsonReader<Report> {}
	public static final Reader READER = GWT.create(Reader.class);

	public interface Writer extends JsonWriter<Report> {}
	public static final Writer WRITER = GWT.create(Writer.class);	
	
	@Override
	public String toJson(Report o) {
		String rs = WRITER.toJson(o);
		logger.log(Level.WARNING, rs);
		return rs;
	}

	@Override
	public Report read(String json) {
		logger.log(Level.WARNING,json);
		Report rs = READER.read(json);
		return rs;
	}
	
}
