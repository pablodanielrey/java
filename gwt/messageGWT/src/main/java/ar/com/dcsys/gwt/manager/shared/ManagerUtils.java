package ar.com.dcsys.gwt.manager.shared;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import ar.com.dcsys.gwt.autoBeans.shared.AutoBeanUtils;
import ar.com.dcsys.gwt.manager.shared.list.DateList;
import ar.com.dcsys.gwt.manager.shared.primitive.BooleanContainer;
import ar.com.dcsys.gwt.utils.client.EncoderDecoder;

import com.google.gwt.core.shared.GWT;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;
import com.sksamuel.gwt.websockets.Base64Utils;

public class ManagerUtils {

	
	/**
	 * Codifcación genérica de una clase a String usando AutoBeanCodex
	 * @param clazz
	 * @param t
	 * @return
	 */
	public static <T> String encode(AutoBeanFactory factory, Class<T> clazz, T t) {
		return AutoBeanUtils.encode(factory, clazz, t);
	}
	
	/**
	 * Decodificiacion genérica de una clase desde un String usando AutoBeanCodex.
	 * @param clazz
	 * @param json
	 * @return
	 */
	public static <T> T decode(AutoBeanFactory factory, Class<T> clazz, String json) {
		return AutoBeanUtils.decode(factory, clazz, json);
	}			
	
	
	/**
	 * Codifica varios strings pasados por parametro a un solo string.
	 * @param params
	 * @return
	 */
	public static String encodeParams(String ... params) {
		StringBuilder sb = new StringBuilder();
		for (String p : params) {
			if (GWT.isClient()) {
				sb.append(EncoderDecoder.b64encode(p));
			} else {
				sb.append(Base64Utils.toBase64(p.getBytes()));
			}
			sb.append("--!!--");
		}
		sb.delete(sb.length() - 1, sb.length());
		return sb.toString();
	}
	
	/**
	 * Decodifica la cadena de parametros a varios strings individuales. uno por cada parametro.
	 * @param params
	 * @return
	 */
	public static List<String> decodeParams(String params) {
		String[] par = params.split("--!!--");
		List<String> r = new ArrayList<String>();
		for (String p : par) {
			String decoded = "";
			if (GWT.isClient()) {
				decoded = EncoderDecoder.b64decode(p);
			} else {
				decoded = new String(Base64Utils.fromBase64(p));
			}
			r.add(decoded);
		}
		return r;
	}	
	
	
	private final ManagerFactory managerFactory;
	
	@Inject
	public ManagerUtils(ManagerFactory managerFactory) {
		this.managerFactory = managerFactory;
	}
	
	/**
	 * Codifica un booleano a un string.
	 * @param managerFactory
	 * @param b
	 * @return
	 */
	public String encodeBoolean(Boolean b) {
		BooleanContainer bc = managerFactory.booleanContainer().as();
		bc.setBoolean(b);
		String r = AutoBeanUtils.encode(managerFactory, BooleanContainer.class, bc);
		return r;
	}
	
	/**
	 * Decodifica un boolean de un string.
	 * @param managerFactory
	 * @param b
	 * @return
	 */
	public Boolean decodeBoolean(String b) {
		BooleanContainer bc = AutoBeanUtils.decode(managerFactory, BooleanContainer.class, b);
		return bc.getBoolean();
	}
	
	
	/**
	 * Decodifica una lista de fechas desde un string.
	 * @param managerFactory
	 * @param list
	 * @return
	 */
	public List<Date> decodeDateList(String list) {
		AutoBean<DateList> bean = AutoBeanCodex.decode(managerFactory, DateList.class, list);
		DateList l = bean.as();
		List<Date> values = l.getList();
		return values;
	}
	
	/**
	 * Codifica una lista de fechas a un string.
	 * @param managerFactory
	 * @param list
	 * @return
	 */
	public String encodeDateList(List<Date> list) {
		AutoBean<DateList> al = managerFactory.dateList();
		DateList l = al.as();
		
		List<Date> cl = AutoBeanUtils.wrapList(managerFactory, Date.class, list);
		l.setList(cl);
		
		String json = AutoBeanCodex.encode(al).getPayload();
		return json;
	}	
	
	
	
	
}
