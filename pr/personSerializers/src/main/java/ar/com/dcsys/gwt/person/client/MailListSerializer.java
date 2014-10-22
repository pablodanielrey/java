package ar.com.dcsys.gwt.person.client;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.person.Mail;
import ar.com.dcsys.gwt.data.utils.client.MailUtilsSerializer;
import ar.com.dcsys.pr.CSD;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;

public class MailListSerializer implements CSD<List<Mail>> {
	
	private static final Logger logger = Logger.getLogger(MailListSerializer.class.getName());

	@Override
	public String toJson(List<Mail> o) {
		if (o == null) {
			logger.log(Level.WARNING, "mails == null");
			return "";
		}
		
		JSONArray array = MailUtilsSerializer.toJsonArray(o);
		JSONObject obj = new JSONObject();
		obj.put("list", array);
		return obj.toString();
	}
	
	@Override
	public List<Mail> read(String json) {
		logger.log(Level.WARNING, "MailListSerializer : " + json);
		try {
			JSONValue value = JSONParser.parseStrict(json);
			JSONObject obj = value.isObject();
			JSONArray mailsArray = obj.get("list").isArray();
			
			if (mailsArray == null) {
				return null;
			}
			
			List<Mail> mails = MailUtilsSerializer.read(mailsArray);
			return mails;
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage());
			return null;
		}
	}
}
