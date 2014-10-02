package ar.com.dcsys.gwt.person.client.ui.group;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.data.group.GroupType;
import ar.com.dcsys.data.person.Mail;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.person.PersonType;
import ar.com.dcsys.gwt.person.client.common.EnumGroupTypesSort;
import ar.com.dcsys.gwt.person.client.common.PersonTypesSort;
import ar.com.dcsys.gwt.person.client.common.filter.FilterGroup;
import ar.com.dcsys.gwt.person.client.common.filter.FilterGroupName;
import ar.com.dcsys.gwt.person.client.common.filter.FilterPerson;
import ar.com.dcsys.gwt.person.client.common.filter.FilterPersonDni;
import ar.com.dcsys.gwt.person.client.common.filter.FilterPersonName;
import ar.com.dcsys.gwt.person.client.ui.cell.PersonCell;
import ar.com.dcsys.gwt.person.client.ui.common.PersonResources;
import ar.com.dcsys.utils.GroupSort;
import ar.com.dcsys.utils.GroupTypeUtils;
import ar.com.dcsys.utils.PersonSort;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.IdentityColumn;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SingleSelectionModel;


public class Groups extends Composite implements GroupsView {

	private static GroupsUiBinder uiBinder = GWT.create(GroupsUiBinder.class);

	interface GroupsUiBinder extends UiBinder<Widget, Groups> {
	}
	
	
	
	private Presenter p;
	private SingleSelectionModel<Group> parentSelection;
	private SingleSelectionModel<GroupType> groupTypesSelection;
	
	private final PersonResources resources = GWT.create(PersonResources.class);
	
	private final List<PersonType> personTypesCache = new ArrayList<PersonType>();
	private final List<GroupType> groupTypesCache = new ArrayList<GroupType>();
	

	
	///////////////////// PERSONAS DENTRO DE UN GRUPO DETERMINADO ////////////////////////////////////////////////////////
	
	// filtros de persona por dni y nombre
	private final FilterPerson[] filtersPersons = new FilterPerson[] { new FilterPersonDni(), new FilterPersonName() };	
	
	private final List<Person> personsInData;
	private final List<Person> personsInFilteredData;
	private Timer filterTimerPersonsIn = null;	
	
	private final List<Person> personsOutData;
	private final List<Person> personsOutFilteredData;
	private Timer filterTimerPersonsOut = null;

	@UiField(provided=true) DataGrid<Person> personsIn;
	@UiField(provided=true) TextBox filterPersonIn;
	@UiField(provided=true) Label inCount;	
	
	@UiField(provided=true) DataGrid<Person> personsOut;
	@UiField(provided=true) TextBox filterPersonOut;
	@UiField(provided=true) Label outCount;	
	@UiField FlowPanel personTypes;
	
	//////////////////////// LISTA DE GRUPOS //////////////////////////////////////////////////////////////////////////////
	
	// filtros de grupo por nombre
	private final FilterGroup[] filtersGroups = new FilterGroup[] {new FilterGroupName()};	
	
	private final List<Group> groupsData;
	private final List<Group> groupsFilteredData;
	private Timer filterTimerGroups = null;	
	
	@UiField(provided=true) DataGrid<Group> groups;
	@UiField(provided=true) TextBox filterGroup;
	@UiField(provided=true) Label groupsCount;
	@UiField(provided=true) ValueListBox<GroupType> groupTypes;
	
	
	///////////////////////// ALTA DE GRUPO //////////////////////////////////////////////////////////////////////////////
	
	@UiField TextBox name;
	@UiField TextBox mail;
	@UiField FlowPanel types;
	@UiField(provided=true) ValueListBox<Group> groupParent;

	
	///////// Tipos de personas (se usan como filtros solamente) /////////
	/*
	private void clearPersonTypes() {
		for (int i = 0; i < this.personTypes.getWidgetCount(); i++) {
			CheckBox c = (CheckBox)this.personTypes.getWidget(i);
			c.setValue(false);
		}
	}*/

	
	
	
	
	///////////////////////// PERSONAS EN LOS GRUPOS //////////////////////////////////////
	
	@Override
	public void setAllPersonTypes(List<PersonType> types) {
		personTypesCache.clear();
		PersonTypesSort.sort(types);
		personTypesCache.addAll(types);
		
		final ClickHandler handler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (p == null) {
					return;
				}
				p.updatePersons();
			}
		};
		
		this.personTypes.clear();
		for (PersonType pt : types) {
			CheckBox c = new CheckBox(pt.getName());
			c.setValue(false);
			c.addClickHandler(handler);
			this.personTypes.add(c);
		}
	}

	@Override
	public List<PersonType> getSelectedPersonTypes() {
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
	
	private void createFilterPersonIn() {
		
		inCount = new Label("0");
		
		filterPersonIn = new TextBox();
		filterPersonIn.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (filterTimerPersonsIn != null) {
					filterTimerPersonsIn.cancel();
				}
				filterTimerPersonsIn = new Timer() {
					public void run() {
						filterTimerPersonsIn = null;
						filterPersonsIn();							
					};
				};
				filterTimerPersonsIn.schedule(2000);				
			}
		});
	}	
	
	private void filterPersonsIn() {
		String ft = filterPersonIn.getText();
		if (ft == null || ft.trim().equals("")) {
			inCount.setText(String.valueOf(personsInData.size()));
			personsIn.setRowData(personsInData);
			return;
		}
		// aplico el filtro de acuerdo a los Filter que tenga definidos.
		// ahora solo es el dni y si no el nombre.
		personsInFilteredData.clear();
		for (Person p : personsInData) {
			for (FilterPerson f : filtersPersons) {
				if (f.checkFilter(p, ft)) {
					personsInFilteredData.add(p);
					break;
				}
			}
		}
		inCount.setText(String.valueOf(personsInFilteredData.size()));
		personsIn.setRowData(personsInFilteredData);
	}	
	
	
	private void createFilterPersonOut() {
		
		outCount = new Label("0");
		
		filterPersonOut = new TextBox();
		filterPersonOut.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (filterTimerPersonsOut != null) {
					filterTimerPersonsOut.cancel();
				}
				filterTimerPersonsOut = new Timer() {
					public void run() {
						filterTimerPersonsOut = null;
						filterPersonsOut();							
					};
				};
				filterTimerPersonsOut.schedule(2000);				
			}
		});
	}	
	
	private void filterPersonsOut() {
		String ft = filterPersonOut.getText();
		if (ft == null || ft.trim().equals("")) {
			outCount.setText(String.valueOf(personsOutData.size()));
			personsOut.setRowData(personsOutData);
			return;
		}
		// aplico el filtro de acuerdo a los Filter que tenga definidos.
		// ahora solo es el dni y si no el nombre.
		personsOutFilteredData.clear();
		for (Person p : personsOutData) {
			for (FilterPerson f : filtersPersons) {
				if (f.checkFilter(p, ft)) {
					personsOutFilteredData.add(p);
					break;
				}
			}
		}
		outCount.setText(String.valueOf(personsOutFilteredData.size()));
		personsOut.setRowData(personsOutFilteredData);
	}	
	
	
	
	
	//////////// LISTA DE GRUPOS //////////////////
	
	@Override
	public void setGroups(List<Group> groups) {
		groupsData.clear();
		if (groups == null) {
			return;
		}
		GroupSort.sort(groups);
		groupsData.addAll(groups);
		filterGroups();				
	}	
	
	@Override
	public void clear() {
		groupsCount.setText("0");
		groups.setRowCount(0);
		groups.setRowData(new ArrayList<Group>());
		clearGroupData();
	}

	@Override
	public void clearGroupData() {
		
		// datos de la seleccion de personas
		
		personsInData.clear();
		personsInFilteredData.clear();		
		inCount.setText("0");
		personsIn.setRowCount(0);
		personsIn.setRowData(personsInData);		
		
		personsOutData.clear();
		personsOutFilteredData.clear();		
		outCount.setText("0");
		personsOut.setRowCount(0);
		personsOut.setRowData(personsOutData);

		// datos de ADD
		
		name.setText("");
		mail.setText("");
		groupParent.setValue(null);
		clearGroupTypes();
	}
	
	private void clearGroupTypes() {
		for (int i = 0; i < this.types.getWidgetCount(); i++) {
			CheckBox c = (CheckBox)this.types.getWidget(i);
			c.setValue(false);
		}
	}
	
	@Override
	public void setAllGroupTypes(List<GroupType> types) {
		
		groupTypesCache.clear();
		
		if (types == null) {
			return;
		}

		EnumGroupTypesSort.sort(types);
		groupTypesCache.addAll(types);
		
		setGroupTypesFilter(types);
		
		this.types.clear();
		for (GroupType pt : types) {
			CheckBox c = new CheckBox(GroupTypeUtils.getDescription(pt));
			c.setValue(false);
			this.types.add(c);
		}
	}
	
	@Override
	public List<GroupType> getSelectedGroupTypes() {
		List<GroupType> selected = new ArrayList<GroupType>();
		for (int i = 0; i < this.types.getWidgetCount(); i++) {
			CheckBox c = (CheckBox)this.types.getWidget(i);
			if (c.getValue()) {
				for (GroupType pt : groupTypesCache) {
					if (GroupTypeUtils.getDescription(pt).equalsIgnoreCase(c.getText())) {
						selected.add(pt);
					}
				}
			}
		}
		return selected;
	}
	
	@Override
	public void setSelectedGroupTypes(List<GroupType> types) {
		if (types != null && types.size() > 0) {
			for (int i = 0; i < this.types.getWidgetCount(); i++) {
				CheckBox c = (CheckBox)this.types.getWidget(i);
				c.setValue(false);
				for (GroupType pt : types) {
					if (c.getText().equalsIgnoreCase(GroupTypeUtils.getDescription(pt))) {
						c.setValue(true);
					}
				}
			}
		}
	}	
	
	//////////////////
	
	
	
	private void createFilterGroup() {
		
		groupsCount = new Label("0");
		
		filterGroup = new TextBox();
		filterGroup.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (filterTimerGroups != null) {
					filterTimerGroups.cancel();
				}
				filterTimerGroups = new Timer() {
					public void run() {
						filterTimerGroups = null;
						filterGroups();						
					};
				};
				filterTimerGroups.schedule(2000);				
			}
		});
	}	
	
	private void filterGroups() {
		String ft = filterGroup.getText();
		if (ft == null || ft.trim().equals("")) {
			groupsCount.setText(String.valueOf(groupsData.size()));
			groups.setRowData(groupsData);
			return;
		}

		groupsFilteredData.clear();
		for (Group g : groupsData) {
			for (FilterGroup f : filtersGroups) {
				if (f.checkFilter(g, ft)) {
					groupsFilteredData.add(g);
					break;
				}
			}
		}
		groupsCount.setText(String.valueOf(groupsFilteredData.size()));
		groups.setRowData(groupsFilteredData);
	}	
	
	
	private void createGroups() {
		
		TextColumn<Group> name = new TextColumn<Group>() {
			public String getValue(Group object) {
				if (object == null) {
					return "";
				}
				return object.getName();
			};
		};
		groups = new DataGrid<Group>();
		groups.addColumn(name,"Grupo");
		
		String imageHtml = AbstractImagePrototype.create(resources.user()).getHTML();
		PersonCell pc = new PersonCell(imageHtml);
		IdentityColumn<Person> person = new IdentityColumn<Person>(pc);
		
		personsIn = new DataGrid<Person>();
		personsIn.addColumn(person,"Integrantes");
		
		personsOut = new DataGrid<Person>();
		personsOut.addColumn(person,"No Integrantes");
		
	}
	
	private void createGroupTypes() {		
		groupTypes = new ValueListBox<GroupType>(new Renderer<GroupType>() {
			private String getValue(GroupType gt) {
				if (gt == null) {
					return "Mostrar Todos";
				}
				return GroupTypeUtils.getDescription(gt);
			}
			
			@Override
			public String render(GroupType object) {
				return getValue(object);
			}
			
			@Override
			public void render(GroupType object, Appendable appendable) throws IOException {
				appendable.append(getValue(object));
			}
		});
		groupTypes.addValueChangeHandler(new ValueChangeHandler<GroupType>() {
			@Override
			public void onValueChange(ValueChangeEvent<GroupType> event) {
				if (groupTypesSelection != null) {
					GroupType gt = groupTypes.getValue();
					groupTypesSelection.setSelected(gt, true);
				}
			}
		});			
	}
	
	
	/////////////// ALTA DE GRUPO ///////////////////////
	
	
	private void createParent() {
		groupParent = new ValueListBox<Group>(new Renderer<Group>() {
			private String getValue(Group g) {
				if (g == null) {
					return "No posee";
				}
				return g.getName();
			}
			
			@Override
			public String render(Group object) {
				return getValue(object);
			}
			
			@Override
			public void render(Group object, Appendable appendable) throws IOException {
				appendable.append(getValue(object));
			}
		});
		groupParent.addValueChangeHandler(new ValueChangeHandler<Group>() {
			@Override
			public void onValueChange(ValueChangeEvent<Group> event) {
				if (parentSelection != null) {
					Group g = groupParent.getValue();
					parentSelection.setSelected(g, true);
				}
			}
		});		
	}
	
	private void clearParent() {
		groupParent.setValue(null);
		groupParent.setAcceptableValues(new ArrayList<Group>());	
	}	
	
	
	
	
	
	public Groups() {
		createFilterPersonIn();
		createFilterPersonOut();
		createFilterGroup();
		createGroups();
		createParent();
		createGroupTypes();
		initWidget(uiBinder.createAndBindUi(this));
		
		groupsData = new ArrayList<Group>();
		groupsFilteredData = new ArrayList<Group>();
		
		personsInData = new ArrayList<Person>();
		personsInFilteredData = new ArrayList<Person>();

		personsOutData = new ArrayList<Person>();
		personsOutFilteredData = new ArrayList<Person>();		
	}

	@Override
	public void setPresenter(Presenter p) {
		this.p = p;
	}

	@Override
	public void setSelectionModel(SingleSelectionModel<Group> group) {
		groups.setSelectionModel(group);
	}


	
	@Override
	public void setGroupTypeSelectionModel(SingleSelectionModel<GroupType> selection) {
		groupTypesSelection = selection;
	}
	
	private void setGroupTypesFilter(List<GroupType> types) {
		List<GroupType> valuesTypes = new ArrayList<GroupType>();
		valuesTypes.add(null);
		valuesTypes.addAll(types);

		groupTypes.setValue(null);
		groupTypes.setAcceptableValues(valuesTypes);
		if (groupTypesSelection != null) {
			groupTypesSelection.setSelected(null,true);
		}			
	}
	
	@Override
	public void setParents(List<Group> groups) {
		groupParent.setValue(null);		
		groups.add(null);
		GroupSort.sort(groups);
		groupParent.setAcceptableValues(groups);
		if (parentSelection != null) {
			parentSelection.setSelected(null,true);
		}		
	}
	
	@Override
	public void setParent(Group group) {
		groupParent.setValue(group);
	}
	
	@Override
	public void setParentSelectionModel(SingleSelectionModel<Group> selection) {
		parentSelection = selection;
	}	
	
	@Override
	public void setInPersons(List<Person> in) {
		personsInData.clear();
		if (in == null) {
			return;
		}
		PersonSort.sort(in);
		personsInData.addAll(in);
		filterPersonsIn();		
	}
	
	@Override
	public void setInSelectionModel(SingleSelectionModel<Person> inSelection) {
		personsIn.setSelectionModel(inSelection);
	}
	
	@Override
	public void setOutPersons(List<Person> out) {
		personsOutData.clear();
		if (out == null) {
			return;
		}
		PersonSort.sort(out);
		personsOutData.addAll(out);
		filterPersonsOut();	
	}
	
	@Override
	public void setOutSelectionModel(SingleSelectionModel<Person> outSelection) {
		personsOut.setSelectionModel(outSelection);
	}

	
	@Override
	public String getMail() {
		return mail.getText();
	}
	
	@Override
	public String getName() {
		return name.getText();
	}
	
	
	/**
	 * Setea los datos del grupo dentro de la ventana de edición.
	 * No setea el padre.
	 */
	@Override
	public void setGroup(Group group) {
		name.setText(group.getName());
		
		String mail = findPrimaryMail(group); 
		if (mail != null) {
			this.mail.setText(mail);
		}
	}
	
	/**
	 * Busca el mail primario del grupo en caso de existir mas de 1 mail.
	 * @param g
	 * @return
	 */
	private String findPrimaryMail(Group g) {
		List<Mail> mails = g.getMails();
		if (mails == null) {
			return null;
		}
		for (Mail m : mails) {
			if (m.isPrimary()) {
				return m.getMail();
			}
		}
		return null;
	}
	
	
	@UiHandler("commit")
	public void onCommit(ClickEvent event) {
		if (p == null) {
			return;
		}
		p.createUpdate();
	}

	
}
