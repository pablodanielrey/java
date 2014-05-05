package ar.com.dcsys.gwt.mapau.client.ui.calendarv2.appointment.repeat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.GenerateRepetitionView;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.GenerateRepetitionView.DayOfWeek;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.GenerateRepetitionView.Presenter;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.GenerateRepetitionView.RepeatType;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DateBox.DefaultFormat;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.view.client.SingleSelectionModel;

public class GenerateRepetition extends Composite implements GenerateRepetitionView {

	private static RepeatAppointmentUiBinder uiBinder = GWT.create(RepeatAppointmentUiBinder.class);

	interface RepeatAppointmentUiBinder extends	UiBinder<Widget, GenerateRepetition> {
	}

	@UiField GenerateRepetitionViewResources res;
	
	@UiField Label title;
	@UiField(provided=true) ValueListBox<RepeatType> repeatType;
	@UiField(provided=true) ListBox repeatedEvery;
	@UiField Label repeatedEveryDescription;
	@UiField FlowPanel repeatOn;
	@UiField(provided=true) DateBox startRepetition;
	@UiField(provided=true) RadioButton endRepetitionCount;
	@UiField(provided=true) TextBox endRepetitionCountValue;
	@UiField(provided=true) RadioButton endRepetitionDate;
	@UiField(provided=true) DateBox endRepetitionDateValue;
	@UiField Label cancel;
	@UiField Label commit;
	
	private GenerateRepetitionView.Presenter presenter;
	
	
	private final DateTimeFormat dateF = DateTimeFormat.getFormat(PredefinedFormat.DATE_SHORT);

	private final Map<String, CheckBox> repeatOnDaysCheck = new HashMap<String, CheckBox>();
	
	/**
	 * Valor que representa, en la repeticion mensual, si se debe repetir el día del mes(false) o el día de la semana(true) 
	 */
	private boolean repeatOnMonthDayOfWeek = false;
	
	private SingleSelectionModel<RepeatType> repeatTypeSelection;
	
	/**
	 * Handler que mantiene actualizada la selección del ValueListBox con los cambios en el selectionModel.
	 */
	private final Handler repeatTypeHandler = new Handler() {
		@Override
		public void onSelectionChange(SelectionChangeEvent event) {
			if (repeatTypeSelection == null) {
				return;
			}
			RepeatType selected = repeatTypeSelection.getSelectedObject();
			if (selected == null) {
				repeatType.setValue(null);
			} else {
				repeatType.setValue(selected);
			}
		}
	}; 
	
	/**
	 * Handler que mantiene actualizado la selección del ValueListBox con el selectionModel
	 */
	private final ValueChangeHandler<RepeatType> repeatTypeValueHandler = new ValueChangeHandler<RepeatType>() {
		@Override
		public void onValueChange(ValueChangeEvent<RepeatType> event) {
			if (repeatTypeSelection != null) {
				RepeatType type = repeatType.getValue();
				repeatTypeSelection.setSelected(type, true);				
				
				switch (type) {
					case DAILY: dailySelection(); break;
					case WEEKLY: weeklySelection(); break;
					case MONTHLY: monthlySelection(); break;
				}
			}
		}
	};
	
	@Override
	public void setPresenter(Presenter p) {
		this.presenter = p;
	}
	
	public GenerateRepetition() {
		createRepeatType();
		createStartRepetition();
		createEndRepetition();
		createRepeatedEvery();
		
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	private void createRepeatedEvery() {
		repeatedEvery = new ListBox();
		for (int i=1; i<=10; i++) {
			repeatedEvery.addItem(String.valueOf(i));
		}
	}
	
	private void createRepeatType() {
		repeatType = new ValueListBox<RepeatType>(new Renderer<RepeatType>() {

			private String getValue(RepeatType type) {
				if (type == null) {
					return "Nulo";
				}
				return type.getDescription();
			}
			
			@Override
			public String render(RepeatType type) {
				return getValue(type);
			}

			@Override
			public void render(RepeatType type, Appendable appendable) throws IOException {
				if (type == null) {
					return;
				}
				appendable.append(getValue(type));
			}
		});
		
		repeatType.addValueChangeHandler(repeatTypeValueHandler);
		
	}
	
	private void createStartRepetition() {
		DatePicker dp = new DatePicker();		
		startRepetition = new DateBox(dp, new Date(), new DefaultFormat(dateF));	
	}
	
	private void createEndRepetition() {
						
		// Finalización cantidad repeticiones  
		String name = "endRepetition";
		String radioLabel="Después de:";
		endRepetitionCount = new RadioButton(name, radioLabel);
		endRepetitionCount.setValue(true);
		endRepetitionCount.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				boolean value = endRepetitionCount.getValue();
				endRepetitionCountValue.setEnabled(value);
				endRepetitionDateValue.setEnabled(!value);
			}
		});
		
		endRepetitionCountValue = new TextBox();
		endRepetitionCountValue.setEnabled(true);
		
		// Finalización fecha límite
		radioLabel = "El:";
		endRepetitionDate = new RadioButton(name, radioLabel);
		endRepetitionDate.setValue(false);
		endRepetitionDate.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				boolean value = endRepetitionDate.getValue();
				endRepetitionCountValue.setEnabled(!value);
				endRepetitionDateValue.setEnabled(value); 
						
			}
		});
		
		DatePicker dp = new DatePicker();
		endRepetitionDateValue = new DateBox(dp, new Date(), new DefaultFormat(dateF));
		endRepetitionDateValue.setEnabled(false);
	}
	
	
	
	//////////////////////////////////////////////////////////////////////////////////////////
	
	private void dailySelection() {
		
		repeatedEveryDescription.setText("día(s)");
		
		repeatOn.clear();
		repeatOn.setStyleName(res.style().repeatOnPanelHidden());
	}
	
	private void weeklySelection() {
		
		repeatedEveryDescription.setText("semana(s)");
		
		repeatOn.clear();
		repeatOnDaysCheck.clear();
		repeatOn.setStyleName(res.style().repeatOnPanel());

		Label repeatOnLabel = new Label();
		repeatOnLabel.setText("Repetir el:");
		repeatOnLabel.setStyleName(res.style().repeatOnLabel());
		repeatOn.add(repeatOnLabel);
		
		// creo los checkbox correspondiente a cada dia de la semana
		
		DayOfWeek[] daysOfWeek = DayOfWeek.values();
		for (DayOfWeek d : daysOfWeek) {
			CheckBox dayCheck = new CheckBox(d.getDescription());
			dayCheck.setStyleName(res.style().repeatOnCheck());
			dayCheck.setValue(false);			
			repeatOnDaysCheck.put(d.getDescription(), dayCheck);
			repeatOn.add(dayCheck);
		}
		
		
	}
	
	private void monthlySelection() {
		
		repeatedEveryDescription.setText("mes(es)");
		repeatOn.clear();
		repeatOnDaysCheck.clear();
		
		repeatOn.setStyleName(res.style().repeatOnPanel());

		Label repeatOnLabel = new Label();
		repeatOnLabel.setText("Repetir el:");
		repeatOnLabel.setStyleName(res.style().repeatOnLabel());
		repeatOn.add(repeatOnLabel);
		
		//creo las dos opciones posibles de repeticion del dia: dia del mes o dia de la semana

		String name = "repeatOnMonthRadio";
		RadioButton radioDayOfMonth = new RadioButton(name, "día del mes");
		radioDayOfMonth.setStyleName(res.style().repeatOnMonthRadio());
		radioDayOfMonth.setValue(true);
		radioDayOfMonth.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				repeatOnMonthDayOfWeek = false;
			}
		});
		repeatOn.add(radioDayOfMonth);
		
		RadioButton radioDayOfWeek = new RadioButton(name, "día de la semana");
		radioDayOfWeek.setStyleName(res.style().repeatOnMonthRadio());
		radioDayOfWeek.setValue(false);
		radioDayOfWeek.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				repeatOnMonthDayOfWeek = true;
			}
		});
		repeatOn.add(radioDayOfWeek);
	}
	
	@Override
	public void clear() {
		repeatOn.clear();
		repeatedEveryDescription.setText("");
	}
	
	@Override
	public void setRepeatTypeSelectionModel(SingleSelectionModel<RepeatType> selection) {
		this.repeatTypeSelection = selection;
		selection.addSelectionChangeHandler(repeatTypeHandler);		
	}
	
	@Override
	public void setRepeatTypes(List<RepeatType> types) {
		if (repeatTypeSelection != null) {
			RepeatType selected = repeatTypeSelection.getSelectedObject();
			this.repeatType.setValue(selected);
		}
		
		if (types == null) {
			types = new ArrayList<RepeatType>();
		} 
		
		this.repeatType.setAcceptableValues(types);
	}
	
	@UiHandler("commit")
	public void onCommit(ClickEvent event) {
		presenter.commit();
	}
	
	@UiHandler("cancel")
	public void onCancel(ClickEvent event) {
		presenter.cancel();
	}

	@Override
	public Integer getRepeatedEvery() {
		int index = repeatedEvery.getSelectedIndex();
		String value = repeatedEvery.getValue(index);
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	@Override
	public Date getStartRepetition() {
		return startRepetition.getValue();
	}

	@Override
	public void setStartRepetition(Date date) {
		startRepetition.setValue(date);
	}

	@Override
	public boolean getEndRepetitionCount() {
		return endRepetitionCount.getValue();
	}

	@Override
	public Integer getEndRepetitionCountValue() {
		String countStr = endRepetitionCountValue.getText();
		try {
			return Integer.parseInt(countStr);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	@Override
	public boolean getEndRepetitionDate() {
		return endRepetitionDate.getValue();
	}

	@Override
	public Date getEndRepetitionDateValue() {
		return endRepetitionDateValue.getValue();
	}
		
	@Override
	public List<DayOfWeek> getDaysOfWeek() {
		List<DayOfWeek> days = new ArrayList<DayOfWeek>();
		
		for (DayOfWeek d : DayOfWeek.values()) {
			CheckBox c = repeatOnDaysCheck.get(d.getDescription());
			if (c.getValue()) {
				days.add(d);
			}
		} 		
		
		return days;
	}

	@Override
	public boolean isDayOfMonth() {
		return !repeatOnMonthDayOfWeek;
	}	
	
	@Override
	public void setStartReadOnly(boolean v) {
		startRepetition.setEnabled(!v);
	}
	
	
}
