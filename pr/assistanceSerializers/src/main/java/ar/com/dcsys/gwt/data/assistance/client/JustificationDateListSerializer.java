package ar.com.dcsys.gwt.data.assistance.client;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.justification.JustificationDate;
import ar.com.dcsys.pr.CSD;

import com.google.gwt.core.shared.GWT;

public class JustificationDateListSerializer implements CSD<List<JustificationDate>> {
	
	private static final Logger logger = Logger.getLogger(JustificationDateListSerializer.class.getName());
	
	interface Reader extends name.pehl.piriti.json.client.JsonReader<JustificationDateListContainer> {}
	private static final Reader READER = GWT.create(Reader.class);
	
	interface Writer extends name.pehl.piriti.json.client.JsonWriter<JustificationDateListContainer> {}
	private static final Writer WRITER = GWT.create(Writer.class);

	@Override
	public String toJson(List<JustificationDate> o) {
		JustificationDateListContainer sc = new JustificationDateListContainer();
		sc.list = o;
		String d = WRITER.toJson(sc);
		
		d = d.replaceAll("\\\"\\{", "{").replaceAll("\\}\"", "}").replace("\\","");
		
		logger.log(Level.WARNING,"piriti : " + d);
		return d;
	}
	
	@Override
	public List<JustificationDate> read(String json) {
		logger.log(Level.WARNING,"piriti : " + json);
		JustificationDateListContainer sc = READER.read(json);
		return sc.list;
	}

}
