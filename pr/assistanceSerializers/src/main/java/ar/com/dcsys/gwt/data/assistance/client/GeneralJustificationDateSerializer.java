package ar.com.dcsys.gwt.data.assistance.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import name.pehl.piriti.commons.client.Mapping;
import name.pehl.piriti.commons.client.Mappings;
import name.pehl.piriti.json.client.JsonReader;
import name.pehl.piriti.json.client.JsonWriter;
import ar.com.dcsys.data.justification.GeneralJustificationDate;
import ar.com.dcsys.data.justification.GeneralJustificationDate;
import ar.com.dcsys.pr.CSD;

import com.google.gwt.core.client.GWT;

public class GeneralJustificationDateSerializer implements CSD<GeneralJustificationDate> {
	
	public static final Logger logger = Logger.getLogger(GeneralJustificationDateSerializer.class.getName());
		
	public interface Reader extends JsonReader<GeneralJustificationDate> {}
	public static final Reader READER = GWT.create(Reader.class);
	@Mappings({
		@Mapping(value="justification",convert = PiritiJustificationConverter.class)
	})

	public interface Writer extends JsonWriter<GeneralJustificationDate> {}
	public static final Writer WRITER = GWT.create(Writer.class);
	
	
	@Override
	public String toJson(GeneralJustificationDate o) {
		String jd = WRITER.toJson((GeneralJustificationDate)o);
		
		// el justification lo convierte a un string, asi que tengo que sacarle el ""
		jd = jd.replaceAll("justification\\\":\\\"\\{", "justification\":{").replaceAll("\\}\"", "}").replace("\\","");
		
		logger.log(Level.WARNING,"toJson:" + jd);
		
		return jd;
	}

	@Override
	public GeneralJustificationDate read(String json) {
		logger.log(Level.WARNING,json);
		GeneralJustificationDate gjd = READER.read(json);
		return gjd;
	}
}
