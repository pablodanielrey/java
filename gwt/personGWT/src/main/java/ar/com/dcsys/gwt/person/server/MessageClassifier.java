package ar.com.dcsys.gwt.person.server;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;

import ar.com.dcsys.gwt.message.shared.Message;
import ar.com.dcsys.gwt.message.shared.MessageFactory;

import com.google.web.bindery.autobean.vm.AutoBeanFactorySource;

@Singleton
public class MessageClassifier {

	private static Logger logger = Logger.getLogger(MessageClassifier.class.getName());
	
	private MessageFactory messageFactory;
	
	
	@PostConstruct
	public void init() {
		logger.log(Level.INFO,"MessageClassifier iniciando");
		messageFactory = AutoBeanFactorySource.create(MessageFactory.class);
	}
	
	public void classify(Message msg) {
		// decodifico el mensaje:
		logger.log(Level.INFO,"id : " + msg.getId());
		logger.log(Level.INFO,"Type : " + msg.getType().toString());
		logger.log(Level.INFO,"Payload : " + msg.getPayload());

	}
	
}
