package ar.com.dcsys.pr.serializers.server;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.pr.CSD;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;


public class ListSerializer implements CSD<List> {

	private static final Logger logger = Logger.getLogger(ListSerializer.class.getName());
	
	private final Gson gson = (new GsonBuilder()).create();
	
	private class ListContainer {
		List list;
	}	
	
	@Override
	public String toJson(List o) {
		ListContainer sc = new ListContainer();
		sc.list = o;
		String d = gson.toJson(sc);
		logger.log(Level.WARNING, "gson : " + d);
		return d;
	}

	@Override
	public List read(String json) {
		logger.log(Level.WARNING,"gson : " + json);

		TypeToken<ListContainer> type = new TypeToken<ListContainer>() {};
		ListContainer sc = gson.fromJson(json, type.getType());
		return sc.list;
		
		//		TypeToken<List<String>> type = new TypeToken<List<String>>() {};
//		return gson.fromJson(json, type.getType());
//		String[] data = gson.fromJson(json, String[].class);
//		return Arrays.asList(data);
	}

}
