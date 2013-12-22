package ar.com.dcsys.auth;

import java.security.Principal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;

import ar.com.dcsys.data.auth.AbstractAuthHandler;
import ar.com.dcsys.data.auth.principals.IdPrincipal;
import ar.com.dcsys.exceptions.AuthenticationException;
import ar.com.dcsys.persistence.JdbcConnectionProvider;

public class UserPasswordAuthHandler extends AbstractAuthHandler {

	private static final Logger logger = Logger.getLogger(UserPasswordAuthHandler.class.getName());
	private final JdbcConnectionProvider cp;
	private boolean tablesCreated = false;
	
	@Inject
	public UserPasswordAuthHandler(JdbcConnectionProvider cp) {
		this.cp = cp;
		createTables();
	}
	
	private void createTables() {
		try {
			
			Connection con = cp.getConnection();
			try {
				PreparedStatement st = con.prepareStatement("create table if not exists user_password_auth (" +
															"person_id varchar not null," +
															"userName varchar not null," +
															"password varchar not null" +
															")");
				try {
					st.execute();
					
				} finally {
					st.close();
				}

				st = con.prepareStatement("create table if not exists user_password_auth_log (" +
						"date timestamp not null," +
						"host varchar, " +
						"userName varchar not null" +
						")");
				try {
					st.execute();
					
				} finally {
					st.close();
				}
				
				tablesCreated = true;
				
			} finally {
				cp.closeConnection(con);
			}
			
		} catch (SQLException e) {
			logger.log(Level.SEVERE,e.getMessage(),e);
		}
	}
	
	private void log(Connection con, String host, String username) throws SQLException {
		
		PreparedStatement st = con.prepareStatement("insert into user_password_auth_log (date, host, username) values (?,?,?)");
		try {
			st.setTimestamp(1, new Timestamp((new Date()).getTime()));
			st.setString(2,host);
			st.setString(3,username);
			st.execute();
			
		} finally {
			st.close();
		}
		
	}
	
	private AuthenticationInfo authenticate(Connection con, UsernamePasswordToken token) throws SQLException, AuthenticationException {
		
		String username = token.getUsername();
		String password = new String(token.getPassword());
		
		PreparedStatement st = con.prepareStatement("select person_id from user_password_auth where username = ? and password = ?");
		try {
			st.setString(1,username);
			st.setString(2,password);
			ResultSet rs = st.executeQuery();
			try {
				if (!rs.next()) {
					throw new AuthenticationException("Error de autentificación");
				}
				
				String id = rs.getString("person_id");
				Principal p = new IdPrincipal(id);
						
				AuthenticationInfo info = new SimpleAuthenticationInfo(p, token.getPassword(), this.getClass().getName());
				return info;

			} finally {
				rs.close();
			}
			
		} finally {
			st.close();
		}
	}
	
	@Override
	public AuthenticationInfo autenticate(AuthenticationToken token) throws AuthenticationException {
		
		if (!tablesCreated) {
			throw new AuthenticationException("No existen las tablas en la base");
		}
		
		UsernamePasswordToken upt = (UsernamePasswordToken)token;

		try {
			Connection con = cp.getConnection();
			try {
				AuthenticationInfo info = authenticate(con, upt);
				
				String username = upt.getUsername();
				String host = upt.getHost();
				log(con,host,username);
				
				return info;
				
			} finally {
				con.close();
			}
		} catch (SQLException e) {
			throw new AuthenticationException(e.getMessage());
		}
	}
	
	@Override
	public boolean handles(AuthenticationToken token) {
		
		return (token instanceof UsernamePasswordToken);
		
	}
	
}
