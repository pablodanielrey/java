package ar.com.dcsys.gwt.person.client.manager;

import java.util.List;
import java.util.logging.Logger;

import ar.com.dcsys.data.person.Mail;
import ar.com.dcsys.data.person.MailChange;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.ws.client.WebSocket;

import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;

public class MailChangesManagerBean implements MailChangesManager {
	
	private final WebSocket socket;
	private final ar.com.dcsys.gwt.person.shared.MailChangesManager mcm = GWT.create(ar.com.dcsys.gwt.person.shared.MailChangesManager.class);

	private static final Logger logger = Logger.getLogger(MailChangesManagerBean.class.getName());

	/*
	private final SocketMessageEventHandler eventHandler = new SocketMessageEventHandler() {
		@Override
		public void onMessage(Message msg) {
			
			if (MessageType.EVENT.equals(msg.getType())) {
				Event event = messagesFactory.event(msg);
				if (!PersonMethods.mailChangeModifiedEvent.equals(event.getName())) {
					return;
				}

				try {
					eventBus.fireEvent(new MailChangeModifiedEvent());
				} catch (Exception e) {
					logger.log(Level.SEVERE,e.getMessage(),e);
				}
				return;
			}
			
			// en caso de error refresco siempre la pantalla.
			if (MessageType.ERROR.equals(msg.getType())) {
				try {
					eventBus.fireEvent(new MailChangeModifiedEvent());
				} catch (Exception e) {
					logger.log(Level.SEVERE,e.getMessage(),e);
				}
				return;
			}
			
		}
	};
	*/	
	
	@Inject
	public MailChangesManagerBean(WebSocket ws) {
		socket = ws;
		
//		eventBus.addHandler(SocketMessageEvent.TYPE, eventHandler);
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
	public void remove(MailChange mail, Receiver<Void> receiver) {
		mcm.remove(mail, receiver);
	}
	
	@Override
	public void persist(MailChange mailChange, Person person, Receiver<Void> receiver) {
		mcm.persist(mailChange, person, receiver);
	}

	@Override
	public void findAllBy(Person person, Receiver<List<MailChange>> receiver) {
		mcm.findAllBy(person, receiver);
	}
	
	
	@Override
	public void addMail(String personId, Mail m, Receiver<Void> rec) {
		mcm.addMail(personId, m, rec);
	}
	
	@Override
	public void addMails(String personId, List<Mail> mails, Receiver<Void> rec) {
		mcm.addMails(personId, mails, rec);
	}
	
	@Override
	public void findMails(Person p, Receiver<List<Mail>> rec) {
		mcm.findMails(p, rec);
	}
	
	@Override
	public void removeMail(String personId, Mail m, Receiver<Void> rec) {
		mcm.removeMail(personId,m, rec);
	}
	
	@Override
	public void removeMails(String personId, List<Mail> mails,Receiver<Void> rec) {
		mcm.removeMails(personId, mails, rec);
	}

}
