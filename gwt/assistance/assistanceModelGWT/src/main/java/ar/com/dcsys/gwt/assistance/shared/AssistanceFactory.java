package ar.com.dcsys.gwt.assistance.shared;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;
import ar.com.dcsys.gwt.person.shared.PersonValueProxy;
import ar.com.dcsys.gwt.person.shared.lists.PersonValueList;
import ar.com.dcsys.gwt.person.shared.lists.PersonList;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.gwt.assistance.shared.lists.JustificationList;
import ar.com.dcsys.gwt.assistance.shared.lists.JustificationDateList;
import ar.com.dcsys.gwt.assistance.shared.lists.GeneralJustificationDateList;

import ar.com.dcsys.data.justification.GeneralJustificationDate;
import ar.com.dcsys.data.justification.Justification;
import ar.com.dcsys.data.justification.JustificationDate;

public interface AssistanceFactory extends AutoBeanFactory {
	public AutoBean<PersonValueProxy> personValue();
	public AutoBean<PersonValueList> personValueList();
	public AutoBean<Person> person();
	public AutoBean<PersonList> personList();
	
	public AutoBean<Justification> justification();
	public AutoBean<JustificationList> justificationList();
	
	public AutoBean<JustificationDate> justificationDate();
	public AutoBean<JustificationDateList> justificationDateList();
	
	public AutoBean<GeneralJustificationDate> generalJustificationDate();
	public AutoBean<GeneralJustificationDateList> generalJustificationDateList();
	

}
