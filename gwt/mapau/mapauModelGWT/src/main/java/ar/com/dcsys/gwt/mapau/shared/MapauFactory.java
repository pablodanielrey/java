package ar.com.dcsys.gwt.mapau.shared;

import ar.com.dcsys.data.appointment.AppointmentV2;
import ar.com.dcsys.data.filter.TransferFilter;
import ar.com.dcsys.data.reserve.Reserve;
import ar.com.dcsys.data.reserve.ReserveAttemptDate;
import ar.com.dcsys.data.reserve.ReserveAttemptDateType;
import ar.com.dcsys.data.silabouse.Area;
import ar.com.dcsys.gwt.mapau.shared.list.AppointmentList;
import ar.com.dcsys.gwt.mapau.shared.list.AppointmentV2List;
import ar.com.dcsys.gwt.mapau.shared.list.AreaList;
import ar.com.dcsys.gwt.mapau.shared.list.ReserveAttemptList;
import ar.com.dcsys.gwt.mapau.shared.list.ReserveAttemptTypeList;
import ar.com.dcsys.gwt.mapau.shared.list.ReserveList;
import ar.com.dcsys.gwt.mapau.shared.list.TransferFilterList;
import ar.com.dcsys.gwt.mapau.shared.list.TransferFilterTypeList;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;

public interface MapauFactory extends AutoBeanFactory {

	public AutoBean<Area> area();
	public AutoBean<Reserve> reserve();
	public AutoBean<ReserveAttemptDate> reserveAttemptDate();
	public AutoBean<ReserveAttemptDateType> reserveAttemptDateType();
	public AutoBean<AppointmentV2> appointmentV2();
	public AutoBean<TransferFilter> transferFilter();
	
	public AutoBean<AreaList> areaList();
	public AutoBean<AppointmentV2List> appointmentV2List();
	public AutoBean<AppointmentList> appointmentList();
	public AutoBean<TransferFilterTypeList> transferFilterTypeList();
	public AutoBean<TransferFilterList> transferFilterList();
	public AutoBean<ReserveList> reserveList();
	public AutoBean<ReserveAttemptList> reserveAttemptDateList();
	public AutoBean<ReserveAttemptTypeList> reserveAttemptDateTypeList();
	
}
