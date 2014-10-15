package ar.com.dcsys.gwt.person.client;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.group.GroupType;
import ar.com.dcsys.pr.CSD;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

public class GroupTypeListSerializer implements CSD<List<GroupType>> {

	private static final Logger logger = Logger.getLogger(GroupTypeListSerializer.class.getName());
	
	@Override
	public String toJson(List<GroupType> o) {
		if (o == null) {
			logger.log(Level.WARNING, "grouptypes == null");
			return "";
		}
		
		JSONArray array = new JSONArray();
		for (int i = 0; i < o.size(); i++) {
			array.set(i, new JSONString(o.get(i).toString()));
		}
		JSONObject obj = new JSONObject();
		obj.put("list", array);
		return obj.toString();
	}

	@Override
	public List<GroupType> read(String json) {
		logger.log(Level.WARNING, "GroupTypeListSerializer : " + json);
		try {
			JSONValue value = JSONParser.parseStrict(json);
			JSONObject obj = value.isObject();
			JSONArray groupTypesArray = obj.get("list").isArray();
			
			if (groupTypesArray == null) {
				return null;
			}
			
			List<GroupType> groupTypes = new ArrayList<>();
			for (int i = 0; i < groupTypesArray.size(); i++) {
				JSONValue groupTypeVal = groupTypesArray.get(i);
				if (groupTypeVal != null) {
					String groupTypeStr = groupTypeVal.isString().stringValue();
					groupTypes.add(GroupType.valueOf(groupTypeStr));
				}
			}
			
			return groupTypes;
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage());
			return null;
		}
	}

}
