package ar.com.dcsys.auth.shiro;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
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

public class HsqlDCSysSessionDAO implements SessionDAO {

	private static final Logger logger = Logger.getLogger(HsqlDCSysSessionDAO.class.getName());
	
	private static final String pathdb = System.getProperty("java.io.tmpdir") + "/shiro-sessions.db";
	private static final String db = "jdbc:hsqldb:file:" + pathdb;
//	private static final String db = "jdbc:hsqldb:hsql://localhost/";
	private static final String user = "SA";
	private static final String pass = "";

	
	private boolean tablesCreated = false;
	
	public HsqlDCSysSessionDAO() {
		
		createTables();
		
	}
	
	private void createTables() {
		
		if (tablesCreated) {
			return;
		}
		
		try {
			Class.forName("org.hsqldb.jdbc.JDBCDriver" );
			Connection con = getConnection();
			try {
				PreparedStatement st = con.prepareStatement("create table if not exists sessions (" +
						"id longvarchar not null primary key," +
						"created timestamp not null," +
						"lastAccess timestamp not null," +
						"session varbinary(524288000) not null" +			// 500MB
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
	
	private boolean isSerializable(Object v) {
		return (v instanceof Serializable);
	}
	
	
	private byte[] serializeSession(Session s) throws IOException {
		SimpleSession ss = (SimpleSession)s;
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
	
	@Override
	public Serializable create(Session session) {
		try {
			String id = UUID.randomUUID().toString();
			((SimpleSession)session).setId(id);					// como lo hace en otras partes de shiro.
			byte[] s = serializeSession(session);
			
			Connection con = getConnection();
			try {
				PreparedStatement st = con.prepareStatement("insert into sessions (id,created,lastAccess,session) values (?,?,?,?)");
				try {
					st.setString(1,id);
					st.setTimestamp(2, new Timestamp(session.getStartTimestamp().getTime()));
					st.setTimestamp(3, new Timestamp(session.getLastAccessTime().getTime()));
					st.setBytes(4, s);
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

	
	private void setSession(PreparedStatement st, Session session) throws SQLException, IOException {
		
		byte[] s = serializeSession(session);
		
		st.setString(1,session.getId().toString());
		st.setTimestamp(2, new Timestamp(session.getStartTimestamp().getTime()));
		st.setTimestamp(3, new Timestamp(session.getLastAccessTime().getTime()));
		st.setBytes(4, s);
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
		
		session.setId(rs.getString("id"));
		
		return session;
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
				PreparedStatement st = con.prepareStatement("update sessions set id = ?, created = ?, lastAccess = ?, session = ? where id = ?");
				try {
					setSession(st, session);
					st.setString(5,id);
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
				PreparedStatement st = con.prepareStatement("select * from sessions");
				try {
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
