package ar.com.dcsys.gwt.data.assistance.client;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.period.PeriodAssignation;
import ar.com.dcsys.gwt.data.utils.client.PeriodAssignationUtilsSerializers;
import ar.com.dcsys.pr.CSD;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;

public class PeriodAssignationListSerializer implements CSD<List<PeriodAssignation>> {

	private static final Logger logger = Logger.getLogger(PeriodAssignationListSerializer.class.getName());

	@Override
	public String toJson(List<PeriodAssignation> o) {
		if (o == null) {
			logger.log(Level.WARNING,"periodassignations == null");
			return "";
		}
		
		JSONArray array = PeriodAssignationUtilsSerializers.toJsonArray(o);		
		JSONObject obj = new JSONObject();
		obj.put("list", array);
		return obj.toString();
	}
	
	@Override
	public List<PeriodAssignation> read(String json) {
		logger.log(Level.WARNING,"PeriodAssignationListSerializer : " + json);
		try {
			JSONValue value = JSONParser.parseStrict(json);
			JSONObject obj = value.isObject();
			JSONArray paArray = obj.get("list").isArray();
			
			if (paArray == null) {
				return null;
			}
			
			List<PeriodAssignation> periods = PeriodAssignationUtilsSerializers.read(paArray);			
			return periods;
			
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage());
			return null;
		}
	}
}
