package ar.com.dcsys.gwt.assistance.client.ui.menu;

import ar.com.dcsys.gwt.assistance.client.place.DailyPeriodsPlace;
import ar.com.dcsys.gwt.assistance.client.place.GeneralsJustificationPlace;
import ar.com.dcsys.gwt.assistance.client.place.JustificationPersonPlace;
import ar.com.dcsys.gwt.assistance.client.place.ManageJustificationPlace;
import ar.com.dcsys.gwt.assistance.client.place.ManagePersonsPlace;
import ar.com.dcsys.gwt.assistance.client.place.PeriodsAssignationPersonPlace;
import ar.com.dcsys.gwt.assistance.client.place.PeriodsPlace;
import ar.com.dcsys.gwt.assistance.client.place.SyncLogsPlace;

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

		
		menu.addItem(new MenuItem("Actualizar Logs",false,new ScheduledCommand() {
			@Override
			public void execute() {
				ctrl.goTo(new SyncLogsPlace());
			}
		}));

		
		/*
		menu.addItem(new MenuItem("Pin",false,new ScheduledCommand() {
			@Override
			public void execute() {
				ctrl.goTo(new PinAuthDataPlace());
			}
		}));
		*/	
		
		menu.addItem(new MenuItem("Administrar Usuarios",false,new ScheduledCommand() {
			@Override
			public void execute() {
				ctrl.goTo(new ManagePersonsPlace());
			}
		}));
		
		menu.addItem(new MenuItem("Justificaciones Generales",false,new ScheduledCommand() {
			@Override
			public void execute() {
				ctrl.goTo(new GeneralsJustificationPlace());
			}
		}));	
		
		menu.addItem(new MenuItem("Justificaciones Personales",false,new ScheduledCommand() {
			@Override
			public void execute() {
				ctrl.goTo(new JustificationPersonPlace());
			}
		}));	
		
		menu.addItem(new MenuItem("Admistración de Justificaciones",false,new ScheduledCommand() {
			@Override
			public void execute() {
				ctrl.goTo(new ManageJustificationPlace());
			}
		}));	
		
		menu.addItem(new MenuItem("Periodos",false,new ScheduledCommand() {
			@Override
			public void execute() {
				ctrl.goTo(new PeriodsPlace());
			}
		}));	
		
		menu.addItem(new MenuItem("Periodo Diario",false,new ScheduledCommand() {
			@Override
			public void execute() {
				ctrl.goTo(new DailyPeriodsPlace());
			}
		}));

		menu.addItem(new MenuItem("Asignación de Periodos",false,new ScheduledCommand() {
			@Override
			public void execute() {
				ctrl.goTo(new PeriodsAssignationPersonPlace());
			}
		}));
		

		menu.addItem(new MenuItem("Salir",false,new ScheduledCommand() {
			@Override
			public void execute() {
				Window.open("/authGWT/logout", "_self", "");
			}
		}));
		
		panel.add(menu);
	}

}
