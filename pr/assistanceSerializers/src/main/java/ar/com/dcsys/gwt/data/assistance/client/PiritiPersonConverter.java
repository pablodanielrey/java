package ar.com.dcsys.gwt.data.assistance.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import name.pehl.piriti.converter.client.AbstractConverter;
import ar.com.dcsys.data.person.Person;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;

public class PiritiPersonConverter extends AbstractConverter<Person> {

	private static final Logger logger = Logger.getLogger(PiritiPersonConverter.class.getName());
	
	@Override
	public Person convert(String json) {
		try {
			JSONValue value = JSONParser.parseStrict(json);
			JSONObject personObj = value.isObject();
			return PersonUtilsSerializer.read(personObj);
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage());
			return null;
		}
	}
	
	@Override
	public String serialize(Person value) {
		JSONObject jo = PersonUtilsSerializer.toJson(value);
		return jo.toString();
	}
}