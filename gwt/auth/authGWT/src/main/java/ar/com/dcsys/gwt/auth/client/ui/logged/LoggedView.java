package ar.com.dcsys.gwt.auth.client.ui.logged;

import com.google.gwt.user.client.ui.IsWidget;

public interface LoggedView extends IsWidget {

	public void clear();
	public void setUser(String username);
	
}
