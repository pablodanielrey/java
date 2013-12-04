package ar.com.dcsys.gwt.ws.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.inject.spi.BeanManager;
import javax.naming.NamingException;
import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import ar.com.dcsys.gwt.message.server.MessageHandlersDetection;
import ar.com.dcsys.gwt.message.server.MethodHandler;
import ar.com.dcsys.gwt.message.shared.Message;
import ar.com.dcsys.gwt.message.shared.MessageEncoderDecoder;
import ar.com.dcsys.gwt.message.shared.MessageException;
import ar.com.dcsys.gwt.message.shared.MessageFactory;
import ar.com.dcsys.gwt.message.shared.MessageTransport;
import ar.com.dcsys.gwt.message.shared.MessageType;
import ar.com.dcsys.gwt.message.shared.MessageUtils;
import ar.com.dcsys.gwt.message.shared.Method;
import ar.com.dcsys.gwt.utils.server.BeanManagerLocator;
import ar.com.dcsys.gwt.utils.server.BeanManagerUtils;


@ServerEndpoint(value = "/websockets", configurator = WebsocketsConfigurator.class)
public class Websockets {

	private static Logger logger = Logger.getLogger(Websockets.class.getName());

	private Map<String,Session> sessions = new ConcurrentHashMap<>();
	
	private final MessageTransport transport = new MessageTransport() {
		@Override
		public void send(Message msg) throws MessageException {

			if (msg == null) {
				throw new MessageException("msg == null");
			}
			
			MessageEncoderDecoder med = getEncoderDecoder();
			String json = med.encode(Message.class, msg);
			
			if (MessageType.EVENT.equals(msg.getType())) {
				
				for (Session session : sessions.values()) {
					try {
						if (session != null && session.isOpen()) {
							session.getBasicRemote().sendText(json);
						}
					} catch (IOException e) {
						throw new MessageException(e);
					}				
				}
				
				return;
				
			} else if (MessageType.RETURN.equals(msg.getType()) ||
					   MessageType.ERROR.equals(msg.getType())) {
				
				String sId = msg.getSessionId();
				if (sId == null) {
					throw new MessageException("message.sid == null");
				}
				
				Session session = sessions.get(sId);
				if (!session.isOpen()) {
					throw new MessageException("message.sid == closed");
				}
				
				try {
					session.getBasicRemote().sendText(json);
				} catch (IOException e) {
					throw new MessageException(e);
				}				
			
				return;
			}
			
			throw new MessageException("MessageType == unknown");

		}
	};

	
	private MessageUtils messagesFactory = null;
	private MessageEncoderDecoder encoderDecoder = null;
	private MessageFactory messageFactory = null;
	
	private MessageFactory getMessageFactory() {
		if (messageFactory != null) {
			return messageFactory;
		}
		try {
			BeanManager bm = BeanManagerLocator.getBeanManager();
			messageFactory = BeanManagerUtils.lookup(MessageFactory.class,bm);
			return messageFactory;
		} catch (NamingException e) {
			return null;
		}
	}
	
	private MessageUtils getMessagesFactory() {
		if (messagesFactory != null) {
			return messagesFactory;
		}
		try {
			BeanManager bm = BeanManagerLocator.getBeanManager();
			messagesFactory = BeanManagerUtils.lookup(MessageUtils.class,bm);
			return messagesFactory;
		} catch (NamingException e) {
			return null;
		}
	}
	
	private MessageEncoderDecoder getEncoderDecoder() {
		if (encoderDecoder != null) {
			return encoderDecoder;
		}
		try {
			BeanManager bm = BeanManagerLocator.getBeanManager();
			encoderDecoder = BeanManagerUtils.lookup(MessageEncoderDecoder.class,bm);
			return encoderDecoder;
		} catch (NamingException e) {
			return null;
		}
	}	
	
	private List<MethodHandler> handlers = new ArrayList<MethodHandler>();

	public Websockets() {
		
		try {
			BeanManager bm = BeanManagerLocator.getBeanManager();
			MessageHandlersDetection mh = BeanManagerUtils.lookup(MessageHandlersDetection.class,bm);
			logger.info("Detectando handlers");
			List<MethodHandler> methodHandlers = mh.detectMethodHandlers();
			logger.info(methodHandlers.size() + " detectados");
			for (MethodHandler m : methodHandlers) {
				logger.info("Handler : " + m.getClass().getName() + " registrado");
			}
			handlers.addAll(methodHandlers);
			
		} catch (NamingException e) {
			logger.log(Level.SEVERE,"No se configura ningun handler ya que no se pudo obtener el BeanManager",e);
		}
		
	}
	
	/*
	 * No se puede usar porque no funca CDI en websockets todavía.
	@Inject
	public Websockets(MessageFactory messageFactory, MessageEncoderDecoder messageEncoderDecoder, MessagesFactory messagesFactory) {
		logger.info("iniciando constructor inyectado");
		this.messageFactory = messageFactory;
		this.messageEncoderDecoder = messageEncoderDecoder;
		this.messagesFactory = messagesFactory;
	}
	*/
	

	@OnOpen
	public void onOpen(Session session, EndpointConfig config) {
		logger.log(Level.INFO,"onOpen");

		Session s = sessions.get(session.getId());
		if (s == null) {
			sessions.put(session.getId(), session);
		}
	}
	
	@OnClose
	public void onClose(Session session, CloseReason reason) {
		StringBuilder sb = new StringBuilder();
		sb.append("onClose - ").append(reason.getCloseCode().getCode()).append(" - ").append(reason.getReasonPhrase());
		logger.log(Level.INFO,sb.toString());
		
		sessions.remove(session.getId());
	}
	
	@OnError
	public void onError(Session session, Throwable error) {
		logger.log(Level.WARNING,error.getMessage(),error);
	}

	
	@OnMessage
	public void onMessage(Session session, String json) {
		logger.log(Level.INFO,"Msg : " + json);
		
		String sId = session.getId();

		// decodifico el mensaje:
		MessageEncoderDecoder med = getEncoderDecoder();
		Message msg = med.decode(Message.class,json);
		msg.setSessionId(sId);

		if (MessageType.FUNCTION.equals(msg.getType())) {
			
			///////////// METODO ////////////
			
			MessageUtils mf = getMessagesFactory();
			Method method = mf.method(msg);
			
			logger.info("Metodo : " + method.getName());
			logger.info("Parametros : " + method.getParams());
			
			logger.info("Buscando handler para manejarlo");
			boolean handled = false;
			for (MethodHandler mh : handlers) {
				if (mh.handles(method)) {
					mh.handle(msg,method,transport);
					handled = true;
				}
			}
			
			if (!handled) {
				sendError(med,session,msg.getId(),"No se encontró hanlder para la función : " + method.getName());
			}
			
			return;
		} 
	
		
		///////// no se conoce el tipo de mensaje asi que se envía un error /////////////////
		
		sendError(med,session,msg.getId(),"Tipo de mensaje desconocido");
	}
	
	
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
	
}

