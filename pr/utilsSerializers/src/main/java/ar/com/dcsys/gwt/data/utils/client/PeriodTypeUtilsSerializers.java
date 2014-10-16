package ar.com.dcsys.gwt.data.utils.client;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ar.com.dcsys.data.common.Days;
import ar.com.dcsys.data.period.PeriodType;
import ar.com.dcsys.data.period.PeriodTypeDailyParams;
import ar.com.dcsys.data.period.PeriodTypeNull;
import ar.com.dcsys.data.period.PeriodTypeSystem;
import ar.com.dcsys.data.period.PeriodTypeWatchman;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

public class PeriodTypeUtilsSerializers {
	
	public static JSONArray toJsonArray(List<PeriodType> types) {
		if (types == null) {
			return null;
		}
		JSONArray ptArray = new JSONArray();
		for (int i = 0; i < types.size(); i++) {
			ptArray.set(i, toJson(types.get(i)));
		}
		
		return ptArray;
	}
	
	public static JSONObject toJson(PeriodType type) {
		
		JSONObject typeObj = new JSONObject();
		
		String id = type.getId();
		if (id != null) {
			typeObj.put("id",new JSONString(id));
		}
		
		String name = type.getName();
		if (name != null) {
			typeObj.put("name", new JSONString(name));
		}
		
		String typeStr = type.getType();
		if (typeStr != null) {
			typeObj.put("type", new JSONString(typeStr));
		
			if (type instanceof PeriodTypeDailyParams) {
				PeriodTypeDailyParams tDaily = (PeriodTypeDailyParams)type;
				for (Days d : Days.values()) {
					boolean value = (tDaily.getDays().contains(d));
					typeObj.put(d.toString(), JSONBoolean.getInstance(value));
				}
			}
		}
		
		return typeObj;
	}
	
	public static List<PeriodType> read(JSONArray ptArray) {
		
		List<PeriodType> types = new ArrayList<>();
		
		if (ptArray != null) {
			for (int i = 0; i < ptArray.size(); i++) {
				JSONObject ptObject = ptArray.get(i).isObject();
				if (ptObject != null) {
					types.add(read(ptObject));
				}
			}
		}
		
		return types;
	}
	
	public static PeriodType read(JSONObject typeObj) {
		
		if (typeObj == null) {
			return null;
		}
		
		JSONValue tVal = typeObj.get("type");
		if (tVal == null) {
			return null;		
		}
		
		String type = tVal.isString().stringValue();
		
		String id = null;
		JSONValue idValue = typeObj.get("id");
		if (idValue != null) {
			id = idValue.isString().stringValue();
		}
			
		if (PeriodTypeNull.class.getName().equals(type)) {
			PeriodTypeNull ptn = new PeriodTypeNull();
			ptn.setId(id);
			return ptn;
		}
		
		if (PeriodTypeWatchman.class.getName().equals(type)) {
			PeriodTypeWatchman ptw = new PeriodTypeWatchman();
			ptw.setId(id);
			return ptw;			
		}

		if (PeriodTypeSystem.class.getName().equals(type)) {
			PeriodTypeSystem pts = new PeriodTypeSystem();
			pts.setId(id);
			return pts;
		}
		
		if (PeriodTypeDailyParams.class.getName().equals(type)) {
			PeriodTypeDailyParams ptd = new PeriodTypeDailyParams();
			ptd.setId(id);
			Set<Days> days = new HashSet<>();
						
			for(Days d : Days.values()) {
				JSONValue dVal = typeObj.get(d.toString());
				if (dVal != null) {
					Boolean v = dVal.isBoolean().booleanValue();
					if (v) {
						days.add(d);
					}
				}
			}
			
			ptd.setDays(days);
			return ptd;
		}
 		
		
		return null;
	}
	
}
