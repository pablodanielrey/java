package ar.com.dcsys.gwt.person.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import name.pehl.piriti.json.client.JsonReader;
import name.pehl.piriti.json.client.JsonWriter;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.person.Telephone;
import ar.com.dcsys.pr.CSD;

import com.google.gwt.core.client.GWT;

public class PersonSerializer implements CSD<Person> {

	private static final Logger logger = Logger.getLogger(PersonSerializer.class.getName());
	
	public interface TelephoneReader extends JsonReader<Telephone> {};
	public static final TelephoneReader TREADER = GWT.create(TelephoneReader.class);
	
	public interface TelephoneWriter extends JsonWriter<Telephone> {};
	public static final TelephoneWriter TWRITER = GWT.create(TelephoneWriter.class);	
	
	public interface Reader extends JsonReader<Person> {}
	public static final Reader READER = GWT.create(Reader.class);
	
	public interface Writer extends JsonWriter<Person> {}
	public static final Writer WRITER = GWT.create(Writer.class);
	

	@Override
	public String toJson(Person o) {
		String d = WRITER.toJson((Person)o);
		logger.log(Level.WARNING,"PersonSerializer : " + d);
		return d;
	}

	@Override
	public Person read(String json) {
		logger.log(Level.WARNING,"PersonSerializer : " + json);
		Person person = READER.read(json);
		return person;
	}

}
