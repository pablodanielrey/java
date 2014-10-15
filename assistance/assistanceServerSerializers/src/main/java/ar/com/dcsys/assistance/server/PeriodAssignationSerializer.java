package ar.com.dcsys.assistance.server;

import java.util.logging.Logger;

import ar.com.dcsys.assistance.server.common.PeriodTypeJsonDeserializer;
import ar.com.dcsys.assistance.server.common.PeriodTypeJsonSerializer;
import ar.com.dcsys.assistance.server.common.PersonTypeJsonDeserializer;
import ar.com.dcsys.assistance.server.common.PersonTypeJsonSerializer;
import ar.com.dcsys.data.period.PeriodAssignation;
import ar.com.dcsys.data.period.PeriodType;
import ar.com.dcsys.data.person.PersonType;
import ar.com.dcsys.pr.CSD;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class PeriodAssignationSerializer implements CSD<PeriodAssignation> {

	private static final Logger logger = Logger.getLogger(PeriodAssignationSerializer.class.getName());
	
	private final Gson gson = (new GsonBuilder()).registerTypeAdapter(PeriodType.class, new PeriodTypeJsonDeserializer())
												 .registerTypeAdapter(PeriodType.class, new PeriodTypeJsonSerializer())
												 .setDateFormat("HH:mm:ss dd/MM/yyyy")
												 .registerTypeAdapter(PersonType.class, new PersonTypeJsonSerializer())
												 .registerTypeAdapter(PersonType.class, new PersonTypeJsonDeserializer())
												 .create();
	
	
	
	
	@Override
	public PeriodAssignation read(String json) {
		logger.warning("PeriodAssignationSerializer : " + json);
		PeriodAssignation pa = gson.fromJson(json, PeriodAssignation.class);
		return pa;
	}
	
	@Override
	public String toJson(PeriodAssignation o) {
		String d = gson.toJson(o);
		logger.warning("PeriodAssignationSerializer : " + d);
		return d;
	}
	
}
