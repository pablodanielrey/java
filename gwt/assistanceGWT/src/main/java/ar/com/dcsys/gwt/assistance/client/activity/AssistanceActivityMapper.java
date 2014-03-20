package ar.com.dcsys.gwt.assistance.client.activity;

import javax.inject.Inject;

import ar.com.dcsys.gwt.assistance.client.gin.AssistedInjectionFactory;
import ar.com.dcsys.gwt.assistance.client.place.PinAuthDataPlace;

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
		
		if (place instanceof PinAuthDataPlace) {
			return factory.pinAuthDataActivity((PinAuthDataPlace)place);
		}
		
				
		return null;
		
	}

}
