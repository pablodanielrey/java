package ar.com.dcsys.gwt.mapau.shared;

import java.util.List;

import javax.inject.Inject;

import ar.com.dcsys.data.appointment.Appointment;
import ar.com.dcsys.data.appointment.AppointmentV2;
import ar.com.dcsys.data.filter.TransferFilter;
import ar.com.dcsys.data.filter.TransferFilterType;
import ar.com.dcsys.gwt.autoBeans.shared.AutoBeanUtils;
import ar.com.dcsys.gwt.manager.shared.ManagerUtils;
import ar.com.dcsys.gwt.mapau.shared.list.AppointmentList;
import ar.com.dcsys.gwt.mapau.shared.list.AppointmentV2List;
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
	
}
