package ar.com.dcsys.gwt.person.client.gin;

import ar.com.dcsys.gwt.person.client.activity.UpdatePersonDataActivity;
import ar.com.dcsys.gwt.person.client.activity.manage.ManagePersonsActivity;
import ar.com.dcsys.gwt.person.client.place.ManagePersonsPlace;
import ar.com.dcsys.gwt.person.client.place.UpdatePersonDataPlace;


public interface AssistedInjectionFactory {
	public UpdatePersonDataActivity updatePersonDataActivity(UpdatePersonDataPlace place);
	public ManagePersonsActivity managePersonsActivity(ManagePersonsPlace place);
}
