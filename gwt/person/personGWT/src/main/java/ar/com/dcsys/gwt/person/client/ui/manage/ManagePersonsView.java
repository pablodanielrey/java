package ar.com.dcsys.gwt.person.client.ui.manage;

import java.util.List;

import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.person.PersonTypeEnum;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.view.client.SingleSelectionModel;

public interface ManagePersonsView extends IsWidget {

	public void setPresenter(Presenter p);
	public void clear();

	public void setSelectionModel(SingleSelectionModel<Person> person);
	public void setPersons(List<Person> persons);
	
	public void setAllTypes(List<PersonTypeEnum> types);
	public List<PersonTypeEnum> getSelectedTypes();
	public boolean isNoTypeSelected();
	
	public Panel getPersonDataPanel();
	public Panel getPersonGroupsPanel();
	

	
	public interface Presenter {
		public void updateUsers();
	}
	
}
