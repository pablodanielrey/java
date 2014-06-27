package ar.com.dcsys.gwt.person.server;

import java.lang.reflect.Type;
import java.util.logging.Logger;

import ar.com.dcsys.data.person.Mail;
import ar.com.dcsys.data.person.MailBean;
import ar.com.dcsys.data.person.MailChange;
import ar.com.dcsys.data.person.MailChangeBean;
import ar.com.dcsys.pr.CSD;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;

public class MailChangeSerializer implements CSD<MailChange> {
	
	private static final Logger logger = Logger.getLogger(MailChangeSerializer.class.getName());
	
	private final Gson gson = (new GsonBuilder()).registerTypeAdapter(Mail.class, new MailInstanceCreator()).create();
	
	private class MailInstanceCreator implements InstanceCreator<Mail> {
		@Override
		public Mail createInstance(Type arg0) {
			return new MailBean();
		}
	}
	
	@Override
	public MailChange read(String json) {
		
		logger.warning("MailChangeSerializer : " + json);
		
		MailChangeBean mail = gson.fromJson(json, MailChangeBean.class);
		
		return mail;
	}
	
	@Override
	public String toJson(MailChange o) {
		
		String d = gson.toJson(o);
		logger.warning("MailChangeSerializer : " + d);
		
		return d;
	}

}
