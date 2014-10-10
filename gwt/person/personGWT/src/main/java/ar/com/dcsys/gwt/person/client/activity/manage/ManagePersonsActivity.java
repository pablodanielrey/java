package ar.com.dcsys.gwt.person.client.activity.manage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.person.PersonType;
import ar.com.dcsys.data.person.PersonTypeEnum;
import ar.com.dcsys.gwt.auth.client.manager.AuthManager;
import ar.com.dcsys.gwt.clientMessages.client.MessageDialogEvent;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.person.client.activity.UpdatePersonDataActivity;
import ar.com.dcsys.gwt.person.client.manager.PersonsManager;
import ar.com.dcsys.gwt.person.client.manager.events.PersonModifiedEvent;
import ar.com.dcsys.gwt.person.client.manager.events.PersonModifiedEventHandler;
import ar.com.dcsys.gwt.person.client.place.ManagePersonsPlace;
import ar.com.dcsys.gwt.person.client.ui.AcceptsOneWidgetAdapter;
import ar.com.dcsys.gwt.person.client.ui.UpdatePersonDataView;
import ar.com.dcsys.gwt.person.client.ui.basicData.PersonDataView;
import ar.com.dcsys.gwt.person.client.ui.manage.ManagePersonsView;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class ManagePersonsActivity extends AbstractActivity implements ManagePersonsView.Presenter {

	private static Logger logger = Logger.getLogger(ManagePersonsActivity.class.getName());
	
	private final EventBus eventBus;
	private final PersonsManager personsManager;
	private final ManagePersonsView view;
	private final PersonDataView personDataView;
	private final UpdatePersonDataView updatePersonDataView;
	
	private final SingleSelectionModel<Person> selection;
	private final SingleSelectionModel<Person> personSelection;
	
	private final UpdatePersonDataActivity updatePersonDataActivity;
//	private final PersonToGroupActivity personToGroupActivity;
	
	
	/**
	 * Busca la persona seleccionada y mantiene el personSelection actualizado con la selección de la lista.
	 */
	private Handler selectionChange = new Handler() {
		@Override
		public void onSelectionChange(SelectionChangeEvent event) {
			Person person = selection.getSelectedObject();

			if (person == null) {
				personSelection.clear();
				return;
			}

			personSelection.setSelected(person, true);
			/*personsManager.findById(person.getId(), new Receiver<Person>() {
				public void onSuccess(Person person) {
					personSelection.setSelected(person, true);
				};
				@Override
				public void onError(String error) {
					logger.log(Level.SEVERE,error);
				}
			});*/
		}
	};
	
	private HandlerRegistration hr = null;
	private PersonModifiedEventHandler personModifiedHandler = new PersonModifiedEventHandler() {
		@Override
		public void onPersonModified(PersonModifiedEvent event) {
			updateUsers();
		}
	};
	
	
	private void setPersons(List<Person> persons) {
		view.setPersons(persons);
	}
 
	
	@Inject
	public ManagePersonsActivity(EventBus eventBus, 
								 PersonsManager personsManager, 
								 AuthManager authManager,
								 ManagePersonsView view, 
								 UpdatePersonDataView updatePersonDataView, 
								 PersonDataView personDataView, 
//								 PersonGroupsView personGroupsView,
//								 GroupDataView groupDataView,
								 @Assisted ManagePersonsPlace place) {
		this.personsManager = personsManager;
		this.view = view;
		this.updatePersonDataView = updatePersonDataView;
		this.personDataView = personDataView;
		this.eventBus = eventBus;
		
		personSelection = new SingleSelectionModel<Person>();

		selection = new SingleSelectionModel<Person>();
		selection.addSelectionChangeHandler(selectionChange);
		
		
		updatePersonDataActivity = new UpdatePersonDataActivity(personsManager, authManager, updatePersonDataView, personDataView, null);
		updatePersonDataActivity.setSelectionModel(personSelection);
		
		
//		personToGroupActivity = new PersonToGroupActivity(rf, personGroupsView, groupDataView);
//		personToGroupActivity.setSelectionModel(personSelection);
		
		
	}
	
	

	

	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {

		selection.clear();
		personSelection.clear();
		
		view.setPresenter(this);
		view.setSelectionModel(selection);
		view.clear();
		view.getPersonDataPanel().clear();
		

				
		
		AcceptsOneWidgetAdapter container = new AcceptsOneWidgetAdapter(view.getPersonDataPanel());
		updatePersonDataActivity.start(container, eventBus);
		
		personDataView.setDniReadOnly(false);
		personDataView.setNameReadOnly(false);
		personDataView.setLastNameReadOnly(false);
		personDataView.setMailReadOnly(false);
		personDataView.setStudentNumberReadOnly(false);
		personDataView.getPersonTypesView().setReadOnly(false);
		
		personDataView.setMailVisible(true);		
		
//		container = new AcceptsOneWidgetAdapter(view.getPersonGroupsPanel());
//		personToGroupActivity.start(container, eventBus);
		
		
		findAllPersonTypes();
		
		panel.setWidget(view);
		
		hr = eventBus.addHandler(PersonModifiedEvent.TYPE, personModifiedHandler);
	}
	
	@Override
	public void onStop() {
		
		if (hr != null) {
			hr.removeHandler();
		}
		
		selection.clear();
		personSelection.clear();
		
		view.clear();
		
		updatePersonDataActivity.onStop();
//		personToGroupActivity.onStop();
		
		
		
		
		super.onStop();
	}
	

	private void showMessage(String message) {
		eventBus.fireEvent(new MessageDialogEvent(true, message));
	}
	
	private void findAllPersonTypes() {
		List<PersonTypeEnum> types = Arrays.asList(PersonTypeEnum.values());
		view.setAllTypes(types);
	}
	
	/**
	 * Obtiene todas las personas como PersonValueProxy que tengan determinado types.
	 * @param types
	 */
	private void update(List<PersonTypeEnum> types) {
		
		
		if (types == null || types.size() <= 0 && (!view.isNoTypeSelected())) {
			// no hya nada seleccionado. busco todas las personas.
			
			personsManager.findAll(new Receiver<List<Person>>() {
				@Override
				public void onSuccess(List<Person> t) {
					if (t == null) {
						view.setPersons(new ArrayList<Person>());
					} else {
						view.setPersons(t);
					}
				}
				
				@Override
				public void onError(String error) {
					logger.log(Level.SEVERE,error);
				}
			});
			
			return;
		}
		
		if (types == null || types.size() <= 0) {
			// busco las personas que no tienen tipo.
			personsManager.findAll(types,new Receiver<List<Person>>() {
				public void onSuccess(List<Person> persons) {
					setPersons(persons);
				};
				@Override
				public void onError(String error) {
					logger.log(Level.SEVERE,error);
				}
			});
			return;
		}
		
		if (types != null && types.size() > 0) {
			// busco las personas que si tienen algun tipo.
			personsManager.findAll(types,new Receiver<List<Person>>() {
				public void onSuccess(final List<Person> persons) {
					if (view.isNoTypeSelected()) {
						// busco las personas que no tienen ningun tipo.
						personsManager.findAll(new ArrayList<PersonTypeEnum>(),new Receiver<List<Person>>() {
							public void onSuccess(List<Person> persons2) {
								
								if (persons == null) {
									setPersons(persons2);
								} else {
									if (persons2 != null) {
										persons.addAll(persons2);
									}
									setPersons(persons);
								}
							};
							@Override
							public void onError(String error) {
								logger.log(Level.SEVERE,error);
							}
						});
					} else {
						if (persons != null) {
							setPersons(persons);
						}
					}
				};
				@Override
				public void onError(String error) {
					logger.log(Level.SEVERE,error);
				}
			});
		}

		
	}
	
	@Override
	public void updateUsers() {
		selection.clear();
		personSelection.clear();
		
		List<PersonTypeEnum> types = view.getSelectedTypes();
		update(types);
	}

	
	/**
	 * Para reemplazar en la cache de las personas al objeto editado.
	 * @param p
	 */
	/*
	private void replacePerson(Person original, Person p) {
		if (original != null) {
			String id = original.getId();
			PersonValueProxy found = null;
			for (PersonValueProxy pvp : personCache) {
				if (id.equals(pvp.getId())) {
					found = pvp;
					break;
				}
			}
			if (found != null) {
				personCache.remove(found);
			}
		}
		rf.personRequest().toValue(p).fire(new Receiver<PersonValueProxy>() {
			@Override
			public void onSuccess(PersonValueProxy person) {
				personCache.add(person);
				setPersons(personCache);
				selectionChange.onSelectionChange(null);
			}
			@Override
			public void onFailure(Throwable error) {
				super.onFailure(error);
			}
		});		
	}
	*/
	
}
