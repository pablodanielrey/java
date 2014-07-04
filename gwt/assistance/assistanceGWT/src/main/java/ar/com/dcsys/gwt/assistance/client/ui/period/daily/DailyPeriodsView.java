package ar.com.dcsys.gwt.assistance.client.ui.period.daily;

import java.util.Date;
import java.util.List;

import ar.com.dcsys.assistance.entities.AssistancePersonData;
import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.data.justification.JustificationDate;
import ar.com.dcsys.data.report.Report;
import ar.com.dcsys.gwt.assistance.client.ui.period.PERIODFILTER;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;

public interface DailyPeriodsView extends IsWidget {

	public void setPresenter(Presenter p);
	public void clear();
	
	public void clearPeriodData();

	public Date getDate();
	public void setDate(Date date);

	public void setPeriodFilterSelectionModel(SingleSelectionModel<PERIODFILTER> selection);
	public void setPeriodFilterValues(List<PERIODFILTER> values);
	
	
	public void setGroupSelectionModel(SingleSelectionModel<Group> selection);
	public void setGroups(List<Group> groups);

	public void setPeriods(List<Report> periods);
	public void setPeriodSelectionModel(MultiSelectionModel<Report> selection);
	
	public void redrawPeriods();
		
	public interface Presenter {
		public void findPeriods();
		public AssistancePersonData assistanceData();
		public void justify();
		public void removeJustification(JustificationDate j);
	}
	
	
}
