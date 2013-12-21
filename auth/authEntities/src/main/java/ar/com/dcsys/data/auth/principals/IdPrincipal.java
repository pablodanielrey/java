package ar.com.dcsys.data.auth.principals;

import java.security.Principal;

public class IdPrincipal implements Principal {

	private final String name;
	
	public IdPrincipal(String n) {
		this.name = n;
	}
	
	@Override
	public String getName() {
		return name;
	}

	
	
}
