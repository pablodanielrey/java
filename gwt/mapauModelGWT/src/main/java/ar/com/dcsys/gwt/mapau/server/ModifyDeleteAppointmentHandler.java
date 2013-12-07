package ar.com.dcsys.gwt.mapau.server;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;

import ar.com.dcsys.data.appointment.AppointmentV2;
import ar.com.dcsys.gwt.manager.server.AbstractMessageHandler;
import ar.com.dcsys.gwt.manager.shared.ManagerUtils;
import ar.com.dcsys.gwt.mapau.shared.MapauFactory;
import ar.com.dcsys.gwt.mapau.shared.MapauMethods;
import ar.com.dcsys.gwt.message.shared.Message;
import ar.com.dcsys.gwt.message.shared.MessageTransport;
import ar.com.dcsys.gwt.message.shared.MessageUtils;
import ar.com.dcsys.gwt.message.shared.Method;
import ar.com.dcsys.model.reserve.ReserveAttemptsManager;

public class ModifyDeleteAppointmentHandler extends AbstractMessageHandler {

	private static Logger logger = Logger.getLogger(ModifyDeleteAppointmentHandler.class.getName());
	
	private final MessageUtils messageUtils;
	private final ReserveAttemptsManager reserveAttempsManager;
	private final MapauFactory mapauFactory;
	
	@Override
	protected Logger getLogger() {
		return logger;
	}
	
	@Override
	protected MessageUtils getMessageUtils() {
		return messageUtils;
	}
	
	@Inject
	public ModifyDeleteAppointmentHandler(MessageUtils messageUtils,
										  MapauFactory mapauFactory,
										  ReserveAttemptsManager reserveAttempsManager) {
		this.messageUtils = messageUtils;
		this.reserveAttempsManager = reserveAttempsManager;
		this.mapauFactory = mapauFactory;
	}

	@Override
	public boolean handles(Method method) {
		return (MapauMethods.modifyAppointment.equals(method.getName()) || MapauMethods.deleteAppointment.equals(method.getName())) ;
	}
	
	@Override
	public void handle(Message msg, Method method, MessageTransport transport) {
		try {
			
			String params = method.getParams();
			AppointmentV2 app = ManagerUtils.decode(mapauFactory, AppointmentV2.class, params);
			
			if (MapauMethods.modifyAppointment.equals(method.getName())) {
				
				reserveAttempsManager.modify(app);
				
			} else if (MapauMethods.deleteAppointment.equals(method.getName())) {
				
				reserveAttempsManager.deleteAppointment(app);
				
			}
			
			sendResponse(msg, transport, null);
			
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage(),e);
			sendError(msg, transport, e.getMessage());
		}
	}
	
	
}
