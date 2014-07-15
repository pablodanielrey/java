package ar.com.dcsys.gwt.person.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import name.pehl.piriti.json.client.JsonReader;
import name.pehl.piriti.json.client.JsonWriter;
import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.pr.CSD;

import com.google.gwt.core.client.GWT;

public class GroupSerializer implements CSD<Group> {

	private static final Logger logger = Logger.getLogger(GroupSerializer.class.getName());
	
	public interface Reader extends JsonReader<Group> {}
	public static final Reader READER = GWT.create(Reader.class);

	public interface Writer extends JsonWriter<Group> {}
	public static final Writer WRITER = GWT.create(Writer.class);
	
	@Override
	public String toJson(Group o) {
		String d = WRITER.toJson((Group)o);
		
		d = d.replaceAll("\\\"\\{", ":{").replaceAll("\\}\"", "}").replace("\\","");
		
		logger.log(Level.WARNING,d);
		return d;	
	}
	
	@Override
	public Group read(String json) {
		logger.log(Level.WARNING,json);
		Group g = READER.read(json);
		return g;
	}	


}
