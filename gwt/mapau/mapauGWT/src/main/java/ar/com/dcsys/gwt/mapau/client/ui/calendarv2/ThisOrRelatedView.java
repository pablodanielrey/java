package ar.com.dcsys.gwt.mapau.client.ui.calendarv2;

import com.google.gwt.user.client.ui.IsWidget;

public interface ThisOrRelatedView extends IsWidget {

	public void setPresenter(Presenter p);
	public void clear();
	
	
	
	public interface Presenter {
		public void commit(boolean v);
	}
	
	
}
