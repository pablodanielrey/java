package ar.com.dcsys.gwt.assistance.client.ui.period;

import java.util.Date;
import java.util.List;

import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.data.justification.JustificationDate;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.report.Report;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;

public interface PeriodsView extends IsWidget {
	
	public void setPresenter(Presenter p);
	public void clear();
	
	public Date getStart();
	public void setStart(Date date);
	public Date getEnd();
	public void setEnd(Date date);
	
	public void setPeriodFilterValues(List<PERIODFILTER> values);
	public void setPeriodFilterSelectionModel(SingleSelectionModel<PERIODFILTER> selection);

	public void setGroups(List<Group> groups);
	public void setGroupSelectionModel(SingleSelectionModel<Group> selection);
	
	public void setPersons(List<Person> persons);
	public void setPersonSelectionModel(SingleSelectionModel<Person> selection);
	
	public void setPeriods(List<Report> periods);
	public void setPeriodSelectionModel(MultiSelectionModel<Report> selection);
	public void redrawPeriods();
	public void clearPeriodData();
	
	public interface Presenter {
	/*	
		public GeneralJustificationDate generalJustified(Period p);*/
		public void dateChanged();
		public void justify();
		public void removeJustification(JustificationDate j);
	}

}
