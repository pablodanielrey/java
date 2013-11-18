package ar.com.dcsys.persistence;

import java.sql.Connection;
import java.sql.SQLException;

public interface JdbcConnectionProvider {

	public Connection getConnection() throws SQLException;
	public void closeConnection(Connection con) throws SQLException;


}
