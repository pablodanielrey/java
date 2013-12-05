package ar.com.dcsys.gwt.mapau.client.ui.calendarv2.mainCalendar;

import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.AssignClassroomView;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.CreateModifyAppointmentsView;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.DeleteAppointmentsView;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.FiltersView;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.GenerateRepetitionView;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.MainCalendarView;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.MainCalendarView.Presenter;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.OpenAppointmentView;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.ThisOrRelatedView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class MainCalendar extends Composite implements MainCalendarView {

	private static MainCalendarUiBinder uiBinder = GWT.create(MainCalendarUiBinder.class);

	interface MainCalendarUiBinder extends UiBinder<Widget, MainCalendar> {
	}
	
	@UiField MainCalendarViewResources res;

	private MainCalendarView.Presenter presenter;

	@UiField SimplePanel toolbarContainer;
	@UiField SimplePanel calendarContainer;
	PopupPanel filtersDialog;
	PopupPanel createModifyAppDialog;
	PopupPanel deleteAppDialog;
	PopupPanel repeatAppContainer;
	PopupPanel openAppContainer;
	PopupPanel assignClassroomContainer;
	PopupPanel thisOrRelatedContainer;
	
	public MainCalendar() {
		filtersDialog = createDialog();
		createModifyAppDialog = createDialog();
		deleteAppDialog = createDialog();
		repeatAppContainer = createDialog();
		assignClassroomContainer = createDialog();
		thisOrRelatedContainer = createDialog();
		openAppContainer = createPopup();
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	private PopupPanel createDialog() {
		PopupPanel dialog = new PopupPanel();
		dialog.setGlassEnabled(true);
		dialog.setModal(true);
		dialog.setAnimationEnabled(true);
		return dialog;
	}
	
	private PopupPanel createPopup() {
		PopupPanel panel = new PopupPanel(true);
		panel.setAnimationEnabled(true);
		return panel;
	}

	@Override
	public void setPresenter(Presenter p) {
		this.presenter = p;
	}

	@Override
	public AcceptsOneWidget getCreateModifyAppointmentContainer() {
		return createModifyAppDialog;
	}

	@Override
	public AcceptsOneWidget getFiltersContainer() {
		return filtersDialog;
	}

	@Override
	public AcceptsOneWidget getDeleteAppointmentsContainer() {
		return deleteAppDialog;
	}
	
	@Override
	public AcceptsOneWidget getOpenAppointmentsContainer() {
		return openAppContainer;
	}
	
	@Override
	public AcceptsOneWidget getRepeatAppointmentContainer() {
		return repeatAppContainer;
	}

	@Override
	public AcceptsOneWidget getCalendarContainer() {
		return calendarContainer;
	}

	@Override
	public AcceptsOneWidget getToolBarContainer() {
		return toolbarContainer;
	}
	
	@Override
	public AcceptsOneWidget getAssignClassroomContainer() {
		return assignClassroomContainer;
	}
	
	@Override
	public AcceptsOneWidget getThisOrRelatedContainer() {	
		return thisOrRelatedContainer;
	}

	@Override
	public void clear() {
		filtersDialog.hide();
	}

	@Override
	public void showView(String view) {
		if (view == FiltersView.class.getName()) {
			show(filtersDialog);
			return;
		}
		
		if (view == DeleteAppointmentsView.class.getName()) {
			show(deleteAppDialog);
			return;
		}
		
		if (view == CreateModifyAppointmentsView.class.getName()) {
			show(createModifyAppDialog);
			return;
		}	
		
		if (view == OpenAppointmentView.class.getName()) {
			show(openAppContainer);
		}
		
		if (view == GenerateRepetitionView.class.getName()) {
			show(repeatAppContainer);
		}
		
		if (view == AssignClassroomView.class.getName()) {
			show(assignClassroomContainer);
		}
		
		if (view == ThisOrRelatedView.class.getName()) {
			show(thisOrRelatedContainer);
		}
	}
	
	private void show(PopupPanel panel) {
		panel.center();
		panel.show();
	}

	@Override
	public void hideView(String view) {
		if (view == FiltersView.class.getName()) {
			filtersDialog.hide();
			return;
		}
		
		if (view == DeleteAppointmentsView.class.getName()) {
			deleteAppDialog.hide();
			return;
		}
		
		if (view == CreateModifyAppointmentsView.class.getName()) {
			createModifyAppDialog.hide();
			return;
		}	
		
		if (view == OpenAppointmentView.class.getName()) {
			openAppContainer.hide();
		}		
		
		if (view == GenerateRepetitionView.class.getName()) {
			repeatAppContainer.hide();
		}
		
		if (view == AssignClassroomView.class.getName()) {
			assignClassroomContainer.hide();
		}		
		
		if (view == ThisOrRelatedView.class.getName()) {
			thisOrRelatedContainer.hide();
		}
		
	}

}
