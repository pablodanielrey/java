package ar.com.dcsys.gwt.data.utils.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.com.dcsys.data.report.Report;
import ar.com.dcsys.data.report.ReportSummary;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

public class ReportSummaryUtilsSerializer {
	
	private static final DateTimeFormat df = DateTimeFormat.getFormat("HH:mm:ss dd/MM/yyyy");

	public static JSONArray toJsonArray(List<ReportSummary> reports) {
		if (reports == null) {
			return null;
		}
		JSONArray reportsObj = new JSONArray();
		for (int i = 0; i < reports.size(); i++) {
			reportsObj.set(i, toJson(reports.get(i)));
		}
		return reportsObj;
	}
	
	public static JSONObject toJson(ReportSummary reportSum) {
		
		JSONObject reportObj = new JSONObject();
		
		//start
		Date start = reportSum.getStart();
		if (start != null) {
			String startStr = df.format(start);
			reportObj.put("start", new JSONString(startStr));
		}
		
		//end
		Date end = reportSum.getEnd();
		if (end != null) {
			String endStr = df.format(end);
			reportObj.put("end", new JSONString(endStr));
		}
		
		//reports
		List<Report> reports = reportSum.getReports();
		JSONArray reportsObj = ReportUtilsSerializer.toJsonArray(reports);
		if (reportsObj != null) {
			reportObj.put("reports", reportObj);
		}
		
		//minutes
		Long minutes = reportSum.getMinutes();
		if (minutes != null) {
			reportObj.put("minutes", new JSONNumber(minutes));
		}
		
		return reportObj;
	}
	
	public static List<ReportSummary> read(JSONArray reportsArray) {
		
		List<ReportSummary> reports = new ArrayList<>();
		
		if (reportsArray != null) {
			for (int i = 0; i < reportsArray.size(); i++) {
				JSONObject reportObj = reportsArray.get(i).isObject();
				if (reportObj != null) {
					reports.add(read(reportObj));
				}
			}
		}
		
		return reports;
	}
	
	public static ReportSummary read(JSONObject reportSumObj) {
		
		if (reportSumObj == null) {
			return null;
		}
		
		ReportSummary reportSum = new ReportSummary();
		
		//start
		JSONValue startVal = reportSumObj.get("start");
		if (startVal != null) {
			String startStr = startVal.isString().toString();
			Date start = df.parse(startStr);
			reportSum.setStart(start);
		}
		
		
		//end
		JSONValue endVal = reportSumObj.get("end");
		if (endVal != null) {
			String endStr = endVal.isString().toString();
			Date end = df.parse(endStr);
			reportSum.setEnd(end);
		}
		
		//reports
		JSONValue reportsVal = reportSumObj.get("reports");
		if (reportsVal != null) {
			JSONArray reportsArray = reportsVal.isArray();
			if (reportsArray != null) {
				List<Report> reports = ReportUtilsSerializer.read(reportsArray);
				reportSum.setReports(reports);
			}
		}
		
		//minutes
		JSONValue minutesVal = reportSumObj.get("minutes");
		if (minutesVal != null) {
			double minutesDouble = minutesVal.isNumber().doubleValue();
			reportSum.setMinutes((long)minutesDouble);
		}
		
		return reportSum;
	}
}
