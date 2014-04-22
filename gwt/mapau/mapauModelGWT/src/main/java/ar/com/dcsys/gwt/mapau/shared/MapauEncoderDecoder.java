package ar.com.dcsys.gwt.mapau.shared;

import java.util.List;

import javax.inject.Inject;

import ar.com.dcsys.data.appointment.Appointment;
import ar.com.dcsys.data.appointment.AppointmentV2;
import ar.com.dcsys.data.filter.TransferFilter;
import ar.com.dcsys.data.filter.TransferFilterType;
import ar.com.dcsys.data.reserve.Reserve;
import ar.com.dcsys.data.reserve.ReserveAttemptDate;
import ar.com.dcsys.data.reserve.ReserveAttemptDateType;
import ar.com.dcsys.data.silabouse.Area;
import ar.com.dcsys.gwt.autoBeans.shared.AutoBeanUtils;
import ar.com.dcsys.gwt.manager.client.ManagerUtils;
import ar.com.dcsys.gwt.mapau.shared.list.AppointmentList;
import ar.com.dcsys.gwt.mapau.shared.list.AppointmentV2List;
import ar.com.dcsys.gwt.mapau.shared.list.AreaList;
import ar.com.dcsys.gwt.mapau.shared.list.ReserveAttemptList;
import ar.com.dcsys.gwt.mapau.shared.list.ReserveAttemptTypeList;
import ar.com.dcsys.gwt.mapau.shared.list.ReserveList;
import ar.com.dcsys.gwt.mapau.shared.list.TransferFilterList;
import ar.com.dcsys.gwt.mapau.shared.list.TransferFilterTypeList;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;

public class MapauEncoderDecoder {

	private final MapauFactory mapauFactory;
	
	@Inject
	public MapauEncoderDecoder(MapauFactory mapauFactory) {
		this.mapauFactory = mapauFactory;
	}
	
	
	
	
	
	public List<AppointmentV2> decodeAppointmentV2List(String list) {
		AppointmentV2List l = ManagerUtils.decode(mapauFactory, AppointmentV2List.class, list);
		List<AppointmentV2> values = l.getList();
		return values;
	}
	
	public String encodeAppointmentV2List(List<AppointmentV2> list) {
		AutoBean<AppointmentV2List> al = mapauFactory.appointmentV2List();
		AppointmentV2List l = al.as();
		
		List<AppointmentV2> cl = AutoBeanUtils.wrapList(mapauFactory, AppointmentV2.class, list);
		l.setList(cl);
		
		String json = AutoBeanCodex.encode(al).getPayload();
		return json;
	}		
	
	
	
	
	
	
	
	
	
	public List<Appointment> decodeAppointmentList(String list) {
		AppointmentList l = ManagerUtils.decode(mapauFactory, AppointmentList.class, list);
		List<Appointment> values = l.getList();
		return values;
	}
	
	public String encodeAppointmentList(List<Appointment> list) {
		AutoBean<AppointmentList> al = mapauFactory.appointmentList();
		AppointmentList l = al.as();
		
		List<Appointment> cl = AutoBeanUtils.wrapList(mapauFactory, Appointment.class, list);
		l.setList(cl);
		
		String json = AutoBeanCodex.encode(al).getPayload();
		return json;
	}			
	
	
	
	
	
	
	
	public List<TransferFilterType> decodeTransferFilterTypeList(String list) {
		TransferFilterTypeList l = ManagerUtils.decode(mapauFactory, TransferFilterTypeList.class, list);
		List<TransferFilterType> values = l.getList();
		return values;
	}
	
	public String encodeTransferFilterTypeList(List<TransferFilterType> list) {
		AutoBean<TransferFilterTypeList> al = mapauFactory.transferFilterTypeList();
		TransferFilterTypeList l = al.as();
		
		List<TransferFilterType> cl = AutoBeanUtils.wrapList(mapauFactory, TransferFilterType.class, list);
		l.setList(cl);
		
		String json = AutoBeanCodex.encode(al).getPayload();
		return json;
	}		
	

	
	
	
	
	public List<TransferFilter> decodeTransferFilterList(String list) {
		TransferFilterList l = ManagerUtils.decode(mapauFactory, TransferFilterList.class, list);
		List<TransferFilter> values = l.getList();
		return values;
	}
	
	public String encodeTransferFilterList(List<TransferFilter> list) {
		AutoBean<TransferFilterList> al = mapauFactory.transferFilterList();
		TransferFilterList l = al.as();
		
		List<TransferFilter> cl = AutoBeanUtils.wrapList(mapauFactory, TransferFilter.class, list);
		l.setList(cl);
		
		String json = AutoBeanCodex.encode(al).getPayload();
		return json;
	}			
	

	
	
	
	public List<ReserveAttemptDate> decodeReserveAttemptDateList(String list) {
		ReserveAttemptList l = ManagerUtils.decode(mapauFactory, ReserveAttemptList.class, list);
		List<ReserveAttemptDate> values = l.getList();
		return values;
	}
	
	public String encodeReserveAttemptDateList(List<ReserveAttemptDate> list) {
		AutoBean<ReserveAttemptList> al = mapauFactory.reserveAttemptDateList();
		ReserveAttemptList l = al.as();
		
		List<ReserveAttemptDate> cl = AutoBeanUtils.wrapList(mapauFactory, ReserveAttemptDate.class, list);
		l.setList(cl);
		
		String json = AutoBeanCodex.encode(al).getPayload();
		return json;
	}
	
	
	
	
	
	
	
	public List<ReserveAttemptDateType> decodeReserveAttemptDateTypeList(String list) {
		ReserveAttemptTypeList l = ManagerUtils.decode(mapauFactory, ReserveAttemptTypeList.class, list);
		List<ReserveAttemptDateType> values = l.getList();
		return values;
	}
	
	public String encodeReserveAttemptDateTypeList(List<ReserveAttemptDateType> list) {
		AutoBean<ReserveAttemptTypeList> al = mapauFactory.reserveAttemptDateTypeList();
		ReserveAttemptTypeList l = al.as();
		
		List<ReserveAttemptDateType> cl = AutoBeanUtils.wrapList(mapauFactory, ReserveAttemptDateType.class, list);
		l.setList(cl);
		
		String json = AutoBeanCodex.encode(al).getPayload();
		return json;
	}
	
	
	
	

	public List<Reserve> decodeReserveList(String list) {
		ReserveList l = ManagerUtils.decode(mapauFactory, ReserveList.class, list);
		List<Reserve> values = l.getList();
		return values;
	}
	
	public String encodeReserveList(List<Reserve> list) {
		AutoBean<ReserveList> al = mapauFactory.reserveList();
		ReserveList l = al.as();
		
		List<Reserve> cl = AutoBeanUtils.wrapList(mapauFactory, Reserve.class, list);
		l.setList(cl);
		
		String json = AutoBeanCodex.encode(al).getPayload();
		return json;
	}	
	
	
	
	
	
	
	
	
	
	
	
	public List<Area> decodeAreaList(String list) {
		AreaList l = ManagerUtils.decode(mapauFactory, AreaList.class, list);
		List<Area> values = l.getList();
		return values;
	}
	
	public String encodeAreaList(List<Area> list) {
		AutoBean<AreaList> al = mapauFactory.areaList();
		AreaList l = al.as();
		
		List<Area> cl = AutoBeanUtils.wrapList(mapauFactory, Area.class, list);
		l.setList(cl);
		
		String json = AutoBeanCodex.encode(al).getPayload();
		return json;
	}	
	
	
	
}
