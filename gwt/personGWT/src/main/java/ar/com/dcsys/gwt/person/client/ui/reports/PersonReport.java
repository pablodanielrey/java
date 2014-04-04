package ar.com.dcsys.gwt.person.client.ui.reports;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class PersonReport extends Composite implements PersonReportView {

	private static PersonReportUiBinder uiBinder = GWT.create(PersonReportUiBinder.class);

	interface PersonReportUiBinder extends UiBinder<Widget, PersonReport> {
	}

	private Presenter p;
	
	@UiField VerticalPanel reports;
	
	public PersonReport() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setPresenter(Presenter p) {
		this.p = p;
	}

	@Override
	public void clear() {
		reports.clear();
	}

	@Override
	public void addReport(String url) {
		reports.add(new Anchor(url, "/documentWeb/documents?t=" + url));
	}
	
	@UiHandler("generate")
	public void onGenerate(ClickEvent event) {
		if (p == null) {
			return;
		}
		p.generateReport();
	}
	
	@UiHandler("update")
	public void onUpdate(ClickEvent event) {
		if (p == null) {
			return;
		}
		p.updateReports();
	}

	
}

