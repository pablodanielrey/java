package ar.com.dcsys.gwt.message.shared;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;

public class MessageEncoderDecoder {

	public static String encode(Message msg) {
		AutoBean<Message> bean = AutoBeanUtils.getAutoBean(msg);
		String json = AutoBeanCodex.encode(bean).getPayload();
		return json;
	}
	
	public static Message decode(MessageFactory messageFactory, String json) {
		AutoBean<Message> bean = AutoBeanCodex.decode(messageFactory, Message.class, json);
		return bean.as();
	}	
	
}
