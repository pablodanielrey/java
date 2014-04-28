package ar.com.dcsys.gwt.ws.server;

import java.util.List;
import java.util.logging.Logger;

import ar.com.dcsys.gwt.messages.server.handlers.MessageHandler;

public class MessageWorker implements Runnable {

	private static Logger logger = Logger.getLogger(Websockets.class.getName());
	
	private final List<MessageHandler> handlers;
	private final DefaultMessageContext ctx;
	private final String msg;
	private final String id;
	
	public MessageWorker(String id, String msg, DefaultMessageContext ctx, List<MessageHandler> handlers) {
		this.id = id;
		this.msg = msg;
		this.ctx = ctx;
		this.handlers = handlers;
	}
	
	@Override
	public void run() {

		boolean handled = false;
		for (MessageHandler mh : handlers) {
			logger.info("handle : " + mh.getClass().getName());
			if (mh.handle(id, msg, ctx)) {
				handled = true;
			}
		}

		if (!handled) {
			logger.severe("msg : " + id + " " + msg + " not handled");
		}
		
	}	
	
}
