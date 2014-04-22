package ar.com.dcsys.gwt.person.client.ui.widget;

import ar.com.dcsys.data.person.Person;

import com.google.gwt.view.client.MultiSelectionModel;


public interface SelectionPersonListWidget extends PersonListWidget {

	public void setListSelectionModel(MultiSelectionModel<Person> selection);
	public void setReadOnly(boolean v);
	
}
