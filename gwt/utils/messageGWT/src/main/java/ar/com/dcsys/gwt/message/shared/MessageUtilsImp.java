package ar.com.dcsys.gwt.message.shared;



import javax.inject.Inject;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;


public class MessageUtilsImp implements MessageUtils {

	private final MessageFactory messageFactory;
	private final MessageEncoderDecoder encoderDecoder;
	
	
	@Inject
	public MessageUtilsImp(MessageFactory messageFactory, MessageEncoderDecoder encoderDecoder) {
		this.messageFactory = messageFactory;
		this.encoderDecoder = encoderDecoder;
	}
	
	/**
	 * crea un nuevo mensaje para poder despues cargarle los parametros.
	 * @return
	 */
	private Message message() {
		AutoBean<Message> bean = messageFactory.message();
		Message msg = bean.as();
		return msg;
	}
	
	
	@Override
	public Message response(Message request) {
		Message msg = message();
		setAsResponse(request, msg);
		msg.setType(MessageType.RETURN);
		return msg;
	}
	
	@Override
	public void setAsResponse(Message original, Message response) {
		response.setId(original.getId());
		response.setSessionId(original.getSessionId());
	}
	
	
	@Override
	public Message error(String error) {
		Message msg = message();
		msg.setType(MessageType.ERROR);
		msg.setPayload(error);
		return msg;
	}
	
	@Override
	public Message error(Message msg, String error) {
		Message e = error(error);
		e.setId(msg.getId());
		e.setSessionId(msg.getSessionId());
		return e;
	}
	
	
	@Override
	public Message method(String function) {
		
		AutoBean<Method> mbean = messageFactory.method();
		Method method = mbean.as();
		method.setName(function);
		String mjson = encoderDecoder.encode(Method.class, method);

		Message msg = message();
		msg.setType(MessageType.FUNCTION);
		msg.setPayload(mjson);
		
		return msg;
	}
	
	@Override
	public Message method(String function, String params) {
		
		AutoBean<Method> mbean = messageFactory.method();
		Method method = mbean.as();
		method.setName(function);
		method.setParams(params);
		String mjson = encoderDecoder.encode(Method.class, method);
		
		Message msg = message();
		msg.setType(MessageType.FUNCTION);
		msg.setPayload(mjson);
		
		return msg;
	}

	
	@Override
	public Message event(String name, String payload) {
		AutoBean<Event> mbean = messageFactory.event();
		Event e = mbean.as();
		e.setName(name);
		e.setParams(payload);
		String mjson = encoderDecoder.encode(Event.class, e);
		
		Message msg = message();
		msg.setType(MessageType.EVENT);
		msg.setPayload(mjson);
		
		return msg;
	}
	
	
	@Override
	public Method method(Message msg) {
		String json = msg.getPayload();
		Method method = encoderDecoder.decode(Method.class, json);
		return method;
	}

	@Override
	public Event event(Message msg) {
		String json = msg.getPayload();
		Event event = encoderDecoder.decode(Event.class, json);
		return event;
	}
	
}
