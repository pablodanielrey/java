package ar.com.dcsys.gwt.assistance.client.ui.justification.person;

import java.util.Date;
import java.util.List;

import ar.com.dcsys.data.justification.Justification;
import ar.com.dcsys.data.justification.JustificationDate;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.gwt.assistance.client.activity.justification.JustificationStatistic;
import ar.com.dcsys.gwt.assistance.client.ui.justification.widget.SelectionJustificationDateListWidget;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;


public interface JustificationPersonView  extends IsWidget{
	public void setPresenter(Presenter p);
	public void clear();
	public void clearJustificationData();
	public void setJustificationData(List<JustificationDate> jds);

	
	public void setSelectionModel(MultiSelectionModel<Person> person);
	public void setPersons(List<Person> persons);
	
	
	
	public void setTypes(List<Justification> types);
	public void setTypesSelectionModel(SingleSelectionModel<Justification> selection);	
	
	public void setStatistics(List<JustificationStatistic> jds);
	public void clearStatistic();
	
	public Date getStart();
	public Date getEnd();
	
	public String getNotes();
	public void setNotes(String notes);
	
	public void setEnabledJustifications(boolean b);
	public void clearJustificationDateList();
	
	public void setJustificationDateSelectionModel(MultiSelectionModel<JustificationDate> selectionJustificationDate);
	
	public SelectionJustificationDateListWidget getViewSelectionJustificationDate();
	
	public void clearPersonData();
	public void setEnabledRemoveButton(boolean b);
	
	
	public interface Presenter {
		public void persist();
		public void clearPersonsSelection();
		public void clearJustificationSelection();
		public void findBy (Date start, Date end);
		public void removeJustificationDates();
		public void generateStatistics();
	}
}
