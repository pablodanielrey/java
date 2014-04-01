package ar.com.dcsys.gwt.assistance.server;

import java.util.logging.Logger;

import ar.com.dcsys.gwt.manager.server.AbstractMessageHandler;
import ar.com.dcsys.gwt.message.server.MessageContext;
import ar.com.dcsys.gwt.message.shared.Message;
import ar.com.dcsys.gwt.message.shared.MessageUtils;
import ar.com.dcsys.gwt.message.shared.Method;

import javax.inject.Singleton;

@Singleton
public class FindByPersonMethodHandler extends AbstractMessageHandler {

	
	
	@Override
	public boolean handles(Method method) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void handle(MessageContext ctx, Message msg, Method method) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Logger getLogger() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected MessageUtils getMessageUtils() {
		// TODO Auto-generated method stub
		return null;
	}

}
