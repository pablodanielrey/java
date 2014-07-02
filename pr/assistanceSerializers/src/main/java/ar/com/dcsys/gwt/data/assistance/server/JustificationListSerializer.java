package ar.com.dcsys.gwt.data.assistance.server;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.justification.Justification;
import ar.com.dcsys.data.justification.Justification;
import ar.com.dcsys.pr.CSD;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.google.gson.reflect.TypeToken;

public class JustificationListSerializer implements CSD<List<Justification>> {

	private static final Logger logger = Logger.getLogger(JustificationListSerializer.class.getName());
	
	private final Gson gson = (new GsonBuilder()).registerTypeAdapter(Justification.class, new JustificationInstanceCreator()).create();
	
	private class JustificationInstanceCreator implements InstanceCreator<Justification> {
		@Override
		public Justification createInstance(Type arg0) {
			return new Justification();
		}
	}
	
	
	private class Container {
		List<Justification> list = new ArrayList<>();
	}	
	
	@Override
	public String toJson(List<Justification> o) {
		Container sc = new Container();
		for (Justification d : o) {
			sc.list.add((Justification)d);
		}
		String d = gson.toJson(sc);
		logger.log(Level.WARNING, "gson : " + d);
		return d;
	}

	@Override
	public List<Justification> read(String json) {
		logger.log(Level.WARNING,"gson : " + json);
		TypeToken<Container> type = new TypeToken<Container>() {};
		Container sc = gson.fromJson(json, type.getType());
		List<Justification> jd = new ArrayList<>();
		for (Justification d : sc.list) {
			jd.add(d);
		}
		return jd;
	}	

}
