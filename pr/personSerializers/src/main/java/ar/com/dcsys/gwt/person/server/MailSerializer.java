package ar.com.dcsys.gwt.person.server;

import java.util.logging.Logger;

import ar.com.dcsys.data.person.Mail;
import ar.com.dcsys.data.person.MailBean;
import ar.com.dcsys.pr.CSD;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MailSerializer implements CSD<Mail> {
	
	private static final Logger logger = Logger.getLogger(MailSerializer.class.getName());
	
	private final Gson gson = (new GsonBuilder()).create();
	
	@Override
	public Mail read(String json) {
		
		logger.warning("MailSerializer : " + json);
		
		MailBean mail = gson.fromJson(json, MailBean.class);
		
		return mail;
	}
	
	@Override
	public String toJson(Mail o) {
		
		String d = gson.toJson(o);
		logger.warning("MailSerializer : " + d);
		
		return d;
	}

}
