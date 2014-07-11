package ar.com.dcsys.gwt.menu.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class Menu extends Composite implements MenuView {

	private static MenuUiBinder uiBinder = GWT.create(MenuUiBinder.class);

	interface MenuUiBinder extends UiBinder<Widget, Menu> {
	}
	

	public Menu() {
		createPerson();
		createAssistance();
		createLogout();
		createAuth();
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	private Presenter presenter;
	
	@Override
	public void setPresenter(Presenter p) {
		this.presenter = p;
	}
	
	@UiField(provided=true) Label person;
	private void createPerson() {
		person = new Label();
		person.setText("Administración de Personas");
		person.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (presenter != null) {
					presenter.person();
				}
			}
		});
	}
	
	@UiField(provided=true) Label assistance;
	private void createAssistance() {
		person = new Label();
		person.setText("Sistema de Asistencia");
		person.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (presenter != null) {
					presenter.assistance();
				}
			}
		});
	}
	
	@UiField(provided=true) Label logout;
	private void createLogout() {
		person = new Label();
		person.setText("Salir");
		person.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (presenter != null) {
					presenter.logout();
				}
			}
		});
	}
	
	@UiField(provided=true) Label auth;
	private void createAuth() {
		person = new Label();
		person.setText("Login");
		person.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (presenter != null) {
					presenter.auth();
				}
			}
		});
	}

}
