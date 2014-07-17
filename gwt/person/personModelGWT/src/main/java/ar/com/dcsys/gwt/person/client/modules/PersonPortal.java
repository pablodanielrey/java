package ar.com.dcsys.gwt.person.client.modules;

import java.util.ArrayList;
import java.util.List;

public class PersonPortal implements PersonModulesPortal {

	private static PersonPortal portal = null;
	
	public static final PersonModulesPortal getPortal() {
		if (portal == null) {
			portal = new PersonPortal();
		}
		return portal;
	}
	
	
	private final List<PersonModule> modules = new ArrayList<PersonModule>();
	
	@Override
	public void add(PersonModule module) {
		modules.add(module);
	}

	@Override
	public List<PersonModule> getModules() {
		return modules;
	}

}
