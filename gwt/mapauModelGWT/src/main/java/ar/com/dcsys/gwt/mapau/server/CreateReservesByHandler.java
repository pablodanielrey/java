package ar.com.dcsys.gwt.mapau.server;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;

import ar.com.dcsys.data.appointment.AppointmentV2;
import ar.com.dcsys.data.classroom.ClassRoom;
import ar.com.dcsys.data.reserve.ReserveAttemptDate;
import ar.com.dcsys.gwt.manager.server.AbstractMessageHandler;
import ar.com.dcsys.gwt.manager.shared.ManagerUtils;
import ar.com.dcsys.gwt.mapau.shared.ClassRoomsEncoderDecoder;
import ar.com.dcsys.gwt.mapau.shared.MapauEncoderDecoder;
import ar.com.dcsys.gwt.mapau.shared.MapauFactory;
import ar.com.dcsys.gwt.mapau.shared.MapauMethods;
import ar.com.dcsys.gwt.message.shared.Message;
import ar.com.dcsys.gwt.message.shared.MessageTransport;
import ar.com.dcsys.gwt.message.shared.MessageUtils;
import ar.com.dcsys.gwt.message.shared.Method;
import ar.com.dcsys.model.reserve.ReserveAttemptsManager;
import ar.com.dcsys.model.reserve.ReservesManager;

public class CreateReservesByHandler extends AbstractMessageHandler {

	private static Logger logger = Logger.getLogger(CreateReservesByHandler.class.getName());
	
	private final MessageUtils messageUtils;
	private final ManagerUtils managerUtils;
	private final MapauEncoderDecoder encoderDecoder;
	private final ClassRoomsEncoderDecoder classRoomEncoderDecoder;
	private final ReserveAttemptsManager reserveAttemptsManager;
	private final ReservesManager reservesManager;
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
	public CreateReservesByHandler(MessageUtils messageUtils, 
										ManagerUtils managerUtils,
										MapauEncoderDecoder encoderDecoder,
										ClassRoomsEncoderDecoder classRoomEncoderDecoder,
										ReserveAttemptsManager reserveAttempsManager,
										ReservesManager reservesManager,
										MapauFactory mapauFactory) {
		this.messageUtils = messageUtils;
		this.managerUtils = managerUtils;
		this.encoderDecoder = encoderDecoder;
		this.classRoomEncoderDecoder = classRoomEncoderDecoder;
		this.reserveAttemptsManager = reserveAttempsManager;
		this.reservesManager = reservesManager;
		this.mapauFactory = mapauFactory;
	}

	@Override
	public boolean handles(Method method) {
		return MapauMethods.createReserves.equals(method.getName());
	}
	
	@Override
	public void handle(Message msg, Method method, MessageTransport transport) {
		try {
			
			String params = method.getParams();
			List<String> lparams = ManagerUtils.decodeParams(params);
			
			if (lparams.size() != 3) {
				sendError(msg, transport, "Cantidad de parámetros incorrectos : " + lparams.size());
				return;
			}
			
			List<ReserveAttemptDate> rads = encoderDecoder.decodeReserveAttemptDateList(lparams.get(0));
			List<ClassRoom> classRooms = classRoomEncoderDecoder.decodeClassRoomList(lparams.get(1));
			String description = lparams.get(3);

			for (ReserveAttemptDate rad : rads) {
				for (ClassRoom cr : classRooms) {
					reservesManager.createNew(rad, cr, description);
				}
			}
			sendResponse(msg, transport, null);
			
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage(),e);
			sendError(msg, transport, e.getMessage());
		}
	}
	
	
}
