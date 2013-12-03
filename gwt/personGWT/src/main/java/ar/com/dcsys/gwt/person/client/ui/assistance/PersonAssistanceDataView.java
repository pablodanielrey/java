package ar.com.dcsys.gwt.person.client.ui.assistance;

import ar.com.dcsys.data.person.Person;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.SingleSelectionModel;

public interface PersonAssistanceDataView extends IsWidget {

	public void setPresenter(Presenter presenter);
	public void clear();
	
	public String getPinNumber();
	public void setPinNumber(String pin);
	
	public String getNotes();
	public void setNotes(String notes);
	
	public void setReadOnly(boolean v);
	public void setVisible(boolean v);
	
	public interface Presenter {
		public void setSelectionModel(SingleSelectionModel<Person> selection);
		public void persist();
	}
	
}
