package ar.com.dcsys.gwt.data.assistance.client;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.period.Period;
import ar.com.dcsys.gwt.data.utils.client.PeriodUtilsSerializer;
import ar.com.dcsys.pr.CSD;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;

public class PeriodListSerializer implements CSD<List<Period>> {
	
	private static final Logger logger = Logger.getLogger(PeriodListSerializer.class.getName());

	@Override
	public String toJson(List<Period> o) {
		if (o == null) {
			logger.log(Level.WARNING,"periods == null");
			return "";
		}
		
		JSONArray array = PeriodUtilsSerializer.toJsonArray(o);
		return array.toString();
	}
	
	@Override
	public List<Period> read(String json) {
		logger.log(Level.WARNING,"PeriodListSerializer : " + json);
		try {
			JSONValue value = JSONParser.parseStrict(json);
			JSONObject obj = value.isObject();
			JSONArray array = obj.get("list").isArray();
			
			if (array == null) {
				return null;
			}
			
			List<Period> periods = PeriodUtilsSerializer.read(array);
			return periods;
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage());
			return null;
		}
	}

}
