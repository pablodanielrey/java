package ar.com.dcsys.gwt.data.assistance.client;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.justification.GeneralJustificationDate;
import ar.com.dcsys.pr.CSD;

import com.google.gwt.core.shared.GWT;

public class GeneralJustificationDateListSerializer implements CSD<List<GeneralJustificationDate>> {
	
	private static final Logger logger = Logger.getLogger(GeneralJustificationDateListSerializer.class.getName());
	
	interface Reader extends name.pehl.piriti.json.client.JsonReader<GeneralJustificationDateListContainer> {}
	private static final Reader READER = GWT.create(Reader.class);
	
	interface Writer extends name.pehl.piriti.json.client.JsonWriter<GeneralJustificationDateListContainer> {}
	private static final Writer WRITER = GWT.create(Writer.class);

	@Override
	public String toJson(List<GeneralJustificationDate> o) {
		GeneralJustificationDateListContainer sc = new GeneralJustificationDateListContainer();
		sc.list = o;
		String d = WRITER.toJson(sc);
		
		d = d.replaceAll("\\\"\\{", "{").replaceAll("\\}\"", "}").replace("\\","");
		
		logger.log(Level.WARNING,"piriti toJson: " + d);
		return d;
	}
	
	@Override
	public List<GeneralJustificationDate> read(String json) {
		logger.log(Level.WARNING,"piriti read: " + json);
		GeneralJustificationDateListContainer sc = READER.read(json);
		return sc.list;
	}

}
