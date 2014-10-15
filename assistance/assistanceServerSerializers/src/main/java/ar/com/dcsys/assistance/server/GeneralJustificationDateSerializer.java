package ar.com.dcsys.assistance.server;

import java.lang.reflect.Type;
import java.util.logging.Logger;

import ar.com.dcsys.data.justification.GeneralJustificationDate;
import ar.com.dcsys.data.justification.Justification;
import ar.com.dcsys.pr.CSD;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;

public class GeneralJustificationDateSerializer implements CSD<GeneralJustificationDate> {

	private static final Logger logger = Logger.getLogger(GeneralJustificationDateSerializer.class.getName());

	private final Gson gson = (new GsonBuilder()).setDateFormat("HH:mm:ss dd/MM/yyyy")
												 .registerTypeAdapter(Justification.class, new JustificationInstanceCreator())
												 .create();	
	
	private class JustificationInstanceCreator implements InstanceCreator<Justification> {
		@Override
		public Justification createInstance(Type arg0) {
			return new Justification();
		}
	}
	
	@Override
	public GeneralJustificationDate read(String json) {
		
		logger.warning("GeneralJustificationDateSerializer : " + json);
		
		GeneralJustificationDate gjd = gson.fromJson(json, GeneralJustificationDate.class);
		
		return gjd;
	}
	
	@Override
	public String toJson(GeneralJustificationDate o) {
		
		String d = gson.toJson(o);
		logger.warning("JustificationDateSerializer : " + d);
		
		return d;
	}

}
