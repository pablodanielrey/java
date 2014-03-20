package ar.com.dcsys.gwt.assistance.client.ui.menu;

import ar.com.dcsys.gwt.assistance.client.place.PinAuthDataPlace;

import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.RootPanel;

public class Menu {
	
	private final PlaceController ctrl;
	
	public Menu(PlaceController ctrl) {
		this.ctrl = ctrl;
	}
	
	public void attachMenu(RootPanel panel) {
		
		MenuBar menu = new MenuBar();
		
		menu.addItem(new MenuItem("Pin",false,new ScheduledCommand() {
			@Override
			public void execute() {
				ctrl.goTo(new PinAuthDataPlace());
			}
		}));		
		panel.add(menu);
	}

}
