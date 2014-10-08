package ar.com.dcsys.gwt.data.assistance.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.log.AttLog;
import ar.com.dcsys.gwt.data.utils.client.AttLogUtilsSerializer;
import ar.com.dcsys.pr.CSD;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;

public class AttLogSerializer implements CSD<AttLog> {

	public static final Logger logger = Logger.getLogger(AttLogSerializer.class.getName());
	
	@Override
	public String toJson(AttLog o) {
		JSONObject jo = AttLogUtilsSerializer.toJson(o);
		return jo.toString();
	}

	@Override
	public AttLog read(String json) {		
		logger.log(Level.WARNING,"AttLogSerializer : " + json);
		try {
			JSONValue value = JSONParser.parseStrict(json);
			JSONObject logObj = value.isObject();
			return AttLogUtilsSerializer.read(logObj);
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage());
			return null;
		}
	}

}
