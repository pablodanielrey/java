package ar.com.dcsys.assistance.server;

import java.lang.reflect.Type;
import java.util.logging.Logger;

import ar.com.dcsys.data.period.Period;
import ar.com.dcsys.data.period.WorkedHours;
import ar.com.dcsys.pr.CSD;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;

public class PeriodSerializer implements CSD<Period> {

	public static final Logger logger = Logger.getLogger(PeriodSerializer.class.getName());
	
	private final Gson gson = (new GsonBuilder()).setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
			 									 .registerTypeAdapter(WorkedHours.class, new WorkedHoursInstanceCreator()).create();

	
	private class WorkedHoursInstanceCreator implements InstanceCreator<WorkedHours> {
		@Override
		public WorkedHours createInstance(Type arg0) {
			return new WorkedHours();
		}
	}
	@Override
	public String toJson(Period o) {
		String d = gson.toJson(o);
		logger.warning(d);
		return d;
	}

	@Override
	public Period read(String json) {
		logger.warning(json);
		Period rs = gson.fromJson(json, Period.class);
		return rs;
	}

}
