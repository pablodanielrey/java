package ar.com.dcsys.gwt.person.client.activity;

import java.util.List;

import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.gwt.auth.client.manager.AuthManager;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.person.client.manager.AssistancePersonData;
import ar.com.dcsys.gwt.person.client.manager.PersonsManager;
import ar.com.dcsys.gwt.person.client.ui.assistance.PersonAssistanceDataView;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;


public class PersonAssistanceDataActivity extends AbstractActivity implements PersonAssistanceDataView.Presenter {

	private final AuthManager authManager;
	private final PersonAssistanceDataView view;
	private EventBus eventBus;
	private PersonsManager personsManager;
	
	private SingleSelectionModel<Person> selection;
//	private DCSysPrincipalProxy selectedPrincipal;
	
	private boolean hasPermission = false;
	
	private final SelectionChangeEvent.Handler handler = new SelectionChangeEvent.Handler() {
		@Override
		public void onSelectionChange(SelectionChangeEvent event) {
			if (selection == null) {
				// no deberia pasar nunca ya que si no el evento no pudo haber sido disparado. pero por sanidad.
				return;
			}
			
			view.clear();
//			selectedPrincipal = null;			
			
			Person person = selection.getSelectedObject();
			if (person == null) {
				view.setReadOnly(true);
				view.setVisible(false);
				return;
			}
	
			
			view.setReadOnly((!hasPermission) || false);
			view.setVisible(hasPermission);
			findAllCredentials(person);
			findAssistancePersonData(person);
		}
	};
	

	
	@Inject
	public PersonAssistanceDataActivity(PersonsManager personsManager, AuthManager authManager, PersonAssistanceDataView view) {
		this.authManager = authManager;
		this.personsManager = personsManager;
		this.view = view;
	}
	

	private void showMessage(String message) {
		//eventBus.fireEvent(new MessageDialogEvent(message));
	}
	
	// solo ADMIN y ADMIN-ASSISTANCE pueden ver y modificar la clave del reloj de cada persona.
	private void checkPermissions() {

		hasPermission = false;
		
		/*
		
		authManager.isUserInRole("ADMIN", new Receiver<Boolean>() {
			@Override
			public void onSuccess(Boolean ok) {
				if (ok) {
					hasPermission = true;
					return;
				}
				
				authManager.isUserInRole("ADMIN-ASSISTANCE", new Receiver<Boolean>() {
					@Override
					public void onSuccess(Boolean ok) {
						if (ok) {
							hasPermission = true;
							return;
						}
					}
					@Override
					public void onFailure(Throwable error) {
						showMessage(error.getMessage());
					}
				});
				
			}
			@Override
			public void onFailure(Throwable error) {
				showMessage(error.getMessage());
			}
		});
		
		*/
		
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		this.eventBus = eventBus;
		
		checkPermissions();
		
		panel.setWidget(view);
	}
	
	@Override
	public void onStop() {
		eventBus = null;
		hasPermission = false;
		selection = null;
	}
	
	
	/**
	 * Busca todas las credenciales para una persona determinada. y setea los datos en la vista.
	 */
	private void findAllCredentials(Person person) {
		
		/*
		
		authManager.findByPerson(person, new Receiver<DCSysPrincipal>() {
			@Override
			public void onSuccess(DCSysPrincipal principal) {
				
//				selectedPrincipal = principal;
				if (principal == null) {
					return;
				}
				
				authManager.findAllCredentials(principal, new Receiver<List<DCSysCredentials>>() {
					@Override
					public void onSuccess(List<DCSysCredentials> creds) {
						setCredentials(creds);
					}

					@Override
					public void onFailure(Throwable error) {
						showMessage(error.getMessage());
					}
				});		
			};
			
			@Override
			public void onFailure(Throwable error) {
				showMessage(error.getMessage());
			}
		});
		*/
	}
	
	/**
	 * Setea en la vista el numero de pin si es que encuentra alguna credencial del tipo correcto.
	 * @param creds
	 */
	
	/*
	private void setCredentials(List<DCSysCredentials> creds) {
		if (creds == null || creds.size() <= 0) {
			return;
		}
		
		for (DCSysCredentials c : creds) {
			if (c instanceof UserPinCredentials) {
				UserPinCredentials upc = (UserPinCredentials)c;
				String pin = upc.getPassword();
				view.setPinNumber(pin);
			}
		}
	}
	*7
	
	/**
	 * Busca todos los datos de asistencia de la persona y los setea en la vista
	 * @param person
	 */
	private void findAssistancePersonData(Person person) {		
		personsManager.findAssistancePersonData(person, new Receiver<AssistancePersonData>() {
			@Override
			public void onSuccess(AssistancePersonData assistancePersonData) {
				if (view == null || assistancePersonData == null) {
					return;
				}
				view.setNotes(assistancePersonData.getNotes());
			}
			
			@Override
			public void onFailure(Throwable error) {
				showMessage("Error al obtener los datos de asistencia de la persona: " + error.getMessage());
			}
		});
	}
	
	
	@Override
	public void setSelectionModel(SingleSelectionModel<Person> selection) {
		this.selection = selection;
		selection.addSelectionChangeHandler(handler);
	}
	
	@Override
	public void persist() {
		
		if (!hasPermission) {
			return;
		}
		
		final Person person = selection.getSelectedObject();
		if (person == null) {
			return;
		}
		
		
		
		personsManager.findAssistancePersonData(person, new Receiver<AssistancePersonData>() {
			@Override
			public void onSuccess(AssistancePersonData data) {
				if (data == null) {
					//createData(person, data);
				} else {
					//updateData(person, data);
				}
			}
			
			@Override
			public void onFailure(Throwable error) {
				showMessage("Error al obtener los datos de asistencia de la persona:"+error.getMessage());
			}
		});
	}
	
	/*
	
	private void createData(PersonProxy person, AssistancePersonDataProxy data) {
		AssistancePersonDataRequest req2 = prf.personDataRequest();
		AssistancePersonDataProxy aP = req2.create(AssistancePersonDataProxy.class);
		persistAssistanceData(req2,aP,person);
	}
	
	private void updateData(PersonProxy person, AssistancePersonDataProxy data) {
		AssistancePersonDataRequest req2 = prf.personDataRequest();
		AssistancePersonDataProxy aP = req2.edit(data);
		persistAssistanceData(req2,aP,person);
	}
	
	private void persistAssistanceData(AssistancePersonDataRequest req2, AssistancePersonDataProxy data, final PersonProxy person) {
		String notes = view.getNotes();
		data.setNotes(notes);
		data.setPerson(person);
		
		req2.persist(data).fire(new Receiver<String>() {
			@Override
			public void onSuccess(String id) {
				persistPin(person);
			}
			
			@Override
			public void onFailure(ServerFailure error) {
				showMessage("Error al intentar guardar los datos de asistencia:"+error.getMessage());
			}
		});
	}
	
	
	private void persistPin(PersonProxy person) {			
		String pin = view.getPinNumber();
		if (pin == null || pin.trim().equals("")) {
			return;
		}
		
		String dni = person.getDni();
		
		if (selectedPrincipal == null) {
			showMessage("No se pudo obtener el principal de la persona " + dni);
			return;
		}	
		Context ctx = authManager.getContext();
		UserPinCredentialsProxy creds = ctx.getNewUserPinCredentials();
		creds.setDni(dni);
		creds.setPassword(pin);
		
		authManager.persist(ctx, selectedPrincipal, creds, new Receiver<Void>() {
			@Override
			public void onSuccess(Void arg0) {
				showMessage("Credenciales en el reloj actualizadas con éxito");
			}
			@Override
			public void onFailure(ServerFailure error) {
				showMessage(error.getMessage());
			}
		});		
	}
	
	*/
	
}
