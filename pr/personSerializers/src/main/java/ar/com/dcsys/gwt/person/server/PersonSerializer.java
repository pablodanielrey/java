package ar.com.dcsys.gwt.person.server;

import java.util.logging.Logger;

import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.pr.CSD;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class PersonSerializer implements CSD<Person> {

	private static final Logger logger = Logger.getLogger(PersonSerializer.class.getName());
	
	private final Gson gson = (new GsonBuilder()).create();
	
	@Override
	public Person read(String json) {
		logger.warning("PersonSerializer : " + json);
		Person person = gson.fromJson(json, Person.class);
		return person;
	}
	
	@Override
	public String toJson(Person o) {
		String d = gson.toJson(o);
		logger.warning("PersonSerializer : " + d);
		return d;
	}
	
}
