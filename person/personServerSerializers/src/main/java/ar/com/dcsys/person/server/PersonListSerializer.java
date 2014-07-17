package ar.com.dcsys.person.server;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.pr.CSD;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class PersonListSerializer implements CSD<List<Person>> {

	private static final Logger logger = Logger.getLogger(PersonListSerializer.class.getName());
	
	private final Gson gson = (new GsonBuilder()).create();
	
	private class Container {
		List<Person> list;
	}	
	
	
	private List<Person> toPersonBeanList(List<Person> l) {
		List<Person> ps = new ArrayList<Person>();
		for (Person p : l) {
			ps.add((Person)p);
		}
		return ps;
	}

	private List<Person> toPersonList(List<Person> l) {
		List<Person> ps = new ArrayList<Person>();
		for (Person p : l) {
			ps.add(p);
		}
		return ps;
	}
		
	
	@Override
	public String toJson(List<Person> o) {
		Container sc = new Container();
		sc.list = toPersonBeanList(o);
		String d = gson.toJson(sc);
		logger.log(Level.WARNING, "gson : " + d);
		return d;
	}

	@Override
	public List<Person> read(String json) {
		logger.log(Level.WARNING,"gson : " + json);

		TypeToken<Container> type = new TypeToken<Container>() {};
		Container sc = gson.fromJson(json, type.getType());
		return toPersonList(sc.list);
	}	
	
}
