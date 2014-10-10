package ar.com.dcsys.gwt.person.client.ui.manage;

import java.util.ArrayList;
import java.util.List;

import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.person.PersonTypeEnum;
import ar.com.dcsys.gwt.person.client.common.filter.FilterPerson;
import ar.com.dcsys.gwt.person.client.common.filter.FilterPersonDni;
import ar.com.dcsys.gwt.person.client.common.filter.FilterPersonName;
import ar.com.dcsys.gwt.person.client.ui.cell.PersonCell;
import ar.com.dcsys.gwt.person.client.ui.common.PersonResources;
import ar.com.dcsys.utils.PersonSort;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.IdentityColumn;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SingleSelectionModel;

public class ManagePersons extends Composite implements ManagePersonsView {

	private static ManagePersonsUiBinder uiBinder = GWT.create(ManagePersonsUiBinder.class);
	
	interface ManagePersonsUiBinder extends UiBinder<Widget, ManagePersons> {
	}
	

	// filtros de dni y nombre
	private final FilterPerson[] filters = new FilterPerson[] { new FilterPersonDni() , new FilterPersonName() };
	
	
	private Presenter p;
	private final List<Person> personsData;
	private final List<Person> personsFilteredData;
	private final PersonResources resources = GWT.create(PersonResources.class);

	private final List<PersonTypeEnum> typesCache = new ArrayList<PersonTypeEnum>();
	
	private Timer filterTimer = null;
	
	@UiField(provided=true) TextBox filter;
	@UiField(provided=true) Label usersCount;
	@UiField FlowPanel types;
	@UiField(provided=true) DataGrid<Person> persons;
	
	@UiField Button updateUsers;
	
	@UiField FlowPanel personData;
	@UiField FlowPanel personGroups;
	
	@UiHandler("updateUsers")
	public void onUpdateUsers(ClickEvent event) {
		if (p != null) {
			p.updateUsers();
		}
	}
	
	@Override
	public Panel getPersonGroupsPanel() {
		return personGroups;
	}
	
	@Override
	public Panel getPersonDataPanel() {
		return personData;
	}
	
	private void clearTypes() {
		for (int i = 0; i < this.types.getWidgetCount(); i++) {
			CheckBox c = (CheckBox)this.types.getWidget(i);
			c.setValue(false);
		}
	}
	
	@Override
	public void setAllTypes(List<PersonTypeEnum> types) {
		/*
		 * TODO:lo comente para que no filtre los persons
		 */
		//types = PersonTypeUtils.filter(types);
		
		typesCache.clear();
		typesCache.addAll(types);
		
		/*final ClickHandler handler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (p == null) {
					return;
				}
				p.updateUsers();
			}
		}; */
		
		this.types.clear();
		for (PersonTypeEnum pt : types) {
			CheckBox c = new CheckBox(pt.getDescription());
			c.setValue(false);
			//c.addClickHandler(handler);
			this.types.add(c);
		}
		
		CheckBox c = new CheckBox("Sin Tipo");
		c.setValue(false);
		//c.addClickHandler(handler);
		this.types.add(c);
	}	
	
	@Override
	public List<PersonTypeEnum> getSelectedTypes() {
		List<PersonTypeEnum> selected = new ArrayList<PersonTypeEnum>();
		for (int i = 0; i < this.types.getWidgetCount(); i++) {
			CheckBox c = (CheckBox)this.types.getWidget(i);
			if (c.getValue()) {
				for (PersonTypeEnum pt : typesCache) {
					if (pt.getDescription().equalsIgnoreCase(c.getText())) {
						selected.add(pt);
					}
				}
			}
		}
		return selected;
	}	
	

	/**
	 * Retorna si esta seleccionado el tipo "Sin Tipo"
	 * Seimpre es el ultimo checkbox de la lista.
	 */
	@Override
	public boolean isNoTypeSelected() {
		int w = types.getWidgetCount() - 1;
		if (w < 0) {
			return false;
		}
		return ((CheckBox)types.getWidget(w)).getValue();
		 
	}
	
	private void createFilter() {
		
		usersCount = new Label("0");		
		
		filter = new TextBox();
		filter.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (filterTimer != null) {
					filterTimer.cancel();
				}
				filterTimer = new Timer() {
					public void run() {
						filterTimer = null;
						filterPersons();							
					};
				};
				filterTimer.schedule(2000);				
			}
		});
	}
	
	private void createPersons() {
		String imageHtml = AbstractImagePrototype.create(resources.user()).getHTML();
		PersonCell pc = new PersonCell(imageHtml);
		
		IdentityColumn<Person> person = new IdentityColumn<Person>(pc);
			
		persons = new DataGrid<Person>();
		persons.addColumn(person);
	}
	
	

	
	
	
	public ManagePersons() {
		createFilter();
		createPersons();
		
		initWidget(uiBinder.createAndBindUi(this));
		
		personsData = new ArrayList<Person>();
		personsFilteredData = new ArrayList<Person>();
	}

	@Override
	public void setPresenter(Presenter p) {
		this.p = p;
	}

	@Override
	public void clear() {
		clearPersons();
		clearTypes();
	}
	
	private void clearPersons() {
		personsData.clear();
		personsFilteredData.clear();
		
		usersCount.setText("0");
		persons.setRowCount(0);
		persons.setRowData(personsData);
	}

	@Override
	public void setSelectionModel(SingleSelectionModel<Person> selection) {
		this.persons.setSelectionModel(selection);
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
	
	private void filterPersons() {
		String ft = filter.getText();
		if (ft == null || ft.trim().equals("")) {
			usersCount.setText(String.valueOf(personsData.size()));
			persons.setRowData(personsData);
			return;
		}
		
		// aplico el filtro de acuerdo a los Filter que tenga definidos.
		// ahora solo es el dni y si no el nombre.
		ft = ft.toLowerCase();
		personsFilteredData.clear();
		for (Person p : personsData) {
			for (FilterPerson f : filters) {
				if (f.checkFilter(p, ft)) {
					personsFilteredData.add(p);
					break;
				}
			}
		}
		usersCount.setText(String.valueOf(personsFilteredData.size()));
		persons.setRowData(personsFilteredData);
	}

}
