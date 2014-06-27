package ar.com.dcsys.gwt.person.server;

import java.lang.reflect.Type;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.person.Mail;
import ar.com.dcsys.data.person.MailBean;
import ar.com.dcsys.pr.CSD;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.google.gson.reflect.TypeToken;

public class MailListSerializer implements CSD<List<Mail>> {

	private static final Logger logger = Logger.getLogger(MailListSerializer.class.getName());
	
	private final Gson gson = (new GsonBuilder()).registerTypeAdapter(Mail.class, new MailInstanceCreator()).create();
	
	private class MailInstanceCreator implements InstanceCreator<Mail> {
		@Override
		public Mail createInstance(Type arg0) {
			return new MailBean();
		}
	}
	
	
	private class Container {
		List<Mail> list;
	}	
	
	@Override
	public String toJson(List<Mail> o) {
		Container sc = new Container();
		sc.list = o;
		String d = gson.toJson(sc);
		logger.log(Level.WARNING, "gson : " + d);
		return d;
	}

	@Override
	public List<Mail> read(String json) {
		logger.log(Level.WARNING,"gson : " + json);
		TypeToken<Container> type = new TypeToken<Container>() {};
		Container sc = gson.fromJson(json, type.getType());
		return sc.list;
	}	
	
}
