package ar.com.dcsys.gwt.data.assistance.client;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.justification.Justification;
import ar.com.dcsys.gwt.data.utils.client.JustificationUtilsSerializer;
import ar.com.dcsys.pr.CSD;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;

public class JustificationListSerializer implements CSD<List<Justification>> {
	
	private static final Logger logger = Logger.getLogger(JustificationListSerializer.class.getName());

	@Override
	public String toJson(List<Justification> o) {
		if (o == null) {
			logger.log(Level.WARNING,"justifications == null");
			return "";
		}
		
		JSONArray array = JustificationUtilsSerializer.toJsonArray(o);
		return array.toString();
	}
	
	@Override
	public List<Justification> read(String json) {
		logger.log(Level.WARNING,"JustificationListSerializer : " + json);
		try {
			JSONValue value = JSONParser.parseStrict(json);
			JSONObject obj = value.isObject();
			JSONArray array = obj.get("list").isArray();
			
			if (array == null) {
				return null;
			}
			
			List<Justification> justifications = JustificationUtilsSerializer.read(array);
			return justifications;
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage());
			return null;
		}
	}

}
