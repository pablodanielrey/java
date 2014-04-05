package ar.com.dcsys.gwt.person.server;

import javax.enterprise.inject.Produces;

import ar.com.dcsys.gwt.person.shared.DocumentFactory;

import com.google.web.bindery.autobean.vm.AutoBeanFactorySource;

public class DocumentFactoryProducer {

	@Produces
	public DocumentFactory documentServerFactory() {
		DocumentFactory documentFactory = AutoBeanFactorySource.create(DocumentFactory.class);
		return documentFactory;
	}
	
}
