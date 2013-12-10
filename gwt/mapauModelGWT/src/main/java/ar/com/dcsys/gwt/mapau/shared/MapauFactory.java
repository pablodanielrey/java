package ar.com.dcsys.gwt.mapau.shared;

import ar.com.dcsys.data.appointment.AppointmentV2;
import ar.com.dcsys.data.filter.TransferFilter;
import ar.com.dcsys.data.filter.TransferFilterType;
import ar.com.dcsys.gwt.mapau.shared.list.AppointmentList;
import ar.com.dcsys.gwt.mapau.shared.list.AppointmentV2List;
import ar.com.dcsys.gwt.mapau.shared.list.CourseList;
import ar.com.dcsys.gwt.mapau.shared.list.TransferFilterList;
import ar.com.dcsys.gwt.mapau.shared.list.TransferFilterTypeList;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;

public interface MapauFactory extends AutoBeanFactory {

	public AutoBean<AppointmentV2> appointmentV2();
	public AutoBean<TransferFilter> transferFilter();
	public AutoBean<TransferFilterType> transferFilterType();
	
	public AutoBean<AppointmentV2List> appointmentV2List();
	public AutoBean<AppointmentList> appointmentList();
	public AutoBean<TransferFilterTypeList> transferFilterTypeList();
	public AutoBean<TransferFilterList> transferFilterList();
	
}
