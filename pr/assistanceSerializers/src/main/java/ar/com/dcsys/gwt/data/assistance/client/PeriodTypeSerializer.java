package ar.com.dcsys.gwt.data.assistance.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.period.PeriodType;
import ar.com.dcsys.pr.CSD;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;

public class PeriodTypeSerializer implements CSD<PeriodType> {
	
	public static final Logger logger = Logger.getLogger(PeriodTypeSerializer.class.getName());
	
	
	@Override
	public String toJson(PeriodType o) {
		if (o == null) {
			logger.log(Level.WARNING,"periodtype == null");
			return "";
		}
		
		JSONObject typeObj = PeriodTypeUtilsSerializers.toJson(o); 		
		return typeObj.toString(); 
	}
	
	@Override
	public PeriodType read(String json) {
		
		logger.log(Level.WARNING,"PeriodAssignationSerializer : " + json);
		try {
			JSONValue value = JSONParser.parseStrict(json);		
			JSONObject periodTypeObj = value.isObject();
			
			if (periodTypeObj != null) {
				return PeriodTypeUtilsSerializers.read(periodTypeObj);
			} else {
				return null;
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage());
			return null;
		}
	}
	
}
