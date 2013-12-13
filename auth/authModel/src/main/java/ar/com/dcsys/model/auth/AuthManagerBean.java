package ar.com.dcsys.model.auth;

import java.util.List;

import javax.inject.Inject;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;

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
	
}