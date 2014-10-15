package ar.com.dcsys.gwt.person.client;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.gwt.data.utils.client.PersonUtilsSerializer;
import ar.com.dcsys.pr.CSD;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;

public class PersonListSerializer implements CSD<List<Person>> {

	private static final Logger logger = Logger.getLogger(PersonListSerializer.class.getName());
	
	@Override
	public String toJson(List<Person> o) {
		if (o == null) {
			logger.log(Level.WARNING,"persons == null");
			return "";
		}
		
		JSONArray personsObj = PersonUtilsSerializer.toJsonArray(o);
		JSONObject obj = new JSONObject();
		obj.put("list", personsObj);
		return obj.toString();
	}

	@Override
	public List<Person> read(String json) {
		logger.log(Level.WARNING, "PersonListSerializer : " + json);
		try {
			JSONValue value = JSONParser.parseStrict(json);
			JSONObject obj = value.isObject();
			JSONArray personArray = obj.get("list").isArray();
			
			if (personArray == null) {
				return null;
			}
			
			List<Person> persons = PersonUtilsSerializer.read(personArray);						
			return persons;
			
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage());
			return null;
		}
	}
}
