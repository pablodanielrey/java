package ar.com.dcsys.model.reports;

import java.lang.reflect.Method;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

public class ListBeanDataSource<T> implements JRDataSource {

	
	private final List<T> beans;
	private int index = -1;
	
	public ListBeanDataSource(List<T> beans) {
		this.beans = beans;
	}
	
	public boolean next() throws JRException {
		index++;
		return (index < beans.size() && beans.get(index) != null);
	};

	@Override
	public Object getFieldValue(JRField jrField) throws JRException {
		
		String name = jrField.getName();
		if (!(name.startsWith("get") || name.startsWith("is"))) {
			throw new JRException("el nombre del método no es getter");
		}
		
		try {
			T o = beans.get(index);
			Method m = o.getClass().getMethod(name);
			Object r = m.invoke(o);
			return r;
		
		} catch (Exception e) {
			throw new JRException(e);
		}
	}

}
