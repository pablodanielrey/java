package ar.com.dcsys.gwt.assistance.client.ui.period;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import ar.com.dcsys.assistance.entities.AssistancePersonData;
import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.data.justification.GeneralJustificationDate;
import ar.com.dcsys.data.justification.JustificationDate;
import ar.com.dcsys.data.period.Period;
import ar.com.dcsys.data.period.WorkedHours;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.report.Report;
import ar.com.dcsys.gwt.assistance.client.common.GroupsSort;
import ar.com.dcsys.gwt.assistance.client.ui.cell.PersonCell;
import ar.com.dcsys.gwt.assistance.client.ui.common.AssistanceResources;
import ar.com.dcsys.gwt.assistance.client.ui.period.cells.JustificationActionCell;
import ar.com.dcsys.gwt.assistance.client.ui.period.cells.JustificationActionCellPresenter;
import ar.com.dcsys.gwt.person.client.common.filter.FilterPerson;
import ar.com.dcsys.gwt.person.client.common.filter.FilterPersonDni;
import ar.com.dcsys.gwt.person.client.common.filter.FilterPersonName;
import ar.com.dcsys.utils.PersonSort;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.IdentityColumn;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.SingleSelectionModel;

public class Periods extends Composite implements PeriodsView {

	private static PeriodsUiBinder uiBinder = GWT.create(PeriodsUiBinder.class);

	interface PeriodsUiBinder extends UiBinder<Widget, Periods> {
	}
	
	private Presenter presenter;
	
	public Periods() {
		createDates();
		createPeriodFilter();
		createGroupFilter();
		createFilter();
		createPersons();
		createPeriods();
		initWidget(uiBinder.createAndBindUi(this));
		
		personsData = new ArrayList<Person>();
		personsFilteredData = new ArrayList<Person>();
	}
	
	@Override
	public void setPresenter(Presenter p) {
		this.presenter = p;
	}
	
	@Override
	public void clear() {
		clearPeriodFilter();
		clearPersons();
		clearPeriodData();
	}
	
	/* *****************************************************************************
	 * *********************************** DATES ***********************************
	 * *************************************************************************** */
	

	@UiField(provided=true) DateBox start;
	@UiField(provided=true) DateBox end;
	
	private void createDates() {
		DateTimeFormat defaultF = DateTimeFormat.getFormat(PredefinedFormat.DATE_SHORT);
		
		start = new DateBox();
		start.setFormat(new DateBox.DefaultFormat(defaultF));
		start.addValueChangeHandler(new ValueChangeHandler<Date>() {
			@Override
			public void onValueChange(ValueChangeEvent<Date> event) {
				Date date = event.getValue();
				date.setHours(0);
				date.setMinutes(0);
				date.setSeconds(0);
				start.setValue(date);
				
				if (presenter != null) {
					presenter.dateChanged();
				}
			}
		});
		
		end = new DateBox();
		end.setFormat(new DateBox.DefaultFormat(defaultF));
		end.addValueChangeHandler(new ValueChangeHandler<Date>() {
			@Override
			public void onValueChange(ValueChangeEvent<Date> event) {
				Date date = event.getValue();
				date.setHours(23);
				date.setMinutes(59);
				date.setSeconds(59);
				end.setValue(date);
				
				if (presenter != null) {
					presenter.dateChanged();
				}
			}
		});			
	}
	
	@Override
	public Date getStart() {
		return end.getValue();
	}
	
	@Override
	public void setStart(Date date) {
		start.setValue(date);
	}
	
	@Override
	public Date getEnd() {
		return end.getValue();
	}
	
	@Override
	public void setEnd(Date date) {
		end.setValue(date);
	}
	
	
	/* *****************************************************************************
	 * ******************************* PERIODFILTER ********************************
	 * *************************************************************************** */
	
	private SingleSelectionModel<PERIODFILTER> periodFilterSelectionModel;
	@UiField(provided=true) ValueListBox<PERIODFILTER> periodFilter;
	
	/**
	 * Mantiene en sincronía el selectionModel y el Value del ValueListBox
	 */
	private final ValueChangeHandler<PERIODFILTER> periodChangeHandler = new ValueChangeHandler<PERIODFILTER>() {
		@Override
		public void onValueChange(ValueChangeEvent<PERIODFILTER> event) {
			if (periodFilterSelectionModel == null) {
				return;
			}
			PERIODFILTER item = periodFilter.getValue();
			if (item == null) {
				periodFilterSelectionModel.clear();
			} else {
				periodFilterSelectionModel.setSelected(item, true);
			}
		}
	};	
	
	private void createPeriodFilter() {
		periodFilter = new ValueListBox<PERIODFILTER>(new Renderer<PERIODFILTER>() {
			@Override
			public String render(PERIODFILTER object) {
				if (object == null) {
					return "";
				}
				return object.getDescription();
			}
			@Override
			public void render(PERIODFILTER object, Appendable appendable) throws IOException {
				if (object == null) {
					return;
				}
				appendable.append(object.getDescription());
			}
		});
		periodFilter.addValueChangeHandler(periodChangeHandler);
	}	
	
	private void clearPeriodFilter() {
		periodFilter.setAcceptableValues(new ArrayList<PERIODFILTER>());
	}	
	
	@Override
	public void setPeriodFilterValues(List<PERIODFILTER> values) {
		periodFilter.setValue(values.get(0));
		periodFilter.setAcceptableValues(values);
	}
	
	@Override
	public void setPeriodFilterSelectionModel(SingleSelectionModel<PERIODFILTER> selection) {
		periodFilterSelectionModel = selection;
	}


	
	/* *****************************************************************************
	 * **************************** GROUP FILTER ***********************************
	 * *************************************************************************** */
	
	private SingleSelectionModel<Group> groupFilterSelectionModel;
	@UiField(provided=true) ValueListBox<Group> groupFilter;
	
	/**
	 * Mantiene en sincronía el selectionModel y el Value del ValueListBox
	 */
	private final ValueChangeHandler<Group> groupChangeHandler = new ValueChangeHandler<Group>() {
		@Override
		public void onValueChange(ValueChangeEvent<Group> g) {
			if (groupFilterSelectionModel == null) {
				return;
			}
			Group g2 = groupFilter.getValue();
			if (g2 == null) {
				groupFilterSelectionModel.clear();
			} else {
				groupFilterSelectionModel.setSelected(g2, true);
			}
		}
	};	
	
	private void createGroupFilter() {
		groupFilter = new ValueListBox<Group>(new Renderer<Group>() {
			@Override
			public String render(Group g) {
				if (g == null) {
					return "Todos";
				}
				return g.getName();
			}
			@Override
			public void render(Group g, Appendable appendable) throws IOException {
				if (g == null) {
					return;
				}
				appendable.append(g.getName());
			}
		});
		groupFilter.addValueChangeHandler(groupChangeHandler);
	}
	
	/**
	 * Setea los grupos.
	 * por defecto setea null. o sea todos los grupos posibles.
	 */	
	@Override
	public void setGroups(List<Group> groups) {
		List<Group> groupsc = new ArrayList<Group>();
		if (groups == null || groups.size() <= 0) {
			groupsc.add(null);
			groupFilter.setAcceptableValues(groupsc);
			groupFilter.setValue(null);
		} else {
			groupsc.addAll(groups);
			GroupsSort.sort(groupsc);
			groupFilter.setAcceptableValues(groupsc);
			groupFilter.setValue(null);
		}	
	}
	
	@Override
	public void setGroupSelectionModel(SingleSelectionModel<Group> selection) {
		groupFilterSelectionModel = selection;
	}
	

	/* *****************************************************************************
	 * ********************************* PERSONS ***********************************
	 * *************************************************************************** */
	
	//filtros de dni y nombre
	private final FilterPerson[] filters = new FilterPerson[] {new FilterPersonDni(),new FilterPersonName()};
	private Timer filterTimer = null;
	
	@UiField(provided=true) TextBox filter;
	@UiField(provided=true) Label usersCount;
	@UiField(provided=true) DataGrid<Person> persons;
	
	private final AssistanceResources resources = GWT.create(AssistanceResources.class);
	
	private final List<Person> personsData;
	private final List<Person> personsFilteredData;
	
	private void createFilter() {
		usersCount = new Label("");
		
		filter = new TextBox();
		filter.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (filterTimer != null) {
					filterTimer.cancel();
				}
				filterTimer = new Timer() {
					@Override
					public void run() {
						filterTimer = null;
						filterPersons();
					}
				};
				filterTimer.schedule(2000);
			}
		});
	}
	
	private void filterPersons() {
		String ft = filter.getText();
		if (ft == null || ft.trim().equals("")) {
			usersCount.setText(String.valueOf(personsData.size()));
			persons.setRowData(personsData);
			return;
		}
		//aplico el filtro de acuerdo a los Filter que tenga definidos
		//ahora solo es el dni y si no el nombre
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
	
	private void createPersons() {
		String imageHtml = AbstractImagePrototype.create(resources.user()).getHTML();
		PersonCell pc = new PersonCell(imageHtml);
		
		IdentityColumn<Person> personColumn = new IdentityColumn<Person>(pc);
		
		persons = new DataGrid<Person>();
		persons.addColumn(personColumn);
	}
	
	@Override
	public void setPersons(List<Person> persons) {
		personsData.clear();
		if (persons == null) {
			filterPersons();
			return;
		}
		PersonSort.sort(persons);
		personsData.addAll(persons);
		filterPersons();
	}
	
	@Override
	public void setPersonSelectionModel(SingleSelectionModel<Person> selection) {
		persons.setSelectionModel(selection);
	}
	
	private void clearPersons() {
		personsData.clear();
		personsFilteredData.clear();
		
		usersCount.setText("0");
		persons.setRowCount(0);
		persons.setRowData(personsData);
	}
	
	
	
	/* *****************************************************************************
	 * ********************************* PERIOD ************************************
	 * *************************************************************************** */	
	
	@UiField(provided=true) DataGrid<Report> periods;
	private ListDataProvider<Report> periodsDataProvider;
	
	private void createPeriods() {
		
		final DateTimeFormat dateF = DateTimeFormat.getFormat(PredefinedFormat.DATE_SHORT);
		final DateTimeFormat timeF = DateTimeFormat.getFormat(PredefinedFormat.TIME_SHORT);
		
		TextColumn<Report> notes = new TextColumn<Report>() {
			@Override
			public String getValue(Report object) {
				if (object == null) {
					return "";
				}
				AssistancePersonData data = presenter.assistanceData();
				if (data == null) {
					return "";
				}
				return data.getNotes();
			}
		};
		
		final TextColumn<Report> dateS = new TextColumn<Report>() {
			@Override
			public String getValue(Report object) {
				if (object == null || object.getPeriod() == null) {
					return "";
				}
				Period period = object.getPeriod();
				Date start = period.getStart();
				if (start == null) {
					return "no tiene";
				}
				return dateF.format(start);
			}
		};
		
		final TextColumn<Report> hourS = new TextColumn<Report>() {
			@Override
			public String getValue(Report object) {
				if (object == null || object.getPeriod() == null) {
					return "";
				}
				Period period = object.getPeriod();
				List<? extends WorkedHours> whs = period.getWorkedHours();
				if (whs == null || whs.size() <= 0) {
					return "no tiene";
				}
				
				WorkedHours wh = whs.get(0);
				if (wh.getLogs() != null && wh.getInLog().getDate() != null) {
					return timeF.format(wh.getInLog().getDate());
				}
				
				return "no tiene";
			}
		};
		
		final TextColumn<Report> dateE = new TextColumn<Report>() {
			@Override
			public String getValue(Report object) {
				if (object == null || object.getPeriod() == null) {
					return "";
				}
				Period period = object.getPeriod();
				Date end = period.getEnd();
				if (end == null) {
					return "no tiene";
				}
				return dateF.format(end);
			}
		};
		
		final TextColumn<Report> hourE = new TextColumn<Report>() {
			@Override
			public String getValue(Report object) {
				if (object == null || object.getPeriod() == null) {
					return "";
				}
				
				Period period = object.getPeriod();
				List<WorkedHours> whs = period.getWorkedHours();
				if (whs == null || whs.size() <= 0) {
					return"no tiene";
				}
				
				Date last = WorkedHoursUtil.getLastDate(whs);
				if (last == null) {
					return "no tiene";
				}
				
				return timeF.format(last);
			}
		};
			
		final TextColumn<Report> hours = new TextColumn<Report>() {
			@Override
			public String getValue(Report object) {
				if (object == null) {
					return "";
				}
				Long min = object.getMinutes();
				if (min == null) {
					return "";
				}
				String hS = WorkedHoursUtil.fhm((int)(min / 60));
				String mS = WorkedHoursUtil.fhm((int)(min % 60));
				return hS + ":" + mS;
			}
		};
			
		Cell<Report> justificationActionCell = JustificationActionCell.createCell(new JustificationActionCellPresenter<Report>() {
			
			@Override
			public void justify() {
				Periods.this.presenter.justify();
			}
			
			@Override
			public void deleteJustification(JustificationDate j) {
				Periods.this.presenter.removeJustification(j);
			}
		});
		
		Column<Report,Report> justified = new Column<Report,Report>(justificationActionCell) {
			@Override
			public Report getValue(Report object) {
				return object;
			}
		};
				
		TextColumn<Report> generallyJustified = new TextColumn<Report>() {
			@Override
			public String getValue(Report object) {
				if (object == null || object.getGjustifications() == null) {
					return "";
				}
				
				List<GeneralJustificationDate> gjds = object.getGjustifications();
				GeneralJustificationDate gjd = gjds.get(0);
				if (gjd != null) {
					if (gjd.getJustification() == null) {
						return "error obteniendo justificación";
					}
					return "* " + gjd.getJustification().getCode();
				}
				
				return "";
			}
		};
			

		periods = new DataGrid<Report>();
		periods.addColumn(notes,"Notas");
		periods.addColumn(dateS, "Fecha Entrada");
		periods.addColumn(hourS, "Hora Entrada");
		periods.addColumn(dateE, "Fecha Salida");
		periods.addColumn(hourE, "Hora Salida");
		periods.addColumn(hours, "Cantidad de horas");
		periods.addColumn(justified, "Justificado");
		periods.addColumn(generallyJustified, "JustificadoGeneral");
		
		//seteo la parte de ordenación
		
		periodsDataProvider = new ListDataProvider<Report>();
		periodsDataProvider.addDataDisplay(periods);
		
		ListHandler<Report> sortHandler = new ListHandler<Report>(periodsDataProvider.getList());
		periods.addColumnSortHandler(sortHandler);
		
		dateS.setSortable(true);
		hourS.setSortable(true);
		dateE.setSortable(true);
		hourE.setSortable(true);
		hours.setSortable(true);
		
		periods.getColumnSortList().push(dateS);
		
		sortHandler.setComparator(dateS, new Comparator<Report>() {			
			@Override
			public int compare(Report o1, Report o2) {
				if (o1 == null && o2 == null) {
					return 0;
				}
				if (o1 == null) {
					return -1;
				}
				if (o2 == null) {
					return 1;
				}
				
				Period p1 = o1.getPeriod();
				Period p2 = o2.getPeriod();
				if (p1 == null && p2 == null) {
					return 0;
				}
				if (p1 == null) {
					return -1;
				}
				if (p2 == null) {
					return 1;
				}
				
				if (p1.getStart() == null && p2.getStart() == null) {
					return 0;
				}
				if (p1.getStart() == null) {
					return -1;
				}
				if (p2.getStart() == null) {
					return 1;
				}
				return p1.getStart().compareTo(p2.getStart());
			}
		});
		
		sortHandler.setComparator(hourS, new Comparator<Report>() {
			@Override
			public int compare(Report o1, Report o2) {
				String d1 = hourS.getValue(o1);
				String d2 = hourS.getValue(o2);
				return d1.compareTo(d2);
			}
		});
		
		sortHandler.setComparator(dateE, new Comparator<Report>() {			
			@Override
			public int compare(Report o1, Report o2) {
				if (o1 == null && o2 == null) {
					return 0;
				}
				if (o1 == null) {
					return -1;
				}
				if (o2 == null) {
					return 1;
				}
				
				Period p1 = o1.getPeriod();
				Period p2 = o2.getPeriod();
				if (p1 == null && p2 == null) {
					return 0;
				}
				if (p1 == null) {
					return -1;
				}
				if (p2 == null) {
					return 1;
				}
				
				if (p1.getEnd() == null && p2.getEnd() == null) {
					return 0;
				}
				if (p1.getEnd() == null) {
					return -1;
				}
				if (p2.getEnd() == null) {
					return 1;
				}
				return p1.getEnd().compareTo(p2.getEnd());
			}
		});
		
		sortHandler.setComparator(hourE, new Comparator<Report>() {
			@Override
			public int compare(Report o1, Report o2) {
				String d1 = hourE.getValue(o1);
				String d2 = hourE.getValue(o2);
				return d1.compareTo(d2);
			}
		});
		
		sortHandler.setComparator(hours, new Comparator<Report>() {
			@Override
			public int compare(Report o1, Report o2) {
				String d1 = hours.getValue(o1);
				String d2 = hours.getValue(o2);
				return d1.compareTo(d2);
			}
		});
	}
	
	@Override
	public void setPeriods(List<Report> periods) {
		if (periods == null) {
			return;
		}
		
		this.periods.setVisibleRange(new Range(0, periods.size()));
		periodsDataProvider.getList().clear();
		periodsDataProvider.getList().addAll(periods);
		periodsDataProvider.refresh();
		
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				ColumnSortEvent.fire(Periods.this.periods, Periods.this.periods.getColumnSortList());
			}
		});
		
		/*
		 * TODO: falta implementar la parte de estadistica
		 */
		//setStatistics(periods);
	}
	
	@Override
	public void setPeriodSelectionModel(MultiSelectionModel<Report> selection) {
		periods.setSelectionModel(selection);
	}
	
	@Override
	public void redrawPeriods() {
		periods.redraw();
	}
	
	@Override
	public void clearPeriodData() {
		periodsDataProvider.getList().clear();
		periodsDataProvider.refresh();
		
		//clearStatistics();
	}
	
}