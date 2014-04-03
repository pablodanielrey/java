package ar.com.dcsys.gwt.person.client.activity;

import javax.inject.Inject;

import ar.com.dcsys.gwt.person.client.gin.AssistedInjectionFactory;
import ar.com.dcsys.gwt.person.client.place.MailChangePlace;
import ar.com.dcsys.gwt.person.client.place.ManagePersonsPlace;
import ar.com.dcsys.gwt.person.client.place.PersonReportPlace;
import ar.com.dcsys.gwt.person.client.place.UpdatePersonDataPlace;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;

public class PersonActivityMapper implements ActivityMapper {

	private final AssistedInjectionFactory factory;
	
	@Inject
	public PersonActivityMapper(AssistedInjectionFactory factory) {
		this.factory = factory;
	}
	
	@Override
	public Activity getActivity(Place place) {
		
		if (place instanceof ManagePersonsPlace) {
			return factory.managePersonsActivity((ManagePersonsPlace)place);
		}
		
		if (place instanceof UpdatePersonDataPlace) {
			//return factory.updatePersonDataActivity((UpdatePersonDataPlace)place);
			return factory.loggedPersonActivity((UpdatePersonDataPlace)place);
		}
		
		if (place instanceof MailChangePlace) {
			return factory.mailChangeActivity((MailChangePlace)place);
		}
		
		if (place instanceof PersonReportPlace) {
			return factory.personReportActivity((PersonReportPlace)place);
		}
		
		return null;
		
	}
	
}
