package ar.com.dcsys.gwt.mapau.server.classRoom;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;

import ar.com.dcsys.data.classroom.ClassRoom;
import ar.com.dcsys.gwt.manager.server.AbstractMessageHandler;
import ar.com.dcsys.gwt.manager.shared.ManagerUtils;
import ar.com.dcsys.gwt.mapau.shared.ClassRoomMethods;
import ar.com.dcsys.gwt.mapau.shared.ClassRoomsEncoderDecoder;
import ar.com.dcsys.gwt.mapau.shared.ClassRoomsFactory;
import ar.com.dcsys.gwt.message.shared.Message;
import ar.com.dcsys.gwt.message.shared.MessageTransport;
import ar.com.dcsys.gwt.message.shared.MessageUtils;
import ar.com.dcsys.gwt.message.shared.Method;
import ar.com.dcsys.model.classroom.ClassRoomsManager;

public class FindAllClassRoomsHandler extends AbstractMessageHandler {

	private static final Logger logger = Logger.getLogger(FindAllClassRoomsHandler.class.getName());
	
	private final MessageUtils messageUtils;
	private final ManagerUtils managerUtils;
	private final ClassRoomsFactory classRoomsFactory;
	private final ClassRoomsEncoderDecoder classRoomEncoderDecoder;
	private final ClassRoomsManager classRoomsManager;
	
	@Inject
	public FindAllClassRoomsHandler(MessageUtils messageUtils,
											   ManagerUtils managerUtils,
											   ClassRoomsFactory classRoomsFactory,
											   ClassRoomsEncoderDecoder classRoomEncoderDecoder,
											   ClassRoomsManager classRoomsManager) {
		this.messageUtils = messageUtils;
		this.classRoomEncoderDecoder = classRoomEncoderDecoder;
		this.managerUtils = managerUtils;
		this.classRoomsFactory = classRoomsFactory;
		this.classRoomsManager = classRoomsManager;
	}
	
	
	@Override
	public boolean handles(Method method) {
		return ClassRoomMethods.findAllCharacteristics.equals(method.getName());
	}

	@Override
	public void handle(Message msg, Method method, MessageTransport transport) {
		
		try {
			List<ClassRoom> classRooms = classRoomsManager.findAll();
			String list = classRoomEncoderDecoder.encodeClassRoomList(classRooms);
			sendResponse(msg, transport, list);
			
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage(),e);
			sendError(msg, transport, e.getMessage());
		}
		
	}

	@Override
	protected Logger getLogger() {
		return logger;
	}

	@Override
	protected MessageUtils getMessageUtils() {
		return messageUtils;
	}

	
	
}
