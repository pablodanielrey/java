package ar.com.dcsys.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.inject.Inject;

import ar.com.dcsys.config.Config;
import ar.com.dcsys.persistence.JdbcConnectionProvider;


public class PostgresSqlConnectionProvider implements JdbcConnectionProvider {

	@Inject @Config String server;
	@Inject @Config String port;
	@Inject @Config String database;
	@Inject @Config String user;
	@Inject @Config String password;
	
	@Override
	public synchronized Connection getConnection() throws SQLException {
		try {
			
			StringBuilder sb = new StringBuilder();
			sb.append("jdbc:postgresql://").append(server).append(":").append(port).append("/").append(database);
			String url = sb.toString();
			
			Class.forName("org.postgresql.Driver");
			return DriverManager.getConnection(url, user,password);					
			
		} catch (ClassNotFoundException e) {
			throw new SQLException(e);
		}
	}
	
	
	@Override
	public synchronized void closeConnection(Connection con) throws SQLException {
		con.close();
	}

}
