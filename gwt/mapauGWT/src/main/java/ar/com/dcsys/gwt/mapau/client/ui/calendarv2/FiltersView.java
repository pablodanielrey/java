package ar.com.dcsys.gwt.mapau.client.ui.calendarv2;

import java.util.List;

import ar.com.dcsys.gwt.mapau.shared.data.filter.FilterType;
import ar.com.dcsys.gwt.mapau.shared.data.filter.FilterValue;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.MultiSelectionModel;

public interface FiltersView extends IsWidget {

	public void setPresenter(Presenter p);
	public void clear();	
	
	public void setSelectionModel(MultiSelectionModel<FilterValue<?>> selection);
	public void setFilters(List<FilterType<?>> filters); 
	
	public interface Presenter {
		public void showView(String view);
		public void hideView(String view);		
		public void update();
	}
	
}
