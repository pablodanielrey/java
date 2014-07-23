package ar.com.dcsys.gwt.ws.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.naming.NamingException;
import javax.servlet.http.HttpSession;
import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import ar.com.dcsys.gwt.messages.server.handlers.MessageHandler;
import ar.com.dcsys.gwt.messages.server.handlers.MessageHandlers;
import ar.com.dcsys.gwt.messages.shared.MessageEncoderDecoder;
import ar.com.dcsys.gwt.messages.shared.Transport;
import ar.com.dcsys.gwt.messages.shared.TransportEvent;
import ar.com.dcsys.gwt.messages.shared.TransportReceiver;
import ar.com.dcsys.utils.BeanManagerLocator;
import ar.com.dcsys.utils.BeanManagerUtils;


@ServerEndpoint(value = "/websockets", configurator = WebsocketsConfigurator.class)
public class Websockets implements Transport {

	private static Logger logger = Logger.getLogger(Websockets.class.getName());
	
	private SessionRegister sessions;
	
	private final Map<String,List<String>> responses = new ConcurrentHashMap<>();
	private final List<MessageHandler> handlers = new ArrayList<>();
	private final ExecutorService execService = Executors.newCachedThreadPool();

	
	/*
	 * Envío de eventos desde el server hacia el cliente.
	 */
	private void onTransportEvent(@Observes TransportEvent event) {
		String type = event.getType();
		String msg = event.getMessage();
		TransportReceiver tr = event.getTransportReceiver();
		sendEvent(type + ";" + msg,tr);
	}

	public Websockets() {
		
		try {
			BeanManager bm = BeanManagerLocator.getBeanManager();

			MessageHandlers md = BeanManagerUtils.lookup(MessageHandlers.class, bm);
			logger.info("Detectando handlers");
			List<MessageHandler> mhandlers = md.discover();
			logger.info(mhandlers.size() + " detectados");
			for (MessageHandler mh : mhandlers) {
				logger.info("Message Handler : " + mh.getClass().getName() + " registrado");
			}
			
			handlers.clear();
			handlers.addAll(mhandlers);
			
			sessions = BeanManagerUtils.lookup(SessionRegister.class, bm);
			
		} catch (NamingException e) {
			logger.log(Level.SEVERE,"No se configura ningun handler ya que no se pudo obtener el BeanManager",e);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Exception", e);
		}
		
	}
	
	
	private void saveHttpSession(Session session, EndpointConfig config) {
		// paso la session desde la config a la session de websockets.
		Object o = config.getUserProperties().get(HttpSession.class.getName());
		session.getUserProperties().put(HttpSession.class.getName(),o);
	}
	
	private HttpSession getHttpSession(Session session) {
		Object o = session.getUserProperties().get(HttpSession.class.getName());
		return (HttpSession)o;
	}
	

	@OnOpen
	public void onOpen(Session session, EndpointConfig config) {
		logger.log(Level.INFO,"onOpen");

		saveHttpSession(session, config);
		
		
		sessions.registerSession(session);
		/*
		Session s = sessions.get(session.getId());
		if (s == null) {
			sessions.put(session.getId(), session);
		}
		*/
	}
	
	@OnClose
	public void onClose(Session session, CloseReason reason) {
		StringBuilder sb = new StringBuilder();
		sb.append("onClose - ").append(reason.getCloseCode().getCode()).append(" - ").append(reason.getReasonPhrase());
		logger.log(Level.INFO,sb.toString());
		
		sessions.unregisterSession(session);
	}
	
	@OnError
	public void onError(Session session, Throwable error) {
		logger.log(Level.WARNING,error.getMessage(),error);
	}

	
	private void registerResponse(String id, String sid) {
		List<String> s = responses.get(id);
		if (s == null) {
			s = new ArrayList<>();
		}
		s.add(sid);
		responses.put(id, s);
	}
	
	private void unregisterResponse(String id, String sid) {
		List<String> s = responses.get(id);
		if (s == null) {
			return;
		}
		s.remove(sid);
	}
	
	
	
	
	@OnMessage
	public void onMessage(Session session, String msg) {
		logger.log(Level.INFO,"Msg : " + msg);
		
		HttpSession hs = getHttpSession(session);

		DefaultMessageContext ctx = new DefaultMessageContext();
		ctx.setTransport(this);
		ctx.setHttpSession(hs);
		ctx.setWsSession(session);
		
		String[] dmsg = MessageEncoderDecoder.decode(msg);

		sessions.registerSession(session);
		if (!MessageEncoderDecoder.BROADCAST.equals(dmsg[0])) {
			registerResponse(dmsg[0], session.getId());
		}
		
		execService.execute(new MessageWorker(dmsg[0], dmsg[2], ctx, handlers));
		/*
		try {
			MessageWorker mw = new MessageWorker(dmsg[0], dmsg[1], ctx, handlers);
			mw.run();
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage(),e);
		}
		*/
	}
	
	
	/**
	 * Envía un mensaje con id. si el id es igual a un mensaje recibido, esta registrado y por lo tanto se responde 
	 * solo a esa sesion. en caso contrarió se envía como un mensaje sin id. (genera un broadcast)
	 */
	@Override
	public void send(String id, String msg, TransportReceiver rec) {
		if (MessageEncoderDecoder.BROADCAST.equals(id)) {
			
			logger.info("BROADCAST : " + msg);
			sendBroadcast(msg, rec);
			
		} else {
			
			List<String> sids = responses.get(id);
			if (sids == null || sids.size() <= 0) {
				
				// si no existe ninguna session registrada con ese id de mensaje entonces hace un broadcast.
				logger.info("BROADCAST : " + msg);
				sendBroadcast(msg, rec);
				
			} else {
				
				// se responde solo por las sessiones que se vio ese id.
				logger.info("MESSAGE : SERVER -> CLIENT : " + msg);
				
				try {
					String emsg = MessageEncoderDecoder.encode(MessageEncoderDecoder.RPC, id, msg);
					boolean failure = false;
					String error = "";
					for (String sid : sids) {
						
						Session s = sessions.get(sid);
						if (s == null) {
							continue;
						}
						
						if (!s.isOpen()) {
							
							sessions.unregisterSession(s);;
							
						} else {
							try {
								s.getBasicRemote().sendText(emsg);
								unregisterResponse(id, sid);
								
							} catch (Exception e) {
								logger.severe(e.getMessage());
								error = e.getMessage();
								failure = true;
							}
						}
					}
					
					if (failure) {
						rec.onFailure(error);
					} else {
						rec.onSuccess(null);
					}
					
				} catch (Exception e) {
					logger.severe(e.getMessage());
					rec.onFailure(e.getMessage());
				}
			}
		}
	}

	/**
	 * Envía un mensaje sin id, por lo que se envía a todos como broadcast.
	 */
	@Override
	public void send(String msg, TransportReceiver rec) {
		sendBroadcast(msg, rec);
	}

	
	/**
	 * Envía un evento a todas las sesiones abiertas.
	 * @param msg
	 * @param rec
	 */
	private void sendEvent(String msg, TransportReceiver rec) {
		try {
			String emsg = MessageEncoderDecoder.encode(MessageEncoderDecoder.EVENT, MessageEncoderDecoder.BROADCAST, msg);
			for (String sid : sessions.getIds()) {
				Session s = sessions.get(sid);
				if (!s.isOpen()) {
					sessions.unregisterSession(s);
				} else {
					s.getBasicRemote().sendText(emsg);
				} 
			}
			rec.onSuccess(null);
			
		} catch (Exception e) {
			logger.severe(e.getMessage());
			rec.onFailure(e.getMessage());
		}
	}	

	
	/**
	 * Envía un mensaje a todas las sesiones abiertas.
	 * @param msg
	 * @param rec
	 */
	private void sendBroadcast(String msg, TransportReceiver rec) {
		try {
			String emsg = MessageEncoderDecoder.encode(MessageEncoderDecoder.RPC, MessageEncoderDecoder.BROADCAST, msg);
			for (String sid : sessions.getIds()) {
				Session s = sessions.get(sid);
				if (!s.isOpen()) {
					sessions.unregisterSession(s);
				} else {
					s.getBasicRemote().sendText(emsg);
				} 
			}
			rec.onSuccess(null);
			
		} catch (Exception e) {
			logger.severe(e.getMessage());
			rec.onFailure(e.getMessage());
		}
	}
	
	
	/*
	
	private void sendError(MessageEncoderDecoder med, Session session, String id, String error) {
		
		Message merror = getMessageFactory().message().as();
		merror.setId(id);
		merror.setType(MessageType.ERROR);
		merror.setPayload(error);
		String response = med.encode(Message.class,merror);

		if (!session.isOpen()) {
			logger.log(Level.SEVERE,"error : " + error + " session cerrada");
		}

		try {
			session.getBasicRemote().sendText(response);
		} catch (IOException e) {
			logger.log(Level.SEVERE,e.getMessage(),e);
		}
		
	}
	*/
	
}

