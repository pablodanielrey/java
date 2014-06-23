package ar.com.dcsys.gwt.person.client;

import ar.com.dcsys.data.person.PersonType;
import ar.com.dcsys.pr.CSD;

public class PersonTypeSerializer implements CSD<PersonType> {
	
	
	@Override
	public PersonType read(String json) {
		String data = json.replace("\"", "");
		return PersonType.valueOf(data);
	}
	
	@Override
	public String toJson(PersonType o) {
		String data = "\"" + o.toString() + "\"";
		return data;
	}
	
}
