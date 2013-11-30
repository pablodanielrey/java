package ar.com.dcsys.gwt.message.shared;



import javax.inject.Inject;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;


public class MessagesFactoryImpl implements MessagesFactory {

	private final MessageFactory messageFactory;
	
	@Inject
	public MessagesFactoryImpl(MessageFactory messageFactory) {
		this.messageFactory = messageFactory;
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
		msg.setId(request.getId());
		msg.setSessionId(request.getSessionId());
		msg.setType(MessageType.RETURN);
		return msg;
	}	
	
	
	@Override
	public Message error(String error) {
		Message msg = message();
		msg.setType(MessageType.ERROR);
		msg.setPayload(error);
		return msg;
	}
	
	
	@Override
	public Message method(String function) {
		
		AutoBean<Method> mbean = messageFactory.method();
		Method method = mbean.as();
		method.setName(function);
		String mjson = AutoBeanCodex.encode(mbean).getPayload();

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
		String mjson = AutoBeanCodex.encode(mbean).getPayload();
		
		Message msg = message();
		msg.setType(MessageType.FUNCTION);
		msg.setPayload(mjson);
		
		return msg;
	}

	
	
	
	@Override
	public Method method(Message msg) {
		String json = msg.getPayload();
		AutoBean<Method> bean = AutoBeanCodex.decode(messageFactory, Method.class, json);
		Method method = bean.as();
		return method;
	}
	
}
