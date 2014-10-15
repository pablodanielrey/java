package ar.com.dcsys.assistance.server.common;

import java.lang.reflect.Type;

import ar.com.dcsys.data.common.Days;
import ar.com.dcsys.data.period.PeriodType;
import ar.com.dcsys.data.period.PeriodTypeDailyParams;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class PeriodTypeJsonSerializer  implements JsonSerializer<PeriodType> {
	@Override
	public JsonElement serialize(PeriodType type, Type typeOfSrc,JsonSerializationContext context) {
		
		JsonObject typeObj = new JsonObject();
		
		String id = type.getId();
		if (id != null) {
			typeObj.add("id", new JsonPrimitive(id));
		}
		
		String name = type.getName();
		if (name != null) {
			typeObj.add("name", new JsonPrimitive(name));
		}
		
		String typeStr = type.getType();
		if (typeStr != null) {
			typeObj.add("type", new JsonPrimitive(typeStr));
			
			if (type instanceof PeriodTypeDailyParams) {
				PeriodTypeDailyParams tDaily = (PeriodTypeDailyParams)type;
				for (Days d : Days.values()) {
					boolean value = (tDaily.getDays().contains(d));
					typeObj.add(d.toString(), new JsonPrimitive(value));
				}
			}
		}
		
		return typeObj;
	}

}
