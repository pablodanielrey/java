package ar.com.dcsys.gwt.assistance.shared;


import java.util.List;

import ar.com.dcsys.data.justification.Justification;
import ar.com.dcsys.data.justification.JustificationDate;
import ar.com.dcsys.data.justification.GeneralJustificationDate;

import javax.inject.Inject;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;

import ar.com.dcsys.gwt.assistance.shared.lists.GeneralJustificationDateList;
import ar.com.dcsys.gwt.assistance.shared.lists.JustificationDateList;
import ar.com.dcsys.gwt.assistance.shared.lists.JustificationList;
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
	
	public List<GeneralJustificationDate> decodeGeneralJustificationDateList(String list) {
		AutoBean<GeneralJustificationDateList> bean = AutoBeanCodex.decode(assistanceFactory, GeneralJustificationDateList.class, list);
		GeneralJustificationDateList l = bean.as();
		List<GeneralJustificationDate> values = l.getList();
		return values;
	}
	
	
	
}
