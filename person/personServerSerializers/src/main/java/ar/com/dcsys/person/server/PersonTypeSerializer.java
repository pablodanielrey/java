package ar.com.dcsys.person.server;

import ar.com.dcsys.data.person.PersonType;
import ar.com.dcsys.pr.CSD;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class PersonTypeSerializer implements CSD<PersonType> {
	
	private static final Gson gson = (new GsonBuilder()).create();
	
	@Override
	public PersonType read(String json) {
		PersonType pt = gson.fromJson(json, PersonType.class);
		return pt;
	}
	
	@Override
	public String toJson(PersonType o) {
		String data = gson.toJson(o);
		return data;
	}
	
}
