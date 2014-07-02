package ar.com.dcsys.gwt.data.assistance.server;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.justification.GeneralJustificationDate;
import ar.com.dcsys.data.justification.GeneralJustificationDate;
import ar.com.dcsys.data.justification.Justification;
import ar.com.dcsys.pr.CSD;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.google.gson.reflect.TypeToken;

public class GeneralJustificationDateListSerializer implements CSD<List<GeneralJustificationDate>> {

	private static final Logger logger = Logger.getLogger(GeneralJustificationDateListSerializer.class.getName());
	
	private final Gson gson = (new GsonBuilder()).setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
												 .registerTypeAdapter(GeneralJustificationDate.class, new GeneralJustificationInstanceCreator())
												 .registerTypeAdapter(Justification.class, new JustificationInstanceCreator())
												 .create();
	
	private class GeneralJustificationInstanceCreator implements InstanceCreator<GeneralJustificationDate> {
		@Override
		public GeneralJustificationDate createInstance(Type arg0) {
			return new GeneralJustificationDate();
		}
	}
	
	private class JustificationInstanceCreator implements InstanceCreator<Justification> {
		@Override
		public Justification createInstance(Type arg0) {
			return new Justification();
		}
	}	
	
	
	private class Container {
		List<GeneralJustificationDate> list = new ArrayList<>();
	}	
	
	@Override
	public String toJson(List<GeneralJustificationDate> o) {
		Container sc = new Container();
		for (GeneralJustificationDate d : o) {
			sc.list.add((GeneralJustificationDate)d);
		}
		String d = gson.toJson(sc);
		logger.log(Level.WARNING, "gson : " + d);
		return d;
	}

	@Override
	public List<GeneralJustificationDate> read(String json) {
		logger.log(Level.WARNING,"gson : " + json);
		TypeToken<Container> type = new TypeToken<Container>() {};
		Container sc = gson.fromJson(json, type.getType());
		List<GeneralJustificationDate> jd = new ArrayList<>();
		for (GeneralJustificationDate d : sc.list) {
			jd.add(d);
		}
		return jd;
	}	

}
