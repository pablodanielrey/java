package ar.com.dcsys.gwt.person.client;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.pr.CSD;

import com.google.gwt.core.shared.GWT;

public class GroupListSerializer implements CSD<List<Group>> {

	private static final Logger logger = Logger.getLogger(GroupListSerializer.class.getName());
	
	interface Reader extends name.pehl.piriti.json.client.JsonReader<GroupListContainer> {}
	private static final Reader READER = GWT.create(Reader.class);
	
	interface Writer extends name.pehl.piriti.json.client.JsonWriter<GroupListContainer> {}
	private static final Writer WRITER = GWT.create(Writer.class);
	
	@Override
	public String toJson(List<Group> o) {
		GroupListContainer sc = new GroupListContainer();
		sc.list = o;
		String d = WRITER.toJson(sc);
		logger.log(Level.WARNING,"piriti : " + d);
		return d;
	}

	@Override
	public List<Group> read(String json) {
		logger.log(Level.WARNING,"piriti : " + json);
		GroupListContainer sc = READER.read(json);
		return sc.list;
	}

}
