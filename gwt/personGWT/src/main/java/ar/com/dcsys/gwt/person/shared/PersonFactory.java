package ar.com.dcsys.gwt.person.shared;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;

public interface PersonFactory extends AutoBeanFactory {
	
	AutoBean<PersonProxy> person();
	AutoBean<PersonList> personList();
	
}
