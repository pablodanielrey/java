package ar.com.dcsys.gwt.person.client;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.gwt.data.utils.client.GroupUtilsSerializer;
import ar.com.dcsys.pr.CSD;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;

public class GroupListSerializer implements CSD<List<Group>> {

	private static final Logger logger = Logger.getLogger(GroupListSerializer.class.getName());
		
	@Override
	public String toJson(List<Group> o) {
		if (o == null) {
			logger.log(Level.WARNING, "groups == null");
			return "";
		}
		
		JSONArray groupsObj = GroupUtilsSerializer.toJsonArray(o);
		return groupsObj.toString();		
		
	}

	@Override
	public List<Group> read(String json) {
		logger.log(Level.WARNING, "GroupListSerializer : " + json);
		try {
			JSONValue value = JSONParser.parseStrict(json);
			JSONObject obj = value.isObject();
			JSONArray groupsArray = obj.get("list").isArray();
			
			if (groupsArray == null) {
				return null;
			}
			
			List<Group> groups = GroupUtilsSerializer.read(groupsArray);
			return groups;
			
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage());
			return null;
		}
	}

}
