package ar.com.dcsys.gwt.assistance.client.ui.period.daily;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.data.justification.GeneralJustificationDate;
import ar.com.dcsys.data.justification.JustificationDate;
import ar.com.dcsys.data.period.Period;
import ar.com.dcsys.data.period.WorkedHours;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.report.Report;
import ar.com.dcsys.gwt.assistance.client.common.GroupsSort;
import ar.com.dcsys.gwt.assistance.client.ui.period.PERIODFILTER;
import ar.com.dcsys.gwt.assistance.client.ui.period.WorkedHoursUtil;
import ar.com.dcsys.gwt.assistance.client.ui.period.cells.JustificationActionCell;
import ar.com.dcsys.gwt.assistance.client.ui.period.cells.JustificationActionCellPresenter;
import ar.com.dcsys.utils.PersonSort;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.SingleSelectionModel;

public class DailyPeriods extends Composite implements DailyPeriodsView {

	private static DailyPeriodsUiBinder uiBinder = GWT.create(DailyPeriodsUiBinder.class);

	interface DailyPeriodsUiBinder extends UiBinder<Widget, DailyPeriods> {
	}


	private Presenter p;
	
	
	public DailyPeriods() {
		createDates();
		createPeriodFilter();
		createGroupFilter();
		createPeriods();
		
		initWidget(uiBinder.createAndBindUi(this));
		
	}

	@Override
	public void setPresenter(Presenter p) {
		this.p = p;
	}

	@Override
	public void clear() {
		clearPeriodFilter();
		clearPeriodData();
	}
	
	
	/* *****************************************************************************
	 * *********************************** DATES ***********************************
	 * *************************************************************************** */	
	
	@UiField(provided=true) DateBox date;
	
	
	private void createDates() {
		DateTimeFormat defaultF = DateTimeFormat.getFormat(PredefinedFormat.DATE_SHORT);

		date = new DateBox();
		date.setFormat(new DateBox.DefaultFormat(defaultF));
		date.addValueChangeHandler(new ValueChangeHandler<Date>() {
			@Override
			public void onValueChange(ValueChangeEvent<Date> event) {
				Date datev = event.getValue();
				datev.setHours(0);
				datev.setMinutes(0);
				datev.setSeconds(0);
				date.setValue(datev);
			}
		});
		
	}	
	
	
	@Override
	public Date getDate() {
		return date.getValue();
	}



	@Override
	public void setDate(Date date) {
		this.date.setValue(date);
	}


	/* *****************************************************************************
	 * *********************************** FIND ***********************************
	 * *************************************************************************** */
	
	@UiHandler("find")
	public void onFind(ClickEvent event) {
		p.findPeriods();
	}	
	

	/* *****************************************************************************
	 * ******************************* PERIODFILTER ********************************
	 * *************************************************************************** */

	
	
	private SingleSelectionModel<PERIODFILTER> periodFilterSelectionModel;
	
	@UiField(provided=true) ValueListBox<PERIODFILTER> periodFilter;

	
	/**
	 * Mantiene en sincronía el selectionModel y el Value de la ValueListBox
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
	public void setPeriodFilterSelectionModel(SingleSelectionModel<PERIODFILTER> selection) {
		periodFilterSelectionModel = selection;
	}	
	
	@Override
	public void setPeriodFilterValues(List<PERIODFILTER> values) {
		periodFilter.setValue(values.get(0));
		periodFilter.setAcceptableValues(values);
	}
	
	
	/* *****************************************************************************
	 * **************************** GROUP FILTER ***********************************
	 * *************************************************************************** */
	
	@UiField(provided=true) ValueListBox<Group> groupFilter;
	
	private SingleSelectionModel<Group> groupFilterSelectionModel;

	/**
	 * Mantiene en sincronía el selectionModel y el Value de la ValueListBox
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
			public void render(Group g, Appendable a) throws IOException {
				if (g == null) {
					a.append("Todos");
				} else {
					a.append(g.getName());
				}
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
	 * ********************************* PERIOD ************************************
	 * *************************************************************************** */
	
	
	@UiField(provided=true) DataGrid<Report> periods;
	private ListDataProvider<Report> periodsDataProvider;
	

	private String getFullName(Person person) {
		String name = person.getName();
		String lastName = person.getLastName();
		return ((lastName != null) ? lastName : "No tiene") + " " +((name != null) ? name : "No tiene");
	}
	
	private void createPeriods() {
		
		final DateTimeFormat dateF = DateTimeFormat.getFormat(PredefinedFormat.DATE_SHORT);
		final DateTimeFormat timeF = DateTimeFormat.getFormat(PredefinedFormat.TIME_SHORT);
		
		final TextColumn<Report> name = new TextColumn<Report>() {
			@Override
			public String getValue(Report report) {
				if (report == null) {
					return "no existe";
				}
				
				Person p = report.getPerson();
				
				if (p == null || p.getName() == null || p.getLastName() == null) {
					return "no tiene";
				}

				return getFullName(p);
			}
		};

		
		
		final TextColumn<Report> dni = new TextColumn<Report>() {
			@Override
			public String getValue(Report report) {
				if (report == null) {
					return "no existe";
				}
				
				Person p = report.getPerson();
				
				if (p == null || p.getDni() == null) {
					return "no tiene";
				}

				return p.getDni();
			}
		};
		
		final TextColumn<Report> date = new TextColumn<Report>() {
			@Override
			public String getValue(Report report) {
				if (report == null) {
					return "";
				}
				Period period = report.getPeriod();
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
				List<WorkedHours> whs = period.getWorkedHours();
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
				DailyPeriods.this.p.justify();
			}
			
			@Override
			public void deleteJustification(JustificationDate j) {
				DailyPeriods.this.p.removeJustification(j);
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
				if (object == null || object.getGjustifications() == null || object.getGjustifications().size() == 0) {
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
		periods.addColumn(name, "Nombre");
		periods.addColumn(dni, "Dni");
//		periods.addColumn(notes, "Notas");
		periods.addColumn(date, "Fecha");
		periods.addColumn(hourS, "Hora Entrada");
		//periods.addColumn(dateE, "Fecha Salida");
		periods.addColumn(hourE, "Hora Salida");
		periods.addColumn(hours, "Cantidad de Horas");
		periods.addColumn(justified,"Justificado");
		periods.addColumn(generallyJustified,"Justificado General");
		
		
		//seteo la parte de ordenación
		
		periodsDataProvider = new ListDataProvider<Report>();
		periodsDataProvider.addDataDisplay(periods);

		ListHandler<Report> sortHandler = new ListHandler<Report>(periodsDataProvider.getList());
		periods.addColumnSortHandler(sortHandler);
		
		name.setSortable(true);
		dni.setSortable(true);
		hourS.setSortable(true);
		hourE.setSortable(true);
		hours.setSortable(true);
		
		periods.getColumnSortList().push(name);
		
		final Comparator<Report> nameComparator = new Comparator<Report>() {
			private final Comparator<Person> personComparator = PersonSort.getComparator();
			@Override
			public int compare(Report p1, Report p2) {
				if (p1 == null && p2 == null) {
					return 0;
				}
				if (p1 == null) {
					return -1;
				}
				if (p2 == null) {
					return 1;
				}
				return personComparator.compare(p1.getPerson(), p2.getPerson());
			}
		};
		
		sortHandler.setComparator(name, nameComparator);
		
		sortHandler.setComparator(dni, new Comparator<Report>() {
			@Override
			public int compare(Report p1, Report p2) {
				if (p1 == null && p2 == null) {
					return 0;
				}
				if (p1 == null) {
					return -1;
				}
				if (p2 == null) {
					return 1;
				}

				if (p1.getPerson() == null && p2.getPerson() == null) {
					return 0;
				}
				if (p1.getPerson() == null) {
					return -1;
				}
				if (p2.getPerson() == null) {
					return 1;
				}
				
				if (p1.getPerson().getDni() == null && p2.getPerson().getDni() == null) {
					return 0;
				}
				if (p1.getPerson().getDni() == null) {
					return -1;
				}
				if (p2.getPerson().getDni() == null) {
					return 1;
				}
				return p1.getPerson().getDni().compareTo(p2.getPerson().getDni());
			}
		});
		
		sortHandler.setComparator(hourS, new Comparator<Report>() {
			@Override
			public int compare(Report p1, Report p2) {
				String s1 = hourS.getValue(p1);
				String s2 = hourS.getValue(p2);
				int data = s1.compareTo(s2);
				
				if (data != 0) {
					return data;
				}
				return nameComparator.compare(p1, p2);
			}				

		});
		
		sortHandler.setComparator(hourE, new Comparator<Report>() {
			@Override
			public int compare(Report p1, Report p2) {
				String s1 = hourE.getValue(p1);
				String s2 = hourE.getValue(p2);
				int data = s1.compareTo(s2);

				if (data != 0) {
					return data;
				}
				return nameComparator.compare(p1, p2);
			}
		});
		
		sortHandler.setComparator(hours, new Comparator<Report>() {
			@Override
			public int compare(Report p1, Report p2) {
				String s1 = hours.getValue(p1);
				String s2 = hours.getValue(p2);
				int data = s1.compareTo(s2);
				
				if (data != 0) {
					return data;
				}
				return nameComparator.compare(p1, p2);
				
			}
		});
		
	}		
	
	
	@Override
	public void clearPeriodData() {
		periodsDataProvider.getList().clear();
		periodsDataProvider.refresh();
	}
	

	@Override
	public void redrawPeriods() {
		periods.redraw();
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
				ColumnSortEvent.fire( DailyPeriods.this.periods, DailyPeriods.this.periods.getColumnSortList());
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
	
	
}
