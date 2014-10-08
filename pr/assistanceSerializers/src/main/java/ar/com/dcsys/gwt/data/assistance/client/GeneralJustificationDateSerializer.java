package ar.com.dcsys.gwt.data.assistance.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.justification.GeneralJustificationDate;
import ar.com.dcsys.gwt.data.utils.client.GeneralJustificationDateUtilsSerializer;
import ar.com.dcsys.pr.CSD;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;

public class GeneralJustificationDateSerializer implements CSD<GeneralJustificationDate> {
	
	public static final Logger logger = Logger.getLogger(GeneralJustificationDateSerializer.class.getName());
	
	@Override
	public String toJson(GeneralJustificationDate o) {
		JSONObject jo = GeneralJustificationDateUtilsSerializer.toJson(o);
		return jo.toString();
	}

	@Override
	public GeneralJustificationDate read(String json) {
		logger.log(Level.WARNING,"GeneralJustificationDateSerializer : " + json);
		try {
			JSONValue value = JSONParser.parseStrict(json);
			JSONObject gjdObj = value.isObject();
			return GeneralJustificationDateUtilsSerializer.read(gjdObj);
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage());
			return null;
		}
	}
}
