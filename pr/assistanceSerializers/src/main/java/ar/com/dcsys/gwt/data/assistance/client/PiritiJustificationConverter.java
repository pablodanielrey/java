package ar.com.dcsys.gwt.data.assistance.client;

import name.pehl.piriti.converter.client.Converter;
import name.pehl.piriti.json.client.JsonReader;
import name.pehl.piriti.json.client.JsonWriter;
import ar.com.dcsys.data.justification.Justification;
import ar.com.dcsys.data.justification.JustificationBean;

import com.google.gwt.core.shared.GWT;

public class PiritiJustificationConverter implements Converter<Justification> {


	public interface JustificationReader extends JsonReader<JustificationBean> {};
	public static final JustificationReader JREADER = GWT.create(JustificationReader.class);

	public interface JustificationWriter extends JsonWriter<JustificationBean> {}
	public static final JustificationWriter JWRITER = GWT.create(JustificationWriter.class);
	
	@Override
	public Justification convert(String value) {
		return JREADER.read(value);
	}
	@Override
	public String serialize(Justification value) {
		String d = JWRITER.toJson((JustificationBean)value);
		return d;
	}
}