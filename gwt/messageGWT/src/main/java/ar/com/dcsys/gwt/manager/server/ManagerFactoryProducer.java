package ar.com.dcsys.gwt.manager.server;

import javax.enterprise.inject.Produces;

import com.google.web.bindery.autobean.vm.AutoBeanFactorySource;

import ar.com.dcsys.gwt.manager.shared.ManagerFactory;

public class ManagerFactoryProducer {
	
	@Produces
	public ManagerFactory managerFactory() {
		return AutoBeanFactorySource.create(ManagerFactory.class);
	}
	
}
