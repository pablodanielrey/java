package ar.com.dcsys.gwt.person.client;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.person.Mail;
import ar.com.dcsys.pr.CSD;

import com.google.gwt.core.shared.GWT;

public class MailListSerializer implements CSD<List<Mail>> {
	
	private static final Logger logger = Logger.getLogger(MailListSerializer.class.getName());
	
	interface Reader extends name.pehl.piriti.json.client.JsonReader<MailListContainer> {}
	private static final Reader READER = GWT.create(Reader.class);
	
	interface Writer extends name.pehl.piriti.json.client.JsonWriter<MailListContainer> {}
	private static final Writer WRITER = GWT.create(Writer.class);

	@Override
	public String toJson(List<Mail> o) {
		MailListContainer sc = new MailListContainer();
		sc.list = o;
		String d = WRITER.toJson(sc);
		logger.log(Level.WARNING,"piriti : " + d);
		return d;
	}
	
	@Override
	public List<Mail> read(String json) {
		logger.log(Level.WARNING,"piriti : " + json);
		MailListContainer sc = READER.read(json);
		return sc.list;
	}
}
