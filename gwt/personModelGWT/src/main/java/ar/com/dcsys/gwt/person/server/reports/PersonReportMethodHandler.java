package ar.com.dcsys.gwt.person.server.reports;

import java.io.ByteArrayOutputStream;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;

import ar.com.dcsys.exceptions.PersonException;
import ar.com.dcsys.gwt.manager.server.AbstractMessageHandler;
import ar.com.dcsys.gwt.message.server.MessageContext;
import ar.com.dcsys.gwt.message.server.handlers.MessageHandlers;
import ar.com.dcsys.gwt.message.shared.Message;
import ar.com.dcsys.gwt.message.shared.MessageException;
import ar.com.dcsys.gwt.message.shared.MessageTransport;
import ar.com.dcsys.gwt.message.shared.MessageUtils;
import ar.com.dcsys.gwt.message.shared.Method;
import ar.com.dcsys.gwt.person.shared.PersonEncoderDecoder;
import ar.com.dcsys.gwt.person.shared.PersonFactory;
import ar.com.dcsys.gwt.person.shared.PersonMethods;
import ar.com.dcsys.model.PersonsManager;

@Singleton
public class PersonReportMethodHandler extends AbstractMessageHandler {

	private static final Logger logger = Logger.getLogger(PersonReportMethodHandler.class.getName());

	private final PersonEncoderDecoder encoderDecoder;
	private final MessageUtils mf;
	private final PersonsManager personsModel;
	private final PersonFactory personFactory;
	private final ReportContainer reportContainer;
	
	@Override
	protected Logger getLogger() {
		return logger;
	}
	
	@Override
	protected MessageUtils getMessageUtils() {
		return mf;
	}
		
	
	@Inject
	public PersonReportMethodHandler(PersonFactory personFactory,
									  PersonEncoderDecoder encoderDecoder, 
									  MessageUtils messagesFactory,
									  PersonsManager personsModel,
									  ReportContainer reportContainer) {
		this.encoderDecoder = encoderDecoder;
		this.mf = messagesFactory;
		this.personsModel = personsModel;
		this.personFactory = personFactory;
		this.reportContainer = reportContainer;
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
			
			String id = generateReport();
			sendResponse(msg, transport, id);
			sendEvent(transport, id);
			
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
	
	

	private String generateReport() throws PersonException {
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		personsModel.reportPersons(out);
		
		try {
			String id = UUID.randomUUID().toString();
			reportContainer.addReport(id, out.toByteArray());
			return id;
			
		} catch (Exception e) {
			throw new PersonException(e);
		}
		
	}
	
	
}
