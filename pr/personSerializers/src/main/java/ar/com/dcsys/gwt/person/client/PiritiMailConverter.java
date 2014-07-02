package ar.com.dcsys.gwt.person.client;

import name.pehl.piriti.converter.client.Converter;
import name.pehl.piriti.json.client.JsonReader;
import name.pehl.piriti.json.client.JsonWriter;
import ar.com.dcsys.data.person.Mail;
import ar.com.dcsys.data.person.Mail;

import com.google.gwt.core.shared.GWT;

public class PiritiMailConverter implements Converter<Mail> {

	public interface MReader extends JsonReader<Mail> {}
	public static final MReader MREADER = GWT.create(MReader.class);
	
	public interface MWriter extends JsonWriter<Mail> {}
	public static final MWriter MWRITER = GWT.create(MWriter.class);
	
	@Override
	public Mail convert(String value) {
		return MREADER.read(value);
	}
	@Override
	public String serialize(Mail value) {
		String d = MWRITER.toJson((Mail)value);
		return d;
	}
}