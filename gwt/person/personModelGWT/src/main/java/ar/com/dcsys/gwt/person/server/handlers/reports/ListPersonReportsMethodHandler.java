package ar.com.dcsys.gwt.person.server.handlers.reports;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;

import ar.com.dcsys.data.document.Document;
import ar.com.dcsys.gwt.manager.server.AbstractMessageHandler;
import ar.com.dcsys.gwt.manager.server.ServerManagerUtils;
import ar.com.dcsys.gwt.manager.shared.ManagerFactory;
import ar.com.dcsys.gwt.message.server.MessageContext;
import ar.com.dcsys.gwt.message.server.handlers.MessageHandlers;
import ar.com.dcsys.gwt.message.shared.Message;
import ar.com.dcsys.gwt.message.shared.MessageTransport;
import ar.com.dcsys.gwt.message.shared.MessageUtils;
import ar.com.dcsys.gwt.message.shared.Method;
import ar.com.dcsys.gwt.person.shared.DocumentEncoderDecoder;
import ar.com.dcsys.gwt.person.shared.PersonMethods;
import ar.com.dcsys.model.DocumentsManager;

@Singleton
public class ListPersonReportsMethodHandler extends AbstractMessageHandler {

	private static final Logger logger = Logger.getLogger(ListPersonReportsMethodHandler.class.getName());

	private final MessageUtils mf;
	private final ServerManagerUtils managerUtils;
	private final ManagerFactory managerFactory;
	private final DocumentsManager documentsManager;
	private final DocumentEncoderDecoder documentEncoderDecoder;
	
	@Override
	protected Logger getLogger() {
		return logger;
	}
	
	@Override
	protected MessageUtils getMessageUtils() {
		return mf;
	}
		
	
	@Inject
	public ListPersonReportsMethodHandler(
									  MessageUtils messagesFactory,
									  ManagerFactory managerFactory,
									  ServerManagerUtils managerUtils,
									  DocumentsManager documentsManager,
									  DocumentEncoderDecoder documentEncoderDecoder) {
		this.mf = messagesFactory;
		this.managerFactory = managerFactory;
		this.managerUtils = managerUtils;
		this.documentsManager = documentsManager;
		this.documentEncoderDecoder = documentEncoderDecoder;
	}

	/**
	 * Se registra como handler cuando es llamado por el evento disparado por CDI
	 * @param mh
	 */
	public void register(@Observes MessageHandlers mh) {
		mh.addHandler(this);
	}
	
	@Override
	public boolean handles(Method method) {
		return PersonMethods.findPersonReportsData.equals(method.getName());
	}
	
	@Override
	public void handle(MessageContext ctx, Message msg, Method method) {

		MessageTransport transport = ctx.getMessageTransport();
		
		try {
			List<Document> ds = new ArrayList<>();
			List<String> reports = documentsManager.findAll();
			for (String id : reports) {
				Document d = documentsManager.findByIdWithoutContent(id);
				ds.add(d);
			}
			
			String payload = documentEncoderDecoder.encodeDocumentList(ds);
			
			sendResponse(msg, transport, payload);
			
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage(),e);
			sendError(msg,transport,e.getMessage());
			
		}
	}
	
}
