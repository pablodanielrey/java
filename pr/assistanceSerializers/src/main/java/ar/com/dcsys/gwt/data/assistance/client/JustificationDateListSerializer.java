package ar.com.dcsys.gwt.data.assistance.client;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.justification.JustificationDate;
import ar.com.dcsys.gwt.data.utils.client.JustificationDateUtilsSerializer;
import ar.com.dcsys.pr.CSD;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;

public class JustificationDateListSerializer implements CSD<List<JustificationDate>> {
	
	private static final Logger logger = Logger.getLogger(JustificationDateListSerializer.class.getName());

	@Override
	public String toJson(List<JustificationDate> o) {
		if (o == null) {
			logger.log(Level.WARNING,"justificationdates = null");
			return "";
		}
		
		JSONArray array = JustificationDateUtilsSerializer.toJsonArray(o);
		return array.toString();
	}
	
	@Override
	public List<JustificationDate> read(String json) {
		logger.log(Level.WARNING,"JustificationDateListSerializer : " + json);
		try {
			JSONValue value = JSONParser.parseStrict(json);
			JSONObject obj = value.isObject();
			JSONArray array = obj.get("list").isArray();
			
			if (array == null) {
				return null;
			}
			
			List<JustificationDate> jds = JustificationDateUtilsSerializer.read(array);
			return jds;
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage());
			return null;
		}
	}

}
