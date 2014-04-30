package ar.com.dcsys.gwt.assistance.shared;


import java.util.List;

import ar.com.dcsys.data.justification.Justification;
import ar.com.dcsys.data.justification.JustificationDate;
import ar.com.dcsys.data.justification.GeneralJustificationDate;
import ar.com.dcsys.data.period.PeriodAssignation;
import ar.com.dcsys.data.period.PeriodType;

import javax.inject.Inject;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;

import ar.com.dcsys.gwt.assistance.shared.lists.GeneralJustificationDateList;
import ar.com.dcsys.gwt.assistance.shared.lists.JustificationDateList;
import ar.com.dcsys.gwt.assistance.shared.lists.JustificationList;
import ar.com.dcsys.gwt.assistance.shared.lists.PeriodAssignationList;
import ar.com.dcsys.gwt.assistance.shared.lists.PeriodTypeList;
import ar.com.dcsys.gwt.autoBeans.shared.AutoBeanUtils;

public class AssistanceEncoderDecoder {

	private final AssistanceFactory assistanceFactory;
	
	@Inject
	public AssistanceEncoderDecoder(AssistanceFactory assistanceFactory) {
		this.assistanceFactory = assistanceFactory;
	}
	
	public String encodeJustificationList(List<Justification> pt) {
		AutoBean<JustificationList> aptl = assistanceFactory.justificationList();
		JustificationList ptl = aptl.as();
		
		List<Justification> wpt = AutoBeanUtils.wrapList(assistanceFactory, Justification.class, pt);
		ptl.setList(wpt);
		
		String json = AutoBeanCodex.encode(aptl).getPayload();
		return json;
	}
	
	public List<Justification> decodeJustificationList(String list) {
		AutoBean<JustificationList> bean = AutoBeanCodex.decode(assistanceFactory, JustificationList.class, list);
		JustificationList l = bean.as();
		List<Justification> values = l.getList();
		return values;
	}
	
	public String encodeJustificationDateList(List<JustificationDate> pt) {
		AutoBean<JustificationDateList> aptl = assistanceFactory.justificationDateList();
		JustificationDateList ptl = aptl.as();
		
		List<JustificationDate> wpt = AutoBeanUtils.wrapList(assistanceFactory, JustificationDate.class, pt);
		ptl.setList(wpt);
		
		String json = AutoBeanCodex.encode(aptl).getPayload();
		return json;
	}
	
	public List<JustificationDate> decodeJustificationDateList(String list) {
		AutoBean<JustificationDateList> bean = AutoBeanCodex.decode(assistanceFactory, JustificationDateList.class, list);
		JustificationDateList l = bean.as();
		List<JustificationDate> values = l.getList();
		return values;
	}
	
	public String encodeGeneralJustificationDateList(List<GeneralJustificationDate> pt) {
		AutoBean<GeneralJustificationDateList> aptl = assistanceFactory.generalJustificationDateList();
		GeneralJustificationDateList ptl = aptl.as();
		
		List<GeneralJustificationDate> wpt = AutoBeanUtils.wrapList(assistanceFactory, GeneralJustificationDate.class, pt);
		ptl.setList(wpt);
		
		String json = AutoBeanCodex.encode(aptl).getPayload();
		return json;
	}
	
	public List<PeriodAssignation> decodePeriodAssignationList(String list) {
		AutoBean<PeriodAssignationList> bean = AutoBeanCodex.decode(assistanceFactory, PeriodAssignationList.class, list);
		PeriodAssignationList l = bean.as();
		List<PeriodAssignation> values = l.getList();
		return values;
	}
	
	public String encodePeriodAssignationList(List<PeriodAssignation> pt) {
		AutoBean<PeriodAssignationList> aptl = assistanceFactory.periodAssignationList();
		PeriodAssignationList ptl = aptl.as();
		
		List<PeriodAssignation> wpt = AutoBeanUtils.wrapList(assistanceFactory, PeriodAssignation.class, pt);
		ptl.setList(wpt);
		
		String json = AutoBeanCodex.encode(aptl).getPayload();
		return json;
	}
	
	public List<GeneralJustificationDate> decodeGeneralJustificationDateList(String list) {
		AutoBean<GeneralJustificationDateList> bean = AutoBeanCodex.decode(assistanceFactory, GeneralJustificationDateList.class, list);
		GeneralJustificationDateList l = bean.as();
		List<GeneralJustificationDate> values = l.getList();
		return values;
	}
	
	
	public String encodePeriodTypeList(List<PeriodType> pt) {
		AutoBean<PeriodTypeList> aptl = assistanceFactory.periodTypeList();
		PeriodTypeList ptl = aptl.as();

		// no es necesario hacerle wrapping porque los elementos son enumerativos.
		ptl.setList(pt);
		
		String json = AutoBeanCodex.encode(aptl).getPayload();
		return json;
	}
	
	public List<PeriodType> decodePeriodTypeList(String list) {
		AutoBean<PeriodTypeList> bean = AutoBeanCodex.decode(assistanceFactory, PeriodTypeList.class, list);
		PeriodTypeList l = bean.as();
		List<PeriodType> values = l.getList();
		return values;
	}	
	
	
	
}
