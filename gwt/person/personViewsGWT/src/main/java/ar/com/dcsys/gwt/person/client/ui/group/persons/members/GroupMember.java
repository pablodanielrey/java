package ar.com.dcsys.gwt.person.client.ui.group.persons.members;

import java.util.ArrayList;
import java.util.List;

import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.gwt.person.client.common.filter.FilterPerson;
import ar.com.dcsys.gwt.person.client.common.filter.FilterPersonDni;
import ar.com.dcsys.gwt.person.client.common.filter.FilterPersonName;
import ar.com.dcsys.gwt.person.client.ui.cell.PersonCell;
import ar.com.dcsys.gwt.person.client.ui.common.PersonResources;
import ar.com.dcsys.utils.PersonSort;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.IdentityColumn;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.MultiSelectionModel;

public class GroupMember extends Composite implements GroupMemberView {

	private static GroupMemberUiBinder uiBinder = GWT.create(GroupMemberUiBinder.class);

	interface GroupMemberUiBinder extends UiBinder<Widget, GroupMember> {
	}
	
	@UiField(provided=true) TextBox filterPerson;
	@UiField(provided=true) Label count;
	@UiField(provided=true) DataGrid<Person> persons;
	
	private final List<Person> personsData;
	private final List<Person> personsFilteredData;
	private Timer filterTimerPersons = null;	
	private final FilterPerson[] filtersPersons = new FilterPerson[] { new FilterPersonDni(), new FilterPersonName() };	
	
	private MultiSelectionModel<Person> personsSelection;
	
	private GroupMemberView.Presenter presenter;
	
	private final PersonResources resources = GWT.create(PersonResources.class);

	private void createFilterPerson() {
		
		count = new Label("0");
		
		filterPerson = new TextBox();
		filterPerson.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (filterTimerPersons != null) {
					filterTimerPersons.cancel();
				}
				filterTimerPersons = new Timer() {
					public void run() {
						filterTimerPersons = null;
						filterPersons();							
					};
				};
				filterTimerPersons.schedule(2000);				
			}
		});
	}	
	
	private void createMembers() {
		String imageHtml = AbstractImagePrototype.create(resources.user()).getHTML();
		PersonCell pc = new PersonCell(imageHtml);
		
		IdentityColumn<Person> person = new IdentityColumn<Person>(pc);		
		
		persons = new DataGrid<Person>();
		persons.addColumn(person,"Integrantes");				
	}
	
	public GroupMember() {
		createFilterPerson();
		createMembers();
		
		initWidget(uiBinder.createAndBindUi(this));
		
		personsData = new ArrayList<Person>();
		personsFilteredData = new ArrayList<Person>();
	}
	
	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}
	
	public void clear() {
		personsData.clear();
		personsFilteredData.clear();		
		count.setText("0");
		
		if (personsSelection != null) {
			personsSelection.clear();
		}
		
		persons.setRowCount(0);
		persons.setRowData(personsData);			
	}
	
	@Override
	public void setPersons(List<Person> persons) {
		personsData.clear();
		if (persons == null) {
			return;
		}
		PersonSort.sort(persons);
		personsData.addAll(persons);
		filterPersons();	
	}
	
	@Override
	public void setPersonsSelectionModel(MultiSelectionModel<Person> selection) {
		this.personsSelection = selection;
		persons.setSelectionModel(selection);
	}
	
	private void filterPersons() {
		String ft = filterPerson.getText();
		if (ft == null || ft.trim().equals("")) {
			count.setText(String.valueOf(personsData.size()));
			persons.setRowData(personsData);
			return;
		}
		// aplico el filtro de acuerdo a los Filter que tenga definidos.
		// ahora solo es el dni y si no el nombre.
		personsFilteredData.clear();
		for (Person p : personsData) {
			for (FilterPerson f : filtersPersons) {
				if (f.checkFilter(p, ft)) {
					personsFilteredData.add(p);
					break;
				}
			}
		}
		count.setText(String.valueOf(personsFilteredData.size()));
		persons.setRowData(personsFilteredData);
	}	

}
