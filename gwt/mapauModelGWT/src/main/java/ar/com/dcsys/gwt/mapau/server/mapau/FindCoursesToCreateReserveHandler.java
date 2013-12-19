package ar.com.dcsys.gwt.mapau.server.mapau;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;

import ar.com.dcsys.data.silabouse.Course;
import ar.com.dcsys.gwt.manager.server.AbstractMessageHandler;
import ar.com.dcsys.gwt.mapau.shared.MapauEncoderDecoder;
import ar.com.dcsys.gwt.mapau.shared.MapauMethods;
import ar.com.dcsys.gwt.mapau.shared.SilegEncoderDecoder;
import ar.com.dcsys.gwt.message.shared.Message;
import ar.com.dcsys.gwt.message.shared.MessageTransport;
import ar.com.dcsys.gwt.message.shared.MessageUtils;
import ar.com.dcsys.gwt.message.shared.Method;
import ar.com.dcsys.model.reserve.AppointmentsManager;

public class FindCoursesToCreateReserveHandler extends AbstractMessageHandler {

	private static Logger logger = Logger.getLogger(FindCoursesToCreateReserveHandler.class.getName());
	
	private final MessageUtils messageUtils;
	private final MapauEncoderDecoder encoderDecoder;
	private final SilegEncoderDecoder silegEncoderDecoder;
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
	public FindCoursesToCreateReserveHandler(MessageUtils messageUtils,
									   MapauEncoderDecoder encoderDecoder,
									   SilegEncoderDecoder silegEncoderDecoder,
									   AppointmentsManager appointmentsManager) {
		this.messageUtils = messageUtils;
		this.encoderDecoder = encoderDecoder;
		this.silegEncoderDecoder = silegEncoderDecoder;
		this.appointmentsManager = appointmentsManager;
	}

	@Override
	public boolean handles(Method method) {
		return MapauMethods.findCoursesToCreateReserve.equals(method.getName());
	}
	
	@Override
	public void handle(Message msg, Method method, MessageTransport transport) {
		try {
			
			List<Course> courses = appointmentsManager.getCoursesToCreateReserveAttempt();
			String list = silegEncoderDecoder.encodeCourseList(courses);
			sendResponse(msg, transport, list);
			
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage(),e);
			sendError(msg, transport, e.getMessage());
		}
	}
	
	
}
