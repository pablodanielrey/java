package ar.com.dcsys.person.server.common;

import java.lang.reflect.Type;

import ar.com.dcsys.data.person.PersonType;
import ar.com.dcsys.data.person.PersonTypeExternal;
import ar.com.dcsys.data.person.PersonTypePersonal;
import ar.com.dcsys.data.person.PersonTypePostgraduate;
import ar.com.dcsys.data.person.PersonTypeStudent;
import ar.com.dcsys.data.person.PersonTypeTeacher;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class PersonTypeJsonDeserializer  implements JsonDeserializer<PersonType> {
	@Override
	public PersonType deserialize(JsonElement json, Type typeOfT,JsonDeserializationContext context) throws JsonParseException {
		
		JsonObject jo = json.getAsJsonObject();
		
		JsonElement idElem = jo.get("id");
		String id = null;
		if (idElem != null) {
			id = idElem.getAsString();
		}
		
		JsonElement typeElem = jo.get("type");
		if (typeElem == null) {
			return null;
		}
		
		String typeStr = typeElem.getAsString();
		
		if (PersonTypeExternal.class.getName().equals(typeStr)) {
			PersonTypeExternal t = new PersonTypeExternal();
			t.setId(id);
			return t;
		}
		
		if (PersonTypeStudent.class.getName().equals(typeStr)) {
			PersonTypeStudent t = new PersonTypeStudent();
			t.setId(id);
			String studentNumber = jo.get("studentNumber").getAsString();
			t.setStudentNumber(studentNumber);
			return t;
		}
		
		if (PersonTypePersonal.class.getName().equals(typeStr)) {
			PersonTypePersonal t = new PersonTypePersonal();
			t.setId(id);
			return t;
		}
		
		if (PersonTypePostgraduate.class.getName().equals(typeStr)) {
			PersonTypePostgraduate t = new PersonTypePostgraduate();
			t.setId(id);
			return t;
		}
		
		if (PersonTypeTeacher.class.getName().equals(typeStr)) {
			PersonTypeTeacher t = new PersonTypeTeacher();
			t.setId(id);
			return t;
		}
		
		return null;
	}

}
