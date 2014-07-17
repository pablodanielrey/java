package ar.com.dcsys.gwt.assistance.client.activity.modules;

import ar.com.dcsys.gwt.person.client.modules.PersonModule;

public interface EnrollView extends PersonModule.View {

	public void setPresenter(Presenter p);
	
	public interface Presenter {
		public void enroll();
	}
}
