package ar.com.dcsys.gwt.message.shared;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;

public interface MessageFactory extends AutoBeanFactory {

	public AutoBean<Message> message();
	public AutoBean<Method> method();
	public AutoBean<Event> event();
	
}
