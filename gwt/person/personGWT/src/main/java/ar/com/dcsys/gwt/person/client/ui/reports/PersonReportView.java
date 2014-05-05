package ar.com.dcsys.gwt.person.client.ui.reports;

import ar.com.dcsys.data.document.Document;

import com.google.gwt.user.client.ui.IsWidget;

public interface PersonReportView extends IsWidget {
	
	public void setPresenter(Presenter p);
	
	public void clear();
	public void addReport(Document d);
	
	public interface Presenter {
		public void generateReport();
		public void updateReports();
	}
	
}
