package ar.com.dcsys.assistance.server;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import ar.com.dcsys.data.common.Days;
import ar.com.dcsys.data.period.PeriodAssignation;
import ar.com.dcsys.data.period.PeriodType;
import ar.com.dcsys.data.period.PeriodTypeDailyParams;
import ar.com.dcsys.data.period.PeriodTypeNull;
import ar.com.dcsys.data.period.PeriodTypeSystem;
import ar.com.dcsys.data.period.PeriodTypeWatchman;
import ar.com.dcsys.pr.CSD;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class PeriodAssignationSerializer implements CSD<PeriodAssignation> {

	private static final Logger logger = Logger.getLogger(PeriodAssignationSerializer.class.getName());
	
	private final Gson gson = (new GsonBuilder()).registerTypeAdapter(PeriodType.class, new PeriodTypeDeserializer())
												 .registerTypeAdapter(PeriodType.class, new PeriodTypeSerializer())
												 .setDateFormat("HH:mm:ss dd/MM/yyyy")
												 .create();
	
	private class PeriodTypeSerializer implements JsonSerializer<PeriodType> {
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
	
	
	private class PeriodTypeDeserializer implements JsonDeserializer<PeriodType> {
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
	
	@Override
	public PeriodAssignation read(String json) {
		logger.warning("PeriodAssignationSerializer : " + json);
		PeriodAssignation pa = gson.fromJson(json, PeriodAssignation.class);
		return pa;
	}
	
	@Override
	public String toJson(PeriodAssignation o) {
		String d = gson.toJson(o);
		logger.warning("PeriodAssignationSerializer : " + d);
		return d;
	}
	
}
