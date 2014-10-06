package ar.com.dcsys.gwt.person.client;


import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.person.Mail;
import ar.com.dcsys.gwt.data.utils.client.MailUtilsSerializer;
import ar.com.dcsys.pr.CSD;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;

public class MailSerializer implements CSD<Mail> {
	
	private static final Logger logger = Logger.getLogger(MailSerializer.class.getName());
	
	
	@Override
	public String toJson(Mail o) {
		if (o == null) {
			logger.log(Level.WARNING,"mail == null");
			return "";
		}
		
		JSONObject mailObj = MailUtilsSerializer.toJson(o);
		return mailObj.toString();
	}
	
	@Override
	public Mail read(String json) {
		logger.log(Level.WARNING, "MailSerializer : " + json);
		try {
			JSONValue value = JSONParser.parseStrict(json);
			JSONObject mailObj = value.isObject();
			
			if (mailObj != null) {
				return MailUtilsSerializer.read(mailObj);
			} else {
				return null;
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage());
			return null;
		}
	}

}
