package ar.com.dcsys.gwt.data.utils.client;

import java.util.ArrayList;
import java.util.List;

import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.data.group.GroupType;
import ar.com.dcsys.data.person.Mail;
import ar.com.dcsys.data.person.Person;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

public class GroupUtilsSerializer {
	
	
	public static JSONArray toJsonArray(List<Group> groups) {
		if (groups == null) {
			return null;
		}
		JSONArray groupsObj = new JSONArray();
		for (int i = 0; i < groups.size(); i++) {
			groupsObj.set(i, toJson(groups.get(i)));
		}
		return groupsObj;
	}

	public static JSONObject toJson(Group g) {
		
		JSONObject groupObj = new JSONObject();
		
		String id = g.getId();
		if (id != null) {
			groupObj.put("id", new JSONString(id));
		}
		
		List<Mail> mails = g.getMails();
		JSONArray mailsObj = MailUtilsSerializer.toJsonArray(mails);
		if (mailsObj != null) {
			groupObj.put("mails", mailsObj);
		}
		
		String name = g.getName();
		if (name != null) {
			groupObj.put("name", new JSONString(name));
		}
		
		List<Person> persons = g.getPersons();
		JSONArray personsObj = PersonUtilsSerializer.toJsonArray(persons);
		if (personsObj != null) {
			groupObj.put("persons",personsObj);
		}
		
		List<String> systems = g.getSystems();
		if (systems != null && systems.size() > 0) {
			JSONArray systemsArray = new JSONArray();
			for (int i = 0; i < systems.size(); i++) {
				systemsArray.set(i,new JSONString(systems.get(i)));
			}
			groupObj.put("systems", systemsArray);
		}
		
		List<GroupType> gTypes = g.getTypes();
		if (gTypes != null && gTypes.size() > 0){
			JSONArray gTypesArray = new JSONArray();
			for (int i = 0; i < gTypes.size(); i++) {
				gTypesArray.set(i, new JSONString(gTypes.get(i).toString()));
			}
			groupObj.put("types", gTypesArray);
		}
		
		return groupObj;
	}
	
	public static List<Group> read(JSONArray groupsArray) {
		
		List<Group> groups = new ArrayList<>();
		
		if (groupsArray != null) {
			 for (int i = 0; i < groupsArray.size(); i++) {
				 JSONObject groupObj = groupsArray.get(i).isObject();
				 if (groupObj != null) {
					 groups.add(read(groupObj));
				 }
			 }
		}
		
		return groups;
	}
	
	public static Group read(JSONObject groupObj) {
		
		if (groupObj == null) {
			return null;
		}
		
		Group g = new Group();
		
		//id
		JSONValue idVal = groupObj.get("id");
		if (idVal != null) {
			String id = idVal.isString().stringValue();
			g.setId(id);
		}
		
		//name
		JSONValue nameVal = groupObj.get("name");
		if (nameVal != null) {
			String name = nameVal.isString().stringValue();
			g.setName(name);
		}
		
		//mails
		JSONValue mailsValue = groupObj.get("mails");
		if (mailsValue != null) {
			JSONArray mailsArray = mailsValue.isArray();
			if (mailsArray != null) {
				List<Mail> mails = MailUtilsSerializer.read(mailsArray);
				g.setMails(mails);
			}
		}
		
		//persons
		JSONValue personsValue = groupObj.get("persons");
		if (personsValue != null) {
			JSONArray personsArray = personsValue.isArray();
			if (personsArray != null) {
				List<Person> persons = PersonUtilsSerializer.read(personsArray);
				g.setPersons(persons);
			}
		}
		
		//systems
		JSONValue systemsValue = groupObj.get("systems");
		if (systemsValue != null) {
			JSONArray systemsArray = systemsValue.isArray();
			if (systemsArray != null) {
				List<String> systems = new ArrayList<>();
				for (int i = 0; i < systemsArray.size(); i++) {
					JSONValue systemVal = systemsArray.get(i);
					if (systemVal != null) {
						String systemStr = systemVal.isString().stringValue();
						systems.add(systemStr);
					}
				}
				g.setSystems(systems);
			}
		}
		
		//types
		JSONValue typesValue = groupObj.get("types");
		if (typesValue != null) {
			JSONArray typesArray = typesValue.isArray();
			if (typesArray != null) {
				List<GroupType> types = new ArrayList<>();
				for (int i = 0; i < typesArray.size(); i++) {
					JSONValue typeVal = typesArray.get(i);
					if (typeVal != null) {
						String typeStr = typeVal.isString().stringValue();
						types.add(GroupType.valueOf(typeStr));
					}
				}
				g.setTypes(types);
			}
		}
		
		return g;
		
	}
	
}
