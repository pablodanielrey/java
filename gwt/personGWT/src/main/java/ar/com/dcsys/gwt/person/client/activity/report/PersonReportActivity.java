package ar.com.dcsys.gwt.person.client.activity.report;

import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.person.client.manager.PersonsManager;
import ar.com.dcsys.gwt.person.client.place.PersonReportPlace;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class PersonReportActivity extends AbstractActivity {

	private final PersonsManager personsManager;
	
	@Inject
	public PersonReportActivity(PersonsManager personsManager, @Assisted PersonReportPlace place) {
		this.personsManager = personsManager;
	}
			
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		personsManager.report(new Receiver<String>() {
			@Override
			public void onSuccess(String t) {
				String finalUrl = "personReport?t=" + t; 
				Window.open(finalUrl, "_blank", null);
			}
			@Override
			public void onFailure(Throwable t) {

			}
		});
	}
	
}
