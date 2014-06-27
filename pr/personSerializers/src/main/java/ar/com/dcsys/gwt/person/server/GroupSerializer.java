package ar.com.dcsys.gwt.person.server;

import java.lang.reflect.Type;
import java.util.logging.Logger;

import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.data.group.GroupBean;
import ar.com.dcsys.data.person.Mail;
import ar.com.dcsys.data.person.MailBean;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.person.PersonBean;
import ar.com.dcsys.pr.CSD;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;

public class GroupSerializer implements CSD<Group> {

	private static final Logger logger = Logger.getLogger(GroupSerializer.class.getName());
	
	private final Gson gson = (new GsonBuilder()).registerTypeAdapter(Mail.class, MailInstanceCreator.class)
												 .registerTypeAdapter(Person.class, PersonInstanceCreator.class)
												 .create();
	
	private class MailInstanceCreator implements InstanceCreator<Mail> {
		@Override
		public Mail createInstance(Type arg0) {
			return new MailBean();
		}
	}	

	private class PersonInstanceCreator implements InstanceCreator<Person> {
		@Override
		public Person createInstance(Type arg0) {
			return new PersonBean();
		}
	}	

	
	@Override
	public Group read(String json) {
		logger.warning("GroupSerializer : " + json);
		GroupBean person = gson.fromJson(json, GroupBean.class);
		return person;
	}
	
	@Override
	public String toJson(Group o) {
		String d = gson.toJson(o);
		logger.warning("GroupSerializer : " + d);
		return d;
	}
	
}
