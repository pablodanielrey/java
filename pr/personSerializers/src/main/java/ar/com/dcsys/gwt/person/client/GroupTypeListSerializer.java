package ar.com.dcsys.gwt.person.client;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.group.GroupType;
import ar.com.dcsys.pr.CSD;

import com.google.gwt.core.shared.GWT;

public class GroupTypeListSerializer implements CSD<List<GroupType>> {

	private static final Logger logger = Logger.getLogger(GroupTypeListSerializer.class.getName());
	
	interface Reader extends name.pehl.piriti.json.client.JsonReader<GroupTypeListContainer> {}
	private static final Reader READER = GWT.create(Reader.class);
	
	interface Writer extends name.pehl.piriti.json.client.JsonWriter<GroupTypeListContainer> {}
	private static final Writer WRITER = GWT.create(Writer.class);
	
	@Override
	public String toJson(List<GroupType> o) {
		GroupTypeListContainer sc = new GroupTypeListContainer();
		sc.list = o;
		String d = WRITER.toJson(sc);
		logger.log(Level.WARNING,"piriti : " + d);
		return d;
	}

	@Override
	public List<GroupType> read(String json) {
		logger.log(Level.WARNING,"piriti : " + json);
		GroupTypeListContainer sc = READER.read(json);
		return sc.list;
	}

}
