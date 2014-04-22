package ar.com.dcsys.gwt.assistance.server;

import ar.com.dcsys.gwt.assistance.shared.AssistanceFactory;

import javax.enterprise.inject.Produces;

import com.google.web.bindery.autobean.vm.AutoBeanFactorySource;

public class AssistanceFactoryProducer {
	
	@Produces
	public AssistanceFactory assistanceServerFactory() {
		AssistanceFactory assistanceFactory = AutoBeanFactorySource.create(AssistanceFactory.class);
		return assistanceFactory;
	}

}
