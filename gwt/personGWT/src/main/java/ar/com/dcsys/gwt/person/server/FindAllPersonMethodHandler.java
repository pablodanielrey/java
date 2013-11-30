package ar.com.dcsys.gwt.person.server;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;

import ar.com.dcsys.gwt.message.server.MessageHandlers;
import ar.com.dcsys.gwt.message.server.MethodHandler;
import ar.com.dcsys.gwt.message.shared.Message;
import ar.com.dcsys.gwt.message.shared.MessageException;
import ar.com.dcsys.gwt.message.shared.MessageTransport;
import ar.com.dcsys.gwt.message.shared.MessagesFactory;
import ar.com.dcsys.gwt.message.shared.Method;
import ar.com.dcsys.gwt.person.shared.PersonEncoderDecoder;
import ar.com.dcsys.gwt.person.shared.PersonFactory;
import ar.com.dcsys.gwt.person.shared.PersonMethods;
import ar.com.dcsys.gwt.person.shared.PersonProxy;

@Singleton
public class FindAllPersonMethodHandler implements MethodHandler {

	private static final Logger logger = Logger.getLogger(FindAllPersonMethodHandler.class.getName());

	private final PersonEncoderDecoder encoderDecoder;
	private final MessagesFactory mf;
	private final PersonFactory pf;
	
	@Inject
	public FindAllPersonMethodHandler(PersonEncoderDecoder encoderDecoder, MessagesFactory messagesFactory, PersonFactory personFactory) {
		this.encoderDecoder = encoderDecoder;
		this.mf = messagesFactory;
		this.pf = personFactory;
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
		return PersonMethods.findAll.equals(method.getName());
	}
	
	@Override
	public void handle(Message msg, Method method, MessageTransport transport) {
		
		logger.info("se llamo a " + PersonMethods.findAll);

		List<PersonProxy> persons = generateSampleList();
		String lpersons = encoderDecoder.encodeList(persons);
		
		sendResponse(msg, transport, lpersons);
	}
	
	private List<PersonProxy> generateSampleList() {
		
		List<PersonProxy> persons = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			PersonProxy pp = pf.person().as();
			pp.setName(String.valueOf(i));
			pp.setLastName(String.valueOf(i));
			pp.setDni(String.valueOf(i));
			persons.add(pp);
		}
		
		return persons;
	}
	
	
	
	private void sendResponse(Message r, MessageTransport transport, String payload) {
		
		Message msg = mf.response(r);
		msg.setPayload(payload);
		
		try {
			transport.send(msg);
		} catch (MessageException e) {
			logger.log(Level.SEVERE,e.getMessage(),e);
		}
	}
	
}
