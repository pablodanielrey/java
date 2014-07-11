package ar.com.dcsys.gwt.assistance.client.activity.justification;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.data.justification.Justification;
import ar.com.dcsys.data.justification.JustificationDate;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.gwt.assistance.client.manager.JustificationsManager;
import ar.com.dcsys.gwt.assistance.client.manager.PeriodsManager;
import ar.com.dcsys.gwt.assistance.client.manager.events.JustificationModifiedEvent;
import ar.com.dcsys.gwt.assistance.client.manager.events.JustificationModifiedEventHandler;
import ar.com.dcsys.gwt.assistance.client.ui.justification.person.JustificationPersonView;
import ar.com.dcsys.gwt.clientMessages.client.MessageDialogEvent;
import ar.com.dcsys.gwt.manager.shared.Receiver;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;

public class JustificationPersonActivity extends AbstractActivity implements JustificationPersonView.Presenter {

	private final JustificationPersonView view;
	private final JustificationsManager justificationsManager;
	private final PeriodsManager periodsManager;
	private EventBus eventBus = null;
	
	private final MultiSelectionModel<Person> selection;
	private final MultiSelectionModel<JustificationDate> selectionJustificationDate;
	private final SingleSelectionModel<Justification> selectionJustification;
	
	private final List<Justification> justifications = new ArrayList<Justification>();
	private final List<JustificationDate> justificationDateList = new ArrayList<JustificationDate>();

	private Date startFind = null;
	private Date endFind = null;
	
	private HandlerRegistration hr = null;
	
	private final JustificationModifiedEventHandler handler = new JustificationModifiedEventHandler() {

		@Override
		public void onJustificationModifiedEvent(JustificationModifiedEvent event) {
			if (view == null) {
				return;
			}
			view.clearJustificationData();
			Date start = view.getStart();
			Date end = view.getEnd();

			processing = false;
			
			if (start != null && end != null) {
				findBy(start,end);
			}
			
		}
		
	};
	
	
	@Inject
	public JustificationPersonActivity(JustificationsManager justificationsManager, PeriodsManager periodsManager, JustificationPersonView view) {
		this.view = view;
		this.justificationsManager = justificationsManager;
		this.periodsManager = periodsManager;
		
		selection = new MultiSelectionModel<Person>();
		selection.addSelectionChangeHandler(new Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				Set<Person> persons = selection.getSelectedSet();
				JustificationPersonActivity.this.view.clearPersonData();
				JustificationPersonActivity.this.view.clearStatistic();
				selectionJustificationDate.clear();
				if (persons == null) {
					JustificationPersonActivity.this.view.setEnabledJustifications(false);
				} else {
					JustificationPersonActivity.this.view.setEnabledJustifications(true);
					if (justifications != null && justifications.size() > 0) {
						selectionJustification.setSelected(justifications.get(0), true);
					}
				}
			}
		});
		
		selectionJustification = new SingleSelectionModel<Justification>();
		selectionJustification.addSelectionChangeHandler(new Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {				
			}
		});
		
		selectionJustificationDate = new MultiSelectionModel<JustificationDate>();
		selectionJustificationDate.addSelectionChangeHandler(new Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				Set<JustificationDate> justificationDates = selectionJustificationDate.getSelectedSet();
				
				if (justificationDates == null || justificationDates.size() <= 0) {
					//deshabilito el boton eliminar
					JustificationPersonActivity.this.view.setEnabledRemoveButton(false);
				} else {
					//habilito el boton eliminar
					JustificationPersonActivity.this.view.setEnabledRemoveButton(true);
				}
			}
		});
	}
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		this.eventBus = eventBus;
		view.setPresenter(this);
		
		view.clear();
		view.getViewSelectionJustificationDate().setPresenter(this);
		view.setSelectionModel(selection);
		view.setJustificationDateSelectionModel(selectionJustificationDate);
		view.setTypesSelectionModel(selectionJustification);
		
		panel.setWidget(view);
		update();
		hr = eventBus.addHandler(JustificationModifiedEvent.TYPE, handler);
		
	}
	
	@Override
	public void onStop() {
		if (hr != null) {
			hr.removeHandler();
		}
		view.clear();
		view.setPresenter(null);
		startFind = null;
		endFind = null;
	}
	
	private void showMessage(String message) {
		eventBus.fireEvent(new MessageDialogEvent(message));
	}
	
	private void update() {
		updatePersons();
		updateTypes();
	}

	private void updatePersons() {
		Group g = null;
		this.periodsManager.findPersonsWithPeriodAssignation(g,new Receiver<List<Person>>() {
			@Override
			public void onSuccess(List<Person> persons) {
				if (view == null || persons == null) {
					return;
				}
				view.setPersons(persons);
			}

			@Override
			public void onError(String error) {
				showMessage(error);
			}
		});		
	}
	
	private void updateTypes() {
		this.justificationsManager.getJustifications(new Receiver<List<Justification>>() {

			@Override
			public void onSuccess(List<Justification> justifications) {
				if (view == null || justifications == null || justifications.size() < 1) {
					return;
				}
				view.setTypes(justifications);
				selectionJustification.setSelected(justifications.get(0), true);
				JustificationPersonActivity.this.justifications.clear();
				JustificationPersonActivity.this.justifications.addAll(justifications);
			}

			@Override
			public void onError(String error) {
				showMessage(error);
			}
			
		});
	}
	
	
	private final Queue<Person> personsToProcess = new LinkedList<Person>();
	private boolean processing = false;
	
	@Override
	public void persist() {
		
		if (processing) {
			showMessage("Ya existe un requerimiento en curso");
			return;
		}
		
		Set<Person> persons = selection.getSelectedSet();
		if (persons == null) {
			return;
		}
		
		/*
		 * TODO: falta implementar operationMode
		 */
		
		processing = true;
		personsToProcess.addAll(persons);
		
		createJustifications(new Receiver<Void>() {

			@Override
			public void onSuccess(Void t) {				
				showMessage("Las justificaciones se han generado correctamente");

				findBy();
				
				processing = false;
			}

			@Override
			public void onError(String error) {				
				view.clearJustificationData();
				findBy();
				processing = false;
				showMessage(error);
			}
			
		});
		
	}
	
	/**
	 * Necesario para implementar le Pattern Chain of Responsibillity.
	 * @param receiver
	 */
	private void createJustifications(final Receiver<Void> receiver) {
		if (personsToProcess.size() <= 0) {
			receiver.onSuccess(null);
		} else {
			final Person person = personsToProcess.poll();
			
			createJustitificationDate(person, new Receiver<Void>() {

				@Override
				public void onSuccess(Void response) {
					Scheduler.get().scheduleDeferred(new Command() {
						@Override
						public void execute() {							
							showMessage("Justificaciones de " + person.getDni() + " correctamente creadas");
						}
					});
					if (personsToProcess.size() <= 0) {
						receiver.onSuccess(null);
					} else {
						createJustifications(receiver);
					}
				}

				@Override
				public void onError(String error) {
					showMessage(error);
				}
				
			});
		}
	}
	
	/**
	 * Crea las justificaciones para la persona indicada y llama al Receiver pasado por parámetro.
	 * @param person
	 * @param receiver
	 */
	private void createJustitificationDate(final Person person,final Receiver<Void> receiver) {
		String notes = view.getNotes();
		Justification justification = selectionJustification.getSelectedObject();
		Date start = view.getStart();
		Date end = view.getEnd();
		
		this.justificationsManager.justify(person, start, end, justification, notes, receiver);
	}
	
	

	@Override
	public void clearPersonsSelection() {
		selection.clear();
	}

	@Override
	public void clearJustificationSelection() {
		selectionJustification.clear();
	}

	
	
	private void initTime(Date date) {
		date.setHours(0);
		date.setMinutes(0);
		date.setSeconds(0);
	}
	
	private void endTime(Date date) {
		date.setTime(date.getTime()+(24l*60l*60l*1000l));
		initTime(date);
	}
	
	private void findBy() {
		
		List<Person> persons = new ArrayList<Person>(selection.getSelectedSet());
		
		this.justificationsManager.findBy(persons, startFind, endFind, new Receiver<List<JustificationDate>>() {

			@Override
			public void onSuccess(List<JustificationDate> justificationDates) {
				if (justificationDates == null) {
					return;
				}
				justificationDateList.clear();
				view.setJustificationData(justificationDates);
				justificationDateList.addAll(justificationDates);
				generateStatistics();
			}

			@Override
			public void onError(String error) {
				showMessage(error);
			}
			
		});		
	}
	
	@Override
	public void findBy(Date start, Date end) {
		initTime(start);
		endTime(end);
		startFind = start;
		endFind = end;
		findBy();
	}

	@Override
	public void removeJustificationDates() {
		final List<JustificationDate> justificationDates = new ArrayList<JustificationDate>(selectionJustificationDate.getSelectedSet());
		selectionJustificationDate.clear();
		if (justificationDates == null || justificationDates.size() < 1) {
			return;
		}
		justificationsManager.remove(justificationDates, new Receiver<Void>() {

			@Override
			public void onSuccess(Void t) {
				showMessage("Se han eliminado correctamente las justificaciones seleccionadas");
				justificationDateList.removeAll(justificationDates);
				view.setJustificationData(justificationDateList);
				generateStatistics();
			}

			@Override
			public void onError(String error) {
				showMessage(error);
			}
			
		});
	}

	@Override
	public void generateStatistics() {
		view.clearStatistic();
		if (justificationDateList == null || justificationDateList.size() <= 0) {
			return;
		}
		Map<String, JustificationStatistic> hStatics = new HashMap<String, JustificationStatistic>();
		for (JustificationDate jd : justificationDateList) {
			String idJustification = jd.getJustification().getId();
			JustificationStatistic js = hStatics.get(idJustification);
			if (js == null) {
				js = new JustificationStatistic(jd.getJustification().getCode(), jd.getJustification().getDescription());
				hStatics.put(idJustification, js);
			}
			js.incrementCount();
		}
		List<JustificationStatistic> statics = new ArrayList<JustificationStatistic>(hStatics.values());
		view.setStatistics(statics);
	}

	
	

}
