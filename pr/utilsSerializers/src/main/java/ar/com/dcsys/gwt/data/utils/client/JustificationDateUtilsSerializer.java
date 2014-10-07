package ar.com.dcsys.gwt.data.utils.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.com.dcsys.data.justification.Justification;
import ar.com.dcsys.data.justification.JustificationDate;
import ar.com.dcsys.data.person.Person;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

public class JustificationDateUtilsSerializer {

	private static final DateTimeFormat df = DateTimeFormat.getFormat("HH:mm:ss dd/MM/yyyy");
	
	public static JSONArray toJsonArray(List<JustificationDate> jds) {
		if (jds == null) {
			return null;
		}
		JSONArray jdsObj = new JSONArray();
		for (int i = 0; i < jds.size(); i++) {
			jdsObj.set(i, toJson(jds.get(i)));
		}
		return jdsObj;
	}
	
	public static JSONObject toJson(JustificationDate jd) {
		
		JSONObject jdObj = new JSONObject();
		
		//id
		String id = jd.getId();
		if (id != null) {
			jdObj.put("id", new JSONString(id));
		}
		
		//justification
		Justification justification = jd.getJustification();
		if (justification != null) {
			jdObj.put("justification", JustificationUtilsSerializer.toJson(justification));
		}
		
		//person
		Person person = jd.getPerson();
		if (person != null) {
			jdObj.put("person", PersonUtilsSerializer.toJson(person));
		}
		
		//start
		Date start = jd.getStart();
		if (start != null) {
			String startStr = df.format(start);
			jdObj.put("start",new JSONString(startStr));
		}
		
		//end
		Date end = jd.getEnd();
		if (end != null) {
			String endStr = df.format(end);
			jdObj.put("end", new JSONString(endStr));
		}
		
		//notes
		String notes = jd.getNotes();
		if (notes != null) {
			jdObj.put("notes", new JSONString(notes));
		}
		
		return jdObj;
	}
	
	public static List<JustificationDate> read(JSONArray jdArray) {
		
		List<JustificationDate> jds = new ArrayList<>();
		
		if (jdArray != null) {
			for (int i = 0; i < jdArray.size(); i++) {
				JSONObject jdObject = jdArray.get(i).isObject();
				if (jdObject != null) {
					jds.add(read(jdObject));
				}
			}
		}
		
		return jds;
	}
	
	public static JustificationDate read(JSONObject jdObj) {
		
		if (jdObj == null) {
			return null;
		}
		
		JustificationDate jd = new JustificationDate();
		
		//id
		JSONValue idVal = jdObj.get("id");
		if (idVal != null) {
			String id = idVal.isString().stringValue();
			jd.setId(id);
		}
		
		//person
		JSONValue personVal = jdObj.get("person");
		if (personVal != null) {
			JSONObject personObj = personVal.isObject();
			if (personObj != null) {
				jd.setPerson(PersonUtilsSerializer.read(personObj));
			}
		}
		
		//justification
		JSONValue justificationVal = jdObj.get("justification");
		if (justificationVal != null) {
			JSONObject justificationObj = justificationVal.isObject();
			if (justificationObj != null) {
				jd.setJustification(JustificationUtilsSerializer.read(justificationObj));
			}
		}
		
		//start
		JSONValue startVal = jdObj.get("start");
		if (startVal != null) {
			String startStr = startVal.isString().stringValue();
			Date start = df.parse(startStr);
			jd.setStart(start);
		}
		
		//end
		JSONValue endVal = jdObj.get("end");
		if (endVal != null) {
			String endStr = endVal.isString().stringValue();
			Date end = df.parse(endStr);
			jd.setEnd(end);
		}		
		
		//notes
		JSONValue notesVal = jdObj.get("notes");
		if (notesVal != null) {
			String notes = notesVal.isString().stringValue();
			jd.setNotes(notes);
		}
		
		return jd;
	}
}
