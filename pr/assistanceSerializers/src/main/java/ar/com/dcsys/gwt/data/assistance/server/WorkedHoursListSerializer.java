package ar.com.dcsys.gwt.data.assistance.server;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.period.WorkedHours;
import ar.com.dcsys.pr.CSD;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.google.gson.reflect.TypeToken;

public class WorkedHoursListSerializer implements CSD<List<WorkedHours>> {

	private static final Logger logger = Logger.getLogger(WorkedHoursListSerializer.class.getName());
	
	private final Gson gson = (new GsonBuilder()).setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
												 .registerTypeAdapter(WorkedHours.class, new WorkedHoursInstanceCreator()).create();
	

	private class WorkedHoursInstanceCreator implements InstanceCreator<WorkedHours> {
		@Override
		public WorkedHours createInstance(Type arg0) {
			return new WorkedHours();
		}
	}
	
	
	private class Container {
		List<WorkedHours> list = new ArrayList<>();
	}	
	
	
	
	@Override
	public String toJson(List<WorkedHours> o) {
		Container sc = new Container();
		for (WorkedHours w : o) {
			sc.list.add((WorkedHours)w);
		}
		String d = gson.toJson(sc);
		logger.log(Level.WARNING, "gson : " + d);
		return d;
	}

	@Override
	public List<WorkedHours> read(String json) {
		logger.log(Level.WARNING,"gson : " + json);
		TypeToken<Container> type = new TypeToken<Container>() {};
		Container sc = gson.fromJson(json, type.getType());
		List<WorkedHours> whs = new ArrayList<>();
		for (WorkedHours wh : sc.list) {
			whs.add(wh);
		}
		return whs;
	}	

}
