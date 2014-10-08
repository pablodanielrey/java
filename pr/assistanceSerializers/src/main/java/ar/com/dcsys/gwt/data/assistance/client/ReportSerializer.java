package ar.com.dcsys.gwt.data.assistance.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.report.Report;
import ar.com.dcsys.gwt.data.utils.client.ReportUtilsSerializer;
import ar.com.dcsys.pr.CSD;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;

public class ReportSerializer implements CSD<Report> {
	
	public static final Logger logger = Logger.getLogger(ReportSerializer.class.getName());
	
	@Override
	public String toJson(Report o) {
		JSONObject jo = ReportUtilsSerializer.toJson(o);
		return jo.toString();
	}

	@Override
	public Report read(String json) {
		logger.log(Level.WARNING,"ReportSerializer : " + json);
		try {
			JSONValue value = JSONParser.parseStrict(json);
			JSONObject obj = value.isObject();
			return ReportUtilsSerializer.read(obj);
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage());
			return null;
		}
	}
	
}
