package ar.com.dcsys.gwt.data.assistance.client;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.report.Report;
import ar.com.dcsys.gwt.data.utils.client.ReportUtilsSerializer;
import ar.com.dcsys.pr.CSD;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;

public class ReportListSerializer implements CSD<List<Report>> {
	
	private static final Logger logger = Logger.getLogger(ReportListSerializer.class.getName());
	
	@Override
	public String toJson(List<Report> o) {
		if (o == null) {
			logger.log(Level.WARNING,"reports == null");
			return "";
		}
		
		JSONArray array = ReportUtilsSerializer.toJsonArray(o);
		return array.toString();
	}
	
	@Override
	public List<Report> read(String json) {
		logger.log(Level.WARNING,"reportListSerializer : " + json);
		try {
			JSONValue value = JSONParser.parseStrict(json);
			JSONObject obj = value.isObject();
			JSONArray array = obj.get("list").isArray();
			
			if (array == null) {
				return null;
			}
			
			List<Report> reports = ReportUtilsSerializer.read(array);
			return reports;			
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage());
			return null;
		}
	}

}
