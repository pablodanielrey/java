package ar.com.dcsys.gwt.data.assistance.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.period.PeriodAssignation;
import ar.com.dcsys.gwt.data.utils.client.PeriodAssignationUtilsSerializers;
import ar.com.dcsys.pr.CSD;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;


public class PeriodAssignationSerializer implements CSD<PeriodAssignation> {

	private static final Logger logger = Logger.getLogger(PeriodAssignationSerializer.class.getName());
	
	@Override
	public String toJson(PeriodAssignation o) {
		JSONObject jo = PeriodAssignationUtilsSerializers.toJson(o);			
		return jo.toString();
	}

	@Override
	public PeriodAssignation read(String json) {
		logger.log(Level.WARNING,"PeriodAssignationSerializer : " + json);
		try {
			JSONValue value = JSONParser.parseStrict(json);		
			JSONObject periodAssignationObj = value.isObject();
			return PeriodAssignationUtilsSerializers.read(periodAssignationObj);					
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage());
			return null;
		}
	}

}
