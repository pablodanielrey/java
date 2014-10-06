package ar.com.dcsys.gwt.data.utils.client;

import java.util.ArrayList;
import java.util.List;

import ar.com.dcsys.data.person.PersonType;
import ar.com.dcsys.data.person.PersonTypeExternal;
import ar.com.dcsys.data.person.PersonTypePersonal;
import ar.com.dcsys.data.person.PersonTypePostgraduate;
import ar.com.dcsys.data.person.PersonTypeStudent;
import ar.com.dcsys.data.person.PersonTypeTeacher;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

public class PersonTypeUtilsSerializer {
	
	
	public static JSONArray toJsonArray(List<PersonType> types) {
		if (types == null) {
			return null;
		}
		JSONArray typesObj = new JSONArray();
		for (int i = 0; i < types.size(); i++) {
			typesObj.set(i, toJson(types.get(i)));
		}
		return typesObj;
	}
	
	public static JSONObject toJson(PersonType type) {
		
		JSONObject typeObj = new JSONObject();
		
		String id = type.getId();
		if (id != null) {
			typeObj.put("id", new JSONString(id));
		}
		
		String name = type.getName();
		if (name != null) {
			typeObj.put("name", new JSONString(name));
		}
		
		String typeStr = type.getType();
		if (typeStr != null) {
			typeObj.put("type", new JSONString(typeStr));
			
			if (type instanceof PersonTypeStudent) {
				String studentNumber = ((PersonTypeStudent)type).getStudentNumber();
				typeObj.put("studenNumber", new JSONString(studentNumber));
			}
		}
		
		return typeObj;
	}
	
	public static List<PersonType> read(JSONArray typesArray) {
		
		List<PersonType> types = new ArrayList<>();
		
		if (typesArray != null) {
			for (int i = 0; i < typesArray.size(); i++) {
				JSONObject typeObj = typesArray.get(i).isObject();
				if (typeObj != null) {
					types.add(read(typeObj));
				}
			}
		}
		
		return types;
	}
	
	public static PersonType read(JSONObject typeObj) {
		
		if (typeObj == null) {
			return null;
		}
		
		JSONValue tVal = typeObj.get("type");
		if (tVal == null) {
			return null;
		}
		
		String type = tVal.isString().stringValue();
		
		String id = null;
		JSONValue idValue = typeObj.get("id");
		if (idValue != null) {
			id = idValue.isString().toString();
		}
		
		if (PersonTypeStudent.class.getName().equals(type)) {
			PersonTypeStudent pt = new PersonTypeStudent();
			
			JSONValue snValue = typeObj.get("studentNumber");
			if (snValue != null) {
				pt.setStudentNumber(snValue.isString().toString());
			}
			
			pt.setId(id);
			return pt;
		}
		
		if (PersonTypePersonal.class.getName().equals(type)) {
			PersonTypePersonal pt = new PersonTypePersonal();
			pt.setId(id);
			return pt;
		}
		
		if (PersonTypeExternal.class.getName().equals(type)) {
			PersonTypeExternal pt = new PersonTypeExternal();
			pt.setId(id);
			return pt;
		}
		
		if (PersonTypePostgraduate.class.getName().equals(type)) {
			PersonTypePostgraduate pt = new PersonTypePostgraduate();
			pt.setId(id);
			return pt;
		}
		
		if (PersonTypeTeacher.class.getName().equals(type)) {
			PersonTypeTeacher pt = new PersonTypeTeacher();
			pt.setId(id);
			return pt;
		}
		
		return null;
	}

}
