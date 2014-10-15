package ar.com.dcsys.gwt.person.client;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.person.PersonType;
import ar.com.dcsys.gwt.data.utils.client.PersonTypeUtilsSerializer;
import ar.com.dcsys.pr.CSD;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;

public class PersonTypeListSerializer implements CSD<List<PersonType>> {

	private static final Logger logger = Logger.getLogger(PersonTypeListSerializer.class.getName());
		
	@Override
	public String toJson(List<PersonType> o) {
		if (o == null) {
			logger.log(Level.WARNING,"persontypes == null");
			return "";
		}
		
		JSONArray array = PersonTypeUtilsSerializer.toJsonArray(o);		
		JSONObject obj = new JSONObject();
		obj.put("list", array);
		return obj.toString();
	}

	@Override
	public List<PersonType> read(String json) {
		logger.log(Level.WARNING,"PersonTypeListSerliazer : " + json);
		try {
			JSONValue value = JSONParser.parseStrict(json);
			JSONObject obj = value.isObject();
			JSONArray typesArray = obj.get("list").isArray();
			
			if (typesArray == null) {
				return null;
			}
			
			List<PersonType> types = PersonTypeUtilsSerializer.read(typesArray);			
			return types;
			
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage());
			return null;
		}
	}

}
