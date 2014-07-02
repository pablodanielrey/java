package ar.com.dcsys.gwt.person.server;

import java.lang.reflect.Type;
import java.util.logging.Logger;

import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.data.person.Mail;
import ar.com.dcsys.data.person.Mail;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.pr.CSD;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;

public class GroupSerializer implements CSD<Group> {

	private static final Logger logger = Logger.getLogger(GroupSerializer.class.getName());
	
	private final Gson gson = (new GsonBuilder()).registerTypeAdapter(Mail.class, new MailInstanceCreator())
												 .registerTypeAdapter(Person.class, new PersonInstanceCreator())
												 .create();
	
	private class MailInstanceCreator implements InstanceCreator<Mail> {
		@Override
		public Mail createInstance(Type arg0) {
			return new Mail();
		}
	}	

	private class PersonInstanceCreator implements InstanceCreator<Person> {
		@Override
		public Person createInstance(Type arg0) {
			return new Person();
		}
	}	

	
	@Override
	public Group read(String json) {
		logger.warning("GroupSerializer : " + json);
		Group person = gson.fromJson(json, Group.class);
		return person;
	}
	
	@Override
	public String toJson(Group o) {
		String d = gson.toJson(o);
		logger.warning("GroupSerializer : " + d);
		return d;
	}
	
}
