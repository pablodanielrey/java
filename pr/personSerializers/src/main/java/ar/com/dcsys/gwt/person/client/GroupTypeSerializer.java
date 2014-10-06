package ar.com.dcsys.gwt.person.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.group.GroupType;
import ar.com.dcsys.pr.CSD;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

public class GroupTypeSerializer implements CSD<GroupType> {
	
	private static final Logger logger = Logger.getLogger(GroupTypeSerializer.class.getName());
	
	@Override
	public String toJson(GroupType o) {
		if (o == null) {
			logger.log(Level.WARNING, "grouptype == null");
			return "";
		}
		JSONValue groupTypeVal = new JSONString(o.toString());
		return groupTypeVal.isObject().toString();
	}
	
	@Override
	public GroupType read(String json) {
		logger.log(Level.WARNING, "GroupTypeSerializer : " + json);
		try {
			JSONValue value = JSONParser.parseStrict(json);
			JSONObject groupTypeObj = value.isObject();
			
			if (groupTypeObj != null) {
				String groupTypeStr = groupTypeObj.isString().stringValue();
				GroupType groupType = GroupType.valueOf(groupTypeStr);
				return groupType;
			} else {
				return null;
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage());
			return null;
		}
	}
	
}
