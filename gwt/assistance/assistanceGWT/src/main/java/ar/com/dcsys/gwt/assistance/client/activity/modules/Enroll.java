package ar.com.dcsys.gwt.assistance.client.activity.modules;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class Enroll extends Composite implements EnrollView {

	private static EnrollUiBinder uiBinder = GWT.create(EnrollUiBinder.class);

	interface EnrollUiBinder extends UiBinder<Widget, Enroll> {
	}
	
	@UiField Button enroll;
	
	private Presenter p;

	public Enroll() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiHandler("enroll")
	public void onEnroll(ClickEvent event) {
		if (p == null) {
			return;
		}
		p.enroll();
	}

	@Override
	public void setPresenter(Presenter p) {
		this.p = p;
	}
	
}
