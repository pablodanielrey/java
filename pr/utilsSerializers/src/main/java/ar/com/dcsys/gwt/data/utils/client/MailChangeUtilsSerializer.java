package ar.com.dcsys.gwt.data.utils.client;

import java.util.ArrayList;
import java.util.List;

import ar.com.dcsys.data.person.Mail;
import ar.com.dcsys.data.person.MailChange;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

public class MailChangeUtilsSerializer {

	public static JSONArray toJsonArray(List<MailChange> mailChanges) {
		if (mailChanges == null) {
			return null;
		}
		JSONArray mailChangesObj = new JSONArray();
		for (int i = 0; i < mailChanges.size(); i++) {
			mailChangesObj.set(i, toJson(mailChanges.get(i)));
		}
		return mailChangesObj;
	}
	
	public static JSONObject toJson(MailChange mailChange) {
		
		JSONObject mailChangeObj = new JSONObject();
		
		String personId = mailChange.getPersonId();
		if (personId != null) {
			mailChangeObj.put("personId", new JSONString(personId));
		}
		
		Mail mail = mailChange.getMail();
		if (mail != null) {
			mailChangeObj.put("mail", MailUtilsSerializer.toJson(mail));
		}
		
		Boolean confirmed = mailChange.isConfirmed();
		if (confirmed != null) {
			mailChangeObj.put("confirmed", JSONBoolean.getInstance(confirmed));
		}
		
		String token = mailChange.getToken();
		if (token != null) {
			mailChangeObj.put("token", new JSONString(token));
		}
		
		return mailChangeObj;
	}
	
	public static List<MailChange> read(JSONArray mailChangeArray) {
		
		List<MailChange> mailChanges = new ArrayList<>();
		
		if (mailChangeArray != null) {
			for (int i = 0; i < mailChangeArray.size(); i++) {
				JSONObject mailChangeObj = mailChangeArray.get(i).isObject();
				if (mailChangeObj != null) {
					mailChanges.add(read(mailChangeObj));
				}
			}
		}
		
		return mailChanges;
	}
	
	public static MailChange read(JSONObject mailChangeObj) {
		
		if (mailChangeObj == null) {
			return null;
		}
		
		MailChange mailChange = new MailChange();
		
		//personId
		JSONValue personIdVal = mailChangeObj.get("personId");
		if (personIdVal != null) {
			String personId = personIdVal.isString().stringValue();
			mailChange.setPersonId(personId);
		}
		
		//mail
		JSONValue mailValue = mailChangeObj.get("mail");
		if (mailValue != null) {
			JSONObject mailObject = mailValue.isObject();
			if (mailObject != null) {
				Mail mail = MailUtilsSerializer.read(mailObject);
				mailChange.setMail(mail);
			}
		}
		
		//token
		JSONValue tokenVal = mailChangeObj.get("token");
		if (tokenVal != null) {
			String token = tokenVal.isString().stringValue();
			mailChange.setToken(token);
		}
		
		//confirmed
		JSONValue confirmedVal = mailChangeObj.get("confirmed");
		if (confirmedVal != null) {
			Boolean confirmed = confirmedVal.isBoolean().booleanValue();
			mailChange.setConfirmed(confirmed);
		}
		
		return mailChange;
		
	} 
}
