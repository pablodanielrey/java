package ar.com.dcsys.gwt.assistance.client.activity.periods;

import ar.com.dcsys.gwt.assistance.client.ui.period.daily.DailyPeriodsView;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

public class DailyPeriodsActivity extends AbstractActivity implements DailyPeriodsView.Presenter {

	private final DailyPeriodsView view;
	
	@Inject
	public DailyPeriodsActivity(DailyPeriodsView view) {
		this.view = view;
	}
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		view.setPresenter(this);
		panel.setWidget(view);
		
	}

	@Override
	public void onStop() {
		view.setPresenter(null);
		super.onStop();
	}
	
	@Override
	public void justify() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void find() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void export() {
		// TODO Auto-generated method stub
		
	}

}
