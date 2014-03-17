package ar.com.dcsys.gwt.assistance.client.activity.auth;


import ar.com.dcsys.gwt.assistance.client.place.PinAuthDataPlace;
import ar.com.dcsys.gwt.assistance.client.ui.auth.PinAuthDataView;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class PinAuthDataActivity extends AbstractActivity implements PinAuthDataView.Presenter {

	private final PinAuthDataView view;
	
	@Inject
	public PinAuthDataActivity(PinAuthDataView view, @Assisted PinAuthDataPlace place) {
		this.view = view;
	} 
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		view.setPresenter(this);
		view.clear();
		panel.setWidget(view);
	}
	
	@Override
	public void onStop() {
		view.setPresenter(null);
		super.onStop();
	}
	
	@Override
	public void persist() {

		
	}
	
}
