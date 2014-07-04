package ar.com.dcsys.gwt.person.client;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.pr.CSD;

import com.google.gwt.core.shared.GWT;

public class PersonListSerializer implements CSD<List<Person>> {

	private static final Logger logger = Logger.getLogger(PersonListSerializer.class.getName());
	
	interface Reader extends name.pehl.piriti.json.client.JsonReader<PersonListContainer> { }
	private static final Reader READER = GWT.create(Reader.class);
	
	interface Writer extends name.pehl.piriti.json.client.JsonWriter<PersonListContainer> { }
	private static final Writer WRITER = GWT.create(Writer.class);

	
	@Override
	public String toJson(List<Person> o) {
		PersonListContainer sc = new PersonListContainer();
		sc.list = o;
		String d = WRITER.toJson(sc);
		logger.log(Level.WARNING,"piriti : " + d);
		return d;
	}

	@Override
	public List<Person> read(String json) {
		logger.log(Level.WARNING,"piriti : " + json);
		PersonListContainer sc = READER.read(json);
		return sc.list;
	}
}
