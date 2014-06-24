package ar.com.dcsys.pr.serializers.client;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.pr.CSD;

import com.google.gwt.core.shared.GWT;

public class DateListSerializer implements CSD<List<Date>> {

	private static final Logger logger = Logger.getLogger(DateListSerializer.class.getName());
	
	interface Reader extends name.pehl.piriti.json.client.JsonReader<DateListContainer> { }
	private static final Reader READER = GWT.create(Reader.class);
	
	interface Writer extends name.pehl.piriti.json.client.JsonWriter<DateListContainer> { }
	private static final Writer WRITER = GWT.create(Writer.class);

	
	@Override
	public String toJson(List<Date> o) {
		DateListContainer sc = new DateListContainer();
		sc.list = o;
		String d = WRITER.toJson(sc);
		logger.log(Level.WARNING,"piriti : " + d);
		return d;
	}

	@Override
	public List<Date> read(String json) {
		logger.log(Level.WARNING,"piriti : " + json);
		DateListContainer sc = READER.read(json);
		return sc.list;
	}

}
