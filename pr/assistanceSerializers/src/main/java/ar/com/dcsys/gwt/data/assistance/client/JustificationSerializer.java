package ar.com.dcsys.gwt.data.assistance.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.justification.Justification;
import ar.com.dcsys.gwt.data.utils.client.JustificationUtilsSerializer;
import ar.com.dcsys.pr.CSD;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;

public class JustificationSerializer implements CSD<Justification> {
	
	public static final Logger logger = Logger.getLogger(JustificationSerializer.class.getName());	
	
	@Override
	public String toJson(Justification o) {
		JSONObject jo = JustificationUtilsSerializer.toJson(o);
		return jo.toString();
	}

	@Override
	public Justification read(String json) {
		logger.log(Level.WARNING,"JustificationSerializer : " + json);
		try {
			JSONValue value = JSONParser.parseStrict(json);
			JSONObject obj = value.isObject();
			return JustificationUtilsSerializer.read(obj);
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage());
			return null;
		}
	}
}
