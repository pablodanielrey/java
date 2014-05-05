package ar.com.dcsys.gwt.mapau.client.ui.common.date.selectionDateList;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.cell.client.DateCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.IdentityColumn;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DateBox.DefaultFormat;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.google.gwt.view.client.MultiSelectionModel;

public class SelectionDateList extends Composite {

	private static SelectionDateListUiBinder uiBinder = GWT.create(SelectionDateListUiBinder.class);

	interface SelectionDateListUiBinder extends UiBinder<Widget, SelectionDateList> {
	}

	private final List<Date> dateList;
	
	private MultiSelectionModel<Date> selection;
	
	@UiField(provided=true) DataGrid<Date> dates;
	@UiField(provided=true) DateBox date;
	
	@UiField(provided=true) TextBox initTimeHours;
	@UiField(provided=true) TextBox initTimeMinutes;
	@UiField(provided=true) TextBox endTimeHours;
	@UiField(provided=true) TextBox endTimeMinutes;	


	private void createDates() {
		createTimes();
		final DateTimeFormat dateF = DateTimeFormat.getFormat(PredefinedFormat.DATE_SHORT);
		
		IdentityColumn<Date> dateC = new IdentityColumn<Date>(new DateCell(dateF));
		dates = new DataGrid<Date>();
		dates.addColumn(dateC,"Fechas");
		
		date = new DateBox(new DatePicker(), new Date(), new DefaultFormat(dateF));
	}	
	
	public SelectionDateList() {
		dateList = new ArrayList<Date>();
		createDates();
		
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiHandler("addDate")
	public void onAddDate(ClickEvent event) {		
		Date date = this.date.getValue();		
		if (date == null) {
			return;
		}
		//Tengo que obtener la hora de inicio y hora fin
		
		dateList.add(date);
		dates.setRowData(dateList);
		
		if (selection != null) {
			selection.setSelected(date, true);
		}
	}

	public void setDates(List<Date> ds) {
		if (ds == null) {
			return;
		}
		dateList.clear();
		dateList.addAll(ds);
		dates.setRowData(dateList);
		
		if (selection != null) {
			selection.clear();
			for (Date d : dateList) {
				selection.setSelected(d,true);
			}
		}
	}
	
	public void setSelectionModel(MultiSelectionModel<Date> selection) {
		this.selection = selection;
		if (dateList.size() > 0) {
			for (Date d : dateList) {
				selection.setSelected(d, true);
			}
		}
	}
	
	
	private void clearSelection() {
		if (selection != null) {
			selection.clear();
		}
	}
	
	public void clear() {
		clearSelection();
		clearTimes();
		dateList.clear();
		dates.setRowData(dateList);
		
		date.setValue(new Date());
	}
	
	private void createTimes() {
		
		KeyPressHandler handler = new KeyPressHandler() {
			@Override
			public void onKeyPress(KeyPressEvent event) {
				Character charCode = event.getCharCode();
				if (!(Character.isDigit(charCode))) {
					TextBox box = (TextBox)event.getSource();
					box.cancelKey();
				}
			}
		};
		
		initTimeHours = new TextBox();
		initTimeHours.setText("00");
		initTimeHours.setMaxLength(2);
		initTimeHours.addKeyPressHandler(handler);
		
		initTimeMinutes = new TextBox();
		initTimeMinutes.setText("00");
		initTimeMinutes.setMaxLength(2);
		initTimeMinutes.addKeyPressHandler(handler);
		
		endTimeHours = new TextBox();
		endTimeHours.setText("00");
		endTimeHours.setMaxLength(2);
		endTimeHours.addKeyPressHandler(handler);
		
		endTimeMinutes = new TextBox();
		endTimeMinutes.setText("00");
		endTimeMinutes.setMaxLength(2);		
		endTimeMinutes.addKeyPressHandler(handler);
	}
	
	private void clearTimes() {
		initTimeHours.setValue("00");
		initTimeMinutes.setValue("00");
		endTimeHours.setValue("00");
		endTimeMinutes.setValue("00");
	}	

	public Long getInitTime() {
		Long hora = new Long(initTimeHours.getValue());
		Long min = new Long(initTimeMinutes.getValue());
		return (hora * 60) + min;
	}	
	public Long getEndTime() {
		Long hora = new Long(endTimeHours.getValue());
		Long min = new Long(endTimeMinutes.getValue());
		return ((hora *60) + min)+getInitTime();
	}	
}

