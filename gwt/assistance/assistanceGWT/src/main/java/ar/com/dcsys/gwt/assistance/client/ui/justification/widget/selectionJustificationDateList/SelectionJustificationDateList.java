package ar.com.dcsys.gwt.assistance.client.ui.justification.widget.selectionJustificationDateList;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.com.dcsys.data.justification.JustificationDate;
import ar.com.dcsys.gwt.assistance.client.ui.justification.person.JustificationPersonView;
import ar.com.dcsys.gwt.assistance.client.ui.justification.widget.SelectionJustificationDateListWidget;
import ar.com.dcsys.gwt.assistance.client.ui.justification.widget.justificationDateList.JustificationDateList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.MultiSelectionModel;

public class SelectionJustificationDateList extends Composite implements SelectionJustificationDateListWidget{

	private static SelectionJustificationDateListUiBinder uiBinder = GWT.create(SelectionJustificationDateListUiBinder.class);

	interface SelectionJustificationDateListUiBinder extends
			UiBinder<Widget, SelectionJustificationDateList> {
	}

	
	private JustificationPersonView.Presenter p;
	@UiField (provided=true) JustificationDateList justificationDateList;
	
	/*Se comento porque las fechas esta en la vista superior*/
//	@UiField (provided=true) DateBox start;
//	@UiField (provided=true) DateBox end;
	
	
	private final List<JustificationDate> selectedJustificationDates = new ArrayList<JustificationDate>();
	private MultiSelectionModel<JustificationDate> selection;
	
/*	private void createDates() {
		DateTimeFormat defaultF = DateTimeFormat.getFormat(PredefinedFormat.DATE_SHORT);

		start = new DateBox();
		start.setFormat(new DateBox.DefaultFormat(defaultF));
		start.addValueChangeHandler(new ValueChangeHandler<Date>() {
			@Override
			public void onValueChange(ValueChangeEvent<Date> event) {
				Date date = event.getValue();
				initDate(date);
				start.setValue(date);
			}
		});
		
		end = new DateBox();
		end.setFormat(new DateBox.DefaultFormat(defaultF));
		end.addValueChangeHandler(new ValueChangeHandler<Date>() {
			@Override
			public void onValueChange(ValueChangeEvent<Date> event) {
				Date date = event.getValue();
				endDate(date);
				end.setValue(date);
			}
		});		
		
	}	*/
	
	public SelectionJustificationDateList() {
		justificationDateList = new JustificationDateList();
	//	createDates();
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void clear() {
		if (selection != null) {
			selection.clear();
		}
		selectedJustificationDates.clear();
		justificationDateList.clear();
	/*	Date dateE = new Date();
		endDate(dateE);
		end.setValue(dateE);
		Date dateS = new Date();
		initDate(dateS);
		start.setValue(dateS);*/
	}
	
	private void initDate(Date date){
		date.setHours(0);
		date.setMinutes(0);
		date.setSeconds(0);
	}
	
	private void endDate(Date date){
		date.setHours(23);
		date.setMinutes(59);
		date.setSeconds(59);
	}

	@Override
	public void setSelectionModel (MultiSelectionModel<JustificationDate> selection) {
		justificationDateList.setSelectionModel(selection);
	}

	@Override
	public void setListSelectionModel (MultiSelectionModel<JustificationDate> selection) {
		this.selection = selection;
	}

	@Override
	public void setJustificationDates (List<JustificationDate> justificationDates) {
		this.justificationDateList.setJustificationDates(justificationDates);
	}
	
	/*@UiHandler("search")
	public void onSearch (ClickEvent event){
		p.findBy(start.getValue(), end.getValue());
	}*/
	
	@Override
	public void search(Date start, Date end){
		p.findBy(start,end);
	}
	
	
	@Override
	public void setPresenter (JustificationPersonView.Presenter p){
		this.p = p;
	}


}
