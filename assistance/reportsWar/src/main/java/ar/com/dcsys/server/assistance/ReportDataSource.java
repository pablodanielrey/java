package ar.com.dcsys.server.assistance;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

public class ReportDataSource implements JRDataSource {

	private final ReportSummary rs;
	private final List<Report> rss;
	private int index = -1;
	
	public ReportDataSource(ReportSummary rs) {
		this.rs = rs;
		this.rss = rs.getReports();
	}
	
	@Override
	public Object getFieldValue(JRField field) throws JRException {
		
		try {

			String methodName = field.getName();
			if (!methodName.contains(".")) {
				
				// actuo sobre el summary contenedor
				methodName = methodName.substring(0, methodName.indexOf("."));
				Method method = rs.getClass().getMethod(methodName + "Report");
				Object o = method.invoke(rs, null);
				return o;
				
			} else { 
				
				// actuo sobre los reportes
				methodName = methodName.substring(methodName.indexOf(".") + 1);
				
				Report r = rss.get(index);
				Method method = r.getClass().getMethod(methodName + "Report");
				Object o = method.invoke(r);
				return o;
			
			}
			
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException	| InvocationTargetException e) {
			e.printStackTrace();
			throw new JRException(e);
		}
		
	}
	
	public boolean next() throws JRException {
		index++;
		return (index < rss.size() && rss.get(index) != null);
	};
	
}