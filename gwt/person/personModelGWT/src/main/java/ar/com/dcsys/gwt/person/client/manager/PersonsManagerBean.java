package ar.com.dcsys.gwt.person.client.manager;

import java.util.List;
import java.util.logging.Logger;

import ar.com.dcsys.data.document.Document;
import ar.com.dcsys.data.person.Mail;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.person.PersonType;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.ws.client.WebSocket;

import com.google.gwt.core.shared.GWT;
import com.google.inject.Inject;

public class PersonsManagerBean implements PersonsManager {

	private static final Logger logger = Logger.getLogger(PersonsManagerBean.class.getName());
	
	private final ar.com.dcsys.gwt.person.shared.PersonsManager pm = GWT.create(ar.com.dcsys.gwt.person.shared.PersonsManager.class);
	private final WebSocket socket;
	
	/////////// reportes ///////////////////
	
	
	@Override
	public void report(final Receiver<Document> receiver) {
		pm.report(receiver);
	}
	
	
	@Override
	public void findAllReports(final Receiver<List<Document>> receiver) {
		pm.findAllReports(receiver);
	}
	
	//////////// metodos para hacer compilar el codgio pasado hasta que esten bien implementados o en el manager correcto ////////////
	
	public void findAssistancePersonData(Person p, Receiver<AssistancePersonData> rec) {
		
	};
	
	@Override
	public void findById(String id, final Receiver<Person> rec) {
		pm.findById(id,rec);
	}
	
	
	@Override
	public void addMail(Person p, Mail m, final Receiver<Void> rec) {
		pm.addMail(p, m, rec);		
	}
	
	@Override
	public void findAllTypes(final Receiver<List<PersonType>> rec) {
		pm.findAllTypes(rec);
	}
	
	
	@Override
	public void getLoggedPerson(final Receiver<Person> rec) {
		pm.getLoggedPerson(rec);
	}
	
	@Override
	public void findByDni(String dni, final Receiver<Person> rec) {
		pm.findByDni(dni, rec);
	}
	
	@Override
	public void findMails(Person p, final Receiver<List<Mail>> rec) {
		pm.findMails(p, rec);
	}
	
	
	/////////////////////////////////////////////////////////////////////////////////

	/*
	private final EventBus eventBus;
	
	private final SocketMessageEventHandler eventHandler = new SocketMessageEventHandler() {
		@Override
		public void onMessage(Message msg) {
			
			if (!MessageType.EVENT.equals(msg.getType())) {
				return;
			}
			
			Event event = messagesFactory.event(msg);
			if (!PersonMethods.personModifiedEvent.equals(event.getName())) {
				return;
			}
			
			String id = event.getParams();
			if (id == null) {
				logger.log(Level.SEVERE,"PersonModifiedEvent but id == null");
				return;
			}
			
			try {
				eventBus.fireEvent(new PersonModifiedEvent(id));
			} catch (Exception e) {
				logger.log(Level.SEVERE,e.getMessage(),e);
			}
		}
	};
	
	*/
	
	@Inject
	public PersonsManagerBean(WebSocket ws) {
		socket = ws;
		pm.setTransport(ws);
		
		//eventBus.addHandler(SocketMessageEvent.TYPE, eventHandler);
	}
	
	/*
	private boolean handleError(Message response, Receiver<?> receiver) {
		if (MessageType.ERROR.equals(response.getType())) {
			String error = response.getPayload();
			receiver.onFailure(new MessageException(error));
			return true;
		}		
		return false;
	}
	*/

	
	@Override
	public void persist(Person person, final Receiver<String> receiver) {
		pm.persist(person, receiver);
	}
	
	@Override
	public void findAll(final Receiver<List<Person>> receiver) {
		pm.findAll(receiver);
	}

	@Override
	public void findAll(List<PersonType> types, final Receiver<List<Person>> rec) {
		pm.findAll(types,rec);
	}
	
}
