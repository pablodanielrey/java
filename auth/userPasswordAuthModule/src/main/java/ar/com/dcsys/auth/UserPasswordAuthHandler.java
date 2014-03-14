package ar.com.dcsys.auth;

import java.security.Principal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;

import ar.com.dcsys.data.auth.AbstractAuthHandler;
import ar.com.dcsys.data.auth.principals.IdPrincipal;
import ar.com.dcsys.data.auth.principals.UserNamePrincipal;
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
						"userName varchar not null," +
						"principal varchar not null," +
						"principalName varchar not null" +
						")");
				try {
					st.execute();
					
				} finally {
					st.close();
				}

				st = con.prepareStatement("create table if not exists user_password_auth_error_log (" +
						"date timestamp not null," +
						"host varchar, " +
						"userName varchar not null," +
						"password varchar not null" +
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
	
	private void logSuccess(Connection con, String host, String username, Principal p) throws SQLException {
		
		PreparedStatement st = con.prepareStatement("insert into user_password_auth_log (date, host, username, principal, principalName) values (?,?,?,?,?)");
		try {
			st.setTimestamp(1, new Timestamp((new Date()).getTime()));
			st.setString(2,host);
			st.setString(3,username);
			st.setString(4,p.getClass().getName());
			st.setString(5,p.getName());
			st.execute();
			
		} finally {
			st.close();
		}
		
	}
	
	private void logError(Connection con, String host, String username, String password) throws SQLException {
		
		PreparedStatement st = con.prepareStatement("insert into user_password_auth_error_log (date, host, username, password) values (?,?,?,?)");
		try {
			st.setTimestamp(1, new Timestamp((new Date()).getTime()));
			st.setString(2,host);
			st.setString(3,username);
			st.setString(4,password);
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
				String host = upt.getHost();
				String username = upt.getUsername();

				try {
					AuthenticationInfo info = authenticate(con, upt);
					
					Principal p = (Principal)info.getPrincipals().getPrimaryPrincipal();
					logSuccess(con,host,username,p);
					
					return info;

				} catch (AuthenticationException e) {
					char[] password = upt.getPassword();
					logError(con, host, username, new String(password));
					throw e;
				}
				
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
	
	
	@Override
	public List<Principal> findAllPrincipals(IdPrincipal id) throws AuthenticationException {
		try {
			Connection con = cp.getConnection();
			try {
				String query = "select userName from user_password_auth where person_id = ?";
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1,id.getName());
					ResultSet rs = st.executeQuery();
					try {
						if (rs.next()) {
							String userName = rs.getString("userName");
							Principal unp = new UserNamePrincipal(userName);
							return Arrays.asList(unp);
							
						} else {
							throw new AuthenticationException("user not found");
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
		} catch (SQLException e) {
			throw new AuthenticationException(e.getMessage());
		}
	}
	
	
	@Override
	public String findIdByPrincipal(Principal p) throws AuthenticationException {
				
		if (!(p instanceof UserNamePrincipal)) {
			throw new AuthenticationException("Principal not supported");
		}
		
		try {
			Connection con = cp.getConnection();
			try {
				String query = "select person_id from user_password_auth where userName = ?";
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1,p.getName());
					ResultSet rs = st.executeQuery();
					try {
						if (rs.next()) {
							String id = rs.getString("person_id");
							return id;
							
						} else {
							throw new AuthenticationException("user not found");
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
		} catch (SQLException e) {
			throw new AuthenticationException(e.getMessage());
		}
	}
	
}
