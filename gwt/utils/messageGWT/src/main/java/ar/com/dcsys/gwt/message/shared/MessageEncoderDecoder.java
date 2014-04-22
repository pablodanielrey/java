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
	
	
	public <T> String encode(Class<T> clazz, T t) {
		AutoBean<T> bean = AutoBeanUtils.getAutoBean(t);
		if (bean == null) {
			bean = messageFactory.create(clazz, t);
		}
		String json = AutoBeanCodex.encode(bean).getPayload();
		return json;
	}
	
	public <T> T decode(Class<T> clazz, String json) {
		AutoBean<T> bean = AutoBeanCodex.decode(messageFactory, clazz, json);
		return bean.as();
	}	
	
}
