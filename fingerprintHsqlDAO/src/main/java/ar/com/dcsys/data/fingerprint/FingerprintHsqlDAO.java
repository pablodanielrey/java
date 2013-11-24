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

import ar.com.dcsys.data.HsqlConnectionProvider;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.exceptions.FingerprintException;
import ar.com.dcsys.exceptions.PersonException;
import ar.com.dcsys.security.Finger;
import ar.com.dcsys.security.FingerprintCredentials;

public class FingerprintHsqlDAO implements FingerprintDAO {

	private final Logger logger = Logger.getLogger(FingerprintHsqlDAO.class.getName());
	private final HsqlConnectionProvider cp;
	private final Params params;
	
	@Inject
	public FingerprintHsqlDAO(HsqlConnectionProvider cp, Params params) {
		this.cp = cp;
		this.params = params;
	}

	@PostConstruct
	public void init() {
		try {
			createTables();
		} catch (SQLException e) {
			
		}
	}
	
	/**
	 * Crea las tablas dentro de la base.
	 */
	public void createTables() throws SQLException {
		Connection con = cp.getConnection();
		try {
			PreparedStatement st = con.prepareStatement("create table if not exists fingerprints (" +
					"id longvarchar not null primary key, " +
					"person_id longvarchar not null, " +
					"codification longvarchar not null, " +
					"algorithm longvarchar not null, " +
					"finger int not null, " +
					"template varbinary(1000) not null)");
			try {
				st.executeUpdate();
				
			} finally {
				st.close();
			}
		} finally {
			con.close();
		}
	}
	
	private Fingerprint getFingerPrint(ResultSet rs) throws SQLException, PersonException {

		
		String personId = rs.getString("person_id");
		Person person = params.findPersonById(personId);
		
		String id = rs.getString("id");
		
		String codification = rs.getString("codification");
		String algorithm = rs.getString("algorithm");
		byte[] template = rs.getBytes("template");
		int fingerId = rs.getInt("finger");
		Finger finger = Finger.getFinger(fingerId);

		FingerprintCredentials fpc = new FingerprintCredentials();
		fpc.setAlgorithm(algorithm);
		fpc.setCodification(codification);
		fpc.setFinger(finger);
		fpc.setTemplate(template);
		
		Fingerprint fp = new Fingerprint();
		fp.setId(id);
		fp.setPerson(person);
		fp.setFingerprint(fpc);
		
		return fp;
	}
	
	@Override
	public List<Fingerprint> findByPerson(Person person) throws FingerprintException, PersonException {
		try {
			Connection con = cp.getConnection();
			try {
				String query = "select * from fingerprints where person_id = ?";
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1,person.getId());
					ResultSet rs = st.executeQuery();
					try {
						List<Fingerprint> fingerprints = new ArrayList<>();
						while (rs.next()) {
							Fingerprint fp = getFingerPrint(rs);
							fingerprints.add(fp);
						}
						return fingerprints;
						
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
			throw new FingerprintException(e);
		}
	}
	
	
	@Override
	public String findIdByFingerprint(FingerprintCredentials fp) throws FingerprintException {
		try {
			Connection con = cp.getConnection();
			try {
				String query = "select id from fingerprints where codification = ? and finger = ? and algorithm = ? and template = ?";
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1,fp.getCodification());
					st.setInt(2,fp.getFinger().getId());
					st.setString(3,fp.getAlgorithm());
					st.setBytes(4, fp.getTemplate());
					
					ResultSet rs = st.executeQuery();
					try {
						if (rs.next()) {
							String id = rs.getString("id");
							return id;
						} else {
							return null;
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
			throw new FingerprintException(e);
		}
	}
	
	@Override
	public Fingerprint findByFingerprint(FingerprintCredentials fp) throws FingerprintException, PersonException {
		try {
			Connection con = cp.getConnection();
			try {
				String query = "select id, person_id from fingerprints where codification = ? and finger = ? and algorithm = ? and template = ?";
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1,fp.getCodification());
					st.setInt(2,fp.getFinger().getId());
					st.setString(3,fp.getAlgorithm());
					st.setBytes(4, fp.getTemplate());
					
					ResultSet rs = st.executeQuery();
					try {
						if (rs.next()) {
							String id = rs.getString("id");
							String personId = rs.getString("person_id");
							Person person = params.findPersonById(personId);
							
							Fingerprint fpn = new Fingerprint();
							fpn.setId(id);
							fpn.setPerson(person);
							fpn.setFingerprint(fp);
							
							return fpn;
						} else {
							return null;
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
			throw new FingerprintException(e);
		}
	}
	
	private String persist(Connection con, Fingerprint fp) throws SQLException {
		
		if (fp.getId() == null) {
			String id = UUID.randomUUID().toString();
			fp.setId(id);
		}
		
		String query = "insert into fingerprints (id,person_id,codification,finger,algorithm,template) values (?,?,?,?,?,?)";
		PreparedStatement st = con.prepareStatement(query);
		try {
			
			String id = fp.getId();
			String personId = fp.getPerson().getId();
			String codification = fp.getFingerprint().getCodification();
			int finger = fp.getFingerprint().getFinger().getId();
			String algorithm = fp.getFingerprint().getAlgorithm();
			byte[] templ = fp.getFingerprint().getTemplate();
			
			st.setString(1, id);
			st.setString(2, personId);
			st.setString(3, codification);
			st.setInt(4, finger);
			st.setString(5, algorithm);
			st.setBytes(6, templ);
			st.executeUpdate();
			
			return fp.getId();
			
		} finally {
			st.close();
		}
	}
	
	/**
	 * Actualiza las credenciales de del fingerprint dado como parámetro. 
	 * Se compara por el id del fingerprint.
	 * 
	 * @param con
	 * @param id
	 * @param fp
	 * @throws SQLException
	 */
	private void update(Connection con, Fingerprint fp) throws SQLException {
		String query = "update fingerprints set template = ?, codification = ?, finger = ?, algorithm = ? where id = ?";
		PreparedStatement st = con.prepareStatement(query);
		try {
			st.setBytes(1,fp.getFingerprint().getTemplate());
			st.setString(2, fp.getFingerprint().getCodification());
			st.setInt(3, fp.getFingerprint().getFinger().getId());
			st.setString(4, fp.getFingerprint().getAlgorithm());
			st.setString(5, fp.getId());
			st.executeUpdate();
		} finally {
			st.close();
		}
	}	

	
	@Override
	public String persist(Fingerprint fp) throws FingerprintException {
		try {
			Connection con = cp.getConnection();
			try {
				String id = fp.getId();
				if (id != null) {
					update(con,fp);
				} else {
					id = persist(con,fp);
				}
				return id;
				
			} finally {
				cp.closeConnection(con);
			}
		} catch (SQLException e) {
			logger.log(Level.SEVERE,e.getMessage(),e);
			throw new FingerprintException(e);
		}
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
						if (rs.next()) {
							Fingerprint fp = getFingerPrint(rs);
							return fp;
						} else {
							return null;
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
		} catch (SQLException | PersonException e) {
			throw new FingerprintException(e);
		}
	}
	
}
