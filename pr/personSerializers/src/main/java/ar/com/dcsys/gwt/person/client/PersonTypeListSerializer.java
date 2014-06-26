package ar.com.dcsys.gwt.person.client;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.shared.GWT;

import ar.com.dcsys.data.person.PersonType;
import ar.com.dcsys.pr.CSD;

public class PersonTypeListSerializer implements CSD<List<PersonType>> {

	private static final Logger logger = Logger.getLogger(PersonTypeListSerializer.class.getName());
	
	interface Reader extends name.pehl.piriti.json.client.JsonReader<PersonTypeListContainer> {}
	private static final Reader READER = GWT.create(Reader.class);
	
	interface Writer extends name.pehl.piriti.json.client.JsonWriter<PersonTypeListContainer> {}
	private static final Writer WRITER = GWT.create(Writer.class);
	
	@Override
	public String toJson(List<PersonType> o) {
		PersonTypeListContainer sc = new PersonTypeListContainer();
		sc.list = o;
		String d = WRITER.toJson(sc);
		logger.log(Level.WARNING,"piriti : " + d);
		return d;
	}

	@Override
	public List<PersonType> read(String json) {
		logger.log(Level.WARNING,"piriti : " + json);
		PersonTypeListContainer sc = READER.read(json);
		return sc.list;
	}

}
