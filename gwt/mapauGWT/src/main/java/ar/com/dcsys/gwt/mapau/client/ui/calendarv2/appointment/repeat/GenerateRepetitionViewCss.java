package ar.com.dcsys.gwt.mapau.client.ui.calendarv2.appointment.repeat;

import com.google.gwt.resources.client.CssResource;

public interface GenerateRepetitionViewCss extends CssResource {

	
	String mainContainer();
	
	String title();
	
	String repeatTypePanel();
	String repeatTypeLabel();
	String repeatType();
	
	String repeatedEveryPanel();
	String repeatedEveryLabel();
	String repeatedEvery();
	String repeatedEveryDescription();
	
	String repeatOnPanel();
	String repeatOnPanelHidden();
	String repeatOnLabel();
	String repeatOnCheck();
	String repeatOnMonthRadio();
	
	String startRepetitionPanel();
	String startRepetitionLabel();
	String startRepetition();
	
	String endRepetitionPanel();
	String endRepetitionLabel();
	String endRepetitionRadio();
	
	String endRepetitionCountPanel();
	String endRepetitionCountValue();
	String endRepetitionCountDescription();
	
	String endRepetitionDatePanel();
	String endRepetitionDateValue();
	
	String actionsPanel();
	String cancel();
	String separator();
	String commit();
}
