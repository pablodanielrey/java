package ar.com.dcsys.gwt.data.assistance.client;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.report.Report;
import ar.com.dcsys.pr.CSD;

import com.google.gwt.core.shared.GWT;

public class ReportListSerializer implements CSD<List<Report>> {
	
	private static final Logger logger = Logger.getLogger(ReportListSerializer.class.getName());
	
	interface Reader extends name.pehl.piriti.json.client.JsonReader<ReportListContainer> {}
	private static final Reader READER = GWT.create(Reader.class);
	
	interface Writer extends name.pehl.piriti.json.client.JsonWriter<ReportListContainer> {}
	private static final Writer WRITER = GWT.create(Writer.class);

	@Override
	public String toJson(List<Report> o) {
		ReportListContainer sc = new ReportListContainer();
		sc.list = o;
		String d = WRITER.toJson(sc);
		
		d = d.replaceAll("\\\"\\{", "{").replaceAll("\\}\"", "}").replace("\\","");
		
		logger.log(Level.WARNING,"piriti : " + d);
		return d;
	}
	
	@Override
	public List<Report> read(String json) {
		logger.log(Level.WARNING,"piriti : " + json);
		ReportListContainer sc = READER.read(json);
		return sc.list;
	}

}
