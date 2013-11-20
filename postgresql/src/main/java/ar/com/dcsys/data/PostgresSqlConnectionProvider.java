package ar.com.dcsys.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.inject.Inject;
import javax.inject.Named;

import ar.com.dcsys.persistence.JdbcConnectionProvider;
import ar.com.dcsys.persistence.PersistenceData;

@Named
public class PostgresSqlConnectionProvider implements JdbcConnectionProvider {

	private final PersistenceData data;
	
	@Inject
	public PostgresSqlConnectionProvider(PersistenceData data) {
		this.data = data;
	}
	
	@Override
	public Connection getConnection() throws SQLException {
		try {
			
			StringBuilder sb = new StringBuilder();
			sb.append("jdbc:postgresql://").append(data.getServer()).append(":").append(data.getPort()).append("/").append(data.getDatabase());
			String url = sb.toString();
			
			String user = data.getUserName();
			String password = data.getPassword();

			Class.forName("org.postgresql.Driver");
			return DriverManager.getConnection(url, user,password);					
			
		} catch (ClassNotFoundException e) {
			throw new SQLException(e);
		}
	}
	
	
	@Override
	public void closeConnection(Connection con) throws SQLException {
		con.close();
	}

}
