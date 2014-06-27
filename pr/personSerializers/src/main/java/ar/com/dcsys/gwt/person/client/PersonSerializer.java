package ar.com.dcsys.gwt.person.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import name.pehl.piriti.json.client.JsonReader;
import name.pehl.piriti.json.client.JsonWriter;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.person.PersonBean;
import ar.com.dcsys.data.person.TelephoneBean;
import ar.com.dcsys.pr.CSD;

import com.google.gwt.core.client.GWT;

public class PersonSerializer implements CSD<Person> {

	private static final Logger logger = Logger.getLogger(PersonSerializer.class.getName());
	
	public interface TelephoneReader extends JsonReader<TelephoneBean> {};
	public static final TelephoneReader TREADER = GWT.create(TelephoneReader.class);
	
	public interface Reader extends JsonReader<PersonBean> {}
	public static final Reader READER = GWT.create(Reader.class);
	
	public interface TelephoneWriter extends JsonWriter<TelephoneBean> {};
	public static final TelephoneWriter TWRITER = GWT.create(TelephoneWriter.class);
	
	public interface Writer extends JsonWriter<PersonBean> {}
	public static final Writer WRITER = GWT.create(Writer.class);
	
	
	@Override
	public String toJson(Person o) {
		String d = WRITER.toJson((PersonBean)o);
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
