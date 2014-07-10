package ar.com.dcsys.gwt.assistance.client.activity.periods;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.data.justification.Justification;
import ar.com.dcsys.data.justification.JustificationDate;
import ar.com.dcsys.data.period.Period;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.report.Report;
import ar.com.dcsys.data.report.ReportSummary;
import ar.com.dcsys.gwt.assistance.client.manager.JustificationsManager;
import ar.com.dcsys.gwt.assistance.client.manager.PeriodsManager;
import ar.com.dcsys.gwt.assistance.client.ui.period.PERIODFILTER;
import ar.com.dcsys.gwt.assistance.client.ui.period.daily.DailyPeriodsView;
import ar.com.dcsys.gwt.assistance.client.ui.period.daily.DailyPeriodsViewCss;
import ar.com.dcsys.gwt.assistance.client.ui.period.daily.DailyPeriodsViewResources;
import ar.com.dcsys.gwt.manager.shared.Receiver;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;

public class DailyPeriodsActivity extends AbstractActivity implements DailyPeriodsView.Presenter {

	public static final Logger logger = Logger.getLogger(DailyPeriodsActivity.class.getName());
	
	private final PeriodsManager periodsManager;
	private final JustificationsManager justificationsManager;
	private final DailyPeriodsView view;
	
	//PERIODFILTER
	private final List<PERIODFILTER> periodFilters = Arrays.asList(PERIODFILTER.values());
	private final SingleSelectionModel<PERIODFILTER> periodFilterSelectionModel;
	
	private final SelectionChangeEvent.Handler periodFilterHandler = new SelectionChangeEvent.Handler() {
		@Override
		public void onSelectionChange(SelectionChangeEvent event) {			
		}
	};

	//PERIODOS
	private final MultiSelectionModel<Report> periodSelection;
	private final List<Report> periodsCache = new ArrayList<Report>();

	//GRUPO
	private final SingleSelectionModel<Group> groupSelection;
	
	//PERSONS
	private final List<Person> personsCache = new ArrayList<Person>();

	//Justification
	private final SingleSelectionModel<Justification> justificationSelection;
	
	
	private EventBus eventBus;
	
	@Inject
	public DailyPeriodsActivity(DailyPeriodsView view, JustificationsManager justificationsManager, PeriodsManager periodsManager) {
		this.view = view;
		this.periodsManager = periodsManager;
		this.justificationsManager = justificationsManager;
		
		periodFilterSelectionModel = new SingleSelectionModel<PERIODFILTER>();
		periodFilterSelectionModel.addSelectionChangeHandler(periodFilterHandler);
		periodFilterSelectionModel.setSelected(periodFilters.get(0), true);
		
		justificationSelection = new SingleSelectionModel<Justification>();
		
		groupSelection = new SingleSelectionModel<Group>();
		groupSelection.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				
			}
			
		});
		
		periodSelection = new MultiSelectionModel<Report>();
		periodSelection.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				Set<Report> periods = periodSelection.getSelectedSet();
				if (periods != null) {
					deselectJustified(periods);
				}
				
				periods = periodSelection.getSelectedSet();
				if (periods == null || periods.size() <= 0) {
					DailyPeriodsActivity.this.view.enableJustify(false);
				} else {
					DailyPeriodsActivity.this.view.enableJustify(true);					
				}				
			}
			
		});
	}
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		this.eventBus = eventBus;
		view.setPresenter(this);
		
		view.clear();
		
		view.setDate(new Date());
		
		view.setPeriodFilterValues(periodFilters);
		view.setPeriodFilterSelectionModel(periodFilterSelectionModel);
		
		view.setPeriodSelectionModel(periodSelection);
		
		view.setJustificationSelectionModel(justificationSelection);
		
		view.setGroupSelectionModel(groupSelection);
		
		panel.setWidget(view);
		
		DailyPeriodsViewCss style = DailyPeriodsViewResources.INSTANCE.style();
		style.ensureInjected();
		
		update();
	}
	
		
	private void update() {
		findGroups();
		updateJustifications();
	}
	
	private void updateJustifications() {
		justificationsManager.getJustifications(new Receiver<List<Justification>>() {
			@Override
			public void onSuccess(List<Justification> justifications) {
				if (view == null || justifications == null || justifications.size() <= 0) {
					return;
				}
				view.setJustifications(justifications);
			}
			@Override
			public void onError(String error) {
				logger.log(Level.SEVERE,error);
			}
		});
	}	
	
	private void showMessage(String message) {
		//eventBus.fireEvent(new MessageDialogEvent(message));
		logger.log(Level.SEVERE,message);
	}
	
	private void deselectJustified(Set<Report> periods) {
		for (Report r : periods) {
			if (r.getJustifications() != null && r.getJustifications().size() > 0) {
				periodSelection.setSelected(r, false);
			} 
		}
	}
	
	private void findGroups() {
		/**
		 * TODO: falta implementar el GroupsManager
		 */
		Group g = groupSelection.getSelectedObject();
		updatePersons(g);
	}
	
		private void updatePersons(Group g) {
		this.periodsManager.findPersonsWithPeriodAssignation(g, new Receiver<List<Person>>() {

			@Override
			public void onSuccess(List<Person> persons) {
				personsCache.clear();
				personsCache.addAll(persons);
			}

			@Override
			public void onError(String error) {
				logger.log(Level.WARNING,error);
			}
			
		});
	}	

	@Override
	public void onStop() {
		view.setPresenter(null);
		view.clear();
		
		personsCache.clear();
		
		view.setPeriodFilterSelectionModel(null);
		view.setGroupSelectionModel(null);		
		view.setJustificationSelectionModel(null);

		periodFilterSelectionModel.clear();
		groupSelection.clear();
		periodSelection.clear();
		justificationSelection.clear();
		
		super.onStop();
	}
	

	private Date getStart() {
		Date start = view.getDate();
		if (start == null) {
			return null;
		}
		start.setHours(0);
		start.setMinutes(0);
		start.setSeconds(0);
		return start;
	}
	
	private Date getEnd() {
		Date end = getStart();
		if (end == null) {
			return null;
		}
		CalendarUtil.addDaysToDate(end, 1);
		end.setTime(end.getTime() - 1l);
		return end;
	}
	
	@Override
	public void findPeriods() {
		Date start = getStart();
		Date end = getEnd();	
		
		view.clearJustificationData();
		view.clearPeriodData();
		periodSelection.clear();
		
		PERIODFILTER periodFilter = periodFilterSelectionModel.getSelectedObject();
		if (periodFilter == null) {
			showMessage("Debe seleccionar un tipo de períodos a mostrar");
			return;			
		}
		
		Receiver<ReportSummary> rec = new Receiver<ReportSummary>() {
			@Override
			public void onSuccess(ReportSummary t) {
				List<Report> reports = t.getReports();
				view.setPeriods(reports);				
			}
			@Override
			public void onError(String error) {
				logger.log(Level.WARNING,error);
			}
		};
		
		switch (periodFilter) {
		case ALL: periodsManager.findAllPeriods(start, end, personsCache, false, rec); break;
		case WORKING: periodsManager.findAllPeriods(start, end, personsCache, true, rec); break;
		case ABSENT: periodsManager.findAllAbsences(start, end, personsCache, rec);
		default: showMessage("No se ha seleccionado el tipo de período a buscar");
	}		
	}

	
	@Override
	public void justify() {
		Set<Report> reports = periodSelection.getSelectedSet();
		if (reports == null || reports.size() <= 0) {
			return;
		}
		List<Period> periods = new ArrayList<Period>();
		for (Report r : reports) {
			if (r.getPeriod() != null) {
				periods.add(r.getPeriod());
			}
		}
		
		Justification justification = justificationSelection.getSelectedObject();
		if (justification == null) {
			return;
		}
		
		String notes = view.getNotes();
		
		justificationsManager.justify(periods, justification, notes, new Receiver<Void>() {
			@Override
			public void onSuccess(Void t) {
				findPeriods();
			}
			@Override
			public void onError(String error) {
				showMessage(error);
			}
		});
		
	}
	
	@Override
	public void removeJustification(JustificationDate j) {
		justificationsManager.remove(Arrays.asList(j), new Receiver<Void>() {
			@Override
			public void onSuccess(Void t) {
				findPeriods();
			}
			@Override
			public void onError(String error) {
				showMessage(error);
			}
		});
	}

}
