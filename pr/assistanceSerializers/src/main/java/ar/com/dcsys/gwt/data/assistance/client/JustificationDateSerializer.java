package ar.com.dcsys.gwt.data.assistance.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.justification.JustificationDate;
import ar.com.dcsys.gwt.data.utils.client.JustificationDateUtilsSerializer;
import ar.com.dcsys.pr.CSD;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;

public class JustificationDateSerializer implements CSD<JustificationDate> {
	
	public static final Logger logger = Logger.getLogger(JustificationDateSerializer.class.getName());
	
	@Override
	public String toJson(JustificationDate o) {
		JSONObject jo = JustificationDateUtilsSerializer.toJson(o);
		return jo.toString();
	}

	@Override
	public JustificationDate read(String json) {
		logger.log(Level.WARNING,"JustificationDateSerializer : " + json);
		try {
			JSONValue value = JSONParser.parseStrict(json);
			JSONObject obj = value.isObject();
			return JustificationDateUtilsSerializer.read(obj);
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage());
			return null;
		}
	}
}
