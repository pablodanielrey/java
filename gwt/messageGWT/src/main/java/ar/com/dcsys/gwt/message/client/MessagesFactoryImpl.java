package ar.com.dcsys.gwt.message.client;

import ar.com.dcsys.gwt.message.shared.Message;
import ar.com.dcsys.gwt.message.shared.MessageFactory;
import ar.com.dcsys.gwt.message.shared.MessageType;
import ar.com.dcsys.gwt.message.shared.MessagesFactory;
import ar.com.dcsys.gwt.message.shared.Method;

import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;


public class MessagesFactoryImpl implements MessagesFactory {

	private final MessageFactory messageFactory;
	
	@Inject
	public MessagesFactoryImpl(MessageFactory messageFactory) {
		this.messageFactory = messageFactory;
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

	
	
	
}
