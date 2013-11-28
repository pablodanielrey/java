package ar.com.dcsys.data;

import ar.com.dcsys.persistence.PersistenceData;

public class DatabasePersistenceData implements PersistenceData {

	private static final String userName = "SA";
	private static final String password = "";
	private static final String database = "target/testdb";	
	
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
		return null;
	}
	
	@Override
	public String getPort() {
		return null;
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
