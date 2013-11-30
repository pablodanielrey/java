package ar.com.dcsys.gwt.person.client.manager;

import java.util.List;

import ar.com.dcsys.gwt.message.shared.Message;
import ar.com.dcsys.gwt.message.shared.MessageException;
import ar.com.dcsys.gwt.message.shared.MessageType;
import ar.com.dcsys.gwt.message.shared.MessagesFactory;
import ar.com.dcsys.gwt.person.shared.PersonEncoderDecoder;
import ar.com.dcsys.gwt.person.shared.PersonFactory;
import ar.com.dcsys.gwt.person.shared.PersonMethods;
import ar.com.dcsys.gwt.person.shared.PersonProxy;
import ar.com.dcsys.gwt.ws.client.WebSocket;
import ar.com.dcsys.gwt.ws.client.WebSocketReceiver;

import com.google.inject.Inject;

public class PersonsManagerBean implements PersonsManager {

	private final MessagesFactory messagesFactory;
	private final PersonFactory personFactory;
	private final PersonEncoderDecoder personEncoderDecoder;
	private final WebSocket socket;
	
	
	@Inject
	public PersonsManagerBean(PersonFactory personFactory, PersonEncoderDecoder personEncoderDecoder, MessagesFactory messagesFactory, WebSocket ws) {
		this.messagesFactory = messagesFactory;
		this.personEncoderDecoder = personEncoderDecoder;
		this.personFactory = personFactory;
		socket = ws;
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
	public void persist(PersonProxy person, final Receiver<String> receiver) {
		try {
			
			// serializo los parametros y genero el mensaje
			String json = personEncoderDecoder.encode(person);
			Message msg = messagesFactory.method(PersonMethods.persist,json);
	
			// envío el mensaje al servidor.
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
	public void findAll(final Receiver<List<PersonProxy>> receiver) {
		try {
			Message msg = messagesFactory.method(PersonMethods.findAll);
			
			// envío el mensaje al servidor.
			socket.open();
			socket.send(msg, new WebSocketReceiver() {
				@Override
				public void onSuccess(Message response) {
					
					if (handleError(response, receiver)) {
						return;
					}
					
					try {
						String list = response.getPayload();
						List<PersonProxy> persons = personEncoderDecoder.decodeList(list);
						
						receiver.onSuccess(persons);
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
