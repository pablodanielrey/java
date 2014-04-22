package ar.com.dcsys.gwt.person.client.ui.manage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ar.com.dcsys.data.person.PersonType;
import ar.com.dcsys.gwt.person.client.common.PersonValueSort;
import ar.com.dcsys.gwt.person.client.common.filter.FilterPersonValue;
import ar.com.dcsys.gwt.person.client.common.filter.FilterPersonValueDni;
import ar.com.dcsys.gwt.person.client.common.filter.FilterPersonValueName;
import ar.com.dcsys.gwt.person.client.ui.cell.PersonValueCell;
import ar.com.dcsys.gwt.person.client.ui.common.PersonResources;
import ar.com.dcsys.gwt.person.shared.PersonValueProxy;

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
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SingleSelectionModel;

public class ManagePersons extends Composite implements ManagePersonsView {

	private static ManagePersonsUiBinder uiBinder = GWT.create(ManagePersonsUiBinder.class);
	
	interface ManagePersonsUiBinder extends UiBinder<Widget, ManagePersons> {
	}
	

	// filtros de dni y nombre
	private final FilterPersonValue[] filters = new FilterPersonValue[] { new FilterPersonValueDni() , new FilterPersonValueName() };
	
	
	private Presenter p;
	private final List<PersonValueProxy> personsData;
	private final List<PersonValueProxy> personsFilteredData;
	private final PersonResources resources = GWT.create(PersonResources.class);

	private final List<PersonType> typesCache = new ArrayList<PersonType>();
	
	private Timer filterTimer = null;
	
	@UiField(provided=true) TextBox filter;
	@UiField(provided=true) Label usersCount;
	@UiField FlowPanel types;
	@UiField(provided=true) DataGrid<PersonValueProxy> persons;
	
	@UiField FlowPanel personData;
	@UiField FlowPanel personGroups;
	
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
	public void setAllTypes(List<PersonType> types) {
		
		typesCache.clear();
		Collections.sort(types);
//		EnumPersonTypesSort.sort(types);
		typesCache.addAll(types);
		
		final ClickHandler handler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (p == null) {
					return;
				}
				p.updateUsers();
			}
		}; 
		
		this.types.clear();
		for (PersonType pt : types) {
			CheckBox c = new CheckBox(pt.getDescription());
			c.setValue(false);
			c.addClickHandler(handler);
			this.types.add(c);
		}
		
		CheckBox c = new CheckBox("Sin Tipo");
		c.setValue(false);
		c.addClickHandler(handler);
		this.types.add(c);
	}	
	
	@Override
	public List<PersonType> getSelectedTypes() {
		List<PersonType> selected = new ArrayList<PersonType>();
		for (int i = 0; i < this.types.getWidgetCount(); i++) {
			CheckBox c = (CheckBox)this.types.getWidget(i);
			if (c.getValue()) {
				for (PersonType pt : typesCache) {
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
		PersonValueCell pc = new PersonValueCell(imageHtml);
		
		IdentityColumn<PersonValueProxy> person = new IdentityColumn<PersonValueProxy>(pc);
			
		persons = new DataGrid<PersonValueProxy>();
		persons.addColumn(person);
	}
	
	public ManagePersons() {
		createFilter();
		createPersons();
		
		initWidget(uiBinder.createAndBindUi(this));
		
		personsData = new ArrayList<PersonValueProxy>();
		personsFilteredData = new ArrayList<PersonValueProxy>();
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
	public void setSelectionModel(SingleSelectionModel<PersonValueProxy> selection) {
		this.persons.setSelectionModel(selection);
	}

	@Override
	public void setPersons(List<PersonValueProxy> persons) {
		personsData.clear();
		if (persons == null) {
			return;
		}
		PersonValueSort.sort(persons);
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
		for (PersonValueProxy p : personsData) {
			for (FilterPersonValue f : filters) {
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
