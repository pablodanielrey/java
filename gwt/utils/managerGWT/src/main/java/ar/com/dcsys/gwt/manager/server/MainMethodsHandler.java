package ar.com.dcsys.gwt.manager.server;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;

import ar.com.dcsys.auth.shiro.SecurityUtils;
import ar.com.dcsys.gwt.manager.server.handler.MethodHandler;
import ar.com.dcsys.gwt.manager.server.handler.MethodHandlersDetection;
import ar.com.dcsys.gwt.manager.shared.message.Message;
import ar.com.dcsys.gwt.manager.shared.message.MessageFactory;
import ar.com.dcsys.gwt.messages.server.MessageContext;
import ar.com.dcsys.gwt.messages.server.cdi.HandlersContainer;
import ar.com.dcsys.gwt.messages.server.handlers.MessageHandler;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.vm.AutoBeanFactorySource;

public class MainMethodsHandler implements MessageHandler {

	private static Logger logger = Logger.getLogger(MainMethodsHandler.class.getName());
	
	/**
	 * Se registra dentro de los habdlers de los mensajes.
	 * Asi cuanod llegan mensajes es llamado para manejarlos.
	 * Si detecta mensajes válidos entonces llama a cada uno de los handlers de los métodos.
	 * @param handlers
	 */
	private void register(@Observes HandlersContainer<MessageHandler> handlers) {
		handlers.add(this);
	}
	
	private final MessageFactory messageFactory = AutoBeanFactorySource.create(MessageFactory.class);
	private final List<MethodHandler> handlers = new ArrayList<>();
	
	@Inject
	public MainMethodsHandler(MethodHandlersDetection mhd) {
		List<MethodHandler> handlers = mhd.discover();
		for (MethodHandler mh : handlers) {
			logger.info("Registering Method Handler : " + mh.getClass().getName());
		}
		
		this.handlers.addAll(handlers);
	}
	
	
	@Override
	public boolean handle(String id, String msg, MessageContext ctx) {
		
		Message dmsg = null;
		try {
			AutoBean<Message> message = AutoBeanCodex.decode(messageFactory, Message.class, msg);
			dmsg = message.as();
		} catch (Exception e) {
			// no se pudo decodificar el mensaje, por ahi esta en otro formato que no se entiende. asi que se retorna false ya que no se lo manejo.
			return false;
		}
		
		if (dmsg != null) {
			registerShiro(ctx);
		}
		
		
		for (MethodHandler mh : handlers) {
			if (mh.process(id, dmsg, ctx)) {
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * crea el subject para que las funciones usando SecurityUtils de shiro funcionen.
	 * trata de registrarlo. si existe alguna exception queire decir que no se esta usando shiro del lado del cliente.
	 * @param ctx
	 */
	private void registerShiro(MessageContext ctx) {
		try {
			HttpSession session = ctx.getHttpSession();
			Subject subject = SecurityUtils.getSubject(session);
			ThreadContext.bind(subject);
			
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage() ,e);
		}
	}

}
