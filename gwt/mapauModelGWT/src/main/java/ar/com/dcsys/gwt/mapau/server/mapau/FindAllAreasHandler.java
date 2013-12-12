package ar.com.dcsys.gwt.mapau.server.mapau;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;

import ar.com.dcsys.data.silabouse.Area;
import ar.com.dcsys.gwt.manager.server.AbstractMessageHandler;
import ar.com.dcsys.gwt.mapau.shared.MapauEncoderDecoder;
import ar.com.dcsys.gwt.mapau.shared.MapauMethods;
import ar.com.dcsys.gwt.mapau.shared.SilegEncoderDecoder;
import ar.com.dcsys.gwt.message.shared.Message;
import ar.com.dcsys.gwt.message.shared.MessageTransport;
import ar.com.dcsys.gwt.message.shared.MessageUtils;
import ar.com.dcsys.gwt.message.shared.Method;
import ar.com.dcsys.model.silabouse.AreasManager;

public class FindAllAreasHandler extends AbstractMessageHandler {

	private static Logger logger = Logger.getLogger(FindAllAreasHandler.class.getName());
	
	private final MessageUtils messageUtils;
	private final MapauEncoderDecoder encoderDecoder;
	private final SilegEncoderDecoder silegEncoderDecoder;
	private final AreasManager areasManager;
	
	@Override
	protected Logger getLogger() {
		return logger;
	}
	
	@Override
	protected MessageUtils getMessageUtils() {
		return messageUtils;
	}
	
	@Inject
	public FindAllAreasHandler(MessageUtils messageUtils,
									   MapauEncoderDecoder encoderDecoder,
									   SilegEncoderDecoder silegEncoderDecoder,
									   AreasManager areasManager) {
		this.messageUtils = messageUtils;
		this.encoderDecoder = encoderDecoder;
		this.silegEncoderDecoder = silegEncoderDecoder;
		this.areasManager = areasManager;
	}

	@Override
	public boolean handles(Method method) {
		return MapauMethods.findAllAreas.equals(method.getName());
	}
	
	@Override
	public void handle(Message msg, Method method, MessageTransport transport) {
		try {
			
			List<Area> areas = areasManager.findAll();
			String list = encoderDecoder.encodeAreaList(areas);
			sendResponse(msg, transport, list);
			
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage(),e);
			sendError(msg, transport, e.getMessage());
		}
	}
	
	
}
