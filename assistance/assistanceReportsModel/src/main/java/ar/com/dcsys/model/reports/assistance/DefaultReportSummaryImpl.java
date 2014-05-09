package ar.com.dcsys.model.reports.assistance;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.com.dcsys.data.report.Report;
import ar.com.dcsys.data.report.ReportSummary;

public class DefaultReportSummaryImpl implements ReportSummary {

	public static final String START = "start";
	public static final String END = "end";
	public static final String GROUP = "group";

	private Date start;
	private Date end;
	
	private List<DefaultReportImpl> reports = new ArrayList<>();
	private Long minutes;

	
	@Override
	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	@Override
	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	@Override
	public List<? extends Report> getReports() {
		return reports;
	}

	public void addReport(DefaultReportImpl r) {
		reports.add(r);
	}
	
	public void setReports(List<DefaultReportImpl> reports) {
		this.reports = reports;
	}

	public void calcReportData() {
		minutes = 0l;
		for (DefaultReportImpl r : reports) {
			r.calcReportData();
			minutes = minutes + r.getMinutes();
		}
	}
	
}
