package ar.com.dcsys.gwt.person.server.handlers.reports;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;

import ar.com.dcsys.data.document.Document;
import ar.com.dcsys.data.document.DocumentBean;
import ar.com.dcsys.exceptions.PersonException;
import ar.com.dcsys.gwt.manager.server.AbstractMessageHandler;
import ar.com.dcsys.gwt.message.server.MessageContext;
import ar.com.dcsys.gwt.message.server.handlers.MessageHandlers;
import ar.com.dcsys.gwt.message.shared.Message;
import ar.com.dcsys.gwt.message.shared.MessageException;
import ar.com.dcsys.gwt.message.shared.MessageTransport;
import ar.com.dcsys.gwt.message.shared.MessageUtils;
import ar.com.dcsys.gwt.message.shared.Method;
import ar.com.dcsys.gwt.person.shared.DocumentEncoderDecoder;
import ar.com.dcsys.gwt.person.shared.PersonEncoderDecoder;
import ar.com.dcsys.gwt.person.shared.PersonFactory;
import ar.com.dcsys.gwt.person.shared.PersonMethods;
import ar.com.dcsys.model.DocumentsManager;
import ar.com.dcsys.model.PersonsManager;

@Singleton
public class PersonReportMethodHandler extends AbstractMessageHandler {

	private static final Logger logger = Logger.getLogger(PersonReportMethodHandler.class.getName());

	private final MessageUtils mf;
	private final PersonsManager personsModel;
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
	public PersonReportMethodHandler( MessageUtils messagesFactory,
									  PersonsManager personsModel,
									  DocumentsManager documentsManager,
									  DocumentEncoderDecoder documentEncoderDecoder) {
		this.mf = messagesFactory;
		this.personsModel = personsModel;
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
		return PersonMethods.reportPersonsData.equals(method.getName());
	}
	
	@Override
	public void handle(MessageContext ctx, Message msg, Method method) {
		
		MessageTransport transport = ctx.getMessageTransport();
		
		try {
			Document d = generateReport();
			String sd = documentEncoderDecoder.encode(d);
			sendResponse(msg, transport, sd);
			//sendEvent(transport, sd);
			
		} catch (PersonException e) {
			logger.log(Level.SEVERE,e.getMessage(),e);
			sendError(msg,transport,e.getMessage());
			
		}
	}
	
	private void sendEvent(MessageTransport transport, String id) {
		Message msg = mf.event(PersonMethods.personReportEvent, id);
		try {
			transport.send(msg);
		} catch (MessageException e) {
			logger.log(Level.SEVERE,e.getMessage(),e);
		}
	}
	
	

	private Document generateReport() throws PersonException {
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		personsModel.reportPersons(out);
		
		try {
			byte[] content = out.toByteArray();
			
			DocumentBean d = new DocumentBean();
			d.setName("Reporte de personas");
			d.setCreated(new Date());
			d.setContent(content);
			
			documentsManager.persist(d);
			
			return d.cloneWithoutContent();
			
		} catch (Exception e) {
			throw new PersonException(e);
		}
		
	}
	
	
}
