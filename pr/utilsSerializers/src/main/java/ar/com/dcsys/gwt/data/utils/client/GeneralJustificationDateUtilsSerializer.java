package ar.com.dcsys.gwt.data.utils.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.com.dcsys.data.justification.GeneralJustificationDate;
import ar.com.dcsys.data.justification.Justification;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

public class GeneralJustificationDateUtilsSerializer {
	
	private static final DateTimeFormat df = DateTimeFormat.getFormat("HH:mm:ss dd/MM/yyyy");
	
	public static JSONArray toJsonArray(List<GeneralJustificationDate> gjds) {
		if (gjds == null) {
			return null;
		}
		JSONArray gjdsObj = new JSONArray();
		for (int i = 0; i < gjds.size(); i++) {
			gjdsObj.set(i, toJson(gjds.get(i)));
		}
		
		return gjdsObj;
	}
	
	public static JSONObject toJson(GeneralJustificationDate gjd) {
		
		JSONObject gjdObj = new JSONObject();
		
		//id
		String id = gjd.getId();
		if (id != null) {
			gjdObj.put("id", new JSONString(id));
		}
		
		//justification
		Justification justification = gjd.getJustification();
		if (justification != null) {
			gjdObj.put("justification", JustificationUtilsSerializer.toJson(justification));
		}
		
		//start
		Date start = gjd.getStart();
		if (start != null) {
			String startStr = df.format(start);
			gjdObj.put("start",new JSONString(startStr));
		}
		
		//end
		Date end = gjd.getEnd();
		if (end != null) {
			String endStr = df.format(end);
			gjdObj.put("end", new JSONString(endStr));
		}
		
		//notes
		String notes = gjd.getNotes();
		if (notes != null) {
			gjdObj.put("notes", new JSONString(notes));
		}
		
		return gjdObj;
	}
	
	public static List<GeneralJustificationDate> read(JSONArray gjdArray) {
		
		List<GeneralJustificationDate> gjds = new ArrayList<>();
		
		if (gjdArray != null) {
			for (int i = 0; i < gjdArray.size(); i++) {
				JSONObject gjdObject = gjdArray.get(i).isObject();
				if (gjdObject != null) {
					gjds.add(read(gjdObject));
				}
			}
		}
		
		return gjds;
	}
	
	public static GeneralJustificationDate read(JSONObject gjdObj) {
		
		if (gjdObj == null) {
			return null;
		}
		
		GeneralJustificationDate gjd = new GeneralJustificationDate();
		
		//id
		JSONValue idVal = gjdObj.get("id");
		if (idVal != null) {
			String id = idVal.isString().stringValue();
			gjd.setId(id);
		}
		
		//justification
		JSONValue justificationVal = gjdObj.get("justification");
		if (justificationVal != null) {
			JSONObject justificationObj = justificationVal.isObject();
			if (justificationObj != null) {
				gjd.setJustification(JustificationUtilsSerializer.read(justificationObj));
			}
		}
		
		//start
		JSONValue startVal = gjdObj.get("start");
		if (startVal != null) {
			String startStr = startVal.isString().stringValue();
			Date start = df.parse(startStr);
			gjd.setStart(start);
		}
		
		//end
		JSONValue endVal = gjdObj.get("end");
		if (endVal != null) {
			String endStr = endVal.isString().stringValue();
			Date end = df.parse(endStr);
			gjd.setEnd(end);
		}		
		
		//notes
		JSONValue notesVal = gjdObj.get("notes");
		if (notesVal != null) {
			String notes = notesVal.isString().stringValue();
			gjd.setNotes(notes);
		}
		
		return gjd;
	}

}
