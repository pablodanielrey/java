package ar.com.dcsys.gwt.assistance.client.manager;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.justification.GeneralJustificationDate;
import ar.com.dcsys.data.justification.Justification;
import ar.com.dcsys.data.justification.JustificationDate;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.gwt.assistance.client.manager.events.JustificationModifiedEvent;
import ar.com.dcsys.gwt.assistance.shared.AssistanceEncoderDecoder;
import ar.com.dcsys.gwt.assistance.shared.AssistanceFactory;
import ar.com.dcsys.gwt.assistance.shared.AssistanceMethods;
import ar.com.dcsys.gwt.assistance.shared.lists.GeneralJustificationDateList;
import ar.com.dcsys.gwt.manager.client.ManagerUtils;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.message.shared.Event;
import ar.com.dcsys.gwt.message.shared.Message;
import ar.com.dcsys.gwt.message.shared.MessageException;
import ar.com.dcsys.gwt.message.shared.MessageType;
import ar.com.dcsys.gwt.message.shared.MessageUtils;
import ar.com.dcsys.gwt.person.shared.PersonEncoderDecoder;
import ar.com.dcsys.gwt.person.shared.PersonValueProxy;
import ar.com.dcsys.gwt.ws.client.WebSocket;
import ar.com.dcsys.gwt.ws.client.WebSocketReceiver;
import ar.com.dcsys.gwt.ws.shared.SocketMessageEvent;
import ar.com.dcsys.gwt.ws.shared.SocketMessageEventHandler;

public class JustificationsManagerBean implements JustificationsManager {

	private static Logger logger = Logger.getLogger(JustificationsManagerBean.class.getName());
	
	private final ManagerUtils managerUtils;
	private final AssistanceFactory assistanceFactory;
	private final MessageUtils messagesFactory;
	private final AssistanceEncoderDecoder assistanceEncoderDecoder;
	private final PersonEncoderDecoder personEncoderDecoder;
	private final WebSocket socket;
	
	private final EventBus eventBus;
	private final SocketMessageEventHandler eventHandler = new SocketMessageEventHandler() {
		
		@Override
		public void onMessage(Message msg) {
			
			if (!MessageType.EVENT.equals(msg.getType())) {
				return;
			}
			
			Event event = messagesFactory.event(msg);
			if (!AssistanceMethods.justificationModifiedEvent.equals(event.getName())) {
				return;
			}
			
			String id = event.getParams();
			if (id == null) {
				logger.log(Level.SEVERE, "JustificationModifiedEvent but id == null");
				return;
			}
			
			try {
				eventBus.fireEvent(new JustificationModifiedEvent(id));
			} catch (Exception e) {
				logger.log(Level.SEVERE, e.getMessage(),e);
			}
		}
	};
	
	
	@Inject
	public JustificationsManagerBean(EventBus eventBus,
									 ManagerUtils managerUtils,
									 AssistanceFactory assistanceFactory,
									 AssistanceEncoderDecoder assistanceEncoderDecoder, PersonEncoderDecoder personEncoderDecoder,
									 MessageUtils messagesFactory, WebSocket ws) {
		this.eventBus = eventBus;
		this.managerUtils = managerUtils;
		this.assistanceFactory = assistanceFactory;
		this.messagesFactory = messagesFactory;
		this.assistanceEncoderDecoder = assistanceEncoderDecoder;
		this.personEncoderDecoder = personEncoderDecoder;
		this.socket = ws;
		
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
	public void getJustifications(final Receiver<List<Justification>> receiver) {
		try {
			Message msg = messagesFactory.method(AssistanceMethods.getJustifications);
			
			//envio el mensaje al servidor
			socket.open();
			socket.send(msg, new WebSocketReceiver() {
				
				@Override
				public void onSuccess(Message response) {
					
					if (handleError(response, receiver)) {
						return;
					}
					
					List<Justification> justifications = null;
					try {
						String list = response.getPayload();
						justifications = assistanceEncoderDecoder.decodeJustificationList(list);
					} catch (Exception e) {
						receiver.onFailure(e);
					}
					
					try {
						receiver.onSuccess(justifications);
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
	public void persist(Justification justification, final Receiver<Void> receiver) {
		try {
			//serializo los parametros y genero el mensaje
			String json = ManagerUtils.encode(assistanceFactory, Justification.class, justification);
			Message msg = messagesFactory.method(AssistanceMethods.persistJustification,json);
			
			//envío el mensaje al servidor
			socket.open();
			socket.send(msg, new WebSocketReceiver() {

				@Override
				public void onSuccess(Message response) {
					if (handleError(response, receiver)) {
						return;
					}
					receiver.onSuccess(null);
				}

				@Override
				public void onFailure(Throwable t) {
					logger.log(Level.SEVERE,t.getMessage());
					receiver.onFailure(t);
				}
				
			});
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage());
			receiver.onFailure(e);
		}
	}

	@Override
	public void remove(Justification justification,final Receiver<Void> receiver) {
		try {
			//serializo los parametros y genero el mensaje
			String json = ManagerUtils.encode(assistanceFactory, Justification.class, justification);
			Message msg = messagesFactory.method(AssistanceMethods.removeJustification,json);
			
			//envío el mensaje al servidor
			socket.open();
			socket.send(msg, new WebSocketReceiver() {

				@Override
				public void onSuccess(Message response) {
					if (handleError(response, receiver)) {
						return;
					}
					receiver.onSuccess(null);
				}

				@Override
				public void onFailure(Throwable t) {
					logger.log(Level.SEVERE,t.getMessage());
					receiver.onFailure(t);
				}
				
			});
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage());
			receiver.onFailure(e);
		}
	}

	@Override
	public void justify(Person person, Date start, Date end, Justification justification, String notes, final Receiver<Void> receiver) {
		try {
			//serializo los parametros y genero el mensaje
			String[] params = new String[5];
			params[0] = ManagerUtils.encode(assistanceFactory, Person.class, person);
			params[1] = start.toString();
			params[2] = end.toString();
			params[3] = ManagerUtils.encode(assistanceFactory, Justification.class, justification);			
			params[4] = notes;
			
			String json = ManagerUtils.encodeParams(params);
			Message msg = messagesFactory.method(AssistanceMethods.justify,json);
			
			//envio el mensaje
			socket.open();
			socket.send(msg, new WebSocketReceiver() {

				@Override
				public void onSuccess(Message response) {
					if (handleError(response, receiver)) {
						return;
					}
					receiver.onSuccess(null);
				}

				@Override
				public void onFailure(Throwable t) {
					logger.log(Level.SEVERE,t.getMessage());
					receiver.onFailure(t);
				}
				
			});
			
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage());
			receiver.onFailure(e);
		}
	}

	@Override
	public void findBy(List<Person> persons, Date start, Date end, final Receiver<List<JustificationDate>> receiver) {
		try {
			//serializo los parametros y genero el mensaje
			String[] params = new String[3];
			params[0] = personEncoderDecoder.encodePersonList(persons);
			params[1] = start.toString();
			params[2] = end.toString(); 
			String json = ManagerUtils.encodeParams(params);
			Message msg = messagesFactory.method(AssistanceMethods.findByPerson,json);
			
			//envio el mensaje
			socket.open();
			socket.send(msg, new WebSocketReceiver() {

				@Override
				public void onSuccess(Message response) {
					if (handleError(response, receiver)) {
						return;
					}
					
					List<JustificationDate> justifications = null;
					try {
						String list = response.getPayload();
						justifications = assistanceEncoderDecoder.decodeJustificationDateList(list);
					} catch (Exception e) {
						receiver.onFailure(e);
					}
					
					try {
						receiver.onSuccess(justifications);
					} catch (Exception e) {
						logger.log(Level.SEVERE,e.getMessage(),e);
					}					
				}

				@Override
				public void onFailure(Throwable e) {
					logger.log(Level.SEVERE,e.getMessage());
					receiver.onFailure(e);
				}
				
			});
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage());
			receiver.onFailure(e);
		}
	}

	@Override
	public void findByPersonValue(List<PersonValueProxy> persons, Date start,
			Date end, Receiver<List<JustificationDate>> receiver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove(List<JustificationDate> justifications,final Receiver<Void> receiver) {
		try {
			//serializo los parámetros y genero el mensaje
			String json = assistanceEncoderDecoder.encodeJustificationDateList(justifications);
			Message msg = messagesFactory.method(AssistanceMethods.removeJustificationDate,json);
			
			//envío el mensaje al servidor
			socket.open();
			socket.send(msg, new WebSocketReceiver() {
				
				@Override
				public void onSuccess(Message response) {
					if (handleError(response, receiver)) {
						return;
					}
					receiver.onSuccess(null);
				}
				
				@Override
				public void onFailure(Throwable t) {
					logger.log(Level.SEVERE,t.getMessage());
					receiver.onFailure(t);
				}
			});
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage());
			receiver.onFailure(e);
		}
	}

	@Override
	public void persist(List<GeneralJustificationDate> justifications, final Receiver<Void> receiver) {
		try {
			//serializo los parametros y genero el mensaje
			String json = assistanceEncoderDecoder.encodeGeneralJustificationDateList(justifications);
			Message msg = messagesFactory.method(AssistanceMethods.persistGeneralJustificationDate,json);
			
			//envío el mensaje al servidor
			socket.open();
			socket.send(msg, new WebSocketReceiver() {

				@Override
				public void onSuccess(Message response) {
					if (handleError(response, receiver)) {
						return;
					}
					receiver.onSuccess(null);
				}

				@Override
				public void onFailure(Throwable t) {
					logger.log(Level.SEVERE,t.getMessage());
					receiver.onFailure(t);
				}
				
			});
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage());
			receiver.onFailure(e);
		}
	}

	@Override
	public void findGeneralJustificationDateBy(Date start, Date end, final Receiver<List<GeneralJustificationDate>> receiver) {
		try {
			String[] params = new String[2];
			params[0] = start.toString();
			params[1] = end.toString(); 
			String json = ManagerUtils.encodeParams(params);
			Message msg = messagesFactory.method(AssistanceMethods.findGeneralJustificationDateBy,json);
			
			//envio el mensaje al servidor
			socket.open();
			socket.send(msg, new WebSocketReceiver() {
	
				@Override
				public void onSuccess(Message response) {
					if (handleError(response, receiver)) {
						return;
					}
					
					List<GeneralJustificationDate> justifications = null;
					try {
						String list = response.getPayload();
						justifications = assistanceEncoderDecoder.decodeGeneralJustificationDateList(list);
					} catch (Exception e) {
						receiver.onFailure(e);
					}
					
					try {
						receiver.onSuccess(justifications);
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
			logger.log(Level.SEVERE,e.getMessage());
			receiver.onFailure(e);
		}
		
	}

	@Override
	public void removeGeneralJustificationDate(List<GeneralJustificationDate> justifications,final Receiver<Void> receiver) {
		try {
			//serializo los parametros y genero el mensaje
			String json = assistanceEncoderDecoder.encodeGeneralJustificationDateList(justifications);
			Message msg = messagesFactory.method(AssistanceMethods.removeGeneralJustificationDate,json);
			
			//envío el mensaje al servidor
			socket.open();
			socket.send(msg, new WebSocketReceiver() {

				@Override
				public void onSuccess(Message response) {
					if (handleError(response, receiver)) {
						return;
					}
					receiver.onSuccess(null);
				}

				@Override
				public void onFailure(Throwable t) {
					logger.log(Level.SEVERE,t.getMessage());
					receiver.onFailure(t);
				}
				
			});
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage());
			receiver.onFailure(e);
		}
	}

}
