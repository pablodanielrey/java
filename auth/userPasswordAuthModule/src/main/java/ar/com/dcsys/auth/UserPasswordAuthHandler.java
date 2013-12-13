package ar.com.dcsys.auth;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;

import ar.com.dcsys.data.auth.AbstractAuthHandler;
import ar.com.dcsys.exceptions.AuthenticationException;

public class UserPasswordAuthHandler extends AbstractAuthHandler {

	@Override
	public AuthenticationInfo autenticate(AuthenticationToken token) throws AuthenticationException {
		
		// autentifico todo lo que venga. falta armar bien el DAO y chequear contra eso.
		
		AuthenticationInfo info = new SimpleAuthenticationInfo(token.getPrincipal(), token.getCredentials(), this.getClass().getName());
		
		return info;
	}
	
	@Override
	public boolean handles(AuthenticationToken token) {
		
		return (token instanceof UsernamePasswordToken);
		
	}
	
}
