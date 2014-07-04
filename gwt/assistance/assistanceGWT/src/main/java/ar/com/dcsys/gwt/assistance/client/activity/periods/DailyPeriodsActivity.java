package ar.com.dcsys.gwt.assistance.client.activity.periods;

import java.util.ArrayList;
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
import ar.com.dcsys.gwt.assistance.client.ui.period.daily.DailyPeriodsView;
import ar.com.dcsys.gwt.manager.shared.Receiver;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;

public class DailyPeriodsActivity extends AbstractActivity implements DailyPeriodsView.Presenter {

	public static final Logger logger = Logger.getLogger(DailyPeriodsActivity.class.getName());
	
	private final PeriodsManager periodsManager;
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
	
	
	private EventBus eventBus;
	
	@Inject
	public DailyPeriodsActivity(DailyPeriodsView view, PeriodsManager periodsManager) {
		this.view = view;
		this.periodsManager = periodsManager;
		
		periodFilterSelectionModel = new SingleSelectionModel<PERIODFILTER>();
		periodFilterSelectionModel.addSelectionChangeHandler(periodFilterHandler);
		periodFilterSelectionModel.setSelected(periodFilters.get(0), true);
		
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
		
		view.setGroupSelectionModel(groupSelection);
		
		panel.setWidget(view);
		
		update();
	}
	
		
	private void update() {
		findGroups();
	}
	
	private void showMessage(String message) {
		//eventBus.fireEvent(new MessageDialogEvent(message));
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
		personsCache.clear();
		super.onStop();
	}
	
	
	@Override
	public void justify() {
		// TODO Auto-generated method stub
		
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
		end.setHours(23);
		end.setMinutes(59);
		end.setSeconds(59);
		//CalendarUtil.addDaysToDate(end, 1);
		return end;
	}
	
	@Override
	public void findPeriods() {
		Date start = getStart();
		Date end = getEnd();	
		
		periodsManager.findAllPeriods(start, end, personsCache, new Receiver<ReportSummary>() {
			@Override
			public void onSuccess(ReportSummary t) {
				List<Report> reports = t.getReports();
				view.setPeriods(reports);				
			}
			@Override
			public void onError(String error) {
				logger.log(Level.WARNING,error);
			}
		});
	}

	@Override
	public AssistancePersonData assistanceData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeJustification(JustificationDate j) {
		// TODO Auto-generated method stub
		
	}

}
