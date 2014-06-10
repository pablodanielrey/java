/**
 * Clase que realiza la conexion entre CDI y las clases creadas por shiro.
 * es para poder obtener la config por cdi dentro de la clase que persiste las sesiones.
 */

package ar.com.dcsys.auth.shiro;

import javax.inject.Inject;

import ar.com.dcsys.config.Config;

public class DatabaseData {

	@Inject @Config String server;
	@Inject @Config String port;
	@Inject @Config String database;
	@Inject @Config String user;
	@Inject @Config String password;
	
	public String getServer() {
		return server;
	}
	
	public String getPort() {
		return port;
	}
	
	public String getDatabase() {
		return database;
	}
	
	public String getUser() {
		return user;
	}
	
	public String getPassword() {
		return password;
	}
	
}
