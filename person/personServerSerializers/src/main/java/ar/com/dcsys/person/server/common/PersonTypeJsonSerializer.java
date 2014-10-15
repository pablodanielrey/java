package ar.com.dcsys.person.server.common;

import java.lang.reflect.Type;

import ar.com.dcsys.data.person.PersonType;
import ar.com.dcsys.data.person.PersonTypeStudent;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class PersonTypeJsonSerializer  implements JsonSerializer<PersonType> {
	@Override
	public JsonElement serialize(PersonType type, Type typeOfSrc,JsonSerializationContext context) {
		
		JsonObject typeObj = new JsonObject();
		
		String id = type.getId();
		if (id != null) {
			typeObj.add("id", new JsonPrimitive(id));
		}
		
		String name = type.getName();
		if  (name != null) {
			typeObj.add("name", new JsonPrimitive(name));
		}
		
		String typeStr = type.getType();
		if (typeStr != null) {
			typeObj.add("type", new JsonPrimitive(typeStr));
			
			if (type instanceof PersonTypeStudent) {
				String studentNumber = ((PersonTypeStudent)type).getStudentNumber();
				if (studentNumber != null) {
					typeObj.add("studentNumber", new JsonPrimitive(studentNumber));
				}
			}
		}
		
		return typeObj;
		
	}

}
