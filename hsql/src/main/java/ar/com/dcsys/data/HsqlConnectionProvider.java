package ar.com.dcsys.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.inject.Inject;

import ar.com.dcsys.persistence.JdbcConnectionProvider;
import ar.com.dcsys.persistence.PersistenceData;

public class HsqlConnectionProvider implements JdbcConnectionProvider {

	private final PersistenceData data;
	
	@Inject
	public HsqlConnectionProvider(PersistenceData data) {
		this.data = data;
	}
	
	@Override
	public Connection getConnection() throws SQLException {
		try {
			
			StringBuilder sb = new StringBuilder();
			sb.append("jdbc:hsqldb:file:").append(data.getDatabase());
			String url = sb.toString();
			String user = data.getUserName();
			String pass = data.getPassword();
			
			Class.forName("org.hsqldb.jdbc.JDBCDriver" );
			Connection c = DriverManager.getConnection(url, user, pass);
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
