package ar.com.dcsys.gwt.message.client;

import ar.com.dcsys.gwt.message.shared.MessageEncoderDecoder;
import ar.com.dcsys.gwt.message.shared.MessageFactory;
import ar.com.dcsys.gwt.message.shared.MessageUtils;
import ar.com.dcsys.gwt.message.shared.MessageUtilsImp;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;

public class MessageGinModule extends AbstractGinModule {

	@Override
	protected void configure() {
		
		bind(MessageFactory.class);
		bind(MessageEncoderDecoder.class).in(Singleton.class);
		bind(MessageUtils.class).to(MessageUtilsImp.class).in(Singleton.class);
		
	}
	
}
