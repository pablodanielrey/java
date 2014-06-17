package ar.com.dcsys.gwt.assistance.client.gin;

import ar.com.dcsys.gwt.assistance.client.activity.AssistanceActivityMapper;
import ar.com.dcsys.gwt.assistance.client.ui.auth.PinAuthData;
import ar.com.dcsys.gwt.assistance.client.ui.auth.PinAuthDataView;
import ar.com.dcsys.gwt.assistance.client.ui.justification.general.GeneralsJustification;
import ar.com.dcsys.gwt.assistance.client.ui.justification.general.GeneralsJustificationView;
import ar.com.dcsys.gwt.assistance.client.ui.justification.manage.ManageJustifications;
import ar.com.dcsys.gwt.assistance.client.ui.justification.manage.ManageJustificationsView;
import ar.com.dcsys.gwt.assistance.client.ui.justification.person.JustificationPerson;
import ar.com.dcsys.gwt.assistance.client.ui.justification.person.JustificationPersonView;
import ar.com.dcsys.gwt.assistance.client.ui.period.Periods;
import ar.com.dcsys.gwt.assistance.client.ui.period.PeriodsView;
import ar.com.dcsys.gwt.assistance.client.ui.period.daily.DailyPeriods;
import ar.com.dcsys.gwt.assistance.client.ui.period.daily.DailyPeriodsView;
import ar.com.dcsys.gwt.assistance.client.ui.period.person.PeriodsAssignationPerson;
import ar.com.dcsys.gwt.assistance.client.ui.period.person.PeriodsAssignationPersonView;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.inject.client.assistedinject.GinFactoryModuleBuilder;
import com.google.inject.Singleton;

public class AssistanceGWTGinModule extends AbstractGinModule {

	@Override
	protected void configure() {

		bind(EventBus.class).to(SimpleEventBus.class).in(Singleton.class);
		
		bind(PinAuthDataView.class).to(PinAuthData.class).in(Singleton.class);
		bind(DailyPeriodsView.class).to(DailyPeriods.class).in(Singleton.class);
		bind(GeneralsJustificationView.class).to(GeneralsJustification.class).in(Singleton.class);
		bind(JustificationPersonView.class).to(JustificationPerson.class).in(Singleton.class);
		bind(ManageJustificationsView.class).to(ManageJustifications.class).in(Singleton.class);
		bind(PeriodsAssignationPersonView.class).to(PeriodsAssignationPerson.class).in(Singleton.class);
		bind(PeriodsView.class).to(Periods.class).in(Singleton.class);
		
		bind(AssistanceActivityMapper.class).in(Singleton.class);
		
		GinFactoryModuleBuilder builder = new GinFactoryModuleBuilder();
		install(builder.build(AssistedInjectionFactory.class));
	}

}
