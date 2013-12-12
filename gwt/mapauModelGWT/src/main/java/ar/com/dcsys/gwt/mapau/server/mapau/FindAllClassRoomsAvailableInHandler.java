package ar.com.dcsys.gwt.mapau.server.mapau;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;

import ar.com.dcsys.data.classroom.ClassRoom;
import ar.com.dcsys.data.reserve.ReserveAttemptDate;
import ar.com.dcsys.gwt.manager.server.AbstractMessageHandler;
import ar.com.dcsys.gwt.manager.shared.ManagerUtils;
import ar.com.dcsys.gwt.mapau.shared.ClassRoomsEncoderDecoder;
import ar.com.dcsys.gwt.mapau.shared.MapauEncoderDecoder;
import ar.com.dcsys.gwt.mapau.shared.MapauMethods;
import ar.com.dcsys.gwt.mapau.shared.SilegEncoderDecoder;
import ar.com.dcsys.gwt.message.shared.Message;
import ar.com.dcsys.gwt.message.shared.MessageTransport;
import ar.com.dcsys.gwt.message.shared.MessageUtils;
import ar.com.dcsys.gwt.message.shared.Method;
import ar.com.dcsys.model.reserve.ReserveAttemptsManager;

public class FindAllClassRoomsAvailableInHandler extends AbstractMessageHandler {

	private static Logger logger = Logger.getLogger(FindAllClassRoomsAvailableInHandler.class.getName());
	
	private final MessageUtils messageUtils;
	private final ManagerUtils managerUtils;
	private final MapauEncoderDecoder encoderDecoder;
	private final SilegEncoderDecoder silegEncoderDecoder;
	private final ClassRoomsEncoderDecoder classRoomEncoderDecoder;
	private final ReserveAttemptsManager reserveAttemtpsManager;
	
	@Override
	protected Logger getLogger() {
		return logger;
	}
	
	@Override
	protected MessageUtils getMessageUtils() {
		return messageUtils;
	}
	
	@Inject
	public FindAllClassRoomsAvailableInHandler(MessageUtils messageUtils,
									   ManagerUtils managerUtils,
									   MapauEncoderDecoder encoderDecoder,
									   SilegEncoderDecoder silegEncoderDecoder,
									   ClassRoomsEncoderDecoder classRoomEncoderDecoder,
									   ReserveAttemptsManager reserveAttemptsManager) {
		this.messageUtils = messageUtils;
		this.managerUtils = managerUtils;
		this.encoderDecoder = encoderDecoder;
		this.silegEncoderDecoder = silegEncoderDecoder;
		this.classRoomEncoderDecoder = classRoomEncoderDecoder;
		this.reserveAttemtpsManager = reserveAttemptsManager;
	}

	@Override
	public boolean handles(Method method) {
		return MapauMethods.findAllClassRoomsAvailableInRads.equals(method.getName());
	}
	
	@Override
	public void handle(Message msg, Method method, MessageTransport transport) {
		try {
			String params = method.getParams();
			List<String> lparams = ManagerUtils.decodeParams(params);
			if (lparams.size() != 2) {
				sendError(msg, transport, "Cantidad de parámetros incorrectos : " + lparams.size());
				return;
			}
			
			List<ReserveAttemptDate> rads = encoderDecoder.decodeReserveAttemptDateList(lparams.get(0));
			Boolean checkCapacity = managerUtils.decodeBoolean(lparams.get(1));
			List<ClassRoom> classRooms = reserveAttemtpsManager.findAllClassRoomsAvailableIn(rads, checkCapacity);
			
			String list = classRoomEncoderDecoder.encodeClassRoomList(classRooms);
			sendResponse(msg, transport, list);
			
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage(),e);
			sendError(msg, transport, e.getMessage());
		}
	}
	
	
}
