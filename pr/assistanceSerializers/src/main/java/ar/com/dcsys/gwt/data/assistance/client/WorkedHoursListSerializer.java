package ar.com.dcsys.gwt.data.assistance.client;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.period.WorkedHours;
import ar.com.dcsys.pr.CSD;

import com.google.gwt.core.shared.GWT;

public class WorkedHoursListSerializer implements CSD<List<WorkedHours>> {

	private static final Logger logger = Logger.getLogger(WorkedHoursListSerializer.class.getName());

	
	interface Reader extends name.pehl.piriti.json.client.JsonReader<WorkedHoursListContainer> {}
	private static final Reader READER = GWT.create(Reader.class);
	
	interface Writer extends name.pehl.piriti.json.client.JsonWriter<WorkedHoursListContainer> {}
	private static final Writer WRITER = GWT.create(Writer.class);
	
	@Override
	public String toJson(List<WorkedHours> o) {
		WorkedHoursListContainer sc = new WorkedHoursListContainer();
		sc.list = o;
		String d = WRITER.toJson(sc);
		
		d = d.replaceAll("\\\"\\{", "{").replaceAll("\\}\"", "}").replace("\\","");

		logger.log(Level.WARNING,"piriti : " + d);
		return d;
	}

	@Override
	public List<WorkedHours> read(String json) {
		logger.log(Level.WARNING,"piriti : " + json);
		WorkedHoursListContainer sc = READER.read(json);
		return sc.list;
	}
	

}
