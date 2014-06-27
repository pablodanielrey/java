package ar.com.dcsys.gwt.data.assistance.client;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.justification.Justification;
import ar.com.dcsys.pr.CSD;

import com.google.gwt.core.shared.GWT;

public class JustificationListSerializer implements CSD<List<Justification>> {
	
	private static final Logger logger = Logger.getLogger(JustificationListSerializer.class.getName());
	
	interface Reader extends name.pehl.piriti.json.client.JsonReader<JustificationListContainer> {}
	private static final Reader READER = GWT.create(Reader.class);
	
	interface Writer extends name.pehl.piriti.json.client.JsonWriter<JustificationListContainer> {}
	private static final Writer WRITER = GWT.create(Writer.class);

	@Override
	public String toJson(List<Justification> o) {
		JustificationListContainer sc = new JustificationListContainer();
		sc.list = o;
		String d = WRITER.toJson(sc);
		logger.log(Level.WARNING,"piriti : " + d);
		return d;
	}
	
	@Override
	public List<Justification> read(String json) {
		logger.log(Level.WARNING,"piriti : " + json);
		JustificationListContainer sc = READER.read(json);
		return sc.list;
	}

}
