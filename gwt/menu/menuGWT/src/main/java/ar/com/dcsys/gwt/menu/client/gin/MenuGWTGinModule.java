package ar.com.dcsys.gwt.menu.client.gin;

import ar.com.dcsys.gwt.menu.client.activity.MenuActivityMapper;
import ar.com.dcsys.gwt.menu.client.ui.Menu;
import ar.com.dcsys.gwt.menu.client.ui.MenuView;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.inject.client.assistedinject.GinFactoryModuleBuilder;
import com.google.inject.Singleton;

public class MenuGWTGinModule extends AbstractGinModule {

	@Override
	protected void configure() {
		
		bind(EventBus.class).to(SimpleEventBus.class).in(Singleton.class);
		
		bind(MenuView.class).to(Menu.class).in(Singleton.class);
		
		bind(MenuActivityMapper.class).in(Singleton.class);
		
		GinFactoryModuleBuilder builder = new GinFactoryModuleBuilder();
		install(builder.build(AssistedInjectionFactory.class));
	}

}
