package ar.com.dcsys.data;

import ar.com.dcsys.persistence.PersistenceData;

public class OpenLdapData implements PersistenceData {

	private static final String userName = "dcsys";
	private static final String password = "dcsys";
	private static final String host = "127.0.0.1";
	
	@Override
	public String getUserName() {
		return userName;
	}
	
	@Override
	public String getPassword() {
		return password;
	}
	
	@Override
	public String getServer() {
		return host;
	}
	
	@Override
	public String getPort() {
		return null;
	}
	
	@Override
	public String getDatabase() {
		return null;
	}

	@Override
	public String getUsersBase() {
		return "ou=people,dc=econo";
	}
	
}
