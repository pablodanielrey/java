package ar.com.dcsys.model.auth;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;

import ar.com.dcsys.exceptions.AuthenticationException;


public interface AuthManager {

	public AuthenticationInfo autentificate(AuthenticationToken token) throws AuthenticationException;
	
	public boolean isAuthenticated() throws AuthenticationException;
	public boolean hasPermission(String perm) throws AuthenticationException;
	
}
