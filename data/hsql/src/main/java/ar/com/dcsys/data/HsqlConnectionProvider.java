package ar.com.dcsys.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.inject.Inject;

import ar.com.dcsys.config.Config;
import ar.com.dcsys.persistence.JdbcConnectionProvider;

public class HsqlConnectionProvider implements JdbcConnectionProvider {

	@Inject @Config String database;	// target/testdb
	@Inject @Config String user;		// "SA"
	@Inject @Config String password;	// ""
	
	
	@Override
	public Connection getConnection() throws SQLException {
		try {
			
			StringBuilder sb = new StringBuilder();
			sb.append("jdbc:hsqldb:file:").append(database);
			String url = sb.toString();
			
			Class.forName("org.hsqldb.jdbc.JDBCDriver" );
			Connection c = DriverManager.getConnection(url, user, password);
			return c;
			
		} catch (ClassNotFoundException e) {
			throw new SQLException(e);
		}
	}
	
	@Override
	public void closeConnection(Connection con) throws SQLException {
		con.close();
	}

}
