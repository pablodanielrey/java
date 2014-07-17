package ar.com.dcsys.person.server;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.person.PersonType;
import ar.com.dcsys.pr.CSD;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;


public class PersonTypeListSerializer implements CSD<List<PersonType>> {
	
	private static final Logger logger = Logger.getLogger(PersonTypeListSerializer.class.getName());
	
	private final Gson gson = (new GsonBuilder()).create();
	
	private class Container {
		List<PersonType> list;
	}
	
	@Override
	public String toJson(List<PersonType> o) {
		Container sc = new Container();
		sc.list = o;
		String d = gson.toJson(sc);
		logger.log(Level.WARNING, "gson : " + d);
		return d;
	}
	
	@Override
	public List<PersonType> read(String json) {
		logger.log(Level.WARNING, "gson : " +json);
		
		TypeToken<Container> type = new TypeToken<Container>() {};
		Container sc = gson.fromJson(json, type.getType());
		return sc.list;
	}

}
