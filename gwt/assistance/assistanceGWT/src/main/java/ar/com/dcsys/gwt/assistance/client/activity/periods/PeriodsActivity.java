package ar.com.dcsys.gwt.assistance.client.activity.periods;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.assistance.entities.AssistancePersonData;
import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.data.justification.JustificationDate;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.report.Report;
import ar.com.dcsys.data.report.ReportSummary;
import ar.com.dcsys.gwt.assistance.client.manager.PeriodsManager;
import ar.com.dcsys.gwt.assistance.client.ui.period.PERIODFILTER;
import ar.com.dcsys.gwt.assistance.client.ui.period.PeriodsView;
import ar.com.dcsys.gwt.manager.shared.Receiver;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;

public class PeriodsActivity extends AbstractActivity implements PeriodsView.Presenter {

	public static final Logger logger = Logger.getLogger(PeriodsActivity.class.getName());
	
	private final PeriodsManager periodsManager;
	private final PeriodsView view;
	
	private EventBus eventBus;
	
	//person
	private final SingleSelectionModel<Person> personSelection;
	private AssistancePersonData personDataCache;
	
	
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
	
	@Inject
	public PeriodsActivity(PeriodsView view, PeriodsManager periodsManager) {
		this.periodsManager = periodsManager;
		this.view = view;
		
		periodFilterSelectionModel = new SingleSelectionModel<PERIODFILTER>();
		periodFilterSelectionModel.addSelectionChangeHandler(periodFilterHandler);
		periodFilterSelectionModel.setSelected(periodFilters.get(0), true);
		
		groupSelection = new SingleSelectionModel<Group>();
		groupSelection.addSelectionChangeHandler(new Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				personSelection.clear();
				updatePersons();
			}
		});
		
		personSelection = new SingleSelectionModel<Person>();
		personSelection.addSelectionChangeHandler(new Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				Person p = personSelection.getSelectedObject();
				//periodSelection.clear();
				//PeriodsActivity.this.viewClearPeriodData();
				//PeriodsActivity.this.view.clearJustificationData();
				personDataCache = null;
				if (p == null) {
					return;
				}
				getPeriods(p);
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
		
		view.setPresenter(null);
		
		periodFilterSelectionModel.clear();
		groupSelection.clear();
		personSelection.clear();
		
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
	
	@Override
	public void dateChanged() {
		// TODO Auto-generated method stub
		
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
	public AssistancePersonData assistanceData() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void justify() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void removeJustification(JustificationDate j) {
		// TODO Auto-generated method stub
		
	}
	
	
}
