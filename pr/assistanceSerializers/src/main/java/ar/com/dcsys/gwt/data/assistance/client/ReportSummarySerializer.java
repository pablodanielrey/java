package ar.com.dcsys.gwt.data.assistance.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.report.ReportSummary;
import ar.com.dcsys.gwt.data.utils.client.ReportSummaryUtilsSerializer;
import ar.com.dcsys.pr.CSD;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;

public class ReportSummarySerializer implements CSD<ReportSummary> {

	public static final Logger logger = Logger.getLogger(ReportSummarySerializer.class.getName());
	
	@Override
	public String toJson(ReportSummary o) {
		JSONObject jo = ReportSummaryUtilsSerializer.toJson(o);
		return jo.toString();
	}

	@Override
	public ReportSummary read(String json) {
		logger.log(Level.WARNING,"ReportSummary : " + json);
		try {
			JSONValue value = JSONParser.parseStrict(json);
			JSONObject obj = value.isObject();
			return ReportSummaryUtilsSerializer.read(obj);
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage());
			return null;
		}
	}

}
