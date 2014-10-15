package ar.com.dcsys.person.server;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.data.person.PersonType;
import ar.com.dcsys.person.server.common.PersonTypeJsonDeserializer;
import ar.com.dcsys.person.server.common.PersonTypeJsonSerializer;
import ar.com.dcsys.pr.CSD;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;


public class GroupListSerializer implements CSD<List<Group>> {
	
	private static final Logger logger = Logger.getLogger(GroupListSerializer.class.getName());
	
	private final Gson gson = (new GsonBuilder()).registerTypeAdapter(PersonType.class, new PersonTypeJsonSerializer())
			 									 .registerTypeAdapter(PersonType.class, new PersonTypeJsonDeserializer())
			 									 .create();
	
	private class Container {
		List<Group> list;
	}
	
	@Override
	public String toJson(List<Group> o) {
		Container sc = new Container();
		sc.list = o;
		String d = gson.toJson(sc);
		logger.log(Level.WARNING, "gson : " + d);
		return d;
	}
	
	@Override
	public List<Group> read(String json) {
		logger.log(Level.WARNING, "gson : " +json);
		
		TypeToken<Container> type = new TypeToken<Container>() {};
		Container sc = gson.fromJson(json, type.getType());
		return sc.list;
	}

}
