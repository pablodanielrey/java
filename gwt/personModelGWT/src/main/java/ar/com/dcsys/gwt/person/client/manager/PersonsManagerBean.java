package ar.com.dcsys.gwt.person.client.manager;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.person.Mail;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.person.PersonType;
import ar.com.dcsys.gwt.message.shared.Message;
import ar.com.dcsys.gwt.message.shared.MessageException;
import ar.com.dcsys.gwt.message.shared.MessageType;
import ar.com.dcsys.gwt.message.shared.MessageUtils;
import ar.com.dcsys.gwt.person.shared.PersonEncoderDecoder;
import ar.com.dcsys.gwt.person.shared.PersonFactory;
import ar.com.dcsys.gwt.person.shared.PersonMethods;
import ar.com.dcsys.gwt.person.shared.PersonValueProxy;
import ar.com.dcsys.gwt.person.shared.lists.PersonTypeList;
import ar.com.dcsys.gwt.utils.client.EncoderDecoder;
import ar.com.dcsys.gwt.ws.client.WebSocket;
import ar.com.dcsys.gwt.ws.client.WebSocketReceiver;

import com.google.inject.Inject;

public class PersonsManagerBean implements PersonsManager {

	private static final Logger logger = Logger.getLogger(PersonsManagerBean.class.getName());
	
	private final PersonFactory personFactory;
	private final MessageUtils messagesFactory;
	private final PersonEncoderDecoder personEncoderDecoder;
	private final WebSocket socket;
	
	
	
	//////////// metodos para hacer compilar el codgio pasado hasta que esten bien implementados o en el manager correcto ////////////
	
	public void findAssistancePersonData(Person p, Receiver<AssistancePersonData> rec) {
		
	};
	
	@Override
	public void findById(String id, final Receiver<Person> rec) {
		try {
			Message msg = messagesFactory.method(PersonMethods.findById,id);
			
			// envío el mensaje al servidor.
			socket.open();
			socket.send(msg, new WebSocketReceiver() {
				@Override
				public void onSuccess(Message response) {
					
					if (handleError(response, rec)) {
						return;
					}
					
					Person person = null;
					try {
						String personS = response.getPayload();
						if (personS != null) {
							person = personEncoderDecoder.decodePerson(personS);
						}
					} catch (Exception e) {
						rec.onFailure(e);
					}
					
					try {
						rec.onSuccess(person);
					} catch (Exception e) {
						logger.log(Level.SEVERE,e.getMessage(),e);
					}
				}
				@Override
				public void onFailure(Throwable t) {
					rec.onFailure(t);
				}
			});
		} catch (Exception e) {
			rec.onFailure(e);
		}		
	}
	
	
	@Override
	public void addMail(Person p, Mail m, final Receiver<Void> rec) {
		try {
			// codifico los parámetros en base64 tambien ya que no tiene el caracter * que lo uso como separador.
			StringBuilder sb = new StringBuilder();
			sb.append(EncoderDecoder.b64encode(personEncoderDecoder.encodePerson(p)));
			sb.append("*");
			sb.append(EncoderDecoder.b64encode(personEncoderDecoder.encodeMail(m)));
			
			Message msg = messagesFactory.method(PersonMethods.addMail, sb.toString());
			
			// envío el mensaje al servidor.
			socket.open();
			socket.send(msg, new WebSocketReceiver() {
				@Override
				public void onSuccess(Message response) {
					if (handleError(response, rec)) {
						return;
					}
					rec.onSuccess(null);
				}
				@Override
				public void onFailure(Throwable t) {
					rec.onFailure(t);
				}
			});
			
		} catch (Exception e) {
			rec.onFailure(e);
		}		
	}
	
	@Override
	public void findAllTypes(final Receiver<List<PersonType>> rec) {
		try {
			Message msg = messagesFactory.method(PersonMethods.findTypes);
			
			// envío el mensaje al servidor.
			socket.open();
			socket.send(msg, new WebSocketReceiver() {
				@Override
				public void onSuccess(Message response) {
					if (handleError(response, rec)) {
						return;
					}
					
					List<PersonType> types = null;
					try {
						String list = response.getPayload();
						types = personEncoderDecoder.decodeTypeList(list);
					} catch (Exception e) {
						rec.onFailure(e);
					}
					
					try {
						rec.onSuccess(types);
					} catch (Exception e) {
						logger.log(Level.SEVERE,e.getMessage(),e);
					}
				}
				@Override
				public void onFailure(Throwable t) {
					rec.onFailure(t);
				}
			});
			
		} catch (Exception e) {
			rec.onFailure(e);
		}		
	}
	
	@Override
	public void findByDni(String dni, final Receiver<Person> rec) {
		try {
			Message msg = messagesFactory.method(PersonMethods.findByDni,dni);
			
			// envío el mensaje al servidor.
			socket.open();
			socket.send(msg, new WebSocketReceiver() {
				@Override
				public void onSuccess(Message response) {
					
					if (handleError(response, rec)) {
						return;
					}
					
					Person person = null;
					try {
						String personS = response.getPayload();
						if (personS != null) {
							person = personEncoderDecoder.decodePerson(personS);
						}
					} catch (Exception e) {
						rec.onFailure(e);
					}
					
					try {
						rec.onSuccess(person);
					} catch (Exception e) {
						logger.log(Level.SEVERE,e.getMessage(),e);
					}
				}
				@Override
				public void onFailure(Throwable t) {
					rec.onFailure(t);
				}
			});
		} catch (Exception e) {
			rec.onFailure(e);
		}
	}
	
	@Override
	public void findMails(Person p, final Receiver<List<Mail>> rec) {
		try {
			
			String json = personEncoderDecoder.encodePerson(p);
			Message msg = messagesFactory.method(PersonMethods.findAllMails, json);
			
			// envío el mensaje al servidor.
			socket.open();
			socket.send(msg, new WebSocketReceiver() {
				@Override
				public void onSuccess(Message response) {
					if (handleError(response, rec)) {
						return;
					}
					
					List<Mail> mails = null;
					try {
						String list = response.getPayload();
						mails = personEncoderDecoder.decodeMailList(list);
					} catch (Exception e) {
						rec.onFailure(e);
					}
					
					try {
						rec.onSuccess(mails);
					} catch (Exception e) {
						logger.log(Level.SEVERE,e.getMessage(),e);
					}
				}
				@Override
				public void onFailure(Throwable t) {
					rec.onFailure(t);
				}
			});
			
		} catch (Exception e) {
			rec.onFailure(e);
		}		
	}
	
	
	//////////////////////////////////////////////////////////////////////////////////
	
	
	
	
	@Inject
	public PersonsManagerBean(PersonFactory personFactory, PersonEncoderDecoder personEncoderDecoder, MessageUtils messagesFactory, WebSocket ws) {
		this.personFactory = personFactory;
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
			String json = personEncoderDecoder.encodePerson(person);
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
						persons = personEncoderDecoder.decodePersonList(list);
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

	@Override
	public void findAllPersonValue(final Receiver<List<PersonValueProxy>> rec) {
		try {
			Message msg = messagesFactory.method(PersonMethods.findAllValues);
			
			// envío el mensaje al servidor.
			socket.open();
			socket.send(msg, new WebSocketReceiver() {
				@Override
				public void onSuccess(Message response) {
					
					if (handleError(response, rec)) {
						return;
					}
					
					List<PersonValueProxy> persons = null;
					try {
						String list = response.getPayload();
						persons = personEncoderDecoder.decodePersonValueList(list);
					} catch (Exception e) {
						rec.onFailure(e);
					}
					
					try {
						rec.onSuccess(persons);
					} catch (Exception e) {
						logger.log(Level.SEVERE,e.getMessage(),e);
					}
				}
				@Override
				public void onFailure(Throwable t) {
					rec.onFailure(t);
				}
			});
		} catch (Exception e) {
			rec.onFailure(e);
		}		
	}
	
	@Override
	public void findAllPersonValue(List<PersonType> types, final Receiver<List<PersonValueProxy>> rec) {
		try {
			
			String params = personEncoderDecoder.encodeTypeList(types);
			Message msg = messagesFactory.method(PersonMethods.findAllValuesByType,params);
			
			// envío el mensaje al servidor.
			socket.open();
			socket.send(msg, new WebSocketReceiver() {
				@Override
				public void onSuccess(Message response) {
					
					if (handleError(response, rec)) {
						return;
					}
					
					List<PersonValueProxy> persons = null;
					try {
						String list = response.getPayload();
						persons = personEncoderDecoder.decodePersonValueList(list);
					} catch (Exception e) {
						rec.onFailure(e);
					}
					
					try {
						rec.onSuccess(persons);
					} catch (Exception e) {
						logger.log(Level.SEVERE,e.getMessage(),e);
					}
				}
				@Override
				public void onFailure(Throwable t) {
					rec.onFailure(t);
				}
			});
		} catch (Exception e) {
			rec.onFailure(e);
		}		
	}
	
}
