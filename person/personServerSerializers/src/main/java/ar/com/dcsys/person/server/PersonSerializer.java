package ar.com.dcsys.person.server;

import java.lang.reflect.Type;
import java.util.logging.Logger;

import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.person.PersonType;
import ar.com.dcsys.data.person.PersonTypeExternal;
import ar.com.dcsys.data.person.PersonTypePersonal;
import ar.com.dcsys.data.person.PersonTypePostgraduate;
import ar.com.dcsys.data.person.PersonTypeStudent;
import ar.com.dcsys.data.person.PersonTypeTeacher;
import ar.com.dcsys.pr.CSD;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class PersonSerializer implements CSD<Person> {

	private static final Logger logger = Logger.getLogger(PersonSerializer.class.getName());
	
	private final Gson gson = (new GsonBuilder()).registerTypeAdapter(PersonType.class, new PersonTypeSerializer())
												 .registerTypeAdapter(PersonType.class, new PersonTypeDeserializer())
												 .setDateFormat("HH:mm:ss dd/MM/yyyy")
												 .create();
	
	private class PersonTypeSerializer implements JsonSerializer<PersonType> {
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
	
	private class PersonTypeDeserializer implements JsonDeserializer<PersonType> {
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
	
	@Override
	public Person read(String json) {
		logger.warning("PersonSerializer : " + json);
		Person person = gson.fromJson(json, Person.class);
		return person;
	}
	
	@Override
	public String toJson(Person o) {
		String d = gson.toJson(o);
		logger.warning("PersonSerializer : " + d);
		return d;
	}
	
}
