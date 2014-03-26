package ar.com.dcsys.model.auth;

import java.security.Principal;
import java.util.List;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;

import ar.com.dcsys.data.auth.principals.IdPrincipal;
import ar.com.dcsys.exceptions.AuthenticationException;


public interface AuthManager {

	public AuthenticationInfo autentificate(AuthenticationToken token) throws AuthenticationException;
	
	public boolean isAuthenticated() throws AuthenticationException;
	public boolean hasPermission(String perm) throws AuthenticationException;
	
	public List<Principal> getPrincipals() throws AuthenticationException;
	
	public String findIdByPrincipal(Principal p) throws AuthenticationException;
	public List<Principal> findAllPrincipals(IdPrincipal p) throws AuthenticationException;
	
}
