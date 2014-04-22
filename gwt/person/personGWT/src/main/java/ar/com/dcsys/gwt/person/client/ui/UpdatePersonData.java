package ar.com.dcsys.gwt.person.client.ui;

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
	@UiField FlowPanel assistancePersonData;
	@UiField Button accept;
	
	private Presenter presenter;
	
	private final AcceptsOneWidgetAdapter aowaBasicPersonData;
	private final AcceptsOneWidgetAdapter aowaAssistancePersonData;
	
	public UpdatePersonData() {
		initWidget(uiBinder.createAndBindUi(this));
		
		aowaBasicPersonData = new AcceptsOneWidgetAdapter(basicPersonData);
		aowaAssistancePersonData = new AcceptsOneWidgetAdapter(assistancePersonData);
	}


	@Override
	public void setPresenter(Presenter p) {
		this.presenter = p;
	}

	@Override
	public AcceptsOneWidget getBasicPersonData() {
		return aowaBasicPersonData;
	}


	@Override
	public AcceptsOneWidget getAssistancePersonData() {
		return aowaAssistancePersonData;
	}
	
	@UiHandler("accept")
	public void onAccept(ClickEvent event) {
		presenter.persist();
	}
	
}
