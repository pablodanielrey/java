package ar.com.dcsys.gwt.person.client.manager;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.document.Document;
import ar.com.dcsys.data.person.Mail;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.person.PersonType;
import ar.com.dcsys.gwt.manager.client.ManagerUtils;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.message.shared.Event;
import ar.com.dcsys.gwt.message.shared.Message;
import ar.com.dcsys.gwt.message.shared.MessageException;
import ar.com.dcsys.gwt.message.shared.MessageType;
import ar.com.dcsys.gwt.message.shared.MessageUtils;
import ar.com.dcsys.gwt.person.client.manager.events.PersonModifiedEvent;
import ar.com.dcsys.gwt.person.shared.DocumentEncoderDecoder;
import ar.com.dcsys.gwt.person.shared.DocumentFactory;
import ar.com.dcsys.gwt.person.shared.PersonEncoderDecoder;
import ar.com.dcsys.gwt.person.shared.PersonFactory;
import ar.com.dcsys.gwt.person.shared.PersonMethods;
import ar.com.dcsys.gwt.person.shared.PersonValueProxy;
import ar.com.dcsys.gwt.ws.client.WebSocket;
import ar.com.dcsys.gwt.ws.client.WebSocketReceiver;
import ar.com.dcsys.gwt.ws.shared.SocketMessageEvent;
import ar.com.dcsys.gwt.ws.shared.SocketMessageEventHandler;

import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;

public class PersonsManagerBean implements PersonsManager {

	private static final Logger logger = Logger.getLogger(PersonsManagerBean.class.getName());
	
	private final ManagerUtils managerUtils;
	private final PersonFactory personFactory;
	private final MessageUtils messagesFactory;
	private final PersonEncoderDecoder personEncoderDecoder;
	private final DocumentEncoderDecoder documentEncoderDecoder;
	private final DocumentFactory documentFactory;
	private final WebSocket socket;
	
	
	
	
	/////////// reportes ///////////////////
	
	
	@Override
	public void report(final Receiver<Document> receiver) {
		try {
			// serializo los parametros y genero el mensaje
			Message msg = messagesFactory.method(PersonMethods.reportPersonsData);
	
			// envío el mensaje al servidor.
			socket.open();
			socket.send(msg, new WebSocketReceiver() {
				@Override
				public void onSuccess(Message response) {
					if (handleError(response, receiver)) {
						return;
					}
					
					String payload = response.getPayload();
					Document d = documentEncoderDecoder.decode(payload);
					
					receiver.onSuccess(d);
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
	public void findAllReports(final Receiver<List<Document>> receiver) {
		try {
			// serializo los parametros y genero el mensaje
			Message msg = messagesFactory.method(PersonMethods.findPersonReportsData);
	
			// envío el mensaje al servidor.
			socket.open();
			socket.send(msg, new WebSocketReceiver() {
				@Override
				public void onSuccess(Message response) {
					if (handleError(response, receiver)) {
						return;
					}
					
					String sreports = response.getPayload();
					List<Document> reports = documentEncoderDecoder.decodeDocumentList(sreports);
					
					receiver.onSuccess(reports);
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
							person = ManagerUtils.decode(personFactory,Person.class,personS);
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
			String[] params = new String[2];
			params[0] = ManagerUtils.encode(personFactory, Person.class, p);
			params[1] = ManagerUtils.encode(personFactory, Mail.class, m);
			String sparams = ManagerUtils.encodeParams(params);
			
			Message msg = messagesFactory.method(PersonMethods.addMail,sparams);
			
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
	public void getLoggedPerson(final Receiver<Person> rec) {
		try {
			Message msg = messagesFactory.method(PersonMethods.getLoggedPerson);
			
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
							person = ManagerUtils.decode(personFactory,Person.class,personS);
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
							person = ManagerUtils.decode(personFactory,Person.class,personS);
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
			
			String json = ManagerUtils.encode(personFactory,Person.class,p);
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
	
	
	/////////////////////////////////////////////////////////////////////////////////
	
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
	
	
	@Inject
	public PersonsManagerBean(EventBus eventBus, 
							  ManagerUtils managerUtils,
							  PersonFactory personFactory, PersonEncoderDecoder personEncoderDecoder,
							  DocumentFactory documenFactory, DocumentEncoderDecoder documentEncoderDecoder,
							  MessageUtils messagesFactory, 
							  WebSocket ws) {
		this.eventBus = eventBus;
		this.managerUtils = managerUtils;
		this.personFactory = personFactory;
		this.messagesFactory = messagesFactory;
		this.personEncoderDecoder = personEncoderDecoder;
		this.documentFactory = documenFactory;
		this.documentEncoderDecoder = documentEncoderDecoder;
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
	public void persist(Person person, final Receiver<String> receiver) {
		try {
			
			// serializo los parametros y genero el mensaje
			String json = ManagerUtils.encode(personFactory,Person.class,person);
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

			// params == null quiere decir que se buscan las personas que no tienen ningun tipo asociado.
			Message msg = null;
			if (types != null) {
				String params = personEncoderDecoder.encodeTypeList(types);
				msg = messagesFactory.method(PersonMethods.findAllValuesByType,params);
			} else {
				msg = messagesFactory.method(PersonMethods.findAllValuesByType);
			}
			
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
