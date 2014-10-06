package ar.com.dcsys.gwt.person.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.gwt.data.utils.client.GroupUtilsSerializer;
import ar.com.dcsys.pr.CSD;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;

public class GroupSerializer implements CSD<Group> {

	private static final Logger logger = Logger.getLogger(GroupSerializer.class.getName());

	@Override
	public String toJson(Group o) {
		if (o == null) {
			logger.log(Level.WARNING, "group == null");
			return "";
		}
		
		JSONObject groupObj = GroupUtilsSerializer.toJson(o);
		return groupObj.toString();
	}
	
	@Override
	public Group read(String json) {
		logger.log(Level.WARNING, "GroupSerializer : " + json);
		try {
			JSONValue value = JSONParser.parseStrict(json);
			JSONObject groupObj = value.isObject();
			
			if (groupObj != null) {
				return GroupUtilsSerializer.read(groupObj);
			} else {
				return null;
			}
		} catch (Exception e) {
			logger.log(Level.WARNING, e.getMessage());
			return null;
		}
	}	


}
