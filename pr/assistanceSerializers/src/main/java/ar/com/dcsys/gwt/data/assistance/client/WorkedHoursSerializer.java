package ar.com.dcsys.gwt.data.assistance.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.period.WorkedHours;
import ar.com.dcsys.gwt.data.utils.client.WorkedHoursUtilsSerializer;
import ar.com.dcsys.pr.CSD;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;

public class WorkedHoursSerializer implements CSD<WorkedHours> {

	public static final Logger logger = Logger.getLogger(WorkedHoursSerializer.class.getName());
	
	@Override
	public String toJson(WorkedHours o) {
		JSONObject jo = WorkedHoursUtilsSerializer.toJson(o);
		return jo.toString();
	}

	@Override
	public WorkedHours read(String json) {
		logger.log(Level.WARNING,"WorkedHoursSerializer : " + json);
		try {
			JSONValue value = JSONParser.parseStrict(json);
			JSONObject obj = value.isObject();
			return WorkedHoursUtilsSerializer.read(obj);
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage());
			return null;
		}
	}
	
}
