package ar.com.dcsys.person.server;

import java.lang.reflect.Type;
import java.util.logging.Logger;

import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.data.person.Mail;
import ar.com.dcsys.data.person.PersonType;
import ar.com.dcsys.person.server.common.PersonTypeJsonDeserializer;
import ar.com.dcsys.person.server.common.PersonTypeJsonSerializer;
import ar.com.dcsys.pr.CSD;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;

public class GroupSerializer implements CSD<Group> {

	private static final Logger logger = Logger.getLogger(GroupSerializer.class.getName());
	
	private final Gson gson = (new GsonBuilder()).registerTypeAdapter(Mail.class, new MailInstanceCreator())
												 .registerTypeAdapter(PersonType.class, new PersonTypeJsonSerializer())
												 .registerTypeAdapter(PersonType.class, new PersonTypeJsonDeserializer())
												 .create();
	
	
	
	private class MailInstanceCreator implements InstanceCreator<Mail> {
		@Override
		public Mail createInstance(Type arg0) {
			return new Mail();
		}
	}	

	
	@Override
	public Group read(String json) {
		logger.warning("GroupSerializer : " + json);
		Group group = gson.fromJson(json, Group.class);
		return group;
	}
	
	@Override
	public String toJson(Group o) {
		String d = gson.toJson(o);
		logger.warning("GroupSerializer : " + d);
		return d;
	}
	
}
