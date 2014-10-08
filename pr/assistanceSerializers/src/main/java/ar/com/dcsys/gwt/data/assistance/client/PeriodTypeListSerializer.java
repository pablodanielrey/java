package ar.com.dcsys.gwt.data.assistance.client;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.period.PeriodType;
import ar.com.dcsys.gwt.data.utils.client.PeriodTypeUtilsSerializers;
import ar.com.dcsys.pr.CSD;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;

public class PeriodTypeListSerializer implements CSD<List<PeriodType>> {

	private static final Logger logger = Logger.getLogger(PeriodTypeListSerializer.class.getName());
	
	
	@Override
	public String toJson(List<PeriodType> o) {
		if (o == null) {
			logger.log(Level.WARNING, "periodstype == null");
			return "";
		}		
		JSONArray typesObj = PeriodTypeUtilsSerializers.toJsonArray(o);		
		return typesObj.toString();
	}

	@Override
	public List<PeriodType> read(String json) {
		logger.log(Level.WARNING,"PeriodTypeListSerializer : " + json);
		try {
			JSONValue value = JSONParser.parseStrict(json);
			JSONObject obj = value.isObject();
			JSONArray array = obj.get("list").isArray();
			
			if (array == null) {
				return null;
			}
			
			List<PeriodType> types = PeriodTypeUtilsSerializers.read(array);
			return types;
			
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage());
			return null;
		}
	}

}
