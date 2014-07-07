package ar.com.dcsys.gwt.assistance.client.activity.periods;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.assistance.entities.AssistancePersonData;
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
import ar.com.dcsys.gwt.assistance.client.ui.period.PeriodsView;
import ar.com.dcsys.gwt.manager.shared.Receiver;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;

public class PeriodsActivity extends AbstractActivity implements PeriodsView.Presenter {

	public static final Logger logger = Logger.getLogger(PeriodsActivity.class.getName());
	
	private final PeriodsManager periodsManager;
	private final JustificationsManager justificationsManager;
	private final PeriodsView view;
	
	private EventBus eventBus;
	
	//person
	private final SingleSelectionModel<Person> personSelection;
	
	
	//PERIODFILTER
	private final List<PERIODFILTER> periodFilters = Arrays.asList(PERIODFILTER.values());
	private final SingleSelectionModel<PERIODFILTER> periodFilterSelectionModel;
	private final SelectionChangeEvent.Handler periodFilterHandler = new SelectionChangeEvent.Handler() {
		@Override
		public void onSelectionChange(SelectionChangeEvent event) {
			if (personSelection == null) {
				return;
			}
			Person person = personSelection.getSelectedObject();
			if (person == null) {
				return;
			}
			getPeriods(person);
		}
	};
	
	//GRUPO
	private final SingleSelectionModel<Group> groupSelection;
	
	//PERIOD
	private final MultiSelectionModel<Report> periodSelection;
	
	//Justification
	private final SingleSelectionModel<Justification> justificationSelection;
	
	@Inject
	public PeriodsActivity(PeriodsView view, PeriodsManager periodsManager, JustificationsManager justificationsManager) {
		this.periodsManager = periodsManager;
		this.view = view;
		this.justificationsManager = justificationsManager;
		
		periodFilterSelectionModel = new SingleSelectionModel<PERIODFILTER>();
		periodFilterSelectionModel.addSelectionChangeHandler(periodFilterHandler);
		periodFilterSelectionModel.setSelected(periodFilters.get(0), true);
		
		justificationSelection = new SingleSelectionModel<Justification>();
		
		groupSelection = new SingleSelectionModel<Group>();
		groupSelection.addSelectionChangeHandler(new Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				personSelection.clear();
				updatePersons();
			}
		});
		
		periodSelection = new MultiSelectionModel<Report>();
		periodSelection.addSelectionChangeHandler(new Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				Set<Report> periods = periodSelection.getSelectedSet();
				if (periods != null) {
					deselectJustified(periods);
				}
				
				periods = periodSelection.getSelectedSet();
				if (periods == null || periods.size() <= 0) {
					PeriodsActivity.this.view.enableJustify(false);
				} else {
					PeriodsActivity.this.view.enableJustify(true);					
				}
			}
		});
		
		personSelection = new SingleSelectionModel<Person>();
		personSelection.addSelectionChangeHandler(new Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				Person p = personSelection.getSelectedObject();		
				if (p == null) {
					PeriodsActivity.this.view.setEnabledFind(false);
					PeriodsActivity.this.view.enableJustify(false);
					
				} else {
					PeriodsActivity.this.view.setEnabledFind(true);
					PeriodsActivity.this.view.enableJustify(true);
				}
			}
		});
	}
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		this.eventBus = eventBus;
		view.setPresenter(this);
		
		view.clear();
		
		view.setStart(new Date());
		view.setEnd(new Date());
		
		view.setPeriodFilterValues(periodFilters);
		view.setPeriodFilterSelectionModel(periodFilterSelectionModel);
		
		view.setJustificationSelectionModel(justificationSelection);
		
		view.setPeriodSelectionModel(periodSelection);
		
		view.setGroupSelectionModel(groupSelection);
		
		view.setPersonSelectionModel(personSelection);
		
		panel.setWidget(view);
		update();
	}
	
	
	@Override
	public void onStop() {
		view.clear();
		view.setPeriodFilterSelectionModel(null);
		view.setGroupSelectionModel(null);
		view.setPersonSelectionModel(null);
		view.setJustificationSelectionModel(null);
		
		view.setPeriodSelectionModel(null);
		
		view.setPresenter(null);
		
		periodFilterSelectionModel.clear();
		groupSelection.clear();
		personSelection.clear();
		periodSelection.clear();
		justificationSelection.clear();
		
		super.onStop();
	}
	
	private void showMessage(String message) {
		//eventBus.fireEvent(new MessageDialogEvent(message));
	}

	private void update() {
		/*
		 * TODO: falta implementar en GroupsManager
		 */
		updatePersons();
		updateJustifications();
	}
	
	private void deselectJustified(Set<Report> periods) {
		for (Report r : periods) {
			if (r.getJustifications() != null && r.getJustifications().size() > 0) {
				periodSelection.setSelected(r, false);
			} 
		}
	}
	
	private void updatePersons() {
		Group g = groupSelection.getSelectedObject();
		this.periodsManager.findPersonsWithPeriodAssignation(g, new Receiver<List<Person>>() {

			@Override
			public void onSuccess(List<Person> persons) {
				view.setPersons(persons);
			}

			@Override
			public void onError(String error) {
				showMessage(error);
			}
			
		});
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
	
	@Override
	public void findPeriods() {
		Person p = personSelection.getSelectedObject();
		if (p == null) {
			return;
		}
		getPeriods(p);
	}
	
	private Date getStart() {
		Date start = view.getStart();
		if (start == null) {
			return null;
		}
		start.setHours(0);
		start.setMinutes(0);
		start.setSeconds(0);
		return start;
	}
	
	private Date getEnd() {
		Date end = view.getEnd();
		if (end == null) {
			return null;
		}
		end.setHours(23);
		end.setMinutes(59);
		end.setSeconds(59);
		return end;
	}	
	
	
	private void getPeriods(Person person) {
		Date start = getStart();
		Date end = getEnd();
		List<Person> persons = Arrays.asList(person);
		
		view.clearJustificationData();
		view.clearPeriodData();
		
		periodsManager.findAllPeriods(start, end, persons, new Receiver<ReportSummary>() {
			@Override
			public void onError(String error) {
				logger.log(Level.WARNING,error);
			}
			@Override
			public void onSuccess(ReportSummary t) {
				List<Report> reports = t.getReports();
				view.setPeriods(reports);
			}
		});
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
		
		final Person person = personSelection.getSelectedObject();
		if (person == null) {
			return;
		}
		
		Justification justification = justificationSelection.getSelectedObject();
		if (justification == null) {
			return;
		}
		
		String notes = view.getNotes();
		
		justificationsManager.justify(person, periods, justification, notes, new Receiver<Void>() {
			@Override
			public void onSuccess(Void t) {					
				 getPeriods(person);
			}
			
			@Override
			public void onError(String error) {
				logger.log(Level.SEVERE,error);
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
				logger.log(Level.SEVERE,error);
			}
		});
	}
	
	
}
