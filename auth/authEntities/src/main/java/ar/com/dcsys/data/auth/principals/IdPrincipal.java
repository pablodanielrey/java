package ar.com.dcsys.data.auth.principals;

import java.io.Serializable;
import java.security.Principal;

public class IdPrincipal implements Principal, Serializable {

	private static final long serialVersionUID = 1L;

	private final String name;
	
	public IdPrincipal(String n) {
		this.name = n;
	}
	
	@Override
	public String getName() {
		return name;
	}

	
	
}
