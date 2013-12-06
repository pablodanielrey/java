package ar.com.dcsys.gwt.mapau.client.gin;

import ar.com.dcsys.gwt.mapau.client.manager.ClassRoomsManager;
import ar.com.dcsys.gwt.mapau.client.manager.ClassRoomsManagerBean;
import ar.com.dcsys.gwt.mapau.client.manager.MapauManager;
import ar.com.dcsys.gwt.mapau.client.manager.MapauManagerBean;
import ar.com.dcsys.gwt.mapau.client.manager.SilegManager;
import ar.com.dcsys.gwt.mapau.client.manager.SilegManagerBean;
import ar.com.dcsys.gwt.mapau.shared.ClassRoomsEncoderDecoder;
import ar.com.dcsys.gwt.mapau.shared.MapauEncoderDecoder;
import ar.com.dcsys.gwt.mapau.shared.MapauFactory;
import ar.com.dcsys.gwt.mapau.shared.SilegEncoderDecoder;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;

public class MapauModelGinModule extends AbstractGinModule {

	@Override
	protected void configure() {
		
		bind(MapauEncoderDecoder.class).in(Singleton.class);
		bind(SilegEncoderDecoder.class).in(Singleton.class);
		bind(ClassRoomsEncoderDecoder.class).in(Singleton.class);
		
		bind(MapauFactory.class);
		
		bind(ClassRoomsManager.class).to(ClassRoomsManagerBean.class).in(Singleton.class);
		bind(MapauManager.class).to(MapauManagerBean.class).in(Singleton.class);
		bind(SilegManager.class).to(SilegManagerBean.class).in(Singleton.class);
		
	}
	
}
