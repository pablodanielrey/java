package ar.com.dcsys.gwt.person.client;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.person.MailChange;
import ar.com.dcsys.gwt.data.utils.client.MailChangeUtilsSerializer;
import ar.com.dcsys.pr.CSD;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;

public class MailChangeListSerializer implements CSD<List<MailChange>> {
	
	private static final Logger logger = Logger.getLogger(MailChangeListSerializer.class.getName());
	
	@Override
	public String toJson(List<MailChange> o) {
		if (o == null) {
			logger.log(Level.WARNING, "mailChanges == null");
			return "";
		}
		
		JSONArray array = MailChangeUtilsSerializer.toJsonArray(o);
		JSONObject obj = new JSONObject();
		obj.put("list", array);
		return obj.toString();
	}
	
	@Override
	public List<MailChange> read(String json) {
		logger.log(Level.WARNING, "MailChangeListSerializer : " + json);
		try {
			JSONValue value = JSONParser.parseStrict(json);
			JSONObject obj = value.isObject();
			JSONArray mailChangeArray = obj.get("list").isArray();
			
			if (mailChangeArray == null) {
				return null;
			}
			
			List<MailChange> mailChanges = MailChangeUtilsSerializer.read(mailChangeArray);
			return mailChanges;
			
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage());
			return null;
		}
	}
}
