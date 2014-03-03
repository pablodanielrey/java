package ar.com.dcsys.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;

import ar.com.dcsys.mail.MailData;
import ar.com.dcsys.persistence.JdbcConnectionProvider;

public class SqlMailConfig implements MailData {

	private static final Logger logger = Logger.getLogger(SqlMailConfig.class.getName());
	private final JdbcConnectionProvider cp;
	
	private String[] administrativeAccounts;
	private String[] assistanceAdministrativeAccounts;
	private String from;
	private String server;
	private String password;
	private String user;
	
	@Inject
	public SqlMailConfig(JdbcConnectionProvider cp) {
		this.cp = cp;
		try {
			createTables();
			loadConfig();
		} catch(SQLException e) {
			logger.log(Level.SEVERE,e.getMessage(),e);
		}
	}
	
	
	private final void createTables() throws SQLException {
		Connection con = cp.getConnection();
		try {
			PreparedStatement st = con.prepareStatement("create table if not exists mailConfig (admin_accounts varchar not null,"
									+ "assistance_admin_accounts varchar not null,"
									+ "ffrom varchar not null,"
									+ "server varchar not null,"
									+ "ppassword varchar not null,"
									+ "uuser varchar not null)");
			try {
				st.executeUpdate();

			} finally {
				st.close();
			}
		} finally {
			cp.closeConnection(con);
		}
	}
	
	/**
	 * Carga los valores de la base en las variables locales de instancia.
	 * @param rs
	 * @throws SQLException
	 */
	private final void loadConfigData(ResultSet rs) throws SQLException {
		String ac = rs.getString("admin_accounts");
		administrativeAccounts = ac.split(",");
		
		ac = rs.getString("assistance_admin_accounts");
		assistanceAdministrativeAccounts = ac.split(",");
		
		from = rs.getString("ffrom");
		server = rs.getString("server");
		password = rs.getString("ppassword");
		user = rs.getString("uuser");
	}
	
	private void loadConfig() throws SQLException {
		Connection con = cp.getConnection();
		try {
			PreparedStatement st = con.prepareStatement("select * from mailConfig");
			try {
				ResultSet rs = st.executeQuery();
				try {
					if (rs.next()) {
						loadConfigData(rs);
					}
				} finally {
					rs.close();
				}
			} finally {
				st.close();
			}
		} finally {
			cp.closeConnection(con);
		}
	}
	
	
	@Override
	public String[] administrativeAccount() {
		return administrativeAccounts;
	}
	
	@Override
	public String[] assistanceAdministrativeAccount() {
		return assistanceAdministrativeAccounts;
	}
	
	@Override
	public String from() {
		return from;
	}
	
	@Override
	public String server() {
		return server;
	}
	
	@Override
	public String serverPassword() {
		return password;
	}
	
	@Override
	public String serverUser() {
		return user;
	}
	
	
	
}
