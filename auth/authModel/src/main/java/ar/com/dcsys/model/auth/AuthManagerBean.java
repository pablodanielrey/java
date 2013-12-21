package ar.com.dcsys.model.auth;

import java.security.Principal;
import java.util.List;

import javax.inject.Inject;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;

import ar.com.dcsys.data.auth.AuthHandler;
import ar.com.dcsys.data.auth.AuthHandlersDetection;
import ar.com.dcsys.exceptions.AuthenticationException;

public class AuthManagerBean implements AuthManager {

	private final List<AuthHandler> handlers;
	
	@Inject
	public AuthManagerBean(AuthHandlersDetection handlersDetection) {
		handlers = handlersDetection.detectAuthHandlers();
	}
	
	@Override
	public AuthenticationInfo autentificate(AuthenticationToken token) throws AuthenticationException {
		AuthenticationException e = null;
		
		for (AuthHandler h : handlers) {
			try {
				if (h.handles(token)) {
					return h.autenticate(token);
				}
			} catch (AuthenticationException ex) {
				e = ex;
			}
		}

		if (e == null) {
			e = new AuthenticationException("No se pudo encontrar un handler de autentificación para manejar ese tipo de token");
		}
		throw e;
	}
	
	@Override
	public boolean hasPermission(String perm) throws AuthenticationException {
		Subject subject = SecurityUtils.getSubject();
		boolean ok = subject.isPermitted(perm);
		return ok;
	}
	
	@Override
	public boolean isAuthenticated() throws AuthenticationException {
		Subject subject = SecurityUtils.getSubject();
		boolean ok = subject.isAuthenticated();
		return ok;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Principal> getPrincipals() throws AuthenticationException {
		Subject subject = SecurityUtils.getSubject();
		PrincipalCollection pc = subject.getPrincipals();
		return pc.asList();
	}
	
}
