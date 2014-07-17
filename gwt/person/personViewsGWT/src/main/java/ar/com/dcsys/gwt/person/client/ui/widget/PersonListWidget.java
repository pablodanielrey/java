package ar.com.dcsys.gwt.person.client.ui.widget;

import java.util.List;

import ar.com.dcsys.data.person.Person;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.SelectionModel;


public interface PersonListWidget extends IsWidget {

	public void setPersons(List<Person> persons);
	public void clear();
	public void setSelectionModel(SelectionModel<Person> selection);	
	
}
