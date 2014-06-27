package ar.com.dcsys.gwt.data.assistance.client;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.period.PeriodType;
import ar.com.dcsys.pr.CSD;

import com.google.gwt.core.shared.GWT;

public class PeriodTypeListSerializer implements CSD<List<PeriodType>> {

	private static final Logger logger = Logger.getLogger(PeriodTypeListSerializer.class.getName());
	
	interface Reader extends name.pehl.piriti.json.client.JsonReader<PeriodTypeListContainer> {}
	private static final Reader READER = GWT.create(Reader.class);
	
	interface Writer extends name.pehl.piriti.json.client.JsonWriter<PeriodTypeListContainer> {}
	private static final Writer WRITER = GWT.create(Writer.class);
	
	@Override
	public String toJson(List<PeriodType> o) {
		PeriodTypeListContainer sc = new PeriodTypeListContainer();
		sc.list = o;
		String d = WRITER.toJson(sc);
		logger.log(Level.WARNING,"piriti : " + d);
		return d;
	}

	@Override
	public List<PeriodType> read(String json) {
		logger.log(Level.WARNING,"piriti : " + json);
		PeriodTypeListContainer sc = READER.read(json);
		return sc.list;
	}

}
