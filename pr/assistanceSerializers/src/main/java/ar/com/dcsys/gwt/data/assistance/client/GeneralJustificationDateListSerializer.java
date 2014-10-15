package ar.com.dcsys.gwt.data.assistance.client;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.justification.GeneralJustificationDate;
import ar.com.dcsys.gwt.data.utils.client.GeneralJustificationDateUtilsSerializer;
import ar.com.dcsys.pr.CSD;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;

public class GeneralJustificationDateListSerializer implements CSD<List<GeneralJustificationDate>> {
	
	private static final Logger logger = Logger.getLogger(GeneralJustificationDateListSerializer.class.getName());

	@Override
	public String toJson(List<GeneralJustificationDate> o) {
		if (o == null) {
			logger.log(Level.WARNING, "generaljustificationdates == null");
			return "";
		}
		
		JSONArray array = GeneralJustificationDateUtilsSerializer.toJsonArray(o);
		JSONObject obj = new JSONObject();
		obj.put("list", array);
		return obj.toString();
	}
	
	@Override
	public List<GeneralJustificationDate> read(String json) {
		logger.log(Level.WARNING, "GeneralJustificationDateSerializer : " + json);
		try {
			JSONValue value = JSONParser.parseStrict(json);
			JSONObject obj = value.isObject();
			JSONArray array = obj.get("list").isArray();
			
			if (array == null) {
				return null;
			}
			
			List<GeneralJustificationDate> gjds = GeneralJustificationDateUtilsSerializer.read(array);
			return gjds;
			
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage());
			return null;
		}
	}

}
