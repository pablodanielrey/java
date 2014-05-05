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
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.gwt.assistance.client.activity.justification.JustificationStatistic;
import ar.com.dcsys.gwt.assistance.client.common.JustificationsSort;
import ar.com.dcsys.gwt.assistance.client.ui.period.PERIODFILTER;
import ar.com.dcsys.utils.PersonSort;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.TextArea;
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

	@UiField TabLayoutPanel tabLayoutPanel;

	private Presenter p;
	
	
	public DailyPeriods() {
		createPeriodFilter();
		createGroupFilter();
		createStatistic();
		createDates();
		createPeriods();
		createType();
		
		initWidget(uiBinder.createAndBindUi(this));
		
		// para cubrir el bug del tablayoutpanel
		tabLayoutPanel.addSelectionHandler(new SelectionHandler<Integer>() {
			@Override
			public void onSelection(SelectionEvent<Integer> event) {
				statistics.redraw();
			}
		});		
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
	
	
	
	
	
	
	
	/////////////////////// GROUP FILTER ///////////////////////////////////////////////
	
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
	
	@Override
	public void setGroups(List<Group> groups) {
		if (groups == null || groups.size() <= 0) {
			groupFilter.setAcceptableValues(new ArrayList<Group>());
			groupFilter.setValue(null);
			return;
		}
//		GroupsSort.sort(groups);
		groupFilter.setValue(null);
		groupFilter.setAcceptableValues(groups);
	}	
	
	@Override
	public void setGroupSelectionModel(SingleSelectionModel<Group> selection) {
		groupFilterSelectionModel = selection;
	}
	
		
	
	
	
	
	
	//////////////////////////////////////////// PERIOD FILTER /////////////////////////

	
	
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
			periodFilterSelectionModel.setSelected(item, true);
		}
	};		
	
	private void clearPeriodFilter() {
		periodFilter.setAcceptableValues(new ArrayList<PERIODFILTER>());
	}

	
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
	
	@Override
	public void setPeriodFilterSelectionModel(SingleSelectionModel<PERIODFILTER> selection) {
		periodFilterSelectionModel = selection;
	}	
	
	@Override
	public void setPeriodFilterValues(List<PERIODFILTER> values) {
		periodFilter.setValue(values.get(0));
		periodFilter.setAcceptableValues(values);
	}
	
	
	
	
	
	
	
	////////////////////// PERIODS //////////////////////////////////////
	
	
	@UiField(provided=true) DataGrid<PersonPeriodContainer> periods;
	private ListDataProvider<PersonPeriodContainer> periodsDataProvider;
	

	@Override
	public void redrawPeriods() {
		periods.redraw();
	}	


	private String getFullName(Person person) {
		String name = person.getName();
		String lastName = person.getLastName();
		return ((lastName != null) ? lastName : "No tiene") + " " +((name != null) ? name : "No tiene");
	}
	
	private void createPeriods() {
		
		final DateTimeFormat dateF = DateTimeFormat.getFormat(PredefinedFormat.DATE_SHORT);
		final DateTimeFormat timeF = DateTimeFormat.getFormat(PredefinedFormat.TIME_SHORT);
		
		final TextColumn<PersonPeriodContainer> name = new TextColumn<PersonPeriodContainer>() {
			@Override
			public String getValue(PersonPeriodContainer pp) {
				if (pp == null) {
					return "no existe";
				}
				
				if (pp.person == null || pp.person.getName() == null || pp.person.getLastName() == null) {
					return "no tiene";
				}

				return getFullName(pp.person);
			}
		};

		
		
		final TextColumn<PersonPeriodContainer> dni = new TextColumn<PersonPeriodContainer>() {
			@Override
			public String getValue(PersonPeriodContainer pp) {
				if (pp == null) {
					return "no existe";
				}
				
				if (pp.person == null || pp.person.getDni() == null) {
					return "no tiene";
				}

				return pp.person.getDni();
			}
		};
		
		/*
		TextColumn<PersonPeriodContainer> notes = new TextColumn<PersonPeriodContainer>() {
			@Override
			public String getValue(PersonPeriodContainer object) {
				if (object == null) {
					return "";
				}
				if (object.person == null) {
					return "";
				}
				AssistancePersonData data = p.assistanceData(object.person);
				if (data == null) {
					return "";
				}
				return data.getNotes();
			}
		};
		*/
		
		final TextColumn<PersonPeriodContainer> date = new TextColumn<PersonPeriodContainer>() {
			@Override
			public String getValue(PersonPeriodContainer object) {
				if (object == null) {
					return "";
				}
				Date start = object.period.getDate();
				if (start == null) {
					return "no tiene";
				}
				return dateF.format(start);
			}
		};
		
		final TextColumn<PersonPeriodContainer> hourS = new TextColumn<PersonPeriodContainer>() {
			@Override
			public String getValue(PersonPeriodContainer object) {
				if (object == null) {
					return "";
				}
				
				Date start = object.period.getStart();
				if (start == null) {
					return "No tiene";
				}
				
				return timeF.format(start);
			}
		};
		
		final TextColumn<PersonPeriodContainer> hourE = new TextColumn<PersonPeriodContainer>() {
			@Override
			public String getValue(PersonPeriodContainer object) {
				if (object == null) {
					return "";
				}

				Date end = object.period.getEnd();
				if (end == null) {
					return "No tiene";
				}
				
				return timeF.format(end);
			}
		};
		
		final TextColumn<PersonPeriodContainer> hours = new TextColumn<PersonPeriodContainer>() {
			@Override
			public String getValue(PersonPeriodContainer object) {
				if (object == null) {
					return "";
				}
				
				Integer minutes = object.period.getMinutes();
				if (minutes == null) {
					return "No tiene";
				}
				
				int hour = minutes / 60;
				int min = minutes % 60;
				
				NumberFormat nf = NumberFormat.getFormat("00");
				String hoursS = nf.format(hour) + ":" + nf.format(min);
				
				return hoursS;
			}
		};
		
/*
		Cell<PersonPeriodContainer> justificationActionCellPersonPeriodContainer = JustificationActionCellPersonPeriodContainer.createCell(new JustificationActionCellPresenter<PersonPeriodContainer>() {
			@Override
			public void justify() {
				p.justify();
			}
			
			@Override
			public JustificationDate getJustifications(PersonPeriodContainer p) {
				return DailyPeriods.this.p.justified(p);
			}
			
			@Override
			public void deleteJustification(JustificationDate j) {
				DailyPeriods.this.p.removeJustification(j);
			}
		});
		
		Column<PersonPeriodContainer,PersonPeriodContainer> justified = new Column<PersonPeriodContainer,PersonPeriodContainer>(justificationActionCellPersonPeriodContainer) {
			@Override
			public PersonPeriodContainer getValue(PersonPeriodContainer object) {
				return object;
			}
		};
		
		
		*/
		
		TextColumn<PersonPeriodContainer> generallyJustified = new TextColumn<PersonPeriodContainer>() {
			@Override
			public String getValue(PersonPeriodContainer object) {
				if (p == null) {
					return "";
				}
				
				//GeneralJustificationDate gjd = p.generalJustified(object);
				GeneralJustificationDate gjd = null;
				if (gjd != null) {
					if (gjd.getJustification() == null) {
						return "error obteniendo justificación";
					}
					return "* " + gjd.getJustification().getCode();
				}
				
				return "";
			}
		};
		
		periods = new DataGrid<PersonPeriodContainer>();
		periods.addColumn(name, "Nombre");
		periods.addColumn(dni, "Dni");
//		periods.addColumn(notes, "Notas");
		periods.addColumn(date, "Fecha");
		periods.addColumn(hourS, "Hora Entrada");
		//periods.addColumn(dateE, "Fecha Salida");
		periods.addColumn(hourE, "Hora Salida");
		periods.addColumn(hours, "Cantidad de Horas");
		//periods.addColumn(justified,"Justificado");
		periods.addColumn(generallyJustified,"Justificado General");
		
		
		//// hacer sortable a las columnas requeridas ////
		
		periodsDataProvider = new ListDataProvider<PersonPeriodContainer>();
		periodsDataProvider.addDataDisplay(periods);

		ListHandler<PersonPeriodContainer> sortHandler = new ListHandler<PersonPeriodContainer>(periodsDataProvider.getList());
		periods.addColumnSortHandler(sortHandler);
		
		name.setSortable(true);
		dni.setSortable(true);
		hourS.setSortable(true);
		hourE.setSortable(true);
		hours.setSortable(true);
		
		periods.getColumnSortList().push(name);
		
		final Comparator<DailyPeriodsView.PersonPeriodContainer> nameComparator = new Comparator<DailyPeriodsView.PersonPeriodContainer>() {
			private final Comparator<Person> personComparator = PersonSort.getComparator();
			@Override
			public int compare(PersonPeriodContainer p1, PersonPeriodContainer p2) {
				if (p1 == null && p2 == null) {
					return 0;
				}
				if (p1 == null) {
					return -1;
				}
				if (p2 == null) {
					return 1;
				}
				return personComparator.compare(p1.person, p2.person);
			}
		};
		
		sortHandler.setComparator(name, nameComparator);
		
		sortHandler.setComparator(dni, new Comparator<DailyPeriodsView.PersonPeriodContainer>() {
			@Override
			public int compare(PersonPeriodContainer p1, PersonPeriodContainer p2) {
				if (p1 == null && p2 == null) {
					return 0;
				}
				if (p1 == null) {
					return -1;
				}
				if (p2 == null) {
					return 1;
				}

				if (p1.person == null && p2.person == null) {
					return 0;
				}
				if (p1.person == null) {
					return -1;
				}
				if (p2.person == null) {
					return 1;
				}
				
				if (p1.person.getDni() == null && p2.person.getDni() == null) {
					return 0;
				}
				if (p1.person.getDni() == null) {
					return -1;
				}
				if (p2.person.getDni() == null) {
					return 1;
				}
				return p1.person.getDni().compareTo(p2.person.getDni());
			}
		});
		
		sortHandler.setComparator(hourS, new Comparator<DailyPeriodsView.PersonPeriodContainer>() {
			@Override
			public int compare(PersonPeriodContainer p1, PersonPeriodContainer p2) {
				String s1 = hourS.getValue(p1);
				String s2 = hourS.getValue(p2);
				int data = s1.compareTo(s2);
				
				if (data != 0) {
					return data;
				}
				return nameComparator.compare(p1, p2);
			}				

		});
		
		sortHandler.setComparator(hourE, new Comparator<DailyPeriodsView.PersonPeriodContainer>() {
			@Override
			public int compare(PersonPeriodContainer p1, PersonPeriodContainer p2) {
				String s1 = hourE.getValue(p1);
				String s2 = hourE.getValue(p2);
				int data = s1.compareTo(s2);

				if (data != 0) {
					return data;
				}
				return nameComparator.compare(p1, p2);
			}
		});
		
		sortHandler.setComparator(hours, new Comparator<DailyPeriodsView.PersonPeriodContainer>() {
			@Override
			public int compare(PersonPeriodContainer p1, PersonPeriodContainer p2) {
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
		
		clearStatistics();
	}
	
	@Override
	public void setPeriods(List<PersonPeriodContainer> periods) {
		if (periods == null) {
			return;
		}

		DailyPeriods.this.periods.setVisibleRange(new Range(0, periods.size()));
		periodsDataProvider.getList().clear();
		periodsDataProvider.getList().addAll(periods);
		periodsDataProvider.refresh();
		
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				ColumnSortEvent.fire( DailyPeriods.this.periods, DailyPeriods.this.periods.getColumnSortList());
			}
		});		
		
		setStatistics(periods);
	}
	
	@Override
	public void setPeriodSelectionModel(MultiSelectionModel<PersonPeriodContainer> selection) {
		periods.setSelectionModel(selection);
	}
	
		
	
	
	
	
	/////////// STATISTICS //////////////////////

	
	
	
	@UiField(provided=true) DataGrid<JustificationStatistic> statistics;
	

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
	
	
	/**
	 * Calcula la estadisticas para mostrarlas.
	 * es ineficiente porque esto ya se consulta en los cells del dataGrid.
	 * @param periods
	 */
	private void setStatistics(List<PersonPeriodContainer> periods) {
		if (periods == null) {
			return;
		}
		
		// calculo la cantidad de justificaciones para cada tipo
		JustificationStatistic absence = new JustificationStatistic("Ausencias","Períodos sin ninguna marcación");
		JustificationStatistic justifiedAbsences = new JustificationStatistic("Ausencias justificadas","Períodos sin marcaciones pero justificados");
		JustificationStatistic unjustifiedAbsences = new JustificationStatistic("Ausencias injustificadas","Períodos sin marcaciones y sin justificación");
		JustificationStatistic periodsCount = new JustificationStatistic("Períodos", "Cantidad de perídos");
		periodsCount.setCount(periods.size());
		
		Map<String,JustificationStatistic> counters = new HashMap<String,JustificationStatistic>();
		for (PersonPeriodContainer period : periods) {

			/*
			// chequeo datos de justificaciones.
			JustificationDate jd = p.justified(period);
			if (jd != null) {
				String code = jd.getJustification().getCode();
				JustificationStatistic js = counters.get(code);
				if (js == null) {
					js = new JustificationStatistic(code,jd.getJustification().getDescription());
					counters.put(code, js);
				}
				js.incrementCount();
			}
			
			// agrego datos de faltas.
			if (period.period.getWorkedHours() == null || period.period.getWorkedHours().size() <= 0) {
				absence.incrementCount();
				if (jd == null) {
					unjustifiedAbsences.incrementCount();
				} else {
					justifiedAbsences.incrementCount();
				}
			}
			*/
		}
		
		List<JustificationStatistic> lcounters = new ArrayList<JustificationStatistic>();
		lcounters.add(periodsCount);
		lcounters.add(absence);
		lcounters.add(unjustifiedAbsences);
		lcounters.add(justifiedAbsences);
		lcounters.addAll(counters.values());
		
		statistics.setRowData(lcounters);
	}		
	
	private void clearStatistics() {
		statistics.setRowCount(0);
		statistics.setRowData(new ArrayList<JustificationStatistic>());
	}	
	

	

	
	////////////////////// 	JUSTIFICATIONS /////////////
	
	@UiField(provided=true) ValueListBox<Justification> types;
	@UiField Button justify;
	@UiField TextArea justifyNotes;
	
	private SingleSelectionModel<Justification> typeSelection;
	
	@Override
	public void setJustificationSelectionModel(SingleSelectionModel<Justification> selection) {
		typeSelection = selection;
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
				justifyNotes.setText("");
				
				if (typeSelection != null) {
					Justification t = types.getValue();
					if (t != null) {
						typeSelection.setSelected(t, true);
					}
				}
			}
		});		
	}		
	
	@Override
	public void clearJustificationData() {
		justifyNotes.setText("");
	}

	@Override
	public void enableJustify(boolean t) {
		justify.setEnabled(t);
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
	public String getNotes() {
		return justifyNotes.getText();
	}
	
	@UiHandler("justify")
	public void onJustify(ClickEvent event) {
		if (p == null) {
			return;
		}
		p.justify();
	}
	

	////////////////////// EXPORT ///////////////////////////////
	
	
	
	@UiHandler("export")
	public void onExport(ClickEvent event) {
		p.export();
	}


	////////////////// DATES ////////////////////////////////////////
	
	
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


	//////////////// Find //////////////////////
	
	@UiHandler("find")
	public void onFind(ClickEvent event) {
		p.find();
	}
	
	
}
