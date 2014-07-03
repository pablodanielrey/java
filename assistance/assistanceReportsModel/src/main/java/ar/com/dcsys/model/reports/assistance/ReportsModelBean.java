package ar.com.dcsys.model.reports.assistance;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.report.ReportSummary;

public class ReportsModelBean implements ReportsModel {

	private final ReportDataGenerator rdg;
	
	@Inject
	public ReportsModelBean(ReportDataGenerator rdg) {
		this.rdg = rdg;
	}
	
	@Override
	public ReportSummary reportPeriods(Date start, Date end, List<Person> persons) throws IOException {
		ReportSummary rs = rdg.getReport(start, end, persons);
		return rs;
	}	
	
}
