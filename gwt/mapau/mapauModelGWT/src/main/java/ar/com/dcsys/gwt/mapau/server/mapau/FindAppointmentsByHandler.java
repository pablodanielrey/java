package ar.com.dcsys.gwt.mapau.server.mapau;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;

import ar.com.dcsys.data.appointment.Appointment;
import ar.com.dcsys.data.appointment.AppointmentV2;
import ar.com.dcsys.data.filter.TransferFilter;
import ar.com.dcsys.gwt.manager.server.AbstractMessageHandler;
import ar.com.dcsys.gwt.mapau.shared.MapauEncoderDecoder;
import ar.com.dcsys.gwt.mapau.shared.MapauMethods;
import ar.com.dcsys.gwt.message.server.MessageContext;
import ar.com.dcsys.gwt.message.shared.Message;
import ar.com.dcsys.gwt.message.shared.MessageTransport;
import ar.com.dcsys.gwt.message.shared.MessageUtils;
import ar.com.dcsys.gwt.message.shared.Method;
import ar.com.dcsys.model.reserve.AppointmentsManager;

public class FindAppointmentsByHandler extends AbstractMessageHandler {

	private static Logger logger = Logger.getLogger(FindAppointmentsByHandler.class.getName());
	
	private final MessageUtils messageUtils;
	private final AppointmentsManager appointmentsManager;
	private final MapauEncoderDecoder encoderDecoder;
	
	@Override
	protected Logger getLogger() {
		return logger;
	}
	
	@Override
	protected MessageUtils getMessageUtils() {
		return messageUtils;
	}
	
	@Inject
	public FindAppointmentsByHandler(MessageUtils messageUtils, 
									 AppointmentsManager appointmentsManager,
									 MapauEncoderDecoder encoderDecoder) {
		this.messageUtils = messageUtils;
		this.appointmentsManager = appointmentsManager;
		this.encoderDecoder = encoderDecoder;
	}

	@Override
	public boolean handles(Method method) {
		return (MapauMethods.findAppointmentsBy.equals(method.getName()) || 
			    MapauMethods.findAppointmentsV2By.equals(method.getName()));
	}
	
	@Override
	public void handle(MessageContext ctx, Message msg, Method method) {
		
		MessageTransport transport = ctx.getMessageTransport();
		
		try {
			
			String params = method.getParams();
			List<TransferFilter> filters = encoderDecoder.decodeTransferFilterList(params);
			
			String sapps = "";
			
			if (MapauMethods.findAppointmentsBy.equals(method.getName())) {
				
				List<Appointment> apps = appointmentsManager.findAppointmentsBy(filters);
				sapps = encoderDecoder.encodeAppointmentList(apps);
				
			} else if (MapauMethods.findAppointmentsV2By.equals(method.getName())) {
				
				List<AppointmentV2> apps = appointmentsManager.findAppointmentsV2By(filters);
				sapps = encoderDecoder.encodeAppointmentV2List(apps);
			
			}
			
			sendResponse(msg, transport, sapps);
			
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage(),e);
			sendError(msg, transport, e.getMessage());
		}
	}
	
	
}
