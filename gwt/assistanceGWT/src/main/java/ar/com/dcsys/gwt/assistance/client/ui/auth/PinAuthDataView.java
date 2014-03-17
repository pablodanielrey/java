package ar.com.dcsys.gwt.assistance.client.ui.auth;

import com.google.gwt.user.client.ui.IsWidget;

public interface PinAuthDataView extends IsWidget{
	
	public void clear();
	public void setPresenter(Presenter p);
	
	public void showMessage(String message);
	
	public String getPin();
	
	public interface Presenter {
		public void persist();
	}

}
