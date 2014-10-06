package ar.com.dcsys.gwt.person.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.person.PersonType;
import ar.com.dcsys.gwt.data.utils.client.PersonTypeUtilsSerializer;
import ar.com.dcsys.pr.CSD;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;

public class PersonTypeSerializer implements CSD<PersonType> {
		
	private static final Logger logger = Logger.getLogger(PersonTypeSerializer.class.getName());

	@Override
	public String toJson(PersonType o) {
		if (o == null) {
			logger.log(Level.WARNING,"persontype == null");
			return "";
		}
		
		JSONObject typeObj = PersonTypeUtilsSerializer.toJson(o);
		return typeObj.toString();
	}
	
	@Override
	public PersonType read(String json) {
		logger.log(Level.WARNING, "PersonTypeSerializer : " + json);
		try {
			JSONValue value = JSONParser.parseStrict(json);
			JSONObject typeObj = value.isObject();
			
			if (typeObj != null) {
				return PersonTypeUtilsSerializer.read(typeObj);
			} else {
				return null;
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage());
			return null;
		}
	}
	
	
}
