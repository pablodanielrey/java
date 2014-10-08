package ar.com.dcsys.gwt.data.assistance.client;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.period.WorkedHours;
import ar.com.dcsys.gwt.data.utils.client.WorkedHoursUtilsSerializer;
import ar.com.dcsys.pr.CSD;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;

public class WorkedHoursListSerializer implements CSD<List<WorkedHours>> {

	private static final Logger logger = Logger.getLogger(WorkedHoursListSerializer.class.getName());

	@Override
	public String toJson(List<WorkedHours> o) {
		if (o == null) {
			logger.log(Level.WARNING,"workedhours == null");
			return "";
		}
		
		JSONArray array = WorkedHoursUtilsSerializer.toJsonArray(o);
		return array.toString();
	}

	@Override
	public List<WorkedHours> read(String json) {
		logger.log(Level.WARNING,"WorkedHoursListSerializer : " + json);
		try {
			JSONValue value = JSONParser.parseStrict(json);
			JSONObject obj = value.isObject();
			JSONArray array = obj.get("list").isArray();
			
			if (array == null) {
				return null;
			}
			
			List<WorkedHours> whs = WorkedHoursUtilsSerializer.read(array);
			return whs;
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage());
			return null;
		}
	}
	

}
