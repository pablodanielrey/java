package ar.com.dcsys.gwt.data.assistance.client;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.period.Period;
import ar.com.dcsys.pr.CSD;

import com.google.gwt.core.shared.GWT;

public class PeriodListSerializer implements CSD<List<Period>> {
	
	private static final Logger logger = Logger.getLogger(PeriodListSerializer.class.getName());
	
	interface Reader extends name.pehl.piriti.json.client.JsonReader<PeriodListContainer> {}
	private static final Reader READER = GWT.create(Reader.class);
	
	interface Writer extends name.pehl.piriti.json.client.JsonWriter<PeriodListContainer> {}
	private static final Writer WRITER = GWT.create(Writer.class);

	@Override
	public String toJson(List<Period> o) {
		PeriodListContainer sc = new PeriodListContainer();
		sc.list = o;
		String d = WRITER.toJson(sc);
		
		d = d.replaceAll("\\\"\\{", "{").replaceAll("\\}\"", "}").replace("\\","");
		
		logger.log(Level.WARNING,"piriti : " + d);
		return d;
	}
	
	@Override
	public List<Period> read(String json) {
		logger.log(Level.WARNING,"piriti : " + json);
		PeriodListContainer sc = READER.read(json);
		return sc.list;
	}

}
