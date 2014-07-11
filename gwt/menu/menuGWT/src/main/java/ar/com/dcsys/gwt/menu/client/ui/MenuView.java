package ar.com.dcsys.gwt.menu.client.ui;

import com.google.gwt.user.client.ui.IsWidget;

public interface MenuView extends IsWidget {

	public void setPresenter(Presenter p);
	
	public interface Presenter {
		public void person();
		public void assistance();
		public void logout();
		public void auth();
	}
}
