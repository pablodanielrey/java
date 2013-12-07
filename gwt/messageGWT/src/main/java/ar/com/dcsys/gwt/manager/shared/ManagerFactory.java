package ar.com.dcsys.gwt.manager.shared;

import ar.com.dcsys.gwt.manager.shared.list.DateList;
import ar.com.dcsys.gwt.manager.shared.primitive.BooleanContainer;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;

public interface ManagerFactory extends AutoBeanFactory {

	public AutoBean<BooleanContainer> booleanContainer();
	public AutoBean<DateList> dateList();
	
	
}
