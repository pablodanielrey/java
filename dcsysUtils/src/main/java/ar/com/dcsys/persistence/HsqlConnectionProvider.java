package ar.com.dcsys.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.inject.Named;

@Named
public class HsqlConnectionProvider implements JdbcConnectionProvider {

	@Override
	public Connection getConnection() throws SQLException {
		try {
			Class.forName("org.hsqldb.jdbc.JDBCDriver" );
			Connection c = DriverManager.getConnection("jdbc:hsqldb:file:testdb", "SA", "");
			return c;
		} catch (ClassNotFoundException e) {
			throw new SQLException(e);
		}
	}

}
