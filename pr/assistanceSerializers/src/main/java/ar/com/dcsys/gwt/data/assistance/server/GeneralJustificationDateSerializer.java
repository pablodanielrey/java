package ar.com.dcsys.gwt.data.assistance.server;

import java.lang.reflect.Type;
import java.util.logging.Logger;

import ar.com.dcsys.data.justification.GeneralJustificationDate;
import ar.com.dcsys.data.justification.GeneralJustificationDateBean;
import ar.com.dcsys.data.justification.Justification;
import ar.com.dcsys.data.justification.JustificationBean;
import ar.com.dcsys.pr.CSD;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;

public class GeneralJustificationDateSerializer implements CSD<GeneralJustificationDate> {

	private static final Logger logger = Logger.getLogger(GeneralJustificationDateSerializer.class.getName());

	private final Gson gson = (new GsonBuilder()).setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").registerTypeAdapter(Justification.class, JustificationInstanceCreator.class).create();	
	
	private class JustificationInstanceCreator implements InstanceCreator<Justification> {
		@Override
		public Justification createInstance(Type arg0) {
			return new JustificationBean();
		}
	}
	
	@Override
	public GeneralJustificationDate read(String json) {
		
		logger.warning("GeneralJustificationDateSerializer : " + json);
		
		GeneralJustificationDateBean gjd = gson.fromJson(json, GeneralJustificationDateBean.class);
		
		return gjd;
	}
	
	@Override
	public String toJson(GeneralJustificationDate o) {
		
		String d = gson.toJson(o);
		logger.warning("JustificationDateSerializer : " + d);
		
		return d;
	}

}
