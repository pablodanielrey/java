package ar.com.dcsys.gwt.person.client.ui;

import java.util.List;

import ar.com.dcsys.gwt.person.client.modules.PersonModule;
import ar.com.dcsys.gwt.person.client.modules.PersonPortal;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

public class UpdatePersonData extends Composite implements UpdatePersonDataView {

	private static UpdatePersonDataUiBinder uiBinder = GWT.create(UpdatePersonDataUiBinder.class);

	interface UpdatePersonDataUiBinder extends	UiBinder<Widget, UpdatePersonData> {
	}

	
	@UiField FlowPanel basicPersonData;
	@UiField FlowPanel modules;
	@UiField Button accept;
	
	private Presenter presenter;
	
	private final AcceptsOneWidgetAdapter aowaBasicPersonData;

	
	public UpdatePersonData() {
		initWidget(uiBinder.createAndBindUi(this));
		
		aowaBasicPersonData = new AcceptsOneWidgetAdapter(basicPersonData);
	}


	/**
	 * Carga los módulos cada vez que se setea un presenter en la vista.
	 */
	private void loadModules() {
		List<PersonModule> lmodules = PersonPortal.getPortal().getModules();
		for (PersonModule m : lmodules) {
			modules.add(m.getView());
		}
	}
	
	@Override
	public void setPresenter(Presenter p) {
		this.presenter = p;
		
		modules.clear();
		if (p != null) {
			loadModules();
		}		
	}

	@Override
	public AcceptsOneWidget getBasicPersonData() {
		return aowaBasicPersonData;
	}


	@UiHandler("accept")
	public void onAccept(ClickEvent event) {
		presenter.persist();
	}
	
}
