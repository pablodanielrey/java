package ar.com.dcsys.gwt.menu.client.activity;

import javax.inject.Inject;

import ar.com.dcsys.gwt.menu.client.gin.AssistedInjectionFactory;
import ar.com.dcsys.gwt.menu.client.place.MenuPlace;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;

public class MenuActivityMapper implements ActivityMapper {

	private final AssistedInjectionFactory factory;
	
	@Inject
	public MenuActivityMapper(AssistedInjectionFactory factory) {
		this.factory = factory;
	}
	
	@Override
	public Activity getActivity(Place place) {
		
		if (place instanceof MenuPlace) {
			return factory.menuActivity((MenuPlace)place);
		}
			
		return null;
		
	}
	
}
