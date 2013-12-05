package ar.com.dcsys.gwt.autoBeans.shared;

import java.util.ArrayList;
import java.util.List;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;


public class AutoBeanUtils {

	
	/**
	 * Generico para hacer wrapping a una lista para que funcione la serializacion de autobeans.
	 * @param clazz
	 * @param l
	 * @return
	 */
	public static <T> List<T> wrapList(AutoBeanFactory factory, Class<T> clazz, List<T> l) {
		List<T> r = new ArrayList<T>(l.size());
		for (T t : l) {
			if (com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(t) != null) {
				r.add(t);
			} else {
				r.add(factory.create(clazz).as());
			}
		}
		return r;
	}
	
	/**
	 * Codifcación genérica de una clase a String usando AutoBeanCodex
	 * @param clazz
	 * @param t
	 * @return
	 */
	public <T> String encode(AutoBeanFactory factory, Class<T> clazz, T t) {
		AutoBean<T> bean = com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean(t);
		if (bean == null) {
			// no tiene bean asi que hay que crearlo.
			bean = factory.create(clazz,t);
		}
		String json = AutoBeanCodex.encode(bean).getPayload();
		return json;
	}
	
	/**
	 * Decodificiacion genérica de una clase desde un String usando AutoBeanCodex.
	 * @param clazz
	 * @param json
	 * @return
	 */
	public <T> T decode(AutoBeanFactory factory, Class<T> clazz, String json) {
		AutoBean<T> bean = AutoBeanCodex.decode(factory, clazz, json);
		T t = bean.as();
		return t;
	}		
	
	
}
