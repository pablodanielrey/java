package ar.com.dcsys.gwt.assistance.client.ui.modules;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class PersonData extends Composite {

	private static PersonDataUiBinder uiBinder = GWT
			.create(PersonDataUiBinder.class);

	interface PersonDataUiBinder extends UiBinder<Widget, PersonData> {
	}

	public PersonData() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
