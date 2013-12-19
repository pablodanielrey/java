package ar.com.dcsys.gwt.mapau.server.mapau;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;

import ar.com.dcsys.data.appointment.AppointmentV2;
import ar.com.dcsys.gwt.manager.server.AbstractMessageHandler;
import ar.com.dcsys.gwt.mapau.shared.MapauEncoderDecoder;
import ar.com.dcsys.gwt.mapau.shared.MapauMethods;
import ar.com.dcsys.gwt.message.shared.Message;
import ar.com.dcsys.gwt.message.shared.MessageTransport;
import ar.com.dcsys.gwt.message.shared.MessageUtils;
import ar.com.dcsys.gwt.message.shared.Method;
import ar.com.dcsys.model.reserve.AppointmentsManager;

public class CreateNewAppointmentHandler extends AbstractMessageHandler {

	private static Logger logger = Logger.getLogger(CreateNewAppointmentHandler.class.getName());
	
	private final MessageUtils messageUtils;
	private final MapauEncoderDecoder encoderDecoder;
	private final AppointmentsManager appointmentsManager;
	
	@Override
	protected Logger getLogger() {
		return logger;
	}
	
	@Override
	protected MessageUtils getMessageUtils() {
		return messageUtils;
	}
	
	@Inject
	public CreateNewAppointmentHandler(MessageUtils messageUtils,
									   MapauEncoderDecoder encoderDecoder,
									   AppointmentsManager appointmentsManager) {
		this.messageUtils = messageUtils;
		this.encoderDecoder = encoderDecoder;
		this.appointmentsManager = appointmentsManager;
	}

	@Override
	public boolean handles(Method method) {
		return MapauMethods.createNewAppointments.equals(method.getName());
	}
	
	@Override
	public void handle(Message msg, Method method, MessageTransport transport) {
		try {
			
			String params = method.getParams();
			List<AppointmentV2> apps = encoderDecoder.decodeAppointmentV2List(params);
			appointmentsManager.createNewAppointments(apps);
			
			sendResponse(msg, transport, null);
			
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage(),e);
			sendError(msg, transport, e.getMessage());
		}
	}
	
	
}
