package ar.com.dcsys.gwt.data.assistance.server;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.justification.Justification;
import ar.com.dcsys.data.justification.Justification;
import ar.com.dcsys.data.justification.JustificationDate;
import ar.com.dcsys.data.justification.JustificationDate;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.pr.CSD;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.google.gson.reflect.TypeToken;

public class JustificationDateListSerializer implements CSD<List<JustificationDate>> {

	private static final Logger logger = Logger.getLogger(JustificationDateListSerializer.class.getName());
	
	private final Gson gson = (new GsonBuilder()).setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
												 .registerTypeAdapter(JustificationDate.class, new JustificationDateInstanceCreator())
												 .registerTypeAdapter(Person.class, new PersonInstanceCreator())
												 .registerTypeAdapter(Justification.class, new JustificationInstanceCreator())
												 .create();
	
	

	
	
	
	private class JustificationDateInstanceCreator implements InstanceCreator<JustificationDate> {
		@Override
		public JustificationDate createInstance(Type arg0) {
			return new JustificationDate();
		}
	}
	
	private class PersonInstanceCreator implements InstanceCreator<Person> {
		@Override
		public Person createInstance(Type arg0) {
			return new Person();
		}
	}

	private class JustificationInstanceCreator implements InstanceCreator<Justification> {
		@Override
		public Justification createInstance(Type arg0) {
			return new Justification();
		}
	}
	
	
	private class Container {
		List<JustificationDate> list = new ArrayList<>();
	}	
	
	
	
	@Override
	public String toJson(List<JustificationDate> o) {
		Container sc = new Container();
		for (JustificationDate d : o) {
			sc.list.add((JustificationDate)d);
		}
		String d = gson.toJson(sc);
		logger.log(Level.WARNING, "gson : " + d);
		return d;
	}

	@Override
	public List<JustificationDate> read(String json) {
		logger.log(Level.WARNING,"gson : " + json);
		TypeToken<Container> type = new TypeToken<Container>() {};
		Container sc = gson.fromJson(json, type.getType());
		List<JustificationDate> jd = new ArrayList<>();
		for (JustificationDate d : sc.list) {
			jd.add(d);
		}
		return jd;
	}	

}
