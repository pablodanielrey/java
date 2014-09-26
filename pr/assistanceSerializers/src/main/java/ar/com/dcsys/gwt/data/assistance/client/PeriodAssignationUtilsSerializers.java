package ar.com.dcsys.gwt.data.assistance.client;

import java.util.Date;

import ar.com.dcsys.data.period.PeriodAssignation;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

public class PeriodAssignationUtilsSerializers {

	private static final DateTimeFormat df = DateTimeFormat.getFormat("HH:mm:ss dd/MM/yyyy");
	
	public static JSONObject toJson(PeriodAssignation o) {
		JSONObject periodAssignationObj = new JSONObject();
		
		String id = o.getId();
		if (id != null) {
			periodAssignationObj.put("id", new JSONString(id));
		}
		
		if (o.getStart() != null) {
			String start = df.format(o.getStart());
			periodAssignationObj.put("start", new JSONString(start));
		}
		
		if (o.getPerson() != null) {
			periodAssignationObj.put("person",PersonUtilsSerializer.toJson(o.getPerson()));
		}
		
		if (o.getType() != null) { 
			periodAssignationObj.put("type", PeriodTypeUtilsSerializers.toJson(o.getType()));
		}
		
		return periodAssignationObj;		
	}
	
	public static PeriodAssignation read(JSONObject periodAssignationObj) {
		if (periodAssignationObj != null) {
			PeriodAssignation periodAssignation = new PeriodAssignation();
			
			JSONValue idValue = periodAssignationObj.get("id");
			if (idValue != null) {
				String id = idValue.isString().stringValue();
				periodAssignation.setId(id);
			}
				
			JSONValue personValue = periodAssignationObj.get("person");
			if (personValue != null) {
				JSONObject personObj = personValue.isObject();
				periodAssignation.setPerson(PersonUtilsSerializer.read(personObj));
			}
			
			JSONValue startValue = periodAssignationObj.get("start");
			if (startValue != null) {
				String startStr = startValue.isString().stringValue();
				Date start = df.parse(startStr);
				periodAssignation.setStart(start);
			}
			
			JSONValue typeValue = periodAssignationObj.get("type");
			if (typeValue != null) {
				JSONObject typeObj = typeValue.isObject();
				periodAssignation.setType(PeriodTypeUtilsSerializers.read(typeObj));
			}
			
			return periodAssignation;
			
		} else {
			return null;
		}
		
		
	}

}
