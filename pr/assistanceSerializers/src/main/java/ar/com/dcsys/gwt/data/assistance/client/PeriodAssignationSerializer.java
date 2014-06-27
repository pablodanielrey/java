package ar.com.dcsys.gwt.data.assistance.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import name.pehl.piriti.json.client.JsonReader;
import name.pehl.piriti.json.client.JsonWriter;
import ar.com.dcsys.data.period.PeriodAssignation;
import ar.com.dcsys.data.period.PeriodAssignationBean;
import ar.com.dcsys.pr.CSD;

import com.google.gwt.core.client.GWT;

public class PeriodAssignationSerializer implements CSD<PeriodAssignation> {

	private static final Logger logger = Logger.getLogger(PeriodAssignationSerializer.class.getName());
	
	public interface Reader extends JsonReader<PeriodAssignationBean> {}
	public static final Reader READER = GWT.create(Reader.class);
	
	public interface Writer extends JsonWriter<PeriodAssignationBean> {}
	public static final Writer WRITER = GWT.create(Writer.class);
	
	
	@Override
	public String toJson(PeriodAssignation o) {
		String d = WRITER.toJson((PeriodAssignationBean)o);
		logger.log(Level.WARNING,"PeriodAssignationSerializer : " + d);
		return d;
	}

	@Override
	public PeriodAssignation read(String json) {
		logger.log(Level.WARNING,"PeriodAssignationSerializer : " + json);
		PeriodAssignation person = READER.read(json);
		return person;
	}

}
