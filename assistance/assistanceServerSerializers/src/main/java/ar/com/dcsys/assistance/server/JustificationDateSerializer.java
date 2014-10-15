package ar.com.dcsys.assistance.server;

import java.lang.reflect.Type;
import java.util.logging.Logger;

import ar.com.dcsys.assistance.server.common.PersonTypeJsonDeserializer;
import ar.com.dcsys.assistance.server.common.PersonTypeJsonSerializer;
import ar.com.dcsys.data.justification.Justification;
import ar.com.dcsys.data.justification.JustificationDate;
import ar.com.dcsys.data.person.PersonType;
import ar.com.dcsys.pr.CSD;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;

public class JustificationDateSerializer implements CSD<JustificationDate> {

	private static final Logger logger = Logger.getLogger(JustificationDateSerializer.class.getName());

	private final Gson gson = (new GsonBuilder()).setDateFormat("HH:mm:ss dd/MM/yyyy")
			 									// .registerTypeAdapter(Justification.class, new JustificationSerializer())
			 									 .registerTypeAdapter(Justification.class, new JustificationInstanceCreator())												 
												 .registerTypeAdapter(PersonType.class, new PersonTypeJsonSerializer())
												 .registerTypeAdapter(PersonType.class, new PersonTypeJsonDeserializer())
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
