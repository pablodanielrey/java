package ar.com.dcsys.gwt.data.utils.client;

import java.util.ArrayList;
import java.util.List;

import ar.com.dcsys.data.log.AttLog;
import ar.com.dcsys.data.period.WorkedHours;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;

public class WorkedHoursUtilsSerializer {
	
	public static JSONArray toJsonArray(List<WorkedHours> whs) {
		if (whs == null) {
			return null;
		}
		JSONArray whsObj = new JSONArray();
		for (int i = 0; i < whs.size(); i++) {
			whsObj.set(i, toJson(whs.get(i)));
		}
		
		return whsObj;
	}
	
	public static JSONObject toJson(WorkedHours wh) {
		
		JSONObject whObj = new JSONObject();
		
		//inLog
		AttLog inLog = wh.getInLog();
		if (inLog != null) {
			whObj.put("inLog", AttLogUtilsSerializer.toJson(inLog));
		}
		
		//outLog
		AttLog outLog = wh.getOutLog();
		if (outLog != null) {
			whObj.put("outLog", AttLogUtilsSerializer.toJson(outLog));
		}
		
		//logs
		List<AttLog> logs = wh.getLogs();
		JSONArray logsObj = AttLogUtilsSerializer.toJsonArray(logs);
		if (logsObj != null) {
			whObj.put("logs", logsObj);
		}
		
		//workedMilis
		Long workedMilis = wh.getWorkedMilis();
		if (workedMilis != null) {
			whObj.put("workedMilis", new JSONNumber(workedMilis));
		}
		
		return whObj;
	}
	
	public static List<WorkedHours> read(JSONArray whArray) {
		
		List<WorkedHours> whs = new ArrayList<>();
		
		if (whArray != null) {
			for (int i = 0; i < whArray.size(); i++) {
				JSONObject whObj = whArray.get(i).isObject();
				if (whObj != null) {
					whs.add(read(whObj));
				}
			}
		}
		
		return whs;
	}
	
	public static WorkedHours read(JSONObject whObj) {
		
		if (whObj == null) {
			return null;
		}
		
		WorkedHours wh = new WorkedHours();
		
		//inLog
		JSONValue inLogVal = whObj.get("inLog");
		if (inLogVal != null) {
			JSONObject inLogObj = inLogVal.isObject();
			if (inLogObj != null) {
				wh.setInLog(AttLogUtilsSerializer.read(inLogObj));
			}
		}
		
		//outLog
		JSONValue outLogVal = whObj.get("outLog");
		if (outLogVal != null) {
			JSONObject outLogObj = outLogVal.isObject();
			if (outLogObj != null) {
				wh.setOutLog(AttLogUtilsSerializer.read(outLogObj));
			}
		}
		
		//logs
		JSONValue logsVal = whObj.get("logs");
		if (logsVal != null) {
			JSONArray logsArray = logsVal.isArray();
			if (logsArray != null) {
				List<AttLog> logs = AttLogUtilsSerializer.read(logsArray);
				wh.setLogs(logs);
			}
		}
		
		//workedMilis
		JSONValue workedMilisVal = whObj.get("workedMilis");
		if (workedMilisVal != null) {
			double workedMilisDouble = workedMilisVal.isNumber().doubleValue();
			wh.setWorkedMilis((long)workedMilisDouble);
		}
		
		return wh;
	}

}
