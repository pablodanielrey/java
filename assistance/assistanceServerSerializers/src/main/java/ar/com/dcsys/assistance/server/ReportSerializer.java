package ar.com.dcsys.assistance.server;

import java.util.logging.Logger;

import ar.com.dcsys.assistance.server.common.PersonTypeJsonDeserializer;
import ar.com.dcsys.assistance.server.common.PersonTypeJsonSerializer;
import ar.com.dcsys.data.person.PersonType;
import ar.com.dcsys.data.report.Report;
import ar.com.dcsys.pr.CSD;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ReportSerializer implements CSD<Report> {

	public static final Logger logger = Logger.getLogger(ReportSerializer.class.getName());
	
	private final Gson gson = (new GsonBuilder()).setDateFormat("HH:mm:ss dd/MM/yyyy")
												 .registerTypeAdapter(PersonType.class, new PersonTypeJsonSerializer())
												 .registerTypeAdapter(PersonType.class, new PersonTypeJsonDeserializer())
												 .create();
	
	@Override
	public String toJson(Report o) {
		String d = gson.toJson(o);
		logger.warning(d);
		return d;
	}

	@Override
	public Report read(String json) {
		logger.warning(json);
		Report rs = gson.fromJson(json, Report.class);
		return rs;
	}
}
