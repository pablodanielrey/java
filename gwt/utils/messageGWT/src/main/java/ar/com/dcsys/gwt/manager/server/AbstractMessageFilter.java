package ar.com.dcsys.gwt.manager.server;

import javax.enterprise.event.Observes;

import ar.com.dcsys.gwt.message.server.MessageContext;
import ar.com.dcsys.gwt.message.server.filters.MessageFilter;
import ar.com.dcsys.gwt.message.server.filters.MessageFilters;
import ar.com.dcsys.gwt.message.shared.Message;

public abstract class AbstractMessageFilter implements MessageFilter {

	@Override
	public abstract void filter(MessageContext ctx, Message msg);
	
	/**
	 * Se registra como filter cuando es llamado por el evento disparado por CDI
	 * @param mh
	 */
	public void register(@Observes MessageFilters mh) {
		mh.addFilter(this);
	}	
	
}
