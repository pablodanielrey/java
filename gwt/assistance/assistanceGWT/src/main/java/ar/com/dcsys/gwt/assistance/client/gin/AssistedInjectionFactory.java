package ar.com.dcsys.gwt.assistance.client.gin;

import ar.com.dcsys.gwt.assistance.client.activity.auth.PinAuthDataActivity;
import ar.com.dcsys.gwt.assistance.client.activity.justification.GeneralsJustificationActivity;
import ar.com.dcsys.gwt.assistance.client.activity.justification.JustificationPersonActivity;
import ar.com.dcsys.gwt.assistance.client.activity.justification.ManageJustificationsActivity;
import ar.com.dcsys.gwt.assistance.client.activity.periods.DailyPeriodsActivity;
import ar.com.dcsys.gwt.assistance.client.activity.periods.PeriodsActivity;
import ar.com.dcsys.gwt.assistance.client.activity.periods.PeriodsAssignationPersonActivity;
import ar.com.dcsys.gwt.assistance.client.place.DailyPeriodsPlace;
import ar.com.dcsys.gwt.assistance.client.place.GeneralsJustificationPlace;
import ar.com.dcsys.gwt.assistance.client.place.JustificationPersonPlace;
import ar.com.dcsys.gwt.assistance.client.place.ManageJustificationPlace;
import ar.com.dcsys.gwt.assistance.client.place.PeriodsAssignationPersonPlace;
import ar.com.dcsys.gwt.assistance.client.place.PeriodsPlace;
import ar.com.dcsys.gwt.assistance.client.place.PinAuthDataPlace;



public interface AssistedInjectionFactory {
	public PinAuthDataActivity pinAuthDataActivity(PinAuthDataPlace place);
	public DailyPeriodsActivity dailyPeriodsActivity(DailyPeriodsPlace place);
	public GeneralsJustificationActivity generalsJustificationActivity(GeneralsJustificationPlace place);
	public JustificationPersonActivity justificationPersonActivity(JustificationPersonPlace place);
	public ManageJustificationsActivity manageJustificationsActivity(ManageJustificationPlace place);
	public PeriodsAssignationPersonActivity periodsAssignationPersonActivity(PeriodsAssignationPersonPlace place);
	public PeriodsActivity periodsActivity(PeriodsPlace place);
}
