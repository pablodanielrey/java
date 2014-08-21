package ar.com.dcsys.gwt.assistance.client.activity;

import javax.inject.Inject;

import ar.com.dcsys.gwt.assistance.client.gin.AssistedInjectionFactory;
import ar.com.dcsys.gwt.assistance.client.place.DailyPeriodsPlace;
import ar.com.dcsys.gwt.assistance.client.place.GeneralsJustificationPlace;
import ar.com.dcsys.gwt.assistance.client.place.JustificationPersonPlace;
import ar.com.dcsys.gwt.assistance.client.place.ManageJustificationPlace;
import ar.com.dcsys.gwt.assistance.client.place.ManagePersonsPlace;
import ar.com.dcsys.gwt.assistance.client.place.PeriodsAssignationPersonPlace;
import ar.com.dcsys.gwt.assistance.client.place.PeriodsPlace;
import ar.com.dcsys.gwt.assistance.client.place.PinAuthDataPlace;
import ar.com.dcsys.gwt.assistance.client.place.SyncLogsPlace;

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
		
		if (place instanceof SyncLogsPlace) {
			return factory.syncLogsActivity((SyncLogsPlace)place);
		}
		
		if (place instanceof PinAuthDataPlace) {
			return factory.pinAuthDataActivity((PinAuthDataPlace)place);
		}
		
		if (place instanceof DailyPeriodsPlace) {
			return factory.dailyPeriodsActivity((DailyPeriodsPlace)place);
		}
		
		if (place instanceof GeneralsJustificationPlace) {
			return factory.generalsJustificationActivity((GeneralsJustificationPlace)place);
		}
		
		if (place instanceof JustificationPersonPlace) {
			return factory.justificationPersonActivity((JustificationPersonPlace)place);
		}
		
		if (place instanceof ManageJustificationPlace) {
			return factory.manageJustificationsActivity((ManageJustificationPlace)place);
		}
		
		if (place instanceof PeriodsAssignationPersonPlace) {
			return factory.periodsAssignationPersonActivity((PeriodsAssignationPersonPlace) place);
		}
		
		if (place instanceof PeriodsPlace) {
			return factory.periodsActivity((PeriodsPlace) place);
		}
		
		if (place instanceof ManagePersonsPlace) {
			return factory.managePersons((ManagePersonsPlace)place);
		}
		
		return null;
		
	}

}
