package ar.com.dcsys.data.auth.principals;

import java.security.Principal;

public class DniPrincipal implements Principal {

	private final String name;
	
	public DniPrincipal(String n) {
		this.name = n;
	}
	
	@Override
	public String getName() {
		return name;
	}

	
	
}
