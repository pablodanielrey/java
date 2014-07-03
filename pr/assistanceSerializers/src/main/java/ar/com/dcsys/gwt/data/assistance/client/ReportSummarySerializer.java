package ar.com.dcsys.gwt.data.assistance.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import name.pehl.piriti.json.client.JsonReader;
import name.pehl.piriti.json.client.JsonWriter;
import ar.com.dcsys.data.report.ReportSummary;
import ar.com.dcsys.pr.CSD;

import com.google.gwt.core.client.GWT;

public class ReportSummarySerializer implements CSD<ReportSummary> {

	public static final Logger logger = Logger.getLogger(ReportSummarySerializer.class.getName());
		
	public static final ReportSerializer reportSerializer = GWT.create(ReportSerializer.class);
	
	public interface Reader extends JsonReader<ReportSummary> {}
	public static final Reader READER = GWT.create(Reader.class);

	public interface Writer extends JsonWriter<ReportSummary> {}
	public static final Writer WRITER = GWT.create(Writer.class);	
	
	@Override
	public String toJson(ReportSummary o) {
		String rs = WRITER.toJson(o);
		logger.log(Level.WARNING, rs);
		return rs;
	}

	@Override
	public ReportSummary read(String json) {
		logger.log(Level.WARNING,json);
		ReportSummary rs = READER.read(json);
		return rs;
	}

}
