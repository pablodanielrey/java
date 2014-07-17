package ar.com.dcsys.gwt.person.client.ui.group.persons.out;

import java.util.List;

import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.person.PersonType;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;

public interface GroupOutPersonsView extends IsWidget {

	public void setPresenter(Presenter presenter);
	public void clear();
	
	public void setPersonsSelectionModel(MultiSelectionModel<Person> selection);
	public void setPersons(List<Person> persons);
	
	public void setPersonTypes(List<PersonType> types);
	public void setTypesSelectionModel(MultiSelectionModel<PersonType> selection);
	
	public interface Presenter {
		public void setSelectionModel(SingleSelectionModel<Group> selection);
		public void setPersonsSelectionModel(MultiSelectionModel<Person> selection);
	}
	
}
