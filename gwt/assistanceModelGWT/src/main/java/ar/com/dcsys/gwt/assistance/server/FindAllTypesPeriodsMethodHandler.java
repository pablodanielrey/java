package ar.com.dcsys.gwt.assistance.server;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.period.PeriodType;
import ar.com.dcsys.gwt.assistance.shared.AssistanceEncoderDecoder;
import ar.com.dcsys.gwt.assistance.shared.AssistanceFactory;
import ar.com.dcsys.gwt.assistance.shared.AssistanceMethods;
import ar.com.dcsys.gwt.manager.server.AbstractMessageHandler;
import ar.com.dcsys.gwt.message.server.MessageContext;
import ar.com.dcsys.gwt.message.server.handlers.MessageHandlers;
import ar.com.dcsys.gwt.message.shared.Message;
import ar.com.dcsys.gwt.message.shared.MessageTransport;
import ar.com.dcsys.gwt.message.shared.MessageUtils;
import ar.com.dcsys.gwt.message.shared.Method;
import ar.com.dcsys.model.period.PeriodsManager;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.enterprise.event.Observes;

@Singleton
public class FindAllTypesPeriodsMethodHandler extends AbstractMessageHandler {
	
	private static final Logger logger = Logger.getLogger(FindAllTypesPeriodsMethodHandler.class.getName());
	
	private final AssistanceEncoderDecoder encoderDecoder;
	private final MessageUtils mf;
	private final AssistanceFactory af;
	private final PeriodsManager periodsManager;

	
	@Inject
	public FindAllTypesPeriodsMethodHandler(AssistanceEncoderDecoder encoderDecoder,
											MessageUtils messagersFactory,
											AssistanceFactory assistanceFactory,
											PeriodsManager periodsModel) {
		this.encoderDecoder = encoderDecoder;
		this.mf = messagersFactory;
		this.af = assistanceFactory;
		this.periodsManager = periodsModel;
	}
	
	
	/**
	 * Se registra como handler cuando es llamado por el evento disparado por CDI
	 * @param mh
	 */
	public void register(@Observes MessageHandlers mh) {
		mh.addHandler(this);
	}
	
	@Override
	public boolean handles(Method method) {
		return AssistanceMethods.findAllTypesPeriods.equals(method.getName());
	}

	@Override
	public void handle(MessageContext ctx, Message msg, Method method) {
		MessageTransport transport = ctx.getMessageTransport();
		
		try {
			List<PeriodType> types = Arrays.asList(PeriodType.values());
			
			String list = encoderDecoder.encodePeriodTypeList(types);
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
		return mf;
	}

}
