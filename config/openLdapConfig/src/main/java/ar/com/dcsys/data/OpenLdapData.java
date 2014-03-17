package ar.com.dcsys.data;

import javax.inject.Named;

import ar.com.dcsys.persistence.PersistenceData;

@Named("ldap")
public class OpenLdapData implements PersistenceData {

	private static final String userName = "cn=admin,dc=econo";
	private static final String password = "algo";
	private static final String host = "127.0.0.1";
	private static final String port = "389";
	
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
		return port;
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
