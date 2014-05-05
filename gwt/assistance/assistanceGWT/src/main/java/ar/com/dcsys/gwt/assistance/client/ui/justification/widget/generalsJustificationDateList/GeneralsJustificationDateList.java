package ar.com.dcsys.gwt.assistance.client.ui.justification.widget.generalsJustificationDateList;



import java.util.List;

import ar.com.dcsys.data.justification.GeneralJustificationDate;
import ar.com.dcsys.data.justification.GeneralJustificationDateProvider;
import ar.com.dcsys.data.justification.Justification;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionModel;

public class GeneralsJustificationDateList extends Composite implements GeneralsJustificationDateListWidget{

	private static JustificationDateListUiBinder uiBinder = GWT.create(JustificationDateListUiBinder.class);

	interface JustificationDateListUiBinder extends	UiBinder<Widget, GeneralsJustificationDateList> {
	}
	
	@UiField(provided=true) DataGrid<GeneralJustificationDate> generalsJustificationDates;

	private GeneralJustificationDateProvider p;
	
	private void createGeneralJustifications() {		

		final DateTimeFormat dateF = DateTimeFormat.getFormat(PredefinedFormat.DATE_SHORT);
		
		TextColumn<GeneralJustificationDate> date = new TextColumn<GeneralJustificationDate>() {
			@Override
			public String getValue(GeneralJustificationDate object) {
				if (object == null) {
					return "nulo";
				}
				return dateF.format(object.getStart());
			}
		};

		TextColumn<GeneralJustificationDate> type = new TextColumn<GeneralJustificationDate>() {
			@Override
			public String getValue(GeneralJustificationDate object) {
				Justification justification = object.getJustification();
				if (object == null || justification == null) {
					return "nulo";
				}
				return justification.getCode();
			}
		};	

		
		TextColumn<GeneralJustificationDate> description= new TextColumn<GeneralJustificationDate>() {
			@Override
			public String getValue(GeneralJustificationDate object) {
				String note = object.getNotes();
				if (object == null || note == null) {
					return "nulo";
				}
				return note;
			}
		};
		
		
		generalsJustificationDates = new DataGrid<GeneralJustificationDate>();
		generalsJustificationDates.addColumn(date,"Fecha");
		generalsJustificationDates.addColumn(type,"Tipo");
		generalsJustificationDates.addColumn(description,"Notas");
		
	}	
	public GeneralsJustificationDateList() {
		createGeneralJustifications();
		initWidget(uiBinder.createAndBindUi(this));
	}
	@Override
	public void setGeneralsJustificationDate(List<GeneralJustificationDate> dates) {
		this.generalsJustificationDates.setRowData(dates);		
	}
	@Override
	public void clear() {
		generalsJustificationDates.setRowCount(0,true);
	}
	@Override
	public void setSelectionModel(SelectionModel<GeneralJustificationDate> selection) {
		generalsJustificationDates.setSelectionModel(selection);
	}

}
