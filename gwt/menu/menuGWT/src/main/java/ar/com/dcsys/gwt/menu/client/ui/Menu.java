package ar.com.dcsys.gwt.menu.client.ui;

import ar.com.dcsys.gwt.menu.client.ui.common.MenuResources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class Menu extends Composite implements MenuView {

	private static MenuUiBinder uiBinder = GWT.create(MenuUiBinder.class);
	
	private final MenuResources resources = GWT.create(MenuResources.class);
	@UiField MenuViewResources res;

	interface MenuUiBinder extends UiBinder<Widget, Menu> {
	}
	

	public Menu() {
		createPerson();
		createAssistance();
		createLogout();
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	private Presenter presenter;
	
	@Override
	public void setPresenter(Presenter p) {
		this.presenter = p;
	}
	
	@UiField(provided=true) Label person;
	@UiField(provided=true) FlowPanel personImageContainer;
	@UiField(provided=true) FlowPanel panelPerson;
	
	private void createPerson() {
		person = new Label();		
		person.setText("Administración de Personas");
		
		ClickHandler clickHandler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (presenter != null) {
					presenter.person();
				}
			}
		};
		panelPerson = new FlowPanel();
		panelPerson.addDomHandler(clickHandler, ClickEvent.getType());
		
		
		personImageContainer = new FlowPanel();
		Image img = new Image(resources.icoPerson());
		personImageContainer.add(img);
		img.addStyleName(res.style().imgPerson());
		
	}
	
	@UiField(provided=true) Label assistance;
	@UiField(provided=true) FlowPanel assistanceImageContainer;
	@UiField(provided=true) FlowPanel panelAssistance;
	
	private void createAssistance() {
		assistance = new Label();		
		assistance.setText("Sistema de Asistencia");
		
		ClickHandler clickHandler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (presenter != null) {
					presenter.assistance();
				}
			}
		};
		panelAssistance = new FlowPanel();
		panelAssistance.addDomHandler(clickHandler, ClickEvent.getType());
		
		assistanceImageContainer = new FlowPanel();
		Image img = new Image(resources.icoAssistance());
		assistanceImageContainer.add(img);
		img.addStyleName(res.style().imgAssistance());
		
	}
	
	@UiField(provided=true) Label logout;
	@UiField(provided=true) FlowPanel logoutImageContainer;
	@UiField(provided=true) FlowPanel panelLogout;
	
	private void createLogout() {
		logout = new Label();		
		logout.setText("Salir");
		
		ClickHandler clickHandler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (presenter != null) {
					presenter.logout();
				}
			}
		};
		panelLogout = new FlowPanel();
		panelLogout.addDomHandler(clickHandler, ClickEvent.getType());
		
		
		logoutImageContainer = new FlowPanel();
		Image img = new Image(resources.icoLogout());
		logoutImageContainer.add(img);
		img.addStyleName(res.style().imgLogout());
		
	}


}
