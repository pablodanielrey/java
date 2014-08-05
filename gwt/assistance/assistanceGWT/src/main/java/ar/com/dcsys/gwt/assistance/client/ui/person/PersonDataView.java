package ar.com.dcsys.gwt.assistance.client.ui.person;

import ar.com.dcsys.data.person.Person;

import com.google.gwt.user.client.ui.IsWidget;

public interface PersonDataView extends IsWidget {

	public void setPresenter(Presenter p);
	
	public interface Presenter {
		public void enroll();
		public void persist();
		public void transferFingerprints();
	}

	public void showMessage(String msg);
	public void setPersonData(Person person);
	
	
}
