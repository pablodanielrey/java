package ar.com.dcsys.gwt.manager.shared.message;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;

public interface MessageFactory extends AutoBeanFactory {

	AutoBean<Message> getMessage();
	AutoBean<Message> getMessage(Message m);
	
}
