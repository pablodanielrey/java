package ar.com.dcsys.gwt.person.client.modules;

import ar.com.dcsys.data.person.Person;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.SingleSelectionModel;

public interface PersonModule {
	
	public interface Activity {
		public void start();
		public void stop();
		public void accept();
		public void setSelectionModel(SingleSelectionModel<Person> selection);
	}
	
	public interface View extends IsWidget {
	}
	
	public Activity getActivity();
	public View getView();
	
}
