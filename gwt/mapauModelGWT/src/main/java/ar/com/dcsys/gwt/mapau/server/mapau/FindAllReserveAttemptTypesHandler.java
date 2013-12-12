package ar.com.dcsys.gwt.mapau.server.mapau;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;

import ar.com.dcsys.data.reserve.ReserveAttemptDateType;
import ar.com.dcsys.gwt.manager.server.AbstractMessageHandler;
import ar.com.dcsys.gwt.mapau.shared.MapauEncoderDecoder;
import ar.com.dcsys.gwt.mapau.shared.MapauMethods;
import ar.com.dcsys.gwt.message.shared.Message;
import ar.com.dcsys.gwt.message.shared.MessageTransport;
import ar.com.dcsys.gwt.message.shared.MessageUtils;
import ar.com.dcsys.gwt.message.shared.Method;
import ar.com.dcsys.model.reserve.ReserveAttemptTypesManager;

public class FindAllReserveAttemptTypesHandler extends AbstractMessageHandler {

	private static Logger logger = Logger.getLogger(FindAllReserveAttemptTypesHandler.class.getName());
	
	private final MessageUtils messageUtils;
	private final MapauEncoderDecoder encoderDecoder;
	private final ReserveAttemptTypesManager reserveAttemtpsManager;
	
	@Override
	protected Logger getLogger() {
		return logger;
	}
	
	@Override
	protected MessageUtils getMessageUtils() {
		return messageUtils;
	}
	
	@Inject
	public FindAllReserveAttemptTypesHandler(MessageUtils messageUtils,
									   MapauEncoderDecoder encoderDecoder,
									   ReserveAttemptTypesManager reserveAttemptsManager) {
		this.messageUtils = messageUtils;
		this.encoderDecoder = encoderDecoder;
		this.reserveAttemtpsManager = reserveAttemptsManager;
	}

	@Override
	public boolean handles(Method method) {
		return MapauMethods.findAllReserveAttemptTypes.equals(method.getName());
	}
	
	@Override
	public void handle(Message msg, Method method, MessageTransport transport) {
		try {
			
			List<ReserveAttemptDateType> types = reserveAttemtpsManager.findAll();
			String list = encoderDecoder.encodeReserveAttemptDateTypeList(types);
			sendResponse(msg, transport, list);
			
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage(),e);
			sendError(msg, transport, e.getMessage());
		}
	}
	
	
}
