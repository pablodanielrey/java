package ar.com.dcsys.gwt.data.assistance.server;

import java.util.logging.Logger;

import ar.com.dcsys.data.justification.Justification;
import ar.com.dcsys.data.justification.JustificationBean;
import ar.com.dcsys.pr.CSD;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JustificationSerializer implements CSD<Justification> {

	private static final Logger logger = Logger.getLogger(JustificationSerializer.class.getName());
	
	private final Gson gson = (new GsonBuilder()).setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
												 .create();
	
	@Override
	public Justification read(String json) {
		logger.warning(json);
		JustificationBean justification = gson.fromJson(json, JustificationBean.class);
		return justification;
	}
	
	@Override
	public String toJson(Justification o) {
		String d = gson.toJson(o);
		logger.warning(d);
		return d;
	}
	
}
