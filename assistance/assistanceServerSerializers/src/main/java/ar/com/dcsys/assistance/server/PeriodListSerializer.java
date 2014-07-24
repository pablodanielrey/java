package ar.com.dcsys.assistance.server;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.period.Period;
import ar.com.dcsys.pr.CSD;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class PeriodListSerializer implements CSD<List<Period>> {

	private static final Logger logger = Logger.getLogger(PeriodListSerializer.class.getName());
	
	private final Gson gson = (new GsonBuilder()).setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").create();
	
	
	private class Container {
		List<Period> list = new ArrayList<>();
	}	
	
	
	
	@Override
	public String toJson(List<Period> o) {
		Container sc = new Container();
		for (Period p : o) {
			sc.list.add((Period)p);
		}
		String d = gson.toJson(sc);
		logger.log(Level.WARNING, "gson : " + d);
		return d;
	}

	@Override
	public List<Period> read(String json) {
		logger.log(Level.WARNING,"gson : " + json);
		TypeToken<Container> type = new TypeToken<Container>() {};
		Container sc = gson.fromJson(json, type.getType());
		List<Period> ps = new ArrayList<>();
		for (Period p : sc.list) {
			ps.add(p);
		}
		return ps;
	}	

}
