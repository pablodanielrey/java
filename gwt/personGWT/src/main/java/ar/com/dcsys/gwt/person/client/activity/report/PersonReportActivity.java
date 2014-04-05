package ar.com.dcsys.gwt.person.client.activity.report;

import java.util.List;

import ar.com.dcsys.data.document.Document;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.person.client.manager.PersonsManager;
import ar.com.dcsys.gwt.person.client.place.PersonReportPlace;
import ar.com.dcsys.gwt.person.client.ui.reports.PersonReportView;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class PersonReportActivity extends AbstractActivity implements PersonReportView.Presenter {

	private final PersonsManager personsManager;
	private final PersonReportView view;
	
	@Inject
	public PersonReportActivity(PersonsManager personsManager, PersonReportView view, @Assisted PersonReportPlace place) {
		this.personsManager = personsManager;
		this.view = view;
	}
			
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		view.setPresenter(this);
		view.clear();
		
		panel.setWidget(view);
	}

	@Override
	public void onStop() {
		view.clear();
		view.setPresenter(null);
	}

	@Override
	public void generateReport() {
		personsManager.report(new Receiver<Document>() {
			@Override
			public void onSuccess(Document t) {
				view.addReport(t);
			}
			@Override
			public void onFailure(Throwable t) {

			}
		});
	}


	@Override
	public void updateReports() {
		view.clear();
		personsManager.findAllReports(new Receiver<List<Document>>() {
			@Override
			public void onFailure(Throwable t) {
				
			}
			public void onSuccess(List<Document> t) {
				view.clear();
				if (t == null) {
					return;
				}
				for (Document s : t) {
					view.addReport(s);
				}
			};
		});
	}
	
}
