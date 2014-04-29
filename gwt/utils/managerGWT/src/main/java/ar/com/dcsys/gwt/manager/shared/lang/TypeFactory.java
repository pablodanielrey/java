package ar.com.dcsys.gwt.manager.shared.lang;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;

public interface TypeFactory extends AutoBeanFactory {

	AutoBean<Boolean> getBoolean(Boolean b);
	AutoBean<Integer> getInteger(Integer i);
	AutoBean<Long> getLong(Long l);
	AutoBean<String> getString(String s);
	
	
}
