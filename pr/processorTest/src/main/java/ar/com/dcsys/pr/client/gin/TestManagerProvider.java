package ar.com.dcsys.pr.client.gin;

import ar.com.dcsys.pr.shared.TestManager;

import com.google.gwt.core.client.GWT;
import com.google.inject.Provider;

public class TestManagerProvider implements Provider<TestManager> {

	private static TestManager tm = null;
	
	@Override
	public TestManager get() {
		if (tm == null) {
			tm = GWT.create(TestManager.class);
		}
		return tm;
	}
	
}
