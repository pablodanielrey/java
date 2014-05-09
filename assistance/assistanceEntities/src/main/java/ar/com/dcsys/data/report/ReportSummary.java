package ar.com.dcsys.data.report;

import java.util.Date;
import java.util.List;

public interface ReportSummary {

	public Date getStart();
	public Date getEnd();
	public List<? extends Report> getReports();
		
	
}
