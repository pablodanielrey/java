package ar.com.dcsys.gwt.message.client;

import ar.com.dcsys.gwt.message.shared.MessageEncoderDecoder;
import ar.com.dcsys.gwt.message.shared.MessageFactory;
import ar.com.dcsys.gwt.message.shared.MessagesFactory;
import ar.com.dcsys.gwt.message.shared.MessagesFactoryImpl;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;

public class MessageGinModule extends AbstractGinModule {

	@Override
	protected void configure() {
		
		bind(MessageFactory.class);
		bind(MessageEncoderDecoder.class).in(Singleton.class);
		bind(MessagesFactory.class).to(MessagesFactoryImpl.class).in(Singleton.class);
		
	}
	
}
