package ar.com.dcsys.gwt.mapau.server.mapau;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;

import ar.com.dcsys.data.reserve.Reserve;
import ar.com.dcsys.data.reserve.ReserveAttemptDate;
import ar.com.dcsys.gwt.manager.client.ManagerUtils;
import ar.com.dcsys.gwt.manager.server.AbstractMessageHandler;
import ar.com.dcsys.gwt.mapau.shared.ClassRoomsEncoderDecoder;
import ar.com.dcsys.gwt.mapau.shared.MapauEncoderDecoder;
import ar.com.dcsys.gwt.mapau.shared.MapauFactory;
import ar.com.dcsys.gwt.mapau.shared.MapauMethods;
import ar.com.dcsys.gwt.mapau.shared.SilegEncoderDecoder;
import ar.com.dcsys.gwt.message.server.MessageContext;
import ar.com.dcsys.gwt.message.shared.Message;
import ar.com.dcsys.gwt.message.shared.MessageTransport;
import ar.com.dcsys.gwt.message.shared.MessageUtils;
import ar.com.dcsys.gwt.message.shared.Method;
import ar.com.dcsys.model.reserve.ReservesManager;

public class FindReservesByHandler extends AbstractMessageHandler {

	private static Logger logger = Logger.getLogger(FindReservesByHandler.class.getName());
	
	private final MapauFactory mapauFactory;
	private final MessageUtils messageUtils;
	private final ManagerUtils managerUtils;
	private final MapauEncoderDecoder encoderDecoder;
	private final SilegEncoderDecoder silegEncoderDecoder;
	private final ClassRoomsEncoderDecoder classRoomEncoderDecoder;
	private final ReservesManager reservesManager;
	
	@Override
	protected Logger getLogger() {
		return logger;
	}
	
	@Override
	protected MessageUtils getMessageUtils() {
		return messageUtils;
	}
	
	@Inject
	public FindReservesByHandler(MapauFactory mapauFactory,
										MessageUtils messageUtils,
									   ManagerUtils managerUtils,
									   MapauEncoderDecoder encoderDecoder,
									   SilegEncoderDecoder silegEncoderDecoder,
									   ClassRoomsEncoderDecoder classRoomEncoderDecoder,
									   ReservesManager reservesManager) {
		this.mapauFactory = mapauFactory;
		this.messageUtils = messageUtils;
		this.managerUtils = managerUtils;
		this.encoderDecoder = encoderDecoder;
		this.silegEncoderDecoder = silegEncoderDecoder;
		this.classRoomEncoderDecoder = classRoomEncoderDecoder;
		this.reservesManager = reservesManager;
	}

	@Override
	public boolean handles(Method method) {
		return MapauMethods.findReservesByRad.equals(method.getName());
	}
	
	@Override
	public void handle(MessageContext ctx, Message msg, Method method) {
		
		MessageTransport transport = ctx.getMessageTransport();
		
		try {
			String params = method.getParams();
			ReserveAttemptDate rad = ManagerUtils.decode(mapauFactory, ReserveAttemptDate.class, params);
			List<Reserve> reserves = reservesManager.findBy(rad);
			
			String list = encoderDecoder.encodeReserveList(reserves);
			sendResponse(msg, transport, list);
			
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage(),e);
			sendError(msg, transport, e.getMessage());
		}
	}
	
	
}
