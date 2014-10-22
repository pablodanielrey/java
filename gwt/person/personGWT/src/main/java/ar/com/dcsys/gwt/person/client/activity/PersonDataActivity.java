package ar.com.dcsys.gwt.person.client.activity;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ar.com.dcsys.data.person.Mail;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.person.PersonType;
import ar.com.dcsys.data.person.PersonTypeEnum;
import ar.com.dcsys.data.person.PersonTypeExternal;
import ar.com.dcsys.data.person.PersonTypePersonal;
import ar.com.dcsys.data.person.PersonTypePostgraduate;
import ar.com.dcsys.data.person.PersonTypeStudent;
import ar.com.dcsys.data.person.PersonTypeTeacher;
import ar.com.dcsys.data.person.Telephone;
import ar.com.dcsys.gwt.auth.client.manager.AuthManager;
import ar.com.dcsys.gwt.clientMessages.client.MessageDialogEvent;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.person.client.manager.MailChangesManager;
import ar.com.dcsys.gwt.person.client.manager.PersonsManager;
import ar.com.dcsys.gwt.person.client.place.UpdatePersonDataPlace;
import ar.com.dcsys.gwt.person.client.ui.basicData.PersonDataView;
import ar.com.dcsys.gwt.person.client.ui.basicData.PersonDataViewCss;
import ar.com.dcsys.gwt.person.client.ui.basicData.PersonDataViewResources;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
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
	private final MailChangesManager mailsManager;
	private final AuthManager authManager;
	
	private MultiSelectionModel<PersonTypeEnum> typesSelection;
	private SingleSelectionModel<Person> selection;
	private EventBus eventBus = null;
		
	private Map<Mail,String> mailsCache = new HashMap<Mail,String>();
	
	private final Handler changeHandler = new Handler() {
		@Override
		public void onSelectionChange(SelectionChangeEvent event) {

			view.clear();
			
			if (selection == null) {
				return;
			}

			
			Person person = selection.getSelectedObject();
			if (person == null) {
				// no hay nadie seleccionado asi que no puedo modificar nada.
				view.getPersonTypesView().clear();
				typesSelection.clear();
				return;
			}			
			setPersonInView(person);
			setPersonTypesInView(person.getTypes());
			setMailInView(person);
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
	public PersonDataActivity(PersonsManager personsManager, MailChangesManager mailsManager, AuthManager authManager, PersonDataView view, @Assisted UpdatePersonDataPlace place) {
		this.personsManager = personsManager;
		this.authManager = authManager;
		this.mailsManager = mailsManager;
		this.view = view;		
		
		typesSelection = new MultiSelectionModel<PersonTypeEnum>();
		typesSelection.addSelectionChangeHandler(typesHandler);
		
		checkStudentData();
	}
	
	private void setMailInView(Person person) {
		this.mailsCache.clear();
		this.mailsDeleted.clear();
		this.mailsManager.findMails(person, new Receiver<List<Mail>>() {
			@Override
			public void onSuccess(List<Mail> mails) {
				mailsCache.clear();
				if (mails == null) {
					mails = new ArrayList<Mail>();
				}
				
				for (Mail m : mails) {
					mailsCache.put(m,m.getMail());
				}
				view.setMails(mails);
								
 			}
			@Override
			public void onError(String error) {
				showMessage(error);
			}

		});
	}
	
	private void setPersonInView(Person person) {
				
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

		view.setStudentDataVisible(false);
		
		
	}
	
	
	private void showMessage(String message) {
		eventBus.fireEvent(new MessageDialogEvent(true, message));
	}
	

	private void checkStudentData() {
		view.setStudentDataVisible(false);
		Set<PersonTypeEnum> types = typesSelection.getSelectedSet();
		if (types == null) {
			return;
		}
		
		for (PersonTypeEnum type : types) {
			if (type == PersonTypeEnum.STUDENT) {
				view.setStudentDataVisible(true);				
				return;
			}
		}
	}
	
	private void setPersonTypesInView(List<PersonType> types) {
		
		typesSelection.clear();
		
		if (types == null || types.size() <= 0) {
			return;
		}
		
		for (PersonType pt : types) {
			
			if (PersonTypeEnum.EXTERNAL.getClazz().equals(pt.getClass().getName())) {
				typesSelection.setSelected(PersonTypeEnum.EXTERNAL,true);
			}
			
			else if (PersonTypeEnum.PERSONAL.getClazz().equals(pt.getClass().getName())) {
				typesSelection.setSelected(PersonTypeEnum.PERSONAL,true);
			}
			
			else if (PersonTypeEnum.POSTGRADUATE.getClazz().equals(pt.getClass().getName())) {
				typesSelection.setSelected(PersonTypeEnum.POSTGRADUATE,true);
			}
			
			else if (PersonTypeEnum.STUDENT.getClazz().equals(pt.getClass().getName())) {
				typesSelection.setSelected(PersonTypeEnum.STUDENT,true);
				String studentNumber = ((PersonTypeStudent)pt).getStudentNumber();
				view.setStudentNumber(studentNumber);
			}
			
			else if (PersonTypeEnum.TEACHER.getClazz().equals(pt.getClass().getName())) {
				typesSelection.setSelected(PersonTypeEnum.TEACHER,true);
			}
			
		}
	}
	
	private void setAllTypesInView(List<PersonTypeEnum> types) {
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
	/*	view.setMailReadOnly(false);
		view.setDniReadOnly(false);
		view.setNameReadOnly(false);
		view.setLastNameReadOnly(false);
		view.setStudentNumberReadOnly(false);
		view.getPersonTypesView().setReadOnly(false);

		view.setMailVisible(true);*/

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
		
		PersonDataViewCss style = PersonDataViewResources.INSTANCE.style();
		style.ensureInjected();
		
		List<PersonTypeEnum> types = Arrays.asList(PersonTypeEnum.values());
		setAllTypesInView(types);
		
	}
	
	@Override
	public void onStop() {
		view.setPresenter(null);
		selection = null;
		mailsCache.clear();
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
		Person eP = new Person();
		updateUser(eP,view);
	}
	
	
	/**
	 * Actualiza un usuario existente.
	 * @param person
	 */
	private void modifyUser(Person person) {
		updateUser(person,view);
	}		
	
		
	private PersonType convert(PersonTypeEnum type) {
		PersonType t = null;
		switch (type) {
			case EXTERNAL: t = new PersonTypeExternal(); break;
			case PERSONAL : t = new PersonTypePersonal(); break;
			case POSTGRADUATE: t = new PersonTypePostgraduate(); break;
			case STUDENT: t = new PersonTypeStudent(); ((PersonTypeStudent)t).setStudentNumber(view.getStudentNumber());break;
			case TEACHER: t = new PersonTypeTeacher(); break;
		}
		return t;
	}
	
	private List<PersonType> convert(List<PersonTypeEnum> typesEnum) {
		List<PersonType> types = new ArrayList<PersonType>();
		for (PersonTypeEnum t : typesEnum) {
			types.add(convert(t));
		}
		return types;
	}
	
	/**
	 * Agrego a un usuario nuevo
	 * @param view
	 */
	private void updateUser(Person eP, final PersonDataView view) {
		setData(eP, view);
		Set<PersonTypeEnum> stypes = typesSelection.getSelectedSet();
		List<PersonTypeEnum> types = (stypes == null) ? new ArrayList<PersonTypeEnum>() : new ArrayList<PersonTypeEnum>(typesSelection.getSelectedSet());
		
		
		eP.setTypes(convert(types));
		
		personsManager.persist(eP, new Receiver<String>() {
			@Override
			public void onSuccess(String id) {
				
				persistMail(id);
				showMessage("Datos actualizados con éxito");
								
			}
			@Override
			public void onError(String error) {
				showMessage("Error actualizando datos del usuario");
			}
		});
	}	
	
	private List<Mail> mailsDeleted = new ArrayList<Mail>();
	@Override
	public void remove(Mail m) {
		if (m == null) {
			return;
		}	
		
		String mStr = this.mailsCache.get(m);
		if (!mStr.equals("new")) {
			m.setMail(mStr);
			mailsDeleted.add(m);
		}
		
		this.mailsCache.remove(m);
		
		view.setMails(new ArrayList<Mail>(mailsCache.keySet()));
	}
	
	@Override
	public void update(Mail m, String value) {
		if (value == null || value.trim().equals("")) {
			return;
		}
		String val = value.trim().toLowerCase();
		
		for (Mail mail : mailsCache.keySet()) {
			if (mail.getMail().equals(val)) {
				return;
			}
		}
		
		if (m == null) {
			m = new Mail();
			mailsCache.put(m,"new");
		}
		
		m.setMail(val);

		view.setMails(new ArrayList<Mail>(mailsCache.keySet()));
	}
	
	private void persistMail(final String idPerson) {
		
		for (Mail m : mailsCache.keySet()) {
			if (!mailsCache.get(m).equals("new")) {
				Mail mOld = new Mail();
				mOld.setMail(mailsCache.get(m));
				mailsDeleted.add(mOld);
			}
		}
		
		if (mailsDeleted.size() <= 0) {
			addMails(idPerson);
			return;
		}
		
		mailsManager.removeMails(idPerson, mailsDeleted,new Receiver<Void>() {
			@Override
			public void onSuccess(Void t) {
				addMails(idPerson);
			}
			
			@Override
			public void onError(String error) {
				showMessage(error);
			}
		});
	}
	
	private void addMails(String personId) {
		if (mailsCache == null || mailsCache.size() <= 0) {
			return;
		}
		
		mailsManager.addMails(personId, new ArrayList<Mail>(mailsCache.keySet()), new Receiver<Void>() {
			@Override
			public void onSuccess(Void t) {
				mailsDeleted.clear();
			}
			
			@Override
			public void onError(String error) {
				mailsDeleted.clear();
				showMessage(error);
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
		
		String tel = view.getTelephone();
		if (tel != null && (!tel.trim().equals(""))) {
			Telephone telP = new Telephone();
			telP.setNumber(tel);
			eP.setTelephones(Arrays.asList(telP));
		}
	}	
	

}
