package ar.com.dcsys.gwt.utils.server;

import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class BeanManagerLocator {

	public static BeanManager getBeanManager() throws NamingException {
		
		BeanManager bm = CDI.current().getBeanManager();
		if (bm != null) {
			return bm;
		}
		
		bm = InitialContext.doLookup("java:comp/BeanManager");
		return bm;
	}
	
}
