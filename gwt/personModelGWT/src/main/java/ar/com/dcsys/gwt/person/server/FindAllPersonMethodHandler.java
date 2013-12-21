package ar.com.dcsys.gwt.person.server;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;

import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.person.PersonType;
import ar.com.dcsys.exceptions.PersonException;
import ar.com.dcsys.gwt.manager.server.AbstractMessageHandler;
import ar.com.dcsys.gwt.message.server.MessageContext;
import ar.com.dcsys.gwt.message.server.handlers.MessageHandlers;
import ar.com.dcsys.gwt.message.shared.Message;
import ar.com.dcsys.gwt.message.shared.MessageTransport;
import ar.com.dcsys.gwt.message.shared.MessageUtils;
import ar.com.dcsys.gwt.message.shared.Method;
import ar.com.dcsys.gwt.person.shared.PersonEncoderDecoder;
import ar.com.dcsys.gwt.person.shared.PersonFactory;
import ar.com.dcsys.gwt.person.shared.PersonMethods;
import ar.com.dcsys.gwt.person.shared.PersonValueProxy;
import ar.com.dcsys.model.PersonsManager;

/**
 * Handler que se encarga de despachar los mensajes de las distintas de versiones de findAll
 * Buscan todas las personas y las retorna.
 * la version que busca values solamente es para retornar al cliente una version reducida de los datos mínimas para 
 * minimizar el trafico desde el servidor al cliente.
 * 
 * @author pablo
 *
 */
@Singleton
public class FindAllPersonMethodHandler extends AbstractMessageHandler {

	private static final Logger logger = Logger.getLogger(FindAllPersonMethodHandler.class.getName());

	private final PersonEncoderDecoder encoderDecoder;
	private final MessageUtils mf;
	private final PersonsManager personsModel;
	private final PersonFactory personFactory;
	
	@Override
	protected Logger getLogger() {
		return logger;
	}
	
	@Override
	protected MessageUtils getMessageUtils() {
		return mf;
	}
	
	
	@Inject
	public FindAllPersonMethodHandler(PersonEncoderDecoder encoderDecoder, 
									  MessageUtils messagesFactory, 
									  PersonFactory personFactory,
									  PersonsManager personsModel) {
		this.encoderDecoder = encoderDecoder;
		this.mf = messagesFactory;
		this.personFactory = personFactory;
		this.personsModel = personsModel;
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
		final String m = method.getName();
		return (PersonMethods.findAll.equals(m) || 
				PersonMethods.findAllValues.equals(m) ||
				PersonMethods.findAllValuesByType.equals(m));
	}
	
	@Override
	public void handle(MessageContext ctx, Message msg, Method method) {

		MessageTransport transport = ctx.getMessageTransport();
		
		try {
			String m = method.getName();
			
			// busco los datos en el modelo.
			List<Person> persons = null;
			if (PersonMethods.findAll.equals(m) || PersonMethods.findAllValues.equals(m)) {
				
				persons = personsModel.findAll();
				
			} else if (PersonMethods.findAllValuesByType.equals(m)) {
				
				String params = method.getParams();
				List<PersonType> types = encoderDecoder.decodeTypeList(params);
				
				persons = personsModel.findAllBy(types);
				
			}

			
			// codifico los resultados.
			
			String lpersons = null;
			if (PersonMethods.findAll.equals(m)) {
				lpersons = encoderDecoder.encodePersonList(persons);
				
			} else if (PersonMethods.findAllValues.equals(m) || PersonMethods.findAllValuesByType.equals(m)) {
				List<PersonValueProxy> pvs = new ArrayList<>();
				for (Person p : persons) {
					PersonValueProxy pv = personFactory.personValue().as();
					pv.setId(p.getId());
					pv.setDni(p.getDni());
					pv.setName(p.getName());
					pv.setLastName(p.getLastName());
					pvs.add(pv);
				}
				lpersons = encoderDecoder.encodePersonValueList(pvs);
			}
			
			sendResponse(msg, transport, lpersons);
		
		} catch (PersonException e) {
			logger.log(Level.SEVERE,e.getMessage(),e);
			sendError(msg,transport,e.getMessage());
		}
	}
	
}
