package ar.com.dcsys.assistance.server;

import java.util.logging.Logger;

import ar.com.dcsys.data.period.PeriodAssignation;
import ar.com.dcsys.data.period.PeriodAssignation;
import ar.com.dcsys.pr.CSD;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class PeriodAssignationSerializer implements CSD<PeriodAssignation> {

	private static final Logger logger = Logger.getLogger(PeriodAssignationSerializer.class.getName());
	
	private final Gson gson = (new GsonBuilder()).setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
												 .create();
	
	@Override
	public PeriodAssignation read(String json) {
		logger.warning("PeriodAssignationSerializer : " + json);
		PeriodAssignation person = gson.fromJson(json, PeriodAssignation.class);
		return person;
	}
	
	@Override
	public String toJson(PeriodAssignation o) {
		String d = gson.toJson(o);
		logger.warning("PeriodAssignationSerializer : " + d);
		return d;
	}
	
}
