package ar.com.dcsys.gwt.person.client.manager;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;

import ar.com.dcsys.data.person.MailChange;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.gwt.manager.shared.ManagerUtils;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.message.shared.Event;
import ar.com.dcsys.gwt.message.shared.Message;
import ar.com.dcsys.gwt.message.shared.MessageException;
import ar.com.dcsys.gwt.message.shared.MessageType;
import ar.com.dcsys.gwt.message.shared.MessageUtils;
import ar.com.dcsys.gwt.person.client.manager.events.MailChangeModifiedEvent;
import ar.com.dcsys.gwt.person.shared.PersonEncoderDecoder;
import ar.com.dcsys.gwt.person.shared.PersonFactory;
import ar.com.dcsys.gwt.person.shared.PersonMethods;
import ar.com.dcsys.gwt.ws.client.WebSocket;
import ar.com.dcsys.gwt.ws.client.WebSocketReceiver;
import ar.com.dcsys.gwt.ws.shared.SocketMessageEvent;
import ar.com.dcsys.gwt.ws.shared.SocketMessageEventHandler;

import com.google.gwt.event.shared.EventBus;

public class MailChangesManagerBean implements MailChangesManager {
	
	private final MessageUtils messagesFactory;
	private final EventBus eventBus;
	private final ManagerUtils managerUtils;
	private final PersonFactory personFactory;
	private final PersonEncoderDecoder personEncoderDecoder;
	private final WebSocket socket;

	private static final Logger logger = Logger.getLogger(MailChangesManagerBean.class.getName());
	
	private final SocketMessageEventHandler eventHandler = new SocketMessageEventHandler() {
		@Override
		public void onMessage(Message msg) {
			
			if (!MessageType.EVENT.equals(msg.getType())) {
				return;
			}
			
			Event event = messagesFactory.event(msg);
			if (!PersonMethods.mailChangeModifiedEvent.equals(event.getName())) {
				return;
			}
			
			try {
				eventBus.fireEvent(new MailChangeModifiedEvent());
			} catch (Exception e) {
				logger.log(Level.SEVERE,e.getMessage(),e);
			}
		}
	};	
	
	@Inject
	public MailChangesManagerBean(EventBus eventBus,
								  ManagerUtils managerUtils,
								  PersonFactory personFactory,
								  PersonEncoderDecoder personEncoderDecoder,
								  MessageUtils messagesFactory,
								  WebSocket ws) {
		this.eventBus = eventBus;
		this.managerUtils = managerUtils;
		this.personFactory = personFactory;
		this.personEncoderDecoder = personEncoderDecoder;
		this.messagesFactory = messagesFactory;
		socket = ws;
		
		eventBus.addHandler(SocketMessageEvent.TYPE, eventHandler);
	}
	
	private boolean handleError(Message response, Receiver<?> receiver) {
		if (MessageType.ERROR.equals(response.getType())) {
			String error = response.getPayload();
			receiver.onFailure(new MessageException(error));
			return true;
		}		
		return false;
	}	

	@Override
	public void persist(MailChange mailChange, Person person, final Receiver<String> receiver) {
		try {
			//serializo los parametros y genero el mensaje
			String[] params = new String[2];
			params[0] = ManagerUtils.encode(personFactory, MailChange.class, mailChange);
			params[1] = ManagerUtils.encode(personFactory, Person.class, person);
			String sparams = ManagerUtils.encodeParams(params);
			
			Message msg = messagesFactory.method(PersonMethods.persistMailChange,sparams);
			
			//envío el mensaje al servidor
			socket.open();
			socket.send(msg, new WebSocketReceiver() {
				@Override
				public void onSuccess(Message response) {
					if (handleError(response, receiver)) {
						return;
					}
					receiver.onSuccess(response.getPayload());
				}
				@Override
				public void onFailure(Throwable t) {
					receiver.onFailure(t);
				}
			});
		} catch (Exception e) {
			receiver.onFailure(e);
		}
	}

	@Override
	public void findAllBy(Person person, final Receiver<List<MailChange>> receiver) {
		try {
			
			String params = ManagerUtils.encode(personFactory, Person.class, person);
			Message msg = messagesFactory.method(PersonMethods.findAllMailChanges, params);
			
			socket.open();
			socket.send(msg, new WebSocketReceiver() {
				
				@Override
				public void onSuccess(Message message) {
					if (handleError(message, receiver)) {
						return;
					}
					
					try {
						String payload = message.getPayload();
						List<MailChange> mailChanges = personEncoderDecoder.decodeMailChangeList(payload);
						
						receiver.onSuccess(mailChanges);

					} catch (Exception e) {
						receiver.onFailure(e);
					}
				}
				
				@Override
				public void onFailure(Throwable t) {
					receiver.onFailure(t);
				}
			});
			
		} catch (Exception e) {
			receiver.onFailure(e);
		}
	}

}
