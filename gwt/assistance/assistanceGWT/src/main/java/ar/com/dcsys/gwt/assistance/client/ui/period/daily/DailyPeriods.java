package ar.com.dcsys.gwt.assistance.client.ui.period.daily;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.data.justification.GeneralJustificationDate;
import ar.com.dcsys.data.justification.Justification;
import ar.com.dcsys.data.justification.JustificationDate;
import ar.com.dcsys.data.period.Period;
import ar.com.dcsys.data.period.WorkedHours;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.report.Report;
import ar.com.dcsys.gwt.assistance.client.activity.justification.JustificationStatistic;
import ar.com.dcsys.gwt.assistance.client.common.GroupsSort;
import ar.com.dcsys.gwt.assistance.client.common.JustificationsSort;
import ar.com.dcsys.gwt.assistance.client.ui.period.PERIODFILTER;
import ar.com.dcsys.gwt.assistance.client.ui.period.WorkedHoursUtil;
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
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
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

	@UiField DailyPeriodsViewResources res;

	private Presenter p;
	
	
	public DailyPeriods() {
		createDates();
		createPeriodFilter();
		createGroupFilter();
		createFilter();
		createPeriods();
		createStatistic();
		createFind();
		createType();
		createJustify();
		
		initWidget(uiBinder.createAndBindUi(this));
		
		periodsData = new ArrayList<Report>();
		periodsFilteredData = new ArrayList<Report>();
		
	}

	@Override
	public void setPresenter(Presenter p) {
		this.p = p;
	}

	@Override
	public void clear() {
		clearPeriodFilter();
		clearPeriodData();
		clearJustificationData();
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
	
	@UiField(provided=true) Button find;
	
	private void createFind() {
		find = new Button();
		find.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				if (p != null) {
					p.findPeriods();
				}
			}
		});
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
	
	//filtros de dni y nombre
	private final FilterPerson[] filters = new FilterPerson[] {new FilterPersonDni(),new FilterPersonName()};
	private Timer filterTimer = null;	
	
	@UiField(provided=true) TextBox filter;
	@UiField(provided=true) Label usersCount;
	
	
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
						filterPeriods();
					}
				};
				filterTimer.schedule(2000);
			}
		});
	}	
	
	private final List<Report> periodsData;
	private final List<Report> periodsFilteredData;
	
	private void filterPeriods() {
		String ft = filter.getText();
		if (ft == null || ft.trim().equals("")) {
			usersCount.setText(String.valueOf(periodsData.size()));
			periodsDataProvider.getList().clear();
			periodsDataProvider.getList().addAll(periodsData);
			periodsDataProvider.refresh();
			return;
		}
		//aplico el filtro de acuerdo a los Filter que tenga definidos
		//ahora solo es el dni y si no el nombre
		ft = ft.toLowerCase();
		periodsFilteredData.clear();
		for (Report r : periodsData) {
			Person p = r.getPerson();
			for (FilterPerson f : filters) {
				if (f.checkFilter(p, ft)) {
					periodsFilteredData.add(r);
					break;
				}
			}
		}
		usersCount.setText(String.valueOf(periodsFilteredData.size()));
		periodsDataProvider.getList().clear();
		periodsDataProvider.getList().addAll(periodsFilteredData);
		periodsDataProvider.refresh();
	}	

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
		
		periodsData.clear();
		periodsFilteredData.clear();
		
		usersCount.setText("0");
		
		clearStatistics();
	}
	

	@Override
	public void redrawPeriods() {
		periods.redraw();
	}	
	
	@Override
	public void setPeriods(List<Report> periods) {
		periodsData.clear();
		if (periods == null) {
			filterPeriods();
			return;
		}

		
		this.periods.setVisibleRange(new Range(0, periods.size()));
		periodsData.addAll(periods);
		
		filterPeriods();
		
		/*if (periods == null) {
			return;
		}
		
		this.periods.setVisibleRange(new Range(0, periods.size()));
		periodsDataProvider.getList().clear();
		periodsDataProvider.getList().addAll(periods);
		periodsDataProvider.refresh();*/
		
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				ColumnSortEvent.fire( DailyPeriods.this.periods, DailyPeriods.this.periods.getColumnSortList());
			}
		});		

		setStatistics(periods);
	}
	
	@Override
	public void setPeriodSelectionModel(MultiSelectionModel<Report> selection) {
		periods.setSelectionModel(selection);
	}
	
	
	/* *****************************************************************************
	 * *********************************** Statics *********************************
	 * *************************************************************************** */
	
	@UiField TabLayoutPanel tabLayoutPanel;
	@UiField(provided=true)DataGrid<JustificationStatistic> statistics;
	
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
	
	private void clearStatistics() {
		statistics.setRowCount(0);
		statistics.setRowData(new ArrayList<JustificationStatistic>());
	}
	
	/**
	 * Calcula la estadisticas para mostrarlas.
	 * es ineficiente porque esto ya se consulta en los cells del dataGrid.
	 * @param periods
	 */
	private void setStatistics(List<Report> periods) {
		if (periods == null) {
			return;
		}
		
		//calculo la cantidad de justificaciones para cada tipo
		JustificationStatistic absence = new JustificationStatistic("Ausencias","Períodos sin ninguna marcación");
		JustificationStatistic justifiedAbsences = new JustificationStatistic("Ausencias justificadas","Períodos sin marcaciones pero justificados");
		JustificationStatistic unjustifiedAbsences = new JustificationStatistic("Ausencias injustificadas","Períodos sin marcaciones y sin justificación");		
		JustificationStatistic periodsCount = new JustificationStatistic("Períodos", "Cantidad de perídos");
		periodsCount.setCount(periods.size());
		
		Map<String,JustificationStatistic> counters = new HashMap<String,JustificationStatistic>();
		for (Report r : periods) {
			
			//chequeo dato de justificaciones
			List<JustificationDate> jds = r.getJustifications();
			if (jds != null && jds.size() > 0) {
				for (JustificationDate jd : jds) {
					if (jd != null) {
						String code = jd.getJustification().getCode();
						JustificationStatistic js = counters.get(code);
						if (js == null) {
							js = new JustificationStatistic(code, jd.getJustification().getDescription());
							counters.put(code, js);
						}
						js.incrementCount();
					}
				}
			}
			
			//agrego datos de faltas
			Period period = r.getPeriod();
			if (period != null) {
				if (period.getWorkedHours() == null || period.getWorkedHours().size() <= 0) {
					absence.incrementCount();
					if (jds == null || jds.size() <= 0) {
						unjustifiedAbsences.incrementCount();
					} else {
						justifiedAbsences.incrementCount();
					}
				}
			}
		}
		
		List<JustificationStatistic> lcounters = new ArrayList<JustificationStatistic>();
		lcounters.add(periodsCount);
		lcounters.add(absence);
		lcounters.add(unjustifiedAbsences);
		lcounters.add(justifiedAbsences);		
		lcounters.addAll(counters.values());
		
		statistics.setRowData(lcounters);
		
	}	

	/* *****************************************************************************
	 * ********************************** Justificar *******************************
	 * *************************************************************************** */
	
	@UiField(provided=true) ValueListBox<Justification> types;
	@UiField TextArea justifyNotes;
	@UiField(provided=true) Button justify;
	private SingleSelectionModel<Justification> typeSelection;
	
	private void createType() {
		types = new ValueListBox<Justification>(new Renderer<Justification>() {
			private String getValue(Justification type) {
				if (type == null) {
					return "";
				}
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
			public void render(Justification object, Appendable appendable)	throws IOException {
				if (object == null) {
					return;
				}
				appendable.append(getValue(object));
			}
		});
		
		types.addValueChangeHandler(new ValueChangeHandler<Justification>() {
			@Override
			public void onValueChange(ValueChangeEvent<Justification> event) {
				justifyNotes.setText("");
				
				if (typeSelection != null) {
					Justification t = types.getValue();
					if (t == null) {
						typeSelection.clear();
					} else {
						typeSelection.setSelected(t,true);
					}
				}
			}
		});
	}
	
	@Override
	public void setJustificationSelectionModel(SingleSelectionModel<Justification> selection) {
		typeSelection = selection;
	}	
	
	@Override
	public void setJustifications(List<Justification> types) {
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
	public void clearJustificationData() {
		justifyNotes.setText("");
		enableJustify(false);
	}
	
	@Override
	public String getNotes() {
		return justifyNotes.getValue();
	}
	
	private void createJustify() {
		justify = new Button();
		justify.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				if (p == null) {
					return;
				}
				p.justify();
			}
		});
	}
	
	@Override
	public void enableJustify(boolean t) {
		justify.setEnabled(t);
	}	
	
}
