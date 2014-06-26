package ar.com.dcsys.gwt.person.server;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.person.Mail;
import ar.com.dcsys.pr.CSD;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class MailListSerializer implements CSD<List<Mail>> {

	private static final Logger logger = Logger.getLogger(MailListSerializer.class.getName());
	
	private final Gson gson = (new GsonBuilder()).create();
	
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
