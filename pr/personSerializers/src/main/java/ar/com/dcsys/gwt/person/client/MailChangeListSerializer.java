package ar.com.dcsys.gwt.person.client;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.person.MailChange;
import ar.com.dcsys.pr.CSD;

import com.google.gwt.core.shared.GWT;

public class MailChangeListSerializer implements CSD<List<MailChange>> {
	
	private static final Logger logger = Logger.getLogger(MailChangeListSerializer.class.getName());
	
	interface Reader extends name.pehl.piriti.json.client.JsonReader<MailChangeListContainer> {}
	private static final Reader READER = GWT.create(Reader.class);
	
	interface Writer extends name.pehl.piriti.json.client.JsonWriter<MailChangeListContainer> {}
	private static final Writer WRITER = GWT.create(Writer.class);

	@Override
	public String toJson(List<MailChange> o) {
		MailChangeListContainer sc = new MailChangeListContainer();
		sc.list = o;
		String d = WRITER.toJson(sc);
		logger.log(Level.WARNING,"piriti : " + d);
		return d;
	}
	
	@Override
	public List<MailChange> read(String json) {
		logger.log(Level.WARNING,"piriti : " + json);
		MailChangeListContainer sc = READER.read(json);
		return sc.list;
	}
}
