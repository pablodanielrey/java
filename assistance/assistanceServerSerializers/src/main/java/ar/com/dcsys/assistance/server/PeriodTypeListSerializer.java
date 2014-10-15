package ar.com.dcsys.assistance.server;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.period.PeriodType;
import ar.com.dcsys.pr.CSD;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;


public class PeriodTypeListSerializer implements CSD<List<PeriodType>> {
	
	private static final Logger logger = Logger.getLogger(PeriodTypeListSerializer.class.getName());
	
	private final Gson gson = (new GsonBuilder()).setDateFormat("HH:mm:ss dd/MM/yyyy")
												 .create();
	
	private class Container {
		List<PeriodType> list;
	}
	
	@Override
	public String toJson(List<PeriodType> o) {
		Container sc = new Container();
		sc.list = o;
		String d = gson.toJson(sc);
		logger.log(Level.WARNING, "gson : " + d);
		return d;
	}
	
	@Override
	public List<PeriodType> read(String json) {
		logger.log(Level.WARNING, "gson : " +json);
		
		TypeToken<Container> type = new TypeToken<Container>() {};
		Container sc = gson.fromJson(json, type.getType());
		return sc.list;
	}

}
