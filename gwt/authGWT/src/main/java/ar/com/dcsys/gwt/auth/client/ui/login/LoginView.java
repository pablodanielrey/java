package ar.com.dcsys.gwt.auth.client.ui.login;

import com.google.gwt.user.client.ui.IsWidget;

public interface LoginView extends IsWidget {
	
	
	public void clear(); 
	public void setPresenter(Presenter presenter);
	public String getUser();
	public String getPassword();
	
	public interface Presenter {
		public void login();
	}
	
}
