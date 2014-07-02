package ar.com.dcsys.gwt.data.assistance.client;

import name.pehl.piriti.converter.client.Converter;
import name.pehl.piriti.json.client.JsonReader;
import name.pehl.piriti.json.client.JsonWriter;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.person.Person;

import com.google.gwt.core.shared.GWT;

public class PiritiPersonConverter implements Converter<Person> {


	public interface PersonReader extends JsonReader<Person> {};
	public static final PersonReader PREADER = GWT.create(PersonReader.class);

	public interface PersonWriter extends JsonWriter<Person> {}
	public static final PersonWriter PWRITER = GWT.create(PersonWriter.class);
	
	@Override
	public Person convert(String value) {
		return PREADER.read(value);
	}
	@Override
	public String serialize(Person value) {
		String d = PWRITER.toJson((Person)value);
		return d;
	}
}