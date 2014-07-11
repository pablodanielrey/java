package ar.com.dcsys.gwt.menu.client.gin;

import ar.com.dcsys.gwt.menu.client.activity.MenuActivity;
import ar.com.dcsys.gwt.menu.client.place.MenuPlace;


public interface AssistedInjectionFactory {
	public MenuActivity menuActivity(MenuPlace place);
}
