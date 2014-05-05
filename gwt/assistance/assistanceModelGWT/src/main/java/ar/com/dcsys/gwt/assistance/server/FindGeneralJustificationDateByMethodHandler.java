package ar.com.dcsys.gwt.assistance.server;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.justification.GeneralJustificationDate;
import ar.com.dcsys.gwt.assistance.shared.AssistanceEncoderDecoder;
import ar.com.dcsys.gwt.assistance.shared.AssistanceFactory;
import ar.com.dcsys.gwt.assistance.shared.AssistanceMethods;
import ar.com.dcsys.gwt.manager.server.AbstractMessageHandler;
import ar.com.dcsys.gwt.manager.server.ServerManagerUtils;
import ar.com.dcsys.gwt.message.server.MessageContext;
import ar.com.dcsys.gwt.message.shared.Message;
import ar.com.dcsys.gwt.message.shared.MessageTransport;
import ar.com.dcsys.gwt.message.shared.MessageUtils;
import ar.com.dcsys.gwt.message.shared.Method;
import ar.com.dcsys.model.justification.JustificationsManager;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class FindGeneralJustificationDateByMethodHandler extends AbstractMessageHandler {

	private static final Logger logger = Logger.getLogger(FindGeneralJustificationDateByMethodHandler.class.getName());
	
	private final AssistanceEncoderDecoder encoderDecoder;
	private final MessageUtils mf;
	private final AssistanceFactory assistanceFactory;
	private final JustificationsManager justificationsModel;
	
	@Inject
	public FindGeneralJustificationDateByMethodHandler(AssistanceEncoderDecoder encoderDecoder,
													   MessageUtils messagesFactory,
													   AssistanceFactory assistanceFactory,
													   JustificationsManager justificationsModel) {
		this.encoderDecoder = encoderDecoder;
		this.mf = messagesFactory;
		this.assistanceFactory = assistanceFactory;
		this.justificationsModel = justificationsModel;
	}
	
	@Override
	public boolean handles(Method method) {
		return AssistanceMethods.findGeneralJustificationDateBy.equals(method.getName());
	}

	@Override
	public void handle(MessageContext ctx, Message msg, Method method) {
		MessageTransport transport = ctx.getMessageTransport();
		
		String params = method.getParams();
		List<String> datesStr = ServerManagerUtils.decodeParams(params);
		
		if (datesStr.size() != 2) {
			logger.log(Level.SEVERE, "Cantidad de parametros incorrectos");
			sendError(msg,transport,"Cantidad de parametros incorrectos");
		}
		
		try {
			Date start = new Date(datesStr.get(0)); 
			Date end = new Date(datesStr.get(1));
			
			List<GeneralJustificationDate> justifications = justificationsModel.findGeneralJustificationDateBy(start, end);
			
			//codifico los resultados
			String ljustifications = encoderDecoder.encodeGeneralJustificationDateList(justifications);
			
			sendResponse(msg,transport,ljustifications);
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage(),e);
			sendError(msg,transport,e.getMessage());
		}
	}

	@Override
	protected Logger getLogger() {
		return logger;
	}

	@Override
	protected MessageUtils getMessageUtils() {
		return mf;
	}

}
