package ar.com.dcsys.gwt.assistance.client.ui.justification.widget.justificationDateList;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

import ar.com.dcsys.data.justification.Justification;
import ar.com.dcsys.data.justification.JustificationDate;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.gwt.assistance.client.ui.cell.PersonCell;
import ar.com.dcsys.gwt.assistance.client.ui.common.AssistanceResources;
import ar.com.dcsys.gwt.assistance.client.ui.justification.widget.JustificationDateListWidget;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;

public class JustificationDateList extends Composite implements JustificationDateListWidget{

	private static JustificationDateListUiBinder uiBinder = GWT.create(JustificationDateListUiBinder.class);

	interface JustificationDateListUiBinder extends UiBinder<Widget, JustificationDateList> {	}
	
	@UiField (provided=true) DataGrid<JustificationDate> justificationDates;
	private ListDataProvider<JustificationDate> justificationsDataProvider;
	
	
	private AssistanceResources res = GWT.create(AssistanceResources.class);

	private class JustificationDateToPersonCellAdapter extends AbstractCell<JustificationDate> {
		private final PersonCell cell;
		
		public JustificationDateToPersonCellAdapter(String image) {
			cell = new PersonCell(image);
		}

		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context, JustificationDate value, SafeHtmlBuilder sb) {
			Person person = value.getPerson();
			cell.render(context, person, sb);
		}
		
	}	
	
	private void createJustificationDate() {
		final DateTimeFormat dateF = DateTimeFormat.getFormat(PredefinedFormat.DATE_SHORT);
		final DateTimeFormat timeF = DateTimeFormat.getFormat(PredefinedFormat.TIME_SHORT);
		
		//String imageHtml = AbstractImagePrototype.create(res.user()).getHTML();
		//IdentityColumn<JustificationDate> userC = new IdentityColumn<JustificationDate>(new JustificationDateToPersonCellAdapter(imageHtml));

		final TextColumn<JustificationDate> userC = new TextColumn<JustificationDate>() {
			@Override
			public String getValue(JustificationDate object) {
				if (object == null) {
					return "";
				}
				
				Person person = object.getPerson();
				if (person == null) {
					return "no tiene";
				}
				String lastName = (person.getLastName() == null) ? "" : person.getLastName();
				String name = (person.getName() == null) ? "" : person.getName();
				String personStr;
				if (lastName.trim() != "" && name.trim() != "") {
					personStr = lastName + ", " + name;
				} else {
					if (lastName.trim() == "" && name.trim() == "") {
						personStr = person.getDni();
					} else {
						personStr = lastName.trim() + name.trim();
					}
				}
				return personStr;
			}
		};		
		
		TextColumn<JustificationDate> dateS = new TextColumn<JustificationDate>() {
			@Override
			public String getValue(JustificationDate object) {
				if (object == null) {
					return "";
				}
				
				Date start = object.getStart();
				if (start == null) {
					return "no tiene";
				}
				return dateF.format(start);
			}
		};
		
		final TextColumn<JustificationDate> hourS = new TextColumn<JustificationDate>() {
			@Override
			public String getValue(JustificationDate object) {
				if (object == null) {
					return "";
				}
				
				Date start = object.getStart();
				if (start == null) {
					return "no tiene";
				}
				return timeF.format(start);
			}
		};
		
		TextColumn<JustificationDate> dateE = new TextColumn<JustificationDate>() {
			@Override
			public String getValue(JustificationDate object) {
				if (object == null) {
					return "";
				}
				
				Date end = object.getEnd();
				if (end == null) {
					return "no tiene";
				}
				return dateF.format(end);
			}
		};
		
		final TextColumn<JustificationDate> hourE = new TextColumn<JustificationDate>() {
			@Override
			public String getValue(JustificationDate object) {
				if (object == null) {
					return "";
				}
				
				Date end = object.getEnd();
				if (end == null) {
					return "no tiene";
				}
				return timeF.format(end);
			}
		};		
		
		final TextColumn<JustificationDate> type = new TextColumn<JustificationDate>() {
			@Override
			public String getValue(JustificationDate object) {
				if (object == null) {
					return "";
				}

				Justification type = object.getJustification();
				if (type == null) {
					return "no tiene";
				}
				String code = type.getCode();
				String description = type.getDescription();
				return code + ((description != null) ? "  " + description : "");
			}
		};			
		
		TextColumn<JustificationDate> notes = new TextColumn<JustificationDate>() {
			@Override
			public String getValue(JustificationDate object) {
				if (object == null) {
					return "";
				}
				
				String notes = object.getNotes();
				if (notes == null) {
					return "";
				}
				return notes;
			}
		};	
		
		justificationDates = new DataGrid<JustificationDate>();
		justificationDates.addColumn(userC,"Usuario");
	//	justificationDates.setColumnWidth(userC,10,Unit.EM);
		justificationDates.addColumn(dateS, "Fecha Inicio");
		justificationDates.addColumn(hourS, "Hora Inicio");
		justificationDates.addColumn(dateE, "Fecha Fin");
		justificationDates.addColumn(hourE, "Hora Fin");
		justificationDates.addColumn(type, "Tipo");
		justificationDates.addColumn(notes, "Notas");
		
		/////// seteo la ordenación /////////
		justificationsDataProvider = new ListDataProvider<JustificationDate>();
		justificationsDataProvider.addDataDisplay(justificationDates);
		
		ListHandler<JustificationDate> sortHandler = new ListHandler<JustificationDate>(justificationsDataProvider.getList());
		justificationDates.addColumnSortHandler(sortHandler);
		
		userC.setSortable(true);
		dateS.setSortable(true);
		hourS.setSortable(true);
		dateE.setSortable(true);
		hourE.setSortable(true);
		type.setSortable(true);
		
		justificationDates.getColumnSortList().push(userC);
		
		sortHandler.setComparator(userC, new Comparator<JustificationDate>() {
			@Override
			public int compare(JustificationDate o1, JustificationDate o2) {
				String d1 = userC.getValue(o1);
				String d2 = userC.getValue(o2);
				return d1.compareTo(d2);
			}
		});
		
		sortHandler.setComparator(dateS,new Comparator<JustificationDate>() {
			@Override
			public int compare(JustificationDate o1, JustificationDate o2) {
				if (o1 == null && o2 == null) {
					return 0;
				}
				if (o1 == null) {
					return -1;
				}
				if (o2 == null) {
					return 1;
				}
				if (o1.getStart() == null && o2.getStart() == null) {
					return 0;
				}
				if (o1.getStart() == null) {
					return -1;
				}
				if (o2.getStart() == null) {
					return 1;
				}
				return o1.getStart().compareTo(o2.getStart());
			}
		});
		
		sortHandler.setComparator(hourS, new Comparator<JustificationDate>() {
			@Override
			public int compare(JustificationDate o1, JustificationDate o2) {
				String d1 = hourS.getValue(o1);
				String d2 = hourS.getValue(o2);
				return d1.compareTo(d2);
			}
		});
		
		
		sortHandler.setComparator(dateE,new Comparator<JustificationDate>() {
			@Override
			public int compare(JustificationDate o1, JustificationDate o2) {
				if (o1 == null && o2 == null) {
					return 0;
				}
				if (o1 == null) {
					return -1;
				}
				if (o2 == null) {
					return 1;
				}
				if (o1.getEnd() == null && o2.getEnd() == null) {
					return 0;
				}
				if (o1.getEnd() == null) {
					return -1;
				}
				if (o2.getEnd() == null) {
					return 1;
				}
				return o1.getEnd().compareTo(o2.getEnd());
			}
		});		
		
		sortHandler.setComparator(hourE, new Comparator<JustificationDate>() {
			@Override
			public int compare(JustificationDate o1, JustificationDate o2) {
				String d1 = hourE.getValue(o1);
				String d2 = hourE.getValue(o2);
				return d1.compareTo(d2);
			}
		});
		
		sortHandler.setComparator(type, new Comparator<JustificationDate>() {
			@Override
			public int compare(JustificationDate o1, JustificationDate o2) {
				String d1 = type.getValue(o1);
				String d2 = type.getValue(o2);
				return d1.compareTo(d2);
			}
		});
	}
	
	public JustificationDateList () {
		createJustificationDate();
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setJustificationDates (List<JustificationDate> justificationDates) {
		JustificationDateList.this.justificationDates.setVisibleRange(0, justificationDates.size());
		justificationsDataProvider.getList().clear();
		justificationsDataProvider.getList().addAll(justificationDates);
		justificationsDataProvider.refresh();
		
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				ColumnSortEvent.fire( JustificationDateList.this.justificationDates, JustificationDateList.this.justificationDates.getColumnSortList());
			}
		});		
	}

	@Override
	public void clear() {
		justificationsDataProvider.getList().clear();
		justificationsDataProvider.refresh();
	}

	@Override
	public void setSelectionModel (MultiSelectionModel<JustificationDate> selection) {
		justificationDates.setSelectionModel(selection);
	}

}
