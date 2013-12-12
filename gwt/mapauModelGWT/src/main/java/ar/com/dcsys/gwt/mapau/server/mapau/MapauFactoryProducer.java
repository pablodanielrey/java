package ar.com.dcsys.gwt.mapau.server.mapau;

import javax.enterprise.inject.Produces;

import ar.com.dcsys.gwt.mapau.shared.ClassRoomsFactory;
import ar.com.dcsys.gwt.mapau.shared.MapauFactory;
import ar.com.dcsys.gwt.mapau.shared.SilegFactory;

import com.google.web.bindery.autobean.vm.AutoBeanFactorySource;


public class MapauFactoryProducer {

	@Produces
	MapauFactory mapauFactory() {
		MapauFactory mapauFactory = AutoBeanFactorySource.create(MapauFactory.class);
		return mapauFactory;
	}
	
	@Produces
	ClassRoomsFactory classRoomsFactory() {
		ClassRoomsFactory classRoomsFactory = AutoBeanFactorySource.create(ClassRoomsFactory.class);
		return classRoomsFactory;
	}

	@Produces
	SilegFactory silegFactory() {
		SilegFactory silegFactory = AutoBeanFactorySource.create(SilegFactory.class);
		return silegFactory;
	}
	
}
