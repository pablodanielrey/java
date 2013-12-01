package ar.com.dcsys.gwt.person.client.manager;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.gwt.message.shared.Message;
import ar.com.dcsys.gwt.message.shared.MessageException;
import ar.com.dcsys.gwt.message.shared.MessageType;
import ar.com.dcsys.gwt.message.shared.MessageUtils;
import ar.com.dcsys.gwt.person.shared.PersonEncoderDecoder;
import ar.com.dcsys.gwt.person.shared.PersonMethods;
import ar.com.dcsys.gwt.ws.client.WebSocket;
import ar.com.dcsys.gwt.ws.client.WebSocketReceiver;

import com.google.inject.Inject;

public class PersonsManagerBean implements PersonsManager {

	private static final Logger logger = Logger.getLogger(PersonsManagerBean.class.getName());
	
	private final MessageUtils messagesFactory;
	private final PersonEncoderDecoder personEncoderDecoder;
	private final WebSocket socket;
	
	
	@Inject
	public PersonsManagerBean(PersonEncoderDecoder personEncoderDecoder, MessageUtils messagesFactory, WebSocket ws) {
		this.messagesFactory = messagesFactory;
		this.personEncoderDecoder = personEncoderDecoder;
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
	public void persist(Person person, final Receiver<String> receiver) {
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
	public void findAll(final Receiver<List<Person>> receiver) {
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
					
					List<Person> persons = null;
					try {
						String list = response.getPayload();
						persons = personEncoderDecoder.decodeList(list);
					} catch (Exception e) {
						receiver.onFailure(e);
					}
					
					try {
						receiver.onSuccess(persons);
					} catch (Exception e) {
						logger.log(Level.SEVERE,e.getMessage(),e);
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
