package ar.com.dcsys.gwt.data.utils.client;

import java.util.ArrayList;
import java.util.List;

import ar.com.dcsys.data.justification.Justification;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

public class JustificationUtilsSerializer {
	
	public static JSONArray toJsonArray(List<Justification> justifications) {
		if (justifications == null) {
			return null;
		}
		JSONArray justificationsObj = new JSONArray();
		for (int i = 0; i < justifications.size(); i++) {
			justificationsObj.set(i, toJson(justifications.get(i)));
		}
		
		return justificationsObj;
		
	}
	
	public static JSONObject toJson(Justification justification) {
		
		JSONObject justificationObj = new JSONObject();
		
		//id
		String id = justification.getId();
		if (id != null) {
			justificationObj.put("id", new JSONString(id));
		}
		
		//code
		String code = justification.getCode();
		if (code != null) {
			justificationObj.put("code", new JSONString(code));
		}
		
		//description
		String description = justification.getDescription();
		if (description != null) {
			justificationObj.put("description", new JSONString(description));
		}
				
		return justificationObj;
	}
	
	public static List<Justification> read(JSONArray justificationArray) {
		
		List<Justification> justifications = new ArrayList<>();
		
		if (justificationArray != null) {
			for (int i = 0; i < justificationArray.size(); i++) {
				JSONObject justificationObj = justificationArray.get(i).isObject();
				if (justificationObj != null) {
					justifications.add(read(justificationObj));
				}
			}
		}
		
		return justifications;
	}
	
	public static Justification read(JSONObject justificationObj) {
		
		if (justificationObj == null) {
			return null;
		}
		
		Justification justification = new Justification();
		
		//id
		JSONValue idVal = justificationObj.get("id");
		if (idVal != null) {
			String id = idVal.isString().stringValue();
			justification.setId(id);
		}
		
		//code
		JSONValue codeVal = justificationObj.get("code");
		if (codeVal != null) {
			String code = codeVal.isString().stringValue();
			justification.setCode(code);
		}
		
		//description
		JSONValue descriptionVal = justificationObj.get("description");
		if (descriptionVal != null) {
			String description = descriptionVal.isString().stringValue();
			justification.setDescription(description);
		}
		
		return justification;
	}

}
