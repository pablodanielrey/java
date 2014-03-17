package ar.com.dcsys.gwt.assistance.client.gin;

import ar.com.dcsys.gwt.assistance.client.activity.AssistanceActivityMapper;
import ar.com.dcsys.gwt.assistance.client.ui.auth.PinAuthData;
import ar.com.dcsys.gwt.assistance.client.ui.auth.PinAuthDataView;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.inject.client.assistedinject.GinFactoryModuleBuilder;
import com.google.inject.Singleton;

public class AssistanceGWTGinModule extends AbstractGinModule {

	@Override
	protected void configure() {

		bind(EventBus.class).to(SimpleEventBus.class).in(Singleton.class);
		
		bind(PinAuthDataView.class).to(PinAuthData.class).in(Singleton.class);
		
		bind(AssistanceActivityMapper.class).in(Singleton.class);
		
		GinFactoryModuleBuilder builder = new GinFactoryModuleBuilder();
		install(builder.build(AssistedInjectionFactory.class));
	}

}
