package ar.com.dcsys.gwt.data.utils.client;

import java.util.ArrayList;
import java.util.List;

import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.data.justification.GeneralJustificationDate;
import ar.com.dcsys.data.justification.JustificationDate;
import ar.com.dcsys.data.period.Period;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.report.Report;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;

public class ReportUtilsSerializer {
	
	public static JSONArray toJsonArray(List<Report> reports) {
		if (reports == null) {
			return null;
		}
		JSONArray reportsObj = new JSONArray();
		for (int i = 0; i < reports.size(); i++) {
			reportsObj.set(i, toJson(reports.get(i)));
		}
		return reportsObj;
	}
	
	public static JSONObject toJson(Report report) {
		
		JSONObject reportObj = new JSONObject();
		
		//person
		Person person = report.getPerson();
		if (person != null) {
			reportObj.put("person", PersonUtilsSerializer.toJson(person));
		}
		
		//group
		Group group = report.getGroup();
		if (group != null) {
			reportObj.put("group", GroupUtilsSerializer.toJson(group));
		}
		
		//groups
		List<Group> groups = report.getGroups();
		JSONArray groupsObj = GroupUtilsSerializer.toJsonArray(groups);
		if (groupsObj != null) {
			reportObj.put("groups", groupsObj);
		}
		
		//period
		Period period = report.getPeriod();
		if (period != null) {
			reportObj.put("period", PeriodUtilsSerializer.toJson(period));
		}
		
		//justifications
		List<JustificationDate> justifications = report.getJustifications();
		JSONArray justificationsObj = JustificationDateUtilsSerializer.toJsonArray(justifications);
		if (justificationsObj != null) {
			reportObj.put("justifications", justificationsObj);
		}
		
		//gjustification
		List<GeneralJustificationDate> gjustification = report.getGjustifications();
		JSONArray gjustificationObj = GeneralJustificationDateUtilsSerializer.toJsonArray(gjustification);
		if (gjustificationObj != null) {
			reportObj.put("gjustification", gjustificationObj);
		}
		
		//minutes
		Long minutes = report.getMinutes();
		if (minutes != null) {
			reportObj.put("minutes", new JSONNumber(minutes));
		}
		
		//abscence
		Boolean abscence = report.isAbscence();
		if (abscence != null) {
			reportObj.put("abscence", JSONBoolean.getInstance(abscence));
		}
		
		return reportObj;
	}
	
	public static List<Report> read(JSONArray reportsArray) {
		
		List<Report> reports = new ArrayList<>();
		
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
	
	public static Report read(JSONObject reportObj) {
		
		if (reportObj == null) {
			return null;
		}
		
		Report report = new Report();
		
		//person
		JSONValue personVal = reportObj.get("person");
		if (personVal != null) {
			JSONObject personObj = personVal.isObject();
			if (personObj != null) {
				report.setPerson(PersonUtilsSerializer.read(personObj));
			}
		}
		
		//group
		JSONValue groupVal = reportObj.get("group");
		if (groupVal != null) {
			JSONObject groupObj = groupVal.isObject();
			if (groupObj != null) {
				report.setGroup(GroupUtilsSerializer.read(groupObj));
			}
		}
		
		//groups
		JSONValue groupsVal = reportObj.get("groups");
		if (groupsVal != null) {
			JSONArray groupsArray = groupsVal.isArray();
			if (groupsArray != null) {
				List<Group> groups = GroupUtilsSerializer.read(groupsArray);
				report.setGroups(groups);
			}
		}
		
		
		//period
		JSONValue periodVal = reportObj.get("period");
		if (periodVal != null) {
			JSONObject periodObj = periodVal.isObject();
			if (periodObj != null) {
				report.setPeriod(PeriodUtilsSerializer.read(periodObj));
			}
		}
		
		
		//justifications
		JSONValue justificationsVal = reportObj.get("justifications");
		if (justificationsVal != null) {
			JSONArray justificationsArray = justificationsVal.isArray();
			if (justificationsArray != null) {
				List<JustificationDate> justifications = JustificationDateUtilsSerializer.read(justificationsArray);
				report.setJustifications(justifications);
			}
		}
		
		//gjustification
		JSONValue gjustificationVal = reportObj.get("gjustification");
		if (gjustificationVal != null) {
			JSONArray gjustificationArray = gjustificationVal.isArray();
			if (gjustificationArray != null) {
				List<GeneralJustificationDate> gjds = GeneralJustificationDateUtilsSerializer.read(gjustificationArray);
				report.setGjustifications(gjds);
			}
		}
		
		//minutes
		JSONValue minutesVal = reportObj.get("minutes");
		if (minutesVal != null) {
			double minutesDouble = minutesVal.isNumber().doubleValue();
			report.setMinutes((long)minutesDouble);
		}
		
		//abscence	
		JSONValue abscenceVal = reportObj.get("abscence");
		if (abscenceVal != null) {
			Boolean abscence = abscenceVal.isBoolean().booleanValue();
			report.setAbscence(abscence);
		}
		
		return report;
	}

}
