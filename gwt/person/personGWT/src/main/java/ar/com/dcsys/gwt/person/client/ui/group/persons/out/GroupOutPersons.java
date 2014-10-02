package ar.com.dcsys.gwt.person.client.ui.group.persons.out;

import java.util.ArrayList;
import java.util.List;

import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.person.PersonType;
import ar.com.dcsys.gwt.person.client.common.PersonTypesSort;
import ar.com.dcsys.gwt.person.client.common.filter.FilterPerson;
import ar.com.dcsys.gwt.person.client.common.filter.FilterPersonDni;
import ar.com.dcsys.gwt.person.client.common.filter.FilterPersonName;
import ar.com.dcsys.gwt.person.client.ui.cell.PersonCell;
import ar.com.dcsys.gwt.person.client.ui.common.PersonResources;
import ar.com.dcsys.utils.PersonSort;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.IdentityColumn;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.MultiSelectionModel;

public class GroupOutPersons extends Composite implements GroupOutPersonsView {

	private static GroupOutPersonsUiBinder uiBinder = GWT.create(GroupOutPersonsUiBinder.class);

	interface GroupOutPersonsUiBinder extends UiBinder<Widget, GroupOutPersons> {
	}
	
	private final FilterPerson[] filtersPersons = new FilterPerson[] { new FilterPersonDni(), new FilterPersonName() };	
	
	@UiField(provided=true) TextBox filterPerson;
	@UiField(provided=true) Label count;
	@UiField(provided=true) DataGrid<Person> persons;
	@UiField FlowPanel personTypes;
	
	private final List<PersonType> personTypesCache = new ArrayList<PersonType>();
	
	private final List<Person> personsData;
	private final List<Person> personsFilteredData;
	private Timer filterTimerPersons = null;
	
	private GroupOutPersonsView.Presenter presenter;
	
	private MultiSelectionModel<PersonType> personTypesSelection;
	
	final ClickHandler handler = new ClickHandler() {
		@Override
		public void onClick(ClickEvent event) {
			if (personTypesSelection == null) {
				return;
			}
			List<PersonType> types = getSelectedPersonTypes();
			personTypesSelection.clear();
			for (PersonType type : types) {
				personTypesSelection.setSelected(type, true);
			}
		}
	};
	
	private final PersonResources resources = GWT.create(PersonResources.class);
	
	public GroupOutPersons() {
		createPersons();
		createFilterPerson();
		initWidget(uiBinder.createAndBindUi(this));
		
		personsData = new ArrayList<Person>();
		personsFilteredData = new ArrayList<Person>();	
	}
	
	private void createPersons() {
		String imageHtml = AbstractImagePrototype.create(resources.user()).getHTML();
		PersonCell pc = new PersonCell(imageHtml);
		IdentityColumn<Person> person = new IdentityColumn<Person>(pc);
		
		persons = new DataGrid<Person>();
		persons.addColumn(person,"No Integrantes");
	}
	
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
	
	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}
	
	public void clear() {
		clearPersons();
		clearFilters();
	}
	
	private void clearPersons() {
		personsData.clear();
		persons.setRowCount(0);
		persons.setRowData(personsData);
	}
	
	private void clearFilters() {
		personsFilteredData.clear();		
		count.setText("0");	
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
	
	@Override
	public void setPersonsSelectionModel(MultiSelectionModel<Person> selection) {
		persons.setSelectionModel(selection);
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
	
	private List<PersonType> getSelectedPersonTypes() {
		List<PersonType> selected = new ArrayList<PersonType>();
		for (int i = 0; i < this.personTypes.getWidgetCount(); i++) {
			CheckBox c = (CheckBox)this.personTypes.getWidget(i);
			if (c.getValue()) {
				for (PersonType pt : personTypesCache) {
					if (pt.getName().equalsIgnoreCase(c.getText())) {
						selected.add(pt);
					}
				}
			}
		}
		return selected;
	}	
	
	@Override
	public void setTypesSelectionModel(MultiSelectionModel<PersonType> selection) {
		personTypesSelection = selection;
	}
	
	@Override
	public void setPersonTypes(List<PersonType> types) {
		personTypesCache.clear();
		PersonTypesSort.sort(types);
		personTypesCache.addAll(types);
		

		
		this.personTypes.clear();
		for (PersonType pt : types) {
			CheckBox c = new CheckBox(pt.getName());
			c.setValue(false);
			c.addClickHandler(handler);
			this.personTypes.add(c);
		}
	}	

}
