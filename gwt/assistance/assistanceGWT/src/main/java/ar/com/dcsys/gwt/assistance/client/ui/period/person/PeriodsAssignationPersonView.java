package ar.com.dcsys.gwt.assistance.client.ui.period.person;

import java.util.Date;
import java.util.List;

import ar.com.dcsys.data.period.PeriodAssignation;
import ar.com.dcsys.data.period.PeriodType;
import ar.com.dcsys.data.person.Person;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.SingleSelectionModel;

public interface PeriodsAssignationPersonView extends IsWidget{
	
	public void setPresenter (Presenter p);
	public void clear();
	public void clearPeriodsData();
	
	public void setPeriodsData(List<PeriodAssignation> periodsAssignation);	
	public void setPersons(List<Person> persons);
	public void setSelectionModel(SingleSelectionModel<Person> selectionModel);
	public void setTypes(List<PeriodType> types);
	public void setTypesSelectionModel(SingleSelectionModel<PeriodType> selection);
	public Date getDate();
	public void setEnabledNewPeriod(boolean enabled);
	
	public interface Presenter {
		public void remove(PeriodAssignation periodAssignation);
		public void create();
		public void updatePersons(boolean isAll);
	}


}
