package ar.com.dcsys.gwt.person.client.ui.group.persons.members;

import java.util.List;

import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.data.person.Person;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;

public interface GroupMemberView extends IsWidget {

	public void setPresenter(Presenter presenter);
	public void clear();
	
	public void setPersons(List<Person> persons);
	public void setPersonsSelectionModel(MultiSelectionModel<Person> selection);
	
	
	public interface Presenter {
		public void setSelectionModel(SingleSelectionModel<Group> selection);	
	}
	
}
