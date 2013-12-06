package ar.com.dcsys.gwt.mapau.server;

import javax.enterprise.inject.Produces;

import ar.com.dcsys.gwt.mapau.shared.MapauFactory;

import com.google.web.bindery.autobean.vm.AutoBeanFactorySource;


public class MapauFactoryProducer {

	@Produces
	MapauFactory mapauFactory() {
		MapauFactory mapauFactory = AutoBeanFactorySource.create(MapauFactory.class);
		return mapauFactory;
	}

	
}
