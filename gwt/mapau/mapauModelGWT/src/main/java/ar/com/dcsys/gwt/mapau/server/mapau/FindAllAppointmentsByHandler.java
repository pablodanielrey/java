package ar.com.dcsys.gwt.mapau.server.mapau;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;

import ar.com.dcsys.data.appointment.AppointmentV2;
import ar.com.dcsys.gwt.manager.client.ManagerUtils;
import ar.com.dcsys.gwt.manager.server.AbstractMessageHandler;
import ar.com.dcsys.gwt.mapau.shared.MapauEncoderDecoder;
import ar.com.dcsys.gwt.mapau.shared.MapauFactory;
import ar.com.dcsys.gwt.mapau.shared.MapauMethods;
import ar.com.dcsys.gwt.message.server.MessageContext;
import ar.com.dcsys.gwt.message.shared.Message;
import ar.com.dcsys.gwt.message.shared.MessageTransport;
import ar.com.dcsys.gwt.message.shared.MessageUtils;
import ar.com.dcsys.gwt.message.shared.Method;
import ar.com.dcsys.model.reserve.AppointmentsManager;

public class FindAllAppointmentsByHandler extends AbstractMessageHandler {

	private static Logger logger = Logger.getLogger(FindAllAppointmentsByHandler.class.getName());
	
	private final MessageUtils messageUtils;
	private final ManagerUtils managerUtils;
	private final MapauEncoderDecoder encoderDecoder;
	private final AppointmentsManager appointmentsManager;
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
	public FindAllAppointmentsByHandler(MessageUtils messageUtils, 
										ManagerUtils managerUtils,
										MapauEncoderDecoder encoderDecoder,
										AppointmentsManager appointmentsManager,
										MapauFactory mapauFactory) {
		this.messageUtils = messageUtils;
		this.managerUtils = managerUtils;
		this.encoderDecoder = encoderDecoder;
		this.appointmentsManager = appointmentsManager;
		this.mapauFactory = mapauFactory;
	}

	@Override
	public boolean handles(Method method) {
		return MapauMethods.findAllAppointmentsBy.equals(method.getName());
	}
	
	@Override
	public void handle(MessageContext ctx, Message msg, Method method) {
		
		MessageTransport transport = ctx.getMessageTransport();
		
		try {
			
			String params = method.getParams();
			List<String> lparams = ManagerUtils.decodeParams(params);
			
			if (lparams.size() != 3) {
				sendError(msg, transport, "Cantidad de parámetros incorrectos : " + lparams.size());
				return;
			}
			
			AppointmentV2 app = ManagerUtils.decode(mapauFactory, AppointmentV2.class, lparams.get(0));
			List<Date> dates = managerUtils.decodeDateList(lparams.get(1));
			Boolean checkHour = managerUtils.decodeBoolean(lparams.get(2));
			
			List<AppointmentV2> apps = appointmentsManager.findAllAppointmentsBy(app, dates, checkHour);
			String sapps = encoderDecoder.encodeAppointmentV2List(apps);
			
			sendResponse(msg, transport, sapps);
			
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage(),e);
			sendError(msg, transport, e.getMessage());
		}
	}
	
	
}
