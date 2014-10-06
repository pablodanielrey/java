package ar.com.dcsys.gwt.person.client;


import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.person.MailChange;
import ar.com.dcsys.gwt.data.utils.client.MailChangeUtilsSerializer;
import ar.com.dcsys.pr.CSD;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;

public class MailChangeSerializer implements CSD<MailChange> {
	
	private static final Logger logger = Logger.getLogger(MailChangeSerializer.class.getName());
	
	@Override
	public String toJson(MailChange o) {
		if (o == null) {
			logger.log(Level.WARNING, "mailChange == null");
			return "";
		}
		
		JSONObject mailChangeObj = MailChangeUtilsSerializer.toJson(o);
		return mailChangeObj.toString();
	}
	
	@Override
	public MailChange read(String json) {
		logger.log(Level.WARNING, "MailChangeSerializer : " + json);
		try {
			JSONValue value = JSONParser.parseStrict(json);
			JSONObject mailChangeObj = value.isObject();
			
			if (mailChangeObj != null) {
				return MailChangeUtilsSerializer.read(mailChangeObj);
			} else {
				return null;
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage());
			return null;
		} 
	}
	
}