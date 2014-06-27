package ar.com.dcsys.gwt.data.assistance.server;

import java.lang.reflect.Type;
import java.util.logging.Logger;

import ar.com.dcsys.data.justification.Justification;
import ar.com.dcsys.data.justification.JustificationBean;
import ar.com.dcsys.data.justification.JustificationDate;
import ar.com.dcsys.data.justification.JustificationDateBean;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.person.PersonBean;
import ar.com.dcsys.pr.CSD;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;

public class JustificationDateSerializer implements CSD<JustificationDate> {

	private static final Logger logger = Logger.getLogger(JustificationDateSerializer.class.getName());

	private final Gson gson = (new GsonBuilder()).setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
												 .registerTypeAdapter(Justification.class, new JustificationInstanceCreator())
												 .registerTypeAdapter(Person.class, new PersonInstanceCreator()).create();	
	
	private class JustificationInstanceCreator implements InstanceCreator<Justification> {
		@Override
		public Justification createInstance(Type arg0) {
			return new JustificationBean();
		}
	}
	
	private class PersonInstanceCreator implements InstanceCreator<Person> {
		@Override
		public Person createInstance(Type arg0) {
			return new PersonBean();
		}
	}	
	
	@Override
	public JustificationDate read(String json) {
		
		logger.warning("JustificationDateSerializer : " + json);
		
		JustificationDateBean jd = gson.fromJson(json, JustificationDateBean.class);
		
		return jd;
	}
	
	@Override
	public String toJson(JustificationDate o) {
		
		String d = gson.toJson(o);
		logger.warning("JustificationDateSerializer : " + d);
		
		return d;
	}

}
