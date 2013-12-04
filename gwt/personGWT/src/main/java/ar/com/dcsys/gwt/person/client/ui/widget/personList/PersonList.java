package ar.com.dcsys.gwt.person.client.ui.widget.personList;

import java.util.List;

import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.gwt.person.client.ui.cell.PersonCell;
import ar.com.dcsys.gwt.person.client.ui.common.PersonResources;
import ar.com.dcsys.gwt.person.client.ui.widget.PersonListWidget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.IdentityColumn;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionModel;


public class PersonList extends Composite implements PersonListWidget {

	private static PersonListUiBinder uiBinder = GWT.create(PersonListUiBinder.class);

	interface PersonListUiBinder extends UiBinder<Widget, PersonList> {	}

	private final PersonResources res = GWT.create(PersonResources.class);
	
	@UiField(provided=true) DataGrid<Person> persons;
	
	private void createPersons() {
		String imageHtml = AbstractImagePrototype.create(res.user()).getHTML();
		PersonCell pc = new PersonCell(imageHtml);
		IdentityColumn<Person> icUser = new IdentityColumn<Person>(pc);

		persons = new DataGrid<Person>();
		persons.addColumn(icUser);
		
	}
	
	public PersonList() {
		createPersons();
		
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@Override
	public void clear() {
		persons.setRowCount(0,true);
	}
	
	@Override
	public void setPersons(List<Person> persons) {
		this.persons.setRowData(persons);
	}

	@Override
	public void setSelectionModel(SelectionModel<Person> selection) {
		persons.setSelectionModel(selection);
	}

}
