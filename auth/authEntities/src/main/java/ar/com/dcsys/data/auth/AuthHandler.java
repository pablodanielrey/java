package ar.com.dcsys.data.auth;


import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;

import ar.com.dcsys.exceptions.AuthenticationException;


public interface AuthHandler {
	
	/**
	 * Chequea a ver si puede manjear y autentificar el tipo de token pasado como parámetro.
	 * @param token, token para chequear por autentificación.
	 * @return true en el caso de que pueda manejar ese tipo de tokens, false en el caso de que no lo maneje.
	 */
	public boolean handles(AuthenticationToken token);
	
	/**
	 * Trata de autentificar dado un determinado token al usuario y retoran la información de autentificación.
	 * @param token, token a usar para autentificar al usaurio.
	 * @return información de autentificación del usuario, en el caso de que haya sido exitosa la autentificacion.
	 * @throws AuthenticationException, falla de autentificación, o autentificacion incorrecta.
	 */
	public AuthenticationInfo autenticate(AuthenticationToken token) throws AuthenticationException;

}
