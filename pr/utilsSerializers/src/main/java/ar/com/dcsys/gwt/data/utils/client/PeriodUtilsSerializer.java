package ar.com.dcsys.gwt.data.utils.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.com.dcsys.data.period.Period;
import ar.com.dcsys.data.period.WorkedHours;
import ar.com.dcsys.data.person.Person;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

public class PeriodUtilsSerializer {
	
	private static final DateTimeFormat df = DateTimeFormat.getFormat("HH:mm:ss dd/MM/yyyy");

	public static JSONArray toJsonArray(List<Period> periods) {
		if (periods == null) {
			return null;
		}
		JSONArray periodsObj = new JSONArray();
		for (int i = 0; i < periods.size(); i++) {
			periodsObj.set(i, toJson(periods.get(i)));
		}
		return periodsObj;
	}
	
	public static JSONObject toJson(Period period) {
		
		JSONObject periodObj = new JSONObject();
		
		//person
		Person person = period.getPerson();
		if (person != null) {
			periodObj.put("person", PersonUtilsSerializer.toJson(person));
		}
		
		//start
		Date start = period.getStart();
		if (start != null) {
			String startStr = df.format(start);
			periodObj.put("start",new JSONString(startStr));
		} 
		
		//end
		Date end = period.getEnd();
		if (end != null) {
			String endStr = df.format(end);
			periodObj.put("end",new JSONString(endStr));
		} 
		
		//workedHours
		List<WorkedHours> workedHours = period.getWorkedHours();
		JSONArray workedHoursObj = WorkedHoursUtilsSerializer.toJsonArray(workedHours);
		if (workedHoursObj != null) {
			periodObj.put("workedHours", workedHoursObj);
		}
		
		return periodObj;
	}
	
	public static List<Period> read(JSONArray periodsArray) {
		
		List<Period> periods = new ArrayList<>();
		
		if (periodsArray != null) {
			for (int i = 0; i < periodsArray.size(); i++) {
				JSONObject periodObj = periodsArray.get(i).isObject();
				if (periodObj != null) {
					periods.add(read(periodObj));
				}
			}
		}
		
		return periods;
	}
	
	public static Period read(JSONObject periodObj) {
		
		if (periodObj == null) {
			return null;
		}
		
		Period period = new Period();
		
		//person
		JSONValue personVal = periodObj.get("person");
		if (personVal != null) {
			JSONObject personObj = personVal.isObject();
			if (personObj != null) {
				period.setPerson(PersonUtilsSerializer.read(personObj));
			}
		}		
		
		//start
		JSONValue startVal = periodObj.get("start");
		if (startVal != null) {
			String startStr = startVal.isString().stringValue();
			Date start = df.parse(startStr);
			period.setStart(start);
		}
		
		//end
		JSONValue endVal = periodObj.get("end");
		if (endVal != null) {
			String endStr = endVal.isString().stringValue();
			Date end = df.parse(endStr);
			period.setEnd(end);
		}
		
		//workedHours
		JSONValue workedHoursVal = periodObj.get("workedHours");
		if (workedHoursVal != null) {
			JSONArray workedHoursArray = workedHoursVal.isArray();
			if (workedHoursArray != null) {
				List<WorkedHours> whs = WorkedHoursUtilsSerializer.read(workedHoursArray);
				period.setWorkedHours(whs);
			}
		}
		
		return period;
	}
}
