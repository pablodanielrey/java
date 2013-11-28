package ar.com.dcsys.gwt.message.shared;


import javax.inject.Inject;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;

public class MessageEncoderDecoder {

	private final MessageFactory messageFactory;
	
	@Inject
	public MessageEncoderDecoder(MessageFactory messageFactory) {
		this.messageFactory = messageFactory;
	}
	
	
	public String encode(Message msg) {
		AutoBean<Message> bean = AutoBeanUtils.getAutoBean(msg);
		String json = AutoBeanCodex.encode(bean).getPayload();
		return json;
	}
	
	public Message decode(String json) {
		AutoBean<Message> bean = AutoBeanCodex.decode(messageFactory, Message.class, json);
		return bean.as();
	}	
	
}
