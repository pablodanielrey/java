package ar.com.dcsys.gwt.assistance.client.manager;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.period.PeriodAssignation;
import ar.com.dcsys.data.period.PeriodType;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.gwt.assistance.client.manager.events.JustificationModifiedEvent;
import ar.com.dcsys.gwt.assistance.client.manager.events.PeriodModifiedEvent;
import ar.com.dcsys.gwt.assistance.shared.AssistanceEncoderDecoder;
import ar.com.dcsys.gwt.assistance.shared.AssistanceFactory;
import ar.com.dcsys.gwt.assistance.shared.AssistanceMethods;
import ar.com.dcsys.gwt.manager.client.ManagerUtils;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.message.shared.Event;
import ar.com.dcsys.gwt.message.shared.Message;
import ar.com.dcsys.gwt.message.shared.MessageException;
import ar.com.dcsys.gwt.message.shared.MessageType;
import ar.com.dcsys.gwt.message.shared.MessageUtils;
import ar.com.dcsys.gwt.person.shared.PersonEncoderDecoder;
import ar.com.dcsys.gwt.ws.client.WebSocket;
import ar.com.dcsys.gwt.ws.client.WebSocketReceiver;
import ar.com.dcsys.gwt.ws.shared.SocketMessageEvent;
import ar.com.dcsys.gwt.ws.shared.SocketMessageEventHandler;

import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;

public class PeriodsManagerBean implements PeriodsManager {
	
	private static Logger logger = Logger.getLogger(PeriodsManagerBean.class.getName());
	
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
			if (!AssistanceMethods.periodModifiedEvent.equals(event.getName())) {
				return;
			}
			
			String id = event.getParams();
			if (id == null) {
				logger.log(Level.SEVERE, "PeriodModifiedEvent but id == null");
				return;
			}
			
			try {
				eventBus.fireEvent(new PeriodModifiedEvent(id));
			} catch (Exception e) {
				logger.log(Level.SEVERE, e.getMessage(),e);
			}
		}
	};	
	
	@Inject
	public PeriodsManagerBean(EventBus eventBus,
							  ManagerUtils managerUtils,
							  AssistanceFactory assistanceFactory, AssistanceEncoderDecoder assistanceEncoderDecoder,
							  PersonEncoderDecoder personEncoderDecoder,
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
	public void findPersonsWithPeriodAssignation(final Receiver<List<Person>> receiver) {
		try {
			Message msg = messagesFactory.method(AssistanceMethods.findPersonsWithPeriodAssignation);
			
			//envio el mensaje al servidor
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
	public void findAllTypesPeriods(final Receiver<List<PeriodType>> receiver) {
		try {
			Message msg = messagesFactory.method(AssistanceMethods.findAllTypesPeriods);
			
			//envio el mensaje al servidor
			socket.open();
			socket.send(msg, new WebSocketReceiver() {
				
				@Override
				public void onSuccess(Message response) {
					
					if (handleError(response, receiver)) {
						return;
					}
					
					List<PeriodType> types = null;
					try {
						String list = response.getPayload();
						types = assistanceEncoderDecoder.decodePeriodTypeList(list);
					} catch (Exception e) {
						receiver.onFailure(e);
					}
					
					try {
						receiver.onSuccess(types);
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
	public void findPeriodsAssignationsBy(Person person,final Receiver<List<PeriodAssignation>> receiver) {
		try {
			//serializo los parametros y genero el mensaje
			String json = ManagerUtils.encode(assistanceFactory, Person.class, person);
			Message msg = messagesFactory.method(AssistanceMethods.findPeriodsAssignationsBy,json);
			
			//envío el mensaje al servidor
			socket.open();
			socket.send(msg, new WebSocketReceiver() {
	
				@Override
				public void onSuccess(Message response) {
					if (handleError(response, receiver)) {
						return;
					}
					
					List<PeriodAssignation> periods = null;
					try {
						String list = response.getPayload();
						periods = assistanceEncoderDecoder.decodePeriodAssignationList(list);
					} catch (Exception e) {
						receiver.onFailure(e);
					}
					
					try {
						receiver.onSuccess(periods);
					} catch (Exception e) {
						logger.log(Level.SEVERE,e.getMessage(),e);
					}
				}
	
				@Override
				public void onFailure(Throwable t) {
					logger.log(Level.SEVERE,t.getMessage());
					receiver.onFailure(t);
				}
				
			});
		} catch (Exception e) {
			receiver.onFailure(e);
		}
	}
	
	@Override
	public void persist(Person person, PeriodAssignation periodAssignation,final Receiver<Void> receiver) {
		try {
			//serializo los parametros y genero el mensaje
			String[] params = new String[2];
			params[0] = ManagerUtils.encode(assistanceFactory, Person.class, person);
			params[1] = ManagerUtils.encode(assistanceFactory, PeriodAssignation.class, periodAssignation);
			String json = ManagerUtils.encodeParams(params);
			Message msg = messagesFactory.method(AssistanceMethods.persistPeriods,json);
			
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
			receiver.onFailure(e);
		}
	}
	
	@Override
	public void remove(Person person, PeriodAssignation periodAssignation,final Receiver<Void> receiver) {
		try {
			//serializo los parametros y genero el mensaje
			String[] params = new String[2];
			params[0] = ManagerUtils.encode(assistanceFactory, Person.class, person);
			params[1] = ManagerUtils.encode(assistanceFactory, PeriodAssignation.class, periodAssignation);
			String json = ManagerUtils.encodeParams(params);
			Message msg = messagesFactory.method(AssistanceMethods.removePeriods,json);
			
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

}
