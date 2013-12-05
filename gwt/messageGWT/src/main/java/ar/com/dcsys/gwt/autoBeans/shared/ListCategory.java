package ar.com.dcsys.gwt.autoBeans.shared;

import java.util.ArrayList;
import java.util.List;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;

public class ListCategory {

	public static <T> T getElementAs(AutoBean<ListContainer<T>> list, Class<T> clazz, int index) {
		AutoBean<T> bean = AutoBeanCodex.decode(list.getFactory(), clazz, list.as().getElementAs().get(index));
		return bean.as();
	}

	public static <T> List<T> getElements(AutoBean<ListContainer<T>> list, Class<T> clazz) {
		int size = list.as().getList().size();
		List<T> rlist = new ArrayList<T>();
		for (int i = 0; i < size; i++) {
			AutoBean<T> bean = AutoBeanCodex.decode(list.getFactory(), clazz, list.as().getElementAs().get(i));
			T t = bean.as();
			rlist.add(t);
		}
		return rlist;
	}
	
	
}
