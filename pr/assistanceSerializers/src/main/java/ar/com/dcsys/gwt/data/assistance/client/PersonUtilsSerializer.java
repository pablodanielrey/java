package ar.com.dcsys.gwt.data.assistance.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.com.dcsys.data.person.Gender;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.person.PersonType;
import ar.com.dcsys.data.person.Telephone;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

public class PersonUtilsSerializer {

	private static final DateTimeFormat df = DateTimeFormat.getFormat("dd/MM/yyyy"); 
	
	public static JSONObject toJson(Person p) {		
				
		JSONObject personObj = new JSONObject();
				
		String address = p.getAddress();
		if (address != null) {
			personObj.put("address", new JSONString(address));	
		}
		
		if (p.getBirthDate() != null) {
			String birthdate = df.format(p.getBirthDate());	
			personObj.put("birthDate",new JSONString(birthdate));
		}
		
		
		String city = p.getCity();
		if (city != null) {
			personObj.put("city", new JSONString(city));
		}
		
		String country = p.getCountry();
		if (country != null) {
			personObj.put("country", new JSONString(country));
		}
				
		String dni = p.getDni();
		if (dni != null) {
			personObj.put("dni", new JSONString(dni));
		}
		
		if (p.getGender() != null) {
			String gender = p.getGender().toString();
			personObj.put("gender", new JSONString(gender));
		}
		
		String id = p.getId();
		if (id != null) {
			personObj.put("id", new JSONString(id));
		}
				
		String lastName = p.getLastName();
		if (lastName != null) {
			personObj.put("lastName", new JSONString(lastName));
		}
		
		String name = p.getName();
		if (name != null) {
			personObj.put("name", new JSONString(name));
		}
		
		List<Telephone> telephones = p.getTelephones();
		if (telephones != null && telephones.size() > 0) {
			JSONArray telephonesObj = new JSONArray();
			for (int i=0; i<telephones.size(); i++) {
				JSONObject telObj = new JSONObject();
				telObj.put("number", new JSONString(telephones.get(i).getNumber()));
				telObj.put("isMobile",JSONBoolean.getInstance(telephones.get(i).isMobile()));
				telephonesObj.set(i, telObj);
			}
			personObj.put("telephones", telephonesObj);
		}
		
		List<PersonType> types = p.getTypes();
		if (types != null && types.size() > 0) {
			JSONArray typesObj = new JSONArray();
			for (int i=0; i<types.size(); i++) {
				typesObj.set(i,new JSONString(types.get(i).toString()));
			}
			personObj.put("types", typesObj);
		}
		
		return personObj;		
	}
	
	public static Person read(JSONObject personObj) {
		
		if (personObj != null) {

			Person p = new Person();
			
			String id = personObj.get("id").isString().stringValue();
			p.setId(id);
			
			JSONValue addressValue = personObj.get("address");
			if (addressValue != null) {
				String address = addressValue.isString().stringValue();
				p.setAddress(address);
			}
			
			JSONValue birthDateValue = personObj.get("birthDate");
			if (birthDateValue != null) {
				String birthDate = birthDateValue.isString().stringValue();
				Date date = df.parse(birthDate);			
				p.setBirthDate(date);
			}
			
			JSONValue cityValue = personObj.get("city");
			if (cityValue != null) {
				String city = cityValue.isString().stringValue();
				p.setCity(city);
			}
			
			JSONValue countryValue = personObj.get("country");
			if (countryValue != null) {
				String country = countryValue.isString().stringValue();
				p.setCountry(country);
			}

			JSONValue dniValue = personObj.get("dni");
			if (dniValue != null) {
				String dni = dniValue.isString().stringValue();			
				p.setDni(dni);
			}
			
			JSONValue genderValue = personObj.get("gender");
			if (genderValue != null) {
				String gender = genderValue.isString().stringValue();
				p.setGender(Gender.valueOf(gender));
			}
			
			JSONValue lastNameValue = personObj.get("lastName");
			if (lastNameValue != null) {
				String lastName = lastNameValue.isString().stringValue();			
				p.setLastName(lastName);
			}
				
			JSONValue nameValue = personObj.get("name");
			if (nameValue != null) {
				String name = nameValue.isString().stringValue();
				p.setName(name);
			}
			
			JSONValue telephonesValue = personObj.get("telephones");
			if (telephonesValue != null) {
				JSONArray telephonesArray = telephonesValue.isArray();
				List<Telephone> telephones = new ArrayList<>();
				if (telephonesArray != null) {
					for (int i=0; i<=telephonesArray.size(); i++) {
						
						JSONValue telVal = telephonesArray.get(i);
						if (telVal != null) {
							JSONObject telObj = telVal.isObject();
							
							Telephone tel = new Telephone();
							
							String number = telObj.get("number").isString().stringValue();
							tel.setNumber(number);
							
							boolean isMobile = telObj.get("isMobile").isBoolean().booleanValue();
							tel.setMobile(isMobile);
							
							telephones.add(tel);
						}
					}
				}
				p.setTelephones(telephones);
			}
			
			JSONValue typesValue = personObj.get("types");
			if (typesValue != null) {
				JSONArray typesArray = typesValue.isArray();
				List<PersonType> types = new ArrayList<>();
				if (typesArray != null) {
					for (int i=0; i<typesArray.size(); i++) {
						JSONValue typeValue = typesArray.get(i);
						if (typeValue != null) {
							String typeStr = typeValue.isString().stringValue();
							types.add(PersonType.valueOf(typeStr));
						}
					}
				}			
				p.setTypes(types);
			}
			
			return p;	
		} else {
			return null;
		}		
	}

}
