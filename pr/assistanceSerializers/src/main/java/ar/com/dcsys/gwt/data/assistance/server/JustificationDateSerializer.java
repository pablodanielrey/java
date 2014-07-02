package ar.com.dcsys.gwt.data.assistance.server;

import java.lang.reflect.Type;
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
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class JustificationDateSerializer implements CSD<JustificationDate> {

	private static final Logger logger = Logger.getLogger(JustificationDateSerializer.class.getName());

	private final Gson gson = (new GsonBuilder()).setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
			 									// .registerTypeAdapter(Justification.class, new JustificationSerializer())
			 									 .registerTypeAdapter(Justification.class, new JustificationInstanceCreator())
												 .registerTypeAdapter(Person.class, new PersonInstanceCreator())
												 .create();	
	
	/*
	public static class InterfaceSerializer<T> implements JsonSerializer<T> {
		@Override
		public JsonElement serialize(T src, Type typeOfSrc,	JsonSerializationContext context) {
			return context.serialize(src, src.getClass());
		}
	}
	*/
	
	/*private class JustificationSerializer implements JsonSerializer<Justification> {
		@Override
		public JsonElement serialize(Justification src, Type typeOfSrc,	JsonSerializationContext context) {
			return context.serialize(src,JustificationBean.class);
		}
	}*/
	
	
	private class JustificationInstanceCreator implements InstanceCreator<Justification> {
		@Override
		public Justification createInstance(Type arg0) {
			return new Justification();
		}
	}
	
	private class PersonInstanceCreator implements InstanceCreator<Person> {
		@Override
		public Person createInstance(Type arg0) {
			return new Person();
		}
	}	
	
	@Override
	public JustificationDate read(String json) {
		
		logger.warning("JustificationDateSerializer : " + json);
		
		JustificationDate jd = gson.fromJson(json, JustificationDate.class);
		
		return jd;
	}
	
	@Override
	public String toJson(JustificationDate o) {
		
		String d = gson.toJson(o);
		logger.warning("JustificationDateSerializer : " + d);
		
		return d;
	}

}
