package ar.com.dcsys.gwt.data.assistance.client;

import name.pehl.piriti.json.client.JsonInstanceCreator;
import ar.com.dcsys.data.period.PeriodType;
import ar.com.dcsys.data.period.PeriodTypeDailyParams;
import ar.com.dcsys.data.period.PeriodTypeNull;
import ar.com.dcsys.data.period.PeriodTypeSystem;
import ar.com.dcsys.data.period.PeriodTypeWatchman;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

public class PeriodTypeCreator extends JsonInstanceCreator<PeriodType> {

	@Override
	public PeriodType newInstance(JSONValue context) {
		PeriodType type = null;
		
		JSONObject jsonObject = context.isObject();
		if (jsonObject == null) {
			return null;
		}
		
		JSONValue typeValue = jsonObject.get("type");
		if (typeValue != null) {			
			JSONString typeString = typeValue.isString();
			if (typeString != null) {
				
				String typeStr = typeString.stringValue();
				
				if (typeStr.equals(PeriodTypeDailyParams.class.getName())) {					
					type = new PeriodTypeDailyParams();
				} else if (typeStr.equals(PeriodTypeSystem.class.getName())) {
					type = new PeriodTypeSystem();
				} else if (typeStr.equals(PeriodTypeWatchman.class.getName())) {
					type = new PeriodTypeWatchman();
				} else if (typeStr.equals(PeriodTypeNull.class.getName())) {
					type = new PeriodTypeNull();
				}
			}
		}
		
		return type;
	}
}
