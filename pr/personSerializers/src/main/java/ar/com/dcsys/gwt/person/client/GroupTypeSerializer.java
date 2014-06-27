package ar.com.dcsys.gwt.person.client;

import ar.com.dcsys.data.group.GroupType;
import ar.com.dcsys.pr.CSD;

public class GroupTypeSerializer implements CSD<GroupType> {
	
	
	@Override
	public GroupType read(String json) {
		String data = json.replace("\"", "");
		return GroupType.valueOf(data);
	}
	
	@Override
	public String toJson(GroupType o) {
		String data = "\"" + o.toString() + "\"";
		return data;
	}
	
}
