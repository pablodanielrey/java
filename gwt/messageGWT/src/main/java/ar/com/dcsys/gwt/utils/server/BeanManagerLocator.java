package ar.com.dcsys.gwt.utils.server;

import javax.enterprise.inject.spi.BeanManager;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class BeanManagerLocator {

	public static BeanManager getBeanManager() throws NamingException {

		/*
		 * lo comento porque no funca en glassfish. en jetty con weld si.
		BeanManager bm = null;
		
		CDI<Object> current = CDI.current();
		if (current != null) {
			bm = current.getBeanManager();
			if (bm != null) {
				return bm;
			}
		}
		*/
		
		
		BeanManager bm = InitialContext.doLookup("java:comp/BeanManager");
		return bm;
	}
	
}
