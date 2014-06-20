package ar.com.dcsys.pr.server.serializers;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.pr.CSD;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;


public class StringListSerializer implements CSD<List<String>> {

	private static final Logger logger = Logger.getLogger(StringListSerializer.class.getName());
	
	private final Gson gson = (new GsonBuilder()).create();
	
	private class StringContainer {
		List<String> list;
	}	
	
	@Override
	public String toJson(List<String> o) {
		StringContainer sc = new StringContainer();
		sc.list = o;
		String d = gson.toJson(sc);
		logger.log(Level.WARNING, "gson : " + d);
		return d;
	}

	@Override
	public List<String> read(String json) {
		logger.log(Level.WARNING,"gson : " + json);

		TypeToken<StringContainer> type = new TypeToken<StringContainer>() {};
		StringContainer sc = gson.fromJson(json, type.getType());
		return sc.list;
		
		//		TypeToken<List<String>> type = new TypeToken<List<String>>() {};
//		return gson.fromJson(json, type.getType());
//		String[] data = gson.fromJson(json, String[].class);
//		return Arrays.asList(data);
	}

}
