package ar.com.dcsys.data.fingerprint;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import ar.com.dcsys.exceptions.FingerprintException;
import ar.com.dcsys.persistence.JdbcConnectionProvider;
import ar.com.dcsys.security.Finger;
import ar.com.dcsys.security.Fingerprint;

public class FingerprintPostgresSqlDAO implements FingerprintDAO {

	private static final Logger logger = Logger.getLogger(FingerprintPostgresSqlDAO.class.getName());
	private final JdbcConnectionProvider cp;
	
	@Inject
	public FingerprintPostgresSqlDAO(JdbcConnectionProvider cp) {
		this.cp = cp;
	}
	
	@PostConstruct
	void init() {
		try {
			Connection con = cp.getConnection();
			try {
				PreparedStatement st = con.prepareStatement("create table if not exists fingerprints ("
						+ "id varchar not null primary key,"
						+ "person_id varchar not null,"
						+ "codification varchar,"
						+ "template bytea not null,"
						+ "finger varchar,"
						+ "algorithm varchar,"
						+ "created timestamp not null default now())");
				try {
					st.execute();
					
				} finally {
					st.close();
				}
			} finally {
				cp.closeConnection(con);
			}
		} catch (SQLException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}
	
	@Override
	public String persist(Fingerprint fp) throws FingerprintException {
		try {
			Connection con = cp.getConnection();
			try {
				String query = "";
				String id = fp.getId();
				
				if (id == null) {
					id = UUID.randomUUID().toString();
					fp.setId(id);
					query = "insert into fingerprints (person_id, codification, template, finger, algorithm, id) values (?,?,?,?,?,?)";
				} else {
					query = "update fingerprints set person_id = ?, codification = ?, template = ?, finger = ?, algorithm = ?) where id = ?";
				}
				
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1, fp.getPersonId());
					st.setString(2, fp.getCodification());
					st.setBytes(3, fp.getTemplate());
					st.setString(4, fp.getFinger().name());
					st.setString(5, fp.getAlgorithm());
					st.setString(6, id);
					st.executeUpdate();
					return id;
					
				} finally {
					st.close();
				}
			} finally {
				cp.closeConnection(con);
			}
		} catch (SQLException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			throw new FingerprintException(e);
		}
	}

	@Override
	public List<String> findAll() throws FingerprintException {
		try {
			Connection con = cp.getConnection();
			try {
				String query = "select id from fingerprints";
				PreparedStatement st = con.prepareStatement(query);
				try {
					ResultSet rs = st.executeQuery();
					try {
						List<String> ids = new ArrayList<>();
						while (rs.next()) {
							String id = rs.getString("id");
							ids.add(id);
						}
						return ids;
						
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
			logger.log(Level.SEVERE, e.getMessage(), e);
			throw new FingerprintException(e);
		}
	}
	
	
	private Finger getFinger(ResultSet rs) throws SQLException {
		Finger f = Finger.valueOf(rs.getString("finger"));
		return f;
	}
	
	/**
	 * Obtiene un fingerprint desde un resultSet.
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private Fingerprint getFingerprint(ResultSet rs) throws SQLException {
		Fingerprint d = new Fingerprint();
		d.setId(rs.getString("id"));
		d.setPersonId(rs.getString("person_id"));
		d.setAlgorithm(rs.getString("algorithm"));
		d.setCodification(rs.getString("codification"));
		d.setFinger(getFinger(rs));
		d.setTemplate(rs.getBytes("template"));
		return d;
	}
	
	@Override
	public Fingerprint findById(String id) throws FingerprintException {
		try {
			Connection con = cp.getConnection();
			try {
				String query = "select * from fingerprints where id = ?";
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1,id);
					ResultSet rs = st.executeQuery();
					try {
						if (rs.next() == false) {
							return null;
						}
						Fingerprint fp = getFingerprint(rs);
						return fp;

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
			logger.log(Level.SEVERE, e.getMessage(), e);
			throw new FingerprintException(e);
		}
	}

	@Override
	public List<Fingerprint> findByPerson(String personId) throws FingerprintException {
		try {
			Connection con = cp.getConnection();
			try {
				String query = "select * from fingerprints where person_id = ?";
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1,personId);
					ResultSet rs = st.executeQuery();
					try {
						List<Fingerprint> fps = new ArrayList<Fingerprint>();
						while (rs.next()) {
							Fingerprint fp = getFingerprint(rs);
							fps.add(fp);
						}
						return fps;

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
			logger.log(Level.SEVERE, e.getMessage(), e);
			throw new FingerprintException(e);
		}
	}
	
	
}
