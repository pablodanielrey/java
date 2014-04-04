package ar.com.dcsys.gwt.person.server.reports;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;

import ar.com.dcsys.gwt.manager.server.AbstractMessageHandler;
import ar.com.dcsys.gwt.manager.server.ServerManagerUtils;
import ar.com.dcsys.gwt.manager.shared.ManagerFactory;
import ar.com.dcsys.gwt.message.server.MessageContext;
import ar.com.dcsys.gwt.message.server.handlers.MessageHandlers;
import ar.com.dcsys.gwt.message.shared.Message;
import ar.com.dcsys.gwt.message.shared.MessageTransport;
import ar.com.dcsys.gwt.message.shared.MessageUtils;
import ar.com.dcsys.gwt.message.shared.Method;
import ar.com.dcsys.gwt.person.shared.PersonEncoderDecoder;
import ar.com.dcsys.gwt.person.shared.PersonFactory;
import ar.com.dcsys.gwt.person.shared.PersonMethods;
import ar.com.dcsys.model.DocumentsManager;

@Singleton
public class ListPersonReportsMethodHandler extends AbstractMessageHandler {

	private static final Logger logger = Logger.getLogger(ListPersonReportsMethodHandler.class.getName());

	private final PersonEncoderDecoder encoderDecoder;
	private final MessageUtils mf;
	private final ServerManagerUtils managerUtils;
	private final ManagerFactory managerFactory;
	private final ReportContainer reportContainer;
	private final DocumentsManager documentsManager;
	
	@Override
	protected Logger getLogger() {
		return logger;
	}
	
	@Override
	protected MessageUtils getMessageUtils() {
		return mf;
	}
		
	
	@Inject
	public ListPersonReportsMethodHandler(PersonFactory personFactory,
									  PersonEncoderDecoder encoderDecoder, 
									  MessageUtils messagesFactory,
									  ManagerFactory managerFactory,
									  ServerManagerUtils managerUtils,
									  ReportContainer reportContainer,
									  DocumentsManager documentsManager) {
		this.encoderDecoder = encoderDecoder;
		this.mf = messagesFactory;
		this.reportContainer = reportContainer;
		this.managerFactory = managerFactory;
		this.managerUtils = managerUtils;
		this.documentsManager = documentsManager;
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
			List<String> reports = documentsManager.findAll();
			String payload = managerUtils.encodeStringList(reports);
			
			sendResponse(msg, transport, payload);
			
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage(),e);
			sendError(msg,transport,e.getMessage());
			
		}
	}
	
}
