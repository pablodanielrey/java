package ar.com.dcsys.gwt.person.client;


import java.util.logging.Level;
import java.util.logging.Logger;

import name.pehl.piriti.json.client.JsonReader;
import name.pehl.piriti.json.client.JsonWriter;
import ar.com.dcsys.data.person.Mail;
import ar.com.dcsys.data.person.MailBean;
import ar.com.dcsys.pr.CSD;

import com.google.gwt.core.shared.GWT;

public class MailSerializer implements CSD<Mail> {
	
	private static final Logger logger = Logger.getLogger(MailSerializer.class.getName());
	
	public interface Reader extends JsonReader<MailBean> {}
	public static final Reader READER = GWT.create(Reader.class);
	
	public interface Writer extends JsonWriter<MailBean> {}
	public static final Writer WRITER = GWT.create(Writer.class);
	
	@Override
	public String toJson(Mail o) {
		
		String d = WRITER.toJson((MailBean) o);
		logger.log(Level.WARNING,"MailSerializer : " + d);
		
		return d;
	}
	
	@Override
	public Mail read(String json) {
	
		logger.log(Level.WARNING,"MailSerializer : " + json);
		Mail mail = READER.read(json);
		
		return mail;
	}

}
