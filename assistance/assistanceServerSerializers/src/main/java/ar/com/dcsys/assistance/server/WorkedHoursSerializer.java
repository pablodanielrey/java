package ar.com.dcsys.assistance.server;

import java.util.logging.Logger;

import ar.com.dcsys.data.period.WorkedHours;
import ar.com.dcsys.pr.CSD;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class WorkedHoursSerializer implements CSD<WorkedHours> {

	public static final Logger logger = Logger.getLogger(WorkedHoursSerializer.class.getName());
	
	private final Gson gson = (new GsonBuilder()).setDateFormat("HH:mm:ss dd/MM/yyyy").create();
	
	@Override
	public String toJson(WorkedHours o) {
		String d = gson.toJson(o);
		logger.warning(d);
		return d;
	}

	@Override
	public WorkedHours read(String json) {
		logger.warning(json);
		WorkedHours rs = gson.fromJson(json, WorkedHours.class);
		return rs;
	}
}
