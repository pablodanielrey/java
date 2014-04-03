package ar.com.dcsys.server.assistance;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.com.dcsys.data.group.Group;

public class ReportSummary {

	public static final String START = "start";
	public static final String END = "end";
	public static final String GROUP = "group";

	private Date start;
	private Date end;
	private Group group;
	
	private List<Report> reports = new ArrayList<>();
	private Long minutes;

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public List<Report> getReports() {
		return reports;
	}

	public void setReports(List<Report> reports) {
		this.reports = reports;
	}

	public void calcReportData() {
		minutes = 0l;
		for (Report r : reports) {
			r.calcReportData();
			minutes = minutes + r.getMinutes();
		}
	}
	
}