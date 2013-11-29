package ar.com.dcsys.gwt.ws.server.inject;

import javax.enterprise.inject.Produces;

import ar.com.dcsys.gwt.message.shared.MessageFactory;

import com.google.web.bindery.autobean.vm.AutoBeanFactorySource;

public class MessageFactoryProducer {

	@Produces
	public MessageFactory messageFactory() {
		MessageFactory messageFactory = AutoBeanFactorySource.create(MessageFactory.class);
		return messageFactory;
	}
	
}
