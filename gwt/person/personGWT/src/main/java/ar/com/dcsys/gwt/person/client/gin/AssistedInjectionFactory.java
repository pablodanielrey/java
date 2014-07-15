package ar.com.dcsys.gwt.person.client.gin;

import ar.com.dcsys.gwt.person.client.activity.GroupsActivity;
import ar.com.dcsys.gwt.person.client.activity.LoggedPersonActivity;
import ar.com.dcsys.gwt.person.client.activity.MailChangeActivity;
import ar.com.dcsys.gwt.person.client.activity.UpdatePersonDataActivity;
import ar.com.dcsys.gwt.person.client.activity.manage.ManagePersonsActivity;
import ar.com.dcsys.gwt.person.client.activity.report.PersonReportActivity;
import ar.com.dcsys.gwt.person.client.place.GroupsPlace;
import ar.com.dcsys.gwt.person.client.place.MailChangePlace;
import ar.com.dcsys.gwt.person.client.place.ManagePersonsPlace;
import ar.com.dcsys.gwt.person.client.place.PersonReportPlace;
import ar.com.dcsys.gwt.person.client.place.UpdatePersonDataPlace;


public interface AssistedInjectionFactory {
	public UpdatePersonDataActivity updatePersonDataActivity(UpdatePersonDataPlace place);
	public ManagePersonsActivity managePersonsActivity(ManagePersonsPlace place);
	public LoggedPersonActivity loggedPersonActivity(UpdatePersonDataPlace place);
	public MailChangeActivity mailChangeActivity(MailChangePlace place);
	public PersonReportActivity personReportActivity(PersonReportPlace place);
	public GroupsActivity groupsActivity(GroupsPlace place);
}
