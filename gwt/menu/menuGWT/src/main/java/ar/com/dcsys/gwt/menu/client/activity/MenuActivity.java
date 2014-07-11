package ar.com.dcsys.gwt.menu.client.activity;

import ar.com.dcsys.gwt.clientMessages.client.MessageDialogEvent;
import ar.com.dcsys.gwt.menu.client.place.MenuPlace;
import ar.com.dcsys.gwt.menu.client.ui.MenuView;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class MenuActivity extends AbstractActivity implements MenuView.Presenter {

	private final MenuView view;
	private EventBus eventBus;
	
	@Inject
	public MenuActivity(MenuView view, @Assisted MenuPlace place) {
		this.view = view;
	}
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		
		this.eventBus = eventBus;
		panel.setWidget(view);		
		view.setPresenter(this);
		
	}
	
	private void showMessage(String msg) {
		eventBus.fireEvent(new MessageDialogEvent(msg));
	}

	private void openUrl(String url) {
		Window.open(url, "_self", "");		
	}
	
	@Override
	public void person() {
		openUrl("/personGWT/");
	}

	@Override
	public void assistance() {
		openUrl("/assistanceGWT/");
	}

	@Override
	public void logout() {
		openUrl("/logout");
	}

	@Override
	public void auth() {
		openUrl("/authGWT/");
	}

	
}
