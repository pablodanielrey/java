package ar.com.dcsys.gwt.person.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.gwt.data.utils.client.PersonUtilsSerializer;
import ar.com.dcsys.pr.CSD;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;

public class PersonSerializer implements CSD<Person> {

	private static final Logger logger = Logger.getLogger(PersonSerializer.class.getName());

	@Override
	public String toJson(Person o) {
		JSONObject jo = PersonUtilsSerializer.toJson(o);
		return jo.toString();
	}

	@Override
	public Person read(String json) {
		logger.log(Level.WARNING,"PersonSerializer : " + json);
		try {
			JSONValue value = JSONParser.parseStrict(json);
			JSONObject personObj = value.isObject();
			return PersonUtilsSerializer.read(personObj);
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage());
			return null;
		}
	}

}
