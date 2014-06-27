package ar.com.dcsys.gwt.data.assistance.client;

import java.util.logging.Level;
import name.pehl.piriti.commons.client.Mapping;
import name.pehl.piriti.commons.client.Mappings;
import java.util.logging.Logger;

import name.pehl.piriti.json.client.JsonReader;
import name.pehl.piriti.json.client.JsonWriter;
import ar.com.dcsys.data.justification.JustificationDate;
import ar.com.dcsys.data.justification.JustificationDateBean;
import ar.com.dcsys.data.person.PersonBean;
import ar.com.dcsys.pr.CSD;

import com.google.gwt.core.client.GWT;

public class JustificationDateSerializer implements CSD<JustificationDate> {
	
	public static final Logger logger = Logger.getLogger(JustificationDateSerializer.class.getName());
	

	public interface PersonReader extends JsonReader<PersonBean> {};
	public static final PersonReader PREADER = GWT.create(PersonReader.class);
	
	public interface Reader extends JsonReader<JustificationDateBean> {}
	public static final Reader READER = GWT.create(Reader.class);
	@Mappings({
		@Mapping(value="justification",convert = PiritiJustificationConverter.class),
		@Mapping(value="person",convert = PiritiPersonConverter.class)
	})

	public interface Writer extends JsonWriter<JustificationDateBean> {}
	public static final Writer WRITER = GWT.create(Writer.class);
	
	
	public interface PersonWriter extends JsonWriter<PersonBean> {}
	public static final PersonWriter PWRITER = GWT.create(PersonWriter.class);	
	
	
	
	
	@Override
	public String toJson(JustificationDate o) {
		String jd = WRITER.toJson((JustificationDateBean)o);
		
		// el justification lo convierte a un string, asi que tengo que sacarle el ""
		jd = jd.replaceAll("justification\\\":\\\"\\{", "justification\":{").replaceAll("\\}\"", "}").replace("\\","");
		// el person lo convierte a un string, asi que tengo que sacarle el ""
		jd = jd.replaceAll("person\\\":\\\"\\{", "person\":{").replaceAll("\\}\"", "}").replace("\\","");
		
		logger.log(Level.WARNING,"toJson:" + jd);
		
		return jd;
	}

	@Override
	public JustificationDate read(String json) {
		logger.log(Level.WARNING,json);
		JustificationDateBean jd = READER.read(json);
		return jd;
	}
}
