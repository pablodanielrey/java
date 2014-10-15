package ar.com.dcsys.assistance.server;

import java.util.logging.Logger;

import ar.com.dcsys.assistance.server.common.PersonTypeJsonDeserializer;
import ar.com.dcsys.assistance.server.common.PersonTypeJsonSerializer;
import ar.com.dcsys.data.log.AttLog;
import ar.com.dcsys.data.person.PersonType;
import ar.com.dcsys.pr.CSD;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class AttLogSerializer implements CSD<AttLog> {
	
	public static final Logger logger = Logger.getLogger(AttLogSerializer.class.getName());
	
	private final Gson gson = (new GsonBuilder()).setDateFormat("HH:mm:ss dd/MM/yyyy")
												 .registerTypeAdapter(PersonType.class, new PersonTypeJsonSerializer())
												 .registerTypeAdapter(PersonType.class, new PersonTypeJsonDeserializer())
												 .create();
	
	@Override
	public String toJson(AttLog o) {
		String d = gson.toJson(o);
		logger.warning(d);
		return d;
	}

	@Override
	public AttLog read(String json) {
		logger.warning(json);
		AttLog rs = gson.fromJson(json, AttLog.class);
		return rs;
	}
}
