package ar.com.dcsys.gwt.assistance.client.ui.justification.person;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.com.dcsys.data.justification.Justification;
import ar.com.dcsys.data.justification.JustificationDate;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.gwt.assistance.client.activity.justification.JustificationStatistic;
import ar.com.dcsys.gwt.assistance.client.common.JustificationDatesSort;
import ar.com.dcsys.gwt.assistance.client.common.JustificationsSort;
import ar.com.dcsys.gwt.assistance.client.ui.cell.PersonCell;
import ar.com.dcsys.gwt.assistance.client.ui.common.AssistanceResources;
import ar.com.dcsys.gwt.assistance.client.ui.justification.widget.SelectionJustificationDateListWidget;
import ar.com.dcsys.gwt.assistance.client.ui.justification.widget.selectionJustificationDateList.SelectionJustificationDateList;
import ar.com.dcsys.gwt.person.client.common.filter.FilterPerson;
import ar.com.dcsys.gwt.person.client.common.filter.FilterPersonDni;
import ar.com.dcsys.gwt.person.client.common.filter.FilterPersonName;
import ar.com.dcsys.utils.PersonSort;

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
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DateBox.DefaultFormat;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;

public class JustificationPerson extends Composite implements JustificationPersonView {

	private static JustificationPersonUiBinder uiBinder = GWT.create(JustificationPersonUiBinder.class);

	interface JustificationPersonUiBinder extends UiBinder<Widget, JustificationPerson> {
	}
	
	
	
	// filtros de dni y nombre
	private final FilterPerson[] filters = new FilterPerson[] { new FilterPersonDni(), new FilterPersonName() };
	
	private final List<Person> personsFilteredData;
	private final List<Person> personsData;
	
	private final AssistanceResources resources = GWT.create(AssistanceResources.class);

	private Presenter p;
	private Timer filterTimer = null;	
	
	@UiField(provided=true) TextBox filter;
	@UiField(provided=true) Label usersCount;
	@UiField(provided=true) DataGrid<Person> persons;	
	@UiField(provided=true) DataGrid<JustificationStatistic> statistics;
	@UiField(provided=true) ValueListBox<Justification> types;
	@UiField(provided=true) DateBox start;
	@UiField(provided=true) DateBox end;	
	@UiField(provided=true) SelectionJustificationDateList selectionJustificationDates;
	@UiField (provided=true) Button remove; 
	@UiField TextArea justifyNotes;
	@UiField FlowPanel blockJustifications;
	@UiField Button commit;
	@UiField (provided=true) Button openJustify;
	private SingleSelectionModel<Justification> typeSelection;
	@UiField DialogBox dialogJustify;
	
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
	
	private void createType() {
		types = new ValueListBox<Justification>(new Renderer<Justification>() {
			private String getValue(Justification type) {
				String code = type.getCode();
				String description = type.getDescription();
				return code + ((description != null) ? "  " + description : "");				
			}
			@Override
			public String render(Justification object) {
				if (object == null) {
					return "";
				}
				return getValue(object);
			}
			@Override
			public void render(Justification object, Appendable appendable) throws IOException {
				if (object == null) {
					return;
				}
				appendable.append(getValue(object));
			}
		});
		
		types.addValueChangeHandler(new ValueChangeHandler<Justification>() {
			@Override
			public void onValueChange(ValueChangeEvent<Justification> event) {
				if (typeSelection != null) {
					Justification t = types.getValue();
					if (t != null) {
						typeSelection.setSelected(t, true);
					}
				}
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
	
	private void createDates() {
		final DateTimeFormat dateF = DateTimeFormat.getFormat(PredefinedFormat.DATE_SHORT);
		start = new DateBox(new DatePicker(), new Date(), new DefaultFormat(dateF));
		start.addValueChangeHandler(new ValueChangeHandler<Date>(){
			@Override
			public void onValueChange(ValueChangeEvent<Date> event) {
			}
			
		});
		
		end = new DateBox(new DatePicker(), new Date(), new DefaultFormat(dateF));
		end.addValueChangeHandler(new ValueChangeHandler<Date>(){
			@Override
			public void onValueChange(ValueChangeEvent<Date> event) {
			}
			
		});
	}		
	
	private void createStatistic() {
		statistics = new DataGrid<JustificationStatistic>();
		TextColumn<JustificationStatistic> name = new TextColumn<JustificationStatistic>() {
			@Override
			public String getValue(JustificationStatistic object) {
				String name = object.getName();
				if (name == null) {
					return "no tiene";
				}
				return name;
			}
		};	
		TextColumn<JustificationStatistic> description = new TextColumn<JustificationStatistic>() {
			@Override
			public String getValue(JustificationStatistic object) {
				String description = object.getDescription();
				if (description == null) {
					return "no tiene";
				}
				return description;
			}
		};	
		TextColumn<JustificationStatistic> count = new TextColumn<JustificationStatistic>() {
			@Override
			public String getValue(JustificationStatistic object) {
				String count = String.valueOf(object.getCount());				
				return count;
			}
		};			
		statistics.addColumn(name,"Tipo");
		statistics.addColumn(count,"Cantidad");
		statistics.addColumn(description,"Descripción");
	}
	
	public JustificationPerson() {
		createFilter();
		createPersons();
		createDates();
		createType();
		createStatistic();
		createFind();
		remove = new Button();
		remove.setEnabled(false);	
		openJustify = new Button();
		openJustify.setEnabled(false);
		selectionJustificationDates = new SelectionJustificationDateList();
		initWidget(uiBinder.createAndBindUi(this));
		personsFilteredData = new ArrayList<Person>();
		personsData = new ArrayList<Person>();
		setEnabledJustifications(false);
		dialogJustify.hide();	
		dialogJustify.setGlassEnabled(true);
		dialogJustify.setModal(true);
		dialogJustify.setAnimationEnabled(true);		
	}

	@Override
	public void setPresenter(Presenter p) {
		this.p = p;
	}

	@Override
	public void clear() {
		clearPersons();
		clearJustificationData();
		clearTypes();
		clearStatistic();
		clearJustificationDateList();
		start.setValue(new Date());
		end.setValue(new Date());
	}
	
	@Override
	public void clearStatistic() {
		statistics.setRowCount(0,true);
	}
	
	private void clearPersons() {
		personsData.clear();
		personsFilteredData.clear();
		usersCount.setText("0");
		persons.setRowCount(0);
		persons.setRowData(personsData);
	}	
	
	@Override
	public void clearPersonData(){
		clearJustificationDateList();
		clearJustificationData();
	}
	
	@Override
	public void clearJustificationDateList(){
		setEnabledRemoveButton(false);
		selectionJustificationDates.clear();
	}

	@Override
	public void clearJustificationData() {
		justifyNotes.setText("");
	}
	
	private void clearTypes() {
		types.setValue(null);
		types.setAcceptableValues(new ArrayList<Justification>());
	}
	
	@Override
	public void setTypes(List<Justification> types) {
		JustificationsSort.sort(types);
		if (types.size() > 0) {
			Justification t = types.get(0);
			this.types.setValue(t);
			if (typeSelection != null) {
				typeSelection.setSelected(t,true);
			}
		}
		this.types.setAcceptableValues(types);		
	}
	
	@Override
	public void setTypesSelectionModel(SingleSelectionModel<Justification> selection) {
		typeSelection = selection;
	}	
	
	@Override
	public void setSelectionModel(MultiSelectionModel<Person> selection) {
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
	
	@UiHandler("commit")
	public void onAssign(ClickEvent event) {
		if (p == null) {
			return;
		}
		dialogJustify.hide();
		p.persist();
	}

	@Override
	public Date getStart() {
		return start.getValue();
	}

	@Override
	public Date getEnd() {
		Date date = new Date(end.getValue().getTime() + (23l * 59l * 59l));
		return date;
	}

	@Override
	public SelectionJustificationDateListWidget getViewSelectionJustificationDate() {
		return selectionJustificationDates;
	}
	
	@Override
	public void setJustificationData(List<JustificationDate> jds){
		JustificationDatesSort.sort(jds);
		selectionJustificationDates.setJustificationDates(jds);
	}

	@Override
	public void setJustificationDateSelectionModel (MultiSelectionModel<JustificationDate> selectionJustificationDate) {
		selectionJustificationDates.setSelectionModel(selectionJustificationDate);
	}
	
	@Override
	public String getNotes(){
		return this.justifyNotes.getText();
	}
	
	@Override
	public void setNotes(String notes){
		this.justifyNotes.setText(notes);
	}

	@Override
	public void setEnabledJustifications(boolean b) {
		start.setEnabled(b);
		end.setEnabled(b);
		commit.setEnabled(b);
		openJustify.setEnabled(b);
		setEnabledFind(b);
	}
	 
	@Override
	public void setStatistics(List<JustificationStatistic> js) {
		this.statistics.setRowData(js);
	}
	
	@UiHandler("remove")
	public void onRemove (ClickEvent event){
		p.removeJustificationDates();
	}
	
	@Override
	public void setEnabledRemoveButton(boolean b) {
		remove.setEnabled(b);
	}		
	
	@UiHandler("openJustify")
	public void onOpenJustify(ClickEvent e) {
		clearJustificationData();
		dialogJustify.center();
		dialogJustify.show();
	}	
	
	@UiHandler("closeJustify")
	public void onCloseJustify(ClickEvent e) {
		dialogJustify.hide();
	}

	
	/* *****************************************************************************
	 * *********************************** FIND ***********************************
	 * *************************************************************************** */	
	@UiField(provided=true) Button find;
	
	private void createFind() {
		find = new Button();
		find.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				if (p != null && selectionJustificationDates != null) {
					clearJustificationDateList();
					selectionJustificationDates.search(start.getValue(), end.getValue());					
				}
			}
		});
		setEnabledFind(false);
	}
	
	@Override
	public void setEnabledFind(boolean b) {
		find.setEnabled(b);
	}
	
}
