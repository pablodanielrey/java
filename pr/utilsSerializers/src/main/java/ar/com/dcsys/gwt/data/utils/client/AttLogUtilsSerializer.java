package ar.com.dcsys.gwt.data.utils.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.com.dcsys.data.device.Device;
import ar.com.dcsys.data.log.AttLog;
import ar.com.dcsys.data.person.Person;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

public class AttLogUtilsSerializer {
	
	private static final DateTimeFormat df = DateTimeFormat.getFormat("HH:mm:ss dd/MM/yyyy");
	
	public static JSONArray toJsonArray(List<AttLog> logs) {
		if (logs == null) {
			return null;
		}
		JSONArray logsObj = new JSONArray();
		for (int i = 0; i < logs.size(); i++) {
			logsObj.set(i, toJson(logs.get(i)));
		}
		return logsObj;
	}
	
	public static JSONObject toJson(AttLog log) {
		
		JSONObject logObj = new JSONObject();
		
		//id
		String id = log.getId();
		if (id != null) {
			logObj.put("id", new JSONString(id));
		}
		
		//device
		Device device = log.getDevice();
		if (device != null) {
			logObj.put("device", DeviceUtilsSerializer.toJson(device));
		}
		
		//person
		Person person = log.getPerson();
		if (person != null) {
			logObj.put("person", PersonUtilsSerializer.toJson(person));
		}
		
		//date
		Date date = log.getDate();
		if (date != null) {
			String dateStr = df.format(date);
			logObj.put("date", new JSONString(dateStr));
		}
		
		//verifyMode
		Long verifyMode = log.getVerifyMode();
		if (verifyMode != null) {
			logObj.put("verifyMode", new JSONNumber(verifyMode));
		}
		
		return logObj;
	}
	
	public static List<AttLog> read(JSONArray logsArray) {
		
		List<AttLog> logs = new ArrayList<>();
		
		if (logsArray != null) {
			for (int i = 0; i < logsArray.size(); i++) {
				JSONObject logObj = logsArray.get(i).isObject();
				if (logObj != null) {
					logs.add(read(logObj));
				}
			}
		}
		
		return logs;
		
	}
	
	public static AttLog read(JSONObject logObj) {
		
		if (logObj == null) {
			return null;
		}
		
		AttLog log = new AttLog();
		
		//id
		JSONValue idVal = logObj.get("id");
		if (idVal != null) {
			String id = idVal.isString().stringValue();
			log.setId(id);
		}
		
		//device
		JSONValue deviceVal = logObj.get("device");
		if (deviceVal != null) {
			JSONObject deviceObj = deviceVal.isObject();
			if (deviceObj != null) {
				log.setDevice(DeviceUtilsSerializer.read(deviceObj));
			}
		}
		
		//person
		JSONValue personVal = logObj.get("person");
		if (personVal != null) {
			JSONObject personObj = personVal.isObject();
			if (personObj != null) {
				log.setPerson(PersonUtilsSerializer.read(personObj));
			}
		}
		
		//date
		JSONValue dateVal = logObj.get("date");
		if (dateVal != null) {
			String dateStr = dateVal.isString().stringValue();
			Date date = df.parse(dateStr);
			log.setDate(date);
		}
		
		//verifyMode
		JSONValue verifyModeVal = logObj.get("verifyMode");
		if (verifyModeVal != null) {
			double verifyModeDouble = verifyModeVal.isNumber().doubleValue();
			log.setVerifyMode((long)verifyModeDouble);
		}
				
		return log;
	}

}
