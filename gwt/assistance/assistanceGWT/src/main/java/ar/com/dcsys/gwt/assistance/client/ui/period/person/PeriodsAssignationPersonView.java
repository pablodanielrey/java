package ar.com.dcsys.gwt.assistance.client.ui.period.person;

import java.util.Date;
import java.util.List;

import ar.com.dcsys.data.common.Days;
import ar.com.dcsys.data.period.PeriodAssignation;
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
	
	public void setTypes(List<PeriodTypeEnum> types);
	public void setTypesSelectionModel(SingleSelectionModel<PeriodTypeEnum> selection);
	
	public List<Days> getDays();
	
	public Date getDate();
	public void setEnabledNewPeriod(boolean enabled);
	
	public enum PeriodTypeEnum {
		NULL("Nulo"),DAILY("Diario"),WATCHMAN("Sereno"),SYSTEM("Sistema");
		
		private String name;
		
		private PeriodTypeEnum(String name) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
	}
	
	public interface Presenter {
		public void remove(PeriodAssignation periodAssignation);
		public void create();
		public void updatePersons(boolean isAll);
	}


}
