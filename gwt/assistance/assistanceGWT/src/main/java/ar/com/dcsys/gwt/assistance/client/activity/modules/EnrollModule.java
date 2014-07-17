package ar.com.dcsys.gwt.assistance.client.activity.modules;

import javax.inject.Inject;

import ar.com.dcsys.gwt.person.client.modules.PersonModule;

public class EnrollModule implements PersonModule {

	private Activity activity;
	private View view;
	
	@Inject
	public EnrollModule(EnrollActivity activity) {
		this.activity = activity;
		this.view = activity.getView();
	}
	
	@Override
	public Activity getActivity() {
		return activity;
	}

	@Override
	public View getView() {
		return view;
	}

}
