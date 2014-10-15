package ar.com.dcsys.assistance.server.common;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

import ar.com.dcsys.data.common.Days;
import ar.com.dcsys.data.period.PeriodType;
import ar.com.dcsys.data.period.PeriodTypeDailyParams;
import ar.com.dcsys.data.period.PeriodTypeNull;
import ar.com.dcsys.data.period.PeriodTypeSystem;
import ar.com.dcsys.data.period.PeriodTypeWatchman;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class PeriodTypeJsonDeserializer  implements JsonDeserializer<PeriodType> {
	@Override
	public PeriodType deserialize(JsonElement je, Type type,JsonDeserializationContext context) throws JsonParseException {
		JsonObject jo = je.getAsJsonObject();
		
		JsonElement idElem = jo.get("id");
		String id;
		if (idElem != null) {
			id = idElem.getAsString();
		} else {
			id = null;
		}
		
		JsonElement typeElem = jo.get("type");
		if (typeElem == null) {
			return null;
		}
		String typeStr = typeElem.getAsString();
		
		if (PeriodTypeNull.class.getName().equals(typeStr)) {
			PeriodTypeNull t = new PeriodTypeNull();
			t.setId(id);
			return t;
		}
		
		if (PeriodTypeSystem.class.getName().equals(typeStr)) {
			PeriodTypeSystem t = new PeriodTypeSystem();
			t.setId(id);
			return t;				
		}
		
		if (PeriodTypeWatchman.class.getName().equals(typeStr)) {
			PeriodTypeWatchman t = new PeriodTypeWatchman();
			t.setId(id);
			return t;				
		}
		
		if (PeriodTypeDailyParams.class.getName().equals(typeStr)) {
			
			PeriodTypeDailyParams t = new PeriodTypeDailyParams();
			t.setId(id);
			
			Set<Days> days = new HashSet<>();
			
			for(Days d : Days.values()) {
				boolean value = jo.get(d.toString()).getAsBoolean();
				if (value) {
					days.add(d);
				}
			}
			
			t.setDays(days);
			return t;				
		}
		
		return null;
	}

}
