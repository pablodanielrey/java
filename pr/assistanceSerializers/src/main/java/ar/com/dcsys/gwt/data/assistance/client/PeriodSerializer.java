package ar.com.dcsys.gwt.data.assistance.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.period.Period;
import ar.com.dcsys.gwt.data.utils.client.PeriodUtilsSerializer;
import ar.com.dcsys.pr.CSD;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;

public class PeriodSerializer implements CSD<Period> {

	public static final Logger logger = Logger.getLogger(PeriodSerializer.class.getName());
	
	@Override
	public String toJson(Period o) {
		JSONObject jo = PeriodUtilsSerializer.toJson(o);
		return jo.toString();
	}

	@Override
	public Period read(String json) {
		logger.log(Level.WARNING,"PeriodSerializer : " + json);
		try {
			JSONValue value = JSONParser.parseStrict(json);
			JSONObject obj = value.isObject();
			return PeriodUtilsSerializer.read(obj);
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage());
			return null;
		}
	}

}
