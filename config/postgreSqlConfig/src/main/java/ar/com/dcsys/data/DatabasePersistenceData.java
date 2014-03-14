package ar.com.dcsys.data;

import javax.inject.Named;

import ar.com.dcsys.persistence.PersistenceData;

@Named("sql")
public class DatabasePersistenceData implements PersistenceData {

	private static final String userName = "dcsys";
	private static final String password = "dcsys";
	private static final String database = "dcsys";	
	private static final String host = "127.0.0.1";
	private static final String port = "5432";
	
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
		return database;
	}

	@Override
	public String getUsersBase() {
		return null;
	}
	
}
