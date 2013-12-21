package ar.com.dcsys.auth.shiro;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.Principal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SimpleSession;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;

public class PostgreSqlDCSysSessionDAO implements SessionDAO {

	private static final Logger logger = Logger.getLogger(PostgreSqlDCSysSessionDAO.class.getName());
	
	private static final String db = "jdbc:postgresql://localhost/dcsys";
	private static final String user = "dcsys";
	private static final String pass = "dcsys";
	
	private boolean tablesCreated = false;
	
	public PostgreSqlDCSysSessionDAO() {
		
		createTables();
		
	}
	
	private void createTables() {
		
		if (tablesCreated) {
			return;
		}
		
		try {
			Connection con = getConnection();
			try {
				PreparedStatement st = con.prepareStatement("create table if not exists sessions (" +
						"id varchar not null primary key," +
						"created timestamp not null," +
						"lastAccess timestamp not null," +
						"stoped timestamp," +
						"timeout bigint, " +
						"expired boolean," +
						
						// estos campos se sacan de la session si es que fue guardado el subject. en caso contrario estan en null. ///
						
						"authenticated boolean," +
						"principal varchar," +
						
						//////
					
						"host varchar," +
						"session bytea not null" +
						")");
				try {
					st.execute();
					tablesCreated = true;
					
				} finally {
					st.close();
				}
				
			} finally {
				con.close();
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage(),e);
		}

	}
	
	private Connection getConnection() throws SQLException {
		return DriverManager.getConnection(db,user,pass);
	}
	
	private byte[] serializeSession(SimpleSession ss) throws IOException {
		Map data = ss.getAttributes();
		
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		ObjectOutputStream oo = new ObjectOutputStream(bo);
		oo.writeObject(data);
		oo.flush();
		return bo.toByteArray();
	}
	
	private Map deserializeSession(byte[] data) throws IOException, ClassNotFoundException {
		ByteArrayInputStream bin = new ByteArrayInputStream(data);
		ObjectInputStream oin = new ObjectInputStream(bin);
		Map s = (Map)oin.readObject();
		return s;
	}
	
	
	private void setSession(PreparedStatement st, Session ses) throws SQLException, IOException {
		
		SimpleSession session = (SimpleSession)ses;
		
		st.setString(1,session.getId().toString());	
		
		Date date = session.getStartTimestamp();
		st.setTimestamp(2, new Timestamp(date.getTime()));
		
		date = session.getLastAccessTime();
		st.setTimestamp(3, new Timestamp(date.getTime()));

		date = session.getStopTimestamp();
		if (date == null) {
			st.setTimestamp(4,null);
		} else {
			st.setTimestamp(4, new Timestamp(date.getTime()));
		}
		
		st.setLong(5, session.getTimeout());
		st.setBoolean(6, session.isExpired());
		st.setString(7,session.getHost());
		
		
		/////////// en el caso de que este habilitado el subject dentro de la session entonces obtengo esos datos (solo informativo para ver las tablas mas facil) ///////////////////
		
		/// definido en DefaultSubjectDAO
		Object oa = session.getAttribute(DefaultSubjectContext.AUTHENTICATED_SESSION_KEY);
		if (oa != null) {
			Boolean authenticated = (Boolean)oa;
			st.setBoolean(8,authenticated);
		} else {
			st.setBoolean(8, false);
		}
		
		// definido el DefaultSubjectDAO
		Object ps = session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
		if (ps != null) {
			PrincipalCollection pc = (PrincipalCollection)ps;
			String name = (String)pc.getPrimaryPrincipal();
			st.setString(9,name);
		} else {
			st.setString(9,"nulo");
		}

		/////////////////////////////////////////////////////////////////////////
		
		byte[] s = serializeSession(session);
		st.setBytes(10, s);

	}
	
	
	private Session getSession(ResultSet rs) throws SQLException, ClassNotFoundException, IOException {
		SimpleSession session = new SimpleSession();
		
		byte[] s = rs.getBytes("session");
		Map m = deserializeSession(s);
		session.setAttributes(m);

		Date accessed = new Date(rs.getTimestamp("lastAccess").getTime());
		session.setLastAccessTime(accessed);
		
		Date created = new Date(rs.getTimestamp("created").getTime());
		session.setStartTimestamp(created);
		
		Timestamp tstop = rs.getTimestamp("stoped");
		if (tstop != null) {
			Date stop = new Date(tstop.getTime());
			session.setStopTimestamp(stop);
		}
		
		Long timeout = rs.getLong("timeout");
		session.setTimeout(timeout);
		
		boolean expired = rs.getBoolean("expired");
		session.setExpired(expired);
		
		String host = rs.getString("host");
		session.setHost(host);
		
		session.setId(rs.getString("id"));
		
		return session;
	}	
	
	@Override
	public Serializable create(Session session) {
		try {
			String id = UUID.randomUUID().toString();
			((SimpleSession)session).setId(id);					// como lo hace en otras partes de shiro.
			
			Connection con = getConnection();
			try {
				PreparedStatement st = con.prepareStatement("insert into sessions (id,created,lastAccess,stoped,timeout,expired,host,authenticated,principal,session) values (?,?,?,?,?,?,?,?,?,?)");
				try {
					setSession(st, session);
					st.executeUpdate();
					
					return id;
					
				} finally {
					st.close();
				}
				
			} finally {
				con.close();
			}
		
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	

	
	@Override
	public Session readSession(Serializable sessionId) throws UnknownSessionException {
		try {
			Connection con = getConnection();
			try {
				PreparedStatement st = con.prepareStatement("select * from sessions where id = ?");
				try {
					st.setString(1,(String)sessionId);
					ResultSet rs = st.executeQuery();
					try {

						if (!rs.next()) {
							throw new UnknownSessionException();
						}
						
						Session session = getSession(rs);
						if (!(session.getId().equals(sessionId))) {
							throw new RuntimeException("los ids no son iguales");
						}
						
						return session;
						
					} finally {
						rs.close();
					}
					
				} finally {
					st.close();
				}
				
			} finally {
				con.close();
			}
		
		} catch (UnknownSessionException e) {
			throw e;
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void update(Session session) throws UnknownSessionException {
		try {
			String id = (String)session.getId();
			
			
			Connection con = getConnection();
			try {
				PreparedStatement st = con.prepareStatement("update sessions set id = ?, created = ?, lastAccess = ?, stoped = ?, timeout = ?, expired = ?, host = ?, authenticated = ?, principal = ?, session = ? where id = ?");
				try {
					setSession(st, session);
					st.setString(11,id);
					if (st.executeUpdate() != 1) {
						throw new UnknownSessionException();
					}
					
				} finally {
					st.close();
				}
				
			} finally {
				con.close();
			}
		
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void delete(Session session) {
		try {
			String id = (String)session.getId();
			
			Connection con = getConnection();
			try {
				PreparedStatement st = con.prepareStatement("delete from sessions where id = ?");
				try {
					st.setString(1,id);
					st.executeUpdate();
					
				} finally {
					st.close();
				}
				
			} finally {
				con.close();
			}
		
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Collection<Session> getActiveSessions() {
		try {
			Connection con = getConnection();
			try {
				PreparedStatement st = con.prepareStatement(" select * from sessions s where s.lastAccess < ? and s.stoped is null");
				try {
					st.setTimestamp(1,new Timestamp((new Date()).getTime()));
					ResultSet rs = st.executeQuery();
					try {
						List<Session> sessions = new ArrayList<>();
						while (rs.next()) {
							Session s = getSession(rs);
							sessions.add(s);
						}
						return sessions;
						
					} finally {
						rs.close();
					}
					
				} finally {
					st.close();
				}
				
			} finally {
				con.close();
			}
		
		} catch (UnknownSessionException e) {
			throw e;
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
