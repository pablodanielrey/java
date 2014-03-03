package ar.com.dcsys.workarrounds.guava;

import java.util.Set;

import javax.enterprise.inject.Produces;

import com.google.common.collect.ImmutableSet;
import com.google.common.util.concurrent.Service;

public class GuavaCDIWorkarround {

		/**
		 * Soluciona el problema de cdi de guava en versiones inferiores a la 16.0
		 * como gwt en versoniones inferiores a la 2.6.0 usa guava 15, hay que solucionarlo usando este workarround en las aplicaciones
		 * gwt.
		 * @return
		 */
		@Produces
		Set<Service> dummyService() {
			return ImmutableSet.of();
		}
	
}
