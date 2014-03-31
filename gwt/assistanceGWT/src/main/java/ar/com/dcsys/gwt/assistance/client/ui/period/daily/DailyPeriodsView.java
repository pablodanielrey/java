package ar.com.dcsys.gwt.assistance.client.ui.period.daily;

import java.util.Date;
import java.util.List;

import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.data.justification.Justification;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.gwt.assistance.client.ui.period.PERIODFILTER;
import ar.com.dcsys.gwt.assistance.shared.DailyPeriod;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;

public interface DailyPeriodsView extends IsWidget {

	public void setPresenter(Presenter p);
	public void clear();
	public void clearPeriodData();
	public void clearJustificationData();

	public void setPeriodFilterSelectionModel(SingleSelectionModel<PERIODFILTER> selection);
	public void setGroupSelectionModel(SingleSelectionModel<Group> selection);
	public void setPeriodSelectionModel(MultiSelectionModel<PersonPeriodContainer> selection);

	public void setJustificationSelectionModel(SingleSelectionModel<Justification> selection);
	public void setJustifications(List<Justification> types);
	
	public void setPeriodFilterValues(List<PERIODFILTER> values);
	public void setGroups(List<Group> groups);
	public void setPeriods(List<PersonPeriodContainer> periods);
	
	public void enableJustify(boolean t);
	public String getNotes();
	
	public Date getDate();
	public void setDate(Date date);
	
	public void redrawPeriods();
	
	public class PersonPeriodContainer {
		public Person person;
		public DailyPeriod period;
	}
	
	public interface Presenter {
		public void justify();
//		public JustificationDateProxy justified(PersonPeriodContainer p);
//		public GeneralJustificationDateProxy generalJustified(PersonPeriodContainer p);
//		public AssistancePersonDataProxy assistanceData(PersonProxy person);
//		public void removeJustification(JustificationDateProxy j);
		public void find();
		public void export();
	}
	
	
}
