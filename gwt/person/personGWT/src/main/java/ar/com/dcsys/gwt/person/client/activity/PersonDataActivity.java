package ar.com.dcsys.gwt.person.client.activity;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.person.PersonBean;
import ar.com.dcsys.data.person.PersonType;
import ar.com.dcsys.data.person.Telephone;
import ar.com.dcsys.data.person.TelephoneBean;
import ar.com.dcsys.gwt.auth.client.manager.AuthManager;
import ar.com.dcsys.gwt.clientMessages.client.MessageDialogEvent;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.person.client.manager.PersonsManager;
import ar.com.dcsys.gwt.person.client.place.UpdatePersonDataPlace;
import ar.com.dcsys.gwt.person.client.ui.basicData.PersonDataView;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class PersonDataActivity extends AbstractActivity implements PersonDataView.Presenter {
	
	private final PersonDataView view;
	private final PersonsManager personsManager;
	private final AuthManager authManager;
	
	private MultiSelectionModel<PersonType> typesSelection;
	private SingleSelectionModel<Person> selection;
	private EventBus eventBus = null;
	
	private final Handler changeHandler = new Handler() {
		@Override
		public void onSelectionChange(SelectionChangeEvent event) {
			
			if (selection == null) {
				return;
			}
			
			Person person = selection.getSelectedObject();
			if (person == null) {
				// no hay nadie seleccionado asi que no puedo modificar nada.
				view.clear();
				view.getPersonTypesView().clear();
				typesSelection.clear();
				return;
			}
			setPersonInView(person);
			setPersonTypesInView(person.getTypes());
		}
	};
	
	private final Handler typesHandler = new Handler() {
		@Override
		public void onSelectionChange(SelectionChangeEvent event) {
			checkStudentData();
		}
	};
	
	
	public void setSelectionModel(SingleSelectionModel<Person> selection) {
		this.selection = selection;
		selection.addSelectionChangeHandler(changeHandler);
	}
	
	
	@Inject
	public PersonDataActivity(PersonsManager personsManager, AuthManager authManager, PersonDataView view, @Assisted UpdatePersonDataPlace place) {
		this.personsManager = personsManager;
		this.authManager = authManager;
		this.view = view;
		
		typesSelection = new MultiSelectionModel<PersonType>();
		typesSelection.addSelectionChangeHandler(typesHandler);
		
		checkStudentData();
	}
	
	private void setPersonInView(Person person) {
		
		view.clear();
		
		view.setName(person.getName());
		view.setLastName(person.getLastName());
		view.setDni(person.getDni());
		view.setAddress(person.getAddress());
		view.setCity(person.getCity());
		view.setCountry(person.getCountry());
		view.setGender(person.getGender());
		
		List<Telephone> telephones = person.getTelephones();
		if (telephones != null && telephones.size() > 0) {
			for (Telephone t : telephones) {
				view.setTelephone(t.getNumber());
			}
		}

		/**
		 * TODO: Hasta que implemente el manager de studentData del lado del cliente.
		 * ahora lo pongo en false.
		 */
		view.setStudentDataVisible(false);
		view.setStudentNumberReadOnly(true);
		
		
	}
	
	
	private void showMessage(String message) {
		eventBus.fireEvent(new MessageDialogEvent(true, message));
	}
	

	private void checkStudentData() {
		view.setStudentDataVisible(false);
//		List<PersonType> types = view.getSelectedTypes();
		Set<PersonType> types = typesSelection.getSelectedSet();
		if (types == null) {
			return;
		}
		
		for (PersonType type : types) {
			if (type.equals(PersonType.STUDENT)) {
				view.setStudentDataVisible(true);
				return;
			}
		}
	}
	
	private void setPersonTypesInView(List<PersonType> types) {
		//view.setSelectedTypes(types);
		typesSelection.clear();
		
		if (types == null || types.size() <= 0) {
			return;
		}
		
		for (PersonType pt : types) {
			typesSelection.setSelected(pt,true);
		}
	}
	
	private void setAllTypesInView(List<PersonType> types) {
		view.getPersonTypesView().setAllTypes(types);
	}
	
	
	/**
	 * Determino los roles al que pertenece el usuario y seteo el css acorde.
	 */
	private void determineUserRole() {

		// valores por defecto.
		
		view.setMailReadOnly(true);
		view.setDniReadOnly(true);
		view.setNameReadOnly(true);
		view.setLastNameReadOnly(true);
		view.setStudentNumberReadOnly(true);
		view.setMailVisible(false);
		
		view.getPersonTypesView().setReadOnly(true);
		

		///////////////// PARCHE HASTA SOLUCIONAR LO DE AUTENTIFICACION /////////////
		view.setMailReadOnly(false);
		view.setDniReadOnly(false);
		view.setNameReadOnly(false);
		view.setLastNameReadOnly(false);
		view.setStudentNumberReadOnly(false);
		view.getPersonTypesView().setReadOnly(false);

		view.setMailVisible(true);

		/*
		authManager.isUserInRole("ADMIN", new Receiver<Boolean>() {
			@Override
			public void onSuccess(Boolean arg0) {
				if (arg0 != null && arg0.booleanValue() == true) {
					PersonDataViewCss style = PersonDataViewResources.INSTANCE.style(); 
					style.ensureInjected();
					
					view.setMailReadOnly(false);
					view.setDniReadOnly(false);
					view.setNameReadOnly(false);
					view.setLastNameReadOnly(false);
					view.setStudentNumberReadOnly(false);
					view.getPersonTypesView().setReadOnly(false);

					view.setMailVisible(true);
					
					
//					view.setStyle(PersonDataViewResources.INSTANCE.personDataCssAdmin());
					return;
				}
				
				authManager.isUserInRole("ADMIN-USERS", new Receiver<Boolean>() {
					@Override
					public void onSuccess(Boolean arg0) {
						if (arg0 != null && arg0.booleanValue() == true) {
							PersonDataViewCss style = PersonDataViewResources.INSTANCE.style(); 
							style.ensureInjected();

							view.setMailReadOnly(false);
							view.setDniReadOnly(false);
							view.setNameReadOnly(false);
							view.setLastNameReadOnly(false);
							view.setStudentNumberReadOnly(false);
							view.getPersonTypesView().setReadOnly(false);
							
							view.setMailVisible(true);
							
//							view.setStyle(PersonDataViewResources.INSTANCE.personDataCssAdmin());
							return;
						}
						
						authManager.isUserInRole("USER", new Receiver<Boolean>() {
							@Override
							public void onSuccess(Boolean arg0) {
								if (arg0 != null && arg0.booleanValue() == true) {
									PersonDataViewCss style = PersonDataViewResources.INSTANCE.personDataCssUser(); 
									style.ensureInjected();									
//									view.setStyle(PersonDataViewResources.INSTANCE.personDataCssUser());
									return;
								}
							}
							@Override
							public void onFailure(Throwable error) {
								
							}
						});
					}
					@Override
					public void onFailure(Throwable error) {

					}
				});
			}
			@Override
			public void onFailure(Throwable error) {
				
			}
		});
		
				*/
		
	}
	
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		
		this.eventBus = eventBus;
		
		view.setPresenter(this);
//		view.enable(false);
//		view.setAdminMode(false);
		view.clear();
		view.getPersonTypesView().setTypesSelectionModel(typesSelection);
		panel.setWidget(view);
		
		determineUserRole();
		
		personsManager.findAllTypes(new Receiver<List<PersonType>>() {
			@Override
			public void onSuccess(List<PersonType> types) {
				setAllTypesInView(types);
			}
			@Override
			public void onError(String error) {
				Window.alert(error);
			}
		});
	}
	
	@Override
	public void onStop() {
		view.setPresenter(null);
		selection = null;
		super.onStop();
	}


	@Override
	public void persist() {
		Person person = selection.getSelectedObject();
		if (person == null) {
			checkNewPerson();
		} else {
			modifyUser(person);
		}
	}
	
	private void checkNewPerson() {
		String dni = view.getDni();
		if (dni == null || dni.trim().equals("")) {
			showMessage("Debe ingresar Dni");
			return;
		}
		personsManager.findByDni(dni, new Receiver<Person>() {
			@Override
			public void onSuccess(Person arg0) {
				if (arg0 == null) {
					// ok no existe ningun usuario con ese dni asi que procedo a crearlo.
					addNewUser();
				} else {
					showMessage("Ya existe una persona con ese dni");
				}
			}
			@Override
			public void onError(String error) {
				showMessage(error);
			}
		});
	}
	
	/**
	 * Crea un usuario nuevo
	 * @param view
	 */
	private void addNewUser() {
		Person eP = new PersonBean();
		updateUser(eP,view);
	}
	
	
	/**
	 * Actualiza un usuario existente.
	 * @param person
	 */
	private void modifyUser(Person person) {
		updateUser(person,view);
	}		
	
	/**
	 * Agrego a un usuario nuevo
	 * @param view
	 */
	private void updateUser(Person eP, final PersonDataView view) {
		setData(eP, view);
		//List<PersonType> types = view.getSelectedTypes();
		Set<PersonType> stypes = typesSelection.getSelectedSet();
		List<PersonType> types = (stypes == null) ? new ArrayList<PersonType>() : new ArrayList<PersonType>(typesSelection.getSelectedSet());
		eP.setTypes(types);
		
		personsManager.persist(eP, new Receiver<String>() {
			@Override
			public void onSuccess(String id) {
				
				showMessage("Datos actualizados con éxito");
				
				/*
				// busco de nuevo para asignarle el mail.
				rf.personRequest().findById(id).with("mails","telephones","types").fire(new Receiver<Person>() {
					@Override
					public void onSuccess(Person person) {
						//String alternativeMail = view.getAlternativeMail();
						replacePerson(originalPerson,person);
						//persistMail(person, alternativeMail, view);
					}
					@Override
					public void onFailure(Throwable error) {
						super.onFailure(error);
					}
				});
				*/
			}
			@Override
			public void onError(String error) {
				showMessage("Error actualizando datos del usuario");
			}
		});
	}	
	
	
	private void setData(Person eP, PersonDataView view) {

		eP.setName(view.getName());
		eP.setLastName(view.getLastName());
		eP.setDni(view.getDni());
		eP.setAddress(view.getAddress());
		eP.setCity(view.getCity());
		eP.setCountry(view.getCountry());
		eP.setGender(view.getGender());
		
		/**
		 * TODO: Hasta que tenga el manager e studentDataManager del lado del cliente loc comento.
		 * 
		 */
		//eP.setStudentNumber(view.getStudentNumber());
		
		String tel = view.getTelephone();
		if (tel != null && (!tel.trim().equals(""))) {
			Telephone telP = new TelephoneBean();
			telP.setNumber(tel);
			eP.setTelephones(Arrays.asList(telP));
		}
	}	
	

}
