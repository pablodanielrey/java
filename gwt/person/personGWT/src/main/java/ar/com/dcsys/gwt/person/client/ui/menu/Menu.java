package ar.com.dcsys.gwt.person.client.ui.menu;

import ar.com.dcsys.gwt.person.client.place.MailChangePlace;
import ar.com.dcsys.gwt.person.client.place.ManagePersonsPlace;
import ar.com.dcsys.gwt.person.client.place.PersonReportPlace;
import ar.com.dcsys.gwt.person.client.place.UpdatePersonDataPlace;

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

		menu.addItem(new MenuItem("Inicio",false,new ScheduledCommand() {
			@Override
			public void execute() {
				Window.open("/menuGWT/", "_self", "");
			}
		}));
		
		menu.addItem(new MenuItem("Datos personales",false,new ScheduledCommand() {
			@Override
			public void execute() {
				ctrl.goTo(new UpdatePersonDataPlace());
			}
		}));
		
		
		/*
		 *  TODO: falta mejorar la pantalla y la funcionalidad.
		 * 
		menu.addItem(new MenuItem("Cambiar e-Mail",false,new ScheduledCommand() {
			@Override
			public void execute() {
				ctrl.goTo(new MailChangePlace());
			}
		}));
		*/		

		menu.addItem(new MenuItem("Administrar personas",false,new ScheduledCommand() {
			@Override
			public void execute() {
				ctrl.goTo(new ManagePersonsPlace());
			}
		}));

		/*
		 * TODO: falta migrar ok la funcionalidad.
		 * 
		menu.addItem(new MenuItem("Reporte de personas",false,new ScheduledCommand() {
			@Override
			public void execute() {
				ctrl.goTo(new PersonReportPlace());
			}
		}));
		*/

		menu.addItem(new MenuItem("Salir",false,new ScheduledCommand() {
			@Override
			public void execute() {
				Window.open("/authGWT/logout", "_self", "");
			}
		}));

		
		panel.add(menu);
	}

}
