package ar.com.dcsys.gwt.assistance.client.activity;

import javax.inject.Inject;

import ar.com.dcsys.gwt.assistance.client.gin.AssistedInjectionFactory;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;

public class AssistanceActivityMapper implements ActivityMapper {

	private final AssistedInjectionFactory factory;
	
	@Inject
	public AssistanceActivityMapper(AssistedInjectionFactory factory) {
		this.factory = factory;
	}
	
	@Override
	public Activity getActivity(Place place) {
		/*
		if (place instanceof ManagePersonsPlace) {
			return factory.managePersonsActivity((ManagePersonsPlace)place);
		}*/
		
				
		return null;
		
	}

}
