package ar.com.dcsys.gwt.mapau.server.classRoom;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;

import ar.com.dcsys.data.classroom.CharacteristicQuantity;
import ar.com.dcsys.data.classroom.ClassRoom;
import ar.com.dcsys.gwt.manager.server.AbstractMessageHandler;
import ar.com.dcsys.gwt.manager.shared.ManagerUtils;
import ar.com.dcsys.gwt.mapau.shared.ClassRoomMethods;
import ar.com.dcsys.gwt.mapau.shared.ClassRoomsEncoderDecoder;
import ar.com.dcsys.gwt.mapau.shared.ClassRoomsFactory;
import ar.com.dcsys.gwt.message.server.MessageContext;
import ar.com.dcsys.gwt.message.shared.Message;
import ar.com.dcsys.gwt.message.shared.MessageTransport;
import ar.com.dcsys.gwt.message.shared.MessageUtils;
import ar.com.dcsys.gwt.message.shared.Method;

public class CreateCharacteristicQuantityHandler extends AbstractMessageHandler {

	private static final Logger logger = Logger.getLogger(CreateCharacteristicQuantityHandler.class.getName());
	
	private final MessageUtils messageUtils;
	private final ManagerUtils managerUtils;
	private final ClassRoomsFactory classRoomsFactory;
	private final ClassRoomsEncoderDecoder classRoomEncoderDecoder;
	
	@Inject
	public CreateCharacteristicQuantityHandler(MessageUtils messageUtils,
											   ManagerUtils managerUtils,
											   ClassRoomsFactory classRoomsFactory,
											   ClassRoomsEncoderDecoder classRoomEncoderDecoder) {
		this.messageUtils = messageUtils;
		this.classRoomEncoderDecoder = classRoomEncoderDecoder;
		this.managerUtils = managerUtils;
		this.classRoomsFactory = classRoomsFactory;
	}
	
	
	@Override
	public boolean handles(Method method) {
		return ClassRoomMethods.createCharacteristicQuantity.equals(method.getName());
	}

	@Override
	public void handle(MessageContext ctx, Message msg, Method method) {
		
		MessageTransport transport = ctx.getMessageTransport();
		
		try {
			String sparams = method.getParams();
			List<String> params = ManagerUtils.decodeParams(sparams);
			if (params.size() != 2) {
				sendError(msg, transport, "CreateCharacteristicQuantity.params.size != 2");
				return;
			}
			
			ClassRoom classRoom = ManagerUtils.decode(classRoomsFactory, ClassRoom.class, params.get(0));
			CharacteristicQuantity cq = ManagerUtils.decode(classRoomsFactory, CharacteristicQuantity.class, params.get(1));
			
			sendError(msg,transport,"FALTA EL MANAGER PARA HACER PERSIST EN EL MODELO DE CHARACTERISTIC QUANTITY");
			
			
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
