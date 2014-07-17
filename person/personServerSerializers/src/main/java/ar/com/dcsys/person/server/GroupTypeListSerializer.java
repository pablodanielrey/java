package ar.com.dcsys.person.server;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.group.GroupType;
import ar.com.dcsys.pr.CSD;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;


public class GroupTypeListSerializer implements CSD<List<GroupType>> {
	
	private static final Logger logger = Logger.getLogger(GroupTypeListSerializer.class.getName());
	
	private final Gson gson = (new GsonBuilder()).create();
	
	private class Container {
		List<GroupType> list;
	}
	
	@Override
	public String toJson(List<GroupType> o) {
		Container sc = new Container();
		sc.list = o;
		String d = gson.toJson(sc);
		logger.log(Level.WARNING, "gson : " + d);
		return d;
	}
	
	@Override
	public List<GroupType> read(String json) {
		logger.log(Level.WARNING, "gson : " +json);
		
		TypeToken<Container> type = new TypeToken<Container>() {};
		Container sc = gson.fromJson(json, type.getType());
		return sc.list;
	}

}
