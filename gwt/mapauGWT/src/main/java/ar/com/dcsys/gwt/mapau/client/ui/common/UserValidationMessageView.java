package ar.com.dcsys.gwt.mapau.client.ui.common;

import com.google.gwt.user.client.ui.IsWidget;

public interface UserValidationMessageView extends IsWidget {

	public void setPresenter(Presenter presenter);
	public void clear();
	
	public void setMessage(String text);
	public void setConfirmationLabel(String text);
	
	public interface Presenter {
		public void commit();
		public void cancel();
	}
}
