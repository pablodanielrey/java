package ar.com.dcsys.gwt.person.client;


import java.util.logging.Level;
import java.util.logging.Logger;

import name.pehl.piriti.commons.client.Mapping;
import name.pehl.piriti.commons.client.Mappings;
import name.pehl.piriti.json.client.JsonReader;
import name.pehl.piriti.json.client.JsonWriter;
import ar.com.dcsys.data.person.MailChange;
import ar.com.dcsys.data.person.MailChangeBean;
import ar.com.dcsys.pr.CSD;

import com.google.gwt.core.shared.GWT;

public class MailChangeSerializer implements CSD<MailChange> {
	
	private static final Logger logger = Logger.getLogger(MailChangeSerializer.class.getName());
	

	public interface Reader extends JsonReader<MailChangeBean> {}
	public static final Reader READER = GWT.create(Reader.class);
	
	@Mappings({
		@Mapping(value="mail",convert = PiritiMailConverter.class)
	})
	public interface Writer extends JsonWriter<MailChangeBean> {}
	public static final Writer WRITER = GWT.create(Writer.class);
	
	@Override
	public String toJson(MailChange o) {
		
		String d = WRITER.toJson((MailChangeBean) o);
		logger.log(Level.WARNING,"MailSerializer : " + d);
		
		return d;
	}
	
	@Override
	public MailChange read(String json) {
	
		logger.log(Level.WARNING,"MailSerializer : " + json);
		MailChange mail = READER.read(json);
		
		return mail;
	}

}
