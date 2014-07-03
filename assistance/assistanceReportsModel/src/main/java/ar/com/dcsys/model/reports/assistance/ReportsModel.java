package ar.com.dcsys.model.reports.assistance;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.report.ReportSummary;

public interface ReportsModel {

	public ReportSummary reportPeriods(Date start, Date end, List<Person> persons) throws IOException;
	
}
