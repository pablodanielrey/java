package ar.com.dcsys.gwt.manager.shared.lang;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;

public interface TypeFactory extends AutoBeanFactory {

	AutoBean<BooleanContainer> getBoolean();
	AutoBean<IntegerContainer> getInteger();
	AutoBean<LongContainer> getLong();
	AutoBean<StringContainer> getString();
	
	AutoBean<StringListContainer> getStringListContainer();
	AutoBean<StringListContainer> getStringListContainer(StringListContainer slc);
	
}
