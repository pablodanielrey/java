package ar.com.dcsys.person.server;

import ar.com.dcsys.data.group.GroupType;
import ar.com.dcsys.pr.CSD;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GroupTypeSerializer implements CSD<GroupType> {
	
	private static final Gson gson = (new GsonBuilder()).create();
	
	@Override
	public GroupType read(String json) {
		GroupType pt = gson.fromJson(json, GroupType.class);
		return pt;
	}
	
	@Override
	public String toJson(GroupType o) {
		String data = gson.toJson(o);
		return data;
	}
	
}
