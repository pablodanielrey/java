package ar.com.dcsys.gwt.person.client.ui.reports;

import com.google.gwt.user.client.ui.IsWidget;

public interface PersonReportView extends IsWidget {
	
	public void setPresenter(Presenter p);
	
	public void clear();
	public void addReport(String url);
	
	public interface Presenter {
		public void generateReport();
		public void updateReports();
	}
	
}
