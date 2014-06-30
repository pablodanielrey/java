package ar.com.dcsys.gwt.assistance.client.ui.period.person;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import ar.com.dcsys.data.period.PeriodAssignation;
import ar.com.dcsys.data.period.PeriodType;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.gwt.assistance.client.common.PeriodsAssignationSort;
import ar.com.dcsys.gwt.assistance.client.ui.cell.PersonCell;
import ar.com.dcsys.gwt.assistance.client.ui.common.AssistanceResources;
import ar.com.dcsys.gwt.person.client.common.filter.FilterPerson;
import ar.com.dcsys.gwt.person.client.common.filter.FilterPersonDni;
import ar.com.dcsys.gwt.person.client.common.filter.FilterPersonName;
import ar.com.dcsys.utils.PersonSort;

import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.ActionCell.Delegate;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.IdentityColumn;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DateBox.DefaultFormat;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.google.gwt.view.client.SingleSelectionModel;

public class PeriodsAssignationPerson extends Composite implements PeriodsAssignationPersonView{

	private static PeriodsPersonUiBinder uiBinder = GWT.create(PeriodsPersonUiBinder.class);

	interface PeriodsPersonUiBinder extends UiBinder<Widget, PeriodsAssignationPerson> {
	}

	// filtros de dni y nombre
	private final FilterPerson[] filters = new FilterPerson[] {	new FilterPersonDni() , new FilterPersonName() };
	
	private final List<Person> personsFilteredData;
	private final List<Person> personsData;
	
	private final AssistanceResources resources = GWT.create(AssistanceResources.class);

	private Timer filterTimer = null;		
	private Presenter p;
	
	private SingleSelectionModel<PeriodType> typeSelection;
	
	@UiField(provided=true) TextBox filter;
	@UiField(provided=true) Label usersCount;
	@UiField(provided=true) DataGrid<Person> persons;	
	@UiField(provided=true) CheckBox displayAll;
	
	@UiField(provided=true) DataGrid<PeriodAssignation> periods;
	@UiField(provided=true) DateBox date;
	@UiField(provided=true) ValueListBox<PeriodType> types;
	@UiField Button commit;

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
		
	
	private void createPeriodsAssignation() {
		final DateTimeFormat dateF = DateTimeFormat.getFormat(PredefinedFormat.DATE_SHORT);
		TextColumn<PeriodAssignation> date = new TextColumn<PeriodAssignation>() {
			@Override
			public String getValue(PeriodAssignation object) {
				if (object == null) {
					return "nulo";
				}
				return dateF.format(object.getStart());
			}
		};	
		TextColumn<PeriodAssignation> type = new TextColumn<PeriodAssignation>() {
			@Override
			public String getValue(PeriodAssignation object) {
				if (object == null) {
					return "nulo";
				}
				/*
				 * TODO: tengo que ponerle descripcion
				 */
				return object.getType().toString();
			}
		};		
		ActionCell<PeriodAssignation> deleteC = new ActionCell<PeriodAssignation>("Borrar", new Delegate<PeriodAssignation>() {
			@Override
			public void execute(PeriodAssignation object) {
				if (p == null) {
					return;
				}
				p.remove(object);
			}
		});
		IdentityColumn<PeriodAssignation> delete = new IdentityColumn<PeriodAssignation>(deleteC);
		
		periods = new DataGrid<PeriodAssignation>();
		periods.addColumn(date, "Desde");
		periods.addColumn(type,"Tipo");
		periods.addColumn(delete);
	}
	
	private void createDate() {
		final DateTimeFormat dateF = DateTimeFormat.getFormat(PredefinedFormat.DATE_SHORT);
		date = new DateBox(new DatePicker(), new Date(), new DefaultFormat(dateF));
	}
	
	private void createCheckDisplayAll() {
		displayAll = new CheckBox();
		displayAll.setChecked(false);
		displayAll.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (p == null) {
					return;
				}
				p.updatePersons(displayAll.isChecked());
			}
		});
	}
	
	
	private String periodTypeToString(PeriodType pt) {
		switch (pt) {
			case NULL:
				return "nulo";
			case DAILY:
				return "diario";
			case SYSTEMS:
				return "sistemas";
			case WATCHMAN:
				return "sereno";
			default:
				return "indefinido";
		}
	}
	
	private void createType() {
		types = new ValueListBox<PeriodType>(new Renderer<PeriodType>() {
			private String getValue(PeriodType type) {
				if (type == null) {
					return "Nulo";
				}
				return periodTypeToString(type);
			}
			@Override
			public String render(PeriodType object) {
				if (object == null) {
					return "";
				}
				return getValue(object);
			}

			@Override
			public void render(PeriodType object, Appendable appendable) throws IOException {
				if (object == null) {
					return;
				}
				appendable.append(getValue(object));
			}
			
		});
		
		types.addValueChangeHandler(new ValueChangeHandler<PeriodType>() {

			@Override
			public void onValueChange(ValueChangeEvent<PeriodType> event) {
				if (typeSelection != null) {
					PeriodType t = types.getValue();
					if (t != null) {
						typeSelection.setSelected(t, true);
					}
				}
			}
			
		});
	}

	public PeriodsAssignationPerson() {
		createFilter();
		createPersons();
		createCheckDisplayAll();
		createPeriodsAssignation();
		createDate();
		createType();
		initWidget(uiBinder.createAndBindUi(this));
		personsFilteredData = new ArrayList<Person>();
		personsData = new ArrayList<Person>();
	}

	@Override
	public void setPresenter(Presenter p) {
		this.p = p;
	}

	@Override
	public void clear() {
		clearPersons();
		clearPeriodsData();
		clearTypes();
		displayAll.setChecked(false);
		setEnabledNewPeriod(false);
	}
	
	
	private void clearPersons() {
		personsData.clear();
		personsFilteredData.clear();
		usersCount.setText("0");
		persons.setRowCount(0);
		persons.setRowData(personsData);
	}		

	@Override
	public void clearPeriodsData() {
		periods.setRowCount(0,true);
	}

	@Override
	public void setPeriodsData(List<PeriodAssignation> periodsAssignation) {
		PeriodsAssignationSort.sort(periodsAssignation);
		periods.setRowData(periodsAssignation);
	}
	
	private void clearTypes() {
		types.setValue(null);
		types.setAcceptableValues(new ArrayList<PeriodType>());
	}
	
	@Override
	public void setTypes(List<PeriodType> types) {
		if (types.size() > 0) {
			PeriodType t = types.get(0);
			this.types.setValue(t);
			if (typeSelection != null) {
				typeSelection.setSelected(t, true);
			}
		}
		this.types.setAcceptableValues(types);
	}
	
	@Override
	public void setTypesSelectionModel(SingleSelectionModel<PeriodType> selection) {
		typeSelection = selection;
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
	public void setSelectionModel(	SingleSelectionModel<Person> selectionModel) {
		this.persons.setSelectionModel(selectionModel);
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
	
	@Override
	public Date getDate() {
		return date.getValue();
	}
		
	@UiHandler("commit")
	public void onCommit (ClickEvent event) {
		if (p == null) {
			return;
		}
		p.create();
	}
	
	@Override
	public void setEnabledNewPeriod(boolean enabled) {
		commit.setEnabled(enabled);
	}

}
