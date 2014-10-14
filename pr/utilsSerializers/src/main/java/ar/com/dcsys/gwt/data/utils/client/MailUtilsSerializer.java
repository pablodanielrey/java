package ar.com.dcsys.gwt.data.utils.client;

import java.util.ArrayList;
import java.util.List;

import ar.com.dcsys.data.person.Mail;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

public class MailUtilsSerializer {
	
	public static JSONArray toJsonArray(List<Mail> mails) {
		if (mails == null) {
			return null;
		}
		JSONArray mailsObj = new JSONArray();
		for (int i = 0; i < mails.size(); i++) {
			mailsObj.set(i, toJson(mails.get(i)));
		}
		return mailsObj;
	}
	
	public static JSONObject toJson(Mail mail) {
		
		JSONObject mailObj = new JSONObject();
		
		String mailStr = mail.getMail();
		if (mailStr != null) {
			mailObj.put("mail", new JSONString(mailStr));
		}
		
		Boolean isPrimary = mail.isPrimary();
		if (isPrimary != null) {
			mailObj.put("primary", JSONBoolean.getInstance(isPrimary));
		} 
		
		return mailObj;
	}
	
	public static List<Mail> read(JSONArray mailsArray) {
		
		List<Mail> mails = new ArrayList<>();
		
		if (mailsArray != null) {
			for (int i = 0; i < mailsArray.size(); i++) {
				JSONObject mailObj = mailsArray.get(i).isObject();
				if (mailObj != null) {
					mails.add(read(mailObj));
				}
			}
		}
		
		return mails;
	}
	
	public static Mail read(JSONObject mailObj) {
		
		if (mailObj == null) {
			return null;
		}
		
		Mail mail = new Mail();
		
		JSONValue mVal = mailObj.get("mail");
		if (mVal != null) {
			String mailStr = mVal.isString().stringValue();
			mail.setMail(mailStr);
		}
		
		JSONValue primaryVal = mailObj.get("primary");
		if (primaryVal != null) {
			Boolean primary = primaryVal.isBoolean().booleanValue();
			mail.setPrimary(primary);
		}
		
		return mail;
	}

}
