package ar.com.dcsys.pr.serializers.server;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.pr.CSD;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;


public class DateListSerializer implements CSD<List<Date>> {

	private static final Logger logger = Logger.getLogger(DateListSerializer.class.getName());
	
	private final Gson gson = (new GsonBuilder()).setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").create();
	
	private class Container {
		List<Date> list;
	}	
	
	@Override
	public String toJson(List<Date> o) {
		Container sc = new Container();
		sc.list = o;
		String d = gson.toJson(sc);
		logger.log(Level.WARNING, "gson : " + d);
		return d;
	}

	@Override
	public List<Date> read(String json) {
		logger.log(Level.WARNING,"gson : " + json);

		TypeToken<Container> type = new TypeToken<Container>() {};
		Container sc = gson.fromJson(json, type.getType());
		return sc.list;
		
		//		TypeToken<List<String>> type = new TypeToken<List<String>>() {};
//		return gson.fromJson(json, type.getType());
//		String[] data = gson.fromJson(json, String[].class);
//		return Arrays.asList(data);
	}

}
