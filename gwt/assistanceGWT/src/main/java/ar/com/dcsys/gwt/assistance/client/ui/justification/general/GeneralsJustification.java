package ar.com.dcsys.gwt.assistance.client.ui.justification.general;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.com.dcsys.data.justification.GeneralJustificationDate;
import ar.com.dcsys.data.justification.GeneralJustificationDateProvider;
import ar.com.dcsys.data.justification.Justification;
import ar.com.dcsys.gwt.assistance.client.ui.justification.widget.generalsJustificationDateList.GeneralsJustificationDateList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DateBox.DefaultFormat;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;

public class GeneralsJustification extends Composite implements GeneralsJustificationView {

	private static GeneralsJustificationUiBinder uiBinder = GWT.create(GeneralsJustificationUiBinder.class);

	interface GeneralsJustificationUiBinder extends	UiBinder<Widget, GeneralsJustification> {
	}

	private Presenter p;
	private GeneralJustificationDateProvider provider;
	
	private List<GeneralJustificationDate> dateList;
	private SingleSelectionModel<Justification> typeSelection;
	@UiField(provided=true) GeneralsJustificationDateList dates;
	@UiField(provided=true) GeneralsJustificationDateList generalsJustificationDate;
	@UiField(provided=true) ValueListBox<Justification> types;
	@UiField(provided=true) DateBox date;
	@UiField(provided=true) DateBox start;
	@UiField(provided=true) DateBox end;
	@UiField TextArea justifyNotes;

	private void createDates() {		
		final DateTimeFormat dateF = DateTimeFormat.getFormat(PredefinedFormat.DATE_SHORT);
		dateList = new ArrayList<GeneralJustificationDate>();
		
		dates = new GeneralsJustificationDateList();
		generalsJustificationDate  = new GeneralsJustificationDateList();
		date = new DateBox(new DatePicker(), new Date(), new DefaultFormat(dateF));
		start = new DateBox(new DatePicker(), new Date(), new DefaultFormat(dateF));
		end = new DateBox(new DatePicker(), new Date(), new DefaultFormat(dateF));
	}	
	
	private void createType() {
		types = new ValueListBox<Justification>(new Renderer<Justification>() {
			private String getValue(Justification type) {
				if (type == null) {
					return "Nulo";
				}
				return type.getCode();
			}
			@Override
			public String render(Justification object) {
				if (object == null) {
					return "";
				}
				return getValue(object);
			}
			@Override
			public void render(Justification object, Appendable appendable) throws IOException {
				if (object == null) {
					return;
				}
				appendable.append(getValue(object));
			}
		});
		
		types.addValueChangeHandler(new ValueChangeHandler<Justification>() {
			@Override
			public void onValueChange(ValueChangeEvent<Justification> event) {
				if (typeSelection != null) {
					Justification t = types.getValue();
					if (t != null) {
						typeSelection.setSelected(t, true);
					}
				}
			}
		});		
	}
	
	public GeneralsJustification() {
		createDates();
		createType();
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setPresenter(Presenter p) {
		this.p = p;
	}

	@Override
	public void clear() {
		clearData();
		clearTypes();
		clearGeneralJustificationDateList();
	}

	@Override
	public void clearData() {
		date.setValue(new Date());
		dateList.clear();
		dates.clear();
		clearNotes();
	}
	
	@Override
	public void clearGeneralJustificationDateList() {
		generalsJustificationDate.clear();
	}
	
	@Override
	public void clearNotes(){
		justifyNotes.setText("");
	}

	private void clearTypes() {
		types.setValue(null);
		types.setAcceptableValues(new ArrayList<Justification>());
	}	
	
	@Override
	public String getNotes() {
		return justifyNotes.getText();
	}
	
	@Override 
	public void setGeneralJustificationDateList (List<GeneralJustificationDate> justifications) {
		generalsJustificationDate.setGeneralsJustificationDate(justifications);
	}

	////  types ////
	
	@Override
	public void setTypes(List<Justification> types) {
		if (types.size() > 0) {
			Justification t = types.get(0);
			this.types.setValue(t);
			if (typeSelection != null) {
				typeSelection.setSelected(t,true);
			}
		}
		this.types.setAcceptableValues(types);		
	}
	
	@Override
	public void setTypesSelectionModel(SingleSelectionModel<Justification> selection) {
		typeSelection = selection;
	}

	@Override
	public List<GeneralJustificationDate> getDates() {
		return dateList;
	}	
	
	@SuppressWarnings("deprecation")
	@UiHandler("add")
	public void onAddDate(ClickEvent event) {		
		Date date = this.date.getValue();		
		if (date == null) {
			return;
		}
		date.setHours(0);
		date.setMinutes(0);
		date.setSeconds(0);		
		Justification type = typeSelection.getSelectedObject();
		if(type == null){
			return;
		}
		
		// chequeo que no exista esa fecha dentor de la tabla.
		// antes de crear el objeto.
		for (GeneralJustificationDate jd : dateList) {
			if (jd.getStart().equals(date)) {
				return;
			}
		}
		
		GeneralJustificationDate gjd = provider.getNew();
		
		Date start = new Date(date.getTime());
		start.setHours(0);
		start.setMinutes(0);
		start.setSeconds(0);
		
		Date end = new Date(date.getTime());
		end.setHours(23);
		end.setMinutes(59);
		end.setSeconds(59);
		
		gjd.setStart(start);
		gjd.setEnd(end);
		gjd.setNotes(this.getNotes());
		gjd.setJustification(type);
		
		dateList.add(gjd);
		clearNotes();
		
		dates.setGeneralsJustificationDate(dateList);		
	}
	
	@UiHandler("commit")
	public void onCommit(ClickEvent event){
		if(p == null){
			return;
		}
		p.persist();
	}
	
	@UiHandler("resset")
	public void onResset(ClickEvent event) {
		this.clearData();
	}
	
	@UiHandler("search")
	public void onSearch(ClickEvent event) {
		p.search(getStart(),getEnd());
	}
	
	@UiHandler("delete")
	public void onDelete(ClickEvent event) {
		p.delete();
	}

	@Override
	public void setProvider(GeneralJustificationDateProvider p) {
		this.provider = p;
	}

	@Override
	public Date getStart() {
		return start.getValue();
	}

	@Override
	public Date getEnd() {
		return end.getValue();
	}

	@Override
	public void setGeneralJustificationSelection (MultiSelectionModel<GeneralJustificationDate> generalJustificationSelection) {
		generalsJustificationDate.setSelectionModel(generalJustificationSelection);
	}
}
