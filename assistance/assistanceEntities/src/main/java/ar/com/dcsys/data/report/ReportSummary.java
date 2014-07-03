package ar.com.dcsys.data.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReportSummary {

	public static final String START = "start";
	public static final String END = "end";
	public static final String GROUP = "group";

	private Date start;
	private Date end;
	
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

	
	public List<? extends Report> getReports() {
		return reports;
	}

	public void addReport(Report r) {
		reports.add(r);
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
