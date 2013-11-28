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
	
	
	@Override
	public Message method(String function) {
		AutoBean<Method> mbean = messageFactory.method();
		Method method = mbean.as();
		method.setName(function);
		String mjson = AutoBeanCodex.encode(mbean).getPayload();
		
		AutoBean<Message> bean = messageFactory.message();
		Message msg = bean.as();
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
		
		AutoBean<Message> bean = messageFactory.message();
		Message msg = bean.as();
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
