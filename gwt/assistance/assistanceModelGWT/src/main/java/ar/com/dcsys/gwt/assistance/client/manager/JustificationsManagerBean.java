package ar.com.dcsys.gwt.assistance.client.manager;

import java.util.ArrayList;
import java.util.Date;

import com.google.inject.Inject;

import java.util.List;
import com.google.gwt.core.client.GWT;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.justification.GeneralJustificationDate;
import ar.com.dcsys.data.justification.Justification;
import ar.com.dcsys.data.justification.JustificationDate;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.gwt.assistance.client.gin.Injector;
import ar.com.dcsys.gwt.assistance.client.gin.JustificationsManagerProvider;
import ar.com.dcsys.gwt.assistance.client.manager.events.JustificationModifiedEvent;
import ar.com.dcsys.gwt.assistance.shared.JustificationsManagerTransfer;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.person.shared.PersonValueProxy;
import ar.com.dcsys.gwt.ws.client.WebSocket;
import ar.com.dcsys.gwt.ws.shared.event.SocketMessageEvent;
import ar.com.dcsys.gwt.ws.shared.event.SocketMessageEventHandler;
import ar.com.dcsys.gwt.ws.shared.event.SocketStateEvent;
import ar.com.dcsys.gwt.ws.shared.event.SocketStateEventHandler;

public class JustificationsManagerBean implements JustificationsManager {

	private static Logger logger = Logger.getLogger(JustificationsManagerBean.class.getName());
	
	private final WebSocket socket;	
	
	private final JustificationsManagerTransfer justificationsManagerTransfer = GWT.create(JustificationsManagerTransfer.class);
	
	
	@Inject
	public JustificationsManagerBean(WebSocket ws) {
		this.socket = ws;
		this.justificationsManagerTransfer.setTransport(ws);
	}
	
	
	@Override
	public void getJustifications(final Receiver<List<Justification>> receiver) {
		try {
			justificationsManagerTransfer.getJustifications(receiver);
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage());
		}
	}

	@Override
	public void persist(Justification justification, final Receiver<Void> receiver) {
		try {
			justificationsManagerTransfer.persist(justification, receiver);
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage());
		}
	}

	@Override
	public void remove(Justification justification,final Receiver<Void> receiver) {
		try {
			justificationsManagerTransfer.remove(justification, receiver);
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage());
		}
	}

	@Override
	public void justify(Person person, Date start, Date end, Justification justification, String notes, final Receiver<Void> receiver) {
		try {
			justificationsManagerTransfer.justify(person, start, end, justification, notes, receiver);
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage());
		}
	}

	@Override
	public void findBy(List<Person> persons, Date start, Date end, final Receiver<List<JustificationDate>> receiver) {
		try {
			justificationsManagerTransfer.findBy(persons, start, end, receiver);
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage());
		}
	}

	@Override
	public void findByPersonValue(List<PersonValueProxy> persons, Date start,Date end, Receiver<List<JustificationDate>> receiver) {
		try {
			justificationsManagerTransfer.findByPersonValue(persons, start, end, receiver);
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage());	
		}
	}

	@Override
	public void remove(List<JustificationDate> justifications,final Receiver<Void> receiver) {
		try {
			justificationsManagerTransfer.remove(justifications, receiver);
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage());
		}
	}

	@Override
	public void persist(List<GeneralJustificationDate> justifications, final Receiver<Void> receiver) {
		try {
			justificationsManagerTransfer.persist(justifications, receiver);
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage());
		}
	}

	@Override
	public void findGeneralJustificationDateBy(Date start, Date end, final Receiver<List<GeneralJustificationDate>> receiver) {
		try {
			justificationsManagerTransfer.findGeneralJustificationDateBy(start, end, receiver);
			
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage());
		}
		
	}

	@Override
	public void removeGeneralJustificationDate(List<GeneralJustificationDate> justifications,final Receiver<Void> receiver) {
		try {
			justificationsManagerTransfer.removeGeneralJustificationDate(justifications, receiver);
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage());
		}
	}

}
